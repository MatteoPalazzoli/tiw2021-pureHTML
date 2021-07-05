package it.pala.demo.utils;

import javax.servlet.http.HttpSession;

public class SessionChecker {

    private SessionChecker(){}

    /**
     * If the user is not logged in (not present in session) redirect to the login
     * @param session user's session
     * @return true if logged, false otherwise
     */
    public static boolean isLogged(HttpSession session){
        return !session.isNew() && session.getAttribute("user") != null;
    }
}
