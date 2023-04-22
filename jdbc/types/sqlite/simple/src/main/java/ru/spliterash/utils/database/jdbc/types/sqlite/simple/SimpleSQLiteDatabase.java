package ru.spliterash.utils.database.jdbc.types.sqlite.simple;

import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.jdbc.types.sqlite.AbstractSQLiteDatabase;
import ru.spliterash.utils.database.jdbc.types.sqlite.LockSQLiteConnectionSession;
import ru.spliterash.utils.database.jdbc.types.sqlite.SQLiteConnectionProvider;
import ru.spliterash.utils.database.jdbc.types.sqlite.SimpleSQLiteConnectionProvider;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimpleSQLiteDatabase extends AbstractSQLiteDatabase<SQLiteConnectionProvider> {
    private final Lock lock = new ReentrantLock();
    public SimpleSQLiteDatabase(SQLiteConnectionProvider simple) {
        super(simple);
    }

    public SimpleSQLiteDatabase(File file) {
        super(new SimpleSQLiteConnectionProvider(file));
    }


    @Override
    public DatabaseSession createSession() {
        return new LockSQLiteConnectionSession(connectionProvider.getConnection(), lock);
    }
}
