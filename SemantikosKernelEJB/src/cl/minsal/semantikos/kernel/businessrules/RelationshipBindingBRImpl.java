package cl.minsal.semantikos.kernel.businessrules;

import cl.minsal.semantikos.kernel.components.ConceptManager;
import cl.minsal.semantikos.kernel.components.RelationshipManager;
import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.DescriptionDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.basictypes.BasicTypeValue;
import cl.minsal.semantikos.model.descriptions.Description;
import cl.minsal.semantikos.model.exceptions.BusinessRuleException;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import cl.minsal.semantikos.model.relationships.TargetDefinition;
import cl.minsal.semantikos.model.users.User;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import java.util.List;

import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.ES_UN_MAPEO_DE;
import static cl.minsal.semantikos.model.relationships.SnomedCTRelationship.isSnomedCTRelationship;
import static cl.minsal.semantikos.model.users.ProfileFactory.MODELER_PROFILE;

/**
 * Esta clase implementa las reglas de negocio de agregar relaciones.
 *
 * @author Andrés Farías on 9/8/16.
 */
@Singleton
public class RelationshipBindingBRImpl implements RelationshipBindingBR {

    @EJB
    private RelationshipManager relationshipManager;

    @EJB
    private ConceptManager conceptManager;

    @EJB
    private ConceptDAO conceptDAO;

    @EJB
    private DescriptionDAO descriptionDAO;

    /**
     * Este método es responsabled de realizar las validaciones de reglas de negocio.
     */
    public void verifyPreConditions(ConceptSMTK concept, Relationship relationship, User user) throws Exception {

        /* Privilegios del usuario */
        brRelationshipBinding001(relationship, user);

        /* Que no se agreguen dos Snomed de tipo "ES UN MAPEDO DE" */
        brSCT002(concept, relationship);

        /* Las relaciones de semantikos con Snomed CT son 1-1 */
        brSCT001(concept, relationship);

        /* BR-SCT-003: ES MAPEO DE, es una relación exclusiva de Snomed CT */
        brSCT003(concept, relationship);

        /* BR-SCT-004: Un concepto con una relación "ES UN" no debe grabarse si existe otro concepto con las mismas relaciones */
        brSCT004(concept, relationship);

        /* BR-SCT-004: Un concepto con una relación "ES UN" no debe grabarse si existe otro concepto con las mismas relaciones */
        brSCT004(concept, relationship);

        brISP001(concept, relationship);

        brISP002(concept, relationship);

        brISP003(concept, relationship);

        brISP004(concept, relationship);

        brGTIN005(concept, relationship);

    }

