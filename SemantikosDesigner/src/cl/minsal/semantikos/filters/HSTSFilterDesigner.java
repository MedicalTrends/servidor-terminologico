package cl.minsal.semantikos.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HSTSFilterDesigner implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) res;

        //if (req.isSecure()) {
            resp.setHeader("Strict-Transport-Security", "max-age=31622400; includeSubDomains");
        //}

        resp.addHeader("x-frame-options", "deny");

        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}


