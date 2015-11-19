package com.example.xw.refresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xw.refresh.R;

import java.util.List;

/**
 * ListAdapter
 * Created by XiaoWei on 2015-11-14.
 */
public class ListAdapter extends BaseListAdapter<String> {

    public ListAdapter(Context context, List<String> objects) {
        super(context, objects);
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

        holder.textView.setText(mDataList.get(position));

        return convertView;
    }

    private static final class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

}
