package com.example.ejemplo;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    boolean suspendido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        solicitarPermisos();

        MyTimerTask tarea = new MyTimerTask();
        Timer myTimer = new Timer();

        Button iniciarServicio = findViewById(R.id.iniciarServicio);
        Button finalizarServicio = findViewById(R.id.finalizarServicio);

        iniciarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myTimer.schedule(tarea, 0, 9000);
                }catch(Exception e){
                    suspendido=false;
                }
                }
        });

        finalizarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suspendido=true;
                  }
       });
    }

    private void solicitarPermisos() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_SMS},1000);
        }
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            if (!suspendido){
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
    }
}