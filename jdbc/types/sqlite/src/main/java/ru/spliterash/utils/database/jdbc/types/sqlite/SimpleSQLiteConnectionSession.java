package ru.spliterash.utils.database.jdbc.types.sqlite;

import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.jdbc.JDBCSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class SimpleSQLiteConnectionSession extends JDBCSession {
    private boolean commitNeed = false;

    public SimpleSQLiteConnectionSession(Connection connection) {
        super(connection);
    }

    @Override
    public int update(String query, Object... args) {
        commitNeed = true;
        return super.update(query, args);
    }

    @Override
    public int update(String query, Map<String, Object> args) {
        commitNeed = true;
        return super.update(query, args);
    }

    @Override
    public int updateDto(String query, Object dto) {
        commitNeed = true;
        return super.updateDto(query, dto);
    }

    @Override
    public void close() throws DatabaseException {
        if (!commitNeed)
            return;
        try {
            this.connection.commit();
            super.close();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
