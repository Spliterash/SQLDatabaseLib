package ru.spliterash.utils.database.jdbc.types.sqlite;

import ru.spliterash.utils.database.jdbc.AbstractJDBCDatabase;

public abstract class AbstractSQLiteDatabase<T extends SQLiteConnectionProvider> extends AbstractJDBCDatabase<T> implements SQLiteDatabase {
    public static final String TYPE = "SQLite";

    public AbstractSQLiteDatabase(T jdbcConnectionProvider) {
        super(jdbcConnectionProvider);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
