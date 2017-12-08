package com.dgy.chatdog.adapte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.dgy.chatdog.R;
import com.dgy.chatdog.activity.NewsCenterActivity;
import com.dgy.chatdog.utils.MapEntity;
import com.dgy.chatdog.view.RatioImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;



/**
 * Created by dgy
 * on 2017/08/31.
 * 新闻列表适配器
 */

public class NewsAdapter extends BaseAdapter implements Serializable {
    private Context context;
    private List<MapEntity> lists;
    private LayoutInflater layoutInflater;
    private NewsCenterActivity newsCenterActivity;


    public NewsAdapter(Context context, List<MapEntity> lists) {
        this.context = context;
        this.newsCenterActivity= (NewsCenterActivity) context;
        this.lists = lists;
        layoutInflater = LayoutInflater.from(context);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_news, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final MapEntity map = (MapEntity) getItem(position);
        holder.article.setText(map.getString("article"));
        holder.source.setText(map.getString("source"));
        ImageLoader.getInstance().displayImage(map.getString("icon"),holder.icon);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsCenterActivity.openWebForOutside(map.getString("detailurl"));
            }
        });
        return convertView;
    }

    static class ViewHolder {
        RatioImageView icon;
        TextView article;
        TextView source;
        ViewHolder(View view) {
           icon= (RatioImageView) view.findViewById(R.id.icon);
            article= (TextView) view.findViewById(R.id.article);
            source= (TextView) view.findViewById(R.id.source);
        }
    }
}
