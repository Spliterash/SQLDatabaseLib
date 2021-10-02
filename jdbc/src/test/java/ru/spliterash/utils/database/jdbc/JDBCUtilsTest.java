package ru.spliterash.utils.database.jdbc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


public class JDBCUtilsTest {
    @Test
    public void makeQueryInputSimpleArgsTest() {
        String originalSql = "SELECT * FROM A WHERE b = ? AND c in (?) limit ?";

        JDBCQueryInput resultQuery = JDBCUtils.makeQueryInput(originalSql, "b arg", Arrays.asList("1", "2,", 3), 10);

        assertThat("SELECT * FROM A WHERE b = ? AND c in (?,?,?) limit ?").isEqualTo(resultQuery.getQuery());
        assertThat(resultQuery.getArgs()).containsExactly("b arg", "1", "2,", 3, 10);
    }

    @Test
    public void makeQueryInputMapArgsTest() {
        String originalSql = "SELECT * FROM A WHERE b = :b AND c in (:c) limit :limit";

        Map<String, Object> params = new HashMap<>(3);

        params.put("b", "b arg");
        params.put("c", Arrays.asList("1", "2,", 3));
        params.put("limit", 10);

        JDBCQueryInput resultQuery = JDBCUtils.makeQueryInput(originalSql, params);

        assertThat(resultQuery.getQuery()).isEqualTo("SELECT * FROM A WHERE b = ? AND c in (?,?,?) limit ?");
        assertThat(new Object[]{"b arg", "1", "2,", 3, 10}).containsExactly(resultQuery.getArgs());
    }
}
