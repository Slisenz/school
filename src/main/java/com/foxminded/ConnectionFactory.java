package com.foxminded;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

public class ConnectionFactory implements ConnectingPool{

    private String url;
    private String user;
    private String password;
    private List<Connection> connectionPool;
    private List<Connection> usedConnections = new ArrayList<>();
    private static final int INITIAL_POOL_SIZE=10;

    public static ConnectionFactory create(String username) {
        List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);
        Properties user = null;
        try {
            user = getProperties(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            pool.add(createConnection(user));
        }
        return new ConnectionFactory(user, pool);

    }

    public ConnectionFactory(Properties user, List<Connection> connectionPool) {
        this.url = user.getProperty("url");
        this.user = user.getProperty("username");
        this.password = user.getProperty("password");
        this.connectionPool = connectionPool;
    }

    @Override
    public Connection getConnection() {
        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    public static Connection createConnection(Properties user) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(user.getProperty("url"), user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public int getSize(){
        return connectionPool.size() + usedConnections.size();
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public String getUser() {
        return this.user;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public static Properties getProperties(String username) throws IOException {
        ClassLoader classLoader =Thread.currentThread().getContextClassLoader();
        InputStream inputStream;
        Properties user = new Properties();
        Properties properties = new Properties();
        try {
            inputStream = classLoader.getResourceAsStream("config.properties");
            properties.load(inputStream);
            user.put("username", properties.get("username." + username));
            user.put("password", properties.get("password." + username));
            user.put("url", properties.get("url." + username));
        } catch (IOException e) {
            logger.log(Level.WARNING, "loading property file failed! ", e);
            throw new IOException();
        }
        return user;
    }
}
