package com.rrtoyewx.recyclerviewlibrary.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rrtoyewx on 16/8/3.
 */
public class SimpleViewHolder extends BaseViewHolder {

    public SimpleViewHolder(View itemView) {
        super(itemView);
    }

    public SimpleViewHolder(Context context, ViewGroup parent, int layoutId) {
        super(context, parent, layoutId);
    }

    @Override
    public void initView(View itemView) {
        //doNothing
    }
}
