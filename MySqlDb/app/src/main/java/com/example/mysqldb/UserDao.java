package com.example.mysqldb;

/*
* 用户数据库操作类
* 实现用户的CRUD操作
* */

import java.util.ArrayList;
import java.util.List;

public class UserDao extends DbOpenHelper {
    //查询所有的用户信息 R
    public List<Userinfo>getAllUserList(){
        List<Userinfo> list = new ArrayList<>();
        try{
            getConnection();    //取得连接信息
            String sql = "select * from userinfo";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()){  //查询数据集合时应使用while
                Userinfo item = new Userinfo();
                item.setId(rs.getInt("id"));
                item.setUname(rs.getString("uname"));
                item.setUpass(rs.getString("upass"));
                item.setCreateDt(rs.getString("createDt"));

                list.add(item);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeAll();
        }
        return list;
    }

    /*
    * 按用户名和密码查询用户信息 R
    * uname 用户名
    * upass 密码
    * Userinfo 实例
    * */
    public Userinfo getUserByUnameAndUpass(String uname, String upass){
        Userinfo item = null;
        try{
            getConnection();    //取得连接信息
            String sql = "select * from userinfo where uname=? and upass=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, uname);
            pStmt.setString(2, upass);
            rs = pStmt.executeQuery();
            if (rs.next()){
                item = new Userinfo();
                item.setId(rs.getInt("id"));
                item.setUname(uname);
                item.setUpass(upass);
                item.setCreateDt(rs.getString("createDt"));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeAll();
        }
        return item;
    }

    /*
    *添加用户信息 C
    * item 要添加的用户
    * iRow 影响的行数
    * */
    public int addUser(Userinfo item){
        int iRow = 0;
        try{
            getConnection();    //取得连接信息
            String sql = "insert into userinfo(uname, upass, createDt) values(?, ?, ?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getUname());
            pStmt.setString(2, item.getUpass());
            pStmt.setString(3, item.getCreateDt());
            iRow = pStmt.executeUpdate();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeAll();
        }
        return iRow;
    }

    /*
     *修改用户信息 U
     * item 要修改的用户
     * iRow 影响的行数
     * */
    public int editUser(Userinfo item){
        int iRow = 0;
        try{
            getConnection();    //取得连接信息
            String sql = "update userinfo set uname=?, upass=? where id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getUname());
            pStmt.setString(2, item.getUpass());
            pStmt.setInt(3, item.getId());
            iRow = pStmt.executeUpdate();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeAll();
        }
        return iRow;
    }

    /*
    *根据id删除用户信息
    * id 要删除的用户
    * iRow 影响的行数
    * */
    public int delUser(int id){
        int iRow = 0;
        try{
            getConnection();    //取得连接信息
            String sql = "delete from userinfo where id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            iRow = pStmt.executeUpdate();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeAll();
        }
        return iRow;
    }
}
