package com.example.zmyyjava.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author : luok
 **/
@Data
public class Hospital {

    private int id;
    private String cname;
    private String addr;
    private String SmallPic;
    private String BigPic;
    private String lat;
    private String lng;
    private String tel;
    private String addr2;
    private int province;
    private int city;
    private int county;
    private int sort;
    private int DistanceShow;
    private String PayMent;
    private boolean IdcardLimit;
    private String notice;
    private String distance;
    private List<String> tags;
}
