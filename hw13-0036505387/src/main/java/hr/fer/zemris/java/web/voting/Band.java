package hr.fer.zemris.java.web.voting;

import java.util.Objects;

public class Band implements Comparable<Band> {

    private final int id;
    private final String name;
    private final String song;

    public Band(int id, String name, String song) {
        this.id = id;
        this.name = Objects.requireNonNull(name, "Name of band cannot be null.");
        this.song = Objects.requireNonNull(song, "Representative song of band cannot be null.");
    }

    public String getSong() {
        return song;
    }

    public String getName() {
        return name;
    }

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
