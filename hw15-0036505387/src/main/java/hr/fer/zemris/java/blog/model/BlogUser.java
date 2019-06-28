package hr.fer.zemris.java.blog.model;

import javax.persistence.*;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Models a single blog user who can post blog entries.
 */
@NamedQueries({
        @NamedQuery(name = "BlogUser.nickExists", query = "select COUNT(u) from BlogUser as u where u.nick = :nick"),
        @NamedQuery(name = "BlogUser.allUsers", query = "select u from BlogUser as u"),
        @NamedQuery(name = "BlogUser.selectByNick", query = "select u from BlogUser as u where u.nick = :nick")
})
@Entity
@Table(name = "blog_users")
public class BlogUser {

    /**
     * The unique identifier of the {@link BlogUser}.
     */
    private Long id;

    /**
     * The first name of the {@link BlogUser}.
     */
    private String firstName;

    /**
     * The last name of the {@link BlogUser}.
     */
    private String lastName;

    /**
     * The nickname of the {@link BlogUser}. Must be unique.
     */
    private String nick;

    /**
     * The email of the {@link BlogUser}.
     */
    private String email;

    /**
     * The SHA-1 hash of the {@link BlogUser}'s password.
     */
    private String passwordHash;

    /**
     * A list of {@link BlogEntry} posted by this {@link BlogUser}.
     */
    private List<BlogEntry> entries = new ArrayList<>();

    /**
     * Returns the unique identifier of the {@link BlogUser}.
     *
     * @return {@link #id}
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Sets the {@link #id} to the given argument.
     *
     * @param id the new value of {@link #id}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the first name of the {@link BlogUser}.
     *
     * @return {@link #firstName}
     */
    @Column(length = 30, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the {@link #firstName} to the given argument.
     *
     * @param firstName the new value of {@link #firstName}
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name of the {@link BlogUser}.
     *
     * @return {@link #lastName}
     */
    @Column(length = 30, nullable = false)
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the {@link #lastName} to the given argument.
     *
     * @param lastName the new value of {@link #lastName}
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the nickname of the {@link BlogUser}.
     *
     * @return {@link #nick}
     */
    @Column(length = 30, nullable = false, unique = true)
    public String getNick() {
        return nick;
    }

    /**
     * Sets the {@link #nick} to the given argument.
     *
     * @param nick the new value of {@link #nick}
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * Returns the email address of the {@link BlogUser}.
     *
     * @return {@link #email}
     */
    @Column(length = 30, nullable = false)
    public String getEmail() {
        return email;
    }

    /**
     * Sets the {@link #email} to the given argument.
     *
     * @param email the new value of {@link #email}
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the SHA-1 hash of the {@link BlogUser}'s password.
     *
     * @return {@link #passwordHash}
     */
    @Column(length = 100, nullable = false)
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the {@link #passwordHash} to the given argument.
     *
     * @param passwordHash the new value of {@link #passwordHash}
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Returns a list of {@link BlogEntry}ies posted by this {@link BlogUser}.
     *
     * @return {@link #entries}
     */
    @OneToMany(mappedBy = "creator")
    public List<BlogEntry> getEntries() {
        return entries;
    }

    /**
     * Sets the {@link #entries} to the given argument.
     *
     * @param entries the new value of {@link #entries}
     */
    public void setEntries(List<BlogEntry> entries) {
        this.entries = entries;
    }

    /**
     * Hashes the argument using SHA-1 and returns the result.
     *
     * @param password the password to hash
     * @return the result of hashing the argument using SHA-1
     */
    public static String hashPassword(String password) {
        MessageDigest digest = null;

        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ignorable) {
        }

        digest.update(password.getBytes(StandardCharsets.UTF_8));
        byte[] digestBytes = digest.digest();

        return DatatypeConverter.printHexBinary(digestBytes);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlogUser other = (BlogUser) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}