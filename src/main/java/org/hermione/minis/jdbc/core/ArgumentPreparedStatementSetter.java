package org.hermione.minis.jdbc.core;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArgumentPreparedStatementSetter {
    private final Object[] args; //参数数组

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    //设置SQL参数
    public void setValues(PreparedStatement pstmt) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                doSetValue(pstmt, i + 1, arg);
            }
        }
    }

    //对某个参数，设置参数值
    protected void doSetValue(PreparedStatement pstmt, int parameterPosition, Object argValue) throws SQLException {
        //判断参数类型，调用相应的JDBC set方法
        if (argValue instanceof String) {
            pstmt.setString(parameterPosition, (String) argValue);
        } else if (argValue instanceof Integer) {
            pstmt.setInt(parameterPosition, (int) argValue);
        } else if (argValue instanceof java.util.Date) {
            pstmt.setDate(parameterPosition, new java.sql.Date(((java.util.Date) argValue).getTime()));
        }
    }
}
