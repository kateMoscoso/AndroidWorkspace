package com.example.miw;


import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Consulta extends Activity{
	private TextView  dni, nombre, apellido, direccion, telefono, equipo;
	private View titulo;
	private String datos;
	private int numRegistros;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscar);
		Bundle reicieveParams = getIntent().getExtras();
		datos = reicieveParams.getString("datos");
		numRegistros = reicieveParams.getInt("numRegistros");
		dni = (TextView)findViewById(R.id.dniTextBuscar);
		nombre = (TextView)findViewById(R.id.nombreTextBuscar);
		apellido = (TextView)findViewById(R.id.apellidoTextBuscar);
		direccion = (TextView)findViewById(R.id.direccionTextBuscar);
		telefono = (TextView)findViewById(R.id.telefonoTextBuscar);
		equipo = (TextView)findViewById(R.id.equipoTextBuscar);
		titulo = findViewById(R.string.consultaRegistros);
		Toast.makeText(getBaseContext(), numRegistros, Toast.LENGTH_LONG).show();
		
		try { 
			
			JSONArray jobject = new JSONArray(datos);
			//String valor = jobject.getJSONObject(1).getString("DNI");
			dni.setText(jobject.getJSONObject(1).getString("DNI"));
			nombre.setText(jobject.getJSONObject(1).getString("Nombre"));
			apellido.setText(jobject.getJSONObject(1).getString("Apellidos"));
			direccion.setText(jobject.getJSONObject(1).getString("Direccion"));
			telefono.setText(jobject.getJSONObject(1).getString("Telefono"));
			equipo.setText(jobject.getJSONObject(1).getString("Equipo"));
			
			//Toast.makeText(getBaseContext(), valor, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			Toast.makeText(getBaseContext(),"Error datos" , Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}



	}
}
