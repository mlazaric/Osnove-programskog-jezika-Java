package searching.slagalica;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Models a simple 3x3 puzzle state.
 *
 * @author Marko Lazarić
 */
public class KonfiguracijaSlagalice {

    /**
     * Solved configration of the puzzle, useful for checking whether a solution has been found.
     */
    public static final KonfiguracijaSlagalice SOLVED_CONFIGURATION = new KonfiguracijaSlagalice(new int[]{
            1, 2, 3,
            4, 5, 6,
            7, 8, 0});

    /**
     * The number of columns.
     */
    private static final int NUMBER_OF_COLUMNS = 3;

    /**
     * The number of rows.
     */
    private static final int NUMBER_OF_ROWS = 3;

    /**
     * The configuration of the puzzle, an array of 9 unique integers (0-8).
     */
    private final int[] configuration;

    /**
     * The index of 0.
     */
    private final int indexOfSpace;

    /**
     * Creates a {@link KonfiguracijaSlagalice} with the specified argument.
     *
     * @param configuration the configuration of the puzzle
     *
     * @throws NullPointerException if {@code configuration} is {@code null}
     * @throws IllegalArgumentException if {@code configuration} does not contain all digits 012345678 or contains too many digits
     */
    public KonfiguracijaSlagalice(int[] configuration) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null.");
        this.indexOfSpace = findIndexOfSpace();

