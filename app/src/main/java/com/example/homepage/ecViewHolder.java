package com.example.homepage;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ecViewHolder extends RecyclerView.ViewHolder {

    TextView txt1;

    public ecViewHolder(@NonNull View itemView) {
        super(itemView);

        txt1=itemView.findViewById(R.id.txt1);
    }
}
