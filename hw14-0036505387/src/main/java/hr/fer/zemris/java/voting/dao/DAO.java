package hr.fer.zemris.java.voting.dao;

import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollOption;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 *
 * @author marcupic
 *
 */
public interface DAO {

	void setUp() throws DAOException;

	void createPoll(Poll poll) throws DAOException;
	void updatePoll(Poll poll) throws DAOException;

	void createPollOption(PollOption pollOption) throws DAOException;
	void updatePollOption(PollOption pollOption) throws DAOException;

	List<Poll> listPolls() throws DAOException;
	List<PollOption> listPollOptions() throws DAOException;

	default Poll getPollById(int id) throws DAOException {
		return listPolls().stream()
				          .filter(poll -> poll.getId() == id)
				          .findFirst()
				          .orElse(null);
	}

	default PollOption getPollOptionById(int id) throws DAOException {
		return listPollOptions().stream()
						 	    .filter(poll -> poll.getId() == id)
								.findFirst()
								.orElse(null);
	}

	default List<PollOption> getPollOptionsByPollId(int pollId) throws DAOException {
		return listPollOptions().stream()
								.filter(option -> option.getPollId() == pollId)
								.collect(Collectors.toList());
	}

	default void voteFor(int pollOptionId) throws DAOException {
		PollOption pollOption = getPollOptionById(pollOptionId);

		if (pollOption == null) {
			throw new DAOException("Cannot vote for poll option with id '" + pollOptionId + "', it does not exist.");
		}

		pollOption.setVoteCount(pollOption.getVoteCount() + 1);

		pollOption.save();
	}

	default List<PollOption> getBestVotedOptions(int pollId) throws DAOException {
		List<PollOption> options = getPollOptionsByPollId(pollId);

		int maxVotes = options.stream()
				           	  .mapToInt(PollOption::getVoteCount)
				           	  .max()
				           	  .orElse(0);

		return options.stream()
				   	  .filter(opt -> opt.getVoteCount() == maxVotes)
				   	  .collect(Collectors.toList());

	}

}