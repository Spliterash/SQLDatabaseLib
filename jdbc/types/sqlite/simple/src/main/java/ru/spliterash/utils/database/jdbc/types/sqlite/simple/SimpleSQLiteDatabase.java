package ru.spliterash.utils.database.jdbc.types.sqlite.simple;

import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.jdbc.types.sqlite.AbstractSQLiteDatabase;
import ru.spliterash.utils.database.jdbc.types.sqlite.SQLiteConnectionProvider;
import ru.spliterash.utils.database.jdbc.types.sqlite.SimpleSQLiteConnectionProvider;
import ru.spliterash.utils.database.jdbc.types.sqlite.SimpleSQLiteConnectionSession;

import java.io.File;

public class SimpleSQLiteDatabase extends AbstractSQLiteDatabase<SQLiteConnectionProvider> {
    public SimpleSQLiteDatabase(SQLiteConnectionProvider simple) {
        super(simple);
    }

    public SimpleSQLiteDatabase(File file) {
        super(new SimpleSQLiteConnectionProvider(file));
    }


    @Override
    public DatabaseSession createSession() {
        return new SimpleSQLiteConnectionSession(connectionProvider.getConnection());
    }
}
