package cl.minsal.semantikos.filters;

import cl.minsal.semantikos.Constants;
import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.users.AuthenticationBean;
import org.apache.catalina.realm.GenericPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

import static cl.minsal.semantikos.users.AuthenticationBean.AUTH_KEY;


/**
 * @author Francisco Mendez on 19-05-2016.
 */
public class AuthFilterDesigner implements Filter {

    static private final Logger logger = LoggerFactory.getLogger(AuthFilterDesigner.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        /* Inició sesión e intenta volver atrás */
        /*
        if(isLoggedIn(req) && req.getRequestURI().contains(Constants.LOGIN_PAGE)) {
            logger.debug("Intento de acceso sin sesión: " + req);
            res.sendRedirect(req.getContextPath() + Constants.HOME_PAGE);
        }
        */

        if(req.getContextPath().equals("/designer")) {

            if (isUnauthorizedPage(req) || hasPermission(req)) {
                if(isUnauthorizedPage(req)) {
                    res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                    res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                    res.setDateHeader("Expires", 0);
                }
                logger.debug("Request válido, se deja continuar: " + req);
                chain.doFilter(request, response);
            }
        /* Perdió la sesión o está tratando de conectarse sin haberse logueado */
            else if (!isLoggedIn(req)) {
                logger.debug("Intento de acceso sin sesión: " + req);
                //res.sendRedirect(req.getContextPath() + Constants.VIEWS_FOLDER + Constants.LOGIN_PAGE);
                chain.doFilter(request, response);
            }

        /* No tiene permiso para acceder a la pagina solicitada */
            else if (!hasPermission(req)) {
                logger.debug("Intento de acceso sin sesión: " + req);
                res.sendRedirect(req.getContextPath() + Constants.VIEWS_FOLDER + Constants.ERRORS_FOLDER + Constants.AUTH_ERROR_PAGE);
            }

        /* Otros casos que nunca deberían darse */
            else {
                logger.debug("Un caso que nunca debiera darse: " + req);
                //res.sendRedirect(req.getContextPath() + Constants.VIEWS_FOLDER + Constants.LOGIN_PAGE);
            }

        }

    }

    private boolean isUnauthorizedPage(HttpServletRequest req) {
        return req.getRequestURI().contains(Constants.LOGIN_PAGE) || req.getRequestURI().contains(Constants.ERRORS_FOLDER) ||
                req.getRequestURI().contains(Constants.ACCOUNT_ACTIVATION_PAGE) || req.getRequestURI().contains(Constants.FORGOT_PASS_PAGE);
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        return req.getSession().getAttribute(AUTH_KEY) != null;
    }

    private boolean hasPermission(HttpServletRequest req) {

        if (!isLoggedIn(req)) {
            return false;
        }

        AuthenticationBean auth = (AuthenticationBean) req.getSession().getAttribute("authenticationBean");
        User u = auth.getLoggedUser();

        return u != null;
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


