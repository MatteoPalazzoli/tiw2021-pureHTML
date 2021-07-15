package it.pala.demo.controllers;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="Logout", value="/Logout")
public class Logout extends Controller{

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        //request.getSession().setAttribute("user", null);
        response.sendRedirect(getServletContext().getContextPath()+"/index.html");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}
