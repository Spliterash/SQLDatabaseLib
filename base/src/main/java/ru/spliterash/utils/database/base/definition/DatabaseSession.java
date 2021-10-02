package ru.spliterash.utils.database.base.definition;

import ru.spliterash.utils.database.base.exception.DatabaseException;

/**
 * Интерфейс, предназначенный для исполнения сразу нескольких запросов в одном месте,
 * чтобы не закрывать Connection у базы данных
 * <p>
 * Пример
 * <pre>
 * {@code
 * try(DatabaseSession session = db.createSession ()) {
 *     List<ResultSetRow> list = session.select("SELECT * FROM ...");
 *     session.update("DROP WHERE a = ?",list.stream().map(s->s.getA()).collect(Collectors.toList()))
 * }
 * }
 * </pre>
 */
public interface DatabaseSession extends QueryExecutor, AutoCloseable {
    @Override
    void close() throws DatabaseException;
}
