package com.example.zmyyjava.api.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.zmyyjava.pojo.DateInfo;
import com.example.zmyyjava.pojo.Hospital;
import com.example.zmyyjava.pojo.HospitalInfo;
import com.example.zmyyjava.pojo.UserInfo;
import com.example.zmyyjava.utils.SingnUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author : luok
 **/
//@Component
public class Api {

    private String jwt;

    public Api(String jwt) {
        this.jwt = jwt;
    }

    public Map<String, String> getHeaderMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("Host", " cloud.cn2030.com");
        map.put("Connection", " keep-alive");
        map.put("Cookie", " ASP.NET_SessionId=" + jwt);
        map.put("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
        map.put("content-type", " application/json");
        map.put("zftsl", SingnUtil.getZfsw());
        map.put("Referer", " https://servicewechat.com/wx2c7f0f3c30d99445/91/page-frame.html");
        map.put("Accept-Encoding", " gzip, deflate, br");
        return map;
    }

    /**
     * http code 408就是登录过期
     */
    @SneakyThrows
    public String unirestGet(String url) {
        HttpResponse<String> response = Unirest.get(url)
                .headers(getHeaderMap())
                .asString();
        return response.getBody();
    }

    /**
     * {"status":408} token过期
     * 获取当前token填写的用户信息
     */
    public UserInfo getUserInfo() {
        String body = unirestGet("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=User");
        String user = new JSONObject(body).getStr("user");
        return JSONUtil.toBean(user, UserInfo.class);
    }

    /**
     * 获取成都所有医院
     */
    public List<Hospital> getHospitalList() {
        String body = unirestGet("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=CustomerList&city=%5B%22%22%2C%22%22%2C%22%22%5D&lat=30.5702&lng=104.06476&id=0&cityCode=0&product=1");
        return new JSONArray(new JSONObject(body).getJSONArray("list")).toList(Hospital.class);
    }


    /**
     * 获取医院信息=预约时间
     * @param id
     * @param lat
     * @param lng
     * @return
     */
    public HospitalInfo getHospitalInfo(Integer id, String lat, String lng) {
        String body = unirestGet(String.format("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=CustomerProduct&id=%d&lat=%s&lng=%s", id, lat, lng));
        HospitalInfo hospitalInfo = JSONUtil.toBean(body, HospitalInfo.class);
        System.out.println(hospitalInfo);
        return hospitalInfo;
    }

    /**
     * 获取所有预约时间
     */
    public List<String> getAllDate(Integer pid, Integer id, String date) {
        List<String> dateList = new ArrayList<>();
        String body = unirestGet(String.format("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=GetCustSubscribeDateAll&pid=%d&id=%d&month=%s", pid, id, date));
        JSONArray list = new JSONObject(body).getJSONArray("list");
        if (Objects.isNull(list)) {
            System.out.println("=======指定医院无" + ((pid == 1) ? "九价" : "其他"));
        }
        for (Object obj : list) {
            dateList.add(new JSONObject(obj).getStr("date"));
        }
        return dateList;
    }

    /**
     * 获取mxid
     * pid = 1 九价 product Id
     * id 医院id
     * date 年月
     */
    public DateInfo getDate(Integer pid, Integer id, String date, String jwt) {
        String body = unirestGet(String.format("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=GetCustSubscribeDateDetail&pid=%d&id=%d&scdate=%s", pid, id, date));
        return SingnUtil.decode(jwt, body);
    }

    /**
     * 提交验证码
     */
    public boolean GetCaptcha(String mxid) {
        String body = unirestGet(String.format("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=GetCaptcha&mxid=" + mxid));
        return "true".equals(new JSONObject(body).getStr("ignore"));
    }

