package com.drcdadaab.mrefugeeadmin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    TextView textViewNewsListTitle, textViewNewsListVisible;
    RelativeTimeTextView textViewNewsListSince;
    View mView;

    public NewsViewHolder(View itemView){
        super(itemView);
        this.mView = itemView;
        textViewNewsListTitle = (TextView) itemView.findViewById(R.id.textViewNewsListTitle);
        textViewNewsListVisible = (TextView) itemView.findViewById(R.id.textViewNewsListVisible);
        textViewNewsListSince = (RelativeTimeTextView) itemView.findViewById(R.id.textViewNewsListSince);

    }
}
