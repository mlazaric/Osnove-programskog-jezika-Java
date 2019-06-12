package hr.fer.zemris.java.voting;

import hr.fer.zemris.java.voting.dao.sql.SQLConnectionProvider;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A filter which creates a connection for every servlet specified by the urlPatterns attribute.
 * It also closes the connection after the servlet is done with it.
 */
@WebFilter(filterName = "f1", urlPatterns = { "/servleti/*" })
public class ConnectionSetterFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		setConnection(request.getServletContext());

		try {
			chain.doFilter(request, response);
		} finally {
			closeConnection();
		}
	}

	/**
	 * Gets a connection from the pool and sets it using {@link SQLConnectionProvider#setConnection(Connection)}.
	 *
	 * @param context the context used to get the data source
	 *
	 * @throws RuntimeException if the database is not available
	 */
	public static void setConnection(ServletContext context) {
		DataSource ds = (DataSource) context.getAttribute(DataSourceInitialisationListener.DBPOOL);
		Connection con = null;

		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Database is not available.", e);
		}

		SQLConnectionProvider.setConnection(con);
	}

	/**
	 * Closes the connection and sets it to null.
	 */
	public static void closeConnection() {
		Connection con = SQLConnectionProvider.getConnection();
		SQLConnectionProvider.setConnection(null);

		try {
			con.close();
		} catch (SQLException ignorable) {
		}
	}

}
