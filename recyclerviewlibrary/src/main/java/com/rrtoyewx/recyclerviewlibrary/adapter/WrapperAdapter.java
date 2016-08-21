package com.rrtoyewx.recyclerviewlibrary.adapter;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.rrtoyewx.recyclerviewlibrary.R;
import com.rrtoyewx.recyclerviewlibrary.refreshheader.BaseRefreshHeader;
import com.rrtoyewx.recyclerviewlibrary.viewholder.SimpleViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rrtoyewx on 16/8/3.
 * 能够addHeaderView和addFooterView
 */
public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = WrapperAdapter.class.getSimpleName();

    public static final int LOAD_MORE_FOOTER_TYPE = -2;
    public static final int PULL_REFRESH_HEADER_TYPE = -1;

    public static final int HEADER_TYPE = 2000;
    public static final int FOOTER_TYPE = 4000;

    private Context context;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;

    private List<View> headerViewList;
    private List<View> footerViewList;

    private View loadMoreView;
    private boolean showLoadMoreViewFlag;

    private BaseRefreshHeader refreshHeader;
    private boolean showPullRefreshFlag;


    {
        headerViewList = new ArrayList<>();
        footerViewList = new ArrayList<>();
    }

    public WrapperAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPullRefreshView(position)) {
            Log.d(TAG, position + "PullRefreshView");
            return PULL_REFRESH_HEADER_TYPE;
        }

        if (isHeader(position)) {
            Log.d(TAG, position + "isHeader" + (HEADER_TYPE + position - (showPullRefreshFlag ? 1 : 0)));
            return HEADER_TYPE + position - (showPullRefreshFlag ? 1 : 0);
        }

        if (isFooter(position)) {
            Log.d(TAG, position + "isFooter" + (FOOTER_TYPE + position
                    - headerViewList.size()
                    - adapter.getItemCount()
                    - (showPullRefreshFlag ? 1 : 0)));

            return FOOTER_TYPE + position
                    - headerViewList.size()
                    - adapter.getItemCount()
                    - (showPullRefreshFlag ? 1 : 0);
        }

        if (isLoadMoreView(position)) {
            Log.d(TAG, position + "isLoadMoreView");
            return LOAD_MORE_FOOTER_TYPE;
        }

        Log.d(TAG, position + "adapter" + (adapter.getItemViewType(position - headerViewList.size() - (showPullRefreshFlag ? 1 : 0))));
        return adapter.getItemViewType(position - headerViewList.size() - (showPullRefreshFlag ? 1 : 0));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == PULL_REFRESH_HEADER_TYPE) {
            Log.d(TAG, "HEADER");
            return new SimpleViewHolder(refreshHeader.getHeaderView());
        }

        if (viewType == LOAD_MORE_FOOTER_TYPE) {
            if (loadMoreView != null) {
                return new SimpleViewHolder(loadMoreView);
            } else {
                return new SimpleViewHolder(context, parent, R.layout.default_load_more_footer);
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
        if (!(isHeader(position) || isFooter(position) || isPullRefreshView(position) || isLoadMoreView(position))) {
            adapter.onBindViewHolder(holder, calculateInnerAdapterPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        return footerViewList.size()
                + headerViewList.size()
                + adapter.getItemCount()
                + (showLoadMoreViewFlag ? 1 : 0)
                + (showPullRefreshFlag ? 1 : 0);
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

    public void removeAllHeaderView() {
        if (headerViewList.size() != 0) {
            headerViewList.clear();
            notifyDataSetChanged();
        }
    }

    public void removeAllFooterView() {
        if (footerViewList.size() != 0) {
            footerViewList.clear();
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

    public void setShowPullRefreshFlag(boolean showPullRefreshFlag) {
        boolean needNotify = !(this.showPullRefreshFlag == showPullRefreshFlag) && refreshHeader != null;
        this.showPullRefreshFlag = showPullRefreshFlag;

        if (needNotify) {
            notifyDataSetChanged();
        }
    }

    public void setPullRefreshHeader(BaseRefreshHeader refreshHeader) {
        this.refreshHeader = refreshHeader;
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
                params.setFullSpan(isHeader(layoutPosition) || isFooter(layoutPosition) || isLoadMoreView(layoutPosition) || isPullRefreshView(layoutPosition));
            }
        }
        super.onViewAttachedToWindow(holder);
    }

    @CheckResult
    public boolean isPullRefreshView(int position) {
        return showPullRefreshFlag && position == 0;
    }

    @CheckResult
    public boolean isHeader(int position) {
        return (showPullRefreshFlag ? position - 1 : position) < headerViewList.size() && !isPullRefreshView(position);
    }

    @CheckResult
    public boolean isFooter(int position) {
        return adapter != null &&
                ((position >= headerViewList.size() + adapter.getItemCount() + (showPullRefreshFlag ? 1 : 0))
                        && !isLoadMoreView(position));
    }

    @CheckResult
    public boolean isLoadMoreView(int position) {
        return showLoadMoreViewFlag && position == getItemCount() - 1;
    }

    private int calculateInnerAdapterPosition(int position) {
        return adapter == null ? -1 : position - headerViewList.size() - (showPullRefreshFlag ? 1 : 0);
    }

    public int getInnerAdapterCount() {
        return adapter == null ? 0 : adapter.getItemCount();
    }


}
