package hr.fer.zemris.java.blog.dao;

import hr.fer.zemris.java.blog.dao.jpa.JPADAOImpl;

/**
 * A provider for the {@link DAO} which is modeled as a singleton.
 */
public class DAOProvider {

    /**
     * The singleton instance.
     */
    private static DAO dao = new JPADAOImpl();

    /**
     * Returns the singleton instance of the {@link DAO}.
     *
     * @return the singleton instance of the {@link DAO}
     */
    public static DAO getDAO() {
        return dao;
    }

}