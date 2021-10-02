package ru.spliterash.utils.database.jdbc.types.mysql;

import ru.spliterash.utils.database.base.objects.AbstractDatabase;
import ru.spliterash.utils.database.jdbc.AbstractJDBCDatabase;

public abstract class AbstractMySQLDatabase<T extends MySQLConnectionProvider> extends AbstractJDBCDatabase<T> {
    public AbstractMySQLDatabase(T connectionProvider) {
        super(connectionProvider);
    }

    @Override
    public String getType() {
        return "MySQL";
    }
}
