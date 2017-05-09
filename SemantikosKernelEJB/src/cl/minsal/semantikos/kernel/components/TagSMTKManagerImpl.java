package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.TagSMTKDAO;
import cl.minsal.semantikos.model.tagsSMTK.TagSMTK;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Andrés Farías on 9/5/16.
 */
@Stateless
public class TagSMTKManagerImpl implements TagSMTKManager {

    @EJB
    TagSMTKDAO tagSMTKDAO;

    @Override
    public List<TagSMTK> getAllTagSMTKs() {
        return tagSMTKDAO.getAllTagSMTKs();
    }

    @Override
    public TagSMTK findTagSTMKByID(@NotNull long idTag) {
        return tagSMTKDAO.findTagSMTKByID(idTag);
    }
}
