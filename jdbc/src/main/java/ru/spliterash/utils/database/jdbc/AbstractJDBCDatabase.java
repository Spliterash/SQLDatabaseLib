package ru.spliterash.utils.database.jdbc;

import lombok.RequiredArgsConstructor;
import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.base.objects.AbstractDatabase;

@RequiredArgsConstructor
public abstract class AbstractJDBCDatabase<T extends JDBCConnectionProvider> extends AbstractDatabase {
    protected final T connectionProvider;

    @Override
    public DatabaseSession createSession() {
        return new JDBCSession(connectionProvider.getConnection());
    }

    @Override
    public void destroy() {
        if (connectionProvider instanceof JDBCCloseableConnectionProvider) {
            ((JDBCCloseableConnectionProvider) connectionProvider).close();
        }
    }
}
