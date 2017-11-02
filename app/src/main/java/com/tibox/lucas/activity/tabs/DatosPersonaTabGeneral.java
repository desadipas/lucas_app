package com.tibox.lucas.activity.tabs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.tibox.lucas.R;
import com.tibox.lucas.activity.ActivityDatosPersona;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import java.util.Calendar;
import java.util.Date;
/**
 * Created by desa02 on 18/09/2017.
 */
public class DatosPersonaTabGeneral extends Fragment {

    protected boolean m_isVistasInicializadas;
    protected boolean m_isPersonaInicializadas;

    protected Persona m_personaInicializar;
    protected ActivityDatosPersona m_actividadPadre;
    private IEventsListener m_Listener;

    protected Boolean m_bEditar;
    private int day, month ,year;

    public static DatosPersonaTabGeneral newsInstance( boolean bEditar, Persona persona ){
        DatosPersonaTabGeneral f = new DatosPersonaTabGeneral();
        Bundle args = new Bundle();
        args.putBoolean( "editar", bEditar );
        args.putSerializable( "datos", persona );
        f.setArguments( args );
        return f;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance( true );
        m_bEditar = getArguments().getBoolean("editar");
        m_personaInicializar = (Persona) getArguments().getSerializable( "datos" );
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.tab_datos_personales_general, container, false );
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_isVistasInicializadas = true;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get( Calendar.YEAR );
        month = calendar.get( Calendar.MONTH );
        day = calendar.get( Calendar.DAY_OF_MONTH );


        etDocumento = ( EditText ) view.findViewById( R.id.et_documento );
        etNombres = (EditText) view.findViewById( R.id.et_nombres );
        etApePaterno = (EditText) view.findViewById( R.id.et_ape_paterno );
        etApeMaterno = (EditText) view.findViewById( R.id.et_ape_materno );
        etCelular = (EditText) view.findViewById( R.id.et_celular);
        etCorreo  = (EditText) view.findViewById( R.id.et_correo);
        etRepCorreo  = (EditText) view.findViewById( R.id.et_repetir_correo);
        etTelefonoFijo  = (EditText) view.findViewById( R.id.et_telefono_fijo);
        etFechaNac  = (EditText) view.findViewById( R.id.et_fecha_nac);
        etDocumentoConyuge = (EditText) view.findViewById( R.id.et_documento_conyuge );
        etNombresConyuge = (EditText) view.findViewById( R.id.et_nombres_conyuge );
        etApellidosConyuge = (EditText) view.findViewById( R.id.et_apellidos_conyuge );
        etCodigoVerificador = ( EditText ) view.findViewById( R.id.et_codigo_verificador );

        lyCodigoVerificador = (LinearLayout) view.findViewById( R.id.ly_codigo_verificador );
        lyConyuge = (LinearLayout) view.findViewById( R.id.ly_conyuge );

        inputlayoutDocumento = (TextInputLayout) view.findViewById( R.id.input_layout_documento );
        inputlayoutCodigoVerificador = (TextInputLayout) view.findViewById( R.id.input_layout_codigo_verificador );
        inputlayoutRepetirCorreo = (TextInputLayout) view.findViewById( R.id.input_layout_repetir_correo);
        inputlayoutDocumentoConyuge = (TextInputLayout) view.findViewById( R.id.input_layout_documento_conyuge );
        inputlayoutNombres = (TextInputLayout) view.findViewById( R.id.input_layout_nombres );
        inputlayoutApePaterno = (TextInputLayout) view.findViewById( R.id.input_layout_ape_paterno );
        inputlayoutApeMaterno = (TextInputLayout) view.findViewById( R.id.input_layout_ape_materno );
        inputlayoutCelular = (TextInputLayout) view.findViewById( R.id.input_layout_celular );
        inputlayoutCorreo = (TextInputLayout) view.findViewById( R.id.input_layout_correo );
        inputlayoutTelefonoFijo = (TextInputLayout) view.findViewById( R.id.input_layout_telefono_fijo );
        inputlayoutNombresConyuge = (TextInputLayout) view.findViewById( R.id.input_layout_nombres_conyuge );
        inputlayoutApellidosConyuge = (TextInputLayout) view.findViewById( R.id.input_layout_apellidos_conyuge );
        inputlayoutFechaNac = (TextInputLayout) view.findViewById( R.id.input_layout_fecha_nac );

