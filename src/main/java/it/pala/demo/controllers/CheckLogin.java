package it.pala.demo.controllers;

import it.pala.demo.Exceptions.WrongUserException;
import it.pala.demo.dao.UserDAO;
import it.pala.demo.utils.ConnectionHandler;
import org.apache.commons.lang3.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
    private String message;
    private Connection connection;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        connection = ConnectionHandler.getConnection(servletContext);

        //da dove va a prendere i dati (in questo caso dal context)
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);

        //luogo e suffisso delle pagine di template
        templateResolver.setPrefix("/WEB-INF/");
        templateResolver.setSuffix(".html");
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html><body><h1> get request</h1></body></html>");
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println();

        String user, pwd;
        try {
            user = StringEscapeUtils.escapeJava(request.getParameter("user"));
            pwd = StringEscapeUtils.escapeJava(request.getParameter("password"));
            if (user == null || pwd == null || user.isEmpty() || pwd.isEmpty()) {
                message = "Missing or empty credential value";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing credential value");
            return;
        }

        // query db to authenticate for user
        UserDAO userDao = new UserDAO(connection);
        try {
            message = userDao.checkCredentials(user, pwd);
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not Possible to check credentials");
        } catch (WrongUserException e){
            final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
            ctx.setVariable("errorMessage", "Incorrect username or password");
            templateEngine.process("/index.html", ctx, response.getWriter());
        }

        request.getSession().setAttribute("user", user);
        response.sendRedirect(getServletContext().getContextPath()+"/Home");
    }

    public void destroy() {
    }
}