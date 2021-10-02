package ru.spliterash.utils.database.jdbc;

import lombok.experimental.UtilityClass;
import ru.spliterash.utils.database.base.exception.DatabaseException;
import ru.spliterash.utils.database.base.objects.QueryResult;
import ru.spliterash.utils.database.base.objects.ResultSetRow;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Тут немножко грязно
@UtilityClass
public class JDBCUtils {
    private static final Pattern namedParameterPattern = Pattern.compile("(?<!')(:[\\w]*)(?!')");
    private static final Pattern simpleParameterPattern = Pattern.compile("\\?");

    public QueryResult extractSet(ResultSet set) throws SQLException {
        List<ResultSetRow> list = new LinkedList<>();
        List<String> columns = getColumns(set);
        while (set.next()) {
            ResultSetRow.ResultSetRowBuilder row = ResultSetRow.builder();
            for (String column : columns) {
                row.addResultRow(column, set.getObject(column));
            }
            list.add(row.build());
        }
        return new QueryResult(Collections.unmodifiableList(list));
    }

    private List<String> getColumns(ResultSet set) throws SQLException {
        ResultSetMetaData meta = set.getMetaData();
        List<String> list = new LinkedList<>();
        for (int i = 0; i < meta.getColumnCount(); i++) {
            list.add(meta.getColumnName(i + 1));
        }
        return list;
    }

    public <T> void setValue(PreparedStatement statement, int i, T obj) throws SQLException {
        int index = i + 1;
        if (obj instanceof Date)
            statement.setLong(index, ((Date) obj).getTime() / 1000L);
        else
            statement.setObject(index, obj);
    }

    public QueryResult query(Connection connection, JDBCQueryInput input) {
        try (PreparedStatement prep = connection.prepareStatement(input.getQuery())) {
            for (int i = 0; i < input.getArgs().length; i++) {
                JDBCUtils.setValue(prep, i, input.getArgs()[i]);
            }

            try (ResultSet set = prep.executeQuery()) {
                return JDBCUtils.extractSet(set);
            }
        } catch (SQLException throwables) {
            throw new DatabaseException(throwables);
        }
    }

    public int update(Connection connection, JDBCQueryInput input) {
        try (PreparedStatement prep = connection.prepareStatement(input.getQuery())) {
            for (int i = 0; i < input.getArgs().length; i++) {
                JDBCUtils.setValue(prep, i, input.getArgs()[i]);
            }

            return prep.executeUpdate();
        } catch (SQLException throwables) {
            throw new DatabaseException(throwables);
        }
    }

    public JDBCQueryInput makeQueryInput(String query, Object... args) {
        if (Arrays.stream(args).noneMatch(a -> a.getClass().isArray() || a instanceof Collection<?>))
            return new JDBCQueryInput(query, args);

        List<Object> finalArgs = new ArrayList<>(args.length);
        StringBuilder builder = new StringBuilder(query.length() + 16);


        Matcher matcher = simpleParameterPattern.matcher(query);
        int oldEnd = 0;
        for (int i = 0; matcher.find(); i++) {
            oldEnd = processMatcher(builder, matcher, query, oldEnd, args[i], finalArgs);
        }

        return finalizeQuery(oldEnd, query, builder, finalArgs);
    }

    private static JDBCQueryInput finalizeQuery(int oldEnd, String query, StringBuilder builder, List<Object> finalArgs) {
        // Если не было ничо
        String finalQuery;
        if (oldEnd == 0)
            finalQuery = query;
        else
            finalQuery = builder.append(query, oldEnd, query.length()).toString();

        return JDBCQueryInput.builder()
                .query(finalQuery)
                .args(finalArgs.toArray())
                .build();
    }

    public JDBCQueryInput makeQueryInput(String query, Map<String, Object> args) {
        List<Object> finalArgs = new ArrayList<>(args.size());
        StringBuilder builder = new StringBuilder(query.length() + 16);


        Matcher matcher = namedParameterPattern.matcher(query);
        int oldEnd = 0;
        while (matcher.find()) {
            String id = matcher.group().substring(1);
            oldEnd = processMatcher(builder, matcher, query, oldEnd, args.get(id), finalArgs);
        }

        return finalizeQuery(oldEnd, query, builder, finalArgs);
    }

    private int processMatcher(StringBuilder builder, Matcher matcher, String query, int oldEnd, Object arg, List<Object> finalArgs) {
        builder.append(query, oldEnd, matcher.start());
        oldEnd = matcher.end();
        Collection<?> argCollection;
        if (arg.getClass().isArray())
            argCollection = Arrays.asList((Object[]) arg);
        else if (arg instanceof Collection)
            argCollection = (Collection<?>) arg;
        else
            argCollection = null;

        StringBuilder replaceTo = new StringBuilder();
        if (argCollection != null && argCollection.size() > 0) {
            finalArgs.addAll(argCollection);
            for (int k = 0; k < argCollection.size(); k++) {
                replaceTo.append('?');
                if (k < argCollection.size() - 1)
                    replaceTo.append(',');
            }
        } else {
            replaceTo.append('?');
            finalArgs.add(arg);
        }

        builder.append(replaceTo);

        return oldEnd;
    }
}
