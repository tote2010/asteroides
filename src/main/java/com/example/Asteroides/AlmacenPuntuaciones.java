package com.example.Asteroides;

import java.util.Vector;

public interface AlmacenPuntuaciones {

	public void guardarPuntuaciones(int puntos, String nombre, long fecha);
	
	public Vector<String> listaPuntuaciones(int cantidad);
	
}
