package org.hermione.minis.batis;


import lombok.Getter;
import lombok.Setter;
import org.hermione.minis.jdbc.core.JdbcTemplate;
import org.hermione.minis.jdbc.core.PreparedStatementCallback;

@Getter
public class DefaultSqlSession implements SqlSession {

    @Setter
    private JdbcTemplate jdbcTemplate;

    @Setter
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Object selectOne(String sqlid, Object[] args, PreparedStatementCallback pstmtcallback) {
        String sql = this.sqlSessionFactory.getMapperNode(sqlid).getSql();
        return jdbcTemplate.query(sql, args, pstmtcallback);
    }

    private void buildParameter() {
    }

    private Object resultSet2Obj() {
        return null;
    }
}
