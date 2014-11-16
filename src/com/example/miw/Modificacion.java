package com.example.miw;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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

public class Modificacion extends Activity {
	private TextView   dni, nombre, apellido, direccion, telefono, equipo;
	private String dniModificar, datos, url;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modificar);
		Bundle reicieveParams = getIntent().getExtras();
		dniModificar = reicieveParams.getString("dniModificar");
		datos =reicieveParams.getString("datos");
		url =reicieveParams.getString("url");
		dni = (TextView)findViewById(R.id.dniTextModificar);
		nombre = (TextView)findViewById(R.id.nombreTextModificar);
		apellido = (TextView)findViewById(R.id.apellidoTextModificar);
		direccion = (TextView)findViewById(R.id.direccionTextModificar);
		telefono = (TextView)findViewById(R.id.telefonoTextModificar);
		equipo = (TextView)findViewById(R.id.equipoTextModificar);

		dni.setText(dniModificar);
		try { 
			JSONArray jobject = new JSONArray(datos);
			nombre.setText(jobject.getJSONObject(1).getString("Nombre"));
			apellido.setText(jobject.getJSONObject(1).getString("Apellidos"));
			direccion.setText(jobject.getJSONObject(1).getString("Direccion"));
			telefono.setText(jobject.getJSONObject(1).getString("Telefono"));
			equipo.setText(jobject.getJSONObject(1).getString("Equipo"));
		}
		catch (JSONException e) {
			Toast.makeText(getBaseContext(),"Error datos" , Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}


	}
	public void modificarRegistro (View view){

			String mensaje ="Modificacion realizada";
			new ModificarBD().execute();
			Intent i = new Intent();
			i.putExtra("mensaje", mensaje);
		    setResult(RESULT_OK, i);
			finish();
		
	}
	private class ModificarBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;
		private final String URL = url;
		JSONObject dato = new JSONObject();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Modificacion.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... dnis) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final = URL;
			try {


				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpPut httpput = new HttpPut(url_final);


				dato.put("DNI", dni.getText().toString());
				dato.put("Nombre", nombre.getText().toString());
				dato.put("Apellidos", apellido.getText().toString());
				dato.put("Direccion", direccion.getText().toString());

				dato.put("Telefono", telefono.getText().toString());
				dato.put("Equipo", equipo.getText().toString());

				httpput.setEntity( new StringEntity(dato.toString(), HTTP.UTF_8));
				httpput.setHeader("Accept", "application/json");
				httpput.setHeader("Content-type", "application/json");
				HttpResponse response = httpclient.execute(httpput);
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
			try {
				JSONArray arrayJSON = new JSONArray(respuesta);
				int numRegistros = arrayJSON.getJSONObject(0).getInt("NUMREG");

				if(numRegistros ==-1){
					Intent intent = new Intent();
					intent.putExtra("mensaje", "Error en la modificación");
					setResult(RESULT_CANCELED, intent);
					finish();
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	public void onBackPressed() {
		String mensaje ="Modificacion cancelada";
		Intent intent = new Intent();
		intent.putExtra("mensaje", mensaje);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}
}
