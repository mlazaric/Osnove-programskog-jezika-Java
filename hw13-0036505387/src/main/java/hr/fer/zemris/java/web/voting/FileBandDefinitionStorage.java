package hr.fer.zemris.java.web.voting;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

public class FileBandDefinitionStorage implements IBandDefinitionStorage {

    private final Map<Integer, Band> bands;

    public FileBandDefinitionStorage(Path path) throws IOException {
        this.bands = Files.lines(path, StandardCharsets.UTF_8)
                          .filter(l -> !l.isEmpty())
                          .map(FileBandDefinitionStorage::parseLine)
                          .collect(Collectors.toMap(Band::getId, b -> b));
    }

    public FileBandDefinitionStorage(String path) throws IOException {
        this(Paths.get(path));
    }

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
