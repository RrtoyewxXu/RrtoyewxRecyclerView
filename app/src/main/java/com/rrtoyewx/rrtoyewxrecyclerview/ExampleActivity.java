package com.rrtoyewx.rrtoyewxrecyclerview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rrtoyewx.recyclerviewlibrary.RrtoyewxRecyclerView;
import com.rrtoyewx.recyclerviewlibrary.refreshheader.BaseRefreshHeader;

import java.util.List;

public class ExampleActivity extends AppCompatActivity {
    private RrtoyewxRecyclerView mRecyclerView;
    private Toolbar mToolbar;

    private int mAdapterCount;
    private Adapter mAdapter;

    private LinearLayoutManager mLinearLayoutManager;
    private GridLayoutManager mGridLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    private int mHeaderCount;
    private List<View> mHeaderViewList;
    private int mFooterCount;
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
        setSupportActionBar(mToolbar);
    }

    private void bindEvent() {
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String msg = "";
                switch (menuItem.getItemId()) {
                    case R.id.action_switch_linear_layout_manager:

                        break;
                    case R.id.action_switch_grid_layout_manager:

                        break;
                    case R.id.action_switch_staggered_grid_layout_manager:

                        break;
                    case R.id.action_add_header_view:
                        break;

                    case R.id.action_remove_header_view:
                        break;

                    case R.id.action_add_footer_view:
                        break;

                    case R.id.action_remove_footer_view:
                        break;

                    case R.id.action_switch_pull_to_refresh_enable:
                        break;

                    case R.id.action_switch_refresh_header:
                        break;

                    case R.id.action_switch_load_more_enable:
                        break;

                    case R.id.action_switch_load_more_view:
                        break;

                }
                msg += menuItem.getTitle();
                if (!msg.equals("")) {
                    Toast.makeText(ExampleActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_example, menu);
        return true;
    }

}
