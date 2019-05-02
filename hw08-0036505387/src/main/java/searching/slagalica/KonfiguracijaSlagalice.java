package searching.slagalica;

import java.util.*;

public class KonfiguracijaSlagalice {

    public static final KonfiguracijaSlagalice SOLVED_CONFIGURATION = new KonfiguracijaSlagalice(new int[]{
            1, 2, 3,
            4, 5, 6,
            7, 8, 0});
    private static final int NUMBER_OF_COLUMNS = 3;
    private static final int NUMBER_OF_ROWS = 3;

    private final int[] configuration;
    private final int indexOfSpace;

    public KonfiguracijaSlagalice(int[] configuration) {
        this.configuration = Objects.requireNonNull(configuration, "Configuration cannot be null.");
        this.indexOfSpace = findIndexOfSpace();

        checkConfiguration();
    }

    private int findIndexOfSpace() {
        for (int index = 0; index < configuration.length; ++index) {
            if (configuration[index] == 0) {
                return index;
            }
        }

        throw new IllegalArgumentException("Configuration does not contain an empty space.");
    }

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

    public KonfiguracijaSlagalice moveLeft() {
        return moveHorizontal(true);
    }

    public KonfiguracijaSlagalice moveRight() {
        return moveHorizontal(false);
    }

    public KonfiguracijaSlagalice moveUp() {
        return moveVertical(true);
    }

    public KonfiguracijaSlagalice moveDown() {
        return moveVertical(false);
    }

    private KonfiguracijaSlagalice moveVertical(boolean moveUp) {
        int[][] matrix = getConfigurationMatrix();
        int rowOfSpace = indexOfSpace / NUMBER_OF_COLUMNS;
        int colOfSpace = indexOfSpace % NUMBER_OF_COLUMNS;

        // We cannot move it, it is already on the border
        if (moveUp && rowOfSpace == 0 || !moveUp && rowOfSpace == (NUMBER_OF_ROWS - 1)) {
            return null;
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

    private KonfiguracijaSlagalice moveHorizontal(boolean moveLeft) {
        int[][] matrix = getConfigurationMatrix();
        int rowOfSpace = indexOfSpace / NUMBER_OF_COLUMNS;
        int colOfSpace = indexOfSpace % NUMBER_OF_COLUMNS;

        // We cannot move it, it is already on the border
        if (moveLeft && colOfSpace == 0 || !moveLeft && colOfSpace == (NUMBER_OF_COLUMNS - 1)) {
            return null;
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

    private int[][] getConfigurationMatrix() {
        int[][] matrix = new int[NUMBER_OF_ROWS][NUMBER_OF_COLUMNS];

        for (int index = 0; index < configuration.length; ++index) {
            matrix[index / NUMBER_OF_COLUMNS][index % NUMBER_OF_COLUMNS] = configuration[index];
        }

        return matrix;
    }

    public int[] getPolje() { // Required for SlagalicaViewer.display...
        return getConfiguration();
    }

    public int[] getConfiguration() {
        return Arrays.copyOf(configuration, configuration.length);
    }

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
