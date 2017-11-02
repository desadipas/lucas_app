package com.tibox.lucas.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import com.tibox.lucas.R;
import com.tibox.lucas.activity.dialogfragment.MyDialogFragConfirmacionNumero;
import com.tibox.lucas.activity.interfaces.OnClickMyDialogConfirmacionNumero;
import com.tibox.lucas.activity.tabs.DatosPersonaTabDatosEmpleo;
import com.tibox.lucas.activity.tabs.DatosPersonaTabDireccion;
import com.tibox.lucas.activity.tabs.DatosPersonaTabDireccionEmpleo;
import com.tibox.lucas.activity.tabs.DatosPersonaTabGeneral;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.dao.ZonasDAO;
import com.tibox.lucas.entidad.CatalagoCodigos;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.entidad.Zonas;
import com.tibox.lucas.lenddo.LenddoActivity;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.CatalagoCodigosDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosEntrada.User;
import com.tibox.lucas.network.dto.DatosSalida.EvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.dto.DatosSalida.PreEvaluacionResp;
import com.tibox.lucas.network.dto.DatosSalida.TokenItems;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.dto.PersonaRespDTO;
import com.tibox.lucas.network.dto.UsuarioDTO;
import com.tibox.lucas.network.dto.ZonasDTO;
import com.tibox.lucas.network.response.PersonaRespResponse;
import com.tibox.lucas.network.response.PersonaResponse;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.response.UserListResponse;
import com.tibox.lucas.network.response.UserResponse;
import com.tibox.lucas.network.response.VarnegocioResponse;
import com.tibox.lucas.network.response.ZonasResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ActivityDatosPersona extends AppCompatActivity implements DatosPersonaTabGeneral.IEventsListener,
        DatosPersonaTabDireccion.IEventsListener, DatosPersonaTabDatosEmpleo.IEventsListener, DatosPersonaTabDireccionEmpleo.IEventsListener{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private boolean m_EditarDatos = false;
    private String m_Access_token = "";
    private int m_nCodPers = 0;
    private int m_nIdFlujoMaestro = 0;
    private Persona m_PersonaForm;
    private int m_nCodVerificacionMovil = 0;
    private String m_Numero_Celular = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_persona);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle( "Datos Personales" );
        toolbar.setLogo( R.drawable.logo_lucas );

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;


        //mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager() );
        mViewPager = (ViewPager) findViewById( R.id.container );
        tabLayout = (TabLayout) findViewById( R.id.tabs );

        //mViewPager.setAdapter( mSectionsPagerAdapter );
        //tabLayout.setupWithViewPager( mViewPager );

        /*
        if ( savedInstanceState == null ) {
            // During initial setup, plug in the details fragment.
            DatosPersonaTabGeneral details = new DatosPersonaTabGeneral();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add( android.R.id.content, details.newsInstance( m_EditarDatos, m_Persona ) );
            ft.commit();
        }
        */

        //m_daDatosPersonaTabGeneral.setControladorPadre( ActivityDatosPersona.this );
        //m_daDatosPersonaTabDireccion.setControladorPadre( this );
        //m_daDatosPersonaTabDatosEmpleo.setControladorPadre( this );
        //m_daDatosPersonaTabDireccionEmpleo.setControladorPadre( this );


        Bundle intent = getIntent().getExtras();
        if ( intent != null) {
            if ( intent.getString("Documento" ) != null ){
                m_EditarDatos = true;
                m_Access_token = m_Sesion.getToken();
            }
        }

        if ( m_EditarDatos ){
            ConsultaDocumentoAsync consultaDocumentoAsync = new ConsultaDocumentoAsync( this );
            consultaDocumentoAsync.execute();
        }
        else {

            UsuarioDTO usuarioTibox = new UsuarioDTO();
            usuarioTibox.setUsername( "tibox@tibox.com.pe" );
            usuarioTibox.setPassword( "TiboxWebApi" );

            ObtenerTokenAsync obtenerTokenAsync = new ObtenerTokenAsync( this, usuarioTibox  );
            obtenerTokenAsync.execute();

            //mViewPager.setAdapter( mSectionsPagerAdapter );
            //tabLayout.setupWithViewPager( mViewPager );
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public void onClickAtras() {
        setResult( RESULT_CANCELED );
        finish();
    }

    @Override
    public void onClickContinuar() {
        mViewPager.setCurrentItem( 1 );
    }

    @Override
    public void onClickAtrasDireccion() {
        mViewPager.setCurrentItem( 0 );
    }

    @Override
    public void onClickContinuarDireccion() {
        mViewPager.setCurrentItem( 2 );
    }

    @Override
    public void onClickAtrasDatosEmpleo() {
        mViewPager.setCurrentItem( 1 );
    }

    @Override
    public void onClickContinuarDatosEmpleo() {
        mViewPager.setCurrentItem( 3 );
    }

    @Override
    public void onClickAtrasLugarEmpleo() {
        mViewPager.setCurrentItem( 2 );
    }

    @Override
    public void onClickContinuarLugarEmpleo( String codigoConfirmar ) {

        //REGISTRAR
        if ( m_EditarDatos )
        {
            int nCodigoVerifiacion = 0;
            nCodigoVerifiacion = Integer.parseInt( codigoConfirmar );
            if ( nCodigoVerifiacion != m_nCodVerificacionMovil ){
                Toast.makeText( this, "Código errado.", Toast.LENGTH_SHORT ).show();
                return;
            }
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        if ( m_EditarDatos )
            dialog.setMessage("¿Esta seguro de actualizar sus datos?");
        else
            dialog.setMessage("¿Esta seguro de registrar los datos?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Persona personaEnviar = new Persona();
                Persona general = new Persona();
                Persona direccion = new Persona();
                Persona datosEmpleo = new Persona();
                Persona direccionEmpelo = new Persona();

                general = m_daDatosPersonaTabGeneral.obtenerEntidad();
                direccion = m_daDatosPersonaTabDireccion.obtenerEntidad();
                datosEmpleo = m_daDatosPersonaTabDatosEmpleo.obtenerEntidad();
                direccionEmpelo = m_daDatosPersonaTabDireccionEmpleo.obtenerEntidad();

                personaEnviar.setnTipoDoc( Constantes.UNO );
                personaEnviar.setnProd( Constantes.PRODUCTO );
                personaEnviar.setnSubProd( Constantes.SUB_PRODUCTO );
                if ( m_EditarDatos ) {
                    personaEnviar.setnCodAge( m_Sesion.getAgencia() );
                    personaEnviar.setnCodPers( m_Sesion.getCodPers() );
                    personaEnviar.setcTextoSms( "" );
                    personaEnviar.setcTextoEmail( "" );
                    personaEnviar.setcTituloEmail( "" );
                }
                else{
                    personaEnviar.setnCodAge( Constantes.CINCO );
                    personaEnviar.setnCodPers( Constantes.CERO );
                }

                //Agregando de los tabs
                //1
                personaEnviar.setnNroDoc( general.getnNroDoc() );
                personaEnviar.setnCodigoVerificador( general.getnCodigoVerificador() );
                personaEnviar.setcNombres( general.getcNombres() );
                personaEnviar.setcApePat( general.getcApePat() );
                personaEnviar.setcApeMat( general.getcApeMat() );
                personaEnviar.setnSexo( general.getnSexo() );
                personaEnviar.setcCelular( general.getcCelular() );
                personaEnviar.setcEmail( general.getcEmail() );
                personaEnviar.setcConfirmaEmail( general.getcConfirmaEmail() );
                personaEnviar.setcTelefono( general.getcTelefono() );
                personaEnviar.setdFechaNacimiento( general.getdFechaNacimiento() );
                personaEnviar.setbCargoPublico( general.getbCargoPublico() );
                personaEnviar.setnEstadoCivil( general.getnEstadoCivil() );
                personaEnviar.setcDniConyuge( general.getcDniConyuge() );
                personaEnviar.setcNomConyuge( general.getcNomConyuge() );
                personaEnviar.setcApeConyuge( general.getcApeConyuge() );
                //2
                personaEnviar.setcDepartamento( direccion.getcDepartamento() );
                personaEnviar.setcProvincia( direccion.getcProvincia() );
                personaEnviar.setcDistrito( direccion.getcDistrito() );
                personaEnviar.setcCodZona( direccion.getcCodZona() );
                personaEnviar.setnDirTipo2( direccion.getnDirTipo2() );
                personaEnviar.setnDirTipo3( direccion.getnDirTipo3() );
                personaEnviar.setcDirValor2( direccion.getcDirValor2() );
                personaEnviar.setcDirValor3( direccion.getcDirValor3() );
                personaEnviar.setnDirTipo1( direccion.getnDirTipo1() );
                personaEnviar.setcDirValor1( direccion.getcDirValor1() );
                personaEnviar.setnTipoResidencia( direccion.getnTipoResidencia() );
                //3
                personaEnviar.setnCUUI( datosEmpleo.getnCUUI() );
                personaEnviar.setnSitLab( datosEmpleo.getnSitLab() );
                personaEnviar.setnProfes( datosEmpleo.getnProfes() );
                personaEnviar.setnTipoEmp( datosEmpleo.getnTipoEmp() );
                personaEnviar.setcRuc( datosEmpleo.getcRuc() );
                personaEnviar.setcNomEmpresa( datosEmpleo.getcNomEmpresa() );
                personaEnviar.setdFecIngrLab( datosEmpleo.getdFecIngrLab() );
                personaEnviar.setcTelfEmpleo( datosEmpleo.getcTelfEmpleo() );
                personaEnviar.setnIngresoDeclado( datosEmpleo.getnIngresoDeclado() );
                //4
                personaEnviar.setcDepartamentoEmpleo( direccionEmpelo.getcDepartamentoEmpleo() );
                personaEnviar.setcProvinciaEmpleo( direccionEmpelo.getcProvinciaEmpleo() );
                personaEnviar.setcDistritoEmpleo( direccionEmpelo.getcDistritoEmpleo() );
                personaEnviar.setcCodZonaEmpleo( direccionEmpelo.getcCodZonaEmpleo() );
                personaEnviar.setcDirValor2Empleo( direccionEmpelo.getcDirValor2Empleo() );
                personaEnviar.setcDirValor3Empleo( direccionEmpelo.getcDirValor3Empleo() );
                personaEnviar.setnDirTipo2Empleo( direccionEmpelo.getnDirTipo2Empleo() );
                personaEnviar.setnDirTipo3Empleo( direccionEmpelo.getnDirTipo3Empleo() );
                personaEnviar.setnDirTipo1Empleo( direccionEmpelo.getnDirTipo1Empleo() );
                personaEnviar.setcDirEmpleo( direccionEmpelo.getcDirEmpleo() );
                personaEnviar.setcDirValor1Empleo( direccionEmpelo.getcDirValor1Empleo() );




                if ( m_EditarDatos ) {
                    ActualizarPersonaAsyns actualizarPersonaAsyns = new ActualizarPersonaAsyns( ActivityDatosPersona.this, personaEnviar);
                    actualizarPersonaAsyns.execute();
                }
                else {

                    personaEnviar.setcTextoSms( Constantes.Mensaje_Texto_Sms );
                    personaEnviar.setcTituloEmail( "Bienvenido a SoyLucas " + personaEnviar.getcNombres() );
                    personaEnviar.setcTextoEmail( emailCuerpo( "BIENVENIDO (A)", personaEnviar.getcNombres(), "¡Gracias por registrarte!", "Para acceder al portal de SoyLucas, ingresa con las siguientes credenciales:", "Usuario: "
                            + personaEnviar.getcEmail(), "Contraseña: " + personaEnviar.getnNroDoc(), "Nota: Por seguridad debes cambiar tu contraseña." ) );

                    VerificarDocumentoAsyns verificarDocumentoAsyns = new VerificarDocumentoAsyns( ActivityDatosPersona.this, personaEnviar );
                    verificarDocumentoAsyns.execute();

                }


            }
        });
        dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog.show();

    }

    @Override
    public void onClickSolicitarCodigoConfirmacion() {


        Bundle bundle = new Bundle();
        bundle.putString("NumeroObtenido", m_daDatosPersonaTabGeneral.getCelularPersona() );
        FragmentManager fm = getSupportFragmentManager();
        MyDialogFragConfirmacionNumero myDialogFrag = new MyDialogFragConfirmacionNumero();
        myDialogFrag.setArguments(bundle);
        myDialogFrag.setCancelable( true );
        myDialogFrag.show(fm,"Diag");
        myDialogFrag.setOnClickEnviarListener(new OnClickMyDialogConfirmacionNumero() {
            @Override
            public void onClickEnviarSms(String numeroConfirmado) {
                m_Numero_Celular = numeroConfirmado;


                if ( m_nCodVerificacionMovil == Constantes.CERO ){
                    ObtenerVerificacion();
                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityDatosPersona.this );
                    dialog.setTitle("Aviso");
                    dialog.setMessage("¿Esta seguro que desea generar nuevo código?");
                    dialog.setCancelable(false);
                    dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ObtenerVerificacion();
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
        });




        /*
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder( this );
        final View mViews = getLayoutInflater().inflate( R.layout.confirmar_numero_input_dialog_box, null);
        final TextInputLayout inputlayoutNumeroConfirmarInputDialog
                = (TextInputLayout) mViews.findViewById( R.id.input_layout_numero_confirmar_InputDialog );
        final EditText etNumeroConfirmarInputDialogEditText
                = (EditText) mViews.findViewById( R.id.numero_confirmar_InputDialog );
        final Button btnEnviarSms
                = (Button) mViews.findViewById( R.id.btn_enviar_sms );
        //traer el numero de celular ingresado
        etNumeroConfirmarInputDialogEditText.setText( m_daDatosPersonaTabGeneral.getCelularPersona() );

        //elimnando el error
        etNumeroConfirmarInputDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutNumeroConfirmarInputDialog.setError(null);
            }
        });
        btnEnviarSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( etNumeroConfirmarInputDialogEditText.getText().toString().trim().equals( "" )
                        || etNumeroConfirmarInputDialogEditText.getText().length() < Constantes.NUEVE )
                {
                    inputlayoutNumeroConfirmarInputDialog.setError( "Número incorrecto" );
                    etNumeroConfirmarInputDialogEditText.requestFocus();
                    return;
                }
                String nroCelularPrimero = etNumeroConfirmarInputDialogEditText.getText().toString().substring(0, 1);
                if ( !nroCelularPrimero.trim().equals( "9" ) )
                {
                    inputlayoutNumeroConfirmarInputDialog.setError( "Agregar 9_ _ _ _ _ _ _ _" );
                    etNumeroConfirmarInputDialogEditText.requestFocus();
                    return;
                }

                m_Numero_Celular = etNumeroConfirmarInputDialogEditText.getText().toString();
                //mBuilder.setCancelable(false);

                if ( m_nCodVerificacionMovil == Constantes.CERO ){
                    ObtenerVerificacion();
                }
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityDatosPersona.this );
                    dialog.setTitle("Aviso");
                    dialog.setMessage("¿Esta seguro que desea generar nuevo código?");
                    dialog.setCancelable(false);
                    dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ObtenerVerificacion();
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
        });
        mBuilder.setView( mViews );
        AlertDialog dialog = mBuilder.create();
        dialog.show();
        */





        /*
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from( this );
        final View mView = layoutInflaterAndroid.inflate( R.layout.confirmar_numero_input_dialog_box, null);
        final AlertDialog.Builder alertDialogBuilderNumeroConfirmarInput = new AlertDialog.Builder( this );
        alertDialogBuilderNumeroConfirmarInput.setView( mView );
        final TextInputLayout inputlayoutNumeroConfirmarInputDialog= (TextInputLayout) mView.findViewById( R.id.input_layout_numero_confirmar_InputDialog );
        final EditText etNumeroConfirmarInputDialogEditText= (EditText) mView.findViewById( R.id.numero_confirmar_InputDialog );

        //traer el numero de celular ingresado
        etNumeroConfirmarInputDialogEditText.setText( m_daDatosPersonaTabGeneral.getCelularPersona() );

        //elimnando el error
        etNumeroConfirmarInputDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutNumeroConfirmarInputDialog.setError(null);
            }
        });

        alertDialogBuilderNumeroConfirmarInput
                .setCancelable(false)
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        if ( etNumeroConfirmarInputDialogEditText.getText().toString().trim().equals( "" )
                                || etNumeroConfirmarInputDialogEditText.getText().length() < Constantes.NUEVE )
                        {
                            inputlayoutNumeroConfirmarInputDialog.setError( "Número incorrecto" );
                            //alertDialogBuilderNumeroConfirmarInput.show();
                            etNumeroConfirmarInputDialogEditText.requestFocus();

                            return;
                        }
                        String nroCelularPrimero = etNumeroConfirmarInputDialogEditText.getText().toString().substring(0, 1);
                        if ( !nroCelularPrimero.trim().equals( "9" ) )
                        {
                            inputlayoutNumeroConfirmarInputDialog.setError( "Agregar 9_ _ _ _ _ _ _ _" );
                            //alertDialogBuilderNumeroConfirmarInput.show();
                            etNumeroConfirmarInputDialogEditText.requestFocus();

                            return;
                        }

                        m_Numero_Celular = etNumeroConfirmarInputDialogEditText.getText().toString();

                        if ( m_nCodVerificacionMovil == Constantes.CERO ){
                            ObtenerVerificacion();
                        }
                        else {
                            AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityDatosPersona.this );
                            dialog.setTitle("Aviso");
                            dialog.setMessage("¿Esta seguro que desea generar nuevo código?");
                            dialog.setCancelable(false);
                            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ObtenerVerificacion();
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
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderNumeroConfirmarInput.create();
        alertDialogAndroid.show();
        */





        /*
        if ( m_nCodVerificacionMovil == Constantes.CERO ){
            ObtenerVerificacion();
        }
        else {
            AlertDialog.Builder dialog = new AlertDialog.Builder( this );
            dialog.setTitle("Aviso");
            dialog.setMessage("¿Esta seguro que desea generar nuevo código?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ObtenerVerificacion();
                }
            });
            dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
        */


    }

    public static class PlaceholderFragment extends Fragment {
        /*
        private static final String ARG_SECTION_NUMBER = "section_number";
        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity_datos_persona, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
        */
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> m_fragmentList = new ArrayList<Fragment>();

        public SectionsPagerAdapter( FragmentManager fm , List<Fragment> fragmentList )
        {
            super(fm);
            m_fragmentList = fragmentList;
        }
        @Override
        public Fragment getItem(int position) {
            return m_fragmentList.get(position);
        }
        @Override
        public int getCount() {
            return 4;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "GENERAL";
                case 1:
                    return "DIRECCION";
                case 2:
                    return "DATOS EMPLEO";
                case 3:
                    return "LUGAR EMPLEO";
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

    public class ConsultaDocumentoAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private PersonaResponse m_PersonaResponse;

        public ConsultaDocumentoAsync( Context context ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Obteniendo datos...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_PersonaForm = new Persona();
                Persona datos = new Persona();

                datos.setnCodPers( m_Sesion.getCodPers() );
                datos.setnNroDoc( m_Sesion.getRolDescripcion() ); //Documento
                datos.setcEmail( m_Sesion.getUsuario() );

                m_PersonaResponse = m_webApi.obtenerPersonaDatos( datos, m_Sesion.getToken() );
                if( !m_PersonaResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    m_PersonaForm = m_PersonaResponse.getM_data();
                }
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
            if ( mensaje.equals( RESULT_OK ) ) {
                pd.dismiss();

                List<Fragment> fragmentList = new ArrayList<Fragment>();

                m_daDatosPersonaTabGeneral = m_daDatosPersonaTabGeneral.newsInstance( m_EditarDatos, m_PersonaForm );
                m_daDatosPersonaTabDireccion = m_daDatosPersonaTabDireccion.newsInstance( m_EditarDatos, m_PersonaForm );
                m_daDatosPersonaTabDatosEmpleo = m_daDatosPersonaTabDatosEmpleo.newsInstance( m_EditarDatos, m_PersonaForm );
                m_daDatosPersonaTabDireccionEmpleo = m_daDatosPersonaTabDireccionEmpleo.newsInstance( m_EditarDatos, m_PersonaForm );

                fragmentList.add( m_daDatosPersonaTabGeneral );
                fragmentList.add( m_daDatosPersonaTabDireccion );
                fragmentList.add( m_daDatosPersonaTabDatosEmpleo );
                fragmentList.add( m_daDatosPersonaTabDireccionEmpleo );

                mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager(), fragmentList );

                mViewPager.setAdapter( mSectionsPagerAdapter );
                tabLayout.setupWithViewPager( mViewPager );

                m_daDatosPersonaTabGeneral.setControlador( ActivityDatosPersona.this );
                m_daDatosPersonaTabDireccion.setControlador( ActivityDatosPersona.this );
                m_daDatosPersonaTabDatosEmpleo.setControlador( ActivityDatosPersona.this );
                m_daDatosPersonaTabDireccionEmpleo.setControlador( ActivityDatosPersona.this );


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
                            return true;
                        }else if( event.getAction()==MotionEvent.ACTION_MOVE ){
                            return true;
                        }
                        else if( event.getAction()==MotionEvent.ACTION_SCROLL ){
                            return true;
                        }
                        else if( event.getAction()==MotionEvent.ACTION_UP ){
                            return true;
                        }
                        else if( event.getAction()==MotionEvent.ACTION_CANCEL ){
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




            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();

                if ( m_PersonaResponse.getM_response().equals( Constantes.No_Autorizado ) ){
                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();
                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }
    }

    public class ActualizarPersonaAsyns extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private PersonaRespResponse m_PersonaRespResponse;

        public ActualizarPersonaAsyns( Context context, Persona persona ){
            m_PersonaCreditoDTO = persona;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando datos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                PersonaRespDTO dto = new PersonaRespDTO();
                m_PersonaRespResponse = m_webApi.actualizarPersonaCredito( m_PersonaCreditoDTO, m_Sesion.getToken() );
                if( !m_PersonaRespResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_PersonaRespResponse.getM_data() != null ){
                        if ( m_PersonaRespResponse.getM_data().getnCodPers() > Constantes.CERO ){ //Solo registra persona o edita persona.
                            m_nCodPers = m_PersonaRespResponse.getM_data().getnCodPers();
                            resp = RESULT_OK;
                        }
                        else {
                            m_mensajeAlerta = m_PersonaRespResponse.getM_data().getcMensaje();
                            resp = RESULT_FALSE;
                        }

                    }
                    else {
                        m_mensajeAlerta = "¡Error en el proceso!";
                        resp = RESULT_FALSE;
                    }
                }

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

                Toast.makeText(m_context, "¡Se realizo el proceso de datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                Intent intent = new Intent ( m_context, ActivityBandejaCreditos.class );
                startActivity( intent );
                finish();

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();

                if ( m_PersonaRespResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private String m_mensajeAlerta;
        private Persona m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    public class RegistrarPersonaAsyns extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private PersonaRespResponse m_PersonaRespResponse;

        public RegistrarPersonaAsyns(Context context, Persona persona ){
            m_PersonaCreditoDTO = persona;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Registrando persona ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                PersonaRespDTO dto = new PersonaRespDTO();

                //dto = m_webApi.registrarPersonaCredito( m_PersonaCreditoDTO, m_Access_token );
                m_PersonaRespResponse = m_webApi.registrarPersonaCredito( m_PersonaCreditoDTO, m_Access_token );
                if( !m_PersonaRespResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_PersonaRespResponse.getM_data() != null ){
                        if ( m_PersonaRespResponse.getM_data().getnCodPers() > Constantes.CERO ){ //Solo registra persona o edita persona.
                            m_nCodPers = m_PersonaRespResponse.getM_data().getnCodPers();
                            resp = RESULT_OK;
                        }
                        else {
                            m_mensajeAlerta = m_PersonaRespResponse.getM_data().getcMensaje();
                            resp = RESULT_FALSE;
                        }

                    }
                    else {
                        m_mensajeAlerta = "¡Error en el proceso!";
                        resp = RESULT_FALSE;
                    }
                }

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

                Toast.makeText(m_context, "¡Se realizo el proceso de datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                // REGISTRO NUEVA PERSONA
                //Agregar sesion creada por la nueva persona

                UsuarioDTO usuarioDTO = new UsuarioDTO();
                usuarioDTO.setUsername( m_PersonaCreditoDTO.getcEmail() );
                usuarioDTO.setPassword( m_PersonaCreditoDTO.getnNroDoc() );

                ObtenerTokenNuevoAsync obtenerTokenAsync =
                        new ObtenerTokenNuevoAsync( m_context, usuarioDTO, m_PersonaCreditoDTO );
                obtenerTokenAsync.execute();


            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();

                if ( m_PersonaRespResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private String m_mensajeAlerta;
        private Persona m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    public class ObtenerTokenAsync extends AsyncTask<Void, Void, String>{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private TokenItems m_TokenItems;
        private UsuarioDTO m_UsuarioDTO;

        public ObtenerTokenAsync( Context context, UsuarioDTO usuario ){
            m_UsuarioDTO = usuario;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Autenticando...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_TokenItems = new TokenItems();
                m_TokenItems = m_webApi.ObtenerToken( m_UsuarioDTO );

                if ( m_TokenItems != null )
                    resp = RESULT_OK;
                else
                    resp = RESULT_FALSE;

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
            if ( mensaje.equals( RESULT_OK ) ) {
                pd.dismiss();

                m_Access_token = m_TokenItems.getAccess_token();

                ActualizarCatalogoInternoAsync actualizarCatalogoZonasAsync =
                        new ActualizarCatalogoInternoAsync( ActivityDatosPersona.this, 0 );
                actualizarCatalogoZonasAsync.execute();


            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class ActualizarCatalogoInternoAsync extends AsyncTask<Void, Void, String>{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private AppCreditoswebApi m_webApi;
        private VarnegocioResponse m_VarnegocioResponse;

        public ActualizarCatalogoInternoAsync ( Context context, int nuevaVersion ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {

            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Cargando...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                //START
                ArrayList<CatalagoCodigos> catalagoSize = new ArrayList<>();
                catalagoSize = new CatalagoCodigosDAO().listCatalagoCodigos();
                if ( catalagoSize.size() <= Constantes.SEIS ) {

                    List<CatalagoCodigosDTO> m_listcatalogo = new ArrayList<>();
                    for (int i = 0; i <= Constantes.SIETE; i++) {
                        m_listcatalogo = new ArrayList<>();
                        int Codigo = 0;
                        if (i == 0)
                            Codigo = Constantes.TIPO_ACT_ECO;
                        else if (i == 1)
                            Codigo = Constantes.TIPO_DIRECCION;
                        else if (i == 2)
                            Codigo = Constantes.TIPO_SEXO;
                        else if (i == 3)
                            Codigo = Constantes.TIPO_ESTADO_CIVIL;
                        else if (i == 4)
                            Codigo = Constantes.TIPO_EST_LABORAL;
                        else if (i == 5)
                            Codigo = Constantes.TIPO_PROFESION;
                        else if (i == 6)
                            Codigo = Constantes.TIPO_EMPLEO;
                        else
                            Codigo = Constantes.TIPO_RESIDENCIA;

                        m_listcatalogo = m_webApi.obtenerCatalago(Codigo, m_Access_token);
                        if (m_listcatalogo != null) {
                            if (m_listcatalogo.size() > Constantes.CERO) {

                                new CatalagoCodigosDAO().LimpiarCatalogoCodigosxID(Codigo); // eliminando

                                for (CatalagoCodigosDTO dto : m_listcatalogo) {

                                    CatalagoCodigos catalagoCodigos = new CatalagoCodigos();
                                    catalagoCodigos.setcNomCod(dto.getcNomCod());
                                    catalagoCodigos.setnCodigo(Codigo);
                                    catalagoCodigos.setnValor(Integer.valueOf(dto.getnValor()));
                                    new CatalagoCodigosDAO().insertar(catalagoCodigos);
                                }
                                resp = RESULT_OK;
                            } else
                                resp = RESULT_FALSE;
                        } else
                            resp = RESULT_FALSE;
                    }
                }
                else
                    resp = RESULT_OK;
                //END

                //START
                ArrayList<Zonas> zonasSize = new ArrayList<>();
                zonasSize = new ZonasDAO().listaZonas();
                if ( zonasSize.size() == Constantes.CERO ) {
                    ZonasResponse m_ZonasResponse;
                    String CodigoSuperior = "";
                    String CodigoInferior = "";

                    //INSERTANDO PROVINCIAS
                    m_ZonasResponse = m_webApi.obtenerZonas(CodigoInferior, CodigoSuperior, Constantes.UNO, m_Access_token);
                    if (!m_ZonasResponse.isM_success()) {
                        resp = RESULT_FALSE;
                    } else {
                        resp = RESULT_OK;
                        if (m_ZonasResponse.getM_listdata() != null) {
                            if (m_ZonasResponse.getM_listdata().size() > Constantes.CERO) {

                                new ZonasDAO().LimpiarZonas(); //eliminado zonas

                                for (ZonasDTO departamento : m_ZonasResponse.getM_listdata()) {

                                    //INSERTANDO DEPARTAMENTOS

                                    Zonas zonas = new Zonas();
                                    zonas.setnCodigo(Integer.parseInt(departamento.getCODIGO()));
                                    zonas.setcDescripcion(departamento.getDESCRIPCION());
                                    zonas.setnNivel(Constantes.UNO);
                                    zonas.setnTipo(Constantes.UNO);
                                    new ZonasDAO().insertar(zonas);


                                    CodigoSuperior = String.valueOf(zonas.getnCodigo());
                                    if (zonas.getnCodigo() < Constantes.DIEZ)
                                        CodigoSuperior = "0" + CodigoSuperior;
                                    m_ZonasResponse = m_webApi.obtenerZonas(CodigoInferior, CodigoSuperior, Constantes.DOS, m_Access_token);

                                    if (m_ZonasResponse.getM_listdata() != null) {
                                        if (m_ZonasResponse.getM_listdata().size() > Constantes.CERO) {
                                            for (ZonasDTO provincia : m_ZonasResponse.getM_listdata()) {

                                                //INSERTANDO PROVINCIAS

                                                zonas = new Zonas();
                                                zonas.setnCodigo(Integer.parseInt(provincia.getCODIGO()));
                                                zonas.setcDescripcion(provincia.getDESCRIPCION());
                                                zonas.setnNivel(Constantes.DOS);
                                                //zonas.setnTipo( Constantes.DOS );
                                                zonas.setnTipo(Integer.parseInt(departamento.getCODIGO()));
                                                new ZonasDAO().insertar(zonas);


                                                CodigoInferior = String.valueOf(zonas.getnCodigo());
                                                if (zonas.getnCodigo() < Constantes.DIEZ)
                                                    CodigoInferior = "0" + CodigoInferior;
                                                m_ZonasResponse = m_webApi.obtenerZonas(CodigoInferior, CodigoSuperior, Constantes.TRES, m_Access_token);
                                                if (m_ZonasResponse.getM_listdata() != null) {
                                                    if (m_ZonasResponse.getM_listdata().size() > Constantes.CERO) {
                                                        for (ZonasDTO distrito : m_ZonasResponse.getM_listdata()) {

                                                            //INSERTANDO DISTRITOS

                                                            zonas = new Zonas();
                                                            zonas.setnCodigo(Integer.parseInt(distrito.getCODIGO()));
                                                            zonas.setcDescripcion(distrito.getDESCRIPCION());
                                                            zonas.setnNivel(Constantes.TRES);
                                                            //zonas.setnTipo( Constantes.TRES );
                                                            zonas.setnTipo(Integer.parseInt(departamento.getCODIGO()));
                                                            zonas.setnSubTipo(Integer.parseInt(provincia.getCODIGO()));
                                                            new ZonasDAO().insertar(zonas);

                                                        }
                                                    } else
                                                        resp = RESULT_FALSE;
                                                } else
                                                    resp = RESULT_FALSE;


                                            }
                                        } else
                                            resp = RESULT_FALSE;
                                    } else
                                        resp = RESULT_FALSE;


                                    resp = RESULT_OK;
                                }
                            } else
                                resp = RESULT_FALSE;
                        } else
                            resp = RESULT_FALSE;
                    }
                }
                else
                    resp = RESULT_OK;
                //END

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
        protected void onPostExecute( String mensaje ) {

            if (mensaje.equals(RESULT_OK)) {
                pd.dismiss();

                List<Fragment> fragmentList = new ArrayList<Fragment>();

                /*
                m_Persona = new Persona();
                m_Persona.setcDistrito( "01" );
                m_Persona.setcDistritoEmpleo( "01" );
                m_Persona.setcProvincia( "01" );
                m_Persona.setcProvinciaEmpleo( "01" );
                m_Persona.setcDepartamento( "01" );
                m_Persona.setcDepartamentoEmpleo( "01" );
                */

                m_daDatosPersonaTabGeneral = m_daDatosPersonaTabGeneral.newsInstance( m_EditarDatos, m_PersonaForm );
                m_daDatosPersonaTabDireccion = m_daDatosPersonaTabDireccion.newsInstance( m_EditarDatos, m_PersonaForm );
                m_daDatosPersonaTabDatosEmpleo = m_daDatosPersonaTabDatosEmpleo.newsInstance( m_EditarDatos, m_PersonaForm );
                m_daDatosPersonaTabDireccionEmpleo = m_daDatosPersonaTabDireccionEmpleo.newsInstance( m_EditarDatos, m_PersonaForm );

                fragmentList.add( m_daDatosPersonaTabGeneral );
                fragmentList.add( m_daDatosPersonaTabDireccion );
                fragmentList.add( m_daDatosPersonaTabDatosEmpleo );
                fragmentList.add( m_daDatosPersonaTabDireccionEmpleo );

                mSectionsPagerAdapter = new SectionsPagerAdapter( getSupportFragmentManager(), fragmentList );
                mViewPager.setAdapter( mSectionsPagerAdapter );
                tabLayout.setupWithViewPager( mViewPager );

                m_daDatosPersonaTabGeneral.setControlador( ActivityDatosPersona.this );
                m_daDatosPersonaTabDireccion.setControlador( ActivityDatosPersona.this );
                m_daDatosPersonaTabDatosEmpleo.setControlador( ActivityDatosPersona.this );
                m_daDatosPersonaTabDireccionEmpleo.setControlador( ActivityDatosPersona.this );

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
                            return true;
                        }else if( event.getAction()==MotionEvent.ACTION_MOVE ){
                            return true;
                        }
                        else if( event.getAction()==MotionEvent.ACTION_SCROLL ){
                            return true;
                        }
                        else if( event.getAction()==MotionEvent.ACTION_UP ){
                            return true;
                        }
                        else if( event.getAction()==MotionEvent.ACTION_CANCEL ){
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

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                Toast.makeText(m_context, "¡No se actualizo data interna!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

    }

    public class ObtenerTokenNuevoAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private TokenItems m_TokenItems;
        private UsuarioDTO m_UsuarioDTO;
        private Persona m_persona;

        public ObtenerTokenNuevoAsync( Context context, UsuarioDTO usuario, Persona persona ){
            m_persona = persona;
            m_UsuarioDTO = usuario;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Autenticando...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_TokenItems = new TokenItems();
                m_TokenItems = m_webApi.ObtenerToken( m_UsuarioDTO );

                if ( m_TokenItems != null )
                    resp = RESULT_OK;
                else
                    resp = RESULT_FALSE;

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
            if ( mensaje.equals( RESULT_OK ) ) {
                pd.dismiss();
                m_Access_token = m_TokenItems.getAccess_token();
                m_UsuarioDTO.setPassword( m_Access_token ); // cambio el password por el token y recuperar el usuario con el email.

                ObtenerAutenticacionAsync obtenerAutenticacionAsync =
                        new ObtenerAutenticacionAsync( m_context, m_UsuarioDTO, m_persona );
                obtenerAutenticacionAsync.execute();

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class ObtenerAutenticacionAsync extends AsyncTask<Void, Void, String> {

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ObtenerAutenticacionAsync ( Context context, UsuarioDTO usuarioDTO, Persona persona ){

            m_PersonaCreditoDTO = persona;
            m_UsuarioDTO = usuarioDTO;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {

            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Autenticando...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                Persona dto = new Persona();
                dto = m_webApi.obtenerAutenticacion( m_UsuarioDTO );

                if ( dto != null ) {
                    if ( dto.getnCodPers() > Constantes.CERO ) {

                        m_Usuario = new Usuario();
                        m_Usuario.setUsuario( m_UsuarioDTO.getUsername() );
                        m_Usuario.setClave( m_UsuarioDTO.getPassword() );
                        m_Usuario.setRolDescripcion( dto.getnNroDoc() ); //DOCUMENTO
                        m_Usuario.setRol( 0 );
                        m_Usuario.setAgencia( Constantes.CINCO );
                        m_Usuario.setAgenciaNombre( "" );
                        m_Usuario.setCodDepAge( 0 );
                        m_Usuario.setCodUsu( 0 );
                        m_Usuario.setNombreUsuario( dto.getcNombres() );
                        m_Usuario.setCodPers( dto.getnCodPers() );
                        m_Usuario.setToken( m_UsuarioDTO.getPassword() );
                        m_Usuario.setTipo( "" );
                        m_Usuario.setIdPais(0 );
                        m_Usuario.setIdEmpresa( 0 );
                        m_Usuario.setIdTipoEmpresa( 0 );
                        m_Usuario.setMoneda( "" );

                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }
                else
                    return RESULT_FALSE;

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
        protected void onPostExecute( String mensaje ) {
            if (mensaje.equals(RESULT_OK)) {
                RegistrarAutenticacion( m_Usuario, pd, m_PersonaCreditoDTO );
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText(m_context, "¡No se pudo realizar el acceso al Usuario!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private Usuario m_Usuario;
        private UsuarioDTO m_UsuarioDTO;
        private Persona m_PersonaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;

    }

    public void RegistrarAutenticacion( Usuario usuario, ProgressDialog pd, Persona oPersonaCreditoDTO ){
        pd.dismiss();
        int ingresoAutenticacion = 0;
        ingresoAutenticacion = new UsuarioDAO().ingresarAutenticacion( usuario );

        //Guardando en modo privado la sesion nueva
        SharedPreferences.Editor spe = getSharedPreferences( getPackageName(), MODE_PRIVATE ).edit();
        Gson gson = new Gson();
        String json = gson.toJson( usuario );
        spe.putString( ActivityLogin.ARG_AUTENTICACION_JSON, json );
        spe.commit();
        //end

        if ( ingresoAutenticacion > Constantes.CERO ) {
            new UsuarioDAO().iniciarSesion( usuario );

            //PERTENECE A UN CARGO PUBLICO
            if ( oPersonaCreditoDTO.getbCargoPublico().equals( "True" ) ) {

                //Toast.makeText(this, "No podemos otorgar un prestamo por ser Persona con Cargo Publico.", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder( this );
                builder.setMessage( "No podemos otorgarte un prestamo por ser persona con cargo público." )
                        .setTitle("SoyLucas!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        Intent bandeja =
                                                new Intent( ActivityDatosPersona.this, ActivityBandejaCreditos.class );
                                        startActivity( bandeja );
                                        finish();

                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                return;

            }
            else{

                // PreEvaluacion
                ObtenerPreEvaluacionAsyns preEvaluacionAsyns =
                        new ObtenerPreEvaluacionAsyns( this, oPersonaCreditoDTO );
                preEvaluacionAsyns.execute();
                //

            }
            //


        }
    }

    public class ObtenerPreEvaluacionAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Persona m_personaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private PreEvaluacionResp m_Respuesta;

        public ObtenerPreEvaluacionAsyns( Context context, Persona oPersona ){

            m_personaCreditoDTO = oPersona;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Evaluando datos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                m_Respuesta = new PreEvaluacionResp();
                m_personaCreditoDTO.setnProducto( Constantes.PRODUCTO_SCORING );
                m_personaCreditoDTO.setnTipoDoc( Constantes.UNO );
                m_Respuesta = m_webApi.obtenerPreEvaluacion( m_personaCreditoDTO, m_Access_token );

                if ( m_Respuesta != null  ){
                    resp = RESULT_OK;
                }
                else
                    resp = RESULT_FALSE;

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

                String validar = "";
                String Bancarizado = m_Respuesta.getcResultado().toString();
                String[] cadena = StringUtils.split( Bancarizado,"|" );
                for ( int i = 0; i < cadena.length; i++ ) {
                    if ( i == Constantes.UNO )
                        validar =  ( cadena[i] );
                }

                if ( validar.equals( "BANCARIZADO" ) ){

                    ObtenerEvaluacionAsyns obtenerEvaluacionAsyns =
                            new ObtenerEvaluacionAsyns( m_context, m_personaCreditoDTO, false );
                    obtenerEvaluacionAsyns.execute();

                }
                else {

                    if ( m_personaCreditoDTO.getnTipoEmp().toString().trim().equals( "1" )
                            && m_personaCreditoDTO.getnSitLab().toString().trim().equals( "1" ) ){

                        ObtenerEvaluacionAsyns obtenerEvaluacionAsyns =
                                new ObtenerEvaluacionAsyns( m_context, m_personaCreditoDTO, true );
                        obtenerEvaluacionAsyns.execute();
                    }
                    else{

                        PersonaCreditoDTO datosPersona = new PersonaCreditoDTO();

                        datosPersona.setcNombres( m_personaCreditoDTO.getcNombres() );
                        datosPersona.setnNroDoc( m_personaCreditoDTO.getnNroDoc() );
                        datosPersona.setcApePat( m_personaCreditoDTO.getcApePat() );
                        datosPersona.setcApeMat( m_personaCreditoDTO.getcApeMat() );
                        datosPersona.setcEmail( m_personaCreditoDTO.getcEmail() );
                        datosPersona.setdFechaNacimiento( m_personaCreditoDTO.getdFechaNacimiento() );
                        datosPersona.setcDirValor1( m_personaCreditoDTO.getcDirValor1() );
                        datosPersona.setcTelefono( m_personaCreditoDTO.getcTelefono() );
                        datosPersona.setcCelular( m_personaCreditoDTO.getcCelular() );
                        datosPersona.setnCodPers( m_personaCreditoDTO.getnCodPers() );
                        datosPersona.setcClienteLenddo( GenerarCodigoVerificadorParaLenddo() );

                        //LENDDO
                        Intent lenddo = new Intent( m_context, LenddoActivity.class );
                        lenddo.putExtra( "DatosPersona", datosPersona );
                        lenddo.putExtra( "OrdenFlujo", Constantes.DOS );
                        startActivity( lenddo );
                        finish();

                    }
                }
            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                Toast.makeText(m_context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class ObtenerEvaluacionAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Persona m_personaCreditoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private EvaluacionResp m_Respuesta;
        private boolean m_bancarizado;

        public ObtenerEvaluacionAsyns( Context context, Persona oPersona, boolean bBancarizado ){

            m_personaCreditoDTO = oPersona;
            m_context = context;
            m_bancarizado = bBancarizado;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Evaluando datos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                m_Respuesta = new EvaluacionResp();
                m_personaCreditoDTO.setnProd( Constantes.PRODUCTO );
                m_personaCreditoDTO.setnSubProd( Constantes.SUB_PRODUCTO );
                m_personaCreditoDTO.setnTipoDoc( Constantes.UNO );
                m_Respuesta = m_webApi.obtenerEvaluacion( m_personaCreditoDTO, m_Access_token );

                if ( m_Respuesta != null  ){
                    m_nIdFlujoMaestro = m_Respuesta.getnIdFlujoMaestro();
                    resp = RESULT_OK;
                }
                else
                    resp = RESULT_FALSE;

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

                String mensajeSms = "";
                if ( m_Respuesta.getnRechazado() == Constantes.UNO ) {

                    mensajeSms = Constantes.Mensaje_Rechazado;
                    if ( m_Respuesta.getnPEP() == Constantes.UNO )
                        mensajeSms = Constantes.Mensaje_Rechazado_PEP;

                    Intent rechazo = new Intent( m_context, ActivityRechazoSolicitud.class );
                    rechazo.putExtra( "Telefono", m_personaCreditoDTO.getcCelular().toString() );
                    rechazo.putExtra( "Mensaje", mensajeSms );
                    startActivity( rechazo );

                }
                else{
                    if ( !m_bancarizado ) {
                        // enviar a solicitud
                        Intent solicitudFinal = new Intent( m_context, ActivitySimulador.class );
                        solicitudFinal.putExtra( "Solicitud" , "OK" );
                        solicitudFinal.putExtra( "IdFlujoMaestro", m_nIdFlujoMaestro );
                        startActivity( solicitudFinal );
                        finish();
                    }
                    else{
                        //NO bancarizado
                        Intent flujo = new Intent( m_context, ActivityRegistroDocumento.class );
                        flujo.putExtra( "IdFlujoMaestro", m_nIdFlujoMaestro );
                        flujo.putExtra( "OrdenFlujo", Constantes.DOS);
                        startActivity(flujo);
                        finish();
                    }

                }

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                Toast.makeText(m_context, "¡Error sin datos!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    protected String GenerarCodigoVerificadorParaLenddo(){
        String appIDCompleto = "";
        int codigo = 0;
        String appID = Constantes.APPLICATION_ID;
        int randomPIN = (int)( Math.random()  * 899999 ) + 100000;
        codigo = randomPIN;

        Calendar calendar = Calendar.getInstance();
        int anio = calendar.get( Calendar.YEAR );
        int mes = calendar.get( Calendar.MONTH );
        int dia = calendar.get( Calendar.DATE );
        int hora = calendar.get( Calendar.HOUR_OF_DAY );
        int minuto = calendar.get( Calendar.MINUTE );
        int segundo = calendar.get( Calendar.SECOND );

        String fecha = String.valueOf( anio + mes + dia );
        String horario = String.valueOf( hora + minuto + segundo );

        appIDCompleto = appID + fecha + horario + String.valueOf( codigo );

        return appIDCompleto;
    }

    public class EnvioSMSAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Alerta m_Alerta;
        private String m_MensajeEnvio;
        private boolean m_bEnvioEmail;
        private RedResponse m_RedResponse;

        public EnvioSMSAsyns( Context context, Alerta alerta, String mensajeEnvio, boolean bEnvioEmail ){
            m_Alerta = alerta;
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_MensajeEnvio = mensajeEnvio;
            m_bEnvioEmail = bEnvioEmail;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Enviando SMS ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }
                boolean m_Respuesta = false;

                m_RedResponse = m_webApi.EnvioSms( m_Alerta, m_Sesion.getToken() );
                if( !m_RedResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_RedResponse.getM_data().iscRed() ){
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }

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
            if ( mensaje.equals( RESULT_OK ) ) {
                Toast.makeText( m_Context, "Mensaje enviado.", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                if( m_bEnvioEmail ){

                    Alerta alerta = new Alerta();
                    alerta.setcTexto( emailCuerpo("MALAS NOTICIAS", m_Sesion.getNombreUsuario(), "Te hemos rechazado", m_MensajeEnvio, "", "", "") );
                    alerta.setcEmail( m_Sesion.getUsuario() );
                    alerta.setcTitulo( "Hola SoyLucas, tenemos malas noticias." );

                    EnvioEmailAsyns envioEmailAsyns = new EnvioEmailAsyns( m_Context, alerta );
                    envioEmailAsyns.execute();
                }

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();

                if ( m_RedResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_Context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                }
                else
                    Toast.makeText( m_Context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else {
                Toast.makeText( m_Context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class EnvioEmailAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Alerta m_Alerta;

        public EnvioEmailAsyns( Context context, Alerta alerta ){
            m_Alerta = alerta;
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Enviando email ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }
                boolean m_Respuesta = false;
                m_Respuesta = m_webApi.EnvioEmail( m_Alerta, m_Sesion.getToken() );
                if ( m_Respuesta == true ){
                    resp = RESULT_OK;
                }
                else
                    resp = RESULT_FALSE;

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
            if ( mensaje.equals( RESULT_OK ) ) {
                Toast.makeText( m_Context, "Email enviado.", Toast.LENGTH_SHORT).show();
                pd.dismiss();

                Intent bandeja = new Intent( m_Context, ActivityBandejaCreditos.class);
                startActivity( bandeja );
                finish();
            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                Toast.makeText( m_Context, "¡Error de envío!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_Context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class VerificarDocumentoAsyns extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private UserListResponse m_UserListResponse;
        private Persona m_personaRegistrar;

        public VerificarDocumentoAsyns( Context context, Persona persona ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_personaRegistrar = persona;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Verificando documento ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }
                PersonaRespDTO dto = new PersonaRespDTO();

                m_UserListResponse = m_webApi.verificarDocumento( m_personaRegistrar.getnNroDoc(), m_Access_token );
                if( !m_UserListResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_UserListResponse.getM_listData() != null ){
                        resp = RESULT_OK;
                    }
                    else {
                        resp = RESULT_FALSE;
                    }
                }
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
            if ( mensaje.equals( RESULT_OK ) ) {
                pd.dismiss();

                if ( m_UserListResponse.getM_listData().size() == Constantes.CERO ){

                    //VALIDAR CELULAR
                    VerificarCelularAsyns celularAsyns = new VerificarCelularAsyns( m_context, m_personaRegistrar );
                    celularAsyns.execute();
                    //

                }
                else{
                    Toast.makeText( m_context, "Usted ya se encuentra registrado!, Por favor inicie sesión.", Toast.LENGTH_SHORT ).show();
                }

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();

                if ( m_UserListResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    public class VerificarCelularAsyns extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private Persona m_persona;
        private int m_respuesta;

        public VerificarCelularAsyns( Context context, Persona persona ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_persona = persona;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Verificando celular ...");
            pd.setCancelable( false );
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                m_respuesta = m_webApi.verificarCelular( m_persona, m_Access_token );
                if ( m_respuesta >= Constantes.CERO ){
                    resp = RESULT_OK;
                }
                else
                    resp = RESULT_FALSE;

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
            if ( mensaje.equals( RESULT_OK ) ) {
                pd.dismiss();
                if ( m_respuesta == Constantes.UNO ){

                    //REGISTRANDO SEGUIR
                    User user = new User();
                    user.setEmail( m_persona.getcEmail() );

                    VerificarEmailAsyns emailAsyns = new VerificarEmailAsyns( m_context, user, m_persona );
                    emailAsyns.execute();
                    //

                }
                else
                    Toast.makeText( m_context, "El N° de celular ya se encuentra registrado!", Toast.LENGTH_SHORT).show();

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
                Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();
            }
            else{
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }

        }
    }

    public class VerificarEmailAsyns extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private User m_User;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private UserListResponse m_userListResponse; //LISTA DE EMAIL.
        private Persona m_persona;

        public VerificarEmailAsyns( Context context, User user, Persona persona ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
            m_User = user;
            m_persona = persona;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Verificando email ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;
                if (!Common.isNetworkConnected( m_context ) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_userListResponse = m_webApi.verificarEmailList( m_User, m_Access_token );
                if( !m_userListResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    if ( m_userListResponse.getM_listData() != null ){
                        resp = RESULT_OK;
                    }
                    else{
                        resp = RESULT_FALSE;
                    }
                }
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
            if( mensaje.equals( RESULT_OK ) ){
                pd.dismiss();

                if ( m_userListResponse.getM_listData().size() == Constantes.CERO ){

                    //REGISTRAR PERSONA YA.
                    //REGISTRAR EL CLIENTE
                    RegistrarPersonaAsyns registrarPersonaAsyns = new RegistrarPersonaAsyns( m_context, m_persona );
                    registrarPersonaAsyns.execute();
                    //
                    //

                }
                else
                    Toast.makeText( m_context, "Usted ya se encuentra registrado!, Por favor inicie sesión.", Toast.LENGTH_SHORT ).show();

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                pd.dismiss();

                if ( m_userListResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }
    }

    private String emailCuerpo( String titulo, String nombre, String resumen, String descripcion1, String texto1, String texto2, String descripcion2 ){
        String body = "";

        body = "<table border='0' style='width: 100%; background: #f1f1f1; font-family: verdana'>" + " \n" +
                "<tr>" + " \n" +
                "<tr>" + " \n" +
                "<table cellspacing='0' cellpadding='0' border='0' style='margin: 0 auto; width: 85%'>" + " \n" +
                "<tr style='background: #FD293F'>" + "\n" +
                "<td colspan='3' style='padding: 30px'>" + " \n" +
                "<h1 style='text-transform: uppercase; text-align: center; color: white; margin: 0 auto; font-family: verdana'>" + titulo + " </h1> " + " \n" +
                "</td>" + " \n" +
                "</tr>" + " \n" +
                "<tr style='background: white'>" + " \n" +
                "<td colspan='3' style='padding: 15px;'>" + " \n" +
                "<h2 style='background: white; text-transform: uppercase; text-align: center; font-family: verdana'>hola " + nombre + "</h2>" + " \n" +
                "<h4 style='font-weight: 500; text-transform: uppercase; text-align: center; font-family: verdana'><i>" + resumen + "</i></h4>" + "\n" +
                "<p style='text-align: justify; padding: 15px; font-family: verdana'>" + descripcion1 + "</p>";

        if ( !texto1.equals( "" )  || !texto2.equals( "" ) ) {
            body = body + "<ul>";
            if ( !texto1.equals( "" ) ) {
                body = body + "<li><b>" + texto1 + "</b></li>";
            }
            if ( !texto2.equals( "" ) ) {
                body = body + "<li><b>" + texto2 + "</b></li>";
            }
            body = body + "<ul>";
        }

        if ( !descripcion2.equals( "" ) ) {
            body = body + "<p>" + descripcion2 + "</p>";
        }

        body = body + "</td>" + " \n" +
                "</tr>" + " \n" +
                "<tr style='background: #454544; text-transform: uppercase; text-align: center'>" + "\n" +
                "<td style='padding: 25px'>" +"\n" +
                "<button style='text-align: center; color: #fff; font-size: 14px; font-weight: 500;  border:none; padding: 10px 20px; background-color: #FFB700; font-size: 25px'>" +"\n" +
                "<span style='color: #454544; text-transform: uppercase; font-weight: bold; font-size: 25px'>Alo Lucas:</span> 01 615-7030</button>" +"\n" +
                "</td>" +"\n" +
                "</tr>" +"\n" +
                "</table>" +"\n" +
                "</td>" +"\n" +
                "</tr>" +"\n" +
                "</table>";

        return body;
    }

    protected void ObtenerVerificacion(){

        m_nCodVerificacionMovil = GenerarCodigoVerificador();

        Alerta alerta = new Alerta();
        //alerta.setcMovil( m_daDatosPersonaTabGeneral.getCelularPersona() );
        alerta.setcMovil( m_Numero_Celular );
        alerta.setcTexto( "Hola SoyLucas, tu código es " + m_nCodVerificacionMovil );
        alerta.setcEmail( "" );
        alerta.setcTitulo( "Lucas" );

        EnvioSMSAsyns envioSMSAsyns = new EnvioSMSAsyns( this, alerta, "", false );
        envioSMSAsyns.execute();
    }

    protected int GenerarCodigoVerificador(){
        int codigo = 0;
        int randomPIN = (int)( Math.random()  * 9000 ) + 1000;
        codigo = randomPIN;
        String val = ""+randomPIN;
        return codigo;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        if ( m_EditarDatos )
            dialog.setMessage("¿Esta seguro que desea salir de la actualización de datos?");
        else
            dialog.setMessage("¿Esta seguro que desea salir del registro de datos?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if ( m_EditarDatos ){
                    setResult( RESULT_CANCELED );
                    finish();
                }
                else {

                    setResult( RESULT_CANCELED );
                    finish();
                }

            }
        });
        dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    protected DatosPersonaTabGeneral m_daDatosPersonaTabGeneral;
    protected DatosPersonaTabDireccion m_daDatosPersonaTabDireccion;
    protected DatosPersonaTabDatosEmpleo m_daDatosPersonaTabDatosEmpleo;
    protected DatosPersonaTabDireccionEmpleo m_daDatosPersonaTabDireccionEmpleo;
    protected Usuario m_Sesion;
}
