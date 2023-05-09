package com.api.bizta.Review;

import com.api.bizta.Review.model.GetReviewInfo;
import com.api.bizta.Review.model.PatchReviewReq;
import com.api.bizta.Review.model.PostReviewReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

//    public List<GetReviewsInfo> getReviewsInfo(int placeIdx){
//        String getReviewInfosQuery =
//                "select r.placeIdx, r.reviewIdx, u.nickName, r.rating, r.content from Review r " +
//                        "join User u on r.userIdx = u.userIdx " +
//                        "where placeIdx = ? and r.status = 'active';";
//
//        try{
//            List<GetReviewsInfo> reviews = this.jdbcTemplate.query(getReviewInfosQuery, reviewInfosRowMapper(), placeIdx);
//            return reviews;
//        }catch(EmptyResultDataAccessException e){
//            return null;
//        }
//    }

    public GetReviewInfo getReviewInfo(int reviewIdx){
        String getReviewInfoQuery =
                "select userIdx, placeIdx from Review where reviewIdx = ? and status = 'active'";

        try{
            GetReviewInfo reviewInfo = this.jdbcTemplate.queryForObject(getReviewInfoQuery, reviewInfoRowMapper(), reviewIdx);
            return reviewInfo;
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

//    private RowMapper<GetReviewsInfo> reviewInfosRowMapper(){
//        return (rs, rowNum) -> {
//            GetReviewsInfo reviewInfo = new GetReviewsInfo();
//            reviewInfo.setPlaceIdx(rs.getInt("placeIdx"));
//            reviewInfo.setReviewIdx(rs.getInt("reviewIdx"));
//            reviewInfo.setNickName(rs.getString("nickName"));
//            reviewInfo.setRating(rs.getFloat("rating"));
//            reviewInfo.setContent(rs.getString("content"));
//            return reviewInfo;
//        };
//    }

    private RowMapper<GetReviewInfo> reviewInfoRowMapper(){
        return (rs, rowNum) -> {
            GetReviewInfo reviewInfo = new GetReviewInfo();
            reviewInfo.setUserIdx(rs.getInt("userIdx"));
            reviewInfo.setPlaceIdx(rs.getInt("placeIdx"));
            return reviewInfo;
        };
    }

    // review 작성 위한 place 존재하는지 확인
    public int checkPlaceExist(int placeIdx){
        String checkPlaceExistQuery = "select exists(select placeIdx from Place where placeIdx = ? and status = 'active')";
        int checkPlaceExistParams = placeIdx;
        return this.jdbcTemplate.queryForObject(checkPlaceExistQuery,
                int.class,
                checkPlaceExistParams);
    }

    // 이미 리뷰를 작성한 유저인지 확인
    public int checkAlreadyExist(int userIdx, int placeIdx){
        String checkAlreadyExistQuery = "select exists(select reviewIdx from Review where userIdx = ? and placeIdx = ?)";
        Object[] checkAlreadyExistParams = new Object[]{userIdx, placeIdx};

        return this.jdbcTemplate.queryForObject(checkAlreadyExistQuery,
                int.class,
                checkAlreadyExistParams);
    }
    public int checkReviewExist(int reviewIdx){
        String checkReviewExistQuery = "select exists(select reviewIdx from Review where reviewIdx = ? and status = 'active')";
        int checkReviewExistParams = reviewIdx;
        return this.jdbcTemplate.queryForObject(checkReviewExistQuery,
                int.class,
                checkReviewExistParams);
    }

    // placeIdx에 대한 userIdx의 review 생성
    public int makeReview(int placeIdx, PostReviewReq postReviewReq){
        String makeReviewQuery = "insert into Review " +
                "(userIdx, placeIdx, rating, content) " +
                "values (?,?,?,?); ";
        Object[] makeReviewParams = new Object[]{postReviewReq.getUserIdx(), placeIdx, postReviewReq.getRating(), postReviewReq.getContent()};

        this.jdbcTemplate.update(makeReviewQuery, makeReviewParams);

        String lastInsertIdxQuery = "select last_insert_id()";

        return this.jdbcTemplate.queryForObject(lastInsertIdxQuery, int.class);
    }

    // review 수정
    public void modifyReview(int reviewIdx, PatchReviewReq patchReviewReq){
        String modifyReviewQuery = "update Review " +
                "set " +
                "rating = coalesce(?, rating), " +
                "content = coalesce(?, content) " +
                "where reviewIdx = ? and status = 'active'";

        Object[] modifyReviewParams = new Object[]{patchReviewReq.getRating(), patchReviewReq.getContent(), reviewIdx};

        jdbcTemplate.update(modifyReviewQuery, modifyReviewParams);
    }

    // review 삭제
    public void deleteReview(int reviewIdx){
        String deleteReviewQuery = "update Review " +
                "set " +
                "status = 'deleted' " +
                "where reviewIdx = ?";
        int deleteReviewParam = reviewIdx;

        this.jdbcTemplate.update(deleteReviewQuery, deleteReviewParam);
    }
}
