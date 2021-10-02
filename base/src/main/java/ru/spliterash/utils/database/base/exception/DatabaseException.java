package ru.spliterash.utils.database.base.exception;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    public DatabaseException(SQLException cause) {
        super(cause);
    }
}
