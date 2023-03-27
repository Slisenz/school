package com.foxminded;


import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Properties;

public class Main {

    public static void main (String[] args) throws SQLException, ClassNotFoundException, IOException {
        SqlConnection sqlConnection = new SqlConnection();
        System.out.println(sqlConnection.getProperties("postgres"));
//        DataGeneration dataGeneration = new DataGeneration();
//        dataGeneration.generateData();
//        Menu menu = new Menu();
//        menu.showMenu(sqlConnection.getProperties("developer"));
    }
}
