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

@WebServlet(name="MoveHere", value="/MoveHere")
public class MoveHere extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!SessionChecker.isLogged(request.getSession())){
            response.sendRedirect( getServletContext().getContextPath()+LOGIN_PAGE);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(!SessionChecker.isLogged(request.getSession())){
            response.sendRedirect( getServletContext().getContextPath()+LOGIN_PAGE);
        }

        String fromId = request.getParameter("fromid");
        String toId = request.getParameter("toid");

        CategoryDAO dao = new CategoryDAO(connection);
        try {
            dao.updateCategory(fromId, toId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to retrieve the tree of categories");
            return;
        } catch (NoSuchCategoryException e) {
            System.out.println(e.getMessage());
            //TODO handling
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to retrieve the tree of categories");
            return;
        }

        final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        templateEngine.process("/Home", ctx, response.getWriter());
    }
}
