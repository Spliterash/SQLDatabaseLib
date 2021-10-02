package ru.spliterash.utils.database.base.definition;

public interface Database extends QueryExecutor {
    DatabaseSession createSession();

    void destroy();

    String getType();
}
