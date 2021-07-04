package it.pala.demo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection con;
    public UserDAO(Connection connection) {
        this.con = connection;
    }

    public String checkCredentials(String username, String pwd) throws SQLException {
        String query = "SELECT  id, username, name, surname FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query)) {
            pstatement.setString(1, username);
            pstatement.setString(2, pwd);
            try (ResultSet result = pstatement.executeQuery()) {
                if (!result.isBeforeFirst()) // no results, credential check failed
                    return "check failed";
                else {
                    result.next();
                    return result.getInt("id")+" "
                            +result.getString("username")+" "
                            +result.getString("name")+" "
                            +result.getString("surname");
                }
            }
        }
    }
}
