package hr.fer.zemris.java.blog.dao.jpa;

import hr.fer.zemris.java.blog.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * A provider for the {@link EntityManager} which is thread safe and each thread is allocated its own {@link EntityManager}
 * when first calling {@link #getEntityManager()}.
 */
public class JPAEMProvider {

    /**
     * The stored {@link EntityManager} local to their thread.
     */
    private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

    /**
     * Returns the {@link EntityManager} stored for this thread or creates a new one if it is null.
     *
     * @return the {@link EntityManager}
     */
    public static EntityManager getEntityManager() {
        EntityManager em = locals.get();
        if (em == null) {
            em = JPAEMFProvider.getEmf().createEntityManager();
            em.getTransaction().begin();
            locals.set(em);
        }
        return em;
    }

    /**
     * Closes the {@link EntityManager} for this thread.
     *
     * @throws DAOException if an error is encountered while closing the {@link EntityManager}
     */
    public static void close() throws DAOException {
        EntityManager em = locals.get();
        if (em == null) {
            return;
        }
        DAOException dex = null;
        try {
            em.getTransaction().commit();
        } catch (Exception ex) {
            dex = new DAOException("Unable to commit transaction.", ex);
        }
        try {
            em.close();
        } catch (Exception ex) {
            if (dex != null) {
                dex = new DAOException("Unable to close entity manager.", ex);
            }
        }
        locals.remove();
        if (dex != null) throw dex;
    }

}