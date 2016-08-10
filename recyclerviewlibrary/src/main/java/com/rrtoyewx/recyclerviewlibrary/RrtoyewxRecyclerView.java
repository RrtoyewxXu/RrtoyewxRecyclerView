package com.rrtoyewx.recyclerviewlibrary;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.rrtoyewx.recyclerviewlibrary.adapter.NoItemAdapter;
import com.rrtoyewx.recyclerviewlibrary.adapter.WrapperAdapter;

import static com.rrtoyewx.recyclerviewlibrary.refreshheader.BaseRefreshHeader.*;

import com.rrtoyewx.recyclerviewlibrary.refreshheader.ArrowRefreshHeader;
import com.rrtoyewx.recyclerviewlibrary.refreshheader.BaseRefreshHeader;


/**
 * Created by Rrtoyewx on 16/8/3.
 * 封装recyclerView
 */
public class RrtoyewxRecyclerView extends RecyclerView {
    private static final String TAG = RrtoyewxRecyclerView.class.getSimpleName();
    private static final int DEFAULT_SCROLL_DISTANCE = 50;
    View pullRefreshView;

    BaseRefreshHeader refreshHeader;
    boolean refreshEnable;

    View loadMoreView;
    boolean loadMoreEnable;

    View emptyView;

    WrapperAdapter wrapperAdapter;
    DataObserver dataObserver;

    int overScrollMode;

    int downMotionEventY;
    int curMotionEventY;

    RefreshListener refreshListener;


    public interface RefreshListener {
        void onLoadMore();
    }

    public RrtoyewxRecyclerView(Context context) {
        this(context, null);
    }

    public RrtoyewxRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RrtoyewxRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        dataObserver = new DataObserver();
        overScrollMode = getOverScrollMode();

        refreshHeader = new ArrowRefreshHeader(context);

