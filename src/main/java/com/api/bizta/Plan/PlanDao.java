package com.api.bizta.Plan;


import com.api.bizta.Plan.model.PostPlansReq;
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
        String insertPlanQuery = "INSERT INTO Plan(userIdx, country, city, hotel, companionCnt, startDate, endDate) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?)";
        Object[] insertPlanParams = new Object[]{userIdx, postPlansReq.getCountry(), postPlansReq.getCity(), postPlansReq.getHotel(),
        postPlansReq.getTransport(), postPlansReq.getStartDate(), postPlansReq.getEndDate(), postPlansReq.getCompanionCnt()};
        this.jdbcTemplate.update(insertPlanQuery, insertPlanParams);

        String lastInsertIdxQuery = "select last_insert_id()"; // 마지막으로 삽입된 Idx 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    // plan 추가 (interest만)
    public int insertPlanInterest(int planIdx, PostPlansReq postPlansReq) {
        String insertPlanQuery = "INSERT INTO ActivitiesPreference(planIdx, interest1, interest2, interest3, interest4, interest5) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        // interest1~5가 다 없을 경우
        //if (postPlansReq.)
        Object[] insertPlanParams = new Object[]{planIdx, postPlansReq.getInterest1(), postPlansReq.getInterest2(),
                postPlansReq.getInterest3(), postPlansReq.getInterest4(), postPlansReq.getInterest5()};
        return this.jdbcTemplate.update(insertPlanQuery, insertPlanParams);
    }
}
