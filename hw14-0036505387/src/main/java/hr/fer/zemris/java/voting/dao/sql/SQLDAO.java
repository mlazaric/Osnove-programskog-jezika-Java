package hr.fer.zemris.java.voting.dao.sql;

import hr.fer.zemris.java.voting.dao.DAO;
import hr.fer.zemris.java.voting.dao.DAOException;
import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollOption;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A concrete implementation of {@link DAO} using SQL for the data persistence. It assumes an available SQL connection
 * using {@link SQLConnectionProvider} and does not close the connection.
 * 
 * @author Marko Lazarić
 */
public class SQLDAO implements DAO {

	/**
	 * A query to create the Polls table.
	 */
	private static final String CREATE_POLLS_TABLE =
			"CREATE TABLE Polls\n" +
				" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
				" title VARCHAR(150) NOT NULL,\n" +
				" message CLOB(2048) NOT NULL\n" +
			")";

	/**
	 * A query to create the PollOptions table.
	 */
	private static final String CREATE_POLL_OPTIONS_TABLE =
			"CREATE TABLE PollOptions\n" +
				" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
				" optionTitle VARCHAR(100) NOT NULL,\n" +
				" optionLink VARCHAR(150) NOT NULL,\n" +
				" pollID BIGINT,\n" +
				" votesCount BIGINT,\n" +
				" FOREIGN KEY (pollID) REFERENCES Polls(id)\n" +
			")";

	/**
	 * A query to insert a new record into the Polls table.
	 */
	private static final String INSERT_INTO_POLLS =
			"INSERT INTO Polls\n" +
					"(title, message)\n" +
					"VALUES\n" +
					"(?, ?)";

	/**
	 * A query to update an existing record in the Polls table.
	 */
	private static final String UPDATE_POLL =
			"UPDATE Polls\n" +
					"SET\n" +
						"title = ?,\n" +
						"message = ?\n" +
					"WHERE id = ?";

	/**
	 * A query to insert a new record into the PollOptions table.
	 */
	private static final String INSERT_INTO_POLL_OPTIONS =
			"INSERT INTO PollOptions\n" +
					"(optionTitle, optionLink, pollID, votesCount)\n" +
					"VALUES\n" +
					"(?, ?, ?, ?)";

	/**
	 * A query to update an existing record in the PollOptions table.
	 */
	private static final String UPDATE_POLL_OPTION =
			"UPDATE PollOptions\n" +
					"SET\n" +
						"optionTitle = ?,\n" +
						"optionLink = ?,\n" +
						"pollId = ?,\n" +
						"votesCount = ?\n" +
					"WHERE id = ?";

	/**
	 * A query to get the number of records in the Polls table.
	 */
	private static final String NUMBER_OF_POLLS =
			"SELECT COUNT(*)\n" +
					"FROM Polls";

	/**
	 * A query to list all records from the Polls table.
	 */
	private static final String LIST_POLLS =
			"SELECT *\n" +
					"FROM Polls";

	/**
	 * A query to list all records from the PollOptions table.
	 */
	private static final String LIST_POLL_OPTIONS =
			"SELECT *\n" +
					"FROM PollOptions";

	/**
	 * A query to efficiently increment the number of votes for a record in the PollOptions table.
	 */
	private static final String INCREMENT_VOTES =
			"UPDATE PollOptions\n" +
					"SET votesCount = votesCount + 1\n" +
					"WHERE id = ?";

	/**
	 * A query to efficiently select a record from the Polls table using the unique identifier of the poll.
	 */
	private static final String SELECT_POLL_BY_ID =
			"SELECT *\n" +
					"FROM Polls\n" +
					"WHERE id = ?";

	@Override
	public void setUp() throws DAOException {
		createTableIfItDoesNotExist(CREATE_POLLS_TABLE);
		createTableIfItDoesNotExist(CREATE_POLL_OPTIONS_TABLE);

		if (pollsIsEmpty()) {
			populateTables();
		}
	}

