package hr.fer.zemris.java.blog.web.forms;

import javax.servlet.http.HttpServletRequest;

public interface ModelForm<E> {

    boolean hasAnyErrors();
    boolean hasErrorForAttribute(String attribute);
    String getErrorForAttribute(String attribute);

    void loadFromHTTPRequest(HttpServletRequest request);
    void loadFromEntity(E entity);
    void saveToEntity(E entity);

    void validate();

}
