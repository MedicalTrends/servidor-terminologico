package cl.minsal.semantikos.kernel.componentsweb;

import cl.minsal.semantikos.kernel.daos.TimeOutWebDAO;

import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 * Created by des01c7 on 09-01-17.
 */
@Stateless
public class TimeOutWebImpl implements TimeOutWeb {
    @EJB
    private TimeOutWebDAO timeOutWeb;

    @Override
    public int getTimeOut() {
        return timeOutWeb.getTimeOut();
    }
}
