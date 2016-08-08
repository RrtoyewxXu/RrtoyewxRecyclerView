package com.rrtoyewx.recyclerviewlibrary;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ListView;

import com.rrtoyewx.recyclerviewlibrary.adapter.NoItemAdapter;
import com.rrtoyewx.recyclerviewlibrary.adapter.WrapperAdapter;

/**
 * Created by Rrtoyewx on 16/8/3.
 */
public class RrtoyewxRecyclerView extends RecyclerView {
    View pullRefreshView;
    View loadMoreView;

    View emptyView;

    WrapperAdapter wrapperAdapter;
    DataObserver dataObserver;

    int overScrollMode;

    {
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
        init(context);
    }

    private void init(Context context) {
        setAdapter(new NoItemAdapter(context));
        this.overScrollMode = getOverScrollMode();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        wrapperAdapter.setAdapter(adapter);
        super.setAdapter(wrapperAdapter);
        adapter.registerAdapterDataObserver(dataObserver);

        dataObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;

        dataObserver.onChanged();
    }

    public void addHeaderView(View headerView) {
        wrapperAdapter.addHeaderView(headerView);
        smoothScrollToPosition(0);
    }

    public void addFooterView(View footerView) {
        wrapperAdapter.addFooterView(footerView);
        smoothScrollToPosition(wrapperAdapter.getItemCount() - 1);
    }

    public void removeHeaderView(View headerView) {
        wrapperAdapter.removeHeaderView(headerView);
    }

    public void removeFooterView(View footerView) {
        wrapperAdapter.removeFooterView(footerView);
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
                        return (wrapperAdapter.isHeader(position) || wrapperAdapter.isFooter(position))
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
