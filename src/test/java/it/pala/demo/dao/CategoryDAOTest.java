package it.pala.demo.dao;

import it.pala.demo.Exceptions.DuplicateCategoryException;
import it.pala.demo.Exceptions.NoSuchCategoryException;
import it.pala.demo.beans.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDAOTest {

    private Connection connection;

    @BeforeEach
    void setUp(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            fail();
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_categories?serverTimezone=UTC",
                    "root",
                    "matteo");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            fail();
        }
    }
    @Test
    void getTree() {
        List<Category> categories = null;
        try {
             categories = new CategoryDAO(connection).getTree();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            fail();
        }
        assert categories != null;
        for(Category c : categories){
            System.out.println(c.toString());
        }
    }

    @Test
    void addNotFirst(){
        try {
            new CategoryDAO(connection).createCategory("Pasta", "Food");
        } catch (SQLException | NoSuchCategoryException | DuplicateCategoryException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void addFirst(){
        try {
            new CategoryDAO(connection).createCategory("F40", "Ferrari");
        } catch (SQLException | NoSuchCategoryException | DuplicateCategoryException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void findInvalidID(){
        assertThrows(NoSuchCategoryException.class, () -> new CategoryDAO(connection).findID("Apes"));
    }

    @Test
    void update(){
        try {
            new CategoryDAO(connection).updateCategory("11", "2");
        } catch (SQLException | NoSuchCategoryException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            fail();
        }
    }
}