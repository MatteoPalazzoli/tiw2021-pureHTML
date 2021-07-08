package it.pala.demo.controllers;

import it.pala.demo.Exceptions.DuplicateCategoryException;
import it.pala.demo.Exceptions.NoSuchCategoryException;
import it.pala.demo.dao.CategoryDAO;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="AddCategory", value="/AddCategory")
public class AddCategory extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().println("getted");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = StringEscapeUtils.escapeJava(request.getParameter("name"));
        String father = StringEscapeUtils.escapeJava(request.getParameter("father"));
        String errorMsg = "";
        if(emptyField(List.of(name, father))){
            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("errorMessage", "Missing or empty credentials.");
            templateEngine.process("/index.html", ctx, response.getWriter());
            return;
        }
        CategoryDAO dao = new CategoryDAO(connection);
        try {
            dao.createCategory(name, father);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Couldn't add the new "+name+" category.");
            return;
        } catch (NoSuchCategoryException | IndexOutOfBoundsException e){
            errorMsg = e.getMessage();
        } catch (DuplicateCategoryException e){
            errorMsg = "Category "+name+" already exists.";
        }
        request.setAttribute("errorMessage", errorMsg);
        ServletContext context = getServletContext();
        RequestDispatcher dispatcher = context.getRequestDispatcher("/Home");
        dispatcher.forward(request, response);
    }
}
