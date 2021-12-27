package com.example.zmyyjava.api.service;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONObject;
import com.example.zmyyjava.pojo.DateInfo;
import com.example.zmyyjava.pojo.Hospital;
import com.example.zmyyjava.pojo.HospitalInfo;
import com.example.zmyyjava.pojo.UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : luok
 **/
@Slf4j
public class ZmyyService {


    public ExecutorService executorService = Executors.newFixedThreadPool(50);


    private String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE2NDA1Njg5MDAuMjc0NTIwOSwiZXhwIjoxNjQwNTcyNTAwLjI3NDUyMDksInN1YiI6IllOVy5WSVAiLCJqdGkiOiIyMDIxMTIyNzA5MzUwMCIsInZhbCI6ImVTYnJBQUlBQUFBUU9XWmlPVGhrWW1KalpUTTJOelE1TXh4dmNYSTFielZIZVdaMVNEZ3pRVEpSWVdSS05XeG9abEJOV0V0QkFCeHZcclxuVlRJMldIUTBSRkIxUTBwbk5IazRTVFpoWlZnMWEwczRkM0J2RERFeE9DNHhNVE11Tnk0Mk9BQUFBQUFBQUFBPSJ9.AzoAuD7vA3oHkhIFVRShFuXV4Om17zAwOWH6PMawfj0";
    private String hospitalName = "成都市成华区猛追湾建设路社区卫生服务中心";

    public UserInfo userInfo;

    public Api api = new Api(jwt);

    // 1= 九价 2=四价 54 = 2价 62 = 四价流感病毒裂解疫苗
    private Integer pid = 62;

    String month = LocalDateTimeUtil.format(LocalDateTime.now(), "yyyyMM");

    public void getAllDate() {
        List<Hospital> hospitalList = api.getHospitalList();
        this.userInfo = api.getUserInfo();
        Hospital hospital = hospitalList.stream().filter(e -> hospitalName.equals(e.getCname())).findFirst().get();
        HospitalInfo hospitalInfo = api.getHospitalInfo(hospital.getId(), hospital.getLat(), hospital.getLng());
        long start = System.currentTimeMillis();
        log.info("开始");
        List<String> allDate = api.getAllDate(pid, hospital.getId(), month);
        String date = allDate.get(0);
        DateInfo apiDate = api.getDate(pid, hospital.getId(), date, jwt);
        long use = System.currentTimeMillis() - start;
        log.info("=====获取mxid时间：" + use);
        String mxid = apiDate.getList().get(0).getMxid();
        api.GetCaptcha(mxid);
        this.seckill(mxid, date, start);
        log.info("dateInfo：" + apiDate);
    }


    /**
     * ====================抢购失败！
     * {"status":408}
     */
    public void seckill(String mxid, String date, long start) {
        String body = api.submit(pid, mxid, date, this.userInfo);
        log.info("总时长：{} ms", System.currentTimeMillis() - start);
        JSONObject result = new JSONObject(body);
        Integer status = result.getInt("status");
        if (status.equals(408)) {
            log.info("====================抢购失败！");
        }
        if (status.equals(200) && "提交成功".equals(result.getStr("msg"))) {
            log.info("抢购成功～");
        }
        System.out.println(body);

    }


    public static void main(String[] args) {
        ZmyyService service = new ZmyyService();
        service.getAllDate();
    }
}
