package com.foxminded;

import java.sql.Connection;

public interface ConnectingPool {
    Connection getConnection();
    boolean releaseConnection(Connection connection);
    String getUrl();
    String getUser();
    String getPassword();
}
