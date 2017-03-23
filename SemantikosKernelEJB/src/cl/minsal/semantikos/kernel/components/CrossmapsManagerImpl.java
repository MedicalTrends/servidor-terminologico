package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.model.businessrules.CrossMapCreationBR;
import cl.minsal.semantikos.model.businessrules.CrossMapRemovalBR;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 8/30/16.
 */
@Stateless
public class CrossmapsManagerImpl implements CrossmapsManager {

    /** El logger de la clase */
    private static final Logger logger = LoggerFactory.getLogger(CrossmapsManagerImpl.class);

    @EJB
    private AuditManager auditManager;

    @EJB
    private CrossmapsDAO crossmapsDAO;

    @EJB
    private RelationshipManager relationshipManager;

    @EJB
    private CrossmapFactory crossmapFactory;

    /** Caché de los crossmapSets */
    private List<CrossmapSet> crossmapSetsCache;

    /**
     * Inicialización básica.
     */
    public CrossmapsManagerImpl() {
        this.crossmapSetsCache = new ArrayList<>();
    }

    @Override
    public Crossmap create(DirectCrossmap directCrossmap, User user) {

        /* Se aplican las reglas de negocio */
        new CrossMapCreationBR().applyRules(directCrossmap, user);

        /* Se realiza la creación a nivel de persitencia*/
        crossmapsDAO.create(directCrossmap, user);

        /* Se registra en el historial */
        if (directCrossmap.getSourceConcept().isModeled()) {
            auditManager.recordCrossMapCreation(directCrossmap, user);
        }

        /* Se retorna la instancia creada */
        return directCrossmap;
    }

    @Override
    public Crossmap remove(Crossmap crossmap, User user) {

        /* Se aplican las reglas de negocio */
        new CrossMapRemovalBR().applyRules(crossmap, user);

        /* TODO: Se realiza la eliminación */

        /* Se registra en el historial */
        if (crossmap.getSourceConcept().isModeled()) {
            auditManager.recordCrossMapRemoval(crossmap, user);
        }

        /* Se retorna la instancia creada */
        return crossmap;
    }

    @Override
    public List<Crossmap> getCrossmaps(ConceptSMTK conceptSMTK) {

        List<Crossmap> allCrossmaps = new ArrayList<>();
        allCrossmaps.addAll(this.getDirectCrossmaps(conceptSMTK));
        allCrossmaps.addAll(this.getIndirectCrossmaps(conceptSMTK));

        return allCrossmaps;
    }

    @Override
    public List<CrossmapSet> getCrossmapSets() {

        /* Se actualiza el caché si está vacío */
        if (crossmapSetsCache.size() == 0){
            this.crossmapSetsCache.addAll(crossmapsDAO.getCrossmapSets());
        }

        /* Se retorna el caché, que estamos seguro que fue actualizado */
        return this.crossmapSetsCache;
    }

    @Override
    public List<DirectCrossmap> getDirectCrossmaps(ConceptSMTK conceptSMTK) {

        /* Se recuperan todas las relaciones del concepto, dentro de las cuales estarán los crossmaps directos */
        List<Relationship> relationshipsBySourceConcept = relationshipManager.getRelationshipsBySourceConcept(conceptSMTK);

        /* Luego se filtran las relaciones a crossmaps indirectos */
        ArrayList<DirectCrossmap> directCrossmaps = new ArrayList<>();
        for (Relationship relationship : relationshipsBySourceConcept) {
            if (relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()){

                /* Se crea una instancia precisa */
                DirectCrossmap directCrossmap;
                if(relationship instanceof DirectCrossmap){
                    directCrossmap = (DirectCrossmap) relationship;
                } else {
                    directCrossmap = crossmapFactory.createDirectCrossmap(relationship);
                }

                /* Y se agrega a la lista */
                directCrossmaps.add(directCrossmap);
            }
        }

        return directCrossmaps;
    }

    @Override
    public List<CrossmapSetMember> getDirectCrossmapsSetMembersOf(ConceptSMTK conceptSMTK) {

        /* Se obtienen los crossmaps directos del concepto */
        List<DirectCrossmap> directCrossmaps = this.getDirectCrossmaps(conceptSMTK);

        /* Luego se almacenan los crossmapSetMembers referenciados en una lista */
        ArrayList<CrossmapSetMember> directCrossmapSetMembers = new ArrayList<>();
        for (DirectCrossmap directCrossmap : directCrossmaps) {
            directCrossmapSetMembers.add(directCrossmap.getTarget());
        }

        return directCrossmapSetMembers;
    }

    @Override
    public CrossmapSetMember getCrossmapSetMemberById(long id) {
        return crossmapsDAO.getCrossmapSetMemberById(id);
    }

    @Override
    public List<CrossmapSetMember> getCrossmapSetByAbbreviatedName(String crossmapSetAbbreviatedName) {

        /* Lo primero es recuperar el crossmapSet a partir de su nombre abreviado */
        return crossmapsDAO.getCrossmapSetMemberByAbbreviatedName(crossmapSetAbbreviatedName);
    }

    @Override
    public List<IndirectCrossmap> getIndirectCrossmaps(ConceptSMTK conceptSMTK) {

        /* Se valida si el concepto tiene cargada sus relaciones */
        if (!conceptSMTK.isRelationshipsLoaded()) {
            List<Relationship> relationshipsBySourceConcept = relationshipManager.getRelationshipsBySourceConcept(conceptSMTK);
            conceptSMTK.setRelationships(relationshipsBySourceConcept);
        }

        /* Se recuperan las relaciones a Snomed CT del tipo ES_UN o ES UN MAPEO DE */
        List<SnomedCTRelationship> relationshipsSnomedCT = conceptSMTK.getRelationshipsSnomedCT();
        List<IndirectCrossmap> indirectCrossmaps = new ArrayList<>();
        for (SnomedCTRelationship snomedCTRelationship : relationshipsSnomedCT) {
            if (snomedCTRelationship.isES_UN_MAPEO() || snomedCTRelationship.isES_UN()) {

                /* Se recuperan los crossmaps del concepto CST y se almacenan */
                indirectCrossmaps.addAll(crossmapsDAO.getCrossmapsBySCT(snomedCTRelationship.getTarget().getId(), conceptSMTK));
            }
        }

        logger.debug("Se cargaron " + indirectCrossmaps.size() + " indirectos para el concepto " + conceptSMTK + ": " + indirectCrossmaps);
        return indirectCrossmaps;
    }

    @Override
    public DirectCrossmap bind(ConceptSMTK conceptSMTK, CrossmapSetMember crossmapSetMember) {
        return crossmapsDAO.bindConceptSMTKToCrossmapSetMember(conceptSMTK, crossmapSetMember);
    }

    @Override
    public List<CrossmapSetMember> findByPattern(CrossmapSet crossmapSet, String pattern) {
        return crossmapsDAO.findCrossmapSetMemberBy(crossmapSet, pattern);
    }

    @Override
    public List<CrossmapSetMember> findByPatternCode1(CrossmapSet crossmapSet, String code) {
        return crossmapsDAO.findCrossmapSetMemberByCod1(crossmapSet,code);
    }
}
