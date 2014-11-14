package prueba.servicio.activityforresult;

import prueba.servicio.ejercicioserviciodasm.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityDos extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	public void responderActividadUno (View v){
		Intent i= new Intent();
		i.putExtra("respuesta", "OK desde la actividad DOS");
		setResult(RESULT_OK, i);
		finish();
	}
	
	@Override
	public void onBackPressed(){
		Intent i= new Intent();
		i.putExtra("respuesta", "OK desde la actividad DOS");
		setResult(RESULT_CANCELED, i);
		super.onBackPressed();
	}
}
