package com.example.miw;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.conn.tsccm.WaitingThread;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class Inicio extends Activity {

	private EditText dni;
	private final int CONSULTA_ACTIVIDAD = 001;
	private final int INSERCION_ACTIVIDAD = 002;
	private final int BORRADO_ACTIVIDAD = 003;
	private final int MODIFICACION_ACTIVIDAD = 004;
	private final int CONFIGURACION_ACTIVIDAD = 005;
	private int numRegistros;
	private String url;
	private View configuracion;
	private Button consulta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inicio);
		dni = (EditText)findViewById(R.id.dni);
		configuracion= findViewById(R.id.configuracion);
		findViewById(R.id.aniadir).setEnabled(false);
		findViewById(R.id.buscar).setEnabled(false);
		findViewById(R.id.editar).setEnabled(false);
		findViewById(R.id.eliminar).setEnabled(false);

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		Toast.makeText(this, "Por favor configure la conexión", Toast.LENGTH_LONG).show();
		return true;

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.configuracion:
			Intent i = new Intent(this, Configuracion.class);
			startActivityForResult(i, CONFIGURACION_ACTIVIDAD);
			break;
		}
		return true;
	}
	public void conectar(View v){
		AsyncTask<String, Void, String> consulta = new ConsultaBD();
		consulta.execute(dni.getText().toString());



	}
	public void insertar(View v){
		if(dni.getText().toString().equals("")){
			Toast.makeText(getBaseContext(), "Debe introducir un DNI", Toast.LENGTH_LONG).show();
		}
		else{

			new InsertaBD().execute(dni.getText().toString());
		}

	}
	public void eliminar(View v){
		if(dni.getText().toString().equals("")){
			Toast.makeText(getBaseContext(), "Debe introducir un DNI", Toast.LENGTH_LONG).show();
		}
		else{
			new EliminarBD().execute(dni.getText().toString());
		}
	}
	public void modificar(View v){
		if(dni.getText().toString().equals("")){
			Toast.makeText(getBaseContext(), "Debe introducir un DNI", Toast.LENGTH_LONG).show();
		}
		else{
			new ModificarBD().execute(dni.getText().toString());
		}
	}

	private class ConsultaBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;
		//private final String URL = url;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Inicio.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... dnis) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final = url;
			if(!dnis[0].equals("")){
				url_final += "/"+dnis[0];

			}

			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());     
				httpclient.close();
			} catch (Exception e)  {
				Log.e(getString(R.string.app_name),e.toString());
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(String respuesta) {
			String mensaje = getString(R.string.registro_noexiste);
			pDialog.dismiss();
			try {
				JSONArray arrayJSON = new JSONArray(respuesta);
				int numRegistros = arrayJSON.getJSONObject(0).getInt("NUMREG");

				if(numRegistros >0){
					Intent i =  new Intent(Inicio.this, Consulta.class);
					i.putExtra("datos", respuesta);
					i.putExtra("numRegistros", numRegistros);
					i.putExtra("url", url);
					startActivityForResult(i, CONSULTA_ACTIVIDAD);
				}
				if(numRegistros ==-1){
					Intent intent = new Intent();
					intent.putExtra("mensaje", "Error en la insercion");
					setResult(RESULT_CANCELED, intent);
					finish();
				}
				else{
					Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	private class InsertaBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;
		private final String URL = url;
		private String dniInsertar;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Inicio.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... dnis) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final = URL;
			if(!dnis[0].equals("")){
				url_final += "/"+dnis[0];
				dniInsertar = dnis[0];

			}

			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());     
				httpclient.close();
			} catch (Exception e)  {
				Log.e(getString(R.string.app_name),e.toString());
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(String respuesta) {
			String mensaje = getString(R.string.registro_existe);
			pDialog.dismiss();
			try {
				JSONArray arrayJSON = new JSONArray(respuesta);
				int numRegistros = arrayJSON.getJSONObject(0).getInt("NUMREG");

				if(numRegistros >0){
					Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();
				}
				else{
					Intent i =  new Intent(Inicio.this, Insercion.class);
					i.putExtra("dniInsertar", dniInsertar);
					i.putExtra("url", url);
					startActivityForResult(i, INSERCION_ACTIVIDAD);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private class EliminarBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;
		private final String URL = url;
		private String dniEliminar;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Inicio.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... dnis) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final = URL;
			if(!dnis[0].equals("")){
				url_final += "/"+dnis[0];
				dniEliminar = dnis[0];

			}

			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());     
				httpclient.close();
			} catch (Exception e)  {
				Log.e(getString(R.string.app_name),e.toString());
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(String respuesta) {
			String mensaje = getString(R.string.registro_noexiste);
			pDialog.dismiss();
			try {
				JSONArray arrayJSON = new JSONArray(respuesta);
				numRegistros = arrayJSON.getJSONObject(0).getInt("NUMREG");

				if(numRegistros >0){
					Intent i =  new Intent(Inicio.this, Borrado.class);
					i.putExtra("dniEliminar", dniEliminar);
					i.putExtra("datos", respuesta);
					i.putExtra("url", url);
					startActivityForResult(i, BORRADO_ACTIVIDAD);
				}
				else{
					Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	private class ModificarBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;
		private final String URL = url;
		private String dniModificar;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Inicio.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... dnis) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final = URL;
			if(!dnis[0].equals("")){
				url_final += "/"+dnis[0];
				dniModificar = dnis[0];

			}

			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				respuesta = EntityUtils.toString(response.getEntity());     
				httpclient.close();
			} catch (Exception e)  {
				Log.e(getString(R.string.app_name),e.toString());
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(String respuesta) {
			String mensaje = getString(R.string.registro_noexiste);
			pDialog.dismiss();
			try {
				JSONArray arrayJSON = new JSONArray(respuesta);
				int numRegistros = arrayJSON.getJSONObject(0).getInt("NUMREG");

				if(numRegistros >0){
					Intent i =  new Intent(Inicio.this, Modificacion.class);
					i.putExtra("dniModificar", dniModificar);
					i.putExtra("datos", respuesta);
					i.putExtra("url", url);
					startActivityForResult(i, MODIFICACION_ACTIVIDAD);
				}
				else{
					Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	public void onActivityResult(int requestCode, int resultCode, Intent data)	{
		String mensaje = "Operación cancelada";
		if(requestCode==CONSULTA_ACTIVIDAD ||requestCode==INSERCION_ACTIVIDAD|| 
				requestCode==BORRADO_ACTIVIDAD || requestCode==MODIFICACION_ACTIVIDAD){   		
			if(resultCode==RESULT_OK){
				Bundle extras = data.getExtras();
				if(extras != null) {
					mensaje = extras.getString("mensaje");
				} 
			}
			if(resultCode==RESULT_CANCELED){
				Bundle extras = data.getExtras();
				if(extras != null) {
					mensaje = extras.getString("mensaje");
				} 
			}

		} 
		if(requestCode==CONFIGURACION_ACTIVIDAD){
			if(resultCode==RESULT_OK){
				Bundle extras = data.getExtras();
				if(extras != null) {
					mensaje = extras.getString("mensaje");
					url = extras.getString("url");
					findViewById(R.id.aniadir).setEnabled(true);
					findViewById(R.id.buscar).setEnabled(true);
					findViewById(R.id.editar).setEnabled(true);
					findViewById(R.id.eliminar).setEnabled(true);
				} 
			}
		}
		Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();


	}



}
