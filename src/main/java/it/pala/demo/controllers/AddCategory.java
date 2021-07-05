package it.pala.demo.controllers;

import it.pala.demo.Exceptions.NoSuchCategoryException;
import it.pala.demo.dao.CategoryDAO;
import it.pala.demo.utils.SessionChecker;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="AddCategory", value="/AddCategory")
public class AddCategory extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!SessionChecker.isLogged(request.getSession())){
            response.sendRedirect( getServletContext().getContextPath()+LOGIN_PAGE);
        }
        response.getWriter().println("getted");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!SessionChecker.isLogged(request.getSession())){
            response.sendRedirect( getServletContext().getContextPath()+LOGIN_PAGE);
        }

        String name = request.getParameter("name");
        String father = request.getParameter("father");
        CategoryDAO dao = new CategoryDAO(connection);
        try {
            dao.createCategory(name, request.getParameter("father"));
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Couldn't add the new "+name+" category.");
            return;
        } catch (NoSuchCategoryException e){
            //fixme redirect to home instead of internal server error
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Category "+father+" doesn't exists.");
            return;
        }

        response.sendRedirect(getServletContext().getContextPath()+"/Home");
    }
}
