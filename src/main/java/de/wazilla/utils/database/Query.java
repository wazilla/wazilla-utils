package de.wazilla.utils.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public class Query {
    
    private final Supplier<Connection> connectionSupplier;

    public Query(Supplier<Connection> connectionSupplier) {
        this.connectionSupplier = connectionSupplier;
    }

    public Result execute() throws SQLException {
        return null;
    }

}
