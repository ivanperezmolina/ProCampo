package com.ivan.procampo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ivan.procampo.Config.Config;
import com.ivan.procampo.adaptadores.AdaptadorCarroCompras;
import com.ivan.procampo.adaptadores.AdaptadorProductos;
import com.ivan.procampo.modelos.Fertilizantes;
import com.ivan.procampo.modelos.Producto;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CarroCompra extends AppCompatActivity {
    ArrayList<Fertilizantes> carroCompras;

    AdaptadorCarroCompras adaptador;

    RecyclerView rvListaCarro;


    private static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    Button btnPayNow;
    TextView tvTotal;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carro_compra);

        //Iniciar servicio de Paypal
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        carroCompras = (ArrayList<Fertilizantes>) getIntent().getSerializableExtra("CarroCompras");

        rvListaCarro = findViewById(R.id.rvListaCarro);
        rvListaCarro.setLayoutManager(new LinearLayoutManager(CarroCompra.this));
        tvTotal = findViewById(R.id.tvTotal);
        String Total = tvTotal.getText().toString().trim();

        adaptador = new AdaptadorCarroCompras(CarroCompra.this,carroCompras,tvTotal);
        rvListaCarro.setAdapter(adaptador);

        btnPayNow = findViewById(R.id.btnPagar);

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                procesarPago();  
            }
        });
    }

    private void procesarPago() {
        String Total = tvTotal.getText().toString().trim();
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(Total),"EUR","Pagar a ProCampo",PayPalPayment.PAYMENT_INTENT_SALE);

        //Enviar parametros con un Intent
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation!=null){
                    try {
                        String Total = tvTotal.getText().toString().trim();
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this,DetallesPago.class).putExtra("PaymentDetails",paymentDetails).putExtra("PaymentAmount",Total));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }


            }


            }else if (resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this, "Cancelada", Toast.LENGTH_SHORT).show();
        }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalidad", Toast.LENGTH_SHORT).show();
        }
        }


        //super.onActivityResult(requestCode, resultCode, data);
    }

