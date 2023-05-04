//package com.api.bizta.User;
//
//import com.api.bizta.User.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Repository;
//
//import javax.sql.DataSource;
//
//@Repository
//public class UserDao {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public UserDao(DataSource dataSource) {
//        this.jdbcTemplate = new JdbcTemplate(dataSource);
//    }
//
//
//    public User insertUser(User user) {
//        String insertUserQuery = "insert into User (id, nickname,email,password,role,provider,provider_id) values (?,?,?,?,?,?,?)";
//        Object[] insertUserParams = new Object[]{user.getId(), user.getNickname(), user.getEmail(), user.getPassword(),
//                user.getRole(), user.getProvider(), user.getProvider_id()};
//
//        this.jdbcTemplate.update(insertUserQuery, insertUserParams);
//
//        Long lastInsertId = this.jdbcTemplate.queryForObject("select last_insert_id()", Long.class);
//
//        user.setUserIdx(lastInsertId);
//
//        return user;
//    }
//
//    public User selectByEmail(String email) {
//        String selectByEmailQuery = "select userIdx, id, nickname, email, password, country, role, provider, provider_id from User where email = ? and status = 'active'";
//        Object[] selectByEmailParams = new Object[]{email};
//        try {
//            return this.jdbcTemplate.queryForObject(selectByEmailQuery,
//                    (rs, rowNum) -> new User(
//                            rs.getLong("userIdx"),
//                            rs.getString("id"),
//                            rs.getString("nickname"),
//                            rs.getString("email"),
//                            rs.getString("password"),
//                            rs.getString("country"),
//                            rs.getString("role"),
//                            rs.getString("provider"),
//                            rs.getString("provider_id")),
//                    selectByEmailParams);
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }
//
//    public User selectById(Long userIdx) {
//        String selectByIdQuery = "select id,nickname,email,password,role, provider, provider_id from user where userIdx = ? and status = 'active'";
//        return this.jdbcTemplate.queryForObject(selectByIdQuery,
//                (rs, rowNum) -> new User(
//                        rs.getString("id"),
//                        rs.getString("nickname"),
//                        rs.getString("email"),
//                        rs.getString("password"),
//                        rs.getString("role"),
//                        rs.getString("provider"),
//                        rs.getString("provider_id")),
//                userIdx);
//    }
//
//    public int checkEmail(String email) {
//        String checkEmailQuery = "select exists(select email from User where email = ? and status = 'active')";
//        Object[] checkEmailParams = new Object[]{email};
//        return this.jdbcTemplate.queryForObject(checkEmailQuery, int.class, checkEmailParams);
//    }
//
//    public int checkId(String id){
//        String checkIdQuery = "select exists(select id from User where id = ? and status = 'active')";
//        Object[] checkIdParams = new Object[]{id};
//        return this.jdbcTemplate.queryForObject(checkIdQuery, int.class, checkIdParams);
//    }
//
//    public int checkUserIdx(Long userIdx) {
//        String checkUserIdxQuery = "select exists(select nickname from User where userIdx = ? and status = 'active')";
//        Long checkIdParam = userIdx;
//        return this.jdbcTemplate.queryForObject(checkUserIdxQuery, int.class, checkIdParam);
//    }
//
//}
//
package com.api.bizta.User;

import com.api.bizta.Place.model.GetPlaceInfo;
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
        String createUserQuery = "insert into User " +
                "(id, nickName, password, email, birth) " +
                "values (?,?,?,?,?); ";
        Object[] createUserParams = new Object[]{postUserReq.getId(), postUserReq.getNickName(),
                postUserReq.getPassword(), postUserReq.getEmail(), postUserReq.getBirth()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdxQuery = "select last_insert_id()";

        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    // 이메일 중복 확인
    public int checkEmail(String email) {
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email; // 해당(확인할) 이메일 값
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    // 아이디 중복 확인
    public int checkId(String id) {
        String checkIdQuery = "select exists(select id from User where id = ?)";
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
                "select userIdx, id, password, email, nickName " +
                        "from User where id = ? and status = 'active';";

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
                GetUserInfo userInfo = new GetUserInfo(rs.getInt("userIdx"), rs.getString("id"),
                        rs.getString("password"), rs.getString("email"), rs.getString("nickName"));
                return userInfo;
            }
        };
    }
}
