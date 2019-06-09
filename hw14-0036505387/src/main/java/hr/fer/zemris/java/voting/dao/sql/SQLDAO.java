package hr.fer.zemris.java.voting.dao.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.voting.dao.DAO;
import hr.fer.zemris.java.voting.dao.DAOException;
import hr.fer.zemris.java.voting.model.Poll;
import hr.fer.zemris.java.voting.model.PollOption;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova konkretna
 * implementacija očekuje da joj veza stoji na raspolaganju preko
 * {@link SQLConnectionProvider} razreda, što znači da bi netko prije no što
 * izvođenje dođe do ove točke to trebao tamo postaviti. U web-aplikacijama
 * tipično rješenje je konfigurirati jedan filter koji će presresti pozive
 * servleta i prije toga ovdje ubaciti jednu vezu iz connection-poola, a po
 * zavrsetku obrade je maknuti.
 * 
 * @author marcupic
 */
public class SQLDAO implements DAO {

	private static final String CREATE_POLLS_TABLE =
			"CREATE TABLE Polls\n" +
				" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
				" title VARCHAR(150) NOT NULL,\n" +
				" message CLOB(2048) NOT NULL\n" +
			")";

	private static final String CREATE_POLL_OPTIONS_TABLE =
			"CREATE TABLE PollOptions\n" +
				" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" +
				" optionTitle VARCHAR(100) NOT NULL,\n" +
				" optionLink VARCHAR(150) NOT NULL,\n" +
				" pollID BIGINT,\n" +
				" votesCount BIGINT,\n" +
				" FOREIGN KEY (pollID) REFERENCES Polls(id)\n" +
			")";

	private static final String INSERT_INTO_POLLS =
			"INSERT INTO Polls\n" +
					"(title, message)\n" +
					"VALUES\n" +
					"(?, ?)";

	private static final String INSERT_INTO_POLL_OPTIONS =
			"INSERT INTO PollOptions\n" +
					"(optionTitle, optionLink, pollID, votesCount)\n" +
					"VALUES\n" +
					"(?, ?, ?, ?)";

	private static final String NUMBER_OF_POLLS =
			"SELECT COUNT(*)\n" +
					"FROM Polls";

	@Override
	public void setUp() throws DAOException {
		createTableIfItDoesNotExist(CREATE_POLLS_TABLE);
		createTableIfItDoesNotExist(CREATE_POLL_OPTIONS_TABLE);

		if (pollsIsEmpty()) {
			populateTables();
		}
	}

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

	@Override
	public void savePoll(Poll poll) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		if (poll.getId() == -1) {
			try (PreparedStatement pst = con.prepareStatement(INSERT_INTO_POLLS, Statement.RETURN_GENERATED_KEYS)) {
				pst.setString(1, poll.getTitle());
				pst.setString(2, poll.getMessage());

				pst.executeUpdate();

				try (ResultSet results = pst.getGeneratedKeys()) {
					results.next();

					poll.setId(results.getInt(1));
					System.out.println(poll.getId());
				}
			} catch (SQLException e) {
				throw new DAOException("Error occurred while creating poll.", e);
			}
		}
	}

	@Override
	public void savePollOption(PollOption pollOption) throws DAOException {
		Connection con = SQLConnectionProvider.getConnection();

		if (pollOption.getId() == -1) {
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
	}

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

}
