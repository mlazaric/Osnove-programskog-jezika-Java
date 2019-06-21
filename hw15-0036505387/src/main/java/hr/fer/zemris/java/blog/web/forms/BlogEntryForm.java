package hr.fer.zemris.java.blog.web.forms;

import hr.fer.zemris.java.blog.model.BlogEntry;

import javax.servlet.http.HttpServletRequest;

/**
 * The form used creating and editing a {@link BlogEntry}.
 *
 * @author Marko Lazarić
 */
public class BlogEntryForm extends AbstractModelForm<BlogEntry> {

    /**
     * The unique identifier of the {@link BlogEntry}.
     */
    private String id;

    /**
     * The title of the {@link BlogEntry}.
     */
    private String title;

    /**
     * The contents of the {@link BlogEntry}.
     */
    private String text;

    @Override
    public void loadFromHTTPRequest(HttpServletRequest request) {
        id = normalise(request.getParameter("id"));
        title = normalise(request.getParameter("title"));
        text = normalise(request.getParameter("text"));
    }

    @Override
    public void loadFromEntity(BlogEntry entity) {
        id = entity.getId().toString();
        title = entity.getTitle();
        text = entity.getText();
    }

    @Override
    public void saveToEntity(BlogEntry entity) {
        if (id != null && !id.isEmpty()) {
            entity.setId(Long.valueOf(id));
        }

        entity.setTitle(title);
        entity.setText(text);
    }

    @Override
    public void validate() {
        if (!id.isEmpty()) {
            checkAssumptions("id", new Assumptions("id", id).isParsableLong());
        }

        checkAssumptions("title", new Assumptions("title", title).isRequired());
        checkAssumptions("text", new Assumptions("message", text).isRequired());
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
     * @return {@link #title}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets {@link #title} to the given argument.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return {@link #text}
     */
    public String getText() {
        return text;
    }

    /**
     * Sets {@link #text} to the given argument.
     */
    public void setText(String text) {
        this.text = text;
    }
}
