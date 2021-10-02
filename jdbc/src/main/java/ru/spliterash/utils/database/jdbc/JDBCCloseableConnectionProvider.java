package ru.spliterash.utils.database.jdbc;

public interface JDBCCloseableConnectionProvider extends JDBCConnectionProvider {
    void close();
}
