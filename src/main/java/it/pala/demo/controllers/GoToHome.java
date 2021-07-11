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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name="GoToHomePage", value={"/Home"})
public class GoToHome extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String id = request.getParameter("id");
        List<String> names;
        CategoryDAO dao = new CategoryDAO(connection);
        List<Category> categories;
        try{
            categories = dao.getTree();
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to retrieve the tree of categories");
            return;
        }
        String effId;
        if(id==null){
            effId="0";
            names = new ArrayList<>();
        } else {
            //names with the button
            effId=id;
            names = categories
                    .stream()
                    .filter(c -> c.getId().length()-id.length()<0 || !c.getId().startsWith(id))
                    .map(Category::getName)
                    .collect(Collectors.toList());
        }

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("tree", categories);
        ctx.setVariable("names", names);
        ctx.setVariable("id", effId);
        templateEngine.process("/WEB-INF/home.html", ctx, response.getWriter());
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
