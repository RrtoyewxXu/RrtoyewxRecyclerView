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

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * Created by Rrtoyewx on 16/8/3.
 * 封装recyclerView
 */
public class RrtoyewxRecyclerView extends RecyclerView {
    private static final String TAG = RrtoyewxRecyclerView.class.getSimpleName();

    private Context mContext;
    private WrapperAdapter mWrapperAdapter;

    private BaseRefreshHeader mRefreshHeader;
    private boolean mRefreshEnable;

    private View mLoadMoreView;
    private boolean mLoadMoreEnable;

    private View mEmptyView;
    private DataObserver mDataObserver;

    private int mOverScrollMode;

    private int mDownMotionEventY;
    private int mCurMotionEventY;

    private OnScrollListener mOnScrollListener;
    private RefreshDataListener mRefreshDataListener;

    public interface RefreshDataListener {
        void onRefresh();

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
        this.mContext = context;
        mDataObserver = new DataObserver();
        mOverScrollMode = getOverScrollMode();

        mWrapperAdapter = new WrapperAdapter(context);
        setAdapter(new NoItemAdapter(context));

        mOnScrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == SCROLL_STATE_DRAGGING) {
                    if (checkCanLoadMore()) {
                        showLoadMoreView();
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        };

    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapperAdapter.setAdapter(adapter);
        super.setAdapter(mWrapperAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);

        mDataObserver.onChanged();
    }


    /**
     * setEmptyView
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;

        mDataObserver.onChanged();
    }

    /**
     * addHeaderView
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        mWrapperAdapter.addHeaderView(headerView);
        smoothScrollToPosition(0);
    }

    /**
     * addFooterView
     *
     * @param footerView
     */
    public void addFooterView(View footerView) {
        mWrapperAdapter.addFooterView(footerView);
        smoothScrollToPosition(mWrapperAdapter.getItemCount() - 1);
    }

    /**
     * remove headerView
     *
     * @param headerView
     */
    public void removeHeaderView(View headerView) {
        mWrapperAdapter.removeHeaderView(headerView);
    }

    /**
     * remove footerView
     *
     * @param footerView
     */
    public void removeFooterView(View footerView) {
        mWrapperAdapter.removeFooterView(footerView);
    }

    /**
     * setLoadMoreEnable
     *
     * @param loadMoreEnable
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.mLoadMoreEnable = loadMoreEnable;
        if (loadMoreEnable) {
            addOnScrollListener(mOnScrollListener);
        } else {
            removeOnScrollListener(mOnScrollListener);
        }
    }

    /**
     * setLoadMoreView
     *
     * @param loadMoreView
     */
    public void setLoadMoreView(View loadMoreView) {
        this.mLoadMoreView = loadMoreView;
        mWrapperAdapter.setLoadMoreView(loadMoreView);
    }

    public void showLoadMoreView() {
        mWrapperAdapter.showLoadMoreView();

        if (mRefreshDataListener != null) {
            mRefreshDataListener.onLoadMore();
        }
    }

    public void completeLoadMore() {
        mWrapperAdapter.hideLoadMoreView();
    }

    public boolean checkIsLoadMore() {
        return mWrapperAdapter.isLoadMore();
    }

    public void setRefreshEnable(boolean mRefreshEnable) {
        this.mRefreshEnable = mRefreshEnable;
        mWrapperAdapter.setShowPullRefreshFlag(mRefreshEnable);

        if (mRefreshEnable) {

            if (mRefreshHeader == null) {
                BaseRefreshHeader refreshHeader = new ArrowRefreshHeader(mContext);
                setRefreshHeader(refreshHeader);
            }
        }
    }

    public void setRefreshHeader(BaseRefreshHeader header) {
        boolean needNotify = !(this.mRefreshHeader == header);

        if (needNotify) {
            mRefreshHeader = header;
            mWrapperAdapter.setPullRefreshHeader(header);

            super.setAdapter(mWrapperAdapter);
        }
    }

    public void completeRefresh() {
        if (mRefreshHeader != null) {
            mRefreshHeader.completeRefresh();
        }
    }

    public void addRefreshListener(RefreshDataListener refreshListener) {
        this.mRefreshDataListener = refreshListener;
    }

    public void removeRefreshListener(RefreshDataListener listener) {
        if (this.mRefreshDataListener == listener) {
            this.mRefreshDataListener = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        switch (e.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownMotionEventY = (int) e.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                mCurMotionEventY = (int) e.getRawY();

                if (checkCanRefresh()) {
                    Log.d(TAG, "canRefresh");
                    mRefreshHeader.move((mCurMotionEventY - mDownMotionEventY) / 2, mCurMotionEventY, mDownMotionEventY);
                }

                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mRefreshHeader.getRefreshState() != REFRESH_STATE_REFRESHING) {
                    mRefreshHeader.upOrCancel(mRefreshDataListener);
                }
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
            Log.d(TAG, "firstVisiblePosition" + firstVisiblePosition);
            if (mRefreshEnable
                    && !checkIsLoadMore()
                    && firstVisiblePosition == (mRefreshHeader.getVisibleHeaderHeight() == 0 ? 1 : 0)
                    && mRefreshHeader.getRefreshState() != REFRESH_STATE_REFRESHING) {

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
                    && !checkIsLoadMore()
                    && mRefreshHeader.getRefreshState() == REFRESH_STATE_IDLE) {

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
        this.mOverScrollMode = overScrollMode;
    }

    @Override
    public void setLayoutManager(LayoutManager layoutManager) {
        super.setLayoutManager(layoutManager);
        if (mWrapperAdapter != null) {

            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final int spanCount = gridLayoutManager.getSpanCount();
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (mWrapperAdapter.isHeader(position)
                                || mWrapperAdapter.isFooter(position)
                                || mWrapperAdapter.isLoadMoreView(position))
                                || mWrapperAdapter.isPullRefreshView(position)
                                ? spanCount : 1;
                    }
                });
            }
        }
    }

    public void clear() {
        try {
            Field recyclerFiled = RecyclerView.class.getDeclaredField("mRecycler");
            recyclerFiled.setAccessible(true);
//            Method clearMethod = Class.forName("android.support.v7.widget.RecyclerView$Recycler")
//                    .getDeclaredMethod("clear");
//            clearMethod.setAccessible(true);
            RecyclerView.Recycler recycler = (Recycler) recyclerFiled.get(RrtoyewxRecyclerView.this);
            recycler.clear();
//            clearMethod.invoke(recycler);
            recycler.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getRecycledViewPool().clear();
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            int innerAdapterCount = mWrapperAdapter.getInnerAdapterCount();
            if (mEmptyView != null) {

                mEmptyView.setVisibility(innerAdapterCount == 0
                        ? VISIBLE : INVISIBLE);
                RrtoyewxRecyclerView.this.setVisibility(innerAdapterCount == 0
                        ? INVISIBLE : VISIBLE);
                RrtoyewxRecyclerView.this.setOverScrollMode(innerAdapterCount == 0
                        ? OVER_SCROLL_NEVER : mOverScrollMode);
            }

            if (mWrapperAdapter != null) {
                mWrapperAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            if (mWrapperAdapter != null) {
                mWrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mWrapperAdapter != null) {
                mWrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mWrapperAdapter != null) {
                mWrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mWrapperAdapter != null) {
                mWrapperAdapter.notifyItemRangeRemoved(fromPosition, itemCount);
            }
        }
    }
}
