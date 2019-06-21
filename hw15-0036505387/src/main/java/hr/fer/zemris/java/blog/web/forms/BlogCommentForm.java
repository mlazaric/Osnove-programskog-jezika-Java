package hr.fer.zemris.java.blog.web.forms;

import hr.fer.zemris.java.blog.model.BlogComment;

import javax.servlet.http.HttpServletRequest;

public class BlogCommentForm extends AbstractModelForm<BlogComment> {

    private String id;
    private String email;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
