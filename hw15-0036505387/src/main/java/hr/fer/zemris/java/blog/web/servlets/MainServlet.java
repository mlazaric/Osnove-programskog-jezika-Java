package hr.fer.zemris.java.blog.web.servlets;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.blog.web.forms.LoginForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet which renders a login form and allows a {@link BlogUser} to login.
 * It also lists the registered {@link BlogUser}s.
 *
 * @author Marko Lazarić
 */
@WebServlet(urlPatterns = "/servleti/main")
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = -1922205196207147389L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<BlogUser> authors = DAOProvider.getDAO().listUsers();
        LoginForm form = new LoginForm();

        req.setAttribute("authors", authors);
        req.setAttribute("form", form);

        req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoginForm form = new LoginForm();

        form.loadFromHTTPRequest(req);

        form.validate();

        if (form.hasAnyErrors()) {
            req.setAttribute("form", form);
            req.getRequestDispatcher("/WEB-INF/pages/main.jsp").forward(req, resp);
            return;
        }

        req.getSession().setAttribute("current.user.id", form.getUser().getId());
        req.getSession().setAttribute("current.user.firstName", form.getUser().getFirstName());
        req.getSession().setAttribute("current.user.lastName", form.getUser().getLastName());
        req.getSession().setAttribute("current.user.nick", form.getUser().getNick());
        req.getSession().setAttribute("current.user.email", form.getUser().getEmail());

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }
}
