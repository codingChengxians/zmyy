package com.example.zmyyjava.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author : luok
 **/
@Data
public class HospitalInfo {
    private String tel;
    private String addr;//地址
    private String cname;//医院名称
    private double lat;//经度
    private double lng;//纬度
    private int distance;
    private String BigPic;
    private boolean IdcardLimit;
    private String notice;
    private List<Vaccin> list;
    private List<String> selectTime;


    @Data
    public static class Vaccin {
        /**
         * 1=九价 2=四价
         */
        private int id;
        private String text;
        private String price;
        private String descript;
        private String warn;
        private List<String> tags;
        private int questionnaireId;
        private String remarks;
        private List<NumbersVaccine> NumbersVaccine;
        private String date;
        private String BtnLable;
        private boolean enable;
    }

    @Data
    public static class NumbersVaccine {
        private String cname;
        private int value;
    }


}
