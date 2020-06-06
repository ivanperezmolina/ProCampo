package com.ivan.procampo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DetallesPago extends AppCompatActivity {

    TextView txtId,txtEstatus,txtMonto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pago);

        //Referencias
        txtId = findViewById(R.id.txtId);
        txtEstatus = findViewById(R.id.txtEstatus);
        txtMonto = findViewById(R.id.txtMonto);

        //Coger los datos de antes
        Intent intent = getIntent();
        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            verDetalles(jsonObject.getJSONObject("response"),intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
