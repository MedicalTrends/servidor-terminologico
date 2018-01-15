package browser;

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

        ((HttpServletResponse) response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        ((HttpServletResponse) response).setHeader("Pragma", "no-cache"); // HTTP 1.0.
        ((HttpServletResponse) response).setHeader("Expires", "0");

        if(req.getSession().isNew()) {
            req.getSession().invalidate();
        }

        chain.doFilter(request, response);
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


