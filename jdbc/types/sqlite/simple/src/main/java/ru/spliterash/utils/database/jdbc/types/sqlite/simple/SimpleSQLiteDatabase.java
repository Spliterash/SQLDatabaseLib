package ru.spliterash.utils.database.jdbc.types.sqlite.simple;

import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.jdbc.types.sqlite.AbstractSQLiteDatabase;
import ru.spliterash.utils.database.jdbc.types.sqlite.LockSQLiteConnectionSession;
import ru.spliterash.utils.database.jdbc.types.sqlite.SQLiteConnectionProvider;
import ru.spliterash.utils.database.jdbc.types.sqlite.SimpleSQLiteConnectionProvider;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleSQLiteDatabase extends AbstractSQLiteDatabase<SQLiteConnectionProvider> {
    private final Lock lock = new ReentrantLock();
    private Connection connection;

    public SimpleSQLiteDatabase(SQLiteConnectionProvider simple) {
        super(simple);
        connection = simple.getConnection();
    }

    public SimpleSQLiteDatabase(File file) {
        this(new SimpleSQLiteConnectionProvider(file));
    }


    @Override
    public DatabaseSession createSession() {
        return new LockSQLiteConnectionSession(getConnection(), lock);
    }

    private Connection getConnection() {
        try {
            if (connection == null || connection.isClosed())
                connection = connectionProvider.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    @Override
    public void destroy() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        super.destroy();
    }
}
