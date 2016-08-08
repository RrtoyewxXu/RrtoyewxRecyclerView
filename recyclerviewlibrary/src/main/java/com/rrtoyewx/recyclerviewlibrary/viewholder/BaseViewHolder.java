package com.rrtoyewx.recyclerviewlibrary.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rrtoyewx on 16/8/3.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    public BaseViewHolder(Context context, ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    public abstract void initView(View itemView);
}
