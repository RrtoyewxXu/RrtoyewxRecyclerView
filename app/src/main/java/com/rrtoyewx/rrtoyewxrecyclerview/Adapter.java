package com.rrtoyewx.rrtoyewxrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrtoyewx.recyclerviewlibrary.viewholder.SimpleViewHolder;

/**
 * Created by Rrtoyewx on 16/8/3.
 */
public class Adapter extends RecyclerView.Adapter {
    private Context context;
    private int itemCount = 0;

    public Adapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new SimpleViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemCount;
    }


    public void setItemCount(int itemCount){
        this.itemCount = itemCount;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
