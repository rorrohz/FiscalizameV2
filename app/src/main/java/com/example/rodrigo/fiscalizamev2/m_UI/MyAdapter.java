package com.example.rodrigo.fiscalizamev2.m_UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rodrigo.fiscalizamev2.R;
import com.example.rodrigo.fiscalizamev2.m_DataObject.Denuncia;
import com.example.rodrigo.fiscalizamev2.m_DetailActivity.DetailActivity;

import java.util.ArrayList;

/**
 * Created by Rodrigo on 27-06-2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyHolder>{

    Context c;
    ArrayList<Denuncia> denuncias;

    public MyAdapter(Context c, ArrayList<Denuncia> denuncias) {
        this.c = c;
        this.denuncias = denuncias;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model,parent,false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final Denuncia d = denuncias.get(position);

        holder.fechaTxt.setText(d.getFecha());
        holder.patenteTxt.setText(d.getPatente());
        holder.empresaTxt.setText(d.getEmpresa());
        PicassoClient.downloadImage(c,d.getImageUrl(),holder.img);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick() {
                openDetailActivity(d.getFecha(),d.getPatente(),d.getRecorrido(),d.getEmpresa(),d.getDescripcion(),d.getImageUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return denuncias.size();
    }

    public void openDetailActivity(String fecha,String patente, String recorrido, String empresa, String descripcion, String imageUrl)
    {
        Intent i = new Intent(c, DetailActivity.class);

        //PACK DATA
        i.putExtra("FECHA_KEY",fecha);
        i.putExtra("PATENTE_KEY",patente);
        i.putExtra("RECORRIDO_KEY",recorrido);
        i.putExtra("EMPRESA_KEY",empresa);
        i.putExtra("DESCRIPCION_KEY",descripcion);
        i.putExtra("URL_KEY",imageUrl);

        c.startActivity(i);

    }

}

