package com.ivan.procampo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ivan.procampo.funcionalidades.RecordatoriosActivity;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class WorkManager extends Worker {
    public WorkManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void guardarNotificacion(long duracion, Data data, String tag){
        OneTimeWorkRequest notificacion = new OneTimeWorkRequest.Builder(WorkManager.class)
                .setInitialDelay(duracion, TimeUnit.MILLISECONDS).addTag(tag)
                .setInputData(data).build();

        androidx.work.WorkManager instance = androidx.work.WorkManager.getInstance();
        instance.enqueue(notificacion);
    }

    @NonNull
    @Override
    public Result doWork() {

        String titulo = getInputData().getString("titulo");
        String detalle = getInputData().getString("detalle");

        int id = (int) getInputData().getLong("idnotificacion",0);


        cuerpo(titulo,detalle);

        return Result.success();
    }

    private void cuerpo(String t, String d){
        String id = "message";
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),id);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(id,"nuevo",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Notificacion ProCampo");
            notificationChannel.setShowBadge(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);

            Intent intent = new Intent(getApplicationContext(), RecordatoriosActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);

            builder.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(t)
                    .setTicker("Nueva notificación de ProCampo")
                    .setSmallIcon(R.mipmap.ic_index_round)
                    .setContentText(d)
                    .setContentIntent(pendingIntent)
                    .setContentInfo("nuevo");

            Random random = new Random();
            int idNotift = random.nextInt(6000);

            assert notificationManager != null;
            notificationManager.notify(idNotift,builder.build());

        }
    }
}
