package com.example.rodrigo.fiscalizamev2.m_MySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.rodrigo.fiscalizamev2.m_DataObject.Denuncia;
import com.example.rodrigo.fiscalizamev2.m_UI.MyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rodrigo on 27-06-2018.
 */

public class DataParser extends AsyncTask<Void,Void,Boolean> {

    Context c;
    String jsonData;
    RecyclerView rv;

    ProgressDialog pd;
    ArrayList<Denuncia> denuncias = new ArrayList<>();

    public DataParser(Context c, String jsonData, RecyclerView rv) {
        this.c = c;
        this.jsonData = jsonData;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd=new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parseando... Espere por favor");
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Boolean parsed) {
        super.onPostExecute(parsed);

        pd.dismiss();

        if(parsed)
        {
            //BIND
            MyAdapter adapter = new MyAdapter(c,denuncias);
            rv.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(c,"Incapaz de parsear",Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean parseData()
    {
        try {
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo;

            denuncias.clear();
            Denuncia denuncia;

            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                int id=jo.getInt("id");
                String fecha=jo.getString("fecha");
                String patente=jo.getString("patente");
                String recorrido=jo.getString("recorrido");
                String empresa=jo.getString("empresa");
                String descripcion=jo.getString("descripcion");
                String imageUrl=jo.getString("imageurl");

                denuncia = new Denuncia();

                denuncia.setId(id);
                denuncia.setFecha(fecha);
                denuncia.setPatente(patente);
                denuncia.setEmpresa(empresa);
                denuncia.setRecorrido(recorrido);
                denuncia.setDescripcion(descripcion);
                denuncia.setImageUrl(imageUrl);


                denuncias.add(denuncia);

            }
            return true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


}