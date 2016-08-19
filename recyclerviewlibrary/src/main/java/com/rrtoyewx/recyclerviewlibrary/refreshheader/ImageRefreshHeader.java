package com.rrtoyewx.recyclerviewlibrary.refreshheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rrtoyewx.recyclerviewlibrary.R;
import com.rrtoyewx.recyclerviewlibrary.RrtoyewxRecyclerView;
import com.rrtoyewx.recyclerviewlibrary.utils.ScreenUtil;

/**
 * Created by Rrtoyewx on 16/8/18.
 * ImageRefreshHeader
 */
public class ImageRefreshHeader extends BaseRefreshHeader {
    private static final int DEFAULT_IMAGE_HEIGHT = 100;
    private static final int DEFAULT_REFRESH_HEADER_HEIGHT = 150;
    private static final int DEFAULT_REFRESH_HEADER_MAX_HEIGHT = 200;

    private Context mContext;
    private ImageView mHeaderImage;
    private int mImageHeight;

    public ImageRefreshHeader(Context context) {
        mContext = context;
        initView();
        initDate();
    }

    private void initView() {
        mHeaderImage = new ImageView(mContext);
        mHeaderContainer = mHeaderImage;
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ScreenUtil.dpToPx(mContext, DEFAULT_IMAGE_HEIGHT));
        mHeaderContainer.setLayoutParams(layoutParams);
    }

    private void initDate() {
        mRefreshHeaderHeight = ScreenUtil.dpToPx(mContext, DEFAULT_REFRESH_HEADER_HEIGHT);
        mRefreshHeaderMaxHeight = ScreenUtil.dpToPx(mContext, DEFAULT_REFRESH_HEADER_MAX_HEIGHT);
        mImageHeight = ScreenUtil.dpToPx(mContext, DEFAULT_IMAGE_HEIGHT);

        mHeaderImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mHeaderImage.setImageResource(R.drawable.image_header);
    }

    @Override
    public void changeContainerUI(int distance, int newY, int oldY) {
        ViewGroup.LayoutParams layoutParams = mHeaderContainer.getLayoutParams();
        layoutParams.height = mImageHeight + distance;
        mHeaderContainer.setLayoutParams(layoutParams);
    }

    @Override
    public void changeRefreshStates(int distance) {
        if (mRefreshState == REFRESH_STATE_IDLE) {

            if (distance > 0) {
                mRefreshState = REFRESH_STATE_PULL;
            }

        } else if (mRefreshState == REFRESH_STATE_PULL) {

            if (distance + mImageHeight > mRefreshHeaderHeight) {
                mRefreshState = REFRESH_STATE_RELEASE;
            }

        } else if (mRefreshState == REFRESH_STATE_RELEASE) {

            if (distance + mImageHeight <= mRefreshHeaderHeight) {
                mRefreshState = REFRESH_STATE_PULL;

            }
        }

        if (distance <= 0) {
            resetRefreshHeader();
        }
        Log.e("TAG", mRefreshState + "mRefreshState");
    }

    @Override
    public void upOrCancel(RrtoyewxRecyclerView.RefreshDataListener refreshListener) {
        if (mRefreshState == REFRESH_STATE_RELEASE) {
            mRefreshState = REFRESH_STATE_REFRESHING;

            ViewGroup.LayoutParams layoutParams = mHeaderContainer.getLayoutParams();
            layoutParams.height = mRefreshHeaderHeight;
            mHeaderContainer.setLayoutParams(layoutParams);

            if (refreshListener != null) {
                refreshListener.onRefresh();
            }

        } else {
            resetRefreshHeader();
        }
    }

    private void resetRefreshHeader() {
        mRefreshState = REFRESH_STATE_IDLE;

        ViewGroup.LayoutParams layoutParams = mHeaderContainer.getLayoutParams();
        layoutParams.height = mImageHeight;
        mHeaderContainer.setLayoutParams(layoutParams);
    }

    @Override
    public void completeRefresh() {
        resetRefreshHeader();
    }
}
