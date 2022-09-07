package de.wazilla.utils.database;

import java.util.List;

public class Result {
    
    private String sql;
    private long duration;
    private List<String> columns;
    private List<Row> rows;

    public String getSql() {
        return sql;
    }

    public long getDuration() {
        return duration;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Row> getRows() {
        return rows;
    }

}
