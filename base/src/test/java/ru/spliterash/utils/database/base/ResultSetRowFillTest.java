package ru.spliterash.utils.database.base;

import org.junit.jupiter.api.Test;
import ru.spliterash.utils.database.base.objects.ResultSetRow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultSetRowFillTest {
    @Test
    public void test() {
        ResultSetRow row = ResultSetRow.builder()
                .addResultRow("number", 1)
                .addResultRow("str", "Strrrrrr")
                .addResultRow("flt", 12.2F)
                .addResultRow("bln", true)
                .addResultRow("custom_name", "?????")
                .build();

        TestWrapper fill = row.fill(TestWrapper.class);

        assertEquals(1, fill.getNumber());
        assertEquals("Strrrrrr", fill.getStr());
        assertEquals(12.2D, fill.getFlt());
        assertTrue(fill.isBln());
        assertEquals("?????", fill.getRandomProperty());
    }
}
