package com.example.Asteroides;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class AlmacenPuntuacionesFicheroExterno implements AlmacenPuntuaciones{

	private static String FICHERO = Environment.getExternalStorageDirectory() + "/puntuaciones.txt";
    private Context context;
    
    public AlmacenPuntuacionesFicheroExterno(Context context) {
        this.context = context;
    }
    
	@Override
	public void guardarPuntuaciones(int puntos, String nombre, long fecha) {
		
		String estadoSD = Environment.getExternalStorageState();
		if(!estadoSD.equals(Environment.MEDIA_MOUNTED)){
			Toast.makeText(context, "No se ha podido escribir en la memoria externa", Toast.LENGTH_LONG).show();
			return;
		}else{
					
			try {
	             FileOutputStream f = new FileOutputStream(FICHERO, true);
	             String texto = puntos + " " + nombre + "\n";
	             f.write(texto.getBytes());
	             f.close();
		      } catch (Exception e) {
		             Log.e("Asteroides", e.getMessage(), e);
		      }
		}
		
	}

	@Override
	public Vector<String> listaPuntuaciones(int cantidad) {
		 Vector<String> result = new Vector<String>();
		 
		 String estadoSD = Environment.getExternalStorageState();
		 if(!estadoSD.equals(Environment.MEDIA_MOUNTED) && !estadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
			 Toast.makeText(context, "No se ha podido leer la memoria externa", Toast.LENGTH_LONG).show();
				return result;
		 }else{
	         try {
	                FileInputStream f = new FileInputStream(FICHERO);
	                BufferedReader entrada = new BufferedReader(new InputStreamReader(f));
	                int n = 0;
	                String linea;
	                do {
	                       linea = entrada.readLine();
	                       if (linea != null) {
	                              result.add(linea);
	                              n++;
	                       }
	                } while (n < cantidad && linea != null);
	                f.close();
	         } catch (Exception e) {
	                Log.e("Asteroides", e.getMessage(), e);
	         }
	         return result;
		 }
	}

}
