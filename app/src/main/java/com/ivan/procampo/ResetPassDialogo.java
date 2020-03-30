package com.ivan.procampo;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassDialogo extends DialogFragment {
    private EditText mEditTextEmail;


    private String email = "";

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(getActivity());

        ViewGroup container;



        //Creación del dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialogo_reset_password,null);

                //Obtener referencia del layoit que quiero cargar
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //Cargamos el layout


        builder.setView(view);



          mEditTextEmail  =  view.findViewById(R.id.correoReset);

        builder.setTitle(R.string.reset_pass)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        email = mEditTextEmail.getText().toString();
                        if(!email.isEmpty()){
                            mDialog.setMessage("Espere un momento mientras mandado su correo");
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                            resetPassword();

                            Context context = getActivity();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getActivity(), R.string.ok_reset_pass, duration);
                            toast.show();
                            dialog.dismiss();

                        } else{
                            Context context = getActivity();
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(context, R.string.campo_vacio, duration);
                            toast.show();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        return builder.create();

    }

    private void resetPassword() {
        mAuth.setLanguageCode("es");
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Context context = getArguments();
                    //int duration = Toast.LENGTH_SHORT;
                    //Toast toast = Toast.makeText(context, R.string.ok_reset_pass, duration);
                    //toast.show();
                    //View v=getView();
                    //Snackbar.make(v, getResources().getText(R.string.ivan), Snackbar.LENGTH_LONG).show();
                    mDialog.dismiss();

                }else{
                    Context context = getContext();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, "No sa podio", duration);
                    toast.show();

                }


            }
        });

    }


}
