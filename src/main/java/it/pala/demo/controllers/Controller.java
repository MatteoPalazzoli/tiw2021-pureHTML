package it.pala.demo.controllers;

import it.pala.demo.utils.ConnectionHandler;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.Collection;

public class Controller extends HttpServlet {

    TemplateEngine templateEngine;
    Connection connection;
    static final String LOGIN_PAGE = "/index.html";

    @Override
    public void init() throws ServletException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    /**
     * If the user is not logged in (not present in session) redirect to the login
     * @param session user's session
     * @return true if logged, false otherwise
     */
    public static boolean notLogged(HttpSession session){
        return session.isNew() || session.getAttribute("user") == null;
    }

    public static boolean emptyField(Collection<String> params){
        for(String s : params){
            if(s == null || s.isEmpty()) return true;
        }
        return false;
    }
}
