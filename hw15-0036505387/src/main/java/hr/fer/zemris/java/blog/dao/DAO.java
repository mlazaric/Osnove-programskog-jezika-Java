package hr.fer.zemris.java.blog.dao;

import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;

import java.util.List;

/**
 * Models a simple data persistence layer. Allows for abstracting away the implementation details of the data storage.
 *
 * @author Marko Lazarić
 */
public interface DAO {

    /**
     * Finds the {@link BlogEntry} with the specified id.
     *
     * @param id the id of the {@link BlogEntry} to find
     * @return the {@link BlogEntry} with the specified id or null if no such {@link BlogEntry} exists
     * @throws DAOException if an error occurs while finding the {@link BlogEntry} with the specified id
     */
    BlogEntry getBlogEntry(Long id) throws DAOException;

    /**
     * Finds the {@link BlogUser} with the specified id.
     *
     * @param id the id of the {@link BlogUser} to find
     * @return the {@link BlogUser} with the specified id or null if no such {@link BlogUser} exists
     * @throws DAOException if an error occurs while finding the {@link BlogUser} with the specified id
     */
    BlogUser getBlogUser(Long id) throws DAOException;

    /**
     * Returns whether the given nickname already exists.
     *
     * @param nick the nick to check if it exists in the data persistence layer already
     * @return true if it already exists, false otherwise
     * @throws DAOException if an error occurs while checking whether the nick already exists
     */
    boolean nicknameExists(String nick) throws DAOException;

    /**
     * Saves {@link BlogUser} to persistent storage.
     *
     * @param user the {@link BlogUser} to store
     * @throws DAOException if an error occurs while saving {@link BlogUser} to persistent storage
     */
    void persistUser(BlogUser user) throws DAOException;

    /**
     * Saves {@link BlogComment} to persistent storage.
     *
     * @param comment the {@link BlogComment} to store
     * @throws DAOException if an error occurs while saving {@link BlogComment} to persistent storage
     */
    void persistComment(BlogComment comment) throws DAOException;

    /**
     * Saves {@link BlogEntry} to persistent storage.
     *
     * @param entry the {@link BlogEntry} to store
     * @throws DAOException if an error occurs while saving {@link BlogEntry} to persistent storage
     */
    void persistEntry(BlogEntry entry) throws DAOException;

    /**
     * Returns a list of all the registered users.
     *
     * @return a list of all the registered users
     * @throws DAOException if an error occurs while selecting all the users
     */
    List<BlogUser> listUsers() throws DAOException;

    /**
     * Returns the user with the specified nickname and passwordHash or null if no such user exists.
     *
     * @param nick         the nick to use to find the user
     * @param passwordHash the password has to check against
     * @return the user with the specified nickname and passwordHash or null if no such user exists
     * @throws DAOException if an error occurs while selecting the user with the specified attributes
     */
    BlogUser getUserByNickAndPasswordHash(String nick, String passwordHash) throws DAOException;

    /**
     * Returns a list of entries for the user specified by their nickname.
     *
     * @param nick the nickname of the user
     * @return a list of all the entries for the user specified by their nickname
     * @throws DAOException if an error occurs while selecting the blog entries
     */
    List<BlogEntry> listEntriesForUser(String nick) throws DAOException;


}