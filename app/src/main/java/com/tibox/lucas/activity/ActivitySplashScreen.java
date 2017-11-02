package com.tibox.lucas.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.google.gson.Gson;
import com.tibox.lucas.R;
import com.tibox.lucas.dao.DataBaseHelper;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.Usuario;

import java.util.Timer;
import java.util.TimerTask;

public class ActivitySplashScreen extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 3000;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT ); // Set portrait orientation
        requestWindowFeature(Window.FEATURE_NO_TITLE);  // Hide title bar

        setContentView(R.layout.activity_splash_screen);

        try {
            dataBaseHelper = new DataBaseHelper( ActivitySplashScreen.this);
            dataBaseHelper.createDataBase();
            dataBaseHelper.openDataBase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Leer de Preferencias compartidas
        SharedPreferences sp = getSharedPreferences( getPackageName(), MODE_PRIVATE );
        String jsonAuntenticacion = sp.getString( ActivityLogin.ARG_AUTENTICACION_JSON, "" );
        // end

        if ( !jsonAuntenticacion.equals("") ) {

            Usuario spUsuario = new Usuario();
            spUsuario = new Gson().fromJson( jsonAuntenticacion, Usuario.class );
            new UsuarioDAO().iniciarSesion( spUsuario );

            Intent i = new Intent( ActivitySplashScreen.this, ActivityBandejaCreditos.class);
            startActivity( i );
            finish();

        }
        else {

            TimerTask task = new TimerTask() {
                @Override
                public void run() {

                    Intent mainIntent = new Intent().setClass( ActivitySplashScreen.this, ActivityLogin.class );
                    startActivity(mainIntent);
                    finish();

                }
            };

            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);

        }
    }
}
