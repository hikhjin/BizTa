package com.api.bizta.Review;

import com.api.bizta.Review.model.GetReviewInfo;
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

    public List<GetReviewInfo> getReviewInfos(int placeIdx){
        String getReviewInfosQuery =
                "select reviewIdx, placeIdx, userIdx, rating, content from Review " +
                        "where placeIdx = ? and status = 'active';";

        try{
            List<GetReviewInfo> reviews = this.jdbcTemplate.query(getReviewInfosQuery, reviewInfosRowMapper(), placeIdx);
            return reviews;
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    private RowMapper<GetReviewInfo> reviewInfosRowMapper(){
        return (rs, rowNum) -> {
            GetReviewInfo reviewInfo = new GetReviewInfo();
            reviewInfo.setReviewIdx(rs.getInt("reviewIdx"));
            reviewInfo.setPlaceIdx(rs.getInt("placeIdx"));
            reviewInfo.setUserIdx(rs.getInt("userIdx"));
            reviewInfo.setRating(rs.getFloat("rating"));
            reviewInfo.setContent(rs.getString("content"));
            return reviewInfo;
        };
    }
}