    /**
     * 提交预约信息
     */
    @SneakyThrows
    public String submit(Integer pid, String mxid, String date, UserInfo userInfo) {
//        String birthday = userInfo.getBirthday();
//        String cname = userInfo.getCname();
//        String idcard = userInfo.getIdcard();
//        Integer sex = userInfo.getSex();
//        String tel = userInfo.getTel();
//        String url = String.format("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=Save20&birthday=%s&tel=%s&sex=%d&cname=%s&doctype=1&idcard=%s&mxid=%s&date=%s&pid=%d&Ftime=1&guid="
//                , birthday, tel, sex, cname, idcard, mxid, date, pid);
//        return unirestGet(url);
        HttpResponse<String> response = Unirest.get("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=Save20&birthday=2000-04-03&tel=15882496241&sex=2&cname=%E5%88%98%E8%8C%9C&doctype=1&idcard=510122200004038760&mxid=eSbrAD6YAAAbZjQB&date=2021-12-27&pid=62&Ftime=1&guid=")
                .header("Host", " cloud.cn2030.com")
                .header("Connection", " keep-alive")
                .header("Cookie", " ASP.NET_SessionId=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDA1Njg5MDAuMjc0NTIwOSwiZXhwIjoxNjQwNTcyNTAwLjI3NDUyMDksInN1YiI6IllOVy5WSVAiLCJqdGkiOiIyMDIxMTIyNzA5MzUwMCIsInZhbCI6ImVTYnJBQUlBQUFBUU9XWmlPVGhrWW1KalpUTTJOelE1TXh4dmNYSTFielZIZVdaMVNEZ3pRVEpSWVdSS05XeG9abEJOV0V0QkFCeHZcclxuVlRJMldIUTBSRkIxUTBwbk5IazRTVFpoWlZnMWEwczRkM0J2RERFeE9DNHhNVE11Tnk0Mk9BQUFBQUFBQUFBPSJ9.AzoAuD7vA3oHkhIFVRShFuXV4Om17zAwOWH6PMawfj0")
                .header("User-Agent", " Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat")
                .header("content-type", " application/json")
                .header("zftsl", SingnUtil.getZfsw())
                .header("Referer", " https://servicewechat.com/wx2c7f0f3c30d99445/91/page-frame.html")
                .header("Accept-Encoding", " gzip, deflate, br")
                .asString();
        return  response.getBody();

        //https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=Save20&birthday=2000-04-03&tel=15882496241&sex=2&cname=%E5%88%98%E8%8C%9C&doctype=1&idcard=510122200004038760&mxid=eSbrAEGcAAAbZjQB&date=2021-12-27&pid=54&Ftime=1&guid=
    }

    public static void main(String[] args) {
//        new Api().getHospitalInfo(6537,"30.5702","104.06476");

//        String str = "32013B845027629F2A1015EFFF097978A34EB0C00D7676BA804ACCE78DD15809E56A54ACF0FFB04E896CACA29F3308B6BA9C3928910B6F06650602472E2C80C2945B1476D6E5F63973AED420D8E0A31FCA2D89F5D7723EA3DD89233194F6EF5F2103E90AA3AA8FB57EF5D30CA8BD32C35B3F1B0BB13CE80815967D5C434F399FD915DFE9BACBF1D58F0EAF388660AF73342DA9618F8BFC5118B73914F9F9E1B34089CA347C301D72672FDBA9BCE7528F36D24C2210A3732FE3A5CA60CDD11E9AB6985E120B109551256D3FE04B6B873CC1F8C8C9D32F3699A89E33667AD8141208756DE3CB5BA67DBA119F518771156D80E52DD038D98C3A665300A543190B36A9864D3DA70C41C72A870843A151BD673158055454E74DD125FB51B0D6DB0392DEBDA186B7EF7A98C926576BC34F750B";
//        String body = new Api().unirestGet(String.format("https://cloud.cn2030.com/sc/wx/HandlerSubscribe.ashx?act=GetCaptcha&mxid=" + "eSbrAEGcAAAbZjQB"));
//        System.out.println(body);
//        System.out.println("true".equals(new JSONObject(body).getStr("ignore")));


    }
}
