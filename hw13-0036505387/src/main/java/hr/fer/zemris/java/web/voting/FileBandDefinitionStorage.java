package hr.fer.zemris.java.web.voting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A concrete implementation of {@link IBandDefinitionStorage} which uses a file for storage.
 *
 * @author Marko Lazarić
 */
public class FileBandDefinitionStorage implements IBandDefinitionStorage {

    /**
     * The mapping of the band's unique identifiers to their java bean objects.
     */
    private final Map<Integer, Band> bands;

    /**
     * Creates a new {@link FileBandDefinitionStorage} with the specified argument.
     *
     * @param path the path to the file to use as the band definition storage
     * @throws IOException if an error occurs while reading from the file
     */
    public FileBandDefinitionStorage(Path path) throws IOException {
        this.bands = Files.lines(path, StandardCharsets.UTF_8)
                          .filter(l -> !l.isEmpty())
                          .map(FileBandDefinitionStorage::parseLine)
                          .collect(Collectors.toMap(Band::getId, b -> b));
    }

    /**
     * Creates a new {@link FileBandDefinitionStorage} with the specified argument.
     *
     * @param path the path to the file to use as the band definition storage
     * @throws IOException if an error occurs while reading from the file
     */
    public FileBandDefinitionStorage(String path) throws IOException {
        this(Paths.get(path));
    }

    /**
     * Parse a single line which contains an integer (the band's unique identifier) and two strings (the name of the band
     * and their representative song) into a {@link Band} object. The fields are separated by a single TAB character.
     *
     * @param line the line to parse
     *
     * @throws IllegalArgumentException if the line is not parsable, with an appropriate message
     */
    private static Band parseLine(String line) {
        String[] parts = line.split("\\t");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid number of fields in line.");
        }

        try {
            int id = Integer.parseInt(parts[0]);

            return new Band(id, parts[1], parts[2]);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("'" + parts[0] + "' is not a valid id (not parsable as an integer).");
        }
    }

    @Override
    public Band getById(int id) {
        return bands.get(id);
    }

    @Override
    public Iterator<Band> iterator() {
        return bands.values()
                    .stream()
                    .sorted()
                    .iterator();
    }
}
