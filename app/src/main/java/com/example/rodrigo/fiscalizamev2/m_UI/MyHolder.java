package com.example.rodrigo.fiscalizamev2.m_UI;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodrigo.fiscalizamev2.R;


/**
 * Created by Rodrigo on 27-06-2018.
 */

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView fechaTxt,patenteTxt,empresaTxt;
    ImageView img;
    ItemClickListener itemClickListener;


    public MyHolder(View itemView) {
        super(itemView);

        fechaTxt = (TextView) itemView.findViewById(R.id.fechaTxt);
        patenteTxt=(TextView) itemView.findViewById(R.id.patenteTxt);
        empresaTxt=(TextView) itemView.findViewById(R.id.empresaTxt);
        img = (ImageView) itemView.findViewById(R.id.denunciaImagen);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClick();
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
}

