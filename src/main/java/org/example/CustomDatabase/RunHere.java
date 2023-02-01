package org.example.CustomDatabase;

public class RunHere {
    public static void main(String[] args) {
//        System.out.println("hi");
        MyCustomDB mcdb = new MyCustomDB();
        mcdb.createTableAndData();
        mcdb.insertNewUser(3,"Panos","12345");
        mcdb.insertNewUser(4,"Nik","22222");
        mcdb.insertNewUser(5,"Logan","22222");
        mcdb.deleteUser(5);
        mcdb.selectAll();


    }
}
