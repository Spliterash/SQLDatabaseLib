package ru.spliterash.utils.database.jdbc.types.mysql.hikari;

import ru.spliterash.utils.database.jdbc.types.mysql.AbstractMySQLDatabase;

public class HikariMySQLDatabase extends AbstractMySQLDatabase<HikariMySQLConnectionProvider> {
    public HikariMySQLDatabase(HikariMySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
    }
}
