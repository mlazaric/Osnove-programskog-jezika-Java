package hr.fer.zemris.java.tecaj_13.console;

import java.util.Date;
import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Example1 {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.blog");  

		// 1. korak - stvaranje novog blog zapisa...
		// -----------------------------------------
		System.out.println("Dodajem blog zapis.");
		BlogEntry blogEntry = dodajZapis(emf);
		System.out.println("Dodajem blog zapis - gotovo.");
		
		Long blogEntryID = blogEntry.getId();
		
		// 2. korak - dodavanje dva komentara...
		// -----------------------------------------
		System.out.println("Dodajem komentar.");
		dodajKomentar(emf, blogEntryID, "Blog ti je super!");
		System.out.println("Dodajem komentar - gotovo.");
		
		try { Thread.sleep(1000); } catch(InterruptedException ex) {}
		
		System.out.println("Dodajem komentar.");
		dodajKomentar(emf, blogEntryID, "Vau!");
		System.out.println("Dodajem komentar - gotovo.");
		
		try { Thread.sleep(1000); } catch(InterruptedException ex) {}
		
		System.out.println("Dodajem komentar.");
		dodajKomentar(emf, blogEntryID, "Još jedan komentar.");
		System.out.println("Dodajem komentar - gotovo.");

		System.out.println("Primjer upita.");
		primjerUpita(emf, blogEntryID);
		System.out.println("Primjer upita - gotovo.");

		try { Thread.sleep(1000); } catch(InterruptedException ex) {}
		
		System.out.println("Primjer upita 2.");
		primjerUpita2(emf, blogEntryID);
		System.out.println("Primjer upita 2 - gotovo.");
		
		emf.close();
	}

	public static BlogEntry dodajZapis(EntityManagerFactory emf) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		BlogEntry blogEntry = new BlogEntry();
		blogEntry.setCreatedAt(new Date());
		blogEntry.setLastModifiedAt(blogEntry.getCreatedAt());
		blogEntry.setTitle("Moj prvi blog");
		blogEntry.setText("Ovo je moj prvi blog zapis.");
		
		em.persist(blogEntry);
		
		em.getTransaction().commit();
		em.close();
		
		return blogEntry;
	}
	
	public static void dodajKomentar(EntityManagerFactory emf, Long blogEntryID, String message) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryID);
		
		BlogComment blogComment = new BlogComment();
		blogComment.setUsersEMail("ivica@host.com");
		blogComment.setPostedOn(new Date());
		blogComment.setMessage(message);
		blogComment.setBlogEntry(blogEntry);
		
		em.persist(blogComment);

		blogEntry.getComments().add(blogComment);
		
		em.getTransaction().commit();
		em.close();
	}
	
	/**
	 * Uporaba jezika JPA-QL
	 * @param emf
	 * @param blogEntryID
	 */
	private static void primjerUpita(EntityManagerFactory emf, Long blogEntryID) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryID);

		@SuppressWarnings("unchecked")
		List<BlogComment> comments = 
				(List<BlogComment>)em.createQuery("select b from BlogComment as b where b.blogEntry=:be")
					.setParameter("be", blogEntry)
					.getResultList();
		
		for(BlogComment bc : comments) {
			System.out.println("Blog ["+bc.getBlogEntry().getTitle()+"] ima komentar: ["+bc.getMessage()+"]");
		}
		
		em.getTransaction().commit();
		em.close();
	}

	/**
	 * Uporaba jezika JPA-QL
	 * @param emf
	 * @param blogEntryID
	 */
	private static void primjerUpita2(EntityManagerFactory emf, Long blogEntryID) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		BlogEntry blogEntry = em.find(BlogEntry.class, blogEntryID);

		@SuppressWarnings("unchecked")
		List<BlogComment> comments = 
				(List<BlogComment>)em.createNamedQuery("BlogEntry.upit1")
					.setParameter("be", blogEntry)
					.setParameter("when", new Date(new Date().getTime() - 2000))
					.getResultList();
		
		for(BlogComment bc : comments) {
			System.out.println("Blog ["+bc.getBlogEntry().getTitle()+"] ima komentar: ["+bc.getMessage()+"]");
		}
		
		em.getTransaction().commit();
		em.close();
	}

}