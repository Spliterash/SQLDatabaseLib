package ru.spliterash.utils.database.jdbc.types.sqlite;

import ru.spliterash.utils.database.jdbc.AbstractJDBCDatabase;

public abstract class SQLiteDatabase<T extends SQLiteConnectionProvider> extends AbstractJDBCDatabase<T> {
    public static final String TYPE = "SQLite";

    public SQLiteDatabase(T jdbcConnectionProvider) {
        super(jdbcConnectionProvider);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
