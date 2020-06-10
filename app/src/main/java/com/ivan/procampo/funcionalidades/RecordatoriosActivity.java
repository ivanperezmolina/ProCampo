package com.ivan.procampo.funcionalidades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ivan.procampo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class RecordatoriosActivity extends AppCompatActivity {

    private EditText nombreRecordatorio;
    private TextView tvFecha, tvHora;
    private Button seleFecha, seleHora, guardar, btnEliminar;

    Calendar actual = Calendar.getInstance();
    Calendar calendar = Calendar.getInstance();

    private int minutos,hora,dia, mes, anno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios);

        nombreRecordatorio = findViewById(R.id.nombreRecordatorio);
        tvFecha = findViewById(R.id.tv_fecha);
        tvHora = findViewById(R.id.tv_hora);
        seleFecha = findViewById(R.id.btn_selefecha);
        seleHora = findViewById(R.id.btn_selehora);
        guardar = findViewById(R.id.btn_guardar);
        btnEliminar = findViewById(R.id.btn_eliminar);



        seleFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Tomar la fecha actual
                anno = actual.get(Calendar.YEAR);
                mes = actual.get(Calendar.MONTH);
                dia = actual.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.YEAR,year);


                        //Ponemos un formato mas comprimido
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = format.format(calendar.getTime());
                        tvFecha.setText(strDate);
                    }
                },anno,mes,dia);
                datePickerDialog.show();
            }
        });


        seleHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hora = actual.get(Calendar.HOUR_OF_DAY);
                minutos = actual.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        tvHora.setText(String.format("%02d:%02d",hourOfDay,minute));

                    }
                },hora,minutos,true);
                timePickerDialog.show();

            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = generateKey();
                //Fecha seleccionada - la hora del sistema; para saber cuando tiene que pitar
                Long alertTime = calendar.getTimeInMillis() - System.currentTimeMillis();
                int random = (int) (Math.random()*50+1);

                String detalleNoti = nombreRecordatorio.getText().toString();
                Data data = guardarData("Notificación ProCampo",detalleNoti,random);

                com.ivan.procampo.WorkManager.guardarNotificacion(alertTime,data,tag );

                Toast.makeText(RecordatoriosActivity.this, "¡Te avisaré!", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarNotifi("tag");
            }
        });

    }//Fin onCreate

    private void eliminarNotifi(String tag){
        WorkManager.getInstance(this).cancelAllWorkByTag(tag);
        Toast.makeText(RecordatoriosActivity.this, "Recordatorio eliminado con exito", Toast.LENGTH_SHORT).show();
    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    private Data guardarData(String titulo, String detalle, int id_noti){
        return new Data.Builder()
                .putString("titulo",titulo)
                .putString("detalle",detalle)
                .putInt("id_noti",id_noti).build();
    }
}