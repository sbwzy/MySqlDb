package com.example.mysqldb;

import java.io.Serializable;

/*
 * 货物信息实体类
 * */

public class Goodsinfo implements Serializable {
    private int id;
    private String gname;
    private String gnumber;
    private String createDt;    //创建时间


    public Goodsinfo(int id, String gname, String gnumber, String createDt) {
        this.id = id;
        this.gname = gname;
        this.gnumber = gnumber;
        this.createDt = createDt;
    }

    public Goodsinfo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGnumber() {
        return gnumber;
    }

    public void setGnumber(String gnumber) {
        this.gnumber = gnumber;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }
}
