package com.example.rodrigo.fiscalizamev2.m_MySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by Rodrigo on 27-06-2018.
 */

public class Downloader extends AsyncTask<Void,Void,String> {

    Context c;
    String urlAdress;
    RecyclerView rv;

    ProgressDialog pd;

    public Downloader() {
    }

    public Downloader(Context c, String urlAdress, RecyclerView rv) {
        this.c = c;
        this.urlAdress = urlAdress;
        this.rv = rv;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Retrieve");
        pd.setMessage("Retrievin");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        return this.downloadData();
    }

    @Override
    protected void onPostExecute(String jsonData) {
        super.onPostExecute(jsonData);

        pd.dismiss();

        if(jsonData==null){
            Toast.makeText(c,"Error: No se puedieron recuperar datos",Toast.LENGTH_SHORT).show();
        }
        else{
            //PARSE
            new DataParser(c,jsonData,rv).execute();
        }

    }

    private String downloadData(){
        HttpURLConnection con = Connector.connect(urlAdress);
        if(con==null){
            return null;
        }
        try
        {
            InputStream is = new BufferedInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line;
            StringBuffer jsonData = new StringBuffer();

            while ((line=br.readLine())!=null)
            {
                jsonData.append(line+"\n");
            }
            br.close();
            is.close();

            return jsonData.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}

