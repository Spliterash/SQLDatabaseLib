package ru.spliterash.utils.database.base.definition;

import org.intellij.lang.annotations.Language;
import ru.spliterash.utils.database.base.objects.QueryResult;
import ru.spliterash.utils.database.base.utils.FillUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Исполнитель запросов, штука которая выполняет запросы
 * <p>
 * Если используется метод с {@code Object... args}, то аргументы по очереди подставляются вместо знаков ?
 * <p>
 * Если используется метод с {@code Map<String,Object>}, то аргументы подставляются по имени с двоеточием: :arg1, :arg2 и прочие
 * <p>
 * Если используется метод с dto, то объект автоматически преобразуется в Map, и подставляется в аргументы как с {@code Map<String,Object>}
 */
public interface QueryExecutor {
    QueryResult query(@Language("SQL") String query, Object... args);

    QueryResult query(@Language("SQL") String query, Map<String, Object> args);

    int update(@Language("SQL") String query, Object... args);

    int update(@Language("SQL") String query, Map<String, Object> args);

    default int updateDto(@Language("SQL") String query, Object dto) {
        try {
            return update(query, FillUtils.toMap(dto));
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    default QueryResult queryDto(@Language("SQL") String query, Object dto) {
        try {
            return query(query, FillUtils.toMap(dto));
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
