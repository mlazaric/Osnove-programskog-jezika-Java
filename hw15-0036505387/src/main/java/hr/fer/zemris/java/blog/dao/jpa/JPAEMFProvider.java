package hr.fer.zemris.java.blog.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * A provider for the {@link EntityManagerFactory}.
 */
public class JPAEMFProvider {

    /**
     * The stored {@link EntityManagerFactory}.
     */
    public static EntityManagerFactory emf;

    /**
     * Returns the stored {@link EntityManagerFactory}.
     *
     * @return the stored {@link EntityManagerFactory}
     */
    public static EntityManagerFactory getEmf() {
        return emf;
    }

    /**
     * Sets the stored {@link EntityManagerFactory} to the new value.
     *
     * @param emf the new value of {@link #emf}
     */
    public static void setEmf(EntityManagerFactory emf) {
        JPAEMFProvider.emf = emf;
    }
}