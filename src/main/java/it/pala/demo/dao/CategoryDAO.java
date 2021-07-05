package it.pala.demo.dao;

import it.pala.demo.beans.Category;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class CategoryDAO {

    private Connection connection;

    public CategoryDAO(Connection connection){
        this.connection = connection;
    }

    /**
     * @param name Name of a category
     * @return Category's ID
     * @throws SQLException if there is a database error
     */
    public String findID(String name) throws SQLException {
        String query = "SELECT ID FROM category WHERE Name = ?";
        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            pStatement.setString(1, name);
            try(ResultSet result = pStatement.executeQuery()){
                result.next();
                return result.getString("ID");
            }
        }
    }

    public String findNextIdByFather(String father) throws SQLException {
        String query = "SELECT CAST(" +
                "(SELECT MAX(ID) FROM category WHERE ID LIKE CONCAT(" +
                "(SELECT ID FROM category WHERE Name = ?), \"_\"" +
                ")) AS UNSIGNED) + 1 AS LastChildID";
        ResultSet result;
        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            pStatement.setString(1, father);
            result = pStatement.executeQuery(query);
        }
        if(result.isBeforeFirst()){
            return findID(father)+"1";
        }
        result.next();
        return result.getString("LastChildID");
    }

    /**
     * Retrieves the complete tree sorted by ID and returns it as a list.
     * @return Sorted List of categories
     * @throws SQLException if there is a database error
     */
    public List<Category> getTree() throws SQLException {
        List<Category> categories = new LinkedList<>();
        String query = "SELECT ID, Name FROM category WHERE ID <> 0 ORDER BY ID";
        ResultSet result;
        //no need for preparing
        try (Statement statement = connection.prepareStatement(query)) {
            result = statement.executeQuery(query);
            while(result.next()){
                categories.add(new Category(
                        result.getString("ID"),
                        result.getString("Name"), ""));
            }
        }
        return categories;
    }

    public void createCategory(String name, String father) throws SQLException{
        String query = "INSERT INTO category (ID, Name, Father) VALUES (?, ?, ?)";
        try(PreparedStatement pStatement = connection.prepareStatement(query)){
            pStatement.setString(1, findNextIdByFather(father));
            pStatement.setString(2, name);
            pStatement.setString(3, father);
            pStatement.executeQuery();
        }
    }
}