        ibtnFechaNac = (ImageButton) view.findViewById( R.id.ibtn_fecha_nac );

        spSexo = (Spinner) view.findViewById( R.id.sp_sexo );
        spEstadoCivil = (Spinner) view.findViewById( R.id.sp_estado_civil );

        rbtnSi = (RadioButton) view.findViewById( R.id.radio_button_si );
        rbtnNo = (RadioButton) view.findViewById( R.id.radio_button_no );
        radioGroupCargo = (RadioGroup) view.findViewById( R.id.radio_group_cargo_publico );

        ibtnCodigoVerificador = (ImageButton) view.findViewById( R.id.ibtn_codigo_verificador );
        btnAtras = (Button) view.findViewById( R.id.btn_atras );
        btnContinuar = (Button) view.findViewById( R.id.btn_continuar );

        btnContinuar.setOnClickListener( btnContinuarsetOnClickListener );
        btnAtras.setOnClickListener( btnAtrassetOnClickListener );
        ibtnCodigoVerificador.setOnClickListener( ibtnCodigoVerificadorsetOnClickListener );

        new CatalagoCodigosDAO().rellenaSpinner( spSexo, Constantes.TIPO_SEXO, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spEstadoCivil, Constantes.TIPO_ESTADO_CIVIL, view.getContext() );

