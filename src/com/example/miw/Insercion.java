package com.example.miw;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Insercion extends Activity {
	private TextView   dni, nombre, apellido, direccion, telefono, equipo;
	private String dniInsertar;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insertar);
		Bundle reicieveParams = getIntent().getExtras();
		dniInsertar = reicieveParams.getString("dniInsertar");

		dni = (TextView)findViewById(R.id.dniTextInsertar);
		nombre = (TextView)findViewById(R.id.nombreTextInsertar);
		apellido = (TextView)findViewById(R.id.apellidoTextInsertar);
		direccion = (TextView)findViewById(R.id.direccionTextInsertar);
		telefono = (TextView)findViewById(R.id.telefonoTextInsertar);
		equipo = (TextView)findViewById(R.id.equipoTextInsertar);

		dni.setText(dniInsertar);


	}
	public void insertarRegistro (View view){
		if (nombre.getText().toString().equals("") || apellido.getText().toString().equals("") ||
				direccion.getText().toString().equals("") || telefono.getText().toString().equals("") ||
				equipo.getText().toString().equals("") ){
			Toast.makeText(getBaseContext(),"Hay alguno campos vacios" , Toast.LENGTH_LONG).show();
		}
		else{
			String mensaje ="Insercion realizada";
			new InsertarBD().execute();
			Intent i = new Intent();
			i.putExtra("mensaje", mensaje);
		    setResult(RESULT_OK, i);
			finish();
		}
	}
	private class InsertarBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;
		private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw27/fichas";
		JSONObject dato = new JSONObject();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Insercion.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... dnis) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final = URL;
			//Toast.makeText(getBaseContext(), "before", Toast.LENGTH_LONG).show();
			try {


				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpPost httppost = new HttpPost(url_final);


				dato.put("DNI", dni.getText().toString());
				dato.put("Nombre", nombre.getText().toString());
				dato.put("Apellidos", apellido.getText().toString());
				dato.put("Direccion", direccion.getText().toString());

				dato.put("Telefono", telefono.getText().toString());
				dato.put("Equipo", equipo.getText().toString());

				httppost.setEntity( new StringEntity(dato.toString(), HTTP.UTF_8));
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type", "application/json");
				HttpResponse response = httpclient.execute(httppost);
				respuesta = EntityUtils.toString(response.getEntity());  
				httpclient.close();
			} catch (Exception e)  {
				Log.e(getString(R.string.app_name),e.toString());
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(String respuesta) {
			pDialog.dismiss();

		}

	}
	public void onBackPressed() {
		String mensaje ="Insercion cancelada";
		Intent intent = new Intent();
		intent.putExtra("mensaje", mensaje);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}
	  

}
