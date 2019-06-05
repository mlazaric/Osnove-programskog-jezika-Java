package hr.fer.zemris.java.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

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
