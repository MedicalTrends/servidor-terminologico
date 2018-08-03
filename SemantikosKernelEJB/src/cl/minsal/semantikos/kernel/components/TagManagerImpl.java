package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.ConceptDAO;
import cl.minsal.semantikos.kernel.daos.TagDAO;
import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.tags.Tag;
import cl.minsal.semantikos.kernel.businessrules.TagCreationBR;
import cl.minsal.semantikos.model.users.Roles;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author Andrés Farías on 8/26/16.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class TagManagerImpl implements TagManager {

    /**
     * El logger de la clase
     */
    private static final Logger logger = LoggerFactory.getLogger(TagManagerImpl.class);

    @EJB
    private TagDAO tagDAO;

    @EJB
    private ConceptDAO conceptDAO;

    @Override
    @PermitAll
    public List<Tag> getAllTags() {
        logger.debug("Obteniendo todos los Tags del sistema.");
        return tagDAO.getAllTags();
    }

    @Override
    @PermitAll
    public List<Tag> getAllTagsWithoutParent() {
        logger.debug("Obteniendo todos los Tags sin padres.");
        return tagDAO.getAllTagsWithoutParent();
    }

    @Override
    @PermitAll
    public Tag findTagByID(long id) {
        logger.debug("Buscando tag con ID=" + id);

        Tag tagByID = tagDAO.findTagByID(id);
        logger.debug("Tag encontrado: " + tagByID);

        return tagByID;
    }

    @Override
    @PermitAll
    public List<Tag> findTagByNamePattern(String pattern) {
        logger.debug("Buscando tags por patrón: " + pattern);


        //String[] patterns = patternToArray(pattern);
        List<Tag> tagsBy = tagDAO.findTagsBy(new String[]{pattern});
        logger.debug(tagsBy.size() + " tags encontrados por patrón: " + pattern);

        return tagsBy;
    }


    @Override
    @PermitAll
    public List<Tag> findTag(Tag tag, String pattern) {
        List<Tag> tagList = findTagByNamePattern(pattern);

        for (int j = 0; j < tagList.size(); j++) {
            if(tag.getParentTag()!=null){
                if(tag.getParentTag().equals(tagList.get(j))){
                    tagList.remove(j);
                    continue;
                }
            }
            if (tag.equals(tagList.get(j))) {
                tagList.remove(j);
                continue;
            } else {
                for (Tag tagSon:tag.getSonTag()) {
                    if (tagSon.equals(tagList.get(j))) {
                        tagList.remove(j);
                        break;
                    }
                }
            }
        }
        return tagList;
    }

    @Override
    @PermitAll
    public void removeTag(Tag tag) {
        logger.debug("Eliminando Tag: " + tag);
        tagDAO.remove(tag);
        logger.debug("Tag eliminado: " + tag);
    }

    @Override
    @PermitAll
    public List<ConceptSMTK> findConceptsByTag(Tag tag) {
        logger.debug("Buscando conceptos por Tag: " + tag);

        List<ConceptSMTK> conceptsByTag = conceptDAO.findConceptsByTag(tag);
        logger.debug(conceptsByTag.size() + " conceptos asociados al tag " + tag + " encontrados.");

        return conceptsByTag;
    }

    @Override
    @PermitAll
    public List<Tag> getOtherTags(Tag tag) {

        List<Tag> allTags = getAllTagsWithoutParent();
        List<Tag> otherTags = new ArrayList<>();
        for (Tag aTag : allTags) {
            if (!aTag.containsInItsFamily(tag)) {
                otherTags.add(aTag);
            }
        }

        return otherTags;
    }

    @Override
    @PermitAll
    public List<Tag> getTagByConcept(ConceptSMTK conceptSMTK) {
        return tagDAO.getTagsByConcept(conceptSMTK);
    }

    @Override
    @PermitAll
    public void assignTag(ConceptSMTK conceptSMTK, Tag tag) {
        logger.debug("Asociando el tag " + tag + " al concepto " + conceptSMTK);

        tagDAO.assignTag(conceptSMTK, tag);
        logger.debug("Se ha asociando el tag " + tag + " al concepto " + conceptSMTK);
    }

    @Override
    @PermitAll
    public void unassignTag(ConceptSMTK conceptSMTK, Tag tag) {
        logger.debug("Desasociando el tag " + tag + " al concepto " + conceptSMTK);

        tagDAO.unassignTag(conceptSMTK, tag);
        logger.debug("Se ha asociando el tag " + tag + " al concepto " + conceptSMTK);
    }

    @Override
    @PermitAll
    public long persist(Tag tag) {
        logger.debug("Creando concepto " + tag);

        /* Se validan las reglas de negocio */
        new TagCreationBR().applyRules(tag);

        /* Se persiste primero el padre, luego el tag, y luego sus hijos */
        Tag parentTag = tag.getParentTag();
        if (parentTag != null) {
            if (parentTag.getId() == -1) persist(parentTag);
        }

        /* Luego el tag mismo */
        if (tag.getId() == -1) tagDAO.persist(tag);

        /* Luego sus hijos */
        for (Tag son : tag.getSonTag()) {

            if (son.getId() == -1) {
                son.setParentTag(tag);
                persist(son);
            }else{
                son.setParentTag(tag);
                update(son);
            }

        }
        logger.debug("Tag creado:" + tag);

        return tag.getId();
    }

    @Override
    @PermitAll
    public void update(Tag tag) {
        logger.debug("Actualizando tag " + tag);
        tagDAO.update(tag);
        logger.debug("Tag actualizado:" + tag);
    }

    @Override
    @PermitAll
    public void link(Tag tag, Tag tagLink) {
        logger.debug("Asociación de Tags:" + tag + " --> " + tagLink);

        tagDAO.linkTagToTag(tag, tagLink);
        logger.debug("Se asociaron tags:" + tag + " --> " + tagLink);
    }

    @Override
    @PermitAll
    public void unlink(Tag tag, Tag tagUnlink) {
        logger.debug("Desasociación de Tags:" + tag + " --> " + tagUnlink);

        tagDAO.unlinkTagToTag(tag, tagUnlink);
        logger.debug("Se desasociaron Tags:" + tag + " --> " + tagUnlink);
    }


    private String[] patternToArray(String pattern) {
        if (pattern != null) {
            StringTokenizer st;
            String token;
            st = new StringTokenizer(pattern, " ");
            ArrayList<String> listPattern = new ArrayList<>();

            while (st.hasMoreTokens()) {
                token = st.nextToken();
                listPattern.add(token.trim());

            }
            return listPattern.toArray(new String[listPattern.size()]);
        }
        return new String[0];
    }

    @Override
    @PermitAll
    public long conceptContain(Tag tag) {
        return tagDAO.countConceptContainTag(tag);
    }

    @Override
    @PermitAll
    public boolean containTag(String nameTag) {
        return tagDAO.containTag(nameTag);
    }
}
