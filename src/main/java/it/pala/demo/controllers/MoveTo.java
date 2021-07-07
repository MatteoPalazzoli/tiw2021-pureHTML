package it.pala.demo.controllers;

import it.pala.demo.beans.Category;
import it.pala.demo.dao.CategoryDAO;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name="MoveTo", value="/MoveTo")
public class MoveTo extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        CategoryDAO dao = new CategoryDAO(connection);
        List<Category> categories;
        try{
            categories = dao.getTree();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to retrieve the tree of categories");
            return;
        }

        //names with the button
        List<String> names = categories
                .stream()
                .filter(c -> c.getId().length()-id.length()<0 || !c.getId().startsWith(id))
                .map(Category::getName)
                .collect(Collectors.toList());

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("tree", categories);
        ctx.setVariable("id", id);
        ctx.setVariable("names", names);
        templateEngine.process("/WEB-INF/moveto.html", ctx, response.getWriter());

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }
}
