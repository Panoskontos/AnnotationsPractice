package org.example.CustomDatabase;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

//Available choices are H2, derby, sqlite
@Database(dbtype="sqlite",name="newdb")
@Table(name="Admin")
public class MyCustomDB {

    @TableColumn(name="Name", type="Text", initialValue="someName")
    private String name;
    @TableColumn(name="Password", type="Text", initialValue="somePassword")
    private String password;


    public void createTableAndData(){
        try {
            Connection connection = connect();
            //            default
            String createTableSQL = "CREATE TABLE IF NOT EXISTS D_USER"
                    + "(ID INTEGER NOT NULL PRIMARY KEY,";
//                    + "USERNAME VARCHAR(20),"
//                    + "PASSWORD VARCHAR(20))";
            String insertSQL = "INSERT INTO D_USER VALUES(2,'PANTELIS','P12345')";


            // If there is table annotation
            if(MyCustomDB.class.isAnnotationPresent(Table.class)) {
                Table table = MyCustomDB.class.getAnnotation(Table.class);
                createTableSQL = "CREATE TABLE "+ table.name()
                        + "(ID INTEGER NOT NULL PRIMARY KEY,";
//                        + "USERNAME VARCHAR(20),"
//                        + "PASSWORD VARCHAR(20))";
                insertSQL = "INSERT INTO "+table.name()+" VALUES(2,";
            }

//            If there are custom columns get their names
            boolean ThereAreCustomColumns = false;
            String mySQLcommand ="";
            for(Field f: MyCustomDB.class.getDeclaredFields()){
                if(f.isAnnotationPresent(TableColumn.class)){
                    ThereAreCustomColumns=true;
                    TableColumn cf = f.getAnnotation(TableColumn.class);
                    System.out.println(cf.name()+cf.type());
                    mySQLcommand+= cf.name()+" ";
                    if(Objects.equals(cf.type(), "Text")){
                        mySQLcommand+="VARCHAR(20),";
                    }
                    insertSQL+="'"+cf.initialValue()+"',";
                }
            }
            mySQLcommand = mySQLcommand.substring(0, mySQLcommand.length() - 1);
            mySQLcommand+=")";
            createTableSQL+=mySQLcommand;
            System.out.println(createTableSQL);

            insertSQL = insertSQL.substring(0, insertSQL.length() - 1);
            insertSQL+=")";
            System.out.println(insertSQL);

            Statement statement = connection.createStatement();
            statement.executeUpdate(createTableSQL);
            statement.executeUpdate(insertSQL);
            statement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Connection connect(){

        //        default connection
        String connectionString = "jdbc:sqlite:javaprotal5.db";
        //        change the database type and name
        if(MyCustomDB.class.isAnnotationPresent(Database.class)){
//            System.out.println("Database annotation is present");
            Database db = MyCustomDB.class.getAnnotation(Database.class);
            if(Objects.equals(db.dbtype(), "sqlite")){
                connectionString = "jdbc:"+db.dbtype()+":"+db.name()+".db";
            }
            if(Objects.equals(db.dbtype(), "derby")){
                connectionString = "jdbc:"+db.dbtype()+":"+db.name()+";create=true";
            }
            if(Objects.equals(db.dbtype(), "h2")){
                connectionString = "jdbc:"+db.dbtype()+":~/"+db.name();
            }
//            System.out.println(connectionString);
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connection;
    }

    public void selectAll(){
        try {

            Connection connection = connect();
            Statement statement = connection.createStatement();
            String selectSQL = "select * from D_USER";
            // If there is table annotation
            if(MyCustomDB.class.isAnnotationPresent(Table.class)) {
                Table table = MyCustomDB.class.getAnnotation(Table.class);
                selectSQL = "select * from "+table.name();
            }
            ResultSet resultSet = statement.executeQuery(selectSQL);
            boolean ThereAreCustomColumns = false;
            List<String> list = new ArrayList<>();
            for(Field f: MyCustomDB.class.getDeclaredFields()){
                if(f.isAnnotationPresent(TableColumn.class)) {
                    ThereAreCustomColumns = true;
                    TableColumn cf = f.getAnnotation(TableColumn.class);
                    list.add(cf.name());
                }
            }
            if(ThereAreCustomColumns){
                while(resultSet.next()){
                    System.out.println(resultSet.getString(list.get(0))+","+resultSet.getString(list.get(1)));
                }
            }else {
                while(resultSet.next()){
                    System.out.println(resultSet.getString("USERNAME")+","+resultSet.getString("PASSWORD"));
                }
            }

            statement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertNewUser(int id, String username, String password){
        try {
            Connection connection = connect();
            String insertSQL = "INSERT INTO D_USER VALUES(?,?,?)";
            // If there is table annotation
            if(MyCustomDB.class.isAnnotationPresent(Table.class)) {
                Table table = MyCustomDB.class.getAnnotation(Table.class);
                insertSQL = "INSERT INTO "+table.name()+" VALUES(?,?,?)";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            int count = preparedStatement.executeUpdate();
            if(count>0){
                System.out.println(count+" record updated");
            }
            preparedStatement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteUser(int id){
        try {
            Connection connection = connect();
            String insertSQL = "DELETE FROM D_USER WHERE id = ?";
            // If there is table annotation
            if(MyCustomDB.class.isAnnotationPresent(Table.class)) {
                Table table = MyCustomDB.class.getAnnotation(Table.class);
                insertSQL = "DELETE FROM "+table.name()+" WHERE id = ?";
            }
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setInt(1, id);
            int count = preparedStatement.executeUpdate();
            if(count>0){
                System.out.println(count+" record deleted");
            }
            preparedStatement.close();
            connection.close();
            System.out.println("Done!");
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


}
