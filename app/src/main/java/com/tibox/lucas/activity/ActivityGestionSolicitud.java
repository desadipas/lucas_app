package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.dto.FlujoLucasDTO;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import java.io.IOException;

public class ActivityGestionSolicitud extends AppCompatActivity {

    private Button btnHistorial;

    private int m_nIdFlujoMaestro = 0;
    protected Usuario m_Sesion;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_solicitud);

        btnHistorial = (Button) findViewById( R.id.btnHistorial );
        btnHistorial.setOnClickListener( btnHistorialsetOnClickListener );

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        setTitle( "Obten tu crédito" );
        toolbar.setTitleTextColor( getResources().getColor( R.color.White ) );
        toolbar.setSubtitleTextColor( getResources().getColor( R.color.White ));
        toolbar.setSubtitle( R.string.title_activity_meta );
        toolbar.setLogo( R.drawable.logo_lucas );

        Bundle intent = getIntent().getExtras();
        if ( intent != null) {
            m_nIdFlujoMaestro = intent.getInt( "IdFlujoMaestro" );
        }

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.OCHO ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_OCHO );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //

    }

    View.OnClickListener btnHistorialsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.OCHO ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_OCHO );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnHistorial.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            Intent metafinal = new Intent( ActivityGestionSolicitud.this, ActivityBandejaCreditos.class );
            startActivity( metafinal );
            finish();

        }
    };

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir del flujo?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent solicitudFinal = new Intent( ActivityGestionSolicitud.this, ActivityBandejaCreditos.class );
                startActivity( solicitudFinal );
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
