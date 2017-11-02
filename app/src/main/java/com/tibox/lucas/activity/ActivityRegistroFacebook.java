package com.tibox.lucas.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tibox.lucas.R;

/**
 * Created by desa02 on 08/05/2017.
 */

public class ActivityRegistroFacebook extends AppCompatActivity {
    private Button btnFacebook;
    private TextView tvDetalleFb, tvTituloFb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_facebook);

        btnFacebook = (Button) findViewById( R.id.btnFacebook );
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
