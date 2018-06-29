package com.example.rodrigo.fiscalizamev2.m_DetailActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rodrigo.fiscalizamev2.R;
import com.example.rodrigo.fiscalizamev2.m_UI.PicassoClient;

/**
 * Created by Rodrigo on 27-06-2018.
 */

public class DetailActivity extends AppCompatActivity {

    TextView fechaTxt,patenteTxt,recorridoTxt, empresaTxt, descripcionTxt;
    ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_todas_denuncias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fechaTxt=(TextView)findViewById(R.id.fechaTxtDetail);
        patenteTxt= (TextView) findViewById(R.id.patenteTxtDetail);
        recorridoTxt= (TextView) findViewById(R.id.recorridoTxtDetail);
        empresaTxt= (TextView) findViewById(R.id.empresaTxtDetail);
        descripcionTxt= (TextView) findViewById(R.id.descripcionTxtDetail);
        img= (ImageView) findViewById(R.id.denunciaImagenDetail);

        //RECEIVE
        Intent i = this.getIntent();
        String fecha=i.getExtras().getString("FECHA_KEY");
        String patente=i.getExtras().getString("PATENTE_KEY");
        String recorrido=i.getExtras().getString("RECORRIDO_KEY");
        String empresa=i.getExtras().getString("EMPRESA_KEY");
        String descripcion=i.getExtras().getString("DESCRIPCION_KEY");
        String imageurl=i.getExtras().getString("URL_KEY");

        //BIND
        fechaTxt.setText(fecha);
        patenteTxt.setText(patente);
        recorridoTxt.setText(recorrido);
        empresaTxt.setText(empresa);
        descripcionTxt.setText(descripcion);
        PicassoClient.downloadImage(this,imageurl,img);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
