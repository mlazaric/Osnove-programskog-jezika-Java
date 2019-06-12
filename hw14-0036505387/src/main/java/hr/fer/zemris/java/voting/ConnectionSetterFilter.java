package hr.fer.zemris.java.voting;

import hr.fer.zemris.java.voting.dao.sql.SQLConnectionProvider;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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

	public static void setConnection(ServletContext context) {
		DataSource ds = (DataSource) context.getAttribute(DataSourceInitialisationListener.DBPOOL);
		Connection con = null;

		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException("Baza podataka nije dostupna.", e);
		}

		SQLConnectionProvider.setConnection(con);
	}

	public static void closeConnection() {
		Connection con = SQLConnectionProvider.getConnection();
		SQLConnectionProvider.setConnection(null);

		try {
			con.close();
		} catch (SQLException ignorable) {
		}
	}

}
