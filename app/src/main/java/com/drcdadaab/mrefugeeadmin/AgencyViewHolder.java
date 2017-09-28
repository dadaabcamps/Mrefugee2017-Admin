package com.drcdadaab.mrefugeeadmin;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class AgencyViewHolder extends RecyclerView.ViewHolder {

    TextView textViewAgencyListName;
    View mView;

    public AgencyViewHolder(View itemView){
        super(itemView);
        this.mView = itemView;
        textViewAgencyListName = (TextView) itemView.findViewById(R.id.textViewAgencyListName);
    }
}