	/**
	 * Creates the table if does not already exist. If it exists, it does nothing.
	 *
	 * @param tableQuery the query to create the table
	 *
	 * @throws DAOException if an error occurs while creating the table
	 */
	private void createTableIfItDoesNotExist(String tableQuery) {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(tableQuery)) {
			pst.executeUpdate();
		} catch (SQLException e) {
			// Table already exists:
			//     https://stackoverflow.com/questions/5866154/how-to-create-table-if-it-doesnt-exist-using-derby-db
			if (e.getSQLState().equals("X0Y32")) {
				return;
			}

			throw new DAOException("Error occurred while creating table.", e);
		}
	}

	/**
	 * Returns whether the Polls table contains any records.
	 *
	 * @return true if it contains 0 records,
	 *         false otherwise
	 *
	 * @throws DAOException if an error occurs while calculating the number of records in the Polls table
	 */
	private boolean pollsIsEmpty() {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(NUMBER_OF_POLLS)) {
			try (ResultSet results = pst.executeQuery()) {
				if (results == null || !results.next()) {
					return true;
				}
				else {
					return results.getInt(1) == 0;
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error occurred while counting polls.", e);
		}
	}

	/**
	 * Adds a couple test records to the Polls and PollOptions tables, used for testing.
	 */
	private void populateTables() {
		new Poll("Glasanje za omiljeni bend:", "Od sljedećih bendova, koji Vam je bend najdraži?",
				List.of(
						new PollOption("The Beatles", "https://www.youtube.com/watch?v=z9ypq6_5bsg"),
						new PollOption("The Platters", "https://www.youtube.com/watch?v=H2di83WAOhU"),
						new PollOption("The Beach Boys", "https://www.youtube.com/watch?v=2s4slliAtQU"),
						new PollOption("The Four Seasons", "https://www.youtube.com/watch?v=y8yvnqHmFds"),
						new PollOption("The Marcels", "https://www.youtube.com/watch?v=qoi3TH59ZEs"),
						new PollOption("The Everly Brothers", "https://www.youtube.com/watch?v=tbU3zdAgiX8"),
						new PollOption("The Mamas And The Papas", "https://www.youtube.com/watch?v=N-aK6JnyFmk")
				)).save();

		new Poll("Glasanje za omiljenu knjigu:", "Od sljedećih knjiga, koja Vam je knjiga najdraža?",
				List.of(
						new PollOption("Jostein Gaarder: Sofijin svijet", "https://www.goodreads.com/book/show/11250800-sophie-s-world"),
						new PollOption("Miroslav Krleža: Gospoda Glembajevi", "https://www.goodreads.com/book/show/6918013-gospoda-glembajevi"),
						new PollOption("Antoine de Saint Exupery: Mali princ", "https://www.goodreads.com/book/show/157993.The_Little_Prince"),
						new PollOption("Fjodor Mihajlovič Dostojevski: Zločin i kazna", "https://www.goodreads.com/book/show/7144.Crime_and_Punishment"),
						new PollOption("Margaret Atwood: Sluškinjina priča", "https://www.goodreads.com/book/show/12961964-the-handmaid-s-tale"),
						new PollOption("George Orwell: 1984", "https://www.goodreads.com/book/show/40961427-1984"),
						new PollOption("William Shakespeare: Hamlet", "https://www.goodreads.com/book/show/1420.Hamlet")
				)).save();
	}

	@Override
	public void createPoll(Poll poll) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(INSERT_INTO_POLLS, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, poll.getTitle());
			pst.setString(2, poll.getMessage());

			pst.executeUpdate();

			try (ResultSet results = pst.getGeneratedKeys()) {
				results.next();

				poll.setId(results.getInt(1));
			}
		} catch (SQLException e) {
			throw new DAOException("Error occurred while creating poll.", e);
		}
	}

	@Override
	public void updatePoll(Poll poll) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(UPDATE_POLL)) {
			pst.setString(1, poll.getTitle());
			pst.setString(2, poll.getMessage());
			pst.setInt(3, poll.getId());

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Error occurred while creating poll.", e);
		}
	}

	@Override
	public void createPollOption(PollOption pollOption) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(INSERT_INTO_POLL_OPTIONS, Statement.RETURN_GENERATED_KEYS)) {
			pst.setString(1, pollOption.getTitle());
			pst.setString(2, pollOption.getLink());
			pst.setInt(3, pollOption.getPollId());
			pst.setInt(4, pollOption.getVoteCount());

			pst.executeUpdate();

			try (ResultSet results = pst.getGeneratedKeys()) {
				results.next();

				pollOption.setId(results.getInt(1));
			}
		} catch (SQLException e) {
			throw new DAOException("Error occurred while creating poll option.", e);
		}
	}

	@Override
	public void updatePollOption(PollOption pollOption) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(UPDATE_POLL_OPTION)) {
			pst.setString(1, pollOption.getTitle());
			pst.setString(2, pollOption.getLink());
			pst.setInt(3, pollOption.getPollId());
			pst.setInt(4, pollOption.getVoteCount());
			pst.setInt(5, pollOption.getId());

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Error occurred while creating poll option.", e);
		}
	}

	@Override
	public List<Poll> listPolls() throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		List<Poll> polls = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(LIST_POLLS)) {
			try (ResultSet results = pst.executeQuery()) {
				while (results != null && results.next()) {
					int id = results.getInt("id");
					String title = results.getString("title");
					String message = results.getString("message");

					polls.add(new Poll(id, title, message, null));
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error occurred while listing polls.", e);
		}

		return polls;
	}

	@Override
	public List<PollOption> listPollOptions() throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		List<PollOption> options = new ArrayList<>();

		try (PreparedStatement pst = con.prepareStatement(LIST_POLL_OPTIONS)) {
			try (ResultSet results = pst.executeQuery()) {
				while (results != null && results.next()) {
					int id = results.getInt("id");
					String title = results.getString("optionTitle");
					String link = results.getString("optionLink");
					int pollId = results.getInt("pollId");
					int votesCount = results.getInt("votesCount");

					options.add(new PollOption(id, title, link, pollId, votesCount));
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error occurred while listing poll options.", e);
		}

		return options;
	}

	@Override
	public void voteFor(int id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		try (PreparedStatement pst = con.prepareStatement(INCREMENT_VOTES)) {
			pst.setInt(1, id);

			pst.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("Error occurred while voting for poll option.", e);
		}
	}

	@Override
	public Poll getPollById(int id) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();
		Poll poll = null;

		try (PreparedStatement pst = con.prepareStatement(SELECT_POLL_BY_ID)) {
			pst.setInt(1, id);

			try (ResultSet results = pst.executeQuery()) {
				if (results != null && results.next()) {
					int pollId = results.getInt("id");
					String title = results.getString("title");
					String message = results.getString("message");

					poll = new Poll(pollId, title, message, null);
				}
			}
		} catch (SQLException e) {
			throw new DAOException("Error occurred getting poll by id.", e);
		}

		return poll;
	}
}