        wrapperAdapter = new WrapperAdapter(context);
        setAdapter(new NoItemAdapter(context));
    }

    @Override
    public void setAdapter(Adapter adapter) {
        wrapperAdapter.setAdapter(adapter);
        super.setAdapter(wrapperAdapter);
        adapter.registerAdapterDataObserver(dataObserver);

        dataObserver.onChanged();
    }

    /**
     * setEmptyView
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;

        dataObserver.onChanged();
    }

    /**
     * addHeaderView
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        wrapperAdapter.addHeaderView(headerView);
        smoothScrollToPosition(0);
    }

    /**
     * addFooterView
     *
     * @param footerView
     */
    public void addFooterView(View footerView) {
        wrapperAdapter.addFooterView(footerView);
        smoothScrollToPosition(wrapperAdapter.getItemCount() - 1);
    }

    /**
     * remove headerView
     *
     * @param headerView
     */
    public void removeHeaderView(View headerView) {
        wrapperAdapter.removeHeaderView(headerView);
    }

    /**
     * remove footerView
     *
     * @param footerView
     */
    public void removeFooterView(View footerView) {
        wrapperAdapter.removeFooterView(footerView);
    }

    /**
     * setLoadMoreEnable
     *
     * @param loadMoreEnable
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    /**
     * setLoadMoreView
     *
     * @param loadMoreView
     */
    public void setLoadMoreView(View loadMoreView) {
        this.loadMoreView = loadMoreView;
        wrapperAdapter.setLoadMoreView(loadMoreView);
    }

    public void showLoadMoreView() {
        wrapperAdapter.showLoadMoreView();

        if (refreshListener != null) {
            refreshListener.onLoadMore();
        }
    }

    public void completeLoadMore() {
        wrapperAdapter.hideLoadMoreView();
    }

    public boolean checkIsLoadMore() {
        return wrapperAdapter.isLoadMore();
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.refreshEnable = refreshEnable;
        wrapperAdapter.setShowPullRefreshFlag(refreshEnable);
    }

    public void addRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public void removeRefreshListener(RefreshListener listener) {
        if (this.refreshListener == listener) {
            this.refreshListener = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        switch (e.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                downMotionEventY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                curMotionEventY = (int) e.getRawY();
                if (downMotionEventY - curMotionEventY >= DEFAULT_SCROLL_DISTANCE) {
                    if (checkCanLoadMore()) {
                        showLoadMoreView();
                    }
                }

                if (checkCanRefresh()) {
                    Log.e(TAG, "checkCanRefresh()" + checkCanRefresh());
                    refreshHeader.moveTo(curMotionEventY - downMotionEventY, curMotionEventY, downMotionEventY);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return super.onTouchEvent(e);
    }

    @CheckResult
    private boolean checkCanRefresh() {
        LayoutManager layoutManager = getLayoutManager();
        Adapter adapter = getAdapter();
        if (adapter != null && layoutManager != null) {
            int firstVisiblePosition = calculateFirstVisiblePosition(layoutManager);
            if (firstVisiblePosition != -1
                    && refreshHeader.getRefreshState() != REFRESH_STATE_REFRESHING
                    && refreshEnable) {

                return true;
            }
        }

        return false;
    }

    @CheckResult
    private boolean checkCanLoadMore() {
        LayoutManager layoutManager = getLayoutManager();
        WrapperAdapter adapter = (WrapperAdapter) getAdapter();
        if (adapter != null && layoutManager != null) {

            int lastVisiblePosition = calculateLastVisiblePosition(layoutManager);

            if (lastVisiblePosition != -1
                    && adapter.getInnerAdapterCount() != 0
                    && (lastVisiblePosition == adapter.getItemCount() - 1)
                    && !checkIsLoadMore()) {

                return true;
            }
        }

        return false;
    }

    @CheckResult
    private int calculateLastVisiblePosition(LayoutManager layoutManager) {

        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            return linearLayoutManager.findLastVisibleItemPosition();
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;

            int[] lastVisibleItemPositions = staggeredGridLayoutManager.findLastVisibleItemPositions(null);
            int max = -1;
            for (int i = 0; i < lastVisibleItemPositions.length; i++) {
                max = Math.max(max, lastVisibleItemPositions[i]);
                Log.e(TAG, "lastVisibleItemPositions" + i + lastVisibleItemPositions[i]);
            }

            return max;
        }

        return -1;
    }

    private int calculateFirstVisiblePosition(LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            return linearLayoutManager.findFirstVisibleItemPosition();
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int[] firstVisibleItemPositions = staggeredGridLayoutManager.findFirstVisibleItemPositions(null);

            int min = 10;
            for (int i = 0; i < firstVisibleItemPositions.length; i++) {
                min = Math.min(min, firstVisibleItemPositions[i]);
                Log.e(TAG, "firstVisibleItemPositions" + i + firstVisibleItemPositions[i]);
            }

            return min;
        }

        return -1;
    }

    @Override
    public void setOverScrollMode(int overScrollMode) {
        super.setOverScrollMode(overScrollMode);
        this.overScrollMode = overScrollMode;
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        if (wrapperAdapter != null) {

            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final int spanCount = gridLayoutManager.getSpanCount();
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (wrapperAdapter.isHeader(position)
                                || wrapperAdapter.isFooter(position)
                                || wrapperAdapter.isLoadMoreView(position))
                                ? spanCount : 1;
                    }
                });
            }
        }
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            int innerAdapterCount = wrapperAdapter.getInnerAdapterCount();
            if (emptyView != null) {

                emptyView.setVisibility(innerAdapterCount == 0
                        ? VISIBLE : INVISIBLE);
                RrtoyewxRecyclerView.this.setVisibility(innerAdapterCount == 0
                        ? INVISIBLE : VISIBLE);
                RrtoyewxRecyclerView.this.setOverScrollMode(innerAdapterCount == 0
                        ? OVER_SCROLL_NEVER : overScrollMode);
            }

            if (wrapperAdapter != null) {
                wrapperAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            if (wrapperAdapter != null) {
                wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (wrapperAdapter != null) {
                wrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (wrapperAdapter != null) {
                wrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (wrapperAdapter != null) {
                wrapperAdapter.notifyItemRangeRemoved(fromPosition, itemCount);
            }
        }
    }
}
