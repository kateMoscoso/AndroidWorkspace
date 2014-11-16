package com.example.miw;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
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



public class Borrado  extends Activity{
	private TextView  dni, nombre, apellido, direccion, telefono, equipo;
	private String dniEliminar;
	private String datos, url;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrar);
		Bundle reicieveParams = getIntent().getExtras();
		dniEliminar = reicieveParams.getString("dniEliminar");
		datos = reicieveParams.getString("datos");
		url = reicieveParams.getString("url");
		dni = (TextView)findViewById(R.id.dniTextBorrar);
		nombre = (TextView)findViewById(R.id.nombreTextBorrar);
		apellido = (TextView)findViewById(R.id.apellidoTextBorrar);
		direccion = (TextView)findViewById(R.id.direccionTextBorrar);
		telefono = (TextView)findViewById(R.id.telefonoTextBorrar);
		equipo = (TextView)findViewById(R.id.equipoTextBorrar);
		dni.setText(dniEliminar);
		try { 
			JSONArray jobject = new JSONArray(datos);
			dni.setText(jobject.getJSONObject(1).getString("DNI"));
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
	public void borrarRegistro (View view){

		String mensaje ="Borrado realizado";
		new BorrarBD().execute();
		Intent i = new Intent();
		i.putExtra("mensaje", mensaje);
		setResult(RESULT_OK, i);
		super.onBackPressed();

	}
	private class BorrarBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Borrado.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... dnis) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final = url + "/"+dniEliminar;

			try {


				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpDelete httpDelete = new HttpDelete(url_final);
				httpDelete.setHeader("content-type", "application/json");
				HttpResponse response = httpclient.execute(httpDelete);
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
					intent.putExtra("mensaje", "Error en el borrado");
					setResult(RESULT_CANCELED, intent);
					finish();
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	public void onBackPressed() {
		String mensaje ="Borrado cancelado";
		Intent intent = new Intent();
		intent.putExtra("mensaje", mensaje);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

}
