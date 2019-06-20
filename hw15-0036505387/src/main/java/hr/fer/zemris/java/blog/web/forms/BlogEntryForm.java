package hr.fer.zemris.java.blog.web.forms;

import hr.fer.zemris.java.blog.model.BlogEntry;

import javax.servlet.http.HttpServletRequest;

public class BlogEntryForm extends AbstractModelForm<BlogEntry> {

    private String id;
    private String title;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
