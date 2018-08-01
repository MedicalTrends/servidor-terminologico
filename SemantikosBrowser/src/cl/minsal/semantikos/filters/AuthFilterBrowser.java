package cl.minsal.semantikos.filters;

import cl.minsal.semantikos.browser.AuthenticationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * @author Francisco Mendez on 19-05-2016.
 */
public class AuthFilterBrowser implements Filter {

    static private final Logger logger = LoggerFactory.getLogger(AuthFilterBrowser.class);

    static public final String AUTH_KEY = "bp.session.user";

    static public final String AUTH_VALUE = "semantikos.minsal@gmail.com";

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        ((HttpServletResponse) response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        ((HttpServletResponse) response).setHeader("Pragma", "no-cache"); // HTTP 1.0.
        ((HttpServletResponse) response).setHeader("Expires", "0");

        if(req.getSession().isNew()) {
            req.getSession().invalidate();
        }

        if((req.getRequestURI().equals("/views/home.xhtml") || req.getRequestURI().equals("/")) && req.getParameterMap().isEmpty()) {
            req.getSession().invalidate();
        }

        chain.doFilter(request, response);
    }

    private void login(HttpServletRequest req) {

        req.getSession().setAttribute(AUTH_KEY, AUTH_VALUE);

    }

    private boolean isLoggedIn(HttpServletRequest req) {
        return req.getSession().getAttribute(AuthenticationBean.AUTH_KEY) != null;
    }

    private boolean hasPermission(HttpServletRequest req) {

        if (!isLoggedIn(req)) {
            return false;
        }

        return true;
    }

    public void destroy() {
        /**
         * Not implemented yet
         */
    }


    public void init(FilterConfig arg0) throws ServletException {
        /**
         * Not implemented yet
         */
    }


}


