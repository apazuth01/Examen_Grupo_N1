package com.example.examen_grupo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import com.example.examen_grupo.RestApiMethods;
public class ActivityVerContactos extends AppCompatActivity {

    // Variables Globales
    ListView listemple;
    List<Empleado> empleadoList;
    ArrayList<String> arrayEmple;
  EditText tbuscar, tnumero, tnombre,tlatitud, tlongitud;
  Button bbuscar;
  String idd;
  RequestQueue  requestQueue;
    public static final String EXTRA_MESSAGE="";
    public static final String EXTRA_MESSAGE2="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_contactos);

        listemple = findViewById(R.id.ListEmpleados);
        empleadoList = new ArrayList<>();
        arrayEmple = new ArrayList<String>();
        tbuscar = (EditText) findViewById(R.id.tbuscar);
        bbuscar= (Button)findViewById(R.id.bbuscar);
        tnumero =(EditText) findViewById(R.id.tnumero);
        tnombre =(EditText) findViewById(R.id.tnombre);
        tlatitud =(EditText) findViewById(R.id.tlatitud);
        tlongitud =(EditText) findViewById(R.id.tlongitud);
        ConsumirListaEmpleados();

        listemple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String informacion = "ID: " + empleadoList.get(position).getId() + "\n";
                informacion += "Nombre: " + empleadoList.get(position).getNombre() + "\n";
                informacion += "Telefono: " + empleadoList.get(position).getTelefono();
                informacion += "Latitud: " + empleadoList.get(position).getLatitud();
                informacion += "Longitud: " + empleadoList.get(position).getLongitud();
                String numero =empleadoList.get(position).getTelefono();
                String nombre =empleadoList.get(position).getNombre();
                String latitud =empleadoList.get(position).getLatitud();
                String longitud =empleadoList.get(position).getLongitud();
                tnumero.setText(numero);
                tnombre.setText(nombre);
                tlatitud.setText(latitud);
                tlongitud.setText(longitud);
            }
        });

    }

    private void ConsumirListaEmpleados()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApiMethods.EndPointGetEmple,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject  jsonObject = new JSONObject(response);
                            JSONArray EmpleArray = jsonObject.getJSONArray("empleado");

                            for(int i=0; i< EmpleArray.length(); i++)
                            {
                                JSONObject RowEmple = EmpleArray.getJSONObject(i);
                                Empleado emple = new Empleado(RowEmple.getString("id"),
                                        RowEmple.getString("nombre"),
                                        RowEmple.getString("telefono"),
                                        RowEmple.getString("latitud"),
                                        RowEmple.getString("longitud"));

                                empleadoList.add(emple);
                               // arrayEmple.add(emple.getNombre()+' '+emple.getTelefono());
                                arrayEmple.add(empleadoList.get(i).getId() + "" + Html.fromHtml("<br />")
                                        +empleadoList.get(i).getNombre() + "" + Html.fromHtml("<br />")
                                        +empleadoList.get(i).getTelefono()+ "" + Html.fromHtml("<br />")
                                        +empleadoList.get(i).getLatitud()+ "" + Html.fromHtml("<br />")
                                        +empleadoList.get(i).getLongitud()+ "" + Html.fromHtml("<br />"));
                            }

                            ArrayAdapter adp = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrayEmple);
                            listemple.setAdapter(adp);
                        }
                        catch (JSONException ex)
                        {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        queue.add(stringRequest);
    }




   public void atras(View view)
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    public void ubicar(View view)
    {
        Intent intent = new Intent(this,MapsActivity1.class);
        startActivity(intent);

    }
    public void Editarcontacto(View view)
    {
        Intent intent = new Intent(this,ActivityEditar.class);
        String message = tnumero.getText().toString();
        String message2 = tnumero.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_MESSAGE2, message2);
        startActivity(intent);

    }
    private void BuscarDatos(String Idd)    {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(RestApiMethods.EndPointBuscarContacto, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject= null;
                for (int i=0; i< response.length(); i++){
                    try{
                        jsonObject=response.getJSONObject(i);
                        Empleado emple = new Empleado(jsonObject.getString("id"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("telefono"),
                                jsonObject.getString("latitud"),
                                jsonObject.getString("longitud"));
                        empleadoList.add(emple);
                        arrayEmple.add(empleadoList.get(i).getId() + "" + Html.fromHtml("<br />")
                                +empleadoList.get(i).getNombre() + "" + Html.fromHtml("<br />")
                                +empleadoList.get(i).getTelefono()+ "" + Html.fromHtml("<br />")
                                +empleadoList.get(i).getLatitud()+ "" + Html.fromHtml("<br />")
                                +empleadoList.get(i).getLongitud()+ "" + Html.fromHtml("<br />"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
                public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error 2", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    public void onClickBuscar(View v) {
        BuscarDatos("id=" + tbuscar.getText().toString());
    }
}

