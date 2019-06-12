package hr.fer.zemris.java.voting;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import hr.fer.zemris.java.voting.dao.DAOProvider;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;

/**
 * A web listener which initialises a data source pool for use by servlets and filters.
 *
 * @author Marko Lazarić
 */
@WebListener
public class DataSourceInitialisationListener implements ServletContextListener {

	/**
	 * The path to the file defining the settings for the database connection.
	 */
	private static final String DATABASE_CONNECTION_SETTINGS_PATH = "/WEB-INF/dbsettings.properties";

	/**
	 * The key to use for storing the data source poool in the servlet context.
	 */
	public static final String DBPOOL = "hr.fer.zemris.dbpool";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
        String connectionURL = getConnectionUrl(sce.getServletContext());

        ComboPooledDataSource cpds = new ComboPooledDataSource();


		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		}
		catch (PropertyVetoException e1) {
			throw new RuntimeException("Error occurred while initialising the database pool.", e1);
		}

		cpds.setJdbcUrl(connectionURL);

		sce.getServletContext().setAttribute(DBPOOL, cpds);

		ConnectionSetterFilter.setConnection(sce.getServletContext());

		DAOProvider.getDao().setUp();

		ConnectionSetterFilter.closeConnection();
	}

	/**
	 * Calculates the connection url using the settings from {@link #DATABASE_CONNECTION_SETTINGS_PATH} file.
	 *
	 * @param context the context to use to parse the relative file path
	 * @return the calculated connection url
	 *
	 * @throws RuntimeException if the database settings file is not readable or does not exist
	 */
    private String getConnectionUrl(ServletContext context) {
        Path settings = Paths.get(context.getRealPath(DATABASE_CONNECTION_SETTINGS_PATH));

        if (!Files.exists(settings)) {
            throw new RuntimeException("Database settings file does not exist.");
        }

        Properties properties = new Properties();

        try (InputStream is = Files.newInputStream(settings)) {
            properties.load(is);
        }
        catch (IOException e) {
            throw new RuntimeException("Error occurred while reading database settings.", e);
        }

        return "jdbc:derby://" + getPropertyOrThrow(properties, "host") +
                ":" + getPropertyOrThrow(properties, "port") +
                "/" + getPropertyOrThrow(properties, "name") +
                ";user=" + getPropertyOrThrow(properties, "user") +
                ";password=" + getPropertyOrThrow(properties, "password");
    }

    private String getPropertyOrThrow(Properties properties, String propertyName) {
		String property = properties.getProperty(propertyName);

		if (property == null) {
			throw new RuntimeException("The '" + propertyName + "' has not been set.");
		}

		return property;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute(DBPOOL);

		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}