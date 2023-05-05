package com.api.bizta.Plan;


import com.api.bizta.Plan.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PlanDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    // plan 추가 (interest 제외)
    public int insertPlanInfo(int userIdx, PostPlanReq postPlanReq) {
        String insertPlanQuery = "INSERT INTO Plan(userIdx, country, city, hotel, transport, startDate, endDate, companionCnt) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] insertPlanParams = new Object[]{userIdx, postPlanReq.getCountry(), postPlanReq.getCity(), postPlanReq.getHotel(),
        postPlanReq.getTransport(), postPlanReq.getStartDate(), postPlanReq.getEndDate(), postPlanReq.getCompanionCnt()};
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

    // plan 삭제
    public int deletePlan(int planIdx) {
        String deletePlanQuery = "UPDATE Plan SET status='deleted' WHERE planIdx=?;";
        Object[] deletePlanParams = new Object[]{planIdx};
        return this.jdbcTemplate.update(deletePlanQuery, deletePlanParams);
    }
}
