package hr.fer.zemris.java.blog.dao.jpa;

import javax.persistence.EntityManagerFactory;

public class JPAEMFProvider {

	public static EntityManagerFactory emf;
	
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}