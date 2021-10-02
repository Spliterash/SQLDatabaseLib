package ru.spliterash.utils.database.jdbc.types.mysql.simple;

import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.jdbc.types.mysql.AbstractMySQLConnectionProvider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SimpleMySQLConnectionProvider extends AbstractMySQLConnectionProvider {

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC class not found: com.mysql.jdbc.Driver");
        }
    }

    public SimpleMySQLConnectionProvider(String host, int port, String user, String password, String database) {
        super(host, port, user, password, database);
    }

    @Override
    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    getJDBCString(),
                    user,
                    password
            );
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
