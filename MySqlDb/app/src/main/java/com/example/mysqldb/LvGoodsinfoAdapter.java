package com.example.mysqldb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/*
 * 自定义商品数据适配类
 * */

public class LvGoodsinfoAdapter extends BaseAdapter {
    private Context context;    //上下文信息
    private List<Goodsinfo> goodsinfoList;    //商品信息数据集合

    private OnEditButtonClickListener onEditButtonClickListener;    //修改按钮点击事件的监听实例
    private OnDelBtnClickListener onDelBtnClickListener;    //删除按钮点击事件的监听实例

    public LvGoodsinfoAdapter() {
    }

    public LvGoodsinfoAdapter(Context context, List<Goodsinfo> goodsinfoList) {
        this.context = context;
        this.goodsinfoList = goodsinfoList;
    }

    public void setGoodsinfoList(List<Goodsinfo> goodsinfoList) {
        this.goodsinfoList = goodsinfoList;
    }

    public void setOnEditButtonClickListener(OnEditButtonClickListener onEditButtonClickListener) {
        this.onEditButtonClickListener = onEditButtonClickListener;
        Log.i("数据适配器", "货物数量:" + goodsinfoList.size());
    }

    public void setOnDelBtnClickListener(OnDelBtnClickListener onDelBtnClickListener) {
        this.onDelBtnClickListener = onDelBtnClickListener;
    }

    @Override
    public int getCount() {
        return goodsinfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsinfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.goods_list_item, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_id = convertView.findViewById(R.id.tv_gId);
            viewHolder.tv_gname = convertView.findViewById(R.id.tv_gName);
            viewHolder.tv_gnumber = convertView.findViewById(R.id.tv_gNumber);
            viewHolder.tv_createDt = convertView.findViewById(R.id.tv_gCreateDt);

            viewHolder.btn_edit = convertView.findViewById(R.id.btn_gEdit);
            viewHolder.btn_delete = convertView.findViewById(R.id.btn_gDelete);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //这里进行数据填充
        Goodsinfo item = goodsinfoList.get(position);
        viewHolder.tv_id.setText(item.getId() + ".");
        viewHolder.tv_gname.setText(item.getGname());
        viewHolder.tv_gnumber.setText(item.getGnumber());
        viewHolder.tv_createDt.setText(item.getCreateDt());

        //修改按钮的点击事件
        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditButtonClickListener.onEditBtnClick(v, position);
            }
        });

        //删除按钮的点击事件
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelBtnClickListener.onDelBtnClick(v, position);
            }
        });

        return convertView;
    }

    //自定义内部类
    private class ViewHolder{
        private TextView tv_id, tv_gname, tv_gnumber, tv_createDt;
        private ImageView btn_edit, btn_delete;
    }
}
