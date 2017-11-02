package com.tibox.lucas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tibox.lucas.R;

/**
 * Created by desa02 on 08/05/2017.
 */

public class ActivityRegistroCorreo extends AppCompatActivity {
    private Button btnGmail, btnYahoo, btnOutlook;
    private TextView tvTitulo, tvDetalle, tvVinculoCuentas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_correo);

        btnGmail = (Button) findViewById( R.id.btnGmail );
        tvVinculoCuentas = (TextView) findViewById( R.id.tvVinculoCuentas );
        tvVinculoCuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityRegistroCorreo.this, "CLICK", Toast.LENGTH_SHORT).show();
            }
        });
        btnGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( ActivityRegistroCorreo.this,ActivityRegistroFacebook.class );
                startActivity(intent);

            }
        });


    }
}
