package it.pala.demo.dao;

import it.pala.demo.Exceptions.WrongUserException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection con;
    public UserDAO(Connection connection) {
        this.con = connection;
    }

    public String checkCredentials(String username, String pwd) throws SQLException, WrongUserException {
        String query = "SELECT  username, password, name FROM user WHERE Username = ? AND Password = ?";
        try (PreparedStatement pstatement = con.prepareStatement(query)) {
            pstatement.setString(1, username);
            pstatement.setString(2, pwd);
            ResultSet result = pstatement.executeQuery();
            if (!result.isBeforeFirst()) // no results, credential check failed
                throw new WrongUserException();
            else {
                result.next();
                return result.getString("name");
            }
        }
    }
}
