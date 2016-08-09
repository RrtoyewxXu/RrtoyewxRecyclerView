package com.rrtoyewx.recyclerviewlibrary.adapter;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.rrtoyewx.recyclerviewlibrary.R;
import com.rrtoyewx.recyclerviewlibrary.viewholder.SimpleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rrtoyewx on 16/8/3.
 * 能够addHeaderView和addFooterView
 */
public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int LOAD_MORE_VIEW_FOOTER_TYPE = -2;

    public static final int HEADER_TYPE = 2000;
    public static final int FOOTER_TYPE = 4000;

    private Context context;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    private List<View> headerViewList;
    private List<View> footerViewList;

    private View loadMoreView;
    private boolean showLoadMoreViewFlag;


    {
        headerViewList = new ArrayList<>();
        footerViewList = new ArrayList<>();
    }

    public WrapperAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        //  Log.e("TAG", "getItemViewType" + position);

        if (isHeader(position)) {
            return HEADER_TYPE + position;
        }

        if (isFooter(position)) {
            return FOOTER_TYPE + position - headerViewList.size() - adapter.getItemCount();
        }

        if (isLoadMoreView(position)) {
            return LOAD_MORE_VIEW_FOOTER_TYPE;
        }

        return adapter.getItemViewType(position - headerViewList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LOAD_MORE_VIEW_FOOTER_TYPE) {
            if (loadMoreView != null) {
                return new SimpleViewHolder(loadMoreView);
            } else {
                return new SimpleViewHolder(context, parent, R.layout.default_item_load_more_view);
            }
        }

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
            adapter.onBindViewHolder(holder, calculateInnerAdapterPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return footerViewList.size() + headerViewList.size() + adapter.getItemCount() + (showLoadMoreViewFlag ? 1 : 0);
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

    public void removeHeaderView(View headerView) {
        if (headerViewList.contains(headerView)) {
            headerViewList.remove(headerView);
            notifyDataSetChanged();
        }
    }

    public void removeFooterView(View footerView) {
        if (footerViewList.contains(footerView)) {
            footerViewList.remove(footerView);
            notifyDataSetChanged();
        }
    }

    public void setLoadMoreView(View loadMoreView) {
        this.loadMoreView = loadMoreView;
    }

    public void showLoadMoreView() {
        showLoadMoreViewFlag = true;
        notifyDataSetChanged();
    }

    public void hideLoadMoreView() {
        showLoadMoreViewFlag = false;
        notifyDataSetChanged();
    }

    public boolean isLoadMore() {
        return showLoadMoreViewFlag;
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
                params.setFullSpan(isHeader(layoutPosition) || isFooter(layoutPosition) || isLoadMoreView(layoutPosition));
            }
        }
        super.onViewAttachedToWindow(holder);
    }

    @CheckResult
    public boolean isHeader(int position) {
        return position < headerViewList.size();
    }

    @CheckResult
    public boolean isFooter(int position) {
        return adapter != null && ((position >= headerViewList.size() + adapter.getItemCount()) && !isLoadMoreView(position));
    }

    @CheckResult
    public boolean isLoadMoreView(int position) {
        return showLoadMoreViewFlag && position == getItemCount() - 1;
    }

    private int calculateInnerAdapterPosition(int position) {
        return adapter == null ? -1 : position - headerViewList.size();
    }

    public int getInnerAdapterCount() {
        return adapter == null ? 0 : adapter.getItemCount();
    }
}
