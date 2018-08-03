package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.CrossmapsDAO;
import cl.minsal.semantikos.kernel.factories.CrossmapFactory;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.relationships.Target;
import cl.minsal.semantikos.model.snomedct.ConceptSCT;
import cl.minsal.semantikos.model.users.Roles;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.kernel.businessrules.CrossMapCreationBR;
import cl.minsal.semantikos.kernel.businessrules.CrossMapRemovalBR;
import cl.minsal.semantikos.model.crossmaps.*;
import cl.minsal.semantikos.model.relationships.Relationship;
import cl.minsal.semantikos.model.relationships.SnomedCTRelationship;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrés Farías on 8/30/16.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
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


    @Override
    @PermitAll
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
    @PermitAll
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
    @PermitAll
    public List<Crossmap> getCrossmaps(ConceptSMTK conceptSMTK) throws Exception {

        List<Crossmap> allCrossmaps = new ArrayList<>();
        allCrossmaps.addAll(this.getDirectCrossmaps(conceptSMTK));
        allCrossmaps.addAll(this.getIndirectCrossmaps(conceptSMTK));

        return allCrossmaps;
    }

    @Override
    @PermitAll
    public List<CrossmapSet> getCrossmapSets() {

        return CrossmapSetFactory.getInstance().getCrossmapSets();
    }

    @Override
    @PermitAll
    public List<DirectCrossmap> getDirectCrossmaps(ConceptSMTK conceptSMTK) {

        /* Se recuperan todas las relaciones del concepto, dentro de las cuales estarán los crossmaps directos */
        List<Relationship> relationshipsBySourceConcept;

        if(!conceptSMTK.isRelationshipsLoaded()) {
            relationshipsBySourceConcept = relationshipManager.getRelationshipsBySourceConcept(conceptSMTK);
        }
        else {
            relationshipsBySourceConcept = conceptSMTK.getRelationships();
        }

        /* Luego se filtran las relaciones a crossmaps indirectos */
        ArrayList<DirectCrossmap> directCrossmaps = new ArrayList<>();
        for (Relationship relationship : relationshipsBySourceConcept) {
            if (relationship.getRelationshipDefinition().getTargetDefinition().isCrossMapType()){

                if(relationship.getValidityUntil() != null && relationship.getValidityUntil().before(new Timestamp(System.currentTimeMillis()))) {
                    continue;
                }

                /* Se crea una instancia precisa */
                DirectCrossmap directCrossmap;
                if(relationship instanceof DirectCrossmap){
                    directCrossmap = (DirectCrossmap) relationship;
                } else {
                    directCrossmap = createDirectCrossmap(relationship);
                }

                /* Y se agrega a la lista */
                directCrossmaps.add(directCrossmap);
            }
        }

        return directCrossmaps;
    }

    public DirectCrossmap createDirectCrossmap(Relationship relationship) {

        /* Se transforma el target a un CrossmapSetMember */
        Target target = relationship.getTarget();
        if (!(target instanceof CrossmapSetMember)){
            throw new IllegalArgumentException("La relación no es de tipo DirectCrossmap.");
        }
        CrossmapSetMember crossmapSetMember = (CrossmapSetMember) target;

        /* Se retorna una instancia fresca */
        return new DirectCrossmap(relationship.getId(), relationship.getSourceConcept(), crossmapSetMember, relationship.getRelationshipDefinition(), relationship.getValidityUntil());
    }

    @Override
    @PermitAll
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
    @PermitAll
    public CrossmapSetMember getCrossmapSetMemberById(CrossmapSet crossmapSet, long id) {
        return crossmapsDAO.getCrossmapSetMemberById(crossmapSet, id);
    }

    @Override
    @PermitAll
    public List<CrossmapSetMember> getCrossmapSetMemberByCrossmapSet(CrossmapSet crossmapSet, int page, int pageSize) {

        /* Lo primero es recuperar el crossmapSet a partir de su nombre abreviado */
        return crossmapsDAO.getCrossmapSetMembersPaginated(crossmapSet, page, pageSize);
    }

    @Override
    @PermitAll
    public List<IndirectCrossmap> getIndirectCrossmaps(ConceptSMTK conceptSMTK) throws Exception {

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
                indirectCrossmaps.addAll(crossmapsDAO.getCrossmapsBySCT(snomedCTRelationship, conceptSMTK));
            }
        }

        logger.debug("Se cargaron " + indirectCrossmaps.size() + " indirectos para el concepto " + conceptSMTK + ": " + indirectCrossmaps);
        return indirectCrossmaps;
    }

    @Override
    @PermitAll
    public List<IndirectCrossmap> getIndirectCrossmaps(ConceptSCT conceptSCT) throws Exception {
        return crossmapsDAO.getCrossmapsBySCT(conceptSCT);
    }

    @Override
    @PermitAll
    public List<CrossmapSetMember> findByPattern(CrossmapSet crossmapSet, String pattern) {
        return crossmapsDAO.findCrossmapSetMemberByPattern(crossmapSet, pattern);
    }

}
