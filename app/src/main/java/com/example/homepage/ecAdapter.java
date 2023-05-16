package com.example.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ecAdapter extends RecyclerView.Adapter<ecViewHolder>{
    Context c;
    List<String> mdata;

    public ecAdapter(Context c, List<String> mdata){
        this.c = c;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public ecViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.list_item, viewGroup, false);
        return new ecViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ecViewHolder holder, int i) {
        holder.txt1.setText(mdata.get(i));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}
