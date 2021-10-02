package ru.spliterash.utils.database.base.objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import ru.spliterash.utils.database.base.utils.FillUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Builder
public class ResultSetRow {
    @Getter
    @Singular("addResultRow")
    private final Map<String, Object> result;

    public Integer getInt(String key) {
        return (Integer) result.get(key);
    }

    public String getString(String key) {
        return result.get(key).toString();
    }

    public String getString(int i) {
        return getObject(i).toString();
    }

    public Integer getInt(int i) {
        return (Integer) getObject(i);
    }

    public Object getObject(String key) {
        return result.get(key);
    }

    public Object getObject(int i) {
        int k = -1;
        for (Map.Entry<String, Object> entry : result.entrySet()) {
            if (++k == i) {
                return entry.getValue();
            }
        }
        return null;
    }

    public long getLong(int i) {
        return (long) getObject(i);
    }

    @Override
    public String toString() {
        return "[" + result.toString() + "]";
    }

    public <T> T fill(Class<T> clazz) {
        try {
            //noinspection unchecked
            return (T) FillUtils.create(clazz, this);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
