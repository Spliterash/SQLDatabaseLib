package ru.spliterash.utils.database.jdbc;

import java.sql.Connection;

public interface JDBCConnectionProvider {
    Connection getConnection();
}
