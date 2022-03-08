package ru.spliterash.utils.database.jdbc.types.sqlite.hikaricp;

import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.jdbc.types.sqlite.AbstractSQLiteDatabase;
import ru.spliterash.utils.database.jdbc.types.sqlite.SimpleSQLiteConnectionSession;

import java.io.File;

public class HikariCPSQLiteDatabase extends AbstractSQLiteDatabase<HikariCPSQLiteConnectionProvider> {
    public HikariCPSQLiteDatabase(HikariCPSQLiteConnectionProvider provider) {
        super(provider);
    }

    public HikariCPSQLiteDatabase(File file) {
        super(new HikariCPSQLiteConnectionProvider(file));
    }

    @Override
    public DatabaseSession createSession() {
        return new SimpleSQLiteConnectionSession(connectionProvider.getConnection());
    }
}
