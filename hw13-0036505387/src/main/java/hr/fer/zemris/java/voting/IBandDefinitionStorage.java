package hr.fer.zemris.java.voting;

/**
 * An interface which models the storage of band definitions.
 *
 * It is used to decouple the logic from a concrete implementation. For example,
 * instead of a file storage, it could be switched to a database storage.
 *
 * @author Marko Lazarić
 */
public interface IBandDefinitionStorage extends Iterable<Band> {

    /**
     * Returns the band with the specified unique identifier or null if such a band does not exist.
     *
     * @param id the unique identifier of the band to return
     * @return the band with the specified unique identifier or null if such a band does not exist
     */
    Band getById(int id);

}
