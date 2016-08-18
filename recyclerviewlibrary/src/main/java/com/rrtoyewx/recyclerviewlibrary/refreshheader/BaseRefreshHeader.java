package com.rrtoyewx.recyclerviewlibrary.refreshheader;

import android.view.View;

import com.rrtoyewx.recyclerviewlibrary.RrtoyewxRecyclerView;

/**
 * Created by Rrtoyewx on 16/8/10.
 */
public abstract class BaseRefreshHeader {
    public static final int REFRESH_STATE_IDLE = 0;
    public static final int REFRESH_STATE_PULL = 1;
    public static final int REFRESH_STATE_RELEASE = 2;
    public static final int REFRESH_STATE_REFRESHING = 3;

    protected int mRefreshState;

    protected View mHeaderContainer;
    protected int mRefreshHeaderHeight;
    protected int mRefreshHeaderMaxHeight;

    public final void move(int distance, int newY, int oldY) {
        if (distance <= mRefreshHeaderMaxHeight) {
            changeContainerUI(distance, newY, oldY);
            changeRefreshStates(distance);
        }
    }

    public abstract void changeContainerUI(int distance, int newY, int oldY);

    public abstract void changeRefreshStates(int distance);

    public abstract void upOrCancel(RrtoyewxRecyclerView.RefreshDataListener refreshListener);

    public abstract void completeRefresh();

    public View getHeaderView() {
        return mHeaderContainer;
    }

    public void setHeaderHeight(int headerHeight) {
        this.mRefreshHeaderHeight = headerHeight;
    }

    public int getHeaderHeight() {
        return mRefreshHeaderHeight;
    }

    public void setHeaderMaxHeight(int headerMaxHeight) {
        this.mRefreshHeaderMaxHeight = headerMaxHeight;
    }

    public int getHeaderMaxHeight() {
        return mRefreshHeaderMaxHeight;
    }

    public void setRefreshState(int refreshState) {
        this.mRefreshState = refreshState;
    }

    public int getRefreshState() {
        return mRefreshState;
    }

    public int getVisibleHeaderHeight() {
        return mHeaderContainer.getLayoutParams().height;
    }
}
