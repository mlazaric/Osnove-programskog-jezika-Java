package hr.fer.zemris.java.voting;

import java.util.Objects;

/**
 * A java bean object which models a single band represented by its id, name and a representative song link.
 * It is ordered by the unique identifier in ascending order.
 *
 * @author Marko Lazarić
 */
public class Band implements Comparable<Band> {

    /**
     * The unique identifier for the band.
     */
    private final int id;

    /**
     * The name of the band.
     */
    private final String name;

    /**
     * The representative song link for this band.
     */
    private final String song;

    /**
     * Creates a new {@link Band} with the specified parameters.
     *
     * @param id the unique identifier for the band
     * @param name the name of the band
     * @param song the representative song link for this band
     *
     * @throws NullPointerException if the name or the song parameters is null
     */
    public Band(int id, String name, String song) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name of band cannot be null.");
        this.song = Objects.requireNonNull(song, "Representative song of band cannot be null.");
    }

    /**
     * Returns the representative song link for this band.
     *
     * @return the representative song link for this band
     */
    public String getSong() {
        return song;
    }

    /**
     * Returns the name of the band.
     *
     * @return the name of the band
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the unique identifier for the band.
     *
     * @return the unique identifier for the band
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Band band = (Band) o;

        return id == band.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Band other) {
        return Integer.compare(this.id, other.id);
    }

}
