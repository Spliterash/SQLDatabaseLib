package ru.spliterash.utils.database.jdbc.types.sqlite.inMemory;

import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.jdbc.JDBCSession;

import java.sql.Connection;
import java.util.Map;

public class InMemorySQLiteConnectionSession extends JDBCSession {

    private final InMemorySQLiteDatabase db;
    private boolean needSave;

    public InMemorySQLiteConnectionSession(Connection connection, InMemorySQLiteDatabase db) {
        super(connection);
        this.db = db;
        this.needSave = false;
    }

    @Override
    public int update(String query, Object... args) {
        needSave = true;
        return super.update(query, args);
    }

    @Override
    public int update(String query, Map<String, Object> args) {
        needSave = true;
        return super.update(query, args);
    }

    @Override
    public void close() throws DatabaseException {
        if (needSave)
            db.save();
    }
}
