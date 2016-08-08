package com.rrtoyewx.recyclerviewlibrary.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Rrtoyewx on 16/8/8.
 * 没有item的adapter，解决没有设置的adapter滑动造成的崩溃
 */
public class NoItemAdapter extends BaseAdapter  {

    public NoItemAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }
}
