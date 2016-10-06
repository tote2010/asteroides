package com.example.Asteroides;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;

public class AlmacenPuntuacionesPreferencias implements AlmacenPuntuaciones{

	private static String PREFERENCIAS = "puntuaciones";
	private Context context;
	
	public AlmacenPuntuacionesPreferencias(Context context){
		this.context = context;
	}
	
	@Override
	public void guardarPuntuaciones(int puntos, String nombre, long fecha) {
		
		SharedPreferences preferencias = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferencias.edit();
		//editor.putString("puntuacion", puntos + " " + nombre);
		for (int n = 9; n >= 1; n--) {
	        editor.putString("puntuacion" + n,
	        preferencias.getString("puntuacion" + (n - 1), ""));
	   }
	   editor.putString("puntuacion0", puntos + " " + nombre);
	   editor.commit();
		
	}

	@Override
	public Vector<String> listaPuntuaciones(int cantidad) {
		
		Vector<String> result = new Vector<String>();
		SharedPreferences preferencias = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);
		//String s = preferencias.getString("puntuacion", "");
		for (int n = 0; n <= 9; n++) {
			String s = preferencias.getString("puntuacion" + n, "");
			if(s != ""){
				result.add(s);
			}			
		}
		return result;
	}

}
