package hr.fer.zemris.java.voting;

import java.util.Objects;

/**
 * A java bean object representing a band and the number of votes it got.
 * It is ordered by the vote count in descending order and then by the band's order.
 *
 * @author Marko Lazarić
 */
public class BandVoteCount implements Comparable<BandVoteCount> {

    /**
     * The band whose vote count this object represents.
     */
    private final Band band;

    /**
     * The number of votes the band got.
     */
    private final int voteCount;

    /**
     * Creates a new {@link BandVoteCount} with the given arguments.
     *
     * @param band the band whose vote count this object represents
     * @param voteCount the number of votes the band got
     *
     * @throws NullPointerException if band is null
     */
    public BandVoteCount(Band band, int voteCount) {
        this.band = Objects.requireNonNull(band, "Band cannot be null.");
        this.voteCount = voteCount;
    }

    /**
     * Returns the number of votes the band got.
     *
     * @return the number of votes the band got
     */
    public int getVoteCount() {
        return voteCount;
    }

    /**
     * Returns the band whose vote count this object represents.
     *
     * @return the band whose vote count this object represents
     */
    public Band getBand() {
        return band;
    }

    @Override
    public int compareTo(BandVoteCount other) {
        int result = Integer.compare(this.voteCount, other.voteCount);

        if (result != 0) {
            // We want to reverse the order, so the higher voted bands are higher
            return -result;
        }

        return band.compareTo(other.band);
    }

}
