package hr.fer.zemris.java.blog.dao.jpa;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * A simple filter which closes the {@link javax.persistence.EntityManager} stored by {@link JPAEMProvider} after a
 * request has been responded to.
 */
@WebFilter("/servleti/*")
public class JPAFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            JPAEMProvider.close();
        }
    }

    @Override
    public void destroy() {
    }

}