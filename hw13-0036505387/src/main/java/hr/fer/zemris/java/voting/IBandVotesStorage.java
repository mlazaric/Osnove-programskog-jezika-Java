package hr.fer.zemris.java.voting;

/**
 * An interface which models the storage of votes and interacting with the votes.
 *
 * It is used to decouple the logic from a concrete implementation. For example,
 * instead of a file storage, it could be switched to a database storage.
 *
 * @author Marko Lazarić
 */
public interface IBandVotesStorage {

    /**
     * Adds a single vote for the band with the specified unique identifier.
     *
     * @param id the band's unique identifier
     */
    void voteFor(int id);

    /**
     * Adds a single vote for the band.
     *
     * @param band the band to add a vote for
     */
    default void voteFor(Band band) {
        voteFor(band.getId());
    }

    /**
     * Returns an iterable which iterates over the {@link BandVoteCount}s sorted by vote count in descending order.
     *
     * @param bandDefinition the definitions used for joining the attributes of the bands to their ids,
     *                       can be null if the implementation does not need it (for example, a database)
     * @return the described iterable
     */
    Iterable<BandVoteCount> sortedByVoteCount(IBandDefinitionStorage bandDefinition);

    /**
     * Returns an interable which iterates over the {@link BandVoteCount}s with the vote count equal to the maximum of
     * all vote counts.
     *
     * @param bandDefinition the definitions used for joining the attributes of the bands to their ids,
     *                       can be null if the implementation does not need it (for example, a database)
     * @return the described iterable
     */
    Iterable<BandVoteCount> bestVoted(IBandDefinitionStorage bandDefinition);

}
