package com.rrtoyewx.recyclerviewlibrary.adapter;

import android.support.annotation.CheckResult;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.rrtoyewx.recyclerviewlibrary.RrtoyewxRecyclerView;
import com.rrtoyewx.recyclerviewlibrary.viewholder.SimpleViewHolder;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Rrtoyewx on 16/8/3.
 * 能够addHeaderView和addFooterView
 */
public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int HEADER_TYPE = 2000;
    public static final int FOOTER_TYPE = 4000;

    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    private List<View> headerViewList;
    private List<View> footerViewList;


    {
        headerViewList = new ArrayList<>();
        footerViewList = new ArrayList<>();
    }

    public WrapperAdapter() {

    }


    @Override
    public int getItemViewType(int position) {
        Log.e("TAG", "getItemViewType" + position);

        if (isHeader(position)) {
            return HEADER_TYPE + position;
        }

        if (isFooter(position)) {
            return FOOTER_TYPE + position - headerViewList.size() - adapter.getItemCount();
        }

        return adapter.getItemViewType(position - headerViewList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType >= FOOTER_TYPE) {
            return new SimpleViewHolder(footerViewList.get(viewType - FOOTER_TYPE));
        }

        if (viewType >= HEADER_TYPE) {
            return new SimpleViewHolder(headerViewList.get(viewType - HEADER_TYPE));
        }

        return adapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (!(isHeader(position) && isFooter(position))) {
            adapter.onBindViewHolder(holder, calculateAdapterPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return footerViewList.size() + headerViewList.size() + adapter.getItemCount();
    }

    public void addHeaderView(View headerView) {
        headerViewList.add(headerView);
        notifyDataSetChanged();
    }

    public void addFooterView(View footerView) {
        footerViewList.add(footerView);
        notifyDataSetChanged();
    }

    public void setAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        this.adapter = adapter;

        footerViewList.clear();
        headerViewList.clear();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) {
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final int spanCount = gridLayoutManager.getSpanCount();
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return isHeader(position) || isFooter(position)
                                ? spanCount : 1;
                    }
                });
            }
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        int layoutPosition = holder.getLayoutPosition();
        View itemView = holder.itemView;
        if (itemView != null) {
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if (layoutParams != null
                    && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                params.setFullSpan(isHeader(layoutPosition) || isFooter(layoutPosition));

            }
        }
        super.onViewAttachedToWindow(holder);
    }

    @CheckResult
    private boolean isHeader(int position) {
        return position < headerViewList.size();
    }

    @CheckResult
    private boolean isFooter(int position) {
        return adapter != null && (position >= headerViewList.size() + adapter.getItemCount());
    }

    private int calculateAdapterPosition(int position) {
        return adapter == null ? -1 : position - headerViewList.size();
    }
}
