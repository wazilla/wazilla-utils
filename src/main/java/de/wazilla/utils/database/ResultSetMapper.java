package de.wazilla.utils.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper {
    
    void map(ResultSet rs) throws SQLException;

}
