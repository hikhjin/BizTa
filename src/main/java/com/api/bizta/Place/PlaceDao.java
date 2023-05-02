package com.api.bizta.Place;

import com.api.bizta.Place.model.*;
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
public class PlaceDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    // 장소 전체 조회
    public List<GetPlaces> getPlaces(String category) {

        String getPlacesQuery;
        if(category.equals("")){
            getPlacesQuery = "select placeIdx, name, imgUrl, grade, reviewCnt " +
                    "from Place where status = 'active';";
        }else{
            getPlacesQuery = "select placeIdx, name, imgUrl, grade, reviewCnt " +
                    "from Place where category = ? and status = 'active'";
        }

        try {
            List<GetPlaces> places;
            if(category.equals("")){
                places = this.jdbcTemplate.query(getPlacesQuery, placesRowMapper());
            }else {
                places = this.jdbcTemplate.query(getPlacesQuery, placesRowMapper(), category);
            }
            return places;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //특정 장소 정보 조회
    public GetPlaceInfo getPlaceInfo(int placeIdx) {

        String getPlaceInfoQuery =
                "select placeIdx, name, category, imgUrl, address, description, grade, reviewCnt " +
                        "from Place where placeIdx = ? and status = 'active';";

        try {
            return this.jdbcTemplate.queryForObject(getPlaceInfoQuery, placeInfoRowMapper(), placeIdx);
        } catch (EmptyResultDataAccessException e) { // 쿼리문에 해당하는 결과가 없을 때
            return null;
        }

    }

    //특정 장소 예약 정보 조회
    public GetPlaceReservation getPlaceReservation(int placeIdx) {

        String getPlaceReservationQuery =
                "select placeIdx, siteUrl, contact from Place " +
                        "where placeIdx = ? and status = 'active';";

        try {
            return this.jdbcTemplate.queryForObject(getPlaceReservationQuery, placeReservationRowMapper(), placeIdx);
        } catch (EmptyResultDataAccessException e) { // 쿼리문에 해당하는 결과가 없을 때
            return null;
        }

    }

    private RowMapper<GetPlaces> placesRowMapper(){
        return new RowMapper<GetPlaces>() {
            @Override
            public GetPlaces mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetPlaces places = new GetPlaces();
                places.setPlaceIdx(rs.getInt("placeIdx"));
                places.setName(rs.getString("name"));
                places.setImgUrl(rs.getString("imgUrl"));
                places.setGrade(rs.getFloat("grade"));
                places.setReviewCnt(rs.getInt("reviewCnt"));
                return places;
            }
        };
    }

    private RowMapper<GetPlaceInfo> placeInfoRowMapper(){
        return new RowMapper<GetPlaceInfo>() {
            @Override
            public GetPlaceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetPlaceInfo placeInfo = new GetPlaceInfo();
                placeInfo.setPlaceIdx(rs.getInt("placeIdx"));
                placeInfo.setName(rs.getString("name"));
                placeInfo.setCategory(rs.getString("category"));
                placeInfo.setImgUrl(rs.getString("imgUrl"));
                placeInfo.setAddress(rs.getString("address"));
                placeInfo.setDescription(rs.getString("description"));
                placeInfo.setGrade(rs.getFloat("grade"));
                placeInfo.setReviewCnt(rs.getInt("reviewCnt"));
                return placeInfo;
            }
        };
    }

    private RowMapper<GetPlaceReservation> placeReservationRowMapper(){
        return new RowMapper<GetPlaceReservation>() {
            @Override
            public GetPlaceReservation mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetPlaceReservation placeReservation = new GetPlaceReservation();
                placeReservation.setPlaceIdx(rs.getInt("placeIdx"));
                placeReservation.setSiteUrl(rs.getString("siteUrl"));
                placeReservation.setContact(rs.getString("contact"));
                return placeReservation;
            }
        };
    }
}
