package hr.fer.zemris.java.blog.web.forms;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;

import javax.servlet.http.HttpServletRequest;

public class BlogUserForm extends AbstractModelForm<BlogUser> {

    private String id;
    private String firstName;
    private String lastName;
    private String nick;
    private String email;
    private String passwordHash;

    @Override
    public void fillFromHTTPRequst(HttpServletRequest request) {
        id = normalise(request.getParameter("id"));
        firstName = normalise(request.getParameter("firstName"));
        lastName = normalise(request.getParameter("lastName"));
        nick = normalise(request.getParameter("nick"));
        email = normalise(request.getParameter("email"));

        String password = normalise(request.getParameter("password"));

        if (password != "") {
            passwordHash = BlogUser.hashPassword(password);
        }
        else {
            passwordHash = "";
        }
    }

    private String normalise(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }


    @Override
    public void fillFromEntity(BlogUser entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void fillEntity(BlogUser entity) {
        if (id.isEmpty()) {
            entity.setId(null);
        }
        else {
            entity.setId(Long.valueOf(id));
        }

        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setNick(nick);
        entity.setEmail(email);
        entity.setPasswordHash(passwordHash);
    }

    @Override
    public void validate() {
        if (!id.isEmpty()) {
            checkAssumptions("id", new Assumptions("id", id).isParsableLong());
        }

        checkAssumptions("firstName", new Assumptions("first name", firstName).isRequired());
        checkAssumptions("lastName", new Assumptions("last name", firstName).isRequired());
        checkAssumptions("nick", new Assumptions("nickname", nick).isRequired());
        checkAssumptions("email", new Assumptions("email", email).isRequired().isValidEmailAddress());
        checkAssumptions("passwordHash", new Assumptions("password", passwordHash).isRequired());

        if (!hasErrorForAttribute("nick")) {
            if (DAOProvider.getDAO().nicknameExists(nick)) {
                errors.put("nick", "Nick is already in use.");
            }
        }
    }

    private void checkAssumptions(String name, Assumptions assumptions) {
        if (assumptions.hasError()) {
            errors.put(name, assumptions.getError());
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

}
