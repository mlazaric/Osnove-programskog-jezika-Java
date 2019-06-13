package hr.fer.zemris.java.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Context listener used to store the current time in milliseconds to "startTime" which is used for calculating how
 * long the server has been running for.
 *
 * @author Marko Lazarić
 */
@WebListener
public class StartTimeContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("startTime", System.currentTimeMillis());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
