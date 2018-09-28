package cl.minsal.semantikos.kernel.componentsweb;

import cl.minsal.semantikos.kernel.daos.TimeOutWebDAO;
import cl.minsal.semantikos.model.users.Roles;
import org.jboss.ejb3.annotation.SecurityDomain;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by des01c7 on 09-01-17.
 */
@Stateless
@SecurityDomain("SemantikosDomain")
@DeclareRoles({Roles.ADMINISTRATOR_ROLE, Roles.DESIGNER_ROLE, Roles.MODELER_ROLE, Roles.WS_CONSUMER_ROLE, Roles.REFSET_ADMIN_ROLE, Roles.QUERY_ROLE})
public class TimeOutWebImpl implements TimeOutWeb {

    @EJB
    private TimeOutWebDAO timeOutWeb;

    @Override
    @PermitAll
    public int getTimeOut() {
        return timeOutWeb.getTimeOut();
    }
}
