package hr.fer.zemris.java.blog.web.forms;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;

import javax.servlet.http.HttpServletRequest;

/**
 * The form used registering as a {@link BlogUser}.
 *
 * @author Marko Lazarić
 */
public class BlogUserForm extends AbstractModelForm<BlogUser> {

    /**
     * The unique identifier of the {@link BlogUser}.
     */
    private String id;

    /**
     * The first name of the {@link BlogUser}.
     */
    private String firstName;

    /**
     * The last name of the {@link BlogUser}.
     */
    private String lastName;

    /**
     * The nick of the {@link BlogUser}.
     */
    private String nick;

    /**
     * The email address of the {@link BlogUser}.
     */
    private String email;

    /**
     * The SHA-1 hash of the {@link BlogUser}'s password.
     */
    private String passwordHash;

    @Override
    public void loadFromHTTPRequest(HttpServletRequest request) {
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

    @Override
    public void loadFromEntity(BlogUser entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveToEntity(BlogUser entity) {
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
        checkAssumptions("lastName", new Assumptions("last name", lastName).isRequired());
        checkAssumptions("nick", new Assumptions("nickname", nick).isRequired());
        checkAssumptions("email", new Assumptions("email", email).isRequired().isValidEmailAddress());
        checkAssumptions("passwordHash", new Assumptions("password", passwordHash).isRequired());

        if (!hasErrorForAttribute("nick")) {
            if (DAOProvider.getDAO().nicknameExists(nick)) {
                errors.put("nick", "Nick is already in use.");
            }
        }
    }

    /**
     * @return {@link #id}
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the {@link #id} to the given argument.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return {@link #firstName}
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the {@link #firstName} to the given argument.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return {@link #lastName}
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the {@link #lastName} to the given argument.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return {@link #nick}
     */
    public String getNick() {
        return nick;
    }

    /**
     * Sets the {@link #nick} to the given argument.
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * @return {@link #email}
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the {@link #email} to the given argument.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return {@link #passwordHash}
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Sets the {@link #passwordHash} to the given argument.
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

}
