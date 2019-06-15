package hr.fer.zemris.java.voting.dao;

import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollOption;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface towards the data persistence layer.
 * Allows for implementing certain default methods more efficiently (for example, it is possible to implement {@link #voteFor(int)}
 * with a single query).
 *
 * @author Marko Lazarić
 *
 */
public interface DAO {

	/**
	 * Called during set up of the data persistence layer. Can be used to check if the relevant files are created (in case of
	 * file storage) or tables (in case of database storage).
	 *
	 * @throws DAOException if an error occurs while setting up the data persistence layer
	 */
	void setUp() throws DAOException;

	/**
	 * Creates a {@link Poll} in the data persistence layer with the specified data and sets the ID of the argument to the
	 * ID of the newly created poll.
	 *
	 * @param poll the poll to create in the data persistence layer
	 *
	 * @throws DAOException if an error occurs while creating the poll
	 */
	void createPoll(Poll poll) throws DAOException;


	/**
	 * Updates the {@link Poll} with the same ID in the data persistence layer to the values of the argument.
	 *
	 * @param poll the poll to update in the data persistence layer
	 *
	 * @throws DAOException if an error occurs while updating the poll
	 */
	void updatePoll(Poll poll) throws DAOException;

	/**
	 * Creates a {@link PollOption} in the data persistence layer with the specified data and sets the ID of the argument to the
	 * ID of the newly created poll option.
	 *
	 * @param pollOption the poll option to create in the data persistence layer
	 *
	 * @throws DAOException if an error occurs while creating the poll option
	 */
	void createPollOption(PollOption pollOption) throws DAOException;

	/**
	 * Updates the {@link PollOption} with the same ID in the data persistence layer to the values of the argument.
	 *
	 * @param pollOption the poll option to update in the data persistence layer
	 *
	 * @throws DAOException if an error occurs while updating the poll option
	 */
	void updatePollOption(PollOption pollOption) throws DAOException;


	/**
	 * List all polls currently stored in the data persistence layer.
	 *
	 * @return a list of all the polls stored in the data persistence layer
	 *
	 * @throws DAOException if an error occurs while listing the polls
	 */
	List<Poll> listPolls() throws DAOException;

	/**
	 * List all poll options currently stored in the data persistence layer.
	 *
	 * @return a list of all the poll options stored in the data persistence layer
	 *
	 * @throws DAOException if an error occurs while listing the poll options
	 */
	List<PollOption> listPollOptions() throws DAOException;

	/**
	 * Returns the {@link Poll} with the specified ID, or null if no such poll exists.
	 *
	 * @param id the unique identifier of the poll to find
	 * @return the {@link Poll} with the specified ID or null if no such poll exists
	 *
	 * @throws DAOException if an error occurs while selecting the poll
	 */
	default Poll getPollById(int id) throws DAOException {
		return listPolls().stream()
				          .filter(poll -> poll.getId() == id)
				          .findFirst()
				          .orElse(null);
	}

	/**
	 * Returns the {@link PollOption} with the specified ID, or null if no such poll option exists.
	 *
	 * @param id the unique identifier of the poll option to find
	 * @return the {@link PollOption} with the specified ID or null if no such poll option exists
	 *
	 * @throws DAOException if an error occurs while selecting the poll option
	 */
	default PollOption getPollOptionById(int id) throws DAOException {
		return listPollOptions().stream()
						 	    .filter(poll -> poll.getId() == id)
								.findFirst()
								.orElse(null);
	}

	/**
	 * Returns a list of all the {@link PollOption}s associated with the specified poll.
	 *
	 * @param pollId the unique identifier of the poll
	 * @return a list of all the {@link PollOption}s associated with the specified poll
	 *
	 * @throws DAOException if an error occurs while selecting the poll option
	 */
	default List<PollOption> getPollOptionsByPollId(int pollId) throws DAOException {
		return listPollOptions().stream()
								.filter(option -> option.getPollId() == pollId)
								.collect(Collectors.toList());
	}

	/**
	 * Increments the number of votes the poll option received.
	 *
	 * @param pollOptionId the unique identifier of the poll option
	 *
	 * @throws DAOException if an error occurs while incrementing the number of votes
	 */
	default void voteFor(int pollOptionId) throws DAOException {
		PollOption pollOption = getPollOptionById(pollOptionId);

		if (pollOption == null) {
			throw new DAOException("Cannot vote for poll option with id '" + pollOptionId + "', it does not exist.");
		}

		pollOption.setVoteCount(pollOption.getVoteCount() + 1);

		pollOption.save();
	}

	/**
	 * Returns a list of the {@link PollOption}s with the highest number of votes for the specified poll.
	 *
	 * @param pollId the unique identifier of the poll
	 * @return a list of the {@link PollOption}s with the highest number of votes for the specified poll
	 *
	 * @throws DAOException if an error occurs while selecting the best voted options
	 */
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