package com.example.xw.refresh.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
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

    public ListAdapter(Context context, List<String> objects, Handler handler) {
        super(context, objects, handler);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = ((Activity) (mContext)).getLayoutInflater()
                    .inflate(R.layout.item_list_view, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String string = mDataList.get(position);
        holder.textView.setText(string);

        return convertView;
    }

    private static final class ViewHolder {
        TextView textView;
    }

}
