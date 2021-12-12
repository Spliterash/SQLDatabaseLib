package ru.spliterash.utils.database.jdbc.types.sqlite.inMemory;

import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.base.objects.AbstractDatabase;
import ru.spliterash.utils.database.jdbc.types.sqlite.AbstractSQLiteDatabase;
import ru.spliterash.utils.database.jdbc.types.sqlite.SQLiteDatabase;
import ru.spliterash.utils.database.jdbc.types.sqlite.SimpleSQLiteConnectionProvider;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InMemorySQLiteDatabase extends AbstractDatabase implements SQLiteDatabase {
    private final SimpleSQLiteConnectionProvider connectionProvider;
    private final Executor asyncExecutor;
    private final Lock lock;
    private Connection connection;

    public InMemorySQLiteDatabase(File dbFile, Executor executor) {
        this.connectionProvider = new SimpleSQLiteConnectionProvider(Objects.requireNonNull(dbFile));
        this.asyncExecutor = Objects.requireNonNull(executor);
        this.lock = new ReentrantLock();
    }

    public InMemorySQLiteDatabase(File dbFile) {
        this(dbFile, Executors.newSingleThreadExecutor());
    }

    private boolean isActiveConnection() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    @Override
    public DatabaseSession createSession() {
        try {
            if (!isActiveConnection())
                recreateConnection();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
        return new InMemorySQLiteConnectionSession(connection, this);
    }

    private void recreateConnection() {
        connection = connectionProvider.getConnection();
    }

    @Override
    public void destroy() {
        try {
            lock.lock();
            if (isActiveConnection())
                connection.close();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String getType() {
        return AbstractSQLiteDatabase.TYPE;
    }

    public void save() {
        asyncExecutor.execute(() -> {
            lock.lock();
            try {
                if (isActiveConnection())
                    connection.commit();
            } catch (SQLException ex) {
                throw new DatabaseException(ex);
            } finally {
                lock.unlock();
            }
        });
    }
}
