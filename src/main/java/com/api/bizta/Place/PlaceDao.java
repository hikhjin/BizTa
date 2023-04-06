package com.api.bizta.Place;

import com.api.bizta.Place.model.GetPlaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PlaceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    //특정 주식 정보 조회
    public GetPlaceInfo getPlaceInfo(int placeIdx) {

        String getPlaceInfoQuery =
                "select placeIdx, name, imgUrl, siteUrl, contact, address, description, grade, reviewCnt " +
                        "from Place where placeIdx = ? and status = 'active';";

        try {
            return this.jdbcTemplate.queryForObject(getPlaceInfoQuery, placeInfoRowMapper(), placeIdx);
        } catch (EmptyResultDataAccessException e) { // 쿼리문에 해당하는 결과가 없을 때
            return null;
        }

    }

    private RowMapper<GetPlaceInfo> placeInfoRowMapper(){
        return new RowMapper<GetPlaceInfo>() {
            @Override
            public GetPlaceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetPlaceInfo placeInfo = new GetPlaceInfo();
                placeInfo.setPlaceIdx(rs.getInt("placeIdx"));
                placeInfo.setName(rs.getString("name"));
                placeInfo.setImgUrl(rs.getString("imgUrl"));
                placeInfo.setSiteUrl(rs.getString("siteUrl"));
                placeInfo.setContact(rs.getString("contact"));
                placeInfo.setAddress("address");
                placeInfo.setDescription(rs.getString("description"));
                placeInfo.setGrade(rs.getFloat("grade"));
                placeInfo.setReviewCnt(rs.getInt("reviewCnt"));
                return placeInfo;
            }
        };
    }
}
