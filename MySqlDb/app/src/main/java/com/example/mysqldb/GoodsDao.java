package com.example.mysqldb;

/*
 * 货物数据库操作类
 * 实现货物的CRUD操作
 * */

import java.util.ArrayList;
import java.util.List;

public class GoodsDao extends DbOpenHelper {
    //查询所有的货物信息 R
    public List<Goodsinfo>getAllGoodsList(){
        List<Goodsinfo> list = new ArrayList<>();
        try{
            getConnection();    //取得连接信息
            String sql = "select * from goodsinfo";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while (rs.next()){  //查询数据集合时应使用while
                Goodsinfo item = new Goodsinfo();
                item.setId(rs.getInt("id"));
                item.setGname(rs.getString("gname"));
                item.setGnumber(rs.getString("gnumber"));
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
     * 按商品名查询用户信息 R
     * gname 商品名
     * id 商品编号
     * Goodsinfo 实例
     * */
    public Goodsinfo getGoodsByid(int id){
        Goodsinfo item = null;
        try{
            getConnection();    //取得连接信息
            String sql = "select * from goodsinfo where id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, String.valueOf(id));
            rs = pStmt.executeQuery();
            if (rs.next()){
                item = new Goodsinfo();
                item.setId(rs.getInt("id"));
                item.setGname(rs.getString("gname"));
                item.setGnumber(rs.getString("gnumber"));
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
     * item 要添加的货物
     * iRow 影响的行数
     * */
    public int addGoods(Goodsinfo item){
        int iRow = 0;
        try{
            getConnection();    //取得连接信息
            String sql = "insert into goodsinfo (gname, gnumber, createDt) values(?, ?, ?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getGname());
            pStmt.setString(2, item.getGnumber());
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
    public int editGoods(Goodsinfo item){
        int iRow = 0;
        try{
            getConnection();    //取得连接信息
            String sql = "update goodsinfo set gname=?, gnumber=? where id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getGname());
            pStmt.setString(2, item.getGnumber());
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
    public int delGoods(int id){
        int iRow = 0;
        try{
            getConnection();    //取得连接信息
            String sql = "delete from goodsinfo where id=?";
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
