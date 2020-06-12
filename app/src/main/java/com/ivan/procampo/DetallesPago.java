package com.ivan.procampo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DetallesPago extends AppCompatActivity {

    TextView txtId,txtEstatus,txtMonto;

    Button volver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pago);

        //Referencias
        txtId = findViewById(R.id.txtId);
        txtEstatus = findViewById(R.id.txtEstatus);
        txtMonto = findViewById(R.id.txtMonto);
        volver = findViewById(R.id.volver);

        //Coger los datos de antes
        Intent intent = getIntent();
        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            verDetalles(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent volver = new Intent(DetallesPago.this,MenuPrincipal.class);
                startActivity(volver);
            }
        });
    }

    private void verDetalles(JSONObject response, String monto) {
        try {
            txtId.setText(response.getString("id"));
            txtEstatus.setText(response.getString("state"));
            txtMonto.setText("€"+monto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
