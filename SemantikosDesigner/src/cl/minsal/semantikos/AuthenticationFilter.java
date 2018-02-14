package cl.minsal.semantikos;

import cl.minsal.semantikos.model.users.User;
import cl.minsal.semantikos.users.AuthenticationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.ws.rs.core.HttpHeaders.CACHE_CONTROL;
import static javax.ws.rs.core.HttpHeaders.EXPIRES;


/**
 * @author Francisco Mendez on 19-05-2016.
 */
public class AuthenticationFilter implements Filter {

    static private final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getSession() == null) {
            res.sendRedirect(req.getContextPath() + Constants.LOGIN_PAGE); // No logged-in user found, so redirect to login page.
        } else {
            if (req.getRequestURI().contains(Constants.ROOT) || req.getRequestURI().contains(Constants.LOGIN_PAGE) || req.getRequestURI().contains(Constants.LOGOUT_PAGE) ||
                    req.getRequestURI().contains(Constants.ERRORS_FOLDER) || req.getRequestURI().contains(Constants.ACCOUNT_ACTIVATION_PAGE) ||
                    req.getRequestURI().contains(Constants.FORGOT_PASS_PAGE) || hasPermission(req)) {

                /*
                if(req.getRequestURI().contains(Constants.LOGOUT_PAGE)) {
                    req.getSession().invalidate();
                    res.sendRedirect(req.getContextPath() + Constants.LOGIN_PAGE);
                }
                */

                res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
                res.setDateHeader("Expires", 0);
                chain.doFilter(req, res);
            }
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


