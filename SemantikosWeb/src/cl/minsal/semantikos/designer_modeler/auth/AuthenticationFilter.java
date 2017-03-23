package cl.minsal.semantikos.designer_modeler.auth;

import cl.minsal.semantikos.designer_modeler.Constants;
import cl.minsal.semantikos.model.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Francisco Mendez on 19-05-2016.
 */
public class AuthenticationFilter implements Filter {

    static private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getRequestURI().contains(Constants.LOGIN_PAGE) || req.getRequestURI().contains(Constants.ERRORS_FOLDER) ||
            req.getRequestURI().contains(Constants.ACCOUNT_ACTIVATION_PAGE) || hasPermission(req)) {
            logger.debug("Request válido, se deja continuar: " + req);
            chain.doFilter(request, response);
        }

        /* Perdió la sesión o está tratando de conectarse sin haberse logueado */
        else if (!isLoggedIn(req)) {
            logger.debug("Intento de acceso sin sesión: " + req);
            res.sendRedirect(req.getContextPath() + "/" + Constants.VIEWS_FOLDER + "/" + Constants.LOGIN_PAGE + "?viewExpired=true&originalURI=" + req.getRequestURI());
        }

        /* No tiene permiso para acceder a la pagina solicitada */
        else if (!hasPermission(req)) {
            logger.debug("Intento de acceso sin sesión: " + req);
            res.sendRedirect(req.getContextPath() + "/" + Constants.VIEWS_FOLDER + "/" + Constants.ERRORS_FOLDER + "/" + Constants.AUTH_ERROR_PAGE);
        }

        /* Otros casos que nunca deberían darse */
        else {
            logger.debug("Un caso que nunca debiera darse: " + req);
            res.sendRedirect(req.getContextPath() + "/" + Constants.VIEWS_FOLDER + "/" + Constants.LOGIN_PAGE);
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        return req.getSession().getAttribute(AuthenticationBean.AUTH_KEY) != null;
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
    }


    public void init(FilterConfig arg0) throws ServletException {
    }


}


