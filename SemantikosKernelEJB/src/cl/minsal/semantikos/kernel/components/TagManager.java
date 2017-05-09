package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.model.ConceptSMTK;
import cl.minsal.semantikos.model.tags.Tag;

import javax.ejb.Local;
import java.util.List;

/**
 * @author Andrés Farías on 8/26/16.
 */
@Local
public interface TagManager {

    public List<Tag> getAllTags();

    public List<Tag> getAllTagsWithoutParent();

    /**
     * Este método es responsable de recuperar un Tag por su ID.
     *
     * @param id El ID del tag buscado.
     *
     * @return El Tag con ID <code>id</code>
     */
    public Tag findTagByID(long id);

    public List<Tag> findTagByNamePattern(String pattern);

    public List<Tag> findTag(Tag tag, String pattern);

    public void removeTag(Tag tag);

    public List<ConceptSMTK> findConceptsByTag(Tag tag);

    /**
     * Este método es responsable de retornar una lista de todos los Tags existentes que no se encuentran en la familia
     * del <code>tag</code>.
     *
     * @param tag El tag cuyo complemento se desea recuperar.
     *
     * @return Una lista con todos los tags que representan el complemento
     */
    public List<Tag> getOtherTags(Tag tag);

    //TODO arreglar esto
    public List<Tag> getTagByConcept(ConceptSMTK conceptSMTK);

    public void assignTag(ConceptSMTK conceptSMTK, Tag tag);

    public void unassignTag(ConceptSMTK conceptSMTK, Tag tag);

    public void persist(Tag tag);

    public void update(Tag tag);

    public void link(Tag tag, Tag tagLink);

    public void unlink(Tag tag, Tag tagUnlink);

    public boolean containTag(String nameTag);

    public long conceptContain(Tag tag);
}
