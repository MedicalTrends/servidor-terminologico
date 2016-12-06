package cl.minsal.semantikos.model.businessrules;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.User;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.List;

import static cl.minsal.semantikos.model.ProfileFactory.MODELER_PROFILE;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.isSnomedCTRelationship;

/**
 * Esta clase implementa las reglas de negocio de agregar relaciones.
 *
 * @author Andrés Farías on 9/8/16.
 */
@Singleton
public class RelationshipBindingBR implements RelationshipBindingBRInterface {

    @EJB
    private RelationshipManager relationshipManager;
    @EJB
    private ConceptManager conceptManager;
    @EJB
    private ConceptDAO conceptDAO;

    /**
     * Este método es responsabled de realizar las validaciones de reglas de negocio.
     */
    public void verifyPreConditions(ConceptSMTK concept, Relationship relationship, User user) {

        /* Privilegios del usuario */
        brRelationshipBinding001(relationship, user);

        /* Que no se agreguen dos Snomed de tipo "ES UN MAPEDO DE" */
        brSCT002(concept, relationship);

        /* Las relaciones de semantikos con Snomed CT son 1-1 */
        brSCT001(concept, relationship);

        /* BR-SCT-003: ES MAPEO DE, es una relación exclusiva de Snomed CT */
        brSCT003(concept, relationship);

        /* BR-SCT-004: Un concepto con una relación "ES UN" no debe grabarse si existe otro concepto con las mismas relaciones */
        brRelationshipBinding005(concept, relationship);

    }

    /**
     * Este método gatilla todas las acciones relacionadas con la asociación de relaciones.
     * Un concepto que se elimina siempre es invalidado. Sólo si satisface una regla de negocio (BR-
     *
     * @param relationship La relación que se asoció.
     */
    @Override
    public void postActions(Relationship relationship, User user) {
        publishConceptBySCT(relationship, user);
    }

    /**
     * Este método es responsable de implementar la regla de negocio BR-CON-004.
     * ﻿BR-CON-004 Solo un usuario con rol Modelador puede agregar una relación Snomed CT.
     *
     * @param relationship La relación en cuestión.
     * @param user         El usuario que realiza la asociación.
     */
    public void brRelationshipBinding001(Relationship relationship, User user) {
        if (relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType() && !user.getProfiles().contains(MODELER_PROFILE)) {
            throw new BusinessRuleException("BR-CON-004", "Solo el usuario con rol Modelador puede agregar relaciones de tipo Snomed CT.");
        }
    }

    /**
     * <b>BR-SCT-001</b>: La Relación de Tipo “Es un Mapeo de” que asocia un Concepto de Semantikos con un Concepto de
     * SNOMED-CT; esta relación es uno a uno y es la que hereda el grado de definición de la tabla del Snapshot
     * conceptos de SNOMED CT.
     *
     * @param concept      El concepto al cual se desea agregar la relación.
     * @param relationship La relación que se desea agregar.
     */
    private void brSCT001(ConceptSMTK concept, Relationship relationship) {

        /* Esta regla de negocio aplica sólo a relaciones de tipo SnomedCT */
        if (!isSnomedCTRelationship(relationship)) {
            return;
        }

        /* Se transforma a una relación Snomed CT */
        SnomedCTRelationship snomedCTRelationship = SnomedCTRelationship.createSnomedCT(relationship);


        /* Y se verifica que sup tipo sea "ES_UN_MAPEO DE" */
        if (!snomedCTRelationship.isES_UN_MAPEO_DE()) {
            return;
        }

        /*
         * Ahora que sabemos que es una relación Snomed y de tipo ES UN MAPEO DE, se verifica que no exista otra
         * relación Snomed, del mismo tipo, desde otro concepto al mismo concepto destino.
         *
         * Se recuperan todas las relaciones del mismo tipo de relación y que se dirigen al mismo concepto SCT
         */
        List<Relationship> relationshipsLike = relationshipManager.getRelationshipsLike(snomedCTRelationship.getRelationshipDefinition(), snomedCTRelationship.getTarget());
        for (Relationship relationshipCandidate : relationshipsLike) {
            ConceptSMTK candidateConcept = relationshipCandidate.getSourceConcept();

            /* Si es el mismo concepto, no importa porque está bien :D */
            if (candidateConcept.equals(concept)) {
                continue;
            }

            /* Si la relación no es del tipo ES UN MAPEO DE, no importa tanpoco */
            SnomedCTRelationship snomedCandidate = (SnomedCTRelationship) relationshipCandidate;
            if (!snomedCandidate.isES_UN_MAPEO_DE()) {
                continue;
            }

            /* En este punto, se tiene que la condición es violada y se arroja la excepción */
            throw new BusinessRuleException("BR-SCT-001", "La Relación SnomedCT de Tipo '" + ES_UN_MAPEO_DE
                    + "' es uno a uno. Ya existe una relación SnomedCT de este tipo que apunta al mismo concepto Snomed desde el concepto "
                    + candidateConcept);
        }
    }

