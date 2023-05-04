package com.api.bizta.Plan;


import com.api.bizta.Plan.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PlanDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    // plan 추가 (interest 제외)
    public int insertPlanInfo(int userIdx, PostPlansReq postPlansReq) {
        String insertPlanQuery = "INSERT INTO Plan(userIdx, country, city, hotel, transport, startDate, endDate, companionCnt) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] insertPlanParams = new Object[]{userIdx, postPlansReq.getCountry(), postPlansReq.getCity(), postPlansReq.getHotel(),
        postPlansReq.getTransport(), postPlansReq.getStartDate(), postPlansReq.getEndDate(), postPlansReq.getCompanionCnt()};
        this.jdbcTemplate.update(insertPlanQuery, insertPlanParams);

        String lastInsertIdxQuery = "select last_insert_id()"; // 마지막으로 삽입된 Idx 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    // plan 추가 (interest만)
    public int insertPlanInterest(int planIdx, PostInterestReq postInterestReq) {
        String insertPlanQuery = "INSERT INTO Interest(planIdx, interest) VALUES(?, ?);";
        Object[] insertPlanParams = new Object[]{planIdx, postInterestReq.getInterest()};
        return this.jdbcTemplate.update(insertPlanQuery, insertPlanParams);
    }
}