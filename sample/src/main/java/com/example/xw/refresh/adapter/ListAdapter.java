package com.example.xw.refresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xw.refresh.R;
import com.example.xw.refresh.bean.ListData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * ListAdapter
 * Created by XiaoWei on 2015-11-14.
 */
public class ListAdapter extends BaseListAdapter<ListData> {

    public ListAdapter(Context context, List<ListData> objects) {
        super(context, objects);
    }

    public ListAdapter(Context context, List<ListData> objects, Handler handler) {
        super(context, objects, handler);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.item_list_view, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.textView = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListData data = mDataList.get(position);
        Picasso.with(mContext).load(data.getUrl())
                .error(R.mipmap.ic_launcher).into(holder.imageView);
        holder.textView.setText(data.getPublishedAt());

        return convertView;
    }

    private static final class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
