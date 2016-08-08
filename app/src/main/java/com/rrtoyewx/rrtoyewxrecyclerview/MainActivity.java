package com.rrtoyewx.rrtoyewxrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrtoyewx.recyclerviewlibrary.RrtoyewxRecyclerView;

public class MainActivity extends AppCompatActivity {
    RrtoyewxRecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    GridLayoutManager gridLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    Adapter adapter;

    int headerCount;
    int footerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adapter = new Adapter(this);

        recyclerView = (RrtoyewxRecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setEmptyView(findViewById(R.id.empty));
    }

    public void addHeadView(View view) {
        View addView = LayoutInflater.from(this).inflate(R.layout.item_head, null);
        TextView tv = (TextView) addView.findViewById(R.id.tv);
        tv.setText("header" + headerCount);
        recyclerView.addHeaderView(addView);
    }

    public void addFooterView(View view) {
        View addView = LayoutInflater.from(this).inflate(R.layout.item_head, null);
        TextView tv = (TextView) addView.findViewById(R.id.tv);
        tv.setText("footer" + footerCount);
        recyclerView.addFooterView(addView);
    }

    public void showEmpty(View view) {
        adapter.setItemCount(0);
    }

    public void addData(View view) {
        adapter.setItemCount(5);
    }

    public void linearlayoutmanager(View view) {
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void gridlayoutmanager(View view) {
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public void staggeredgridlayoutmanager(View view) {
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }


}
