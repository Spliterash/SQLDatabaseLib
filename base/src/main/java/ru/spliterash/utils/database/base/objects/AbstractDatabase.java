package ru.spliterash.utils.database.base.objects;

import lombok.RequiredArgsConstructor;
import ru.spliterash.utils.database.base.definition.Database;
import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.base.objects.QueryResult;

import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractDatabase implements Database {

    @Override
    public QueryResult query(String query, Object... args) {
        try (DatabaseSession session = createSession()) {
            return session.query(query, args);
        }
    }

    @Override
    public QueryResult query(String query, Map<String, Object> args) {
        try (DatabaseSession session = createSession()) {
            return session.query(query, args);
        }
    }

    @Override
    public int update(String query, Object... args) {
        try (DatabaseSession session = createSession()) {
            return session.update(query, args);
        }
    }

    @Override
    public int update(String query, Map<String, Object> args) {
        try (DatabaseSession session = createSession()) {
            return session.update(query, args);
        }
    }
}
