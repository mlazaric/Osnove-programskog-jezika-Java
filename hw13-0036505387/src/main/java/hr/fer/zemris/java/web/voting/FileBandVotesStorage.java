package hr.fer.zemris.java.web.voting;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * A concrete implementation of {@link IBandVotesStorage} which uses a file for storage.
 *
 * @author Marko Lazarić
 */
public class FileBandVotesStorage implements IBandVotesStorage {

    /**
     * The path to the file to use as the votes storage.
     */
    private final Path path;

    /**
     * The mapping of band unique identifiers to their vote counts.
     */
    private final Map<Integer, Integer> votes;

    /**
     * Creates a new {@link FileBandDefinitionStorage} with the specified argument.
     *
     * @param path the path to the file to use as the votes storage
     * @throws IOException if an error occurs while reading from the file
     */
    public FileBandVotesStorage(Path path) throws IOException {
        this.path = path;
        votes = new HashMap<>();

        if (Files.exists(path)) {
            loadFromFile(path);
        }
    }

    /**
     * Creates a new {@link FileBandDefinitionStorage} with the specified argument.
     *
     * @param path the path to the file to use as the votes storage
     * @throws IOException if an error occurs while reading from the file
     */
    public FileBandVotesStorage(String path) throws IOException {
        this(Paths.get(path));
    }

    /**
     * Loads the band vote counts from the specified file.
     *
     * @param path the path to load the band vote counts from
     * @throws IOException if an error occurs while reading from the file or during parsing
     */
    private void loadFromFile(Path path) throws IOException {
        Files.lines(path, StandardCharsets.UTF_8)
             .filter(l -> !l.isEmpty())
             .forEach(this::parseLine);
    }

    /**
     * Parse a single line which contains two integers (the band's unique identifier and the vote count) and adds it to
     * the map. The fields are separated by a single TAB character.
     *
     * @param line the line to parse
     *
     * @throws IllegalArgumentException if the line is not parsable, with an appropriate message
     */
    private void parseLine(String line) {
        String[] parts = line.split("\\t");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid number of fields in line, should be two. Line: '" + line + "'. " +
                    "Number of parts: " + parts.length + ".");
        }

        try {
            int id = Integer.parseInt(parts[0]);
            int voteNumber = Integer.parseInt(parts[1]);

            votes.put(id, voteNumber);
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Field is not parsable to integer in line: '" + line + "'.");
        }
    }

    /**
     * Saves the vote counts to the file.
     *
     * @throws IOException if an error occurs while writing to the file
     */
    private void saveToFile() throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            votes.entrySet()
                 .stream()
                 .map(e -> e.getKey() + "\t" + e.getValue()) // Concatenate lines
                 .forEach(line -> {
                     try {
                         writer.append(line).append('\n');
                     } catch (IOException e) { // Should never happen, but java complains if we do not check
                         e.printStackTrace();
                     }
                 });
        }
    }

    @Override
    public void voteFor(int id) {
        votes.merge(id, 1, Integer::sum);

        try {
            saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e); // Wrap exception...
        }
    }

    @Override
    public Iterable<BandVoteCount> sortedByVoteCount(IBandDefinitionStorage bandDefinition) {
        return votes.entrySet()
                    .stream()
                    .map(e -> new BandVoteCount(bandDefinition.getById(e.getKey()), e.getValue())) // Map to java bean objects
                    .sorted()                                                                      // Sort by vote count and band id
                    ::iterator;                                                                    // Return Iterable instead of Iterator
                                                                                                   //  so it can be used in for-each loops
    }

    @Override
    public Iterable<BandVoteCount> bestVoted(IBandDefinitionStorage bandDefinition) {
        int maxVotes = votes.entrySet()
                            .stream()
                            .mapToInt(Map.Entry<Integer, Integer>::getValue)  // Map to vote count
                            .max()                                            // Get maximum vote count
                            .orElse(0);

        return votes.entrySet()
                    .stream()
                    .filter(e -> maxVotes == e.getValue())                                          // Only filter the bands with max votes
                    .map(e -> new BandVoteCount(bandDefinition.getById(e.getKey()), e.getValue()))  // Map to java bean objects
                    .sorted()                                                                       // Sort by band id since the vote counts are equal
                    ::iterator;                                                                     // Return Iterable instead of Iterator
                                                                                                    //  so it can be used in for-each loops
    }

}
