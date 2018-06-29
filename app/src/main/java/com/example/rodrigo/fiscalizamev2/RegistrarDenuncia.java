package com.example.rodrigo.fiscalizamev2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrarDenuncia.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarDenuncia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarDenuncia extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Spinner s1,s2;
    String recorridos[]=null;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String CARPETA_PRINCIPAL="misImagenesApp/";
    private static final String CARPETA_IMAGEN="imagenes";
    private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL+CARPETA_IMAGEN;
    private String path;
    File fileImagen;
    Bitmap bitmap;

    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;

    EditText campoFecha,campoPatente,campoRecorrido,campoEmpresa,campoDescripcion;
    Button btnRegistro,btnFoto;
    ImageView imgFoto;
    ProgressDialog progreso;
    Button btnFecha;
    EditText txtFecha;
    private int dia,mes,ano;

    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    StringRequest stringRequest;

    public RegistrarDenuncia() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarDenuncia.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrarDenuncia newInstance(String param1, String param2) {
        RegistrarDenuncia fragment = new RegistrarDenuncia();
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

        View vista=inflater.inflate(R.layout.fragment_registrar_denuncia,container,false);

        btnFecha=(Button)vista.findViewById(R.id.btnFecha);
        txtFecha=(EditText) vista.findViewById(R.id.txtFecha);
        btnFecha.setOnClickListener(this);

        s1=(Spinner)vista.findViewById(R.id.spinnerEmpresas);
        s2=(Spinner)vista.findViewById(R.id.spinnerRecorridos);

        s1.setOnItemSelectedListener(this);

        campoPatente= (EditText) vista.findViewById(R.id.txtPatente);
        //campoRecorrido= (EditText) vista.findViewById(R.id.txtRecorrido);
        //campoEmpresa= (EditText) vista.findViewById(R.id.txtEmpresa);
        campoDescripcion= (EditText) vista.findViewById(R.id.txtDescripcion);
        btnRegistro= (Button) vista.findViewById(R.id.btnRegistrar);
        btnFoto=(Button) vista.findViewById(R.id.btnFoto);

        if(validaPermisos())
        {
            btnFoto.setEnabled(true);
        }
        else
        {
            btnFoto.setEnabled(false);
        }


        imgFoto=(ImageView) vista.findViewById(R.id.imgFoto);

        request= Volley.newRequestQueue(getContext());

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogOpciones();
            }
        });

        return vista;
    }

    private boolean validaPermisos() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((getContext().checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&(getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if((shouldShowRequestPermissionRationale(CAMERA))||(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)))
        {
            cargarDialogoRecomendacion();
        }
        else
        {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                btnFoto.setEnabled(true);
            }
            else
            {
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"Sí","No"};
        final AlertDialog.Builder alertOpciones= new AlertDialog.Builder(getContext());
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(opciones[i].equals("Sí"))
                {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package", getContext().getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        dialogo.setTitle("Permisos Desactivados");
        dialogo.setMessage("Debe acepar los permisos parael correcto funcionamiento de la app");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA},100);
            }
        });
        dialogo.show();

    }

    private void mostrarDialogOpciones() {
        final CharSequence[] opciones={"Tomar foto","Elegir de Galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(opciones[i].equals("Tomar foto"))
                {
                    abrirCamara();
                }
                else if(opciones[i].equals("Elegir de Galeria"))
                {
                    Intent intent=new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent,"Seleccione"),COD_SELECCIONA);
                }
                else
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada=miFile.exists();

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }
        if(isCreada==true){
            Toast.makeText(getContext(),"Convirtiendo...",Toast.LENGTH_SHORT).show();
            Long consecutivo=System.currentTimeMillis()/1000;
            String nombre=consecutivo.toString()+".jpg";

            path= Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+File.separator+nombre;

            fileImagen=new File(path);

            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));

            startActivityForResult(intent,COD_FOTO);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath=data.getData();
                imgFoto.setImageURI(miPath);

                try {
                    bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),miPath);
                    imgFoto.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;
            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(),new String[]{path},null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i("Path",""+path);
                    }
                });
                bitmap= BitmapFactory.decodeFile(path);
                imgFoto.setImageBitmap(bitmap);

                break;
        }
        bitmap=redimensionarImagen(bitmap,800,600);
    }

    private Bitmap redimensionarImagen(Bitmap bitmap, float anchoNuevo, float altoNuevo) {
        int ancho=bitmap.getWidth();
        int alto=bitmap.getHeight();
        if(ancho>anchoNuevo||alto>altoNuevo)
        {
            float escalaAncho=anchoNuevo/ancho;
            float escalaAlto=altoNuevo/alto;

            Matrix matrix= new Matrix();
            matrix.postScale(escalaAncho,escalaAlto);
            return Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);
        }
        else
        {
            return bitmap;
        }


    }

    private void cargarWebService() {

        progreso=new ProgressDialog(getContext());
        progreso.setMessage("Cargando...");
        progreso.show();

        String url="https://rodrigomaster01.000webhostapp.com/volleyDenunciaMovil.php?";

        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.hide();

                if(response.trim().equalsIgnoreCase("registra")){
                    txtFecha.setText("");
                    campoPatente.setText("");
                    //campoRecorrido.setText("");
                    //campoEmpresa.setText("");
                    s1.setSelection(0);
                    campoDescripcion.setText("");
                    Toast.makeText(getContext(),"Se ha registrado con exito",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(),"No se ha podido registrar",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"No se ha podido conectar",Toast.LENGTH_SHORT).show();
                progreso.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String fecha=txtFecha.getText().toString();
                String patente=campoPatente.getText().toString();
                String empresa=s1.getSelectedItem().toString().trim();
                String recorrido=s2.getSelectedItem().toString().trim();
                String descripcion=campoDescripcion.getText().toString();

                String imagen=convertImgString(bitmap);

                Map<String,String> parametros = new HashMap<>();
                parametros.put("fecha",fecha);
                parametros.put("patente",patente);
                parametros.put("empresa",empresa);
                parametros.put("recorrido",recorrido);
                parametros.put("descripcion",descripcion);
                parametros.put("imagen",imagen);

                return parametros;
            }
        };
        VolleySingleton.getIntanciaVolley(getContext()).addToRequestQueue(stringRequest);



    }

    private String convertImgString(Bitmap bitmap) {
        ByteArrayOutputStream array= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,array);
        byte[] imagenByte=array.toByteArray();
        String imagenString= Base64.encodeToString(imagenByte,Base64.DEFAULT);

        return imagenString;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onClick(View v) {
        if(v==btnFecha)
        {
            final Calendar c = Calendar.getInstance();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            ano=c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    txtFecha.setText(year+"-"+month+"-"+dayOfMonth);
                }
            }
                    ,ano,mes,dia);
            datePickerDialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0)
        {
            recorridos=new String[]{"Seleccione un recorrido"};
        }
        if(position==1)
        {
            recorridos=new String[]{"101","101c","102","103","104","104n",
                    "105","106","107","107c","107cy","107n","108","109",
                    "109n","110","110c","111","112n","113","114","115",
                    "116","116e","117","117c","118","119","119n","120",
                    "121","125","126","130","408","408e","410","410e"};
        }
        if(position==2)
        {
            recorridos=new String[]{"401","401n","402","403","403n","404",
                    "404c","405","405c","405n","405y","406","406c","407",
                    "409","411","412","413c","413v","414e","415e","416e",
                    "417e","418","418n","419","419y","420e","421","421y",
                    "422","423","425","426","426n","427","427n","428","428c",
                    "428e","429","429c","430","430y","431c","431v","432n",
                    "435","449n","D01","D02","D03","D05","D07","D07c","D08",
                    "D08c","D09","D10","D11","D12","D14","D15","D16","D17","D18","D20"};
        }
        if(position==3)
        {
            recorridos=new String[]{"301","301c","301c2","301e","302","302e",
                    "302n","303","303e","307","307e","308","312e","313e","314",
                    "314e","315e","316e","321","322","323","325","329","345",
                    "346n","348","350","350c","385","D13","E01","E02","E03",
                    "E04","E05","E06","E07","E08","E09","E10","E11","E12","E13",
                    "E14","E15C","E16","E17","E18","H02","H03","H04","H05","H05c",
                    "H06","H07","H08","H09","H12","H13","H14","I01","I02","I03",
                    "I03c","I04","I04c","I04e","I05","I07","I08","I08c","I08n",
                    "I09","I09c","I09e","I10","I10n","I11","I12","I13","I14","I14n",
                    "I16","I17","I18","I20","I21","I22","I24"};

        }
        if(position==4)
        {
            recorridos=new String[]{"424","501","502","502c","503","504","505","506",
                    "506e","506v","507","507c","508","509","510","510c","511","513",
                    "513v","514","514c","515n","516","517","518","519e","546e","J01",
                    "J01c","J02","J03","J04","J05","J06","J07","J08","J10","J11","J12",
                    "J13","J13c","J14c","J15c","J16","J17","J18","J18c","J19","J20"};

        }
        if(position==5)
        {
            recorridos=new String[]{"B01","B02","B02n","B03","B04","B04v","B05","B06",
                    "B07","B08","B09","B10","B11","B12","B13","B14","B15","B16","B17",
                    "B18","B18e","B19","B20","B21","B22","B23","B24","B25","B26","B27",
                    "B28","B29","B30n","B31n","C01","C01c","C02","C02c","C03","C04","C05",
                    "C06","C07","C08","C09","C10e","C11","C12","C13","C14","C15","C16","C17",
                    "C19","C20","C21n","C22","C23"};

        }
        if(position==6)
        {
            recorridos=new String[]{"213e","712","F01","F01c","F02","F03","F03c","F05","F06",
                    "F07","F08","F09","F10","F11","F12","F12c","F13","F13c","F14","F15","F16",
                    "F18","F19","F20","F23","F24","F25","F25e","F26","F27","F28n","F29","F30n"};
        }
        if(position==7)
        {
            recorridos=new String[]{"201","201e","202c","203","203e","204","204e","204n",
                    "205","205e","206","206c","207e","208","208c","209","209e","210","210v",
                    "211","211c","212","214","216","217e","219e","221e","223","224","224c",
                    "225","226","227","228","229","230","262n","264n","G01","G01c","G02",
                    "G04","G05","G07","G08","G08v","G09","G11","G12","G13","G14","G15",
                    "G16","G18","G22","G23"};
        }
        ArrayAdapter<String> adt = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,recorridos);
        s2.setAdapter(adt);



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
