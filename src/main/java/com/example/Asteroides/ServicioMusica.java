package com.example.Asteroides;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;


public class ServicioMusica extends Service{

	MediaPlayer reproductor;
	private NotificationManager nm;
	private static final int ID_NOTIFICACION_CREAR = 1;
	private Notification notificacion;
	
	@Override
	public void onCreate(){
		Toast.makeText(this, "Servicio Creado", Toast.LENGTH_SHORT).show();
		reproductor = MediaPlayer.create(this, R.raw.audio);
		
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int idArranque){
		notificacion = new Notification(R.drawable.ic_launcher, "Creando servicio de m�sica", System.currentTimeMillis());
		PendingIntent intencionPendiente = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
		notificacion.setLatestEventInfo(this, "Reproduciendo Musica", "Info adicional", intencionPendiente);
		nm.notify(ID_NOTIFICACION_CREAR, notificacion);
		
		Toast.makeText(this, "Servicio arrancado" + idArranque, Toast.LENGTH_SHORT).show();
		reproductor.start();
		return START_NOT_STICKY;
	}
	

	@Override
	public void onDestroy(){
		Toast.makeText(this, "Servicio detenido", Toast.LENGTH_SHORT).show();
		reproductor.stop();
		nm.cancel(ID_NOTIFICACION_CREAR);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
