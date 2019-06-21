package hr.fer.zemris.java.blog.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Models a single blog entry posted by a blog user.
 *
 * @author Marko Lazarić
 */
@NamedQueries({
        @NamedQuery(name = "BlogEntry.entriesByUser", query = "select b from BlogEntry as b inner join b.creator as u" +
                " where u.nick=:nick")
})
@Entity
@Table(name = "blog_entries")
@Cacheable(true)
public class BlogEntry {

    /**
     * The unique identifer of the {@link BlogEntry}.
     */
    private Long id;

    /**
     * The comments posted to this entry.
     */
    private List<BlogComment> comments = new ArrayList<>();

    /**
     * The timestamp when this entry was created.
     */
    private Date createdAt;

    /**
     * The timestamp when this entry was updated.
     */
    private Date lastModifiedAt;

    /**
     * The title of this blog entry.
     */
    private String title;

    /**
     * The contents of this blog entry.
     */
    private String text;

    /**
     * The {@link BlogUser} who posted this entry.
     */
    private BlogUser creator;

    /**
     * Returns the unique identifier of the {@link BlogEntry}.
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
     * Returns a list of the comments posted on this {@link BlogEntry}.
     *
     * @return {@link #comments}
     */
    @OneToMany(mappedBy = "blogEntry", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @OrderBy("postedOn")
    public List<BlogComment> getComments() {
        return comments;
    }

    /**
     * Sets the {@link #comments} to the given argument.
     *
     * @param comments the new value of {@link #comments}
     */
    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    /**
     * Returns the timestamp when this {@link BlogEntry} was created.
     *
     * @return {@link #createdAt}
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets {@link #createdAt} to the given argument.
     *
     * @param createdAt the new value of {@link #createdAt}
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns the timestamp when this {@link BlogEntry} was last updated.
     *
     * @return {@link #lastModifiedAt}
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    /**
     * Sets {@link #lastModifiedAt} to the given argument.
     *
     * @param lastModifiedAt the new value of {@link #lastModifiedAt}
     */
    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    /**
     * Returns the title of the {@link BlogEntry}.
     *
     * @return {@link #title}
     */
    @Column(length = 200, nullable = false)
    public String getTitle() {
        return title;
    }

    /**
     * Sets {@link #title} to the given argument.
     *
     * @param title the new value of {@link #title}
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the contents of the {@link BlogEntry}.
     *
     * @return {@link #text}
     */
    @Column(length = 4096, nullable = false)
    public String getText() {
        return text;
    }

    /**
     * Sets {@link #text} to the given argument.
     *
     * @param text the new value of {@link #text}
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the {@link BlogUser} who created this {@link BlogEntry}.
     *
     * @return {@link #creator}
     */
    @ManyToOne
    @JoinColumn(nullable = false)
    public BlogUser getCreator() {
        return creator;
    }

    /**
     * Sets the {@link #creator} to the given argument.
     *
     * @param creator the new value of {@link #creator}
     */
    public void setCreator(BlogUser creator) {
        this.creator = creator;
    }

    /**
     * Runs before persisting the {@link BlogEntry}, sets the {@link #createdAt} and {@link #lastModifiedAt} to the
     * current timestamp.
     */
    @PrePersist
    protected void onCreate() {
        // TODO: @CreationTimestamp does not work for some reason...
        createdAt = new Date();
        lastModifiedAt = new Date();
    }

    /**
     * Runs before updating the {@link BlogEntry}, sets {@link #lastModifiedAt} to the current timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        // TODO: @UpdateTimestamp does not work for some reason...
        lastModifiedAt = new Date();
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
        BlogEntry other = (BlogEntry) obj;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }


}