        etDocumento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutDocumento.setError(null); // hide error
            }
        });
        etNombres.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutNombres.setError(null); // hide error
            }
        });
        etApePaterno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutApePaterno.setError(null); // hide error
            }
        });
        etApeMaterno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutApeMaterno.setError(null); // hide error
            }
        });
        etCelular.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutCelular.setError(null); // hide error
            }
        });
        etCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutCorreo.setError(null); // hide error
            }
        });
        etRepCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutRepetirCorreo.setError(null); // hide error
            }
        });
        etTelefonoFijo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutTelefonoFijo.setError(null); // hide error
            }
        });
        etFechaNac.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutFechaNac.setError(null); // hide error
            }
        });
        etDocumentoConyuge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutDocumentoConyuge.setError(null); // hide error
            }
        });
        etNombresConyuge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutNombresConyuge.setError(null); // hide error
            }
        });
        etApellidosConyuge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutApellidosConyuge.setError(null); // hide error
            }
        });
        etCodigoVerificador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutCodigoVerificador.setError(null); // hide error
            }
        });

        if ( m_bEditar ){
            lyCodigoVerificador.setVisibility( View.GONE );
            inputlayoutRepetirCorreo.setVisibility( View.GONE );
            etDocumento.setEnabled( !m_bEditar );
            etApePaterno.setEnabled( !m_bEditar );
            etApeMaterno.setEnabled( !m_bEditar );
            etNombres.setEnabled( !m_bEditar );
            etCorreo.setEnabled( !m_bEditar );
            etCelular.setEnabled( !m_bEditar );
        }
        //CargarDatos( m_bEditar );

        spEstadoCivil.setOnItemSelectedListener( spEstadoCivilsetOnItemSelectedListener );
        ibtnFechaNac.setOnClickListener( ibtnFechaNacsetOnClickListener );


        if ( ( m_personaInicializar != null ) && !m_isPersonaInicializadas ) {
            procesarPersonaInicializar( m_personaInicializar );
        }

    }

    View.OnClickListener ibtnCodigoVerificadorsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            LayoutInflater factory = LayoutInflater.from( view.getContext() );
            final View viewI = factory.inflate( R.layout.image_dni, null);
            AlertDialog.Builder builder = new AlertDialog.Builder( view.getContext() );
            builder.setTitle("Código verificador")
                    .setView( viewI )
                    .setCancelable(false)
                    .setNeutralButton("Cerrar",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    View.OnClickListener btnContinuarsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if ( validandoDatos() )
            {
                Toast.makeText( m_actividadPadre, "Debe de completar los campos requeridos.", Toast.LENGTH_SHORT ).show();
                return;
            }

            if ( m_Listener != null) {
                m_Listener.onClickContinuar();
            }
        }
    };

    View.OnClickListener btnAtrassetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( m_Listener != null) {
                m_Listener.onClickAtras();
            }
        }
    };

    View.OnClickListener ibtnFechaNacsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            DatePickerDialog fecha = new DatePickerDialog( view.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int Year, int monthOfYear, int dayOfMonth) {
                    etFechaNac.setText( dayOfMonth + "/" + ( monthOfYear + 1 ) + "/" + Year );
                    year = Year;
                    month = monthOfYear;
                    day = dayOfMonth;
                }
            }, year, month, day );
            fecha.getDatePicker().setMaxDate( new Date().getTime() );
            fecha.show();
        }
    };

    AdapterView.OnItemSelectedListener spEstadoCivilsetOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected( AdapterView<?> adapterView, View view, int i, long l) {

            if( (int) spEstadoCivil.getSelectedItemId() == Constantes.DOS )
                lyConyuge.setVisibility( View.VISIBLE );
            else
                lyConyuge.setVisibility( View.GONE );
        }

        @Override
        public void onNothingSelected( AdapterView<?> adapterView) {

        }
    };

    protected void CargarDatos( boolean estado ){

        if ( m_personaInicializar != null ){

            etDocumento.setText( m_personaInicializar.getnNroDoc() );
            etNombres.setText( m_personaInicializar.getcNombres() );
            etApePaterno.setText( m_personaInicializar.getcApePat() );
            etApeMaterno.setText( m_personaInicializar.getcApeMat() );
            etCelular.setText( m_personaInicializar.getcCelular() );
            etCorreo.setText( m_personaInicializar.getcEmail() );
            etRepCorreo.setText( m_personaInicializar.getcConfirmaEmail() );
            etTelefonoFijo.setText( m_personaInicializar.getcTelefono() );
            etFechaNac.setText( m_personaInicializar.getdFechaNacimiento() );

            etDocumentoConyuge.setText( m_personaInicializar.getcDniConyuge() );
            etApellidosConyuge.setText( m_personaInicializar.getcApeConyuge() );
            etNombresConyuge.setText( m_personaInicializar.getcNomConyuge() );

            if ( Boolean.parseBoolean( m_personaInicializar.getbCargoPublico() ) )
                rbtnSi.setChecked( true );
            else
                rbtnNo.setChecked( true );
            if ( Integer.valueOf( m_personaInicializar.getnSexo() ) > Constantes.CERO )
                spSexo.setSelection( Utilidades.getCursorSpinnerPositionById( spSexo, Integer.valueOf( m_personaInicializar.getnSexo() ) ) );
            if ( Integer.valueOf( m_personaInicializar.getnEstadoCivil() ) > Constantes.CERO )
                spEstadoCivil.setSelection( Utilidades.getCursorSpinnerPositionById( spEstadoCivil, Integer.valueOf( m_personaInicializar.getnEstadoCivil() ) ) );
        }

    }

    public interface IEventsListener {
        public void onClickAtras();
        public void onClickContinuar();
    }

    public void setControlador( ActivityDatosPersona sectionsPagerAdapter ) {
        m_actividadPadre = sectionsPagerAdapter;
    }

    public boolean validandoDatos(){
        boolean bValidate = false;

        if ( etDocumento.getText().toString().trim().equals( "" ) || etDocumento.getText().length() < Constantes.OCHO )
        {
            inputlayoutDocumento.setError( "Documento" );
            bValidate = true;
            return bValidate;
        }

        if ( !m_bEditar ){

            if ( etCodigoVerificador.getText().toString().trim().equals("") ) {
                inputlayoutCodigoVerificador.setError( "Codigo verificador" );
                bValidate = true;
                return bValidate;
            }
        }


        if ( etNombres.getText().toString().trim().equals( "" ) )
        {
            inputlayoutNombres.setError( "Nombres" );
            bValidate = true;
            return bValidate;
        }


        if ( etApePaterno.getText().toString().trim().equals( "" ) )
        {
            inputlayoutApePaterno.setError( "Primer apellido" );
            bValidate = true;
            return bValidate;
        }

        if ( etApeMaterno.getText().toString().trim().equals( "" ) )
        {
            inputlayoutApeMaterno.setError( "Segundo apellido" );
            bValidate = true;
            return bValidate;
        }

        if ( (int) spSexo.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( etCelular.getText().toString().trim().equals( "" ) || etCelular.getText().length() < Constantes.NUEVE )
        {
            inputlayoutCelular.setError( "Celular" );
            bValidate = true;
            return bValidate;
        }

        // validar que empieze 9--
        String nroCelularPrimero = etCelular.getText().toString().substring(0, 1);
        if ( !nroCelularPrimero.trim().equals( "9" ) )
        {
            inputlayoutCelular.setError( "Celular" );
            bValidate = true;
            return bValidate;
        }
        //

        if ( !Utilidades.isValidEmail( etCorreo.getText().toString() ) )
        {
            inputlayoutCorreo.setError( "Correo" );
            bValidate = true;
            return bValidate;
        }


        if ( !etRepCorreo.getText().toString().trim().equals( etCorreo.getText().toString() )
                && !m_bEditar )
        {
            inputlayoutRepetirCorreo.setError( "Correo confirmar" );
            bValidate = true;
            return bValidate;
        }

        if ( etFechaNac.getText().toString().trim().equals( "" ) )
        {
            inputlayoutFechaNac.setError( "Fecha nacimiento" );
            bValidate = true;
            return bValidate;
        }

        if ( (int) spEstadoCivil.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if( (int) spEstadoCivil.getSelectedItemId() == Constantes.DOS ){
            if ( etDocumentoConyuge.getText().toString().trim().equals( "" ) || etDocumentoConyuge.getText().length() < Constantes.OCHO )
            {
                inputlayoutDocumentoConyuge.setError( "Documento conyuge" );
                bValidate = true;
                return bValidate;
            }

            if ( etNombresConyuge.getText().toString().trim().equals( "" ) )
            {
                inputlayoutNombresConyuge.setError( "Nombres conyuge" );
                bValidate = true;
                return bValidate;
            }

            if ( etApellidosConyuge.getText().toString().trim().equals( "" ) )
            {
                inputlayoutApellidosConyuge.setError( "Apellidos conyuge" );
                bValidate = true;
                return bValidate;
            }
        }

        // valido codigo
        if ( !m_bEditar ) {
            boolean bverificadorDoc = false;
            bverificadorDoc = validaDocumento( etDocumento.getText().toString(), String.valueOf( etCodigoVerificador.getText().toString()));
            if (!bverificadorDoc)
            {
                bValidate = true;
                inputlayoutCodigoVerificador.setError( "Codigo verificador incorrecto" );
                return bValidate;
            }
        }

        //valido menor de edad
        Calendar calendar = Calendar.getInstance();
        int yearActual = calendar.get( Calendar.YEAR );
        String Fecha = etFechaNac.getText().toString();
        String[] cadena = Fecha.split("/");
        int AnioCliente = 1999;
        for (int i = 0; i < cadena.length; i++) {
            if ( i == Constantes.DOS )
                AnioCliente = Integer.parseInt(cadena[i]);
        }
        if( ( yearActual - AnioCliente ) < Constantes.DIECIOCHO )
        {
            inputlayoutFechaNac.setError( "Es menor de edad" );
            bValidate = true;
            return bValidate;
        }
        //



        return bValidate;
    }

    protected boolean validaDocumento( String pnDocumento, String pnCodChequeo ){
        int addition;
        String[] HashNumbers = new String[11];
        boolean OK;
        addition = 0;
        Integer[] Hash = new Integer[10];
        String identificationComponent;
        identificationComponent = pnDocumento;
        int identificationComponentLength;
        identificationComponentLength = identificationComponent.length();
        Hash[0] = 5;
        Hash[1] = 4;
        Hash[2] = 3;
        Hash[3] = 2;
        Hash[4] = 7;
        Hash[5] = 6;
        Hash[6] = 5;
        Hash[7] = 4;
        Hash[8] = 3;
        Hash[9] = 2;
        int diff;
        diff = ( 10 - identificationComponentLength );
        int i;
        i = identificationComponentLength;
        while ( ( i >= 1 ) ) {
            addition = ( addition + ( Integer.parseInt( identificationComponent.substring( i-1, i ) ) - 0 ) * Hash[( (i - 1) + diff)] );
            i = ( i - 1);
        }

        addition = ( 11 - ( addition % 11 ) );

        if ( (addition == 11 ) ) {
            addition = 0;
        }

        if ( Utilidades.IsNumeric( pnCodChequeo ) ) {
            HashNumbers[0] = "6";
            HashNumbers[1] = "7";
            HashNumbers[2] = "8";
            HashNumbers[3] = "9";
            HashNumbers[4] = "0";
            HashNumbers[5] = "1";
            HashNumbers[6] = "1";
            HashNumbers[7] = "2";
            HashNumbers[8] = "3";
            HashNumbers[9] = "4";
            HashNumbers[10] = "5";

            if ( pnCodChequeo.equals( HashNumbers[addition] ) ) {
                OK = true;
            }
            else {
                OK = false;
            }

        }
        else {
            HashNumbers[0] = "K";
            HashNumbers[1] = "A";
            HashNumbers[2] = "B";
            HashNumbers[3] = "C";
            HashNumbers[4] = "D";
            HashNumbers[5] = "E";
            HashNumbers[6] = "F";
            HashNumbers[7] = "G";
            HashNumbers[8] = "H";
            HashNumbers[9] = "I";
            HashNumbers[10] = "J";
            if ( pnCodChequeo.equals( HashNumbers[addition] ) ) {
                OK = true;
            }
            else {
                OK = false;
            }
        }
        return OK;
    }

    public String getCelularPersona(){
        String celular = "";
        //return celular;

        if ( m_isVistasInicializadas ) {
            celular = etCelular.getText().toString();
            return celular;
        }
        else {
            if ( m_personaInicializar != null ) {
                return m_personaInicializar.getcCelular();
            }
            else {
                return "";
            }
        }

    }

    public Persona obtenerEntidad(){
        if ( m_isVistasInicializadas ) {
            return obtenerEntidad( new Persona() );
        }
        else {
            return null;
        }
    }

    public Persona obtenerEntidad( Persona persona ) {
        if ( !m_isVistasInicializadas ) {
            if ( m_personaInicializar != null ) {
                persona.setnNroDoc( m_personaInicializar.getnNroDoc() );
                persona.setnCodigoVerificador( m_personaInicializar.getnCodigoVerificador() );
                persona.setcNombres( m_personaInicializar.getcNombres() );
                persona.setcApePat( m_personaInicializar.getcApePat() );
                persona.setcApeMat( m_personaInicializar.getcApeMat() );
                persona.setnSexo( m_personaInicializar.getnSexo() );
                persona.setcCelular( m_personaInicializar.getcCelular() );
                persona.setcEmail( m_personaInicializar.getcEmail() );
                persona.setcConfirmaEmail( m_personaInicializar.getcConfirmaEmail() );
                persona.setcTelefono( m_personaInicializar.getcTelefono() );
                persona.setdFechaNacimiento( m_personaInicializar.getdFechaNacimiento() );
                persona.setbCargoPublico( m_personaInicializar.getbCargoPublico() );
                persona.setnEstadoCivil( m_personaInicializar.getnEstadoCivil() );
                persona.setcDniConyuge( m_personaInicializar.getcDniConyuge() );
                persona.setcNomConyuge( m_personaInicializar.getcNomConyuge() );
                persona.setcApeConyuge( m_personaInicializar.getcApeConyuge() );
            }
        }
        else{
            persona.setnNroDoc( etDocumento.getText().toString() );
            if ( !m_bEditar )
                persona.setnCodigoVerificador( Integer.parseInt( etCodigoVerificador.getText().toString() ) );
            else
                persona.setnCodigoVerificador( Constantes.CERO );
            persona.setcNombres( etNombres.getText().toString() );
            persona.setcApePat( etApePaterno.getText().toString() );
            persona.setcApeMat( etApeMaterno.getText().toString() );
            persona.setnSexo( String.valueOf( (int) spSexo.getSelectedItemId() ) );
            persona.setcCelular( etCelular.getText().toString() );
            persona.setcEmail( etCorreo.getText().toString() );
            persona.setcConfirmaEmail( etRepCorreo.getText().toString() );
            persona.setcTelefono( etTelefonoFijo.getText().toString() );
            persona.setdFechaNacimiento( etFechaNac.getText().toString() );
            persona.setbCargoPublico( rbtnSi.isChecked() ? "True" : "False" );
            persona.setnEstadoCivil( String.valueOf( (int) spEstadoCivil.getSelectedItemId() ) );
            persona.setcDniConyuge( etDocumentoConyuge.getText().toString() );
            persona.setcNomConyuge( etNombresConyuge.getText().toString() );
            persona.setcApeConyuge( etApellidosConyuge.getText().toString() );
        }

        return persona;
    }

    public void inicializarConEntidad( Persona persona ){
        m_personaInicializar = persona;
        if ( m_isVistasInicializadas ) {
            procesarPersonaInicializar( persona );
        }
    }

    protected void procesarPersonaInicializar( Persona persona ){
        m_isPersonaInicializadas = true;

        if ( persona != null ){
            etDocumento.setText( persona.getnNroDoc() );
            etNombres.setText( persona.getcNombres() );
            etApePaterno.setText( persona.getcApePat() );
            etApeMaterno.setText( persona.getcApeMat() );
            etCelular.setText( persona.getcCelular() );
            etCorreo.setText( persona.getcEmail() );
            //etRepCorreo.setText( persona.getcConfirmaEmail() );
            etRepCorreo.setText( persona.getcEmail() );
            etTelefonoFijo.setText( persona.getcTelefono() );
            etFechaNac.setText( persona.getdFechaNacimiento() );

            if ( Boolean.parseBoolean( persona.getbCargoPublico() ) )
                rbtnSi.setChecked( true );
            else
                rbtnNo.setChecked( true );
            if ( Integer.valueOf( persona.getnSexo() ) > Constantes.CERO )
                spSexo.setSelection( Utilidades.getCursorSpinnerPositionById( spSexo, Integer.valueOf( persona.getnSexo() ) ) );
            if ( Integer.valueOf( persona.getnEstadoCivil() ) > Constantes.CERO )
                spEstadoCivil.setSelection( Utilidades.getCursorSpinnerPositionById( spEstadoCivil, Integer.valueOf( persona.getnEstadoCivil() ) ) );
        }

    }

    private <T extends Fragment> T getFragmentById( int id, T byDefault ) {
        try {
            T fragment = ( T ) getFragmentManager().findFragmentById( id );
            if ( fragment == null ) {
                return byDefault;
            }
            return fragment;
        }
        catch ( Exception ex ) {
            return byDefault;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if ( context instanceof IEventsListener ) {
            m_Listener = (IEventsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnItemClickListener");
        }

        if ( context instanceof ActivityDatosPersona ) {
            m_actividadPadre = ( ActivityDatosPersona ) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_Listener = null;
    }

    private ImageButton ibtnCodigoVerificador;
    private Button btnContinuar, btnAtras;
    private ImageButton ibtnFechaNac;
    protected EditText etDocumento, etNombres, etApePaterno, etApeMaterno, etCelular, etCorreo, etRepCorreo, etTelefonoFijo, etFechaNac,
            etDocumentoConyuge,etNombresConyuge,etApellidosConyuge, etCodigoVerificador;
    private Spinner spEstadoCivil, spSexo;
    private RadioButton rbtnSi,rbtnNo;
    private RadioGroup radioGroupCargo;
    private LinearLayout lyCodigoVerificador, lyConyuge;
    private TextInputLayout inputlayoutDocumento, inputlayoutCodigoVerificador, inputlayoutNombres,
            inputlayoutApePaterno, inputlayoutApeMaterno, inputlayoutCelular, inputlayoutCorreo,inputlayoutFechaNac,
            inputlayoutTelefonoFijo, inputlayoutRepetirCorreo, inputlayoutDocumentoConyuge, inputlayoutNombresConyuge, inputlayoutApellidosConyuge;
}

