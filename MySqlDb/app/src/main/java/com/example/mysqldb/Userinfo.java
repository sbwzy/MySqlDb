package com.example.mysqldb;

import java.io.Serializable;

/*
* 用户信息实体类
* */

public class Userinfo implements Serializable {
    private int id;
    private String uname;
    private String upass;
    private String createDt;    //创建时间
    private int isAdmin;

    public Userinfo(int id, String uname, String upass, String createDt, int isAdmin) {
        this.id = id;
        this.uname = uname;
        this.upass = upass;
        this.createDt = createDt;
        this.isAdmin = isAdmin;
    }

    public Userinfo() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

    public int getIsAdmin(){
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
