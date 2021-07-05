package it.pala.demo.controllers;

import it.pala.demo.beans.Category;
import it.pala.demo.dao.CategoryDAO;
import it.pala.demo.utils.SessionChecker;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="MoveTo", value="/MoveTo")
public class MoveTo extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!SessionChecker.isLogged(request.getSession())){
            response.sendRedirect( getServletContext().getContextPath()+LOGIN_PAGE);
        }
        String name = request.getParameter("name");

        CategoryDAO dao = new CategoryDAO(connection);
        List<Category> categories;
        try{
            categories = dao.getTree();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to retrieve the tree of categories");
            return;
        }

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("tree", categories);
        ctx.setVariable("name", name);
        templateEngine.process("/moveto.html", ctx, response.getWriter());

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }
}
