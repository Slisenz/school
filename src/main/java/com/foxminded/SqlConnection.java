package com.foxminded;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class SqlConnection {

    Randomizer randomizer = new Randomizer();
    private static Logger logger =  Logger.getLogger(SqlConnection.class.getName());
    ConnectingPool dbCreateFactory = ConnectionFactory.create("postgres");
    ConnectingPool dbRequestFactory = ConnectionFactory.create("developer");

    public String sqlRequestByFile(Properties user, String scriptName ) throws SQLException, ClassNotFoundException, IOException {
        Connection connection = null;
        Statement sqlStatement = null;

        try {
            logger.fine("open connection");
            connection = dbCreateFactory.getConnection();
            try {
                logger.fine("create statement");
                sqlStatement = connection.createStatement();
                try {
                    sqlStatement.executeUpdate(readFile(scriptName));
                    logger.fine("sql execution successful");
                } catch (SQLException e) {
                    logger.warning(() -> "sql execution failed! " + scriptName + e);
                    throw new SQLException();
                }
            } finally {
                    try {
                        if (sqlStatement!=null) {
                            sqlStatement.close();
                        }
                    } catch (SQLException e) {
                        logger.warning(() -> "cannot close connection " + e);
                    }
                }
            } catch (SQLException e) {
                logger.warning(() -> "Connection error " + e);
                throw new SQLException();
            } finally {
                try {
                    if( connection!=null ) {
                        dbCreateFactory.releaseConnection(connection);
                        connection.close();

                    }
                    logger.log(Level.FINE, "connection closed");
                } catch (SQLException e) {
                    logger.severe(() -> "cannot close connection" + e);
                }
            }

        return "коннект прошел";
    }

    public String readFile(String fileName) throws IOException {
        String result;
        ClassLoader classLoader =Thread.currentThread().getContextClassLoader();
        try (InputStreamReader inputStreamReader = new InputStreamReader(Objects.requireNonNull(classLoader.getResourceAsStream(fileName)));
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            result = bufferedReader.lines().collect(Collectors.joining());
            logger.log(Level.INFO, ()-> "reading file: "+ fileName + "  successful");
        } catch (IOException exception) {
            logger.log(Level.WARNING, exception, ()-> "Reading file failed: " + fileName);
            throw new IOException();
        } catch (NullPointerException e){
            logger.log(Level.WARNING, e, ()-> "File not found!: " + fileName);
            throw new NullPointerException();
        }
        return result;
    }

    public boolean sqlExecute(String sqlExecute ) throws SQLException {

        Properties developer = new Properties();
        developer.put("user", "developer");
        developer.put("password", "12345");
        developer.put("url", "jdbc:postgresql://localhost:5432/school_db");
        Connection connection = null;
        Statement sqlStatement = null;
        try {
            connection = DriverManager.getConnection(developer.getProperty("url"), developer);
            sqlStatement = connection.createStatement();
            sqlStatement.executeUpdate(sqlExecute);
            logger.log(Level.INFO, ()-> "execution successful: " + sqlExecute);
        } catch (SQLException e) {
            logger.log(Level.WARNING, e, ()-> "file execution error: " + sqlExecute);
            throw new SQLException();
        } finally {
            if (sqlStatement!=null) {
                sqlStatement.close();
            }
            if (connection!=null) {
                connection.close();

            }
        }
        return true;
    }

    public ResultSet sqlTableExecute(Properties developer ,String sqlExecute) throws SQLException {
        Connection connection = null;
        ResultSet resultSet ;
        Statement sqlStatement = null;
        try {
            connection = DriverManager.getConnection(developer.getProperty("url") , developer);
            sqlStatement = connection.createStatement();
            resultSet = sqlStatement.executeQuery(sqlExecute);
            logger.log(Level.INFO, ()-> "execution successful: " + sqlExecute );
        } catch (SQLException e){
            logger.log(Level.WARNING, e, () -> "file execution error: " + sqlExecute);
            throw new SQLException();
        } finally {
            if (sqlStatement!=null) {
                sqlStatement.close();
            }
            assert connection!=null;
            connection.close();
        }
        return resultSet;
    }

}
