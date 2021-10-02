package ru.spliterash.utils.database.jdbc;

import lombok.RequiredArgsConstructor;
import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.base.objects.QueryResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@RequiredArgsConstructor
public class JDBCSession implements DatabaseSession {
    protected final Connection connection;

    @Override
    public void close() throws DatabaseException {
        try {
            connection.close();
        } catch (SQLException cause) {
            throw new DatabaseException(cause);
        }
    }

    @Override
    public QueryResult query(String query, Object... args) {
        JDBCQueryInput input = JDBCUtils.makeQueryInput(query, args);
        return JDBCUtils.query(connection, input);
    }


    @Override
    public QueryResult query(String query, Map<String, Object> args) {
        JDBCQueryInput input = JDBCUtils.makeQueryInput(query, args);
        return JDBCUtils.query(connection, input);
    }

    @Override
    public int update(String query, Object... args) {
        JDBCQueryInput input = JDBCUtils.makeQueryInput(query, args);
        return JDBCUtils.update(connection, input);
    }

    @Override
    public int update(String query, Map<String, Object> args) {
        JDBCQueryInput input = JDBCUtils.makeQueryInput(query, args);
        return JDBCUtils.update(connection, input);
    }

}
