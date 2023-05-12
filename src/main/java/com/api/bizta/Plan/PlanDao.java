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
    private List<Interest> interest;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    // plan 추가 (interest 제외)
    public int insertPlanInfo(int userIdx, PlanInfo planInfo) {
        String insertPlanQuery = "INSERT INTO Plan(userIdx, country, city, hotel, transport, startDate, endDate, companionCnt) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] insertPlanParams = new Object[]{userIdx, planInfo.getCountry(), planInfo.getCity(), planInfo.getHotel(),
        planInfo.getTransport(), planInfo.getStartDate(), planInfo.getEndDate(), planInfo.getCompanionCnt()};
        this.jdbcTemplate.update(insertPlanQuery, insertPlanParams);

        String lastInsertIdxQuery = "select last_insert_id()"; // 마지막으로 삽입된 Idx 반환
        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    // plan 추가 (interest만)
    public int insertPlanInterest(int planIdx, Interest interest) {
        String insertPlanQuery = "INSERT INTO Interest(planIdx, interest) VALUES(?, ?);";
        Object[] insertPlanParams = new Object[]{planIdx, interest.getInterest()};
        return this.jdbcTemplate.update(insertPlanQuery, insertPlanParams);
    }

    // plan 수정 (interest 제외)
    public int modifyPlanInfo(int planIdx, PlanInfo planInfo) {
        String modifyPlanQuery = "UPDATE Plan SET country=?, city=?, hotel=?, transport=?,\n" +
                "                startDate=?, endDate=?, companionCnt=? WHERE planIdx=?;";
        Object[] modifyPlanParams = new Object[]{planInfo.getCountry(), planInfo.getCity(),
                planInfo.getHotel(), planInfo.getTransport(), planInfo.getStartDate(),
                planInfo.getEndDate(), planInfo.getCompanionCnt(), planIdx};
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

    //특정 plan 조회
    public PlanInfo getPlanInfo(int planIdx) {

        String getPlanInfoQuery =
                "SELECT userIdx, country, city, hotel, transport, startDate, endDate, companionCnt " +
                        "FROM Plan WHERE planIdx=? AND status = 'active';";
        String getInterestQuery = "SELECT interest FROM Interest WHERE planIdx=? AND status = 'active';";
        try {
            return this.jdbcTemplate.queryForObject(getPlanInfoQuery,

                    (rs, rsNum) -> new PlanInfo(
                            rs.getInt("userIdx"),
                            rs.getString("country"),
                            rs.getString("city"),
                            rs.getString("hotel"),
                            rs.getString("transport"),
                            rs.getString("startDate"),
                            rs.getString("endDate"),
                            rs.getInt("companionCnt"),

                            interest = this.jdbcTemplate.query(getInterestQuery, new Object[]{planIdx},
                                    (rk, rkNum) -> new Interest(
                                            rk.getString("interest")
                                    ))
                    ), planIdx);
        } catch (EmptyResultDataAccessException e) { // 쿼리문에 해당하는 결과가 없을 때
            return null;
        }

    }

/*
    private RowMapper<GetPlanInfo> planInfoRowMapper(){
        return new RowMapper<GetPlanInfo>() {
            @Override
            public GetPlanInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetPlanInfo planInfo = new GetPlanInfo();
                planInfo.setUserIdx(rs.getInt("userIdx"));
                planInfo.setPlanIdx(rs.getInt("planIdx"));
                planInfo.setCountry(rs.getString("country"));
                planInfo.setCity(rs.getString("city"));
                planInfo.setHotel(rs.getString("hotel"));
                planInfo.setTransport(rs.getString("transport"));
                planInfo.setStartDate(rs.getString("startDate"));
                planInfo.setEndDate(rs.getString("endDate"));
                return planInfo;
            }
        };
    }


    private RowMapper<Interest> InterestRowMapper(){
        return new RowMapper<Interest>() {
            @Override
            public Interest mapRow(ResultSet rs, int rowNum) throws SQLException {
                Interest interest = new Interest();
                interest.setInterest("interest");
                return interest;
            }
        }
    }

     */
}
