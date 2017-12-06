package com.dgy.chatdog.adapte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dgy.chatdog.MainActivity;
import com.dgy.chatdog.R;
import com.dgy.chatdog.utils.MapEntity;
import com.dgy.chatdog.view.RoundImagenewView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DGY
 * on 2017/8/2.
 * 在线客服列表适配器(新)
 */

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<MapEntity> lists;

    public ChatAdapter(Context context, List<MapEntity> lists) {
        this.context = context;
        this.lists = lists;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final MapEntity map = lists.get(position);
        int type = getItemViewType(position);//0客服 1-自己
        if (convertView == null) {
            switch (type) {
                case 0://机器
                    convertView = layoutInflater.inflate(R.layout.list_custom_new_self, null);
                    break;
                case 1://本人
                    convertView = layoutInflater.inflate(R.layout.list_custom_new_cumtom, null);
                    break;
            }
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(type==1){
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                    .showImageForEmptyUri(R.mipmap.headdefault) //
                    .showImageOnFail(R.mipmap.headdefault) //
                    .cacheInMemory(true) //
                    .cacheOnDisk(true) //
                    .build();//
            ImageLoader.getInstance().displayImage(map.getString("pic"),holder.pic,defaultOptions);
        }
        int typecode=map.getInt("typecode",0);/*100000	文本类
                                                200000	链接类
                                                302000	新闻类
                                                308000	菜谱类*/
        holder.tvContent.setText(map.getString("text"));
        holder.time.setText(map.getString("createtime"));
        return convertView;
    }

    //返回布局的种类个数
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //返回布局的类型
    @Override
    public int getItemViewType(int position) {
        final MapEntity map = lists.get(position);
        if (map.getInt("typecode", 0) < 99999) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public int getCount() {
        if (lists != null && lists.size() > 0) {
            return lists.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.pic)
        RoundImagenewView pic;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}