    /**
     * Este método gatilla todas las acciones relacionadas con la asociación de relaciones.
     * Un concepto que se elimina siempre es invalidado. Sólo si satisface una regla de negocio (BR-
     *
     * @param relationship La relación que se asoció.
     */
    @Override
    public void postActions(Relationship relationship, User user) throws Exception {
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
    public void brSCT001(ConceptSMTK concept, Relationship relationship) throws Exception {

        /* Esta regla de negocio aplica sólo a relaciones de tipo SnomedCT */
        if (!isSnomedCTRelationship(relationship)) {
            return;
        }

        /* Se transforma a una relación Snomed CT */
        SnomedCTRelationship snomedCTRelationship = SnomedCTRelationship.createSnomedCT(relationship);


        /* Y se verifica que sup tipo sea "ES_UN_MAPEO DE" */
        if (!snomedCTRelationship.isES_UN_MAPEO()) {
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
            if (!snomedCandidate.isES_UN_MAPEO()) {
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
    private void brSCT002(ConceptSMTK concept, Relationship relationship) throws Exception {

        /* Esta regla de negocio aplica sólo a relaciones de tipo SnomedCT */
        if (!isSnomedCTRelationship(relationship)) {
            return;
        }

        /* Se transforma a una relación Snomed CT */
        SnomedCTRelationship snomedCTRelationship = SnomedCTRelationship.createSnomedCT(relationship);

        /* Y se verifica que sup tipo sea "ES_UN_MAPEO DE" */
        if (!snomedCTRelationship.isES_UN_MAPEO()) {
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
            if (ctRelationship.isES_UN_MAPEO()) {
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
    private void brSCT003(ConceptSMTK concept, Relationship relationship) throws Exception {

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
    private void brSCT004(ConceptSMTK concept, Relationship theRelationship) throws Exception {

        /*
         * Esta arregla aplica sólo a conceptos a los que se les quiere agregar una relación SCT de tipo ES UN MAPEO a
         * un concepto SCT con un grado de definición "Completamente Definido"
         */
        if (!SnomedCTRelationship.isSnomedCTRelationship(theRelationship)) {
            return;
        }

        /**
         * Si el concepto es primitivo no válida
         */
        if(!concept.isFullyDefined()){
            return;
        }

        /* Es una relación SCT. Se realiza el cast y se validan las otras dos condiciones para la BR. */
        SnomedCTRelationship sctRel = SnomedCTRelationship.createSnomedCT(theRelationship);
        if (sctRel.isES_UN_MAPEO() || !sctRel.getTarget().isCompletelyDefined()){
            return;
        }

        /*
         * Dado que es de tipo "ES UN MAPEO" y es "completamente definido", se valida la unicidad en el conjunto de
         * relaciones en el universo Semántikos
         */
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

     /* BR-SCT-005 Para que un Concepto Semantiko pueda ser publicado para su uso, deberá estar Modelado con al menos un tipo de relación “Es un” ó “Es un Mapeo” */
    public void brSCT005(ConceptSMTK concept) throws Exception {

        int countRelationshipSCT=0;

        List<SnomedCTRelationship> relationshipsSnomedCT = concept.getRelationshipsSnomedCT();

        for (SnomedCTRelationship snomedCTRelationship:relationshipsSnomedCT) {
            if(snomedCTRelationship.isES_UN() || snomedCTRelationship.isES_UN_MAPEO()) {
                countRelationshipSCT++;
            }
        }
        if(countRelationshipSCT==0) {
            throw new BusinessRuleException("BR-SCT-005", "Para que un Concepto Semantiko pueda ser publicado para su uso, deberá estar Modelado con al menos un tipo de relación “Es un” ó “Es un Mapeo”");

        }
    }

    /* BR-STK-001 Para agregar una relación a ISP, la dupla ProductoComercual-RegnumRegAño deben ser únicos */
    public void brISP001(ConceptSMTK concept, Relationship relationship) throws Exception {

        /* Verificar en un contexto no persistente */
        /*
        if (relationship.getRelationshipDefinition().isISP() && concept.contains(relationship)) {
            throw new BusinessRuleException("BR-ISP-001", "Para agregar una relación a ISP, la dupla ProductoComercial-Regnum/RegAño deben ser únicos. Registro referenciado por 'Nuevo Concepto Borrador'");
        }
        */

        /* Verificar en un contexto persistente */
        if (relationship.getRelationshipDefinition().isISP()) {
            for (Relationship relationship2 : relationshipManager.findRelationshipsLike(relationship.getRelationshipDefinition(), relationship.getTarget())) {
                if(relationship2.getRelationshipDefinition().isISP() && relationship2.getSourceConcept().equals(concept)) {
                    throw new BusinessRuleException("BR-ISP-001", "Para agregar una relación a ISP, el par ProductoComercial-Regnum/RegAño deben ser únicos. Registro referenciado por concepto "+relationship2.getSourceConcept());
                }
            }
        }
    }

    /* BR-STK-002 Para agregar una relación a ISP, la dupla ProductoComercual-RegNumRegAño no debe existir como bioequivalente  */
    public void brISP002(ConceptSMTK concept, Relationship relationship) {

        /* Verificar en un contexto persistente */
        if (relationship.getRelationshipDefinition().isISP()) {

            /* Verificar en un contexto no persistente */
            for (Relationship relationship2 : concept.getValidRelationships()) {
                if(relationship2.getRelationshipDefinition().isBioequivalente() && relationship2.getTarget().equals(relationship.getTarget())) {
                    throw new BusinessRuleException("BR-ISP-002", "Para agregar una relación a ISP, el par ProductoComercial-RegNumRegAño no debe existir como bioequivalente. Registro referenciado por 'Nuevo Concepto Borrador'");
                }
            }

            for (Relationship relationship2 : relationshipManager.findRelationshipsLike(relationship.getRelationshipDefinition(), relationship.getTarget())) {
                if(relationship2.getRelationshipDefinition().isBioequivalente() && relationship2.getSourceConcept().equals(concept)) {
                    throw new BusinessRuleException("BR-ISP-002", "Para agregar una relación a ISP, el par ProductoComercial-RegNumRegAño no debe existir como bioequivalente. Registro referenciado por concepto "+relationship2.getSourceConcept());
                }
            }
        }
    }

    /* BR-STK-003 Para agregar una relación a Bioequivalente, la dupla ProductoComercual-RegnumRegAño deben ser únicos */
    public void brISP003(ConceptSMTK concept, Relationship relationship) throws Exception {

        /* Verificar en un contexto no persistente */
        /*
        if (relationship.getRelationshipDefinition().isBioequivalente() && concept.contains(relationship)) {
            throw new BusinessRuleException("BR-ISP-003", "Para agregar una relación a Bioequivalente, el par ProductoComercial-Regnum/RegAño deben ser únicos. Registro referenciado por 'Nuevo Concepto Borrador'");
        }
        */

        /* Verificar en un contexto persistente */
        if (relationship.getRelationshipDefinition().isBioequivalente()) {
            for (Relationship relationship2 : relationshipManager.findRelationshipsLike(relationship.getRelationshipDefinition(), relationship.getTarget())) {
                if(relationship2.getRelationshipDefinition().isBioequivalente() && relationship2.getSourceConcept().equals(concept)) {
                    throw new BusinessRuleException("BR-ISP-003", "Para agregar una relación a Bioequivalente, el par ProductoComercial-Regnum/RegAño deben ser únicos. Registro referenciado por concepto "+relationship2.getSourceConcept());
                }
            }
        }
    }

    /* BR-STK-004 Para agregar una relación a Bioequivalente, la dupla ProductoComercual-RegNumRegAño no debe existir como ISP  */
    public void brISP004(ConceptSMTK concept, Relationship relationship) {

        boolean isPairUnique = true;
        ConceptSMTK conceptIssue = null;

        if (relationship.getRelationshipDefinition().isBioequivalente()) {

            /* Verificar en un contexto no persistente */
            for (Relationship relationship2 : concept.getValidRelationships()) {
                if(relationship2.getRelationshipDefinition().isISP() && relationship2.getTarget().equals(relationship.getTarget())) {
                    throw new BusinessRuleException("BR-ISP-004", "Para agregar una relación a Bioequivalente, el par ProductoComercial-RegNumRegAño no debe existir como ISP. Registro referenciado por 'Nuevo Concepto Borrador'");
                }
            }

            for (Relationship relationship2 : relationshipManager.findRelationshipsLike(relationship.getRelationshipDefinition(), relationship.getTarget())) {
                if(relationship2.getRelationshipDefinition().isISP() && relationship2.getSourceConcept().equals(concept)) {
                    throw new BusinessRuleException("BR-ISP-004", "Para agregar una relación a Bioequivalente, el par ProductoComercial-RegNumRegAño no debe existir como ISP. Registro referenciado por concepto "+relationship2.getSourceConcept());
                }
            }
        }
    }

    /* BR-GTIN-001 Para agregar una relación a GTINGS1, su valor debe ser único */
    public void brGTIN005(ConceptSMTK concept, Relationship relationship) throws Exception {
        /* Verificar en un contexto persistente */
        if (relationship.getTarget() != null && relationship.getRelationshipDefinition().isGTIN()) {
            for (Relationship relationship2 : relationshipManager.findRelationshipsLike(relationship.getRelationshipDefinition(), relationship.getTarget())) {
                if(!relationship2.getSourceConcept().equals(concept)) {
                    throw new BusinessRuleException("BR-GTIN-001", "Ya existe un concepto con este númeto GTIN: "+relationship2.getSourceConcept());
                }
            }
        }
    }


    /**
     * <p>Este método implementa la post-acción definida por la regla de negocio BR-CON-003.</p>
     * ﻿<p>BR-CON-003: Concepto pasa de Borrador a Modelado cuando se mapea a SnomedCT.</p>
     *
     * @param relationship La relación que se agregó al concepto.
     */
    private void publishConceptBySCT(Relationship relationship, User user) throws Exception {

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
            /* Se modela cada una de las descripciones del concepto */
            /*
            for (Description description : sourceConcept.getDescriptions()) {
                description.setModeled(true);
                descriptionDAO.update(description);
            }
            */
        }
    }

}