    /**
     * Este método es responsable de implmentar la regla de negocio BR-SCT-002.
     * <b>BR-SCT-002</b>: Si la relación que se agrega es de tipo “Es un Mapeo”, el sistema valida que el concepto no
     * tenga otra relación de tipo “Es un Mapeo”.
     *
     * @param concept      El concepto al cual se quiere asociar la relación.
     * @param relationship La relación que se quiere asociar al concepto.
     */
    private void brSCT002(ConceptSMTK concept, Relationship relationship) {

        /* Esta regla de negocio aplica sólo a relaciones de tipo SnomedCT */
        if (!isSnomedCTRelationship(relationship)) {
            return;
        }

        /* Se transforma a una relación Snomed CT */
        SnomedCTRelationship snomedCTRelationship = SnomedCTRelationship.createSnomedCT(relationship);

        /* Y se verifica que sup tipo sea "ES_UN_MAPEO DE" */
        if (!snomedCTRelationship.isES_UN_MAPEO_DE()) {
            return;
        }

        /* El concepto no debe tener más de una relación del tipo "ES UN MAPEO DE" */
        List<SnomedCTRelationship> relationshipsSnomedCT = concept.getRelationshipsSnomedCT();
        for (SnomedCTRelationship ctRelationship : relationshipsSnomedCT) {


            /* Si es la misma no se compara */
            if (ctRelationship.getTarget() == snomedCTRelationship.getTarget()) {
                continue;
            }

            /* Si es la misma no se compara */
            if (ctRelationship == snomedCTRelationship) {
                continue;
            }

            /* Si la relación es del tipo ES UN MAPEO, viola la regla */
            if (ctRelationship.isES_UN_MAPEO_DE()) {
                throw new BusinessRuleException("BR-SCT-002", "Si la relación que se agrega es de tipo “Es un Mapeo”, el " +
                        "sistema valida que el concepto no tenga otra relación de tipo “Es un Mapeo”.");
            }
        }
    }

    /**
     * BR-SCT-003: Si un concepto Semantikos tiene una relación del tipo “Es un Mapeo” a un Concepto SNOMED-CT, no
     * podrá tener ninguna otra relación de ningún tipo a SNOMED-CT.
     *
     * @param concept      El concepto al que se le desea agregar la relación.
     * @param relationship La relación que se desea agregar.
     */
    private void brSCT003(ConceptSMTK concept, Relationship relationship) {

        /* Esta regla de negocio solo aplica a relaciones de tipo SnomedCT */
        if (!isSnomedCTRelationship(relationship) || !concept.hasES_UN_MAPEO()) {
            return;
        }

        /* Se transforma a una relación Snomed CT */
        SnomedCTRelationship snomedCTRelationship = SnomedCTRelationship.createSnomedCT(relationship);

        /* Si la relación es Snomed, se debe validar que el concepto no tenga otras relaciones */
        List<SnomedCTRelationship> relationshipsSnomedCT = concept.getRelationshipsSnomedCT();

        /* Si tiene una relación verificamos que sea la misma que se está validando (podría ya estar agregada) */
        int size = relationshipsSnomedCT.size();

        if (size == 0 || (size == 1 && relationship.equals(relationshipsSnomedCT.get(0)))) {
            return;
        }

        throw new BusinessRuleException("BR-SCT-003", "Si un concepto Semantikos tiene una relación SnomedCT de tipo “Es un Mapeo el concepto no puede tener ninguna otra relación de tipo SnomedCT.");
    }

