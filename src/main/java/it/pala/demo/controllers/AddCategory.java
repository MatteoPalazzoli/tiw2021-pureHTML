package it.pala.demo.controllers;

import it.pala.demo.Exceptions.DuplicateCategoryException;
import it.pala.demo.Exceptions.NoSuchCategoryException;
import it.pala.demo.dao.CategoryDAO;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="AddCategory", value="/AddCategory")
public class AddCategory extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(getServletContext().getContextPath()+"/Home");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String name = request.getParameter("name");
        String father = request.getParameter("father");
        String errorMsg = "";
        if(emptyField(name) || emptyField(father)){
            ServletContext context = getServletContext();
            request.setAttribute("errorMessage", "Missing or empty inputs.");
            RequestDispatcher dispatcher = context.getRequestDispatcher("/Home");
            dispatcher.forward(request, response);
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
