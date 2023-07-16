package ru.spliterash.utils.database.jdbc.types.mysql.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.jdbc.JDBCCloseableConnectionProvider;
import ru.spliterash.utils.database.jdbc.types.mysql.MySQLConnectionProvider;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariMySQLConnectionProvider implements JDBCCloseableConnectionProvider, MySQLConnectionProvider {

    private final HikariDataSource hikariDataSource;

    public HikariMySQLConnectionProvider(
            String host,
            int port,
            String user,
            String password,
            String database,
            String driverName
    ) {
        HikariConfig config = new HikariConfig();
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName(driverName);

        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useServerPrepStmts", true);
        config.addDataSourceProperty("useLocalSessionState", true);
        config.addDataSourceProperty("rewriteBatchedStatements", true);
        config.addDataSourceProperty("cacheResultSetMetadata", true);
        config.addDataSourceProperty("cacheServerConfiguration", true);
        config.addDataSourceProperty("elideSetAutoCommits", true);
        config.addDataSourceProperty("maintainTimeStats", false);

        hikariDataSource = new HikariDataSource(config);
    }

    public HikariMySQLConnectionProvider(
            String host,
            int port,
            String user,
            String password,
            String database
    ) {
        this(host, port, user, password, database, "com.mysql.jdbc.Driver");
    }

    public HikariMySQLConnectionProvider(HikariConfig config) {
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
