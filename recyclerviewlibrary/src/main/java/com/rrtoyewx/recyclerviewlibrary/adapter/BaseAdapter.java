package com.rrtoyewx.recyclerviewlibrary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rrtoyewx on 16/8/8.
 * T:数据类型
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected BaseAdapter wrapperAdapter;
    protected Context context;
    protected List<T> dataList;

    {
        dataList = new ArrayList<>();
    }

    public BaseAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(@NonNull List<T> dataList) {
        this.dataList = dataList;
    }

    /**
     * 刷新数据，尽量使用这个方法，而不是notifyDataSetChanged();
     */
    public void requestNotify() {
        if (wrapperAdapter != null) {
            wrapperAdapter.notifyDataSetChanged();
        } else {
            notifyDataSetChanged();
        }
    }

    public void setWrapperAdapter(BaseAdapter wrapperAdapter){
        this.wrapperAdapter = wrapperAdapter;
    }

    public void addDataList(@NonNull List<T> dataList) {
        this.dataList.addAll(dataList);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
