package com.example.ut3_tresenraya_guillermorn;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.MediaStore;

public class Musicafondo extends Service {

    MediaPlayer sonido;

    public Musicafondo() {
    }

    //Metodos del servicio de la musica de fondo
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sonido = MediaPlayer.create(this,R.raw.fondo);
        sonido.setLooping(true);
        sonido.start();
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(sonido!= null){
            sonido.stop();
            sonido.release();
        }
    }

}