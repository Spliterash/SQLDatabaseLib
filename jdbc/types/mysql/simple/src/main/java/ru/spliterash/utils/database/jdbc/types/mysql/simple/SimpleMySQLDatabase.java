package ru.spliterash.utils.database.jdbc.types.mysql.simple;

import ru.spliterash.utils.database.jdbc.types.mysql.AbstractMySQLDatabase;

public class SimpleMySQLDatabase extends AbstractMySQLDatabase<SimpleMySQLConnectionProvider> {
    public SimpleMySQLDatabase(SimpleMySQLConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public SimpleMySQLDatabase(String host, int port, String user, String password, String database) {
        super(new SimpleMySQLConnectionProvider(host, port, user, password, database));
    }
}
