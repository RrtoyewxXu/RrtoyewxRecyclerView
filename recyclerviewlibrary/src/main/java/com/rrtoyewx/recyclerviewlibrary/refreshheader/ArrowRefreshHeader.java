package com.rrtoyewx.recyclerviewlibrary.refreshheader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rrtoyewx.recyclerviewlibrary.R;

/**
 * Created by Rrtoyewx on 16/8/10.
 */
public class ArrowRefreshHeader implements BaseRefreshHeader {
    private View mHeaderContainer;
    //refresh_arrow
    private ImageView mArrowImage;
    //refresh_loading
    private ProgressBar mLoadingBar;
    //refresh_date
    private TextView mDateTv;
    //refresh_message
    private TextView mHintMessageTv;

    public ArrowRefreshHeader(Context context) {
        mHeaderContainer = LayoutInflater.from(context).inflate(R.layout.default_normal_refresh_header, null, false);
        init();
    }

    private void init() {
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        mHeaderContainer.setLayoutParams(layoutParams);

        mArrowImage = (ImageView) mHeaderContainer.findViewById(R.id.refresh_arrow);
        mLoadingBar = (ProgressBar) mHeaderContainer.findViewById(R.id.refresh_loading);
        mDateTv = (TextView) mHeaderContainer.findViewById(R.id.refresh_date);
        mHintMessageTv = (TextView) mHeaderContainer.findViewById(R.id.refresh_message);
    }


    @Override
    public void moveTo(int distance, int newY, int oldY) {
        Log.e("TAG","moveTO");
//        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) mHeaderContainer.getLayoutParams();
//        layoutParams.height = 1000;
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 0);
        layoutParams.topMargin=100;
        mHeaderContainer.setLayoutParams(layoutParams);
    }

    @Override
    public View getHeaderView() {
        return mHeaderContainer;
    }

    @Override
    public void setHeaderHeight() {

    }

    @Override
    public void setHeaderMaxHeight() {

    }

    @Override
    public int getHeaderHeight() {
        return 0;
    }

    @Override
    public int getHeaderMaxHeight() {
        return 0;
    }

    @Override
    public void setRefreshState(int refreshState) {

    }

    @Override
    public int getRefreshState() {
        return 0;
    }
}
