package com.example.miw;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;


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


public class Configuracion extends Activity {
	private TextView url, usuario, clave; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// addPreferencesFromResource(R.layout.configuracion);
		setContentView(R.layout.configuracion);
		url = (TextView)findViewById(R.id.urlValor);
		usuario = (TextView)findViewById(R.id.usuarioValor);

		clave = (TextView)findViewById(R.id.claveValor);
	}

	public void mostrarURL(View view){
		if(url.getVisibility()==0){
			url.setVisibility(8);
		}
		else{
			url.setVisibility(0);

		}

	}
	public void mostrarUsuario(View view){
		if(usuario.getVisibility()==0){
			usuario.setVisibility(8);
		}
		else{
			usuario.setVisibility(0);
		}
	}
	public void mostrarClave(View view){
		if(clave.getVisibility()==0){
			clave.setVisibility(8);
		}
		else{
			clave.setVisibility(0);

		}
	}
	@Override
	public void onBackPressed() {
		if(!url.getText().toString().equals("") && !usuario.getText().toString().equals("")
				&&!clave.getText().toString().equals("")){
			new ConsultaBD().execute(url.getText().toString(),usuario.getText().toString()
					,clave.getText().toString());
		}
		else{
			Toast.makeText(getBaseContext(), "Introduzca valores correctos", Toast.LENGTH_LONG).show();
		}

	}
	private class ConsultaBD extends AsyncTask <String, Void, String> {

		private ProgressDialog pDialog;
		private String urlConexion;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Configuracion.this);
			pDialog.setMessage(getString(R.string.progress_title));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... url) {
			String respuesta = getString(R.string.sin_respuesta);
			String url_final ;

			if(!url[0].equals("")&&!url[1].equals("") &&!url[2].equals("")){
				url_final = "http://"+url[0] + "/" + url[1]
						+"/connect/" + url[2];
				urlConexion = "http://"+url[0] + "/" + url[1]
						+"/fichas" ;

				try {
					AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
					HttpGet httppost = new HttpGet(url_final);
					HttpResponse response = httpclient.execute(httppost);
					respuesta = EntityUtils.toString(response.getEntity());     
					httpclient.close();
				} catch (Exception e)  {
					Log.e(getString(R.string.app_name),e.toString());
				}
			}
			return respuesta;
		}

		@Override
		protected void onPostExecute(String respuesta) {
			String mensaje = getString(R.string.conexionincorrecta);
			pDialog.dismiss();

			try {
				JSONArray arrayJSON = new JSONArray(respuesta);
				int numRegistros = arrayJSON.getJSONObject(0).getInt("NUMREG");

				if(numRegistros==0){
					mensaje =getString(R.string.conexioncorrecta);
					Intent intent = new Intent();
					intent.putExtra("mensaje", mensaje);
					intent.putExtra("url", urlConexion);
					setResult(RESULT_OK, intent);
					finish();
				}
				else{
					Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();
			}
		}

	}

}
