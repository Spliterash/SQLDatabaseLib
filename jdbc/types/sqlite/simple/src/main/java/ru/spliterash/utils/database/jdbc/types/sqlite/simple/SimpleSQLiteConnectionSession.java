package ru.spliterash.utils.database.jdbc.types.sqlite.simple;

import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.jdbc.JDBCSession;

import java.sql.Connection;
import java.sql.SQLException;

public class SimpleSQLiteConnectionSession extends JDBCSession {
    public SimpleSQLiteConnectionSession(Connection connection) {
        super(connection);
    }

    @Override
    public void close() throws DatabaseException {
        try {
            this.connection.commit();
            super.close();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
