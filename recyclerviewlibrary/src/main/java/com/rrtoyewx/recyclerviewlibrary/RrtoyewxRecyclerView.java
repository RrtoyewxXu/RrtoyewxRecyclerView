package com.rrtoyewx.recyclerviewlibrary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.rrtoyewx.recyclerviewlibrary.adapter.WrapperAdapter;

/**
 * Created by Rrtoyewx on 16/8/3.
 */
public class RrtoyewxRecyclerView extends RecyclerView {

    View pullRefreshView;

    View loadMoreView;

    SparseArray<View> headerViewList;
    SparseArray<View> footerViewList;

    View emptyView;

    WrapperAdapter wrapperAdapter;
    DataObserver dataObserver;

    {
        headerViewList = new SparseArray<>();
        footerViewList = new SparseArray<>();

        wrapperAdapter = new WrapperAdapter();
        dataObserver = new DataObserver();
    }

    public RrtoyewxRecyclerView(Context context) {
        this(context, null);
    }

    public RrtoyewxRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RrtoyewxRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {

    }

    @Override
    public void setAdapter(Adapter adapter) {
        wrapperAdapter.setAdapter(adapter);
        super.setAdapter(wrapperAdapter);
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public void addHeaderView(View headerView) {
        wrapperAdapter.addHeaderView(headerView);
        smoothScrollToPosition(0);
    }

    public void addFooterView(View footerView) {
        wrapperAdapter.addFooterView(footerView);
        smoothScrollToPosition(wrapperAdapter.getItemCount() - 1);
    }


    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            RrtoyewxRecyclerView.this.smoothScrollToPosition(positionStart);
        }
    }
}
