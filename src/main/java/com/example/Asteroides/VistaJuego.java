package com.example.Asteroides;

import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class VistaJuego extends View implements SensorEventListener {

	/// ASTEROIDES ///
	private Vector<Grafico> Asteroides ; 	// Vector con los asteroides.
	//private Grafico asteroide;
	private int numAsteroides = 5;				// N�mero inicial de asteroides
	private int numFragmentos = 3;				// Fragmentos en que se divide
	private Drawable drawableAsteroide[] = new Drawable[3];
	
	/// NAVE ///
	private Grafico nave;					// Gr�fico de la nave
	private int giroNave;					// Incremento de direcci�n
	private float aceleracionNave; 			// aumento de velocidad
	private static final int MAX_VELOCIDAD_NAVE = 10;
	
	// //// MISIL //////
	private Grafico misil;
	private static int PASO_VELOCIDAD_MISIL = 12;
	private boolean misilActivo = false;
	private int tiempoMisil;
	
	// Incremento est�ndar de giro y aceleraci�n
    private static final int PASO_GIRO_NAVE = 5;
    private static final float PASO_ACELERACION_NAVE = 0.5f;
    
	 // //// THREAD Y TIEMPO //////
	 // Thread encargado de procesar el juego
	 private ThreadJuego thread = new ThreadJuego();
	 // Cada cuanto queremos procesar cambios (ms)
	 private static int PERIODO_PROCESO = 50;
	 // Cuando se realiz� el �ltimo proceso
	 private long ultimoProceso = 0;
	 
	 private float mX=0, mY=0;
	 private boolean disparo = false;
	 
	 /// SENSORES ///
	 private boolean hayValorGiroInicial = false;
	 private float valorGiroInicial;
	 private boolean hayValorAceleracionInicial = false;
	 private float valorAceleracionInicial;
	 
	 /// PREFERENCES ///
	 private SharedPreferences pref; 
	 
	 /// PUNTUACIONS ///
	 private int puntuacion = 0;
	 private Activity padre;
	 
	 public VistaJuego(Context context, AttributeSet attrs){
		
		super(context, attrs);
		
		Drawable drawableNave, drawableMisil;
				
		// ANTERIOR
		//drawableAsteroide = context.getResources().getDrawable(R.drawable.asteroide1);
		//drawableNave = context.getResources().getDrawable(R.drawable.nave);
		pref = context.getSharedPreferences("com.example.asteroides_preferences", Context.MODE_PRIVATE);
		 
		if (pref.getString("graficos", "1").equals("0")) {
			   Path pathAsteroide = new Path();
			       pathAsteroide.moveTo((float) 0.3, (float) 0.0);
			       pathAsteroide.lineTo((float) 0.6, (float) 0.0);
			       pathAsteroide.lineTo((float) 0.6, (float) 0.3);
			       pathAsteroide.lineTo((float) 0.8, (float) 0.2);
			       pathAsteroide.lineTo((float) 1.0, (float) 0.4);
			       pathAsteroide.lineTo((float) 0.8, (float) 0.6);
			       pathAsteroide.lineTo((float) 0.9, (float) 0.9);
			       pathAsteroide.lineTo((float) 0.8, (float) 1.0);
			       pathAsteroide.lineTo((float) 0.4, (float) 1.0);
			       pathAsteroide.lineTo((float) 0.0, (float) 0.6);
			       pathAsteroide.lineTo((float) 0.0, (float) 0.2);
			       pathAsteroide.lineTo((float) 0.3, (float) 0.0);
		       
			       for (int i=0; i<3; i++) {
			    	      ShapeDrawable dAsteroide = new ShapeDrawable(new PathShape(
			    	                                            pathAsteroide, 1, 1));
			    	      dAsteroide.getPaint().setColor(Color.WHITE);
			    	      dAsteroide.getPaint().setStyle(Style.STROKE);
			    	      dAsteroide.setIntrinsicWidth(50 - i * 14);
			    	      dAsteroide.setIntrinsicHeight(50 - i * 14);
			    	      drawableAsteroide[i] = dAsteroide;
			    	}
				    setBackgroundColor(Color.BLACK);
				    
			Path pathNave = new Path();
				pathNave.moveTo((float) 0.0, (float) 0.0);
				//pathNave.addCircle(0.0, 0.0, 0.1, R.);
				pathNave.lineTo((float) 0.0, (float) 1.0);
				pathNave.lineTo((float) 1.0, (float) 0.5);
				pathNave.lineTo((float) 0.0, (float) 0.0);
			
			ShapeDrawable dNave = new ShapeDrawable(new PathShape(pathNave, 1, 1));
				dNave.getPaint().setColor(Color.WHITE);
				dNave.getPaint().setStyle(Style.STROKE);
				dNave.setIntrinsicWidth(20);
				dNave.setIntrinsicHeight(15);
				drawableNave = dNave;
				setBackgroundColor(Color.BLACK);
				
			ShapeDrawable dMisil = new ShapeDrawable(new RectShape());
				dMisil.getPaint().setColor(Color.WHITE);
				dMisil.getPaint().setStyle(Style.STROKE);
				dMisil.setIntrinsicWidth(15);
				dMisil.setIntrinsicHeight(3);
				drawableMisil = dMisil;	
				    
		} else {
				drawableAsteroide[0] = context.getResources().getDrawable(R.drawable.asteroide1);
				drawableAsteroide[1] = context.getResources().getDrawable(R.drawable.asteroide2);
				drawableAsteroide[2] = context.getResources().getDrawable(R.drawable.asteroide3);
				drawableNave = context.getResources().getDrawable(R.drawable.nave);
				drawableMisil = context.getResources().getDrawable(R.drawable.misil1);
		}
				
				
		nave = new Grafico(this, drawableNave);
		
		
		//asteroide = new Grafico(this, drawableAsteroide);
		Asteroides = new Vector<Grafico>();
		for(int i = 0; i < numAsteroides; i++){
			Grafico asteroide = new Grafico(this, drawableAsteroide[0]);
			asteroide.setIncY(Math.random() * 4 - 2);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setAngulo( (int) (Math.random() * 360));
			asteroide.setRotacion( (int) (Math.random() * 8 - 4));
			Asteroides.add(asteroide);
			
		}
		
		misil = new Grafico(this, drawableMisil);
		
							
	}
	
	synchronized protected void actualizaFisica() {
	       long ahora = System.currentTimeMillis();
	       // No hagas nada si el per�odo de proceso no se ha cumplido.
	       if (ultimoProceso + PERIODO_PROCESO > ahora) {
	             return;
	       }
	       // Para una ejecuci�n en tiempo real calculamos retardo           
	       double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
	       ultimoProceso = ahora; // Para la pr�xima vez
	       
	       // Actualizamos velocidad y direcci�n de la nave a partir de 
	       // giroNave y aceleracionNave (seg�n la entrada del jugador)
	       nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
	       double nIncX = nave.getIncX() + aceleracionNave * Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
	       double nIncY = nave.getIncY() + aceleracionNave * Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
	       // Actualizamos si el m�dulo de la velocidad no excede el m�ximo
	       if (Math.hypot(nIncX,nIncY) <= MAX_VELOCIDAD_NAVE){
	             nave.setIncX(nIncX);
	             nave.setIncY(nIncY);
	       }
	       // Actualizamos posiciones X e Y
	       nave.incrementaPos(retardo);
	       for (Grafico asteroide : Asteroides) {
	             asteroide.incrementaPos(retardo);
	       }
	       
	       //Actualizamos posici�n de misil
	       
	       if (misilActivo) {
	              misil.incrementaPos(retardo);
	              tiempoMisil-=retardo;
	              if (tiempoMisil < 0) {
	                    misilActivo = false;
	              } else {
	       for (int i = 0; i < Asteroides.size(); i++)
	                    if (misil.verificaColision(Asteroides.elementAt(i))) {
	                           destruyeAsteroide(i);
	                           break;
	                    }
	              }
	       }
	       
	       for (Grafico asteroide : Asteroides) {
	    	    if (asteroide.verificaColision(nave)) {
	    	       salir();
	    	    }
	    	}
	}
	
	private void destruyeAsteroide(int i) {
		int tam;
		if(Asteroides.get(i).getDrawable()!=drawableAsteroide[2]){
		   if(Asteroides.get(i).getDrawable()==drawableAsteroide[1]){
		          tam=2;
		   } else {
		          tam=1;
		   }
		   for(int n=0;n<numFragmentos;n++){
		          Grafico asteroide = new Grafico(this,drawableAsteroide[tam]);
		          asteroide.setPosX(Asteroides.get(i).getPosX());
		          asteroide.setPosY(Asteroides.get(i).getPosY());
		          asteroide.setIncX(Math.random()*7-2-tam);
		          asteroide.setIncY(Math.random()*7-2-tam);
		          asteroide.setAngulo((int)(Math.random()*360));
		          asteroide.setRotacion((int)(Math.random()*8-4));
		          Asteroides.add(asteroide);
		          Asteroides.remove(i);
		          puntuacion += 1000;
		   }     
		}
		Asteroides.remove(i);
	    misilActivo = false;
	    
	    if (Asteroides.isEmpty()) {
	          salir();
	    }
	}
	 
	private void ActivaMisil() {
		
			misil.setPosX(nave.getPosX());
			misil.setPosY(nave.getPosY());
			misil.setAngulo(nave.getAngulo());
			misil.setIncX(Math.cos(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
			misil.setIncY(Math.sin(Math.toRadians(misil.getAngulo())) * PASO_VELOCIDAD_MISIL);
							
		    tiempoMisil = (int) Math.min(this.getWidth() / Math.abs( misil. getIncX()), this.getHeight() / Math.abs(misil.getIncY())) - 2;
		    misilActivo = true;
				 
	}
	
	
	class ThreadJuego extends Thread {
		 private boolean pausa,corriendo;
		 
		   public synchronized void pausar() {
		          pausa = true;
		   }
		 
		   public synchronized void reanudar() {
		          pausa = false;
		          notify();
		   }
		 
		   public void detener() {
		          corriendo = false;
		          if (pausa) reanudar();
		   }
		  
		   @Override public void run() {
		          corriendo = true;
		          while (corriendo) {
		             actualizaFisica();
		             synchronized (this) {
		                while (pausa)
		                   try {
		                      wait();
		                   } catch (Exception e) {
		                   }
		                }
		             }
		          }  
		       }  
		    
	
	
	@Override
	protected void onSizeChanged(int ancho, int alto, int anchoAnter, int altoAnter){
		
		super.onSizeChanged(ancho, alto, anchoAnter, altoAnter);
		
		// Una vez que conocemos nuestro ancho y alto.
		
		for(Grafico asteroide: Asteroides){
			do{
				asteroide.setPosX(Math.random() * (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			}while(asteroide.distancia(nave) < (ancho + alto) /10);	
		}
			
		//asteroide.setPosX(ancho/3-asteroide.getAncho());
		//asteroide.setPosY(alto/3-asteroide.getAlto());
		
		nave.setPosX(ancho/2-nave.getAncho()/2);
		nave.setPosY(alto/2-nave.getAlto()/2);
		
		ultimoProceso = System.currentTimeMillis();
		thread.start();
	}
	
	@Override
	synchronized protected void onDraw(Canvas canvas){
		
		super.onDraw(canvas);
		
		nave.dibujaGrafico(canvas);
		
		if(misilActivo == true){
			misil.dibujaGrafico(canvas);
		}
		
		//asteroide.dibujaGrafico(canvas);
		
		for (Grafico asteroide: Asteroides){
			asteroide.dibujaGrafico(canvas);
		}
				
	}
	
	   @Override
	    public boolean onKeyDown(int codigoTecla, KeyEvent evento){
		   if (pref.getString("control", "1").equals("0")) {
		    	super.onKeyDown(codigoTecla, evento);
		    	
		    	boolean procesada = true;
		    	switch (codigoTecla) {
				case KeyEvent.KEYCODE_NUMPAD_0:
					aceleracionNave = +PASO_ACELERACION_NAVE;
					break;
				case KeyEvent.KEYCODE_SPACE:
					giroNave = PASO_GIRO_NAVE;
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					giroNave = +PASO_GIRO_NAVE;
					break;
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_ENTER:
					 ActivaMisil();
					break;
				default:
					procesada = false;
					break;
				}
		    	
		    	return procesada;
		   }else{
			   return false;
		   }
	    }
	   
	   @Override
       public boolean onKeyUp(int codigoTecla, KeyEvent evento) {
		   	if (pref.getString("control", "1").equals("0")) {
	             super.onKeyUp(codigoTecla, evento);
	             // Suponemos que vamos a procesar la pulsaci�n
	             boolean procesada = true;
	             switch (codigoTecla) {
	             case KeyEvent.KEYCODE_DPAD_UP:
	                    aceleracionNave = 0;
	                    break;
	             case KeyEvent.KEYCODE_DPAD_LEFT:
	             case KeyEvent.KEYCODE_DPAD_RIGHT:
	                    giroNave = 0;
	                    break;
	             default:
	                    // Si estamos aqu�, no hay pulsaci�n que nos interese
	                    procesada = false;
	                    break;
	             }
	             return procesada;
		   	}else{
		   		return false;
		   	}
       }

	   @Override
	   public boolean onTouchEvent (MotionEvent event) {
		  if (pref.getString("control", "1").equals("1")) {
			  
		      super.onTouchEvent(event);
		      float x = event.getX();
		      float y = event.getY();
		      switch (event.getAction()) {
		      case MotionEvent.ACTION_DOWN:
		             disparo=true;
		             break;
		      case MotionEvent.ACTION_MOVE:
		             float dx = Math.abs(x - mX);
		             float dy = Math.abs(y - mY);
		             if (dy<6 && dx>6){
		                    giroNave = Math.round((x - mX) * 2);
		                    disparo = false;
		             } else if (dx<6 && dy>6){
		                    aceleracionNave = Math.round((mY - y) / 15);
		                    disparo = false;
		             }
		             break;
		      case MotionEvent.ACTION_UP:
		             giroNave = 0;
		             aceleracionNave = 0;
		             if (disparo){
		            	 ActivaMisil();
		             }
		             break;
		      }
		      mX=x; mY=y;       
		      return true;
		      
		  }else if(pref.getString("control", "1").equals("2")){
			  super.onTouchEvent(event);
		      float x = event.getX();
		      float y = event.getY();
		      if(event.getAction() == MotionEvent.ACTION_UP){
		    	  giroNave = 0;
		    	  aceleracionNave = 0;
		    	  if(disparo){
		    		  ActivaMisil();
		    		  return true;
		    	  }else{
		    		  return false;
		    	  }
		      }else{
		    	  return false;
		      }
		      
		  }else{
			  return false;
		  }
	   }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
				
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		if (pref.getString("control", "1").equals("2")) {

			float valorGiro = event.values[0];
			float valorAceleracion = event.values[1];
			
			
			switch(event.sensor.getType()){
				case  Sensor.TYPE_ACCELEROMETER:
					if(!hayValorGiroInicial){
						valorGiroInicial = valorGiro;
						hayValorGiroInicial = true;
					}
					giroNave = (int) (valorGiro - valorGiroInicial) *2;
					if(!hayValorAceleracionInicial){
						valorAceleracionInicial = valorAceleracion;
						hayValorAceleracionInicial = true;
					}
					aceleracionNave = (int) (valorAceleracion - valorAceleracionInicial) /4;
					
				break;
				default:
			}
		}
	}

	public void activarSensores(Context context){
		
		SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		List<Sensor> listaSensores = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		
		if(!listaSensores.isEmpty()){
			Sensor orientationSensor = listaSensores.get(0);
			mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
		}
	}
	
	public void desactivarSensores(SensorEventListener sensorEventListener, Context context){
		
		SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.unregisterListener(sensorEventListener);
		
	}
	
	private void salir() {
	    Bundle bundle = new Bundle();
	    bundle.putInt("puntuacion", puntuacion);
	    Intent intent = new Intent();
	    intent.putExtras(bundle);
	    padre.setResult(Activity.RESULT_OK, intent);
	    padre.finish();
	}
	
	public ThreadJuego getThread() {
		return thread;
	}
	
	public void setPadre(Activity padre) {
	    this.padre = padre;
	}
}
