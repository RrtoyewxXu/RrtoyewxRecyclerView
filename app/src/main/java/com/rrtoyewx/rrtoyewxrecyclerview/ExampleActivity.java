package com.rrtoyewx.rrtoyewxrecyclerview;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rrtoyewx.recyclerviewlibrary.RrtoyewxRecyclerView;
import com.rrtoyewx.recyclerviewlibrary.refreshheader.ArrowRefreshHeader;
import com.rrtoyewx.recyclerviewlibrary.refreshheader.BaseRefreshHeader;
import com.rrtoyewx.recyclerviewlibrary.refreshheader.ImageRefreshHeader;

import java.util.ArrayList;
import java.util.List;

public class ExampleActivity extends AppCompatActivity {
    private RrtoyewxRecyclerView mRecyclerView;
    private Toolbar mToolbar;

    private int mAdapterCount;
    private ExampleAdapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private List<View> mHeaderViewList;
    private List<View> mFooterViewList;

    private View mEmptyView;

    private BaseRefreshHeader mArrowRefreshHeader;
    private BaseRefreshHeader mImageRefreshHeader;

    private View mLoadMoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        initView();
        initData();
        bindEvent();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RrtoyewxRecyclerView) findViewById(R.id.recycler_view_list);
        mEmptyView = findViewById(R.id.empty_image);
    }

    private void initData() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mGridLayoutManager = new GridLayoutManager(this, 3);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);

        setSupportActionBar(mToolbar);
        mAdapter = new ExampleAdapter(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyView);

        mHeaderViewList = new ArrayList<>();
        mFooterViewList = new ArrayList<>();

        mArrowRefreshHeader = new ArrowRefreshHeader(this);
        mImageRefreshHeader = new ImageRefreshHeader(this);
    }

    private void bindEvent() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.action_switch_linear_layout_manager:
                        mRecyclerView.setLayoutManager(mLinearLayoutManager);
                        break;

                    case R.id.action_switch_grid_layout_manager:
                        mRecyclerView.setLayoutManager(mGridLayoutManager);
                        break;

                    case R.id.action_switch_staggered_grid_layout_manager:
                        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
                        break;

                    case R.id.action_add_adapter_data:
                        mAdapterCount += 5;
                        mAdapter.setItemCount(mAdapterCount);
                        break;

                    case R.id.action_clear_adapter_data:
                        mAdapterCount -= 5;
                        if (mAdapterCount <= 0) {
                            mAdapterCount = 0;
                        }
                        mAdapter.setItemCount(mAdapterCount);
                        break;

                    case R.id.action_add_header_view:
                        View addHeaderView = LayoutInflater.from(ExampleActivity.this).inflate(R.layout.item_head, null);
                        TextView headerContent = (TextView) addHeaderView.findViewById(R.id.tv_item_example_header);

                        headerContent.setText("这是HeaderView,序号为:" + mHeaderViewList.size());

                        mHeaderViewList.add(addHeaderView);
                        mRecyclerView.addHeaderView(addHeaderView);
                        break;

                    case R.id.action_remove_header_view:
                        if (mHeaderViewList.size() > 0) {
                            View removeHeaderView = mHeaderViewList.get(mHeaderViewList.size() - 1);
                            mRecyclerView.removeHeaderView(removeHeaderView);
                            mHeaderViewList.remove(removeHeaderView);
                        }
                        break;

                    case R.id.action_add_footer_view:
                        View addFooterView = LayoutInflater.from(ExampleActivity.this).inflate(R.layout.item_footer, null);
                        TextView footerContent = (TextView) addFooterView.findViewById(R.id.tv_item_example_footer);

                        footerContent.setText("这是FooterView,序号为:" + mFooterViewList.size());

                        mFooterViewList.add(addFooterView);
                        mRecyclerView.addFooterView(addFooterView);
                        break;

                    case R.id.action_remove_footer_view:
                        if (mFooterViewList.size() > 0) {
                            View removeFooterView = mFooterViewList.get(mFooterViewList.size() - 1);
                            mRecyclerView.removeFooterView(removeFooterView);
                            mFooterViewList.remove(removeFooterView);
                        }
                        break;

                    case R.id.action_switch_pull_to_refresh_enable:
                        mRecyclerView.setPullToRefreshEnable(!mRecyclerView.checkPullToRefreshEnable());
                        break;

                    case R.id.action_switch_refresh_header:

                        BaseRefreshHeader pullToRefreshHeader = mRecyclerView.getPullToRefreshHeader();
                        if (pullToRefreshHeader instanceof ArrowRefreshHeader) {
                            mRecyclerView.setPullToRefreshHeader(mImageRefreshHeader);
                        } else {
                            mRecyclerView.setPullToRefreshHeader(mArrowRefreshHeader);
                        }
                        break;

                    case R.id.action_switch_load_more_enable:
                        mRecyclerView.setLoadMoreEnable(!mRecyclerView.checkLoadMoreEnable());
                        break;

                    case R.id.action_switch_load_more_view:
                        View customerLoadMoreView = LayoutInflater.from(ExampleActivity.this).inflate(R.layout.item_customer_load_more, null);
                        mRecyclerView.setLoadMoreView(customerLoadMoreView);
                        break;

                }
                msg += menuItem.getTitle();
                if (!msg.equals("")) {
                    Toast.makeText(ExampleActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        mRecyclerView.addRefreshListener(new RrtoyewxRecyclerView.RefreshDataListener() {
            @Override
            public void onRefresh() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.completeRefresh();
                        Toast.makeText(ExampleActivity.this, "下来刷新加载完成", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.completeLoadMore();
                        Toast.makeText(ExampleActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_example, menu);
        return true;
    }

}
