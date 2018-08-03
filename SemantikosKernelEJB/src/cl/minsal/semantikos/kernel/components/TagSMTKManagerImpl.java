package cl.minsal.semantikos.kernel.components;

import cl.minsal.semantikos.kernel.daos.TagSMTKDAO;
import cl.minsal.semantikos.model.tags.TagSMTK;
import cl.minsal.semantikos.model.tags.TagSMTKFactory;
import cl.minsal.semantikos.model.users.Roles;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Andrés Farías on 9/5/16.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class TagSMTKManagerImpl implements TagSMTKManager {

    @EJB
    TagSMTKDAO tagSMTKDAO;

    @Override
    @PermitAll
    public List<TagSMTK> getAllTagSMTKs() {
        return tagSMTKDAO.getAllTagSMTKs();
    }

    @Override
    @PermitAll
    public TagSMTK findTagSTMKByID(@NotNull long idTag) {
        return tagSMTKDAO.findTagSMTKByID(idTag);
    }

    @Override
    @PermitAll
    public TagSMTKFactory getTagSMTKFactory() {
        return TagSMTKFactory.getInstance();
    }
}
