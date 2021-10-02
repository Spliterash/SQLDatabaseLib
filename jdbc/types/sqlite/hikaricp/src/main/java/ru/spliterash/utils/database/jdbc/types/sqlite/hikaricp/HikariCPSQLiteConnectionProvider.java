package ru.spliterash.utils.database.jdbc.types.sqlite.hikaricp;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.jdbc.JDBCCloseableConnectionProvider;
import ru.spliterash.utils.database.jdbc.types.sqlite.SQLiteConnectionProvider;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPSQLiteConnectionProvider implements SQLiteConnectionProvider, JDBCCloseableConnectionProvider {

    private final HikariDataSource hikariDataSource;

    public HikariCPSQLiteConnectionProvider(File file) {
        HikariConfig config = new HikariConfig();
        config.setAutoCommit(false);
        config.setJdbcUrl("jdbc:sqlite:" + file.toPath());

        hikariDataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() {
        try {
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void close() {
        hikariDataSource.close();
    }
}
