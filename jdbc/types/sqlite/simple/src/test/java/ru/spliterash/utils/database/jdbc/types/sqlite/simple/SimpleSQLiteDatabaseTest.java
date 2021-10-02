package ru.spliterash.utils.database.jdbc.types.sqlite.simple;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.spliterash.utils.database.base.definition.Database;
import ru.spliterash.utils.database.base.definition.DatabaseSession;
import ru.spliterash.utils.database.base.objects.QueryResult;
import ru.spliterash.utils.database.base.objects.ResultSetRow;
import ru.spliterash.utils.database.jdbc.types.sqlite.simple.SimpleSQLiteDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("LanguageMismatch")
public class SimpleSQLiteDatabaseTest {
    @Test
    public void testDBBase(@TempDir Path dir) throws IOException {
        File dbFile = new File(dir.toFile(), "base.db");

        Database database = new SimpleSQLiteDatabase(dbFile);

        String createScript = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("CreateTest.sql"), StandardCharsets.UTF_8);

        database.update(createScript);

        List<String> params = IntStream.range(0, 4).mapToObj(a -> UUID.randomUUID().toString()).collect(Collectors.toList());

        database.update("INSERT INTO test_table (id, column1, column2, column3) values (?,?,?,?)", params.toArray());

        QueryResult result = database.query("SELECT id, column1, column2, column3 FROM test_table where id=?", params.get(0));

        ResultSetRow row = result.first().orElseThrow(AssertionError::new);

        for (int i = 0; i < 4; i++) {
            assertEquals(params.get(i), row.getString(i));
        }

        database.destroy();
    }

    @Test
    public void testPerformance(@TempDir Path dir) throws IOException {
        File dbFile = new File(dir.toFile(), "base.db");

        Database database = new SimpleSQLiteDatabase(dbFile);

        String createScript = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("CreateTest.sql"), StandardCharsets.UTF_8);

        database.update(createScript);
        {
            long startDef = System.currentTimeMillis();
            for (int i = 0; i < 1000; i++) {
                List<String> params = IntStream.range(0, 4).mapToObj(a -> UUID.randomUUID().toString()).collect(Collectors.toList());
                database.update("INSERT INTO test_table (id, column1, column2, column3) values (?,?,?,?)", params.toArray());
            }
            System.out.println("Without session: " + (System.currentTimeMillis() - startDef));
            System.out.println("File size: " + dbFile.length());
        }
        {
            long startDef = System.currentTimeMillis();
            try (DatabaseSession session = database.createSession()) {
                for (int i = 0; i < 1000; i++) {
                    List<String> params = IntStream.range(0, 4).mapToObj(a -> UUID.randomUUID().toString()).collect(Collectors.toList());
                    session.update("INSERT INTO test_table (id, column1, column2, column3) values (?,?,?,?)", params.toArray());
                }
            }
            System.out.println("Session: " + (System.currentTimeMillis() - startDef));
            System.out.println("File size: " + dbFile.length());
        }

        database.destroy();
    }
}
