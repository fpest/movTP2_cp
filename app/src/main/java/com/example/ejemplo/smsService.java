package com.example.ejemplo;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Telephony;
import android.util.Log;

import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;

public class smsService extends Service {
    private Timer myTimer = new Timer();
    public smsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                buscarMensajes();
        }
        },0,9000);
        return START_STICKY;

    }
    @Override
    public boolean stopService(Intent name) {
        super.stopService(name);

        myTimer.cancel();
        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myTimer.cancel();
    }

    public void buscarMensajes() {

                Uri sms = Uri.parse("content://sms/inbox");
               ContentResolver cr = getContentResolver();
                Cursor cursor = cr.query(sms, null, null, null, null);
                if (cursor.getCount() > 0) {
                    int mensajeIndex = cursor.getColumnIndex(Telephony.Sms.BODY);
                    int enviadoIndex = cursor.getColumnIndex(Telephony.Sms.DATE_SENT);
                    int indice = 0;
                    while (cursor.moveToNext() && indice < 5) {
                        String mensaje = cursor.getString(mensajeIndex);
                        String enviado = cursor.getString(enviadoIndex);

                        Timestamp timestamp = new Timestamp(Long.parseLong(enviado));

                        Log.d("salida ", "Fecha Envio: " + timestamp + " Mensaje: " + mensaje);
                        indice = indice + 1;
                    }
                }

            }






}