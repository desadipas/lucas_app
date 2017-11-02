package com.tibox.lucas.activity.tabs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tibox.lucas.R;
import com.tibox.lucas.activity.ActivityCaducidadAutorizacion;
import com.tibox.lucas.activity.ActivityDatosPersona;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.ZonasDAO;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.DatosEntrada.Alerta;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.network.response.RedResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.io.IOException;

/**
 * Created by desa02 on 18/09/2017.
 */

public class DatosPersonaTabDireccionEmpleo extends Fragment {

    protected boolean m_isVistasInicializadas;
    protected boolean m_isPersonaInicializadas;

    protected Persona m_personaInicializar;

    protected Boolean m_bEditar;
    private int m_IdspDepartamento = 0;
    private int m_IdspProvincia = 0;
    private int m_IdspDistrito = 0;

    protected ActivityDatosPersona m_actividadPadre;
    private IEventsListener m_Listener;

    public static DatosPersonaTabDireccionEmpleo newsInstance( boolean bEditar, Persona persona ){
        DatosPersonaTabDireccionEmpleo f = new DatosPersonaTabDireccionEmpleo();
        Bundle args = new Bundle();
        args.putBoolean( "editar", bEditar );
        args.putSerializable( "datos", persona );
        f.setArguments( args );
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_bEditar = getArguments().getBoolean("editar");
        m_personaInicializar = (Persona) getArguments().getSerializable( "datos" );
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.tab_datos_personales_direccion_empleo, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_isVistasInicializadas = true;

        spDepartamentoEmpleo = (Spinner) view.findViewById( R.id.sp_departamento_empleo );
        spProvinciaEmpleo = (Spinner) view.findViewById( R.id.sp_provincia_empleo );
        spDistritoEmpleo = (Spinner) view.findViewById( R.id.sp_distrito_empleo );
        spNroMzEmpleo = (Spinner) view.findViewById( R.id.sp_nro_mz_empleo );
        spDptoLtEmpleo = (Spinner) view.findViewById( R.id.sp_dpto_lt_empleo );
        spTipoDireccionEmpleo = (Spinner) view.findViewById( R.id.sp_tipo_direccion_empleo );

        etNroMzEmpleo = (EditText) view.findViewById( R.id.et_nro_mz_empleo );
        etDptoLtEmpleo = (EditText) view.findViewById( R.id.et_dpto_lt_empleo );
        etDireccionEmpleo = (EditText) view.findViewById( R.id.et_direccion_empleo );
        etPinSeguridad = (EditText) view.findViewById( R.id.et_pin_seguridad );

        btnConfirmarEnvioCodigo = (Button) view.findViewById( R.id.btn_confirmar_envio_codigo );
        btnAtras = (Button) view.findViewById( R.id.btn_atras_direccion_empleo );
        btnContinuar = (Button) view.findViewById( R.id.btn_continuar_direccion_empleo );

        cbxAutorizoDatos = (CheckBox) view.findViewById( R.id.cbx_autorizo_datos );
        cbxAutorizoEnvio = (CheckBox) view.findViewById( R.id.cbx_autorizo_envio );

        tvLinkTerminos = (TextView) view.findViewById( R.id.tv_link_terminos );

        inputlayoutNroMzEmpleo = (TextInputLayout) view.findViewById( R.id.input_layout_nro_mz_empleo );
        inputlayoutDptoLoteEmpleo = (TextInputLayout) view.findViewById( R.id.input_layout_dpto_lt_empleo );
        inputlayoutDireccionEmpleo = (TextInputLayout) view.findViewById( R.id.input_layout_direccion_empleo );

        lyCodigoVerificadorNumero = (LinearLayout) view.findViewById( R.id.ly_codigo_verificador_numero );

        new CatalagoCodigosDAO().rellenaSpinner( spNroMzEmpleo, Constantes.TIPO_NRO, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spDptoLtEmpleo, Constantes.TIPO_DPTO, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spTipoDireccionEmpleo, Constantes.TIPO_DIRECCION, view.getContext() );

        spDepartamentoEmpleo.setOnItemSelectedListener( spDepartamentosetOnItemSelectedListener );
        spProvinciaEmpleo.setOnItemSelectedListener( spProvinciasetOnItemSelectedListener );
        spDistritoEmpleo.setOnItemSelectedListener( spDistritoEmpleosetOnItemSelectedListener );

        btnAtras.setOnClickListener( btnAtrassetOnClickListener );
        btnContinuar.setOnClickListener( btnContinuarsetOnClickListener );
        btnConfirmarEnvioCodigo.setOnClickListener( btnConfirmarEnvioCodigosetOnClickListener );
        tvLinkTerminos.setOnClickListener( tvLinkTerminossetOnClickListener );

        etNroMzEmpleo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutNroMzEmpleo.setError(null); // hide error
            }
        });
        etDptoLtEmpleo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutDptoLoteEmpleo.setError(null); // hide error
            }
        });
        etDireccionEmpleo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutDireccionEmpleo.setError(null); // hide error
            }
        });

        if ( m_bEditar ){
            lyCodigoVerificadorNumero.setVisibility( View.VISIBLE );
        }
        else{
            if ( m_IdspDepartamento == 0 ){
                m_IdspDepartamento = Constantes.UNO;
                m_IdspProvincia = Constantes.UNO;
                m_IdspDistrito = Constantes.UNO;
            }
        }
            //CargarDatos( m_bEditar );
        /*else {
            new ZonasDAO().rellenaSpinner( spDepartamentoEmpleo, Constantes.UNO, view.getContext(), Constantes.UNO );
            spDepartamentoEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamentoEmpleo, m_IdspDepartamento ) );
        }*/

        new ZonasDAO().rellenaSpinner( spDepartamentoEmpleo, Constantes.UNO, view.getContext(), Constantes.UNO );
        spDepartamentoEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamentoEmpleo, m_IdspDepartamento ) );


        if ( ( m_personaInicializar != null ) && !m_isPersonaInicializadas ) {
            procesarPersonaInicializar( m_personaInicializar );
        }
    }

    AdapterView.OnItemSelectedListener spDistritoEmpleosetOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            m_IdspDistrito = (int) spDistritoEmpleo.getSelectedItemId();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    View.OnClickListener tvLinkTerminossetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Uri uri = Uri.parse( Constantes.URL_TERMINOS_CONDICIONES );
            Intent intent = new Intent( Intent.ACTION_VIEW, uri);
            startActivity( intent );
        }
    };

    AdapterView.OnItemSelectedListener spDepartamentosetOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //m_IdspDepartamento = (int) spDepartamentoEmpleo.getSelectedItemId();
            //ActualizarZonas( spProvinciaEmpleo, Constantes.DOS, Constantes.DOS, m_IdspDepartamento, getActivity(), false, 0 );


            if ( m_IdspDepartamento != 0 ){
                if(  m_IdspDepartamento != (int) spDepartamentoEmpleo.getSelectedItemId() ){
                    m_IdspDepartamento = (int) spDepartamentoEmpleo.getSelectedItemId();
                    ActualizarZonas( spProvinciaEmpleo, Constantes.DOS, Constantes.DOS, m_IdspDepartamento, getActivity(), false, 0 );
                }
                else
                {
                    if ( m_IdspProvincia > Constantes.CERO ) {
                        new ZonasDAO().rellenaSpinner( spProvinciaEmpleo, Constantes.DOS, getActivity(), m_IdspDepartamento );
                        spProvinciaEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spProvinciaEmpleo, m_IdspProvincia ) );
                    }


                }
            }


        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    AdapterView.OnItemSelectedListener spProvinciasetOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //m_IdspProvincia = (int) spProvinciaEmpleo.getSelectedItemId();
            //ActualizarZonas( spDistritoEmpleo, Constantes.TRES, Constantes.TRES, m_IdspProvincia, getActivity(), true, m_IdspDepartamento );

            if ( m_IdspProvincia != 0 ){
                if(  m_IdspProvincia != (int) spProvinciaEmpleo.getSelectedItemId() ){
                    m_IdspProvincia = (int) spProvinciaEmpleo.getSelectedItemId();
                    ActualizarZonas( spDistritoEmpleo, Constantes.TRES, Constantes.TRES, m_IdspProvincia, getActivity(), true, m_IdspDepartamento );
                }
                else{
                    if ( m_IdspDistrito > Constantes.CERO ) {
                        new ZonasDAO().rellenaSpinnerDistrito( spDistritoEmpleo, m_IdspDepartamento, getActivity(), m_IdspProvincia );
                        spDistritoEmpleo.setSelection(Utilidades.getCursorSpinnerPositionById( spDistritoEmpleo, m_IdspDistrito ) );
                    }
                }
            }


        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    View.OnClickListener btnConfirmarEnvioCodigosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( m_Listener != null) {
                m_Listener.onClickSolicitarCodigoConfirmacion();
            }
        }
    };

    View.OnClickListener btnAtrassetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( m_Listener != null) {
                m_Listener.onClickAtrasLugarEmpleo();
            }
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
                m_Listener.onClickContinuarLugarEmpleo( etPinSeguridad.getText().toString() );
            }
        }
    };

    protected void ActualizarZonas( Spinner spinner, int nNivel, int nTipo, int nFiltro , Context context, boolean bDistrito, int nDepartamento ){
        if ( bDistrito )
            new ZonasDAO().rellenaSpinnerDistrito( spinner, nDepartamento, context, nFiltro );
        else
            new ZonasDAO().rellenaSpinner( spinner, nNivel, context, nFiltro );
        spinner.setSelection( Utilidades.getCursorSpinnerPositionById( spinner, 0 ) );
    }

    protected void CargarDatos( boolean estado ){

        etDireccionEmpleo.setText( m_personaInicializar.getcDirValor1Empleo() );
        etNroMzEmpleo.setText( m_personaInicializar.getcDirValor2Empleo() );
        etDptoLtEmpleo.setText( m_personaInicializar.getcDirValor3Empleo() );

        m_IdspDepartamento = Integer.valueOf( m_personaInicializar.getcDepartamentoEmpleo() );
        m_IdspProvincia = Integer.valueOf( m_personaInicializar.getcProvinciaEmpleo() );
        m_IdspDistrito = Integer.valueOf( m_personaInicializar.getcDistritoEmpleo() );

        if ( m_IdspDepartamento > Constantes.CERO ) {
            //spDepartamento.setSelection(Utilidades.getCursorSpinnerPositionById( spDepartamento, m_IdspDepartamento ) );
            new ZonasDAO().rellenaSpinner( spDepartamentoEmpleo, Constantes.UNO, getActivity(), Constantes.UNO );
            spDepartamentoEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamentoEmpleo, m_IdspDepartamento ) );
        }
        if ( m_IdspProvincia > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinner( spProvinciaEmpleo, Constantes.DOS, getActivity(), m_IdspDepartamento );
            spProvinciaEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spProvinciaEmpleo, m_IdspProvincia ) );
        }
        if ( m_IdspDistrito > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinnerDistrito( spDistritoEmpleo, m_IdspDepartamento, getActivity(), m_IdspProvincia );
            spDistritoEmpleo.setSelection(Utilidades.getCursorSpinnerPositionById( spDistritoEmpleo, m_IdspDistrito ) );
        }

        if ( Integer.valueOf( m_personaInicializar.getnDirTipo2Empleo() ) > 0 )
            spNroMzEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spNroMzEmpleo, Integer.valueOf( m_personaInicializar.getnDirTipo2Empleo() ) ) );
        if ( Integer.valueOf( m_personaInicializar.getnDirTipo3Empleo() ) > 0 )
            spDptoLtEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDptoLtEmpleo, Integer.valueOf( m_personaInicializar.getnDirTipo3Empleo() ) ) );
        if ( Integer.valueOf( m_personaInicializar.getnDirTipo1Empleo() ) > 0 )
            spTipoDireccionEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spTipoDireccionEmpleo, Integer.valueOf( m_personaInicializar.getnDirTipo1Empleo() ) ) );

    }

    public interface IEventsListener {
        public void onClickAtrasLugarEmpleo();
        public void onClickContinuarLugarEmpleo( String numeroConfirmar );
        public void onClickSolicitarCodigoConfirmacion();
    }

    public void setControlador( ActivityDatosPersona sectionsPagerAdapter ) {
        m_actividadPadre = sectionsPagerAdapter;
    }

    public boolean validandoDatos() {
        boolean bValidate = false;

        if ( (int) spDistritoEmpleo.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( (int) spNroMzEmpleo.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( etNroMzEmpleo.getText().toString().trim().equals( "" ) )
        {
            inputlayoutNroMzEmpleo.setError( "Nro/Mz"  );
            bValidate = true;
            return bValidate;
        }

        if ( (int) spDptoLtEmpleo.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }
        /*
        if ( etDptoLtEmpleo.getText().toString().trim().equals( "" ) )
        {
            inputlayoutDptoLoteEmpleo.setError( "Dpto/Lote"  );
            bValidate = true;
            return bValidate;
        }
        */

        if ( (int) spTipoDireccionEmpleo.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( etDireccionEmpleo.getText().toString().trim().equals( "" ) || etDireccionEmpleo.getText().length() < Constantes.TRES )
        {
            inputlayoutDireccionEmpleo.setError( "Dirección empleo"  );
            bValidate = true;
            return bValidate;
        }

        if ( !cbxAutorizoDatos.isChecked() ){
            Toast.makeText( m_actividadPadre, "Autorizar uso de datos personales.", Toast.LENGTH_SHORT).show();
            bValidate = true;
            return bValidate;
        }
        if ( !cbxAutorizoEnvio.isChecked() ){
            Toast.makeText( m_actividadPadre, "Autorizar envios de información.", Toast.LENGTH_SHORT).show();
            bValidate = true;
            return bValidate;
        }

        //validar los datos la confirmacion del codigo de numero si es editable
        if ( m_bEditar ){
            if ( etPinSeguridad.getText().toString().trim().equals( "" ) || etPinSeguridad.getText().length() < Constantes.TRES )
            {
                Toast.makeText( m_actividadPadre, "Ingresar el pin de seguridad.", Toast.LENGTH_SHORT).show();
                bValidate = true;
                return bValidate;
            }
        }
        //end


        return bValidate;
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
            }
        }
        else{
            if ( (int) spDepartamentoEmpleo.getSelectedItemId() < Constantes.DIEZ  )
                persona.setcDepartamentoEmpleo( "0" + String.valueOf ( (int) spDepartamentoEmpleo.getSelectedItemId() ) );
            else
                persona.setcDepartamentoEmpleo( String.valueOf ( (int) spDepartamentoEmpleo.getSelectedItemId() ) );
            if ( (int) spProvinciaEmpleo.getSelectedItemId() < Constantes.DIEZ  )
                persona.setcProvinciaEmpleo( "0" + String.valueOf ( (int) spProvinciaEmpleo.getSelectedItemId() ) );
            else
                persona.setcProvinciaEmpleo( String.valueOf ( (int) spProvinciaEmpleo.getSelectedItemId() ) );
            if ( (int) spDistritoEmpleo.getSelectedItemId() < Constantes.DIEZ  )
                persona.setcDistritoEmpleo( "0" + String.valueOf ( (int) spDistritoEmpleo.getSelectedItemId() ) );
            else
                persona.setcDistritoEmpleo( String.valueOf ( (int) spDistritoEmpleo.getSelectedItemId() ) );

            String cCodZonaEmpleo = ( persona.getcDepartamentoEmpleo() + persona.getcProvinciaEmpleo() + persona.getcDistritoEmpleo() + "000000" );
            persona.setcCodZonaEmpleo( cCodZonaEmpleo );

            persona.setcDirValor2Empleo( etNroMzEmpleo.getText().toString() );
            persona.setcDirValor3Empleo( etDptoLtEmpleo.getText().toString() );
            persona.setnDirTipo2Empleo( String.valueOf( (int) spNroMzEmpleo.getSelectedItemId() ) );
            persona.setnDirTipo3Empleo( String.valueOf( (int) spDptoLtEmpleo.getSelectedItemId() ) );

            persona.setnDirTipo1Empleo( String.valueOf( (int) spTipoDireccionEmpleo.getSelectedItemId() ) );
            persona.setcDirEmpleo( etDireccionEmpleo.getText().toString() );
            persona.setcDirValor1Empleo( etDireccionEmpleo.getText().toString() );

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

        etDireccionEmpleo.setText( persona.getcDirValor1Empleo() );
        etNroMzEmpleo.setText( persona.getcDirValor2Empleo() );
        etDptoLtEmpleo.setText( persona.getcDirValor3Empleo() );

        m_IdspDepartamento = Integer.valueOf( persona.getcDepartamentoEmpleo() );
        m_IdspProvincia = Integer.valueOf( persona.getcProvinciaEmpleo() );
        m_IdspDistrito = Integer.valueOf( persona.getcDistritoEmpleo() );

        if ( m_IdspDepartamento > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinner( spDepartamentoEmpleo, Constantes.UNO, getActivity(), Constantes.UNO );
            spDepartamentoEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamentoEmpleo, m_IdspDepartamento ) );
        }
        if ( m_IdspProvincia > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinner( spProvinciaEmpleo, Constantes.DOS, getActivity(), m_IdspDepartamento );
            spProvinciaEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spProvinciaEmpleo, m_IdspProvincia ) );
        }
        if ( m_IdspDistrito > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinnerDistrito( spDistritoEmpleo, m_IdspDepartamento, getActivity(), m_IdspProvincia );
            spDistritoEmpleo.setSelection(Utilidades.getCursorSpinnerPositionById( spDistritoEmpleo, m_IdspDistrito ) );
        }

        if ( Integer.valueOf( persona.getnDirTipo2Empleo() ) > 0 )
            spNroMzEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spNroMzEmpleo, Integer.valueOf( persona.getnDirTipo2Empleo() ) ) );
        if ( Integer.valueOf( persona.getnDirTipo3Empleo() ) > 0 )
            spDptoLtEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spDptoLtEmpleo, Integer.valueOf( persona.getnDirTipo3Empleo() ) ) );
        if ( Integer.valueOf( persona.getnDirTipo1Empleo() ) > 0 )
            spTipoDireccionEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spTipoDireccionEmpleo, Integer.valueOf( persona.getnDirTipo1Empleo() ) ) );

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if ( context instanceof  IEventsListener) {
            m_Listener = ( IEventsListener) context;
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

    private Spinner spDepartamentoEmpleo,spProvinciaEmpleo,spDistritoEmpleo,spNroMzEmpleo,spDptoLtEmpleo,spTipoDireccionEmpleo;
    private EditText etNroMzEmpleo,etDptoLtEmpleo,etDireccionEmpleo,etPinSeguridad;
    private Button btnConfirmarEnvioCodigo,btnAtras,btnContinuar;
    private CheckBox cbxAutorizoDatos, cbxAutorizoEnvio;
    private TextView tvLinkTerminos;
    private TextInputLayout inputlayoutNroMzEmpleo, inputlayoutDptoLoteEmpleo, inputlayoutDireccionEmpleo;
    private LinearLayout lyCodigoVerificadorNumero;

}
