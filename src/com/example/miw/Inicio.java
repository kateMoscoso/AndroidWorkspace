package com.example.miw;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import android.widget.EditText;
import android.widget.Toast;

import android.view.Menu;
import android.view.MenuItem;

public class Inicio extends Activity {

	private EditText dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        dni = (EditText)findViewById(R.id.dni);
    }
    public void conectar(View v){
    	new ConsultaBD().execute(dni.getText().toString());
    	
    }

    private class ConsultaBD extends AsyncTask <String, Void, String> {
    	         
        private ProgressDialog pDialog;
        private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw27/fichas";

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
        	String mensaje = getString(R.string.sin_datos);
        	pDialog.dismiss();
			try {
				JSONArray arrayJSON = new JSONArray(respuesta);
				int numRegistros = arrayJSON.getJSONObject(0).getInt("NUMREG");
				if(numRegistros >0){
					mensaje = respuesta;
					Intent i =  new Intent(Inicio.this, Consulta.class);
					i.putExtra("datos", respuesta);
					i.putExtra("numRegistros", numRegistros);
					startActivity(i);
				}
				
	        	//Toast.makeText(getBaseContext(), mensaje, Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }

    }

}
