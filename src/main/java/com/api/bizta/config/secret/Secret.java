package com.api.bizta.config.secret;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

// TODO: 해당 KEY 값들을 꼭 바꿔서 사용해주세요!
// TODO: .gitignore에 추가하는거 앚지 마세요!
public class Secret {
//    public static String JWT_SECRET_KEY = "UwKYibQQgkW7g-*k.ap9kje-wxBHb9wdXoBT4vnt4P3sJWt-Nul";
    public static String JWT_SECRET_KEY = "EB12221C43B1EA4EAC68619EA9352ED22563A3507B4006B5E84712F974384059";

    public static String getJwtSecretKey() {
        byte[] decodedKey = JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(decodedKey);
    }
}
