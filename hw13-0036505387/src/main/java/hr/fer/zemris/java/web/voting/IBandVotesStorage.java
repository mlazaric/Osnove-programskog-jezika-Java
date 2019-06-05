package hr.fer.zemris.java.web.voting;

public interface IBandVotesStorage {

    void voteFor(int id);

    default void voteFor(Band band) {
        voteFor(band.getId());
    }

    Iterable<BandVoteCount> sortedByVoteCount(IBandDefinitionStorage bandDefinition);

    Iterable<BandVoteCount> bestVoted(IBandDefinitionStorage bandDefinition);

}