    /**
     * BR-SCT-004: Al agregar una relación SCT de tipo “ES UN MAPEO” a un concepto SCT con grado de definición
     * “Completamente Definido” si y sólo si no existe otro concepto Semantikos con las mismas relaciones SCT (tipo y
     * concepto SCT destino).
     *
     * @param concept         El concepto cuyas relaciones están cambiando.
     * @param theRelationship La relacion que se agrega, que quizas ya esta.
     */
    private void brRelationshipBinding005(ConceptSMTK concept, Relationship theRelationship) {

        /* Esta arregla aplica sólo a conceptos con una relación ES UN */
        if (!concept.contains(SnomedCTRelationship.ES_UN)) {
            return;
        }

        boolean added = false;
        if (!concept.getRelationshipsSnomedCT().contains(theRelationship)) {
            concept.addRelationship(theRelationship);
            added = true;
        }

        /* Se revisa cada una de las relaciones Snomed del concepto */
        List<SnomedCTRelationship> relationshipsSnomedCT = concept.getRelationshipsSnomedCT();
        for (SnomedCTRelationship snomedCTRelationship : relationshipsSnomedCT) {

            /*
             * Se recuperan todas las relaciones similares.
             * Se verifica si cada concepto origen de la relacion tiene las mismas relacioens snomed
             */
            List<Relationship> relationshipsLike = relationshipManager.getRelationshipsLike(snomedCTRelationship.getRelationshipDefinition(), snomedCTRelationship.getTarget());
            for (Relationship relationship : relationshipsLike) {


                /* Se recupera el concepto origen y se cargan sus relaciones */
                ConceptSMTK sourceConcept = relationship.getSourceConcept();
                if (sourceConcept.getId() != concept.getId()) {
                    List<Relationship> relationshipsBySourceConcept = relationshipManager.getRelationshipsBySourceConcept(sourceConcept);
                    sourceConcept.setRelationships(relationshipsBySourceConcept);

                /* Se pregunta si ambos conceptos tienen las mismas relaciones SnomedCT */

                /* Primero se ve si el concepto origen tiene las relaciones Snomed del concepto en cuestion */
                    SnomedCTRelationship[] relationships = relationshipsSnomedCT.toArray(new SnomedCTRelationship[relationshipsSnomedCT.size()]);
                    boolean contains1 = sourceConcept.containsLike(relationships);

                    List<SnomedCTRelationship> relationshipsSnomedCT1 = sourceConcept.getRelationshipsSnomedCT();
                    boolean contains2 = concept.containsLike(relationshipsSnomedCT1.toArray(new SnomedCTRelationship[relationshipsSnomedCT1.size()]));

                    if (contains1 && contains2) {
                        if (added) {
                            concept.removeRelationship(theRelationship);
                        }
                        throw new BusinessRuleException("BR-SCT-004", "Un concepto [" + sourceConcept.toString() + "] con una relación \"ES UN\" no debe grabarse si existe otro concepto con las mismas relaciones.");
                    }
                }
            }
        }

        if (added) {
            concept.removeRelationship(theRelationship);
        }

    }


    /**
     * <p>Este método implementa la post-acción definida por la regla de negocio BR-CON-003.</p>
     * ﻿<p>BR-CON-003: Concepto pasa de Borrador a Modelado cuando se mapea a SnomedCT.</p>
     *
     * @param relationship La relación que se agregó al concepto.
     */
    private void publishConceptBySCT(Relationship relationship, User user) {

        ConceptSMTK sourceConcept = relationship.getSourceConcept();
        boolean isSnomedCTType = relationship.getRelationshipDefinition().getTargetDefinition().isSnomedCTType();

        /* BL-MOD-001: Un término en Borrador pasará a Modelado cuando el usuario con Rol Modelador realice el
         * mapeo del concepto Semantikos a un concepto a SNOMED CT, o al menos realice una relación del tipo “Es un”.
         */
        if (!isSnomedCTType || sourceConcept.isModeled()) {
            return;
        }

        /* Si es una relación definitoria, se hace deja como modelada */
        SnomedCTRelationship sctRelationship = SnomedCTRelationship.createSnomedCT(relationship);
        if (sctRelationship.isDefinitional()) {
            sourceConcept.setModeled(true);
            conceptManager.publish(sourceConcept, user);
            conceptDAO.update(sourceConcept);
        }
    }

}
