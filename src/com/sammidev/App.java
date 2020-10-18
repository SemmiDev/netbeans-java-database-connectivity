package com.sammidev;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sammidev
 */
public class App {
    
    public static void main(String[] args) throws SQLException {
//    App app = new App();
//    Connection conn =  app.createConnection();
    
//        selectAll(conn);
//        selectMale(conn);
//        insert(conn);
//        update(conn);
//        delete(conn);

//        connectionMysql();
    }
    
    public static Scanner scanner() {
        Scanner scanner = new Scanner(System.in);
        return scanner;
    }
    
    
    public static void connectionMysql() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/golang", "root", "");
            System.out.println("berhasil");
            
            Statement statement = connection.createStatement();
            final String sql = "SELECT * FROM todo_models";
            ResultSet result = statement.executeQuery(sql);
            int count = 0;
            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("title");
                String email = result.getString("completed");
                Date createdAt = result.getDate("created_at");
                Date updatedAt = result.getDate("updated_at");
                Date deletedAt = result.getDate("deleted_at");
                System.out.println("name     : " + name);
                System.out.println("email    : " + email);
                System.out.println("created  : " + createdAt);
                System.out.println("updated  : " + updatedAt);
                System.out.println("deleted  : " + deletedAt);
                count++;
            }
            System.out.println("tersedia = " + count + " buah data");
            
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } 
    }
    
    public static Connection createConnection() {
        final String url = "jdbc:postgresql://localhost:5432/swing";
        final String user = "postgres";
        final String password = "sammidev";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("CONNECTED");
            return connection;
        } catch (SQLException e) {
            System.out.println("CONNECTION FAILED : " + " URL SALAH / USER SALAH / PASSWORD SALAH");
        }
        return null;
    }
    
    public static void selectAll(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        final String sql = "SELECT * FROM person";
        ResultSet result = statement.executeQuery(sql);
        int count = 0;
        while (result.next()) {
            Integer id = result.getInt("ID");
            String name = result.getString("NAME");
            String email = result.getString("EMAIL");
            String gender = result.getString("GENDER");
            returnPerson(id,name, email, gender);
            count++;
        }
        System.out.println("tersedia = " + count + " buah data");
    }
    
    public static void returnPerson(Integer id,String name,String email,String gender) {
        System.out.println("{'id': " + id + ", 'name': " + name + ", 'email': " + email + ", 'gender': " + gender + "}");
    }
    
    public static void selectMale(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        final String sql = "SELECT * FROM person where gender like 'male'";
        ResultSet result = statement.executeQuery(sql);
        int count = 0;
        while (result.next()) {
            Integer id = result.getInt("ID");
            String name = result.getString("NAME");
            String email = result.getString("EMAIL");
            String gender = result.getString("GENDER");
            returnPerson(id,name, email, gender);
            count++;
        }
        System.out.println("tersedia = " + count + " buah data");
    }
    
    public static Integer getById(Connection connection) throws SQLException {
        selectAll(connection);
        Integer id;
        
        System.out.print("ID : ");
        id = scanner().nextInt();
        
        Statement statement = connection.createStatement();
        final String searchbyID = "SELECT * FROM person where id = " + id;
        ResultSet result = statement.executeQuery(searchbyID);
        if (result.next()) {
            Integer idnya = result.getInt("ID");
            String name = result.getString("NAME");
            String email = result.getString("EMAIL");
            String gender = result.getString("GENDER");
            returnPerson(idnya, name, email, gender);
        }
        return id;
    }
    
    public static void insert(Connection connection) throws SQLException {
        Integer id;
        String name,email,gender;
        final String sql = "insert into person (id,name,email,gender) values (?,?,?,?)";

        System.out.print("ID     : ");
        id = scanner().nextInt();

        System.out.print("NAME   : ");
        name = scanner().nextLine();

        System.out.print("EMAIL  : ");
        email = scanner().nextLine();

        System.out.print("GENDER : ");
        gender = scanner().nextLine();
        
        PreparedStatement prepared = connection.prepareStatement(sql);
        prepared.setInt(1, id);
        prepared.setString(2, name);
        prepared.setString(3, email);
        prepared.setString(4, gender);
        prepared.executeUpdate();
        System.out.println("'status': 'CREATED'");
        returnPerson(id,name, email, gender);  
    }
    
    public static void update(Connection connection) throws SQLException {
        
        getById(connection);
        
        final String sql = "UPDATE PERSON SET NAME = ?, EMAIL = ?, GENDER = ? WHERE ID = ?";
        final PreparedStatement prepare = connection.prepareStatement(sql);
        
        String name, email, gender;
        
        System.out.print("NAME   : ");
        name = scanner().nextLine();

        System.out.print("EMAIL  : ");
        email = scanner().nextLine();

        System.out.print("GENDER : ");
        gender = scanner().nextLine();
        
        prepare.setString(1, name);
        prepare.setString(2, email);
        prepare.setString(3, gender);
        prepare.setInt(4, getById(connection));
        prepare.executeUpdate();
        System.out.println("'status': 'OK'");
        selectAll(connection);
    }
    
    public static void delete(Connection connection) throws SQLException {
        int id = getById(connection);
        
        final PreparedStatement prepare = connection.prepareStatement("DELETE FROM PERSON WHERE ID = ?");
        prepare.setInt(1, id);
        prepare.executeUpdate();
        System.out.println("'status': 'DELETED'");
        selectAll(connection);
        
    }
}