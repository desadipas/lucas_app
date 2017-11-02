package com.tibox.lucas.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.Usuario;

public class ActivityCaducidadAutorizacion extends AppCompatActivity {

    private Button btnAceptar;
    protected Usuario m_Sesion;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caducidad_autorizacion);

        btnAceptar = (Button)findViewById( R.id.btnAceptar );
        btnAceptar.setOnClickListener( btnAceptarsetOnClickListener );

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo( R.drawable.logo_lucas ); //Establecer icono


    }

    View.OnClickListener btnAceptarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Eliminamos la Preferencia agregada
            SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
            settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();
            //end

            Intent caducidad = new Intent( ActivityCaducidadAutorizacion.this, ActivityLogin.class );
            startActivity( caducidad );
            finish();

        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                //Eliminamos la Preferencia agregada
                SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();
                //end

                Intent salir = new Intent( ActivityCaducidadAutorizacion.this, ActivityLogin.class );
                startActivity( salir );
                finish();
            }
        });
        dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
