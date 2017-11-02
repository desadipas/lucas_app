package com.tibox.lucas.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowInsets;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.activity.tabs.TabCalendario;
import com.tibox.lucas.activity.tabs.TabCredito;
import com.tibox.lucas.activity.tabs.TabOperaciones;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.connections.AppConfig;
import com.tibox.lucas.network.dto.CalendarioDTO;
import com.tibox.lucas.network.dto.DocumentosPdfDTO;
import com.tibox.lucas.network.dto.OperacionesDTO;
import com.tibox.lucas.network.response.FileResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityConsultaCredito extends AppCompatActivity implements TabCredito.IEventsListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<CalendarioDTO> m_listCalendario;
    private List<OperacionesDTO> m_listaOperaciones;
    private String m_datosCredito;
    protected Usuario m_Sesion;

    private int m_nCodCred;
    private int m_nCodAge;
    private double m_nTasaInteres;

    private static final int REQUEST_WRITE_STORAGE = 800;
    private boolean bPermisos;
    private static String TAG = ActivityConsultaCredito.class.getName();

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_credito);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        toolbar.setLogo( R.drawable.logo_lucas ); //Establecer Logo LUCAS

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter( mSectionsPagerAdapter );

        TabLayout tabLayout = (TabLayout) findViewById( R.id.tabs );
        tabLayout.setupWithViewPager( mViewPager );




        // mdipas
        /*
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent( false );
                return true;
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if( event.getAction()==MotionEvent.ACTION_DOWN ){
                    Toast.makeText(ActivityConsultaCredito.this,"ACTION_DOWN",Toast.LENGTH_SHORT).show();
                    return true;
                }else if( event.getAction()==MotionEvent.ACTION_MOVE ){
                    Toast.makeText(ActivityConsultaCredito.this,"right swipe detected",Toast.LENGTH_SHORT)
                            .show();
                    return true;
                }
                else if( event.getAction()==MotionEvent.ACTION_SCROLL ){
                    Toast.makeText(ActivityConsultaCredito.this,"ACTION_SCROLL",Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if( event.getAction()==MotionEvent.ACTION_UP ){
                    Toast.makeText(ActivityConsultaCredito.this,"ACTION_UP",Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if( event.getAction()==MotionEvent.ACTION_CANCEL ){
                    Toast.makeText(ActivityConsultaCredito.this,"ACTION_CANCEL",Toast.LENGTH_SHORT).show();
                    return true;
                }
                return true;
            }
        });


        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for(int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }
        */
        //






        //Traigo el fragment
        /*
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add( R.id.container, TabCredito.newsInstance( "CONFEEE" ) );
        transaction.commit();
        */

        int permission = ContextCompat.checkSelfPermission( this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE );
        if ( permission != PackageManager.PERMISSION_GRANTED ) {

            if ( ActivityCompat.shouldShowRequestPermissionRationale( this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE ) ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Se requiere permiso para acceder a la tarjeta SD para esta aplicación.")
                        .setTitle("SoyLucas!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ObtenerPermisosParaElMovil();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else{
                Toast.makeText(this,"Se requiere permiso para acceder a la tarjeta SD para esta aplicación.",Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else{

            bPermisos = true;
        }


        Bundle intent = getIntent().getExtras();
        m_listCalendario = new ArrayList<CalendarioDTO>();
        m_listaOperaciones = new ArrayList<OperacionesDTO>();

        if ( intent != null) {
            if ( intent.getParcelableArrayList("ListCalendario" ) != null ) {
                m_listCalendario = intent.getParcelableArrayList("ListCalendario");
            }
            if ( intent.getParcelableArrayList("ListOperaciones" ) != null ) {
                m_listaOperaciones = intent.getParcelableArrayList("ListOperaciones");
            }
            m_datosCredito = intent.getString( "datosClienteContrato" );
            m_nCodAge = intent.getInt( "nCodAge" );
            m_nCodCred = intent.getInt( "nCodCred" );
            m_nTasaInteres = intent.getDouble( "nTasa" );
        }

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.ONCE ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_ONCE );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_activity_consulta_credito, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickSolicitud( String nameButton ) {

        if ( checkWriteExternalPermission() ){

            DocumentosPdfDTO param = new DocumentosPdfDTO();
            param.setnAgencia( m_nCodAge );
            param.setnCredito( m_nCodCred );
            param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_SOLICITUD );

            ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( this, param
                    , nameButton );
            pdfAsync.execute();

        }
        else
            ObtenerPermisosParaElMovil();


    }

    @Override
    public void onClickHojaResumen1( String nameButton ) {

        if ( bPermisos ){

            DocumentosPdfDTO param = new DocumentosPdfDTO();
            param.setnAgencia( m_nCodAge );
            param.setnCredito( m_nCodCred );
            param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_HOJA_RESUMEN1 );

            ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( this, param
                    , nameButton );
            pdfAsync.execute();

        }
        else
            ObtenerPermisosParaElMovil();
    }

    @Override
    public void onClickHojaResumen2( String nameButton ) {
        if ( bPermisos ){

            DocumentosPdfDTO param = new DocumentosPdfDTO();
            param.setnAgencia( m_nCodAge );
            param.setnCredito( m_nCodCred );
            param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_HOJA_RESUMEN2);

            ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( this, param
                    , nameButton );
            pdfAsync.execute();

        }
        else
            ObtenerPermisosParaElMovil();
    }

    @Override
    public void onClickContrato( String nameButton ) {
        if ( bPermisos ){

            DocumentosPdfDTO param = new DocumentosPdfDTO();
            param.setnAgencia( m_nCodAge );
            param.setnCredito( m_nCodCred );
            param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_CONTRATO );

            ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( this, param
                    , nameButton );
            pdfAsync.execute();

        }
        else
            ObtenerPermisosParaElMovil();
    }

    @Override
    public void onClickVoucher(String nameButton) {
        if ( bPermisos ){

            DocumentosPdfDTO param = new DocumentosPdfDTO();
            param.setnAgencia( m_nCodAge );
            param.setnCredito( m_nCodCred );
            param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_VOUCHER );

            ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( this, param
                    , nameButton );
            pdfAsync.execute();

        }
        else
            ObtenerPermisosParaElMovil();
    }

    @Override
    public void onClickSeguro(String nameButton) {
        if ( bPermisos ){

            DocumentosPdfDTO param = new DocumentosPdfDTO();
            param.setnAgencia( m_nCodAge );
            param.setnCredito( m_nCodCred );
            param.setnTipoDoc( Constantes.DOC_CRED_ONLINE_SEGURO );

            ObtenerPDFAsync pdfAsync = new ObtenerPDFAsync( this, param
                    , nameButton );
            pdfAsync.execute();

        }
        else
            ObtenerPermisosParaElMovil();
    }


    // DELETE CLASS PlaceholderFragment donde aqui.


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter( FragmentManager fm ) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //retorna los tabs creado
            switch ( position ){
                case 0:
                    TabCredito tab1 =  new TabCredito();
                    return tab1.newsInstance( "DESDE ACTIVITY CREDITO", m_datosCredito, m_nCodAge, m_nCodCred, m_nTasaInteres );
                case 1:
                    TabCalendario tab2 =  new TabCalendario();
                    return tab2.newsInstance( "DESDE ACTIVITY CALENDARIO", m_listCalendario );
                case 2:
                    TabOperaciones tab3 =  new TabOperaciones();
                    return tab3.newsInstance( "DESDE ACTIVITY OPERACIONES", m_listaOperaciones );
                default:
                    return null;
            }

            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "CREDITO";
                case 1:
                    return "CALENDARIO";
                case 2:
                    return "OPERACIONES";
            }
            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem( container, position );
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

        @Override
        public float getPageWidth(int position) {
            return super.getPageWidth(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }


    }


    private boolean ObtenerPermisosParaElMovil() {
        bPermisos = false;
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions( this,new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, //Escribir en memoria del telefono
                    REQUEST_WRITE_STORAGE );
        }
        else
            bPermisos = true;

        return bPermisos;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ( requestCode == REQUEST_WRITE_STORAGE ) {
            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                bPermisos = true;
            }
        }
    }

    public class ObtenerPDFAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private File m_File = null;
        private String m_namePdf;
        private DocumentosPdfDTO m_param;
        private String m_Mensaje;
        private FileResponse m_FileResponse;

        public ObtenerPDFAsync( Context context, DocumentosPdfDTO param, String namePdf ){
            m_param = param;
            m_namePdf = namePdf;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "obteniendo PDF...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                int nResp = 0;
                m_namePdf = m_namePdf + "_" + m_param.getnAgencia() + "-" + m_param.getnCredito();
                //m_File = m_webApi.ObtenerImagePDF( m_param, m_namePdf, m_Sesion.getToken() );
                m_FileResponse = m_webApi.ObtenerImagePDF( m_param, m_namePdf, m_Sesion.getToken() );

                if ( !m_FileResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                }

                /*
                if ( m_File != null ) {
                    resp = RESULT_OK;
                }
                else{
                    m_Mensaje = "¡Documento aún no generado, volver a intentar!";
                    resp = RESULT_FALSE;
                }
                */
                return resp;
            }
            catch (IOException ex) {
                return "Ocurrió un error de conexión con el servicio.";
            }
            catch (Exception ex) {
                return "Ocurrió un error al consultar el servicio.";
            }
        }

        @Override
        protected void onPostExecute(String mensaje) {
            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();
                showPdf( m_namePdf );
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                //Toast.makeText(m_context, m_Mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if ( m_FileResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder( m_context );
                    builder.setMessage( Constantes.Mensaje_Sesion_Caducado )
                            .setTitle("Lucas!")
                            .setCancelable(false)
                            .setNeutralButton("Aceptar",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();

                                            //Eliminamos la Preferencia agregada
                                            SharedPreferences.Editor settings = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
                                            settings.remove( ActivityLogin.ARG_AUTENTICACION_JSON ).commit();
                                            //end

                                            Intent solicitudFinal = new Intent( m_context, ActivityLogin.class );
                                            startActivity( solicitudFinal );
                                            finish();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    */
                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public void showPdf( String namePdf ){
        File file = new File(  AppConfig.directorioPdf, namePdf + ".pdf");
        Intent intent = new Intent();
        intent.setAction( Intent.ACTION_VIEW );
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf" ); startActivity(intent);
    }

    private boolean checkWriteExternalPermission() {
        int res = ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE  );
        return ( res == PackageManager.PERMISSION_GRANTED );
    }

    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent solicitudFinal = new Intent( ActivityConsultaCredito.this, ActivityBandejaCreditos.class );
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
