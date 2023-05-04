package com.api.bizta.Event;

import com.api.bizta.Event.model.GetEventInfo;
import com.api.bizta.Event.model.PatchEventReq;
import com.api.bizta.Event.model.PostEventReq;
import com.api.bizta.Place.model.GetPlaceInfo;
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
public class EventDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    // event 생성
    public int makeEvent(PostEventReq postEventReq){
                String makeEventQuery = "insert into Event " +
                "(planIdx, userIdx, title, date, startTime, endTime, description) " +
                "values (?,?,?,?,?,?,?); ";
        Object[] makeEventParams = new Object[]{postEventReq.getPlanIdx(), postEventReq.getUserIdx(), postEventReq.getTitle(),
        postEventReq.getDate(), postEventReq.getStartTime(), postEventReq.getEndTime(), postEventReq.getDescription()};

        this.jdbcTemplate.update(makeEventQuery, makeEventParams);

        String lastInsertIdxQuery = "select last_insert_id()";

        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    // planIdx 와 eventIdx 로 중복되는 event 시간이 있는지 확인, 없으면 0 리턴
    public int checkDuplicateTime(int planIdx, int userIdx, String date, String startTime, String endTime){

        String checkDuplicateTimeQuery = "SELECT COUNT(*) " +
                "FROM Event " +
                "WHERE planIdx = ? " +
                "AND userIdx = ? " +
                "AND date = ? " +
                "AND EXISTS (" +
                "SELECT 1 " +
                "FROM Event " +
                "WHERE planIdx = ? " +
                "AND userIdx = ? " +
                "AND date = ? " +
                "AND ((startTime <= ? AND ? < endTime) OR " +
                "(startTime < ? AND ? <= endTime) OR " +
                "(? <= startTime AND endTime <= ?)) " +
                ")";

        Object[] checkDuplicateTimeParams = new Object[]{planIdx, userIdx, date, planIdx, userIdx, date, startTime, startTime, endTime, endTime, startTime, endTime};

        return this.jdbcTemplate.queryForObject(checkDuplicateTimeQuery,
                int.class,
                checkDuplicateTimeParams);
    }

    // 특정 이벤트 조회
    public GetEventInfo getEventInfo(int eventIdx) {

        String getEventInfoQuery =
                "select planIdx, userIdx, title, date, startTime, endTime, description " +
                        "from Event where eventIdx = ? and status = 'active';";

        try {
            return this.jdbcTemplate.queryForObject(getEventInfoQuery, eventInfoRowMapper(), eventIdx);
        } catch (EmptyResultDataAccessException e) { // 쿼리문에 해당하는 결과가 없을 때
            return null;
        }

    }
    private RowMapper<GetEventInfo> eventInfoRowMapper(){
        return new RowMapper<GetEventInfo>() {
            @Override
            public GetEventInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetEventInfo eventInfo = new GetEventInfo();
                eventInfo.setPlanIdx(rs.getInt("planIdx"));
                eventInfo.setUserIdx(rs.getInt("userIdx"));
                eventInfo.setTitle(rs.getString("title"));
                eventInfo.setDate(rs.getString("date"));
                eventInfo.setStartTime(rs.getString("startTime"));
                eventInfo.setEndTime(rs.getString("endTime"));
                eventInfo.setDescription(rs.getString("description"));
                return eventInfo;
            }
        };
    }

    // event 수정
    public void modifyEvent(int eventIdx, PatchEventReq patchEventReq){
        String modifyEventQuery = "update Event " +
                "set " +
                "title = coalesce(?, title), " +
                "date = coalesce(?, date), " +
                "startTime = coalesce(?, startTime), " +
                "endTime = coalesce(?, endTime), " +
                "description = coalesce(?, description) "  +
                "where eventIdx = ?";
        Object[] modifyEventParams = new Object[]{patchEventReq.getTitle(), patchEventReq.getDate(), patchEventReq.getStartTime(),
        patchEventReq.getEndTime(), patchEventReq.getDescription(), eventIdx};

        jdbcTemplate.update(modifyEventQuery, modifyEventParams);
    }

    public int checkEventExist(int eventIdx){
        String checkEventExistQuery = "select exists(select eventIdx from Event where eventIdx = ?)";
            int checkEventExistParams = eventIdx;
            return this.jdbcTemplate.queryForObject(checkEventExistQuery,
                    int.class,
                    checkEventExistParams);

    }
}
