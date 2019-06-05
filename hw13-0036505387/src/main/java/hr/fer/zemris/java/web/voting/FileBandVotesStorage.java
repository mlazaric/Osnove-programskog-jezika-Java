package hr.fer.zemris.java.web.voting;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileBandVotesStorage implements IBandVotesStorage {

    private final Path path;
    private final Map<Integer, Integer> votes;

    public FileBandVotesStorage(Path path) throws IOException {
        this.path = path;
        votes = new HashMap<>();

        if (Files.exists(path)) {
            loadFromFile(path);
        }
    }

    public FileBandVotesStorage(String path) throws IOException {
        this(Paths.get(path));
    }

    private void loadFromFile(Path path) throws IOException {
        Files.lines(path, StandardCharsets.UTF_8)
             .filter(l -> !l.isEmpty())
             .forEach(this::parseLine);
    }

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

    private void saveToFile() throws IOException {
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            votes.entrySet()
                 .stream()
                 .map(e -> e.getKey() + "\t" + e.getValue()) // Form lines
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
