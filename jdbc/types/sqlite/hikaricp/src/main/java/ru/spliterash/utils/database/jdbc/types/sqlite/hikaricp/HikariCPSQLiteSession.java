package ru.spliterash.utils.database.jdbc.types.sqlite.hikaricp;

import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.jdbc.JDBCSession;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPSQLiteSession extends JDBCSession {
    public HikariCPSQLiteSession(Connection connection) {
        super(connection);
    }

    @Override
    public void close() throws DatabaseException {
        try {
            connection.commit();
            super.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
