package ru.spliterash.utils.database.jdbc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@RequiredArgsConstructor
public class JDBCQueryInput {
    private final String query;
    private final Object[] args;
}
