package cl.minsal.semantikos.listeners;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by root on 06-11-20.
 */
public class SessionTimeOutListener implements HttpSessionListener {


    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        //httpSessionEvent.getSession().invalidate();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //long now = new java.util.Date().getTime();
        //boolean timeout = (now - httpSessionEvent.getSession().getLastAccessedTime()) >= ((long)httpSessionEvent.getSession().getMaxInactiveInterval() * 1000L);
        httpSessionEvent.getSession().invalidate();
    }
}
