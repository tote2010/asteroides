package com.example.Asteroides;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity implements OnGesturePerformedListener{
	
	private Button bJugar;
	private Button bAcercaDe;
	private Button bConfigurar;
	//private Button bSalir;
	private GestureLibrary libreria;
	
		
	public static AlmacenPuntuaciones almacen;
	public static AlmacenPuntuacionesArray almacenArray;
	public static AlmacenPuntuacionesPreferencias almecenPreferencias;
	public static AlmacenPuntuacionesFicheroInterno almacenFicheroInterno;
	public static AlmacenPuntuacionesFicheroExterno almacenFicheroExterno;
	public static AlmacenPuntuacionesXML_SAX almacenXML_SAX;
	public static AlmacenPuntuacionesXML_DOM almecenXML_DOM;
	public static AlmacenPuntuacionesSQLite almacenSQLite;
	public static AlmacenPuntuacionesSocket almacenSocket;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          
        bAcercaDe = (Button) findViewById(R.id.Button03);
        bJugar = (Button) findViewById(R.id.Play); 
        bConfigurar = (Button) findViewById(R.id.config);
        
       SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
       
      //almacen = new AlmacenPuntuacionesFicheroExterno(this);
      //almacen = new AlmacenPuntuacionesXML_SAX(this);
      //almacen = new AlmacenPuntuacionesXML_DOM(this);
      //almacen = new AlmacenPuntuacionesSQLite(this); 
       //SharedPreferences pref = getSharedPreferences("com.example.asteroides_preference", MODE_PRIVATE);
       //String prefVal = pref.getString("puntuaciones", "2");
      
      	if(pref.getString("puntuaciones", "0").equals("7")){
     		almacen = almacenSocket;
      		almacen = new AlmacenPuntuacionesSQLite(this);
      	}else if(pref.getString("puntuaciones", "0").equals("6")){
         		almacen = almacenSQLite;
          		almacen = new AlmacenPuntuacionesSQLite(this);
      	}else if(pref.getString("puntuaciones", "0").equals("5")){
 	  	   	almacen = almecenXML_DOM;
      		almacen = new AlmacenPuntuacionesXML_DOM(this);
  		}else if(pref.getString("puntuaciones", "0").equals("4")){
  			almacen = almacenXML_SAX;
  			almacen = new AlmacenPuntuacionesXML_SAX(this);
	  	   Toast.makeText(this, "Memoria interna!", Toast.LENGTH_SHORT).show();
  		}else if(pref.getString("puntuaciones", "0").equals("3")){
  			almacen = almacenFicheroExterno;
       		almacen = new AlmacenPuntuacionesFicheroExterno(this);
    	}else if(pref.getString("puntuaciones", "0").equals("2")){
    		almacen = almacenFicheroInterno;
    		almacen = new AlmacenPuntuacionesFicheroInterno(this);
    	   Toast.makeText(this, "Memoria interna!", Toast.LENGTH_SHORT).show();
       }else if(pref.getString("puntuaciones", "0").equals("1")){
    	   	Toast.makeText(this, "XML!", Toast.LENGTH_SHORT).show();
    	   	almacen = almecenPreferencias;
    	   	almacen = new AlmacenPuntuacionesPreferencias(this);
       }else if(pref.getString("puntuaciones", "0").equals("0")){
    	  	Toast.makeText(this, "Array!", Toast.LENGTH_SHORT).show();
    	  	almacen = almacenArray;
    	  	almacen = new AlmacenPuntuacionesArray(this);
       }
        
        
        //startService(new Intent(MainActivity.this, ServicioMusica.class));
        
        
        /// ANIMACION DE BOTONES ///
        /*
	    Animation aBJugar = AnimationUtils.loadAnimation(this, R.anim.aparecer);
	    bJugar.startAnimation(aBJugar);
        Animation aBConfigurar = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha);
        bConfigurar.startAnimation(aBConfigurar);
        Animation aBAdercaDe = AnimationUtils.loadAnimation(this, R.anim.giro_con_zoom);
        bAcercaDe.startAnimation(aBAdercaDe);
        */
       
        bAcercaDe.setOnClickListener(new OnClickListener() {
        	
			public void onClick(View v) {
				
				lanzarAcercaDe(null);
			}
		});
        
        //GESTURES
        /*
        libreria = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if(!libreria.load()){
        	finish();
        }
        GestureOverlayView gesturesView = (GestureOverlayView) findViewById(R.id.gestures);
        gesturesView.addOnGesturePerformedListener(this);
        */
        
       
    }
    
    public void mostrarPreferencias(View view){
    	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
    	String s = "M�sica: " + pref.getBoolean("musica", true)
    			+ ", Multijugador: " + pref.getBoolean("multijugador", true)
    			+ ", Gr�ficos:" + pref.getString("graficos", "?")
    			+ ", Fragmentos:" + pref.getString("fragmentos", "?")
    			+ ", Jugadores:" + pref.getString("jugadores", "?")
    			+ ", Gr�ficos:" + pref.getString("graficos", "?");
    	Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
    
    public void lanzarJuego(View view){
    	Intent i = new Intent(this, Juego.class);
    	//startActivity(i);
    	startActivityForResult(i, 1234);
    	
    }
    
    @Override 
    protected void onActivityResult (int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1234 && resultCode==RESULT_OK && data!=null) {
            int puntuacion = data.getExtras().getInt("puntuacion");
            String nombre = "Yo";
            // Mejor leerlo desde un Dialog o una nueva actividad AlertDialog.Builder
            almacen.guardarPuntuaciones(puntuacion, nombre, System.currentTimeMillis());
            lanzarPuntuaciones(null);
         }
    }
    
    public void lanzarPuntuaciones(View view){
    	Intent i = new Intent(this, Puntuaciones.class);
    	startActivity(i);
    }

    public void lanzarPreferencias(View view){
		Intent i = new Intent(this, Preferencias.class);
		startActivity(i);
	}
    
    
    public void lanzarAcercaDe(View view){
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}
    
    public void salir(View view){
    	finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        switch(id){
        case R.id.action_settings:
        	//return true;
        break;
        case R.id.config:
        	lanzarPreferencias(null);
        break;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		
		ArrayList<Prediction> predictions = libreria.recognize(gesture);
		
		if(predictions.size() > 0){
			String comando = predictions.get(0).name;
			if(comando.equals("play")){
				lanzarJuego(null);
			}else if(comando.equals("configurar")){
				lanzarPreferencias(null);
			}else if(comando.equals("acerca_de")){
				lanzarAcercaDe(null);
			}else if(comando.equals("cancelar")){
				finish();
			}
		}
	}
	
		
}
