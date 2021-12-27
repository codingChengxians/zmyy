package com.example.zmyyjava.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author : luok
 **/
@Data
public class DateInfo {

    private int status;
    private int id;
    private String customer;
    private List<Info> list;
    private String ver;

    @Data
   public static class Info{
        private String customer;
        private int customerid;
        private String StartTime;
        private String EndTime;
        private String mxid;
        private int qty;
    }
}
