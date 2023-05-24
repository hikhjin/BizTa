package com.api.bizta.Place;

import com.api.bizta.Place.model.*;
import com.api.bizta.config.BaseException;
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
            getPlacesQuery = "select placeIdx, name, category, imgUrl, address, description, grade, reviewCnt, price " +
                    "from Place where status = 'active';";
        }else{
            getPlacesQuery = "select placeIdx, name, category, imgUrl, address, description, grade, reviewCnt, price " +
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
                "select placeIdx, name, category, imgUrl, address, description, grade, reviewCnt, siteUrl, price, detail " +
                        "from Place where placeIdx = ? and status = 'active';";

        try {
            return this.jdbcTemplate.queryForObject(getPlaceInfoQuery, placeInfoRowMapper(), placeIdx);
        } catch (EmptyResultDataAccessException e) { // 쿼리문에 해당하는 결과가 없을 때
            return null;
        }
    }

    public List<String> getPlaceImgUrls(int placeIdx){

        String placeImgUrlsQuery =
                "select imgUrl from PlaceImg where placeIdx = ?";
        try{
            return this.jdbcTemplate.query(placeImgUrlsQuery, placeImgUrlsRowMapper(), placeIdx);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    private RowMapper<String> placeImgUrlsRowMapper(){
        return new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                String placeImgUrls;
                placeImgUrls = rs.getString("imgUrl");
                return placeImgUrls;
            }
        };
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

    public List<GetPlaceReview> getPlaceReview(int placeIdx) {

        String getPlaceReviewQuery =

        "select r.reviewIdx, r.placeIdx, u.nickName, r.rating, r.content " +
                "from Review r " +
                "join User u on r.userIdx = u.userIdx " +
                "where r.placeIdx = ?";

        try {
            return this.jdbcTemplate.query(getPlaceReviewQuery, placeReviewRowMapper(), placeIdx);
        } catch (EmptyResultDataAccessException e) { // 쿼리문에 해당하는 결과가 없을 때
            return null;
        }

    }

    public GetPlaceMap getPlaceMap(int placeIdx) {

        String getPlaceMapQuery =
                "select placeIdx, address, lat, lon from Place " +
                        "where placeIdx = ? and status = 'active';";

        try {
            return this.jdbcTemplate.queryForObject(getPlaceMapQuery, placeMapRowMapper(), placeIdx);
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
                places.setCategory(rs.getString("category"));
                places.setImgUrl(rs.getString("imgUrl"));
                places.setAddress(rs.getString("address"));
                places.setDescription(rs.getString("description"));
                places.setGrade(rs.getFloat("grade"));
                places.setReviewCnt(rs.getInt("reviewCnt"));
                places.setPrice(rs.getString("price"));
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
                placeInfo.setSiteUrl(rs.getString("siteUrl"));
                placeInfo.setPrice(rs.getString("price"));
                placeInfo.setDetail(rs.getString("detail"));
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

    private RowMapper<GetPlaceReview> placeReviewRowMapper(){
        return new RowMapper<GetPlaceReview>() {
            @Override
            public GetPlaceReview mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetPlaceReview placeReview = new GetPlaceReview();
                placeReview.setReviewIdx(rs.getInt("reviewIdx"));
                placeReview.setPlaceIdx(rs.getInt("placeIdx"));
                placeReview.setNickName(rs.getString("nickName"));
                placeReview.setRating(rs.getFloat("rating"));
                placeReview.setContent(rs.getString("content"));
                return placeReview;
            }
        };
    }

    private RowMapper<GetPlaceMap> placeMapRowMapper(){
        return new RowMapper<GetPlaceMap>() {
            @Override
            public GetPlaceMap mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetPlaceMap placeMap = new GetPlaceMap();
                placeMap.setPlaceIdx(rs.getInt("placeIdx"));
                placeMap.setAddress(rs.getString("address"));
                placeMap.setLat(rs.getFloat("lat"));
                placeMap.setLon(rs.getFloat("lon"));
                return placeMap;
            }
        };
    }

    // 특정 장소에 대한 리뷰 개수와 grade 계산
    public void updateCntAndGrade(int placeIdx){
        String updateCntAndGradeQuery = "update Place p " +
                "set p.reviewCnt = (SELECT COUNT(*) FROM Review r WHERE r.placeIdx = ? and r.status = 'active'), " +
                "p.grade = round(" +
                "(select sum(rating)/count(*) " +
                "from Review r " +
                "where r.placeIdx = ? and r.status = 'active'), 2) " +
                "where p.placeIdx = ?";
        Object[] updateCntAndGradeParam = new Object[]{placeIdx, placeIdx, placeIdx};

        this.jdbcTemplate.update(updateCntAndGradeQuery, updateCntAndGradeParam);
    }
}
