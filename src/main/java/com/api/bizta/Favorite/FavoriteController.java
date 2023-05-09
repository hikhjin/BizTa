package com.api.bizta.Favorite;

import com.api.bizta.Favorite.model.GetFavoritesInfo;
import com.api.bizta.Place.model.GetPlaces;
import com.api.bizta.config.BaseException;
import com.api.bizta.config.BaseResponse;
import com.api.bizta.config.BaseResponseStatus;
import com.api.bizta.utils.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.api.bizta.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteProvider favoriteProvider;
    private final FavoriteService favoriteService;
    private final JwtService jwtService;

    public FavoriteController(FavoriteProvider favoriteProvider, FavoriteService favoriteService, JwtService jwtService) {
        this.favoriteProvider = favoriteProvider;
        this.favoriteService = favoriteService;
        this.jwtService = jwtService;
    }

    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<String> clickFavorite(@PathVariable("userIdx") int userIdx, @RequestParam int placeIdx){
        try {
            if(userIdx != jwtService.getUserIdx()){
                return new BaseResponse<>(BaseResponseStatus.INVALID_USER_JWT); // jwt 확인
            }
            String favoriteRes = favoriteService.clickFavorite(placeIdx, userIdx);
            return new BaseResponse<>(favoriteRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // userIdx를 어떤식으로 요청할까
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetFavoritesInfo>> getFavorites(@PathVariable("userIdx") int userIdx, @RequestParam(required = false, defaultValue = "latest") String sort){
        try{
            if(userIdx != jwtService.getUserIdx()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetFavoritesInfo> favorites = favoriteProvider.getFavorites(userIdx, sort);

            return new BaseResponse<>(favorites);
        }catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
