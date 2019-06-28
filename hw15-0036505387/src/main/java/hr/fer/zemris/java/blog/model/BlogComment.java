package hr.fer.zemris.java.blog.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Models a single comment on a blog entry.
 *
 * @author Marko Lazarić
 */
@Entity
@Table(name = "blog_comments")
public class BlogComment {

    /**
     * The unique identifier of the comment.
     */
    private Long id;

    /**
     * The {@link BlogEntry} whose comment this is.
     */
    private BlogEntry blogEntry;

    /**
     * The email of the user who posted this comment.
     */
    private String usersEMail;

    /**
     * The contents of the comment.
     */
    private String message;

    /**
     * The timestamp when this comment was posted.
     */
    private Date postedOn;

    /**
     * Returns the unique identifier of this comment.
     *
     * @return {@link #id}
     */
    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    /**
     * Sets {@link #id} to the given argument.
     *
     * @param id the new value of {@link #id}
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the {@link BlogEntry} whose comment this is.
     *
     * @return {@link #blogEntry}
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public BlogEntry getBlogEntry() {
        return blogEntry;
    }

    /**
     * Sets the {@link #blogEntry} to the given argument.
     *
     * @param blogEntry the new value of {@link #blogEntry}
     */
    public void setBlogEntry(BlogEntry blogEntry) {
        this.blogEntry = blogEntry;
    }

    /**
     * Returns the email address of the user who posted this comment.
     *
     * @return {@link #usersEMail}
     */
    @Column(length = 100, nullable = false)
    public String getUsersEMail() {
        return usersEMail;
    }

    /**
     * Sets the {@link #usersEMail} to the given argument.
     *
     * @param usersEMail the new value of {@link #usersEMail}
     */
    public void setUsersEMail(String usersEMail) {
        this.usersEMail = usersEMail;
    }

    /**
     * Returns the contents of the comment.
     *
     * @return {@link #message}
     */
    @Column(length = 4096, nullable = false)
    public String getMessage() {
        return message;
    }

    /**
     * Sets the {@link #message} to the given argument.
     *
     * @param message the new value of {@link #message}
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns the timestamp when this comment was posted.
     *
     * @return {@link #postedOn}
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getPostedOn() {
        return postedOn;
    }

    /**
     * Sets the {@link #postedOn} to the given argument.
     *
     * @param postedOn the new value of {@link #postedOn}
     */
    public void setPostedOn(Date postedOn) {
        this.postedOn = postedOn;
    }

    /**
     * Runs before persisting the object to the database and sets {@link #postedOn} to the current timestamp.
     */
    @PrePersist
    protected void onCreate() {
        // TODO: @CreationTimestamp does not work for some reason...
        postedOn = new Date();
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
        BlogComment other = (BlogComment) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }
}