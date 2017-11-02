package com.tibox.lucas.activity.dialogfragment;

import android.app.Dialog;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.tibox.lucas.R;
import com.tibox.lucas.activity.interfaces.OnClickMyDialogConfirmacionNumero;
import com.tibox.lucas.utilidades.Constantes;

/**
 * Created by desa02 on 24/09/2017.
 */

public class MyDialogFragConfirmacionNumero extends DialogFragment {
    private Button btnEnviarSms;
    private EditText etNumero;
    protected OnClickMyDialogConfirmacionNumero m_listener;
    private TextInputLayout inputlayoutNumeroConfirmarInputDialog;
    private String m_numeroConfirmado;


    public MyDialogFragConfirmacionNumero (){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View v = inflater.inflate( R.layout.confirmar_numero_input_dialog_box, container, false );
        btnEnviarSms = (Button) v.findViewById( R.id.btn_enviar_sms );
        etNumero = (EditText) v.findViewById( R.id.numero_confirmar_InputDialog );
        final TextInputLayout inputlayoutNumeroConfirmarInputDialog
                = (TextInputLayout) v.findViewById( R.id.input_layout_numero_confirmar_InputDialog );
        etNumero.setText( m_numeroConfirmado );
        etNumero.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutNumeroConfirmarInputDialog.setError(null); // hide error
            }
        });

        getDialog().show();
        btnEnviarSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( etNumero.getText().toString().trim().equals( "" )
                        || etNumero.getText().length() < Constantes.NUEVE )
                {
                    inputlayoutNumeroConfirmarInputDialog.setError( "Número incorrecto" );
                    etNumero.requestFocus();
                    return;
                }
                String nroCelularPrimero = etNumero.getText().toString().substring(0, 1);
                if ( !nroCelularPrimero.trim().equals( "9" ) )
                {
                    inputlayoutNumeroConfirmarInputDialog.setError( "Agregar 9_ _ _ _ _ _ _ _" );
                    etNumero.requestFocus();
                    return;
                }

                m_numeroConfirmado = etNumero.getText().toString();
                m_listener.onClickEnviarSms( m_numeroConfirmado );
                dismiss();



            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_numeroConfirmado =  getArguments().getString("NumeroObtenido");
    }

    public void setOnClickEnviarListener( OnClickMyDialogConfirmacionNumero listener ){
        m_listener = listener;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setTitleColor( android.graphics.Color.BLACK );
        dialog.getWindow().addFlags( WindowManager.LayoutParams.FLAG_BLUR_BEHIND );
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.WHITE));
        return dialog;
    }
}
