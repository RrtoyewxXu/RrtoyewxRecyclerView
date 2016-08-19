package com.rrtoyewx.rrtoyewxrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rrtoyewx.recyclerviewlibrary.viewholder.SimpleViewHolder;

/**
 * Created by Rrtoyewx on 16/8/3.
 */
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context context;
    private int itemCount = 0;

    public ExampleAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ExampleViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        holder.contentText.setText(Character.valueOf((char) (position + 'A')).toString());
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        notifyDataSetChanged();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView contentText;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            contentText = (TextView) itemView.findViewById(R.id.tv_item_example_content);
        }
    }
}
