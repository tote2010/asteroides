package com.example.Asteroides;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Juego extends Activity {
	
	private VistaJuego vistaJuego;
	private SensorEventListener sensorEventListener;
	
	private MediaPlayer mp;
	
	private SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_juego);
		
		vistaJuego = (VistaJuego) findViewById(R.id.VistaJuego);
		vistaJuego.setPadre(this);
		
		mp = MediaPlayer.create(this, R.raw.audio);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.juego, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		mp.pause();
		vistaJuego.getThread().pausar();
		vistaJuego.desactivarSensores(sensorEventListener, this);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		
		SharedPreferences pref = getSharedPreferences("com.example.asteroides_preference", MODE_PRIVATE);
		Boolean musica = pref.getBoolean("musica", false);
        if(musica == true){
        	mp.start();
        }
		
		
		vistaJuego.getThread().reanudar();
		vistaJuego.activarSensores(this);
	}
	@Override
	protected void onDestroy(){
		
		SharedPreferences pref = getPreferences(MODE_PRIVATE);
		Boolean musica = pref.getBoolean("musica", false);
        if(musica == true){
        	mp.pause();
        }
        		
		vistaJuego.getThread().detener();
		vistaJuego.desactivarSensores(sensorEventListener, this);
		super.onDestroy();
	}
	
}
