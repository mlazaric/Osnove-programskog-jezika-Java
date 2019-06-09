package hr.fer.zemris.java.p12.dao;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 *
 * @author marcupic
 *
 */
public interface DAO {

	/**
	 * Dohvaća sve postojeće unose u bazi, ali puni samo dva podatka: id i title.
	 *
	 * @return listu unosa
	 * @throws DAOException u slučaju pogreške
	 */
	public List<Unos> dohvatiOsnovniPopisUnosa() throws DAOException;

	/**
	 * Dohvaća Unos za zadani id. Ako unos ne postoji, vraća <code>null</code>.
	 * 
	 * @param id
	 * @return
	 * @throws DAOException
	 */
	public Unos dohvatiUnos(long id) throws DAOException;

}