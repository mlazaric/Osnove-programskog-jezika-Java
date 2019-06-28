package hr.fer.zemris.java.blog.web.forms;

import hr.fer.zemris.java.blog.model.BlogComment;

import javax.servlet.http.HttpServletRequest;

/**
 * The form used for posting a comment to a {@link hr.fer.zemris.java.blog.model.BlogEntry}.
 *
 * @author Marko Lazarić
 */
public class BlogCommentForm extends AbstractModelForm<BlogComment> {

    /**
     * The unique identifier of the {@link BlogComment}.
     */
    private String id;

    /**
     * The email address of the user who posted the {@link BlogComment}.
     */
    private String email;

    /**
     * The contents of the {@link BlogComment}.
     */
    private String message;

    @Override
    public void loadFromHTTPRequest(HttpServletRequest request) {
        id = normalise(request.getParameter("id"));
        email = normalise(request.getParameter("email"));
        message = normalise(request.getParameter("message"));

        if (request.getSession().getAttribute("current.user.email") != null) {
            email = normalise((String) request.getSession().getAttribute("current.user.email"));
        }
    }

    @Override
    public void loadFromEntity(BlogComment entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveToEntity(BlogComment entity) {
        if (id.isEmpty()) {
            entity.setId(null);
        }
        else {
            entity.setId(Long.valueOf(id));
        }

        entity.setUsersEMail(email);
        entity.setMessage(message);
    }

    @Override
    public void validate() {
        if (!id.isEmpty()) {
            checkAssumptions("id", new Assumptions("id", id).isParsableLong());
        }

        checkAssumptions("email", new Assumptions("email", email).isRequired().isValidEmailAddress());
        checkAssumptions("message", new Assumptions("message", message).isRequired());
    }

    /**
     * @return {@link #id}
     */
    public String getId() {
        return id;
    }

    /**
     * Sets {@link #id} to the given argument.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return {@link #email}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets {@link #email} to the given argument.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return {@link #message}
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets {@link #message} to the given argument.
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
