package com.example.rodrigo.fiscalizamev2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


public class Registrar extends AppCompatActivity implements View.OnClickListener {

    public static final String REGISTER_URL = "https://rodrigomaster01.000webhostapp.com/volleyRegistrar.php";

    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    private EditText Nombre, Username, Correo, Pass;
    private Toast toast;
    private Button volver, registrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        volver = (Button) findViewById(R.id.btnV1);
        registrar = (Button) findViewById(R.id.btnRegistrar);
        Nombre = (EditText) findViewById(R.id.txtUsuario);
        Username = (EditText) findViewById(R.id.txtUsername);
        Correo = (EditText) findViewById(R.id.txtEmail);
        Pass = (EditText) findViewById(R.id.txtPass);

        registrar.setOnClickListener(this);
        volver.setOnClickListener(this);
    }
    private  void registerUser() throws JSONException {
        final String name = Nombre.getText().toString().trim();
        final String username = Username.getText().toString().trim();
        final String password = Pass.getText().toString().trim();
        final String email = Correo.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            Toast.makeText(Registrar.this,message,Toast.LENGTH_LONG).show();
                            Toast.makeText(Registrar.this,code,Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            String message = "Error el usuario ya se encuentra registrado";
                            Toast.makeText(Registrar.this,message,Toast.LENGTH_LONG).show();
                        }

                        //Toast.makeText(Registrar.this,response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Registrar.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_NAME,name);
                params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.btnRegistrar:
                if(TextUtils.isEmpty(Nombre.getText().toString().trim())||TextUtils.isEmpty(Username.getText().toString().trim())|| TextUtils.isEmpty(Correo.getText().toString().trim())|| TextUtils.isEmpty(Pass.getText().toString().trim())){
                    toast = Toast.makeText(Registrar.this,"Campos Vacios Ingrese los campos", Toast.LENGTH_SHORT);
                    toast.show();

                }else {
                    try {
                        registerUser();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ;}
                break;

            case R.id.btnV1:
                Intent volver = new Intent(Registrar.this, MainActivity.class);
                startActivity(volver);
        }
    }

}