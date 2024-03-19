package org.hermione.minis.web.test.service;


import org.hermione.minis.beans.factory.annotation.Autowired;
import org.hermione.minis.jdbc.core.JdbcTemplate;
import org.hermione.minis.jdbc.core.RowMapper;
import org.hermione.minis.web.test.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@SuppressWarnings("SqlResolve")
public class UserService {
    @Autowired(value = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<User> getUserInfo(int userid) {
        final String sql = "select id, name, age, birthday from users where id=?";
        return jdbcTemplate.query(sql, new Object[]{userid},
                new RowMapper<User>() {
                    public User mapRow(ResultSet rs, int i) throws SQLException {
                        User rtnUser = new User();
                        rtnUser.setId(rs.getInt("id"));
                        rtnUser.setName(rs.getString("name"));
                        rtnUser.setAge(rs.getInt("age"));
                        rtnUser.setBirthday(new Date(rs.getDate("birthday").getTime()));
                        return rtnUser;
                    }
                }
        );
    }
}
