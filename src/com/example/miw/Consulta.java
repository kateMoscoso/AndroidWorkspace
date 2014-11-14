package com.example.miw;


import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class Consulta extends Activity{
	private TextView  titulo, dni, nombre, apellido, direccion, telefono, equipo;
	private String datos;
	private int numRegistros = 0;
	private int registroActual = 1;
	private JSONArray jobject;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buscar);
		Bundle reicieveParams = getIntent().getExtras();
		datos = reicieveParams.getString("datos");
		numRegistros = reicieveParams.getInt("numRegistros");
		mostrarRegistro(1);

	}
	public void mostrarPrimero(View view){
		if( numRegistros>1){
			mostrarRegistro(1);
			registroActual=1;
		}
	}

	public void mostrarAnterior(View view){
		if(registroActual>1 &&numRegistros>1){
			registroActual--;
			mostrarRegistro(registroActual);
		}
	}
	public void mostrarSiguiente(View view){
		if(registroActual<numRegistros  &&numRegistros>1){
			registroActual++;
			mostrarRegistro(registroActual);
		}
	}
	public void mostrarUltimo(View view){
		if( numRegistros>1){
			registroActual=numRegistros;
			mostrarRegistro(numRegistros);
		}
	}
	public void mostrarRegistro(int registro){
		dni = (TextView)findViewById(R.id.dniTextBuscar);
		nombre = (TextView)findViewById(R.id.nombreTextBuscar);
		apellido = (TextView)findViewById(R.id.apellidoTextBuscar);
		direccion = (TextView)findViewById(R.id.direccionTextBuscar);
		telefono = (TextView)findViewById(R.id.telefonoTextBuscar);
		equipo = (TextView)findViewById(R.id.equipoTextBuscar);
		titulo = (TextView)findViewById(R.id.consulta);
		try { 
			JSONArray jobject = new JSONArray(datos);
			dni.setText(jobject.getJSONObject(registro).getString("DNI"));
			nombre.setText(jobject.getJSONObject(registro).getString("Nombre"));
			apellido.setText(jobject.getJSONObject(registro).getString("Apellidos"));
			direccion.setText(jobject.getJSONObject(registro).getString("Direccion"));
			telefono.setText(jobject.getJSONObject(registro).getString("Telefono"));
			equipo.setText(jobject.getJSONObject(registro).getString("Equipo"));
			titulo.setText("registro "+ registro +" de "+ numRegistros);
		}
		catch (JSONException e) {
			Toast.makeText(getBaseContext(),"Error datos" , Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {
		String mensaje ="Consulta finalizada";
		Intent intent = new Intent();
	    intent.putExtra("mensaje", mensaje);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

}
