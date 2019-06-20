package hr.fer.zemris.java.blog.web.servlets;

import hr.fer.zemris.java.blog.dao.DAOProvider;
import hr.fer.zemris.java.blog.model.BlogComment;
import hr.fer.zemris.java.blog.model.BlogEntry;
import hr.fer.zemris.java.blog.model.BlogUser;
import hr.fer.zemris.java.blog.web.forms.BlogCommentForm;
import hr.fer.zemris.java.blog.web.forms.BlogEntryForm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/servleti/author/*")
public class AuthorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            resp.sendError(404, "Author not found.");
            return;
        }

        String[] parts = pathInfo.split("/");

        // First character is / so, the first String in parts is ""

        // "" and "NICK"
        if (parts.length == 2) {
            listBlogsForAuthor(req, resp, parts[1]);
        }
        else if (parts.length == 3) {
            if ("new".equals(parts[2])) {
                showCreateBlogEntryForm(req, resp, parts[1]);
            }
            else {
                showBlogEntry(req, resp, parts[1], parts[2]);
            }
        }
        else if (parts.length == 4 && "edit".equals(parts[3])) {
            showEditBlogEntryForm(req, resp, parts[1], parts[2]);
        }
        else {
            resp.sendError(404, "File not found.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            resp.sendError(404, "Author not found.");
            return;
        }

        String[] parts = pathInfo.split("/");

        // First character is / so, the first String in parts is ""

        if (parts.length == 3) {
            if ("new".equals(parts[2])) {
                createBlogEntry(req, resp, parts[1]);
            }
            else {
                createBlogEntryComment(req, resp, parts[1], parts[2]);
            }
        }
        else if (parts.length == 4 && "edit".equals(parts[3])) {
            editBlogEntry(req, resp, parts[1], parts[2]);
        }
        else {
            resp.sendError(404, "File not found.");
        }
    }

    private void listBlogsForAuthor(HttpServletRequest req, HttpServletResponse resp, String nick) throws ServletException, IOException {
        List<BlogEntry> entries = DAOProvider.getDAO().listEntriesForUser(nick);

        req.setAttribute("nick", nick);
        req.setAttribute("entries", entries);

        req.getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);
    }

    private void showCreateBlogEntryForm(HttpServletRequest req, HttpServletResponse resp, String nick) throws ServletException, IOException {
        if (req.getSession().getAttribute("current.user.id") == null) {
            resp.sendRedirect(req.getContextPath() + "/servleti/register");
            return;
        }

        BlogEntryForm form = new BlogEntryForm();

        req.setAttribute("form", form);

        req.getRequestDispatcher("/WEB-INF/pages/entries/form.jsp").forward(req, resp);
    }

    private void createBlogEntry(HttpServletRequest req, HttpServletResponse resp, String part) throws ServletException, IOException {
        if (req.getSession().getAttribute("current.user.id") == null) {
            resp.sendRedirect(req.getContextPath() + "/servleti/register");
            return;
        }

        BlogEntryForm form = new BlogEntryForm();

        form.loadFromHTTPRequest(req);
        form.validate();

        if (form.hasAnyErrors()) {
            req.setAttribute("form", form);

            req.getRequestDispatcher("/WEB-INF/pages/entries/form.jsp").forward(req, resp);

            return;
        }

        BlogEntry entry = new BlogEntry();

        form.saveToEntity(entry);

        BlogUser user = DAOProvider.getDAO().getBlogUser((Long) req.getSession().getAttribute("current.user.id"));

        entry.setCreator(user);

        DAOProvider.getDAO().persistEntry(entry);

        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + entry.getCreator().getNick() + "/" + entry.getId());
    }

    private void showEditBlogEntryForm(HttpServletRequest req, HttpServletResponse resp, String nick, String eid) throws ServletException, IOException {
        Long entryID;

        try {
            entryID = Long.valueOf(eid);
        }
        catch (NumberFormatException e) {
            resp.sendError(404, "Blog entry not found.");
            return;
        }

        BlogEntry entry = DAOProvider.getDAO().getBlogEntry(entryID);

        if (req.getSession().getAttribute("current.user.id") == null ||
           !req.getSession().getAttribute("current.user.id").equals(entry.getCreator().getId())) {

            resp.sendRedirect(req.getContextPath() + "/servleti/register");
            return;
        }

        BlogEntryForm form = new BlogEntryForm();

        form.loadFromEntity(entry);

        req.setAttribute("form", form);

        req.getRequestDispatcher("/WEB-INF/pages/entries/form.jsp").forward(req, resp);
    }

    private void editBlogEntry(HttpServletRequest req, HttpServletResponse resp, String part, String eid) throws IOException, ServletException {
        Long entryID;

        try {
            entryID = Long.valueOf(eid);
        }
        catch (NumberFormatException e) {
            resp.sendError(404, "Blog entry not found.");
            return;
        }

        BlogEntry entry = DAOProvider.getDAO().getBlogEntry(entryID);

        if (req.getSession().getAttribute("current.user.id") == null ||
                !req.getSession().getAttribute("current.user.id").equals(entry.getCreator().getId())) {

            resp.sendRedirect(req.getContextPath() + "/servleti/register");
            return;
        }

        BlogEntryForm form = new BlogEntryForm();

        form.loadFromHTTPRequest(req);

        if (form.hasAnyErrors()) {
            req.setAttribute("form", form);

            req.getRequestDispatcher("/WEB-INF/pages/entries/form.jsp").forward(req, resp);
            return;
        }

        form.saveToEntity(entry);

        resp.sendRedirect(req.getContextPath() + "/servleti/author/" + entry.getCreator().getNick() + "/" + entry.getId());
    }

    private void showBlogEntry(HttpServletRequest req, HttpServletResponse resp, String nick, String eid) throws ServletException, IOException {
        Long entryID;

        try {
            entryID = Long.valueOf(eid);
        }
        catch (NumberFormatException e) {
            resp.sendError(404, "Blog entry not found.");
            return;
        }

        BlogCommentForm form = new BlogCommentForm();
        BlogEntry entry = DAOProvider.getDAO().getBlogEntry(entryID);

        if (entry == null || !entry.getCreator().getNick().equals(nick)) {
            resp.sendError(404, "Blog entry not found.");
            return;
        }

        req.setAttribute("entry", entry);
        req.setAttribute("form", form);

        req.getRequestDispatcher("/WEB-INF/pages/entries/show.jsp").forward(req, resp);
    }

    private void createBlogEntryComment(HttpServletRequest req, HttpServletResponse resp, String nick, String eid) throws ServletException, IOException {
        BlogCommentForm form = new BlogCommentForm();

        form.loadFromHTTPRequest(req);

        form.validate();
        System.out.println(form.getErrors());

        if (form.hasAnyErrors()) {
            BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.valueOf(eid));

            req.setAttribute("entry", entry);
            req.setAttribute("form", form);

            req.getRequestDispatcher("/WEB-INF/pages/author.jsp").forward(req, resp);
            return;
        }

        BlogEntry entry = DAOProvider.getDAO().getBlogEntry(Long.valueOf(eid));
        BlogComment comment = new BlogComment();

        form.saveToEntity(comment);
        comment.setBlogEntry(entry);

        DAOProvider.getDAO().persistComment(comment);

        resp.sendRedirect(req.getRequestURI());
    }

}
