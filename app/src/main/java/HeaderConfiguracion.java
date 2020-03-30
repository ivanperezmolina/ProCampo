import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ivan.procampo.R;

public class HeaderConfiguracion extends AppCompatActivity {

    private TextView textoNombre;
    private TextView textoCorreo;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fbdatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);

        textoNombre = findViewById(R.id.textViewNombre);
        textoCorreo = findViewById(R.id.textViewCorreo);
        mAuth = FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        traerInfoUserCorreoFirebase();
    }

    private void traerInfoUserCorreoFirebase() {
        String id = mAuth.getCurrentUser().getUid();
        databaseReference.child("usuarios").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //Si el nodo existe; traerme nombre y correo
                    String nombre = "";
                    String correo = "";


                    nombre = dataSnapshot.child("nombre").getValue().toString();
                    correo = dataSnapshot.child("email").getValue().toString();

                    //Asigno valor
                    if (textoNombre!= null){
                        textoNombre.setText(nombre);
                    }
                    if (textoCorreo!=null){
                        textoCorreo.setText(correo);

                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
