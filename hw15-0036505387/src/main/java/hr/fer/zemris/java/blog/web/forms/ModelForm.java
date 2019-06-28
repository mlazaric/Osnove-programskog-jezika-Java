package hr.fer.zemris.java.blog.web.forms;

import javax.servlet.http.HttpServletRequest;

/**
 * Models a simple form object which holds all attributes as {@link String}s which it can validate.
 * It can be filled from an {@link HttpServletRequest} or an entity.
 * It can save the loaded attributes to an entity.
 *
 * @param <E> the type of the entity represented by this form
 */
public interface ModelForm<E> {

    /**
     * Returns whether any errors were found while validating the attributes.
     *
     * @return true if any errors were found, false otherwise
     */
    boolean hasAnyErrors();

    /**
     * Returns whether any errors were found for the specific attribute while validating it.
     *
     * @param attribute the name of the attribute to check for errors
     * @return true if any errors were found for the specific attribute, false otherwise
     */
    boolean hasErrorForAttribute(String attribute);

    /**
     * Returns the error found while validating the specific attribute.
     *
     * @param attribute the name of the attribute whose error should be returned
     * @return the error found while validating the specific attribute or null if no errors were found
     */
    String getErrorForAttribute(String attribute);

    /**
     * Load the values of the attributes from the given {@link HttpServletRequest}.
     *
     * @param request the {@link HttpServletRequest} to use to fill in the attribute values
     */
    void loadFromHTTPRequest(HttpServletRequest request);

    /**
     * Load the values of the attributes from the given entity.
     *
     * @param entity the entity to use to fill in the attribute values
     */
    void loadFromEntity(E entity);

    /**
     * Save the values of the attributes to the given entity.
     *
     * @param entity the entity to fill with the values of the attributes stored in thi form
     */
    void saveToEntity(E entity);

    /**
     * Check all attributes for errors and store any found errors.
     */
    void validate();

}
