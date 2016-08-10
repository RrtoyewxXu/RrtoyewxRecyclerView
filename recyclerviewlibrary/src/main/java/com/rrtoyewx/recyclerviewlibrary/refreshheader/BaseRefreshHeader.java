package com.rrtoyewx.recyclerviewlibrary.refreshheader;

import android.view.View;

/**
 * Created by Rrtoyewx on 16/8/10.
 */
public interface BaseRefreshHeader {
    int REFRESH_STATE_IDLE = 0;
    int REFRESH_STATE_PULL = 1;
    int REFRESH_STATE_RELEASE = 2;
    int REFRESH_STATE_REFRESHING = 3;

    void moveTo(int distance, int newY, int oldY);

    View getHeaderView();

    void setHeaderHeight();

    void setHeaderMaxHeight();

    int getHeaderHeight();

    int getHeaderMaxHeight();

    void setRefreshState(int refreshState);

    int getRefreshState();
}
