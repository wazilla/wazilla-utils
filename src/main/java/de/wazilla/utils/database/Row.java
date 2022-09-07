package de.wazilla.utils.database;

import java.util.List;

public class Row {
    
    private List<String> columns;
    private List<Object> values;
    
    protected Row(List<String> columns, List<Object> values) {
        this.columns = columns;
        this.values = values;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(int column) {
        return (T) this.values.get(column);
    }

    public <T> T getValue(String column) {
        int index = this.columns.indexOf(column);
        return getValue(index);
    }
    
    
}
