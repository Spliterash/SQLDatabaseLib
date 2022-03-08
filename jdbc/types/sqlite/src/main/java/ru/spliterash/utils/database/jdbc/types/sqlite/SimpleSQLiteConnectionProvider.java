package ru.spliterash.utils.database.jdbc.types.sqlite;

import lombok.RequiredArgsConstructor;
import ru.spliterash.utils.database.base.exception.DatabaseException;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
public class SimpleSQLiteConnectionProvider implements SQLiteConnectionProvider {
    private final File file;

    @Override
    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + file.getPath());
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
