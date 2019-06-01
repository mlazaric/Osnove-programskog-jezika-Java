package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Models a relative position of a component within {@link CalcLayout}.
 *
 * @author Marko Lazarić
 */
public class RCPosition {

    /**
     * The row component of the position.
     */
    private final int row;

    /**
     * The column component of the position.
     */
    private final int column;

    /**
     * Creates a new {@link RCPosition} with the given arguments.
     *
     * @param row the row component of the position
     * @param column the column component of the position
     */
    public RCPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the column component of the position.
     *
     * @return the column component of the position
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the row component of the position.
     *
     * @return the row component of the position
     */
    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "RCPosition{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RCPosition position = (RCPosition) o;

        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
