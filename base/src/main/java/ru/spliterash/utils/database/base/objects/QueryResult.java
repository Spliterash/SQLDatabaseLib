package ru.spliterash.utils.database.base.objects;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class QueryResult implements Iterable<ResultSetRow> {
    private final List<ResultSetRow> rows;

    public Optional<ResultSetRow> first() {
        return Optional.ofNullable(get(0));
    }

    public ResultSetRow get(int i) {
        if (i < 0)
            return null;

        if (i >= rows.size())
            return null;

        return rows.get(i);
    }

    public int size() {
        return rows.size();
    }

    @NotNull
    @Override
    public Iterator<ResultSetRow> iterator() {
        return rows.iterator();
    }

    public Stream<ResultSetRow> stream() {
        return rows.stream();
    }
}
