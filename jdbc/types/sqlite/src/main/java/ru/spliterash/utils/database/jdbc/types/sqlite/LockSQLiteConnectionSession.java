package ru.spliterash.utils.database.jdbc.types.sqlite;

import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.jdbc.JDBCSession;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class LockSQLiteConnectionSession extends JDBCSession {
    private final Lock lock;
    private boolean commitNeed = false;

    public LockSQLiteConnectionSession(Connection connection, Lock lock) {
        super(connection);
        this.lock = lock;
    }

    @Override
    public int update(String query, Object... args) {
        initLock();
        return super.update(query, args);
    }

    @Override
    public int update(String query, Map<String, Object> args) {
        initLock();
        return super.update(query, args);
    }

    @Override
    public int updateDto(String query, Object dto) {
        initLock();
        return super.updateDto(query, dto);
    }

    private void initLock() {
        if (commitNeed)
            return;
        lock.lock();
        commitNeed = true;
    }

    @Override
    public void close() throws DatabaseException {
        if (!commitNeed)
            return;
        try {
            this.connection.commit();
            lock.unlock();
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
