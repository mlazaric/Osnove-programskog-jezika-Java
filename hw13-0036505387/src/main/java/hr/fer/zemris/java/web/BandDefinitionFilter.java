package hr.fer.zemris.java.web;

import hr.fer.zemris.java.web.voting.FileBandDefinitionStorage;
import hr.fer.zemris.java.web.voting.IBandDefinitionStorage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * A web filter which loads the band definitions for the pages which use it, so it easier to change the implementation
 * (for example switching from a file to a database) and stores it as a "bandDefinition" attribute.
 *
 * @author Marko Lazarić
 */
@WebFilter(servletNames = { "glasanje-grafika", "glasanje-rezultati", "glasanje", "glasanje-xls" })
public class BandDefinitionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // We could've loaded it once during init, but let's assume it's big enough that we do not want to keep it in memory constantly
        IBandDefinitionStorage bandDefinition = new FileBandDefinitionStorage(
                request.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));

        request.setAttribute("bandDefinition", bandDefinition);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
