package org.hermione.minis.jdbc.core;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Getter
@SuppressWarnings("SqlSourceToSinkFlow")
@Slf4j
public class JdbcTemplate {

    @Setter
    private DataSource dataSource;

    public Object query(StatementCallback stmtcallback) {
        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            return stmtcallback.doInStatement(stmt);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    public Object query(String sql, Object[] args, PreparedStatementCallback pstmtcallback) {

        try (Connection con = dataSource.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof String) {
                        pstmt.setString(i + 1, (String) arg);
                    } else if (arg instanceof Integer) {
                        pstmt.setInt(i + 1, (int) arg);
                    } else if (arg instanceof java.util.Date) {
                        pstmt.setDate(i + 1, new java.sql.Date(((java.util.Date) arg).getTime()));

                    }
                }
            }
            return pstmtcallback.doInPreparedStatement(pstmt);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return null;

    }

}

