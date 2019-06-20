package hr.fer.zemris.java.blog.web.forms;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;

import javax.servlet.http.HttpServletRequest;

public class LoginForm extends AbstractModelForm<BlogUser> {

    private String nick;
    private String passwordHash;
    private BlogUser user;

    @Override
    public void fillFromHTTPRequst(HttpServletRequest request) {
        nick = normalise(request.getParameter("nick"));

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
        throw new UnsupportedOperationException();
    }

    @Override
    public void validate() {
        checkAssumptions("nick", new Assumptions("nickname", nick).isRequired());
        checkAssumptions("passwordHash", new Assumptions("password", passwordHash).isRequired());

        if (!hasAnyErrors()) {
            user = DAOProvider.getDAO().getUserByNickAndPasswordHash(nick, passwordHash);

            if (user == null) {
                errors.put("nick", "Invalid combination of nickname and password.");
            }
        }
    }

    private void checkAssumptions(String name, Assumptions assumptions) {
        if (assumptions.hasError()) {
            errors.put(name, assumptions.getError());
        }
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public BlogUser getUser() {
        return user;
    }

}