        checkConfiguration();
    }

    /**
     * Returns the position of 0 in {@link #indexOfSpace}.
     *
     * @return the position of 0 in {@link #indexOfSpace}
     *
     * @throws IllegalArgumentException if the configuration does not contain any zeros
     */
    private int findIndexOfSpace() {
        for (int index = 0; index < configuration.length; ++index) {
            if (configuration[index] == 0) {
                return index;
            }
        }

        throw new IllegalArgumentException("Configuration does not contain an empty space.");
    }

    /**
     * Checks whether the configuration contains all digits (0-8) and only those digits.
     *
     * @throws IllegalArgumentException if the configuration contains invalid digits or does not contain all valid digits
     */
    private void checkConfiguration() {
        Set<Integer> fromConfiguration = new HashSet<>();
        Set<Integer> allDigits = new HashSet<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));

        for (int digit : configuration) {
            fromConfiguration.add(digit);
        }

        if (!allDigits.equals(fromConfiguration)) {
            throw new IllegalArgumentException("Configuration is not valid, it contains invalid digits or is missing valid digits.");
        }
    }

    /**
     * Moves the space one place to the left. If the space is already at the left border, {@code this} is returned.
     *
     * @return the new configuration with the space moved one place to the left or {@code this}
     */
    public KonfiguracijaSlagalice moveLeft() {
        return moveHorizontal(true);
    }

    /**
     * Moves the space one place to the right. If the space is already at the right border, {@code this} is returned.
     *
     * @return the new configuration with the space moved one place to the right or {@code this}
     */
    public KonfiguracijaSlagalice moveRight() {
        return moveHorizontal(false);
    }

    /**
     * Moves the space one place up. If the space is already at the top border, {@code this} is returned.
     *
     * @return the new configuration with the space moved one place up or {@code this}
     */
    public KonfiguracijaSlagalice moveUp() {
        return moveVertical(true);
    }

    /**
     * Moves the space one place down. If the space is already at the bottom border, {@code this} is returned.
     *
     * @return the new configuration with the space moved one place down or {@code this}
     */
    public KonfiguracijaSlagalice moveDown() {
        return moveVertical(false);
    }

    /**
     * Moves the space one place vertically. If the space is already at the top/bottom border, {@code this} is returned.
     *
     * @param moveUp true if it should be moved up, false if it should be moved down
     * @return the new configuration with the moved space or {@code this}
     */
    private KonfiguracijaSlagalice moveVertical(boolean moveUp) {
        int[][] matrix = getConfigurationMatrix();
        int rowOfSpace = indexOfSpace / NUMBER_OF_COLUMNS;
        int colOfSpace = indexOfSpace % NUMBER_OF_COLUMNS;

        // We cannot move it, it is already on the border
        if (moveUp && rowOfSpace == 0 || !moveUp && rowOfSpace == (NUMBER_OF_ROWS - 1)) {
            return this;
        }

        if (moveUp) {
            matrix[rowOfSpace][colOfSpace] = matrix[rowOfSpace - 1][colOfSpace];
            matrix[rowOfSpace - 1][colOfSpace] = 0;
        }
        else {
            matrix[rowOfSpace][colOfSpace] = matrix[rowOfSpace + 1][colOfSpace];
            matrix[rowOfSpace + 1][colOfSpace] = 0;
        }

        return new KonfiguracijaSlagalice(flattenMatrix(matrix));
    }

    /**
     * Moves the space one place horizontally. If the space is already at the left/right border, {@code this} is returned.
     *
     * @param moveLeft true if it should be moved left, false if it should be moved right
     * @return the new configuration with the moved space or {@code this}
     */
    private KonfiguracijaSlagalice moveHorizontal(boolean moveLeft) {
        int[][] matrix = getConfigurationMatrix();
        int rowOfSpace = indexOfSpace / NUMBER_OF_COLUMNS;
        int colOfSpace = indexOfSpace % NUMBER_OF_COLUMNS;

        // We cannot move it, it is already on the border
        if (moveLeft && colOfSpace == 0 || !moveLeft && colOfSpace == (NUMBER_OF_COLUMNS - 1)) {
            return this;
        }

        if (moveLeft) {
            matrix[rowOfSpace][colOfSpace] = matrix[rowOfSpace][colOfSpace - 1];
            matrix[rowOfSpace][colOfSpace - 1] = 0;
        }
        else {
            matrix[rowOfSpace][colOfSpace] = matrix[rowOfSpace][colOfSpace + 1];
            matrix[rowOfSpace][colOfSpace + 1] = 0;
        }

        return new KonfiguracijaSlagalice(flattenMatrix(matrix));
    }

    /**
     * Flattens a two dimensional array to a one dimensional array.
     *
     * @param matrix the two dimensional array to flatten
     * @return the flattened array
     */
    private int[] flattenMatrix(int[][] matrix) {
        int[] flattened = new int[matrix.length * matrix[0].length];
        int index = 0;

        for (int[] row : matrix) {
            for (int number : row) {
                flattened[index] = number;

                ++index;
            }
        }

        return flattened;
    }

    /**
     * Returns the two dimensional configuration array.
     *
     * @return the two dimensional configuration array
     */
    private int[][] getConfigurationMatrix() {
        int[][] matrix = new int[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];

        for (int index = 0; index < configuration.length; ++index) {
            matrix[index / NUMBER_OF_COLUMNS][index % NUMBER_OF_COLUMNS] = configuration[index];
        }

        return matrix;
    }

    /**
     * Returns the one dimensional configuration array.
     *
     * @return the one dimensional configuration array
     */
    public int[] getPolje() { // Required for SlagalicaViewer.display...
        return getConfiguration();
    }

    /**
     * Returns the one dimensional configuration array.
     *
     * @return the one dimensional configuration array
     */
    public int[] getConfiguration() {
        return Arrays.copyOf(configuration, configuration.length);
    }

    /**
     * Returns the index of the 0 in the configuration.
     *
     * @return the index of the 0 in the configuration
     */
    public int indexOfSpace() {
        return indexOfSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KonfiguracijaSlagalice that = (KonfiguracijaSlagalice) o;

        return Arrays.equals(configuration, that.configuration);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(configuration);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int[][] matrix = getConfigurationMatrix();

        for (int[] row : matrix) {
            for (int number : row) {
                if (number == 0) {
                    sb.append('*');
                }
                else {
                    sb.append(number);
                }

                sb.append(' ');
            }

            sb.append('\n');
        }

        return sb.toString();
    }
}
