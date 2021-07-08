package it.pala.demo.controllers;

import it.pala.demo.Exceptions.WrongUserException;
import it.pala.demo.dao.UserDAO;
import org.apache.commons.text.StringEscapeUtils;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name="CheckLogin", value={"/CheckLogin"})
public class CheckLogin extends Controller {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        String user, pwd, name = "";
        if(request.getSession().getAttribute("user") != null){
            response.sendRedirect(getServletContext().getContextPath()+"/Home");
            return;
        }
        try {
            user = StringEscapeUtils.escapeJava(request.getParameter("user"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
            if(emptyField(user) || emptyField(pwd)){
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
            return;
        }

        request.getSession().setAttribute("user", name);
        response.sendRedirect(getServletContext().getContextPath()+"/Home");
    }

    @Override
    public void destroy() {

    }

}