package com.example.rodrigo.fiscalizamev2;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rodrigo.fiscalizamev2.m_DataObject.Denuncia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultarDenuncia.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConsultarDenuncia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConsultarDenuncia extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText txtId;
    TextView txtPatente,txtRecorrido,txtEmpresa,txtDescripcion;
    Button btnConsultarDenuncia;
    ProgressDialog progreso;
    ImageView imagenId;


    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;


    public ConsultarDenuncia() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsultarDenuncia.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsultarDenuncia newInstance(String param1, String param2) {
        ConsultarDenuncia fragment = new ConsultarDenuncia();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_consultar_denuncia,container,false);

        txtId=(EditText) vista.findViewById(R.id.txtId);
        txtPatente = (TextView) vista.findViewById(R.id.txtPatente);
        txtRecorrido= (TextView) vista.findViewById(R.id.txtRecorrido);
        txtEmpresa= (TextView) vista.findViewById(R.id.txtEmpresa);
        txtDescripcion= (TextView) vista.findViewById(R.id.txtDescripcion);
        btnConsultarDenuncia = (Button) vista.findViewById(R.id.btnConsultarDenuncia);
        imagenId=(ImageView) vista.findViewById(R.id.imagenId);

        request= Volley.newRequestQueue(getContext());

        btnConsultarDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });


        return vista;
    }

    private void cargarWebService() {
        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Consultando...");
        progreso.show();

        String url="https://rodrigomaster01.000webhostapp.com/ConsultarDenunciaImagen.php?id="+txtId.getText().toString();

        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progreso.hide();
        Toast.makeText(getContext(),"No se pudo consultar la denuncia "+error.toString(),Toast.LENGTH_SHORT).show();
        Log.i("ERROR",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        progreso.hide();

        //Toast.makeText(getContext(),"Mensaje: "+response,Toast.LENGTH_SHORT).show();

        Denuncia miDenuncia = new Denuncia();

        JSONArray json= response.optJSONArray("Denuncia");
        JSONObject jsonObject=null;

        try {
            jsonObject=json.getJSONObject(0);
            miDenuncia.setPatente(jsonObject.optString("patente"));
            miDenuncia.setRecorrido(jsonObject.optString("recorrido"));
            miDenuncia.setEmpresa(jsonObject.optString("empresa"));
            miDenuncia.setDescripcion(jsonObject.optString("descripcion"));
            //miDenuncia.setImageUrl(jsonObject.optString("imagen"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        txtPatente.setText("Patente: "+miDenuncia.getPatente());
        txtRecorrido.setText("Recorrido: "+miDenuncia.getRecorrido());
        txtEmpresa.setText("Empresa: "+miDenuncia.getEmpresa());
        txtDescripcion.setText("Descripcion: "+miDenuncia.getDescripcion());

        //if(miDenuncia.getImageUrl()!=null)
        //{
        //imagenId.setImageBitmap(miDenuncia.getImageUrl());
        //}
        //else
        //{
        //    imagenId.setImageResource(R.drawable.img_base);
        //}


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
