package com.example.zmyyjava.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.example.zmyyjava.pojo.DateInfo;

/**
 * @author : luok
 **/
public class SingnUtil {

    /**
     * 获取zfsw时间戳
     * @return
     */
    public static String getZfsw() {
        return SecureUtil.md5().digestHex("zfsw_" + System.currentTimeMillis() / 1000L);
    }

    public static String getSignature(String token) {
        token  = token.trim().replace("ASP.NET_SessionId=","");
        JWT jwt = JWTUtil.parseToken(token);
        String val = new JSONObject(jwt.getPayload().getClaimsJson()).getStr("val");
        String singnature = new String(Base64.decode(val.split("\r\n")[0]));
        return singnature.substring(9, 25);
    }

    public static void main(String[] args) {
//        System.out.println(new String(String.valueOf((System.currentTimeMillis() / 1000L))).length());
//        JWT jwt = JWTUtil.parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDA0ODkyNzguNzIzNzE5OCwiZXhwIjoxNjQwNDkyODc4LjcyMzcxOTgsInN1YiI6IllOVy5WSVAiLCJqdGkiOiIyMDIxMTIyNjExMjc1OCIsInZhbCI6ImVTYnJBQUlBQUFBUU1XTmhaV1UzTVRKaU5XUm1OekJqWWh4dmNYSTFielZIZVdaMVNEZ3pRVEpSWVdSS05XeG9abEJOV0V0QkFCeHZcclxuVlRJMldIUTBSRkIxUTBwbk5IazRTVFpoWlZnMWEwczRkM0J2RGpJeU1pNHlNVEV1TWpNM0xqRTBBQUFBQUFBQUFBPT0ifQ.NWFk7g7QXttWM2cAycD1r4Pb2QgtweasxLpRKq3UkDM");
//        String val = new JSONObject(jwt.getPayload().getClaimsJson()).getStr("val");
//        String singnature = new String(Base64.decode(val.split("\r\n")[0]));
//        singnature.substring(9, 25);
//        System.out.println(singnature);
        System.out.println(decode("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDA1NDIyMTcuNTg0ODU3NSwiZXhwIjoxNjQwNTQ1ODE3LjU4NDg1NzUsInN1YiI6IllOVy5WSVAiLCJqdGkiOiIyMDIxMTIyNzAyMTAxNyIsInZhbCI6ImVTYnJBQUlBQUFBUU9HSmhaRFEyT0RnM1pEWTNNRE14TXh4dmNYSTFielZIZVdaMVNEZ3pRVEpSWVdSS05XeG9abEJOV0V0QkFCeHZcclxuVlRJMldIUTBSRkIxUTBwbk5IazRTVFpoWlZnMWEwczRkM0J2RGpJeU1pNHlNVEV1TWpNM0xqRTBBQUFBQUFBQUFBPT0ifQ.vjh3wpweEfhUklWTW4Xgt5QgjYb8YdO4KU6anml25ro", "DFA1D5191A6FC72822F5E58993878566067DA2DEB0B96E3E58EE7D6B3545F2B0A38B22ADBE1C7E843DD9AB92FEA48A5B00B173ED2E288FEFCFDB17A4E21F3ACFCF4446500A2AABB6BF661DF14E0AC6C3D3E8785494B0FA930CDF04D1F9F6BE8B67E2C50F4D79644BAD3F04D230CB54CCF4DB3069E892DB72C1BA43510F268BB3A163BA2E021F777FCF73DC509C1750EC63F30C7637EF166D5D9BA31C99E2F27B52F1075BE9B1E753BA241E89A6AD9E5AAD0A06842B0199E8CC1C1F949588E42B8530C5B60BD65E733D469E196CAF1677D7A865781EC0E2005E0D6B03C541F6D8D231993BC30FFCC47D818F895CA5DF0A378891330115C5547E86F1FDD057E1A2DDE6FF20937635FECFD39DA72620AC9D22A30223D2654DB0963DF9831813EA0A0F4F4A3672896E4C6AD1347E42820D9D"));
    }

    public static DateInfo decode(String token, String text) {
        String signature = getSignature(token);
        AES aes = new AES("CBC", "PKCS7Padding",
                signature.getBytes(),
                "1234567890000000".getBytes());
        byte[] decrypt = aes.decrypt(text);
        return JSONUtil.toBean(new String(decrypt), DateInfo.class);
    }

}
