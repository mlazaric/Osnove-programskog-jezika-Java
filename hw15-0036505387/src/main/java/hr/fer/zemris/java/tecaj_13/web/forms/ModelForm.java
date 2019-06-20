package hr.fer.zemris.java.tecaj_13.web.forms;

import javax.servlet.http.HttpServletRequest;

public interface ModelForm<E> {

    boolean hasAnyErrors();
    boolean hasErrorForAttribute(String attribute);
    String getErrorForAttribute(String attribute);

    void fillFromHTTPRequst(HttpServletRequest request);
    void fillFromEntity(E entity);
    void fillEntity(E entity);

    void validate();

}
