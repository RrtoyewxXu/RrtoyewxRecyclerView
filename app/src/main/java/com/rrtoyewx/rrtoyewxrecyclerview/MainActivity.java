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
    private View addView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RrtoyewxRecyclerView) findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 3);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(new Adapter(this));
    }

    public void addHeadView(View view) {
        View addView = LayoutInflater.from(this).inflate(R.layout.item_head, null);
        TextView tv = (TextView) addView.findViewById(R.id.tv);
        tv.setText(System.currentTimeMillis() + "date" + "header");
        recyclerView.addHeaderView(addView);
    }

    public void addFooterView(View view) {
        View addView = LayoutInflater.from(this).inflate(R.layout.item_head, null);
        TextView tv = (TextView) addView.findViewById(R.id.tv);
        tv.setText(System.currentTimeMillis() + "date" + "footer");
        recyclerView.addFooterView(addView);
    }
}
