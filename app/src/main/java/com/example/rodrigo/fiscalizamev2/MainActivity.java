package com.example.rodrigo.fiscalizamev2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOGIN_URL = "https://rodrigomaster01.000webhostapp.com/volleyLogin.php";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_ID = "id";

    private EditText txt,txt2;
    private Button btnLogin;
    private Toast toast;

    private String username;
    private String password;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = (EditText) findViewById(R.id.txtUsuario);
        txt2 = (EditText) findViewById(R.id.txtPass);

        Button registro = (Button) findViewById(R.id.btnRegistro);
        Button Loguear = (Button) findViewById(R.id.btnLog);
        Loguear.setOnClickListener(this);
        registro.setOnClickListener(this);
    }

    private void userLogin(){
        username = txt.getText().toString().trim();
        password = txt2.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,LOGIN_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        if(response.trim().equals("error")){
                            Toast.makeText(MainActivity.this,"Error: Ingrese datos validos",Toast.LENGTH_LONG).show();
                        }else{
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                id = jsonObject.getString("id");
                                //Toast.makeText(MainActivity.this,id,Toast.LENGTH_LONG).show();
                                openMenu();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this,"Error: Ingrese datos validos",Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> map = new HashMap<>();
                map.put(KEY_USERNAME,username);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void openMenu(){
        Intent intent = new Intent(this,Principal.class);
        intent.putExtra(KEY_USERNAME,username);
        intent.putExtra(KEY_ID,id);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLog:
                if(TextUtils.isEmpty(txt.getText().toString().trim())||TextUtils.isEmpty(txt2.getText().toString().trim()))
                {
                    toast = Toast.makeText(MainActivity.this,"Campos Vacios Ingrese los campos", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else
                {
                    userLogin();
                }
                break;
            case R.id.btnRegistro:
                Intent registrar = new Intent(MainActivity.this,Registrar.class);
                startActivity(registrar);

        }
    }



}