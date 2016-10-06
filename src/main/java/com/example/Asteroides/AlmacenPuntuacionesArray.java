package com.example.Asteroides;

import java.util.Vector;

import android.content.Context;

public class AlmacenPuntuacionesArray implements AlmacenPuntuaciones {
	
	private Vector<String> puntuaciones;
	
	public AlmacenPuntuacionesArray(Context context){
		
		puntuaciones = new Vector<String>();
		
		/*
		puntuaciones.add("123000 Pepito Dominguez");
		puntuaciones.add("111000 Pedro Martinez");
		puntuaciones.add("011000 Paco Perez");
		*/
	}

	public void guardarPuntuaciones(int puntos, String nombre, long fecha){
		puntuaciones.add(0, puntos + " " + nombre);
	}
	
	public Vector<String> listaPuntuaciones(int cantidad){
		
		return puntuaciones;
	}
}
