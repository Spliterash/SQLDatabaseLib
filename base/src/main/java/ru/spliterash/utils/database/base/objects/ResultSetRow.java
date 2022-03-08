package ru.spliterash.utils.database.base.objects;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import ru.spliterash.utils.database.base.utils.FillUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Map;

@Builder
public class ResultSetRow {
    @Getter
    @Singular("addResultRow")
    private final Map<String, Object> result;

    public Integer getInt(String key) {
        return (Integer) result.get(key);
    }

    public Integer getInt(int i) {
        return (Integer) getObject(i);
    }

    public String getString(String key) {
        return result.get(key).toString();
    }

    public String getString(int i) {
        return getObject(i).toString();
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

    public Instant getInstant(String key) {
        Object obj = getObject(key);

        return parseInstant(obj);
    }

    public Instant getInstant(int i) {
        Object obj = getObject(i);

        return parseInstant(obj);
    }

    private Instant parseInstant(Object obj) {
        if (obj == null)
            return null;

        if (obj instanceof Instant)
            return (Instant) obj;
        else if (obj instanceof Long)
            return Instant.ofEpochSecond((Long) obj);
        else if (obj instanceof String)
            try {
                long l = Long.parseLong(obj.toString());
                return Instant.ofEpochSecond(l);
            } catch (NumberFormatException ex) {
                return Instant.MIN;
            }
        else
            return Instant.MIN;
    }

    public long getLong(int i) {
        return (long) getObject(i);
    }

    public long getLong(String str) {
        return (long) getObject(str);
    }

    @Override
    public String toString() {
        return "[" + result.toString() + "]";
    }

    public <T> T fill(Class<T> clazz) {
        try {
            return FillUtils.create(clazz, this.result);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
