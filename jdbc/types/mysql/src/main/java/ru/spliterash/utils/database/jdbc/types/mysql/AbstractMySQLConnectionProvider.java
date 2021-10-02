package ru.spliterash.utils.database.jdbc.types.mysql;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractMySQLConnectionProvider implements MySQLConnectionProvider {
    protected final String host;
    protected final int port;
    protected final String user;
    protected final String password;
    protected final String database;


    protected String getJDBCString() {
        return "jdbc:mysql://" + host + ":" + port + "/" + database;
    }
}
