package hr.fer.zemris.java.web.voting;

import java.util.Objects;

public class BandVoteCount implements Comparable<BandVoteCount> {

    private final Band band;
    private final int voteCount;

    public BandVoteCount(Band band, int voteCount) {
        this.band = Objects.requireNonNull(band, "Band cannot be null.");
        this.voteCount = voteCount;
    }

    public int getVoteCount() {
        return voteCount;
    }

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
