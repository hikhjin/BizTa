
package com.api.bizta.User;


import com.api.bizta.Place.model.*;
import com.api.bizta.User.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // 회원가입
    // userIdx가 자꾸 1로 나와서 우선 코드 수정
    public int createUser(PostUserReq postUserReq) {
//        String createUserQuery = "insert into User " +
//                "(id, nickName, password, email, birth) " +
//                "values (?,?,?,?,?); ";
        String createUserQuery = "insert into \"USER\"" +
                "(\"ID\", \"NICKNAME\", \"PASSWORD\", \"EMAIL\", \"BIRTH\") " +
                "values (?,?,?,?,?);";
        Object[] createUserParams = new Object[]{postUserReq.getId(), postUserReq.getNickName(),
                postUserReq.getPassword(), postUserReq.getEmail(), postUserReq.getBirth()};

        KeyHolder keyHolder = new GeneratedKeyHolder();
        this.jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(createUserQuery, Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < createUserParams.length; i++) {
                ps.setObject(i + 1, createUserParams[i]);
            }
            return ps;
        }, keyHolder);

        int userIdx = (int)keyHolder.getKeys().get("USERIDX");
        System.out.println("dao userIdx: " + userIdx);
        return userIdx;
//        int userIdx = this.jdbcTemplate.update(createUserQuery, createUserParams);
//        return userIdx;
    }

    // 이메일 중복 확인
    public int checkEmail(String email) {
//        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailQuery = "select exists(select 'EMAIL' from \"USER\" where email = ?)";
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    // 아이디 중복 확인
    public int checkId(String id) {
//        String checkIdQuery = "select exists(select id from User where id = ?)";
        String checkIdQuery = "select exists(select 'ID' from \"USER\" where id = ?)";
        String checkIdParams = id; // 해당(확인할) id 값
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                checkIdParams);
    }

    // userIdx로 회원이 존재하는지 확인
    public int checkUserExist(int userIdx) {
        String checkUserExistQuery = "select exists(select userIdx from User where userIdx = ?)";
        int checkUserExistParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserExistQuery,
                int.class,
                checkUserExistParams);
    }

    public GetUserInfo getUserInfo(PostLoginReq postLoginReq) {

        String getUserInfoQuery =
//                "select userIdx, id, password, email, nickName " +
//                        "from User where id = ? and status = 'active';";
        "select \"USERIDX\", \"ID\", \"PASSWORD\", \"EMAIL\", \"NICKNAME\" " +
                "from \"USER\" where \"ID\" = ? and \"STATUS\" = 'active';";

        try {
            return this.jdbcTemplate.queryForObject(getUserInfoQuery, userInfoRowMapper(), postLoginReq.getId());
        } catch (EmptyResultDataAccessException e) { // 쿼리문에 해당하는 결과가 없을 때
            return null;
        }
    }

    private RowMapper<GetUserInfo> userInfoRowMapper(){
        return new RowMapper<GetUserInfo>() {
            @Override
            public GetUserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
//                GetUserInfo userInfo = new GetUserInfo(rs.getInt("userIdx"), rs.getString("id"),
//                        rs.getString("password"), rs.getString("email"), rs.getString("nickName"));
                GetUserInfo userInfo = new GetUserInfo(rs.getInt("USERIDX"), rs.getString("ID"),
                        rs.getString("PASSWORD"), rs.getString("EMAIL"), rs.getString("NICKNAME"));
                return userInfo;
            }
        };
    }
}
