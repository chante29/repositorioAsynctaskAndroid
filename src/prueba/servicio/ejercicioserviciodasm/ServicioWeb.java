package prueba.servicio.ejercicioserviciodasm;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ServicioWeb extends Activity {
	private EditText dni;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dni = (EditText) findViewById(R.id.edtDNI);
    }
    
    public void consultar(View v){
    	//con esto hay que cambiar primer parámetro de AsyncTask al meter este parámetro
    	new ConsultaBD().execute(dni.getText().toString());
    	
    }
    
    private class ConsultaBD extends AsyncTask <String, Void, String>{
    	
    	private ProgressDialog pDialog;
    	private boolean error = false;
    	private final String URL = "http://demo.calamar.eui.upm.es/dasmapi/v1/miw24/fichas";
    	@Override
    	protected void onPreExecute(){
    		super.onPreExecute();
    		pDialog = new ProgressDialog(ServicioWeb.this);
    		//getString(R.string.progress_title)
    		pDialog.setMessage(getString(R.string.progress_title));
    		pDialog.setIndeterminate(false);
    		pDialog.setCancelable(true);
    		pDialog.show();
    		
    	}
    	
		@Override
		//se modifica la salida del método a String porque se modifica el tercer parámetro a string y es lo que le pasa a post execute
		//modificar argumento entrada porque primer parámetro es String, te pasa todo por comas y tu recibes en array de string
		protected String doInBackground(String... arg0) {
			String dni = arg0[0];
			String datos= "";
			String url_final = URL;
			if(!dni.equals("")){
				url_final += "/" + dni;
			}
			try {
				AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
				HttpGet httpget = new HttpGet(url_final);
				HttpResponse response = httpclient.execute(httpget);
				//para la respuesta
				datos = EntityUtils.toString(response.getEntity());
				httpclient.close();
			} catch (IOException e) {
				//No se puede usar toast porque no se tiene acceso a la interfaz de usuario
				error = true;
				Log.e("Error en la operación", e.toString());
			}
			/*try{
				Thread.sleep(5000);
				
			}catch(InterruptedException e){
				
			}*/
			return datos;
		}
		
		@Override
		//Se cambia argumento a string porque es lo que devuelve el doInBackground
		protected void onPostExecute(String datos){
			String mensaje = "";
			pDialog.dismiss();
			if(error){
				 mensaje = "La consulta genera un error";
				Toast.makeText(ServicioWeb.this, mensaje, Toast.LENGTH_LONG).show();
				
			}
			//convertimos datos en texto
			try {
				JSONArray arrayDatos = new JSONArray(datos);
				//PARA RECUPERAR EL VALOR DE CUANTOS REGISTROS HAY
				int numRegistros = arrayDatos.getJSONObject(0).getInt("NUMREG");
				switch(numRegistros){
				case -1: mensaje = "La consulta genera un error";
				break;
				case 0: mensaje = "La consulta no devuelve registros";
				break;
				default: mensaje = "La consulta devuelve " + numRegistros + "registro/s";
				
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				mensaje = "La consulta genera un error de datos";
			}
			Toast.makeText(ServicioWeb.this, mensaje, Toast.LENGTH_LONG).show();
		}
    	
    	
    }
}
