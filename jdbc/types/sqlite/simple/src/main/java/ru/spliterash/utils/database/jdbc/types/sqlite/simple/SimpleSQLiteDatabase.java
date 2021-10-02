package ru.spliterash.utils.database.jdbc.types.sqlite.simple;

import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.jdbc.types.sqlite.SQLiteDatabase;

import java.io.File;

public class SimpleSQLiteDatabase extends SQLiteDatabase<SimpleSQLiteConnectionProvider> {
    public SimpleSQLiteDatabase(SimpleSQLiteConnectionProvider simple) {
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
