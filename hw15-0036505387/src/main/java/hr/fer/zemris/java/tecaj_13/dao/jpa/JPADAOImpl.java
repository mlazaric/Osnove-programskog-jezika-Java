package hr.fer.zemris.java.tecaj_13.dao.jpa;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

import javax.persistence.Query;
import java.util.List;

public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
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

}