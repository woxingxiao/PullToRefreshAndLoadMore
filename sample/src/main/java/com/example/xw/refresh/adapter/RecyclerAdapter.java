package com.example.xw.refresh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xw.refresh.R;

import java.util.ArrayList;
import java.util.List;

/**
 * RecylerVeiw适配器
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    /**
     * 条目点击监听
     */
    private OnItemClickListener mOnItemClickListener;
    private Context context;
    private List<String> mStrings = new ArrayList<>();

    public RecyclerAdapter(Context context, List<String> strings) {
        super();
        this.context = context;
        this.mStrings = strings;
    }

    public void updateRecyclerView(List<String> strings) {
        this.mStrings = strings;
        notifyDataSetChanged();
    }

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(String string, ViewHolder viewHolder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_recycler_view, viewGroup);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        // 绑定数据到ViewHolder上
        final String string = mStrings.get(position);

        viewHolder.textView.setText(string);

        //如果设置了回调，则设置点击事件
        if (mOnItemClickListener != null) {

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(string, viewHolder, position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (mStrings == null || mStrings.size() == 0)
            return 0;
        return mStrings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
