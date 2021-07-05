package it.pala.demo.controllers;

import it.pala.demo.utils.SessionChecker;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    }
}
