package hr.fer.zemris.java.tecaj_13.web.servlets;

import hr.fer.zemris.java.tecaj_13.dao.DAOProvider;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;
import hr.fer.zemris.java.tecaj_13.web.forms.BlogUserForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/servleti/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogUserForm form = new BlogUserForm();

        req.setAttribute("form", form);

        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogUserForm form = new BlogUserForm();

        form.fillFromHTTPRequst(req);

        form.validate();

        if (form.hasAnyErrors()) {
            req.setAttribute("form", form);
            req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
            return;
        }

        BlogUser user = new BlogUser();

        form.fillEntity(user);

        DAOProvider.getDAO().persistUser(user);

        resp.sendRedirect(req.getContextPath() + "/servleti/main");
    }
}
