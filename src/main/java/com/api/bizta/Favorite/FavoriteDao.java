package com.api.bizta.Favorite;

import com.api.bizta.Favorite.model.GetFavoritesInfo;
import com.api.bizta.Place.model.GetPlaces;
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
public class FavoriteDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}

    public int checkPlaceExist(int placeIdx){
        String checkPlaceExistQuery = "select exists(select placeIdx from Place where placeIdx = ? and status = 'active')";
        int checkPlaceExistParam = placeIdx;
        return this.jdbcTemplate.queryForObject(checkPlaceExistQuery,
                int.class,
                checkPlaceExistParam);
    }

    public int checkFavoriteExist(int placeIdx, int userIdx){
        String checkFavoriteExistQuery = "select exists(select favoriteIdx from Favorite where placeIdx = ? and userIdx = ?)";
        Object[] checkFavoriteExistParam = new Object[]{placeIdx, userIdx};
        return this.jdbcTemplate.queryForObject(checkFavoriteExistQuery, int.class, checkFavoriteExistParam);
    }

    public void changeFavoriteStatus(int placeIdx, int userIdx){
        String changeFavoriteStatusQuery = "update Favorite " +
                "set status = " +
                "case status when 'active' then 'deleted' " +
                "else 'active' end " +
                "where placeIdx = ? and userIdx = ?;";

        Object[] changeFavoriteStatusParams = new Object[]{placeIdx, userIdx};
        this.jdbcTemplate.update(changeFavoriteStatusQuery, changeFavoriteStatusParams);

        System.out.println(1);

        String changeResultQuery = "select status from Favorite where placeIdx = ? and userIdx = ?;";
        Object[] changeResultParams = new Object[]{placeIdx, userIdx};
        String changeResult = this.jdbcTemplate.queryForObject(changeResultQuery, String.class, changeResultParams);
        System.out.println(2);
        changePlaceCnt(placeIdx, changeResult);
    }

    public void changePlaceCnt(int placeIdx, String changeResult){
        String changePlaceFavoriteCntQuery;
        if(changeResult.equals("active")){
            changePlaceFavoriteCntQuery = "update Place set favoriteCnt = favoriteCnt + 1 where placeIdx = ?;";
        }else{
            changePlaceFavoriteCntQuery = "update Place set favoriteCnt = favoriteCnt - 1 where placeIdx = ?;";
        }
        int changePlaceFavoriteCntParam = placeIdx;
        this.jdbcTemplate.update(changePlaceFavoriteCntQuery, changePlaceFavoriteCntParam);
        System.out.println(3);
    }

    public void addFavorite(int placeIdx, int userIdx){
        String addFavoriteQuery = "insert into Favorite " +
                "(placeIdx, userIdx) " +
                "values (?,?);";
        Object[] addFavoriteParams = new Object[]{placeIdx, userIdx};

        this.jdbcTemplate.update(addFavoriteQuery, addFavoriteParams);
        String changePlaceFavoriteCntQuery = "update Place set favoriteCnt = favoriteCnt + 1 where placeIdx = ?;";
        int changePlaceFavoriteCntParam = placeIdx;
        this.jdbcTemplate.update(changePlaceFavoriteCntQuery, changePlaceFavoriteCntParam);
    }

    // favorite 목록 조회
    public List<GetFavoritesInfo> getFavorites(int userIdx, String sort) {

        String getFavoritesQuery;
        if(sort.equals("") || sort.equals("latest")){
            getFavoritesQuery = "SELECT p.placeIdx, p.name, p.imgUrl, p.reviewCnt, p.grade, f.createdAt " +
                    "FROM Favorite f " +
                    "INNER JOIN Place p ON p.placeIdx = f.placeIdx " +
                    "WHERE f.userIdx = ? " +
                    "ORDER BY f.createdAt DESC;";
        }else{
            getFavoritesQuery = "SELECT p.placeIdx, p.name, p.imgUrl, p.reviewCnt, p.grade, f.createdAt " +
                    "FROM Favorite f " +
                    "INNER JOIN Place p ON p.placeIdx = f.placeIdx " +
                    "WHERE f.userIdx = ? " +
                    "ORDER BY p.grade DESC;";
        }

        try {
            List<GetFavoritesInfo> favorites = this.jdbcTemplate.query(getFavoritesQuery, favoritesRowMapper(), userIdx);
            return favorites;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private RowMapper<GetFavoritesInfo> favoritesRowMapper(){
        return new RowMapper<GetFavoritesInfo>() {
            @Override
            public GetFavoritesInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
                GetFavoritesInfo favorites = new GetFavoritesInfo();
                favorites.setPlaceIdx(rs.getInt("placeIdx"));
                favorites.setName(rs.getString("name"));
                favorites.setImgUrl(rs.getString("imgUrl"));
                favorites.setReviewCnt(rs.getInt("reviewCnt"));
                favorites.setGrade(rs.getFloat("grade"));

                return favorites;
            }
        };
    }
}
