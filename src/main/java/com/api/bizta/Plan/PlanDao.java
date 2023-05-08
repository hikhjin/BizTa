package com.api.bizta.Plan;


import com.api.bizta.Plan.model.*;
import com.api.bizta.config.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import static com.api.bizta.config.BaseResponseStatus.DATABASE_ERROR;

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
    public int insertPlanInterest(int planIdx, InterestReq interestReq) {
        String insertPlanQuery = "INSERT INTO Interest(planIdx, interest) VALUES(?, ?);";
        Object[] insertPlanParams = new Object[]{planIdx, interestReq.getInterest()};
        return this.jdbcTemplate.update(insertPlanQuery, insertPlanParams);
    }

    // plan 수정 (interest 제외)
    public int modifyPlanInfo(PatchPlanReq patchPlanReq) {
        String modifyPlanQuery = "UPDATE Plan SET country=?, city=?, hotel=?, transport=?,\n" +
                "                startDate=?, endDate=?, companionCnt=? WHERE planIdx=?;";
        Object[] modifyPlanParams = new Object[]{patchPlanReq.getCountry(), patchPlanReq.getCity(),
                patchPlanReq.getHotel(), patchPlanReq.getTransport(), patchPlanReq.getStartDate(),
                patchPlanReq.getEndDate(), patchPlanReq.getCompanionCnt(), patchPlanReq.getPlanIdx()};
        this.jdbcTemplate.update(modifyPlanQuery, modifyPlanParams);

        String lastInsertIdxQuery = "select last_insert_id()"; // 마지막으로 삽입된 Idx 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    // 기존 interest status 변경
    public int deleteInterest(int planIdx) {
        String deletePlanQuery = "UPDATE Interest SET status='deleted' WHERE planIdx=?;";
        Object[] deletePlanParams = new Object[]{planIdx};
        return this.jdbcTemplate.update(deletePlanQuery, deletePlanParams);
    }

    /*
    일단 기존 interest 다 status 변경하고 새로 삽입하는 방식으로
    추후 수정 예정
    // plan 수정 (interest만)
    public int modifyPlanInterest(int planIdx, InterestReq interestReq) {
        String insertPlanQuery = "";
        Object[] insertPlanParams = new Object[]{planIdx, interestReq.getInterest()};
        return this.jdbcTemplate.update(insertPlanQuery, insertPlanParams);
    }

    // interest 이미 있는지 확인
    public int checkInterest(String interest, PatchPlanReq patchPlanReq){
        String checkInterest = "select exists(select ? from Interest where planIdx = ?);";
        Object[] checkInterestParams = new Object[]{interest, patchPlanReq.getPlansIdx()};
        return this.jdbcTemplate.queryForObject(checkInterest, int.class, checkInterestParams);

    }

     */

    // plan 삭제
    public int deletePlan(int planIdx) {
        String deletePlanQuery = "UPDATE Plan SET status='deleted' WHERE planIdx=?;";
        Object[] deletePlanParams = new Object[]{planIdx};
        return this.jdbcTemplate.update(deletePlanQuery, deletePlanParams);
    }
}
