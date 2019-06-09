package hr.fer.zemris.java.voting.dao;

import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollOption;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 *
 * @author marcupic
 *
 */
public interface DAO {

	void setUp() throws DAOException;

	void savePoll(Poll poll) throws DAOException;

	void savePollOption(PollOption pollOption) throws DAOException;

}