package ru.spliterash.utils.database.base;

import org.junit.jupiter.api.Test;
import ru.spliterash.utils.database.base.objects.ResultSetRow;
import ru.spliterash.utils.database.base.utils.FillUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FillUtilsTest {
    @Test
    public void fillTest() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Map<String, Object> map = new HashMap<>();
        map.put("number", 1);
        map.put("str", "Strrrrrr");
        map.put("flt", 12.2F);
        map.put("bln", true);
        map.put("custom_name", "?????");

        TestWrapper fill = FillUtils.create(TestWrapper.class, map);

        assertEquals(1, fill.getNumber());
        assertEquals("Strrrrrr", fill.getStr());
        assertEquals(12.2D, fill.getFlt());
        assertTrue(fill.isBln());
        assertEquals("?????", fill.getRandomProperty());
    }

    @Test
    public void toMapTest() throws InvocationTargetException, IllegalAccessException {
        TestObj obj = new TestObj();
        obj.setName(UUID.randomUUID().toString());
        obj.setA(UUID.randomUUID().toString());
        obj.setB(true);
        obj.setC(new Random().nextInt());
        obj.setD(new Random().nextDouble());
        obj.setF(Arrays.asList("1", "2", UUID.randomUUID().toString()));
        obj.setRandomValue(UUID.randomUUID().toString());

        Map<String, Object> map = FillUtils.toMap(obj);

        assertEquals(obj.getName(), map.get("name"));
        assertEquals(obj.getA(), map.get("a"));
        assertEquals(obj.isB(), map.get("b"));
        assertEquals(obj.getC(), map.get("c"));
        assertEquals(obj.getD(), map.get("d"));
        assertEquals(obj.getF(), map.get("f"));
        assertEquals(obj.getRandomValue(), map.get("g"));
        assertEquals(map.size(), 7);
    }
}
