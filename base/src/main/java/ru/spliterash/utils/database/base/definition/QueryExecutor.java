package ru.spliterash.utils.database.base.definition;

import org.intellij.lang.annotations.Language;
import ru.spliterash.utils.database.base.objects.QueryResult;

import java.util.Map;

/**
 * Исполнитель запросов, штука которая выполняет запросы
 * <p>
 * Если используется метод с {@code Object... args}, то аргументы по очереди подставляются вместо знаков ?
 * <p>
 * Если используется метод с {@code Map<String,Object>}, то аргументы подставляются по имени с двоеточием: :arg1, :arg2 и прочие
 */
public interface QueryExecutor {
    QueryResult query(@Language("SQL") String query, Object... args);

    QueryResult query(@Language("SQL") String query, Map<String, Object> args);

    int update(@Language("SQL") String query, Object... args);

    int update(@Language("SQL") String query, Map<String, Object> args);
}
