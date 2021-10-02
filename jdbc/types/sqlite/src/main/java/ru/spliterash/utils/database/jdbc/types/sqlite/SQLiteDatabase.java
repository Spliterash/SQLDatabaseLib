package ru.spliterash.utils.database.jdbc.types.sqlite;

import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.jdbc.AbstractJDBCDatabase;

public abstract class SQLiteDatabase<T extends SQLiteConnectionProvider> extends AbstractJDBCDatabase<T> {
    public SQLiteDatabase(T jdbcConnectionProvider) {
        super(jdbcConnectionProvider);
    }

    @Override
    public String getType() {
        return "SQLite";
    }
}
