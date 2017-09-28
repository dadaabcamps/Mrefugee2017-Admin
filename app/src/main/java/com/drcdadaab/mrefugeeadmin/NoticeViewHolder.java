package com.drcdadaab.mrefugeeadmin;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

public class NoticeViewHolder extends RecyclerView.ViewHolder {
    TextView textViewNoticeListTitle, textViewNoticeListVisible;
    RelativeTimeTextView textViewNoticeListSince;
    View mView;

    public NoticeViewHolder(View itemView){
        super(itemView);
        this.mView = itemView;
        textViewNoticeListTitle = (TextView) itemView.findViewById(R.id.textViewAgencyListName);
        textViewNoticeListVisible = (TextView) itemView.findViewById(R.id.textViewNoticeListVisible);
        textViewNoticeListSince = (RelativeTimeTextView) itemView.findViewById(R.id.textViewNoticeListSince);
    }
}
