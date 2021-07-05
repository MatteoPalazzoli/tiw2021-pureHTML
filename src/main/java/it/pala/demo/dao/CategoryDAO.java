package it.pala.demo.dao;

import it.pala.demo.Exceptions.DuplicateCategoryException;
import it.pala.demo.Exceptions.NoSuchCategoryException;
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
    public String findID(String name) throws NoSuchCategoryException {
        String query = "SELECT ID FROM category WHERE Name = ?";
        try (PreparedStatement pStatement = connection.prepareStatement(query)) {
            pStatement.setString(1, name);
            try(ResultSet result = pStatement.executeQuery()){
                result.next();
                return result.getString("ID");
            }
        } catch (SQLException e){
            throw new NoSuchCategoryException("Category "+name+" doesn't exists.");
        }
    }

    public String findNextIdByFather(String father) throws SQLException, NoSuchCategoryException {
        String subQuery = "(SELECT ID FROM category WHERE Name = ?)";
        try (PreparedStatement pStatement = connection.prepareStatement(subQuery)) {
            pStatement.setString(1, father);
            subQuery = pStatement.toString().substring(43);
        }
        String query = "SELECT CAST((SELECT MAX(ID) FROM category WHERE ID LIKE CONCAT("+subQuery+", \"_\")) AS UNSIGNED) + 1 AS NextChildID";
        ResultSet result;
        try (Statement statement = connection.prepareStatement(query)) {
            result = statement.executeQuery(query);
            result.next();
            String id = result.getString("NextChildID");
            return id==null ? findID(father)+"1" : id;
        }
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

    public void createCategory(String name, String father) throws SQLException, NoSuchCategoryException, DuplicateCategoryException {
        String query = "INSERT INTO category (ID, Name, Father) VALUES (?, ?, ?)";
        if(isPresent(name)) throw new DuplicateCategoryException();
        try(PreparedStatement pStatement = connection.prepareStatement(query)){
            pStatement.setString(1, findNextIdByFather(father));
            pStatement.setString(2, name);
            pStatement.setString(3, father);
            pStatement.executeUpdate();
        }
    }

    private boolean isPresent(String name) throws SQLException {
        String query = "SELECT * FROM category WHERE Name = ?";
        ResultSet set;
        try(PreparedStatement s = connection.prepareStatement(query)){
            s.setString(1, name);
            set = s.executeQuery();
            return set.next();
        }
    }
}
