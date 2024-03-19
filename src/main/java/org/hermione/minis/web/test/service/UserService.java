package org.hermione.minis.web.test.service;


import org.hermione.minis.beans.factory.annotation.Autowired;
import org.hermione.minis.jdbc.core.JdbcTemplate;
import org.hermione.minis.web.test.User;

import java.sql.ResultSet;
import java.util.Date;

@SuppressWarnings("SqlResolve")
public class UserService {
    @Autowired(value = "jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public User getUserInfo(int userid) {
        final String sql = "select id, name, age, birthday from users where id=?";
        return (User) jdbcTemplate.query(sql, new Object[]{userid},
                pstmt -> {
                    ResultSet rs = pstmt.executeQuery();
                    User rtnUser = null;
                    if (rs.next()) {
                        rtnUser = new User();
                        rtnUser.setId(userid);
                        rtnUser.setName(rs.getString("name"));
                        rtnUser.setAge(rs.getInt("age"));
                        rtnUser.setBirthday(new Date(rs.getDate("birthday").getTime()));
                    }
                    return rtnUser;
                }
        );
    }
}
