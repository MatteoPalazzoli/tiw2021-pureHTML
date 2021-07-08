package it.pala.demo.controllers;

import it.pala.demo.Exceptions.IllegalMoveException;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="MoveHere", value="/MoveHere")
public class MoveHere extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String fromId = StringEscapeUtils.escapeJava(request.getParameter("fromid"));
        String toId = StringEscapeUtils.escapeJava(request.getParameter("toid"));

        if(emptyField(new ArrayList<>(List.of(fromId, toId)))){
            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("errorMessage", "Missing or empty credentials.");
            templateEngine.process("/WEB-INF/index.html", ctx, response.getWriter());
        }

        CategoryDAO dao = new CategoryDAO(connection);
        try {
            dao.updateCategory(fromId, toId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not possible to retrieve the tree of categories");
            return;
        } catch (NoSuchCategoryException | IllegalMoveException | IndexOutOfBoundsException e) {
            ServletContext context = getServletContext();
            request.setAttribute("errorMessage", e.getMessage());
            RequestDispatcher dispatcher = context.getRequestDispatcher("/Home");
            dispatcher.forward(request, response);
            return;
        }

        response.sendRedirect(getServletContext().getContextPath()+"/Home");
    }
}
