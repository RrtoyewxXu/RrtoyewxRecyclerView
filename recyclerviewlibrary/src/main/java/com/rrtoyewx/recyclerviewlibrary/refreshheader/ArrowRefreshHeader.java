package com.rrtoyewx.recyclerviewlibrary.refreshheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rrtoyewx.recyclerviewlibrary.R;
import com.rrtoyewx.recyclerviewlibrary.RrtoyewxRecyclerView;
import com.rrtoyewx.recyclerviewlibrary.utils.AnimationUtil;
import com.rrtoyewx.recyclerviewlibrary.utils.DateUtil;
import com.rrtoyewx.recyclerviewlibrary.utils.ScreenUtil;

/**
 * Created by Rrtoyewx on 16/8/10.
 * 带箭头的下拉刷新
 */
public class ArrowRefreshHeader extends BaseRefreshHeader {
    private static final int DEFAULT_REFRESH_HEADER_HEIGHT = 50;
    private static final int DEFAULT_REFRESH_HEADER_MAX_HEIGHT = 200;

    private Context context;
    //refresh_arrow
    private ImageView mArrowImage;
    //refresh_loading
    private ProgressBar mLoadingBar;
    //refresh_date
    private TextView mDateTv;
    //refresh_message
    private TextView mHintMessageTv;

    private long preRefreshTime;

    public ArrowRefreshHeader(Context context) {
        this.context = context;
        mHeaderContainer = LayoutInflater.from(context).inflate(R.layout.default_normal_refresh_header, null, false);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 0);
        mHeaderContainer.setLayoutParams(layoutParams);

        mArrowImage = (ImageView) mHeaderContainer.findViewById(R.id.refresh_arrow);
        mLoadingBar = (ProgressBar) mHeaderContainer.findViewById(R.id.refresh_loading);
        mDateTv = (TextView) mHeaderContainer.findViewById(R.id.refresh_date);
        mHintMessageTv = (TextView) mHeaderContainer.findViewById(R.id.refresh_message);

        mDateTv.setVisibility(View.GONE);
    }

    private void initData() {
        mRefreshState = REFRESH_STATE_IDLE;
        mRefreshHeaderHeight = ScreenUtil.dpToPx(context, DEFAULT_REFRESH_HEADER_HEIGHT);
        mRefreshHeaderMaxHeight = ScreenUtil.dpToPx(context, DEFAULT_REFRESH_HEADER_MAX_HEIGHT);
    }

    @Override
    public void changeContainerUI(int distance, int newY, int oldY) {
        ViewGroup.LayoutParams layoutParams = mHeaderContainer.getLayoutParams();
        layoutParams.height = distance;
        mHeaderContainer.setLayoutParams(layoutParams);
    }

    @Override
    public void changeRefreshStates(int distance) {
        if (mRefreshState == REFRESH_STATE_IDLE) {

            if (distance > 0) {
                mRefreshState = REFRESH_STATE_PULL;
                firstEnterHeaderPullState();
            }

        } else if (mRefreshState == REFRESH_STATE_PULL) {

            if (distance > mRefreshHeaderHeight) {
                mRefreshState = REFRESH_STATE_RELEASE;
                enterHeaderReleaseState();
            }

        } else if (mRefreshState == REFRESH_STATE_RELEASE) {

            if (distance <= mRefreshHeaderHeight) {
                mRefreshState = REFRESH_STATE_PULL;
                enterHeaderPullState();
            }
        }

        if (distance <= 0) {
            resetRefreshHeader();
        }
    }

    private void firstEnterHeaderPullState() {
        flushDateView();
    }

    private void enterHeaderPullState() {
        mHintMessageTv.setText(R.string.refresh_pull_refresh);
        startPullAnimator();
    }

    private void enterHeaderReleaseState() {
        mHintMessageTv.setText(R.string.refresh_release_refresh);
        startReleaseAnimator();
    }

    private void enterHeaderRefreshingState() {
        ViewGroup.LayoutParams layoutParams = mHeaderContainer.getLayoutParams();
        layoutParams.height = mRefreshHeaderHeight;
        mHeaderContainer.setLayoutParams(layoutParams);

        mArrowImage.setVisibility(View.INVISIBLE);
        mLoadingBar.setVisibility(View.VISIBLE);
        mHintMessageTv.setText(R.string.loading_medium);
    }

    private void startReleaseAnimator() {
        mArrowImage.clearAnimation();
        AnimationUtil.generateRotateAnimation(mArrowImage, 300, 0, 180).start();
    }

    private void startPullAnimator() {
        mArrowImage.clearAnimation();
        AnimationUtil.generateRotateAnimation(mArrowImage, 300, 180, 0).start();
    }

    private void resetRefreshHeader() {
        mRefreshState = REFRESH_STATE_IDLE;

        mArrowImage.setVisibility(View.VISIBLE);
        mLoadingBar.setVisibility(View.INVISIBLE);
        mHintMessageTv.setText(R.string.refresh_pull_refresh);

        ViewGroup.LayoutParams layoutParams = mHeaderContainer.getLayoutParams();
        layoutParams.height = 0;
        mHeaderContainer.setLayoutParams(layoutParams);
    }

    @Override
    public void upOrCancel(RrtoyewxRecyclerView.RefreshDataListener refreshListener) {
        if (mRefreshState == REFRESH_STATE_RELEASE) {
            mRefreshState = REFRESH_STATE_REFRESHING;
            enterHeaderRefreshingState();

            if (refreshListener != null) {
                refreshListener.onRefresh();
            }

        } else {
            resetRefreshHeader();
        }
    }

    @Override
    public void completeRefresh() {
        saveDateTime();
        resetRefreshHeader();
    }

    private void saveDateTime() {
        preRefreshTime = System.currentTimeMillis();
    }

    private void flushDateView() {
        if (preRefreshTime == 0) {
            return;
        }

        if (!mDateTv.isShown()) {
            mDateTv.setVisibility(View.VISIBLE);
        }
        long curRefreshTime = System.currentTimeMillis();
        mDateTv.setText(R.string.refresh_date);

        String refreshDistanceTime = DateUtil.convertTime(curRefreshTime - preRefreshTime);
        if (refreshDistanceTime.startsWith("0")) {
            refreshDistanceTime = "刚刚";
        } else {
            refreshDistanceTime += "前";
        }
        mDateTv.append(refreshDistanceTime);
    }
}
