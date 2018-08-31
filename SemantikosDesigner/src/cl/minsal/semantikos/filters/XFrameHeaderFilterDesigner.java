package cl.minsal.semantikos.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by des01c7 on 29-03-18.
 */
public class XFrameHeaderFilterDesigner implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        ((HttpServletResponse) resp).addHeader("x-frame-options", "deny");
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }
}
