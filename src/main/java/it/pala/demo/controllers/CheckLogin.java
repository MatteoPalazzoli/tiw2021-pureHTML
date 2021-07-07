package it.pala.demo.controllers;

import it.pala.demo.Exceptions.WrongUserException;
import it.pala.demo.dao.UserDAO;
import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name="CheckLogin", value="/CheckLogin")
public class CheckLogin extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body><h1> get request</h1></body></html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String user, pwd, name = "";
        try {
            user = StringEscapeUtils.escapeJava(request.getParameter("user"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
            if(emptyField(List.of(user, pwd))){
                final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
                ctx.setVariable("errorMessage", "Missing or empty credentials.");
                templateEngine.process("/index.html", ctx, response.getWriter());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        // query db to authenticate for user
        UserDAO userDao = new UserDAO(connection);
        try {
            name = userDao.checkCredentials(user, pwd);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
            return;
        } catch (WrongUserException e){
            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("errorMessage", "Incorrect username or password");
            templateEngine.process("/index.html", ctx, response.getWriter());
        }

        request.getSession().setAttribute("user", name);
        response.sendRedirect(getServletContext().getContextPath()+"/Home");
    }

    @Override
    public void destroy() {

    }

}