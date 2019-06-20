package hr.fer.zemris.java.blog.dao.jpa;

import hr.fer.zemris.java.blog.dao.DAO;
import hr.fer.zemris.java.blog.dao.DAOException;
import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;

import javax.persistence.Query;
import java.util.List;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);

		return blogEntry;
	}

	@Override
	public BlogUser getBlogUser(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
	}

	@Override
	public boolean nicknameExists(String nick) throws DAOException {
		Query query = JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.nickExists");

		query.setParameter("nick", nick);

		List<? extends Number> list = query.getResultList();

		return list.get(0).longValue() > 0;
	}

	@Override
	public void persistUser(BlogUser user) throws DAOException {
		JPAEMProvider.getEntityManager().persist(user);
	}

	@Override
	public void persistComment(BlogComment comment) {
		JPAEMProvider.getEntityManager().persist(comment);
	}

	@Override
	public void persistEntry(BlogEntry entry) {
		JPAEMProvider.getEntityManager().persist(entry);
	}

	@Override
	public List<BlogUser> listUsers() throws DAOException {
		return JPAEMProvider.getEntityManager()
				            .createNamedQuery("BlogUser.allUsers", BlogUser.class)
						    .getResultList();
	}

	@Override
	public BlogUser getUserByNickAndPasswordHash(String nick, String passwordHash) throws DAOException {
		List<BlogUser> users = JPAEMProvider.getEntityManager()
											.createNamedQuery("BlogUser.selectByNick", BlogUser.class)
											.setParameter("nick", nick)
											.getResultList();

		if (users.size() == 0) {
			return null;
		}

		if (users.get(0).getPasswordHash().equals(passwordHash)) {
			return users.get(0);
		}

		return null;
	}

	@Override
	public List<BlogEntry> listEntriesForUser(String nick) throws DAOException {
		return JPAEMProvider.getEntityManager()
				     		.createNamedQuery("BlogEntry.entriesByUser", BlogEntry.class)
				     		.setParameter("nick", nick)
					 		.getResultList();
	}

}