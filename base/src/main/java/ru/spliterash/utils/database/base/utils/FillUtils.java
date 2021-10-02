package ru.spliterash.utils.database.base.utils;

import lombok.experimental.UtilityClass;
import ru.spliterash.utils.database.base.objects.ResultSetRow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

@UtilityClass
public class FillUtils {

    @SuppressWarnings("unchecked")
    private static <T> Class<T> wrap(Class<T> c) {
        return c.isPrimitive() ? (Class<T>) PRIMITIVES_TO_WRAPPERS.get(c) : c;
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVES_TO_WRAPPERS;

    static {
        PRIMITIVES_TO_WRAPPERS = new HashMap<>();
        PRIMITIVES_TO_WRAPPERS.put(boolean.class, Boolean.class);
        PRIMITIVES_TO_WRAPPERS.put(byte.class, Byte.class);
        PRIMITIVES_TO_WRAPPERS.put(char.class, Character.class);
        PRIMITIVES_TO_WRAPPERS.put(double.class, Double.class);
        PRIMITIVES_TO_WRAPPERS.put(float.class, Float.class);
        PRIMITIVES_TO_WRAPPERS.put(int.class, Integer.class);
        PRIMITIVES_TO_WRAPPERS.put(long.class, Long.class);
        PRIMITIVES_TO_WRAPPERS.put(short.class, Short.class);
        PRIMITIVES_TO_WRAPPERS.put(void.class, Void.class);
    }

    public static Object create(final Class<?> clazz, ResultSetRow row)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {

        Object instance = clazz.getConstructor().newInstance();

        for (Map.Entry<String, Object> entry : row.getResult().entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();

            trySetProperty(instance, name, value);
        }

        return instance;
    }

    private static void trySetProperty(Object bean, String name, Object value) throws InvocationTargetException, IllegalAccessException {
        String setter = "set";

        String upperName = name.substring(0, 1).toUpperCase() + name.substring(1);
        String setterName = setter + upperName;

        Class<?> clazz = bean.getClass();

        Method method = Arrays.stream(clazz.getMethods())
                .filter(m -> m.getName().equals(setterName) || (m.isAnnotationPresent(SQLProperty.class) && m.getAnnotation(SQLProperty.class).value().equals(name)))
                .findFirst()
                .orElse(null);
        if (method == null) {
            Logger.getAnonymousLogger().warning("Cant find setter method " + setterName + " in class " + clazz.getName());
            return;
        }

        if (method.getParameterCount() == 0 || method.getParameterCount() > 1) {
            Logger.getAnonymousLogger().warning("Invalid " + setterName + " method parameters count in class " + clazz.getName());
            return;
        }


        setField(method, bean, value);
    }

    private static void setField(Method method, Object bean, Object value) throws InvocationTargetException, IllegalAccessException {
        Class<?> parameterType = wrap(method.getParameterTypes()[0]);

        if (parameterType.equals(String.class))
            method.invoke(bean, transform(parameterType, value, value::toString));
        else if (parameterType.equals(Integer.class))
            method.invoke(bean, transform(parameterType, value, () -> Integer.parseInt(value.toString())));
        else if (parameterType.equals(Double.class))
            method.invoke(bean, transform(parameterType, value, () -> Double.parseDouble(value.toString())));
        else if (parameterType.equals(Boolean.class))
            method.invoke(bean, transform(parameterType, value, () -> Boolean.parseBoolean(value.toString())));
        else if (parameterType.equals(Date.class))
            method.invoke(bean, transform(parameterType, value, () -> new Date(Long.parseLong(value.toString()))));
        else
            method.invoke(bean, transform(parameterType, value, () -> {
                Logger.getAnonymousLogger().warning("Unknown method " + method.getName() + "  parameter type: " + parameterType.getName());
                return null;
            }));
    }

    private static Object transform(Class<?> clazz, Object value, Supplier<?> getter) {
        if (clazz.isInstance(value))
            return value;
        else
            return getter.get();
    }
}
