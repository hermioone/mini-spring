package org.hermione.minis.jdbc.core;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

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
                // 通过argumentSetter统一设置参数值
                ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
                argumentSetter.setValues(pstmt);
            }
            return pstmtcallback.doInPreparedStatement(pstmt);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        RowMapperResultSetExtractor<T> resultExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        ResultSet rs = null;

        try (Connection con = dataSource.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            //建立数据库连接

            //准备SQL命令语句
            //设置参数
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(pstmt);
            //执行语句
            rs = pstmt.executeQuery();

            //数据库结果集映射为对象列表，返回
            return resultExtractor.extractData(rs);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

}

