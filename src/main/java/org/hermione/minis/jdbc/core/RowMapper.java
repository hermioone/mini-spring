package org.hermione.minis.jdbc.core;



import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Jdbc 返回的某一行数据
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
