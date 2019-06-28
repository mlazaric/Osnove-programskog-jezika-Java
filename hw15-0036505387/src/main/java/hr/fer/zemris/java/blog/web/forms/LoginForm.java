package hr.fer.zemris.java.blog.web.forms;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;

import javax.servlet.http.HttpServletRequest;

/**
 * The form used logging in as a {@link BlogUser}.
 *
 * @author Marko Lazarić
 */
public class LoginForm extends AbstractModelForm<BlogUser> {

    /**
     * The nickname of the {@link BlogUser}.
     */
    private String nick;

    /**
     * The SHA-1 hash of the {@link BlogUser}'s password.
     */
    private String passwordHash;

    /**
     * The {@link BlogUser} with the given nickname and password hash.
     */
    private BlogUser user;

    @Override
    public void loadFromHTTPRequest(HttpServletRequest request) {
        nick = normalise(request.getParameter("nick"));

        String password = normalise(request.getParameter("password"));

        if (password != "") {
            passwordHash = BlogUser.hashPassword(password);
        }
        else {
            passwordHash = "";
        }
    }

    @Override
    public void loadFromEntity(BlogUser entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveToEntity(BlogUser entity) {
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

    /**
     * @return #nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * Sets {@link #nick} to the given argument.
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * @return #passwordHash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets {@link #passwordHash} to the given argument.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * @return #user
     */
    public BlogUser getUser() {
        return user;
    }

}
