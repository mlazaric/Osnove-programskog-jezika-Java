package hr.fer.zemris.java.web.voting;

import hr.fer.zemris.java.voting.FileBandVotesStorage;
import hr.fer.zemris.java.voting.IBandVotesStorage;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * A web filter which loads the vote storage for the pages which use it, so it easier to change the implementation
 * (for example switching from a file to a database) and stores it as a "votes" attribute.
 *
 * @author Marko Lazarić
 */
@WebFilter(servletNames = { "glasanje-glasaj", "glasanje-grafika", "glasanje-rezultati", "glasanje-xls" })
public class BandVotesFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // We could've loaded it once during init, but let's assume it's big enough that we do not want to keep it in memory constantly
        IBandVotesStorage votes = new FileBandVotesStorage(
                request.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));

        request.setAttribute("votes", votes);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
