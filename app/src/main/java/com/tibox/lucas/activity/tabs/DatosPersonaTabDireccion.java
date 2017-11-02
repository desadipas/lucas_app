package com.tibox.lucas.activity.tabs;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tibox.lucas.R;
import com.tibox.lucas.activity.ActivityDatosPersona;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.dao.ZonasDAO;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;
import com.weiwangcn.betterspinner.library.BetterSpinner;

/**
 * Created by desa02 on 18/09/2017.
 */

public class DatosPersonaTabDireccion extends Fragment {

    protected boolean m_isVistasInicializadas;
    protected boolean m_isPersonaInicializadas;

    protected ActivityDatosPersona m_actividadPadre;
    protected Persona m_personaInicializar;
    private IEventsListener m_Listener;

    protected Boolean m_bEditar;
    private int m_IdspDepartamentoIndex = 0;
    private int m_IdspProvinciaIndex = 0;
    private int m_IdspDistritoIndex = 0;

    private int m_IdspDepartamento = 0;
    private int m_IdspProvincia = 0;
    private int m_IdspDistrito = 0;



    public static DatosPersonaTabDireccion newsInstance( boolean bEditar, Persona persona ){
        DatosPersonaTabDireccion f = new DatosPersonaTabDireccion();
        Bundle args = new Bundle();
        args.putBoolean( "editar", bEditar );
        args.putSerializable( "datos", persona );
        f.setArguments( args );
        return f;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
        return inflater.inflate( R.layout.tab_datos_personales_direccion, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_isVistasInicializadas = true;

        spDepartamento = (Spinner) view.findViewById( R.id.sp_departamento );
        spProvincia = (Spinner) view.findViewById( R.id.sp_provincia );
        spDistrito = (Spinner) view.findViewById( R.id.sp_distrito );
        spNroMz = (Spinner) view.findViewById( R.id.sp_nro_mz );
        spDptoLt = (Spinner) view.findViewById( R.id.sp_dpto_lt );
        spTipoResidencia = (Spinner) view.findViewById( R.id.sp_tipo_residencia );
        spTipoDireccion = (Spinner) view.findViewById( R.id.sp_tipo_direccion );

        etNroMz = (EditText) view.findViewById( R.id.et_nro_mz );
        etDptoLt = (EditText) view.findViewById( R.id.et_dpto_lt );
        etDireccion = (EditText) view.findViewById( R.id.et_direccion );

        btnAtras = (Button) view.findViewById( R.id.btn_atras_direccion );
        btnContinuar = (Button) view.findViewById( R.id.btn_continuar_direccion );

        inputlayoutDptoLote = (TextInputLayout) view.findViewById( R.id.input_layout_dpto_lt );
        inputlayoutNroMz = (TextInputLayout) view.findViewById( R.id.input_layout_nro_mz );
        inputlayoutDireccion = (TextInputLayout) view.findViewById( R.id.input_layout_direccion );

        //new ZonasDAO().rellenaSpinner( spDepartamento, Constantes.UNO, view.getContext(), Constantes.UNO );
        //spDepartamento.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamento, m_IdspDepartamento ) );

        //new ZonasDAO().rellenaSpinner( spProvincia, Constantes.DOS, view.getContext(), Constantes.DOS );
        //new ZonasDAO().rellenaSpinner( spDistrito, Constantes.TRES, view.getContext(), Constantes.TRES );

        new CatalagoCodigosDAO().rellenaSpinner( spNroMz, Constantes.TIPO_NRO, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spDptoLt, Constantes.TIPO_DPTO, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spTipoResidencia, Constantes.TIPO_RESIDENCIA, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spTipoDireccion, Constantes.TIPO_DIRECCION, view.getContext() );

        //spDepartamento.setOnItemSelectedListener( spDepartamentosetOnItemSelectedListener );
        spProvincia.setOnItemSelectedListener( spProvinciasetOnItemSelectedListener );
        spDistrito.setOnItemSelectedListener( spDistritosetOnItemSelectedListener );

        btnAtras.setOnClickListener( btnAtrassetOnClickListener );
        btnContinuar.setOnClickListener( btnContinuarsetOnClickListener );

        etNroMz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutNroMz.setError(null); // hide error
            }
        });
        etDptoLt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutDptoLote.setError(null); // hide error
            }
        });
        etDireccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutDireccion.setError(null); // hide error
            }
        });

        /*
        if ( m_bEditar )
            CargarDatos( m_bEditar );
        else {
            new ZonasDAO().rellenaSpinner( spDepartamento, Constantes.UNO, view.getContext(), Constantes.UNO );
            spDepartamento.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamento, m_IdspDepartamento ) );
        }
        */

        spDepartamento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //m_IdspDepartamento = (int) spDepartamento.getSelectedItemId();
                //ActualizarZonas( spProvincia, Constantes.DOS, Constantes.DOS, m_IdspDepartamento, getActivity(), false, 0 );

                if ( m_IdspDepartamento != 0 ){
                    if(  m_IdspDepartamento != (int) spDepartamento.getSelectedItemId() ){
                        m_IdspDepartamento = (int) spDepartamento.getSelectedItemId();
                        ActualizarZonas( spProvincia, Constantes.DOS, Constantes.DOS, m_IdspDepartamento, getActivity(), false, 0 );
                    }
                    else
                    {
                        if ( m_IdspProvincia > Constantes.CERO ) {
                            new ZonasDAO().rellenaSpinner( spProvincia, Constantes.DOS, getActivity(), m_IdspDepartamento );
                            spProvincia.setSelection( Utilidades.getCursorSpinnerPositionById( spProvincia, m_IdspProvincia ) );
                        }


                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //

        if( !m_bEditar ){
            if ( m_IdspDepartamento == 0 ){
                m_IdspDepartamento = Constantes.UNO;
                m_IdspProvincia = Constantes.UNO;
                m_IdspDistrito = Constantes.UNO;
            }
        }

        new ZonasDAO().rellenaSpinner( spDepartamento, Constantes.UNO, view.getContext(), Constantes.UNO );
        spDepartamento.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamento, m_IdspDepartamento ) );

        if ( ( m_personaInicializar != null ) && !m_isPersonaInicializadas ) {
            procesarPersonaInicializar( m_personaInicializar );
        }

    }

    AdapterView.OnItemSelectedListener spDistritosetOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            m_IdspDistrito = (int) spDistrito.getSelectedItemId();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    View.OnClickListener btnAtrassetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( m_Listener != null) {
                m_Listener.onClickAtrasDireccion();
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
                m_Listener.onClickContinuarDireccion();
            }
        }
    };

    /*
    AdapterView.OnItemSelectedListener spDepartamentosetOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            m_IdspDepartamento = (int) spDepartamento.getSelectedItemId();
            ActualizarZonas( spProvincia, Constantes.DOS, Constantes.DOS, m_IdspDepartamento, getActivity(), false, 0 );

        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };
    */

    AdapterView.OnItemSelectedListener spProvinciasetOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            //m_IdspProvincia = (int) spProvincia.getSelectedItemId();
            //ActualizarZonas( spDistrito, Constantes.TRES, Constantes.TRES, m_IdspProvincia, getActivity(), true, m_IdspDepartamento );

            if ( m_IdspProvincia != 0 ){
                if(  m_IdspProvincia != (int) spProvincia.getSelectedItemId() ){
                    m_IdspProvincia = (int) spProvincia.getSelectedItemId();
                    ActualizarZonas( spDistrito, Constantes.TRES, Constantes.TRES, m_IdspProvincia, getActivity(), true, m_IdspDepartamento );
                }
                else{
                    if ( m_IdspDistrito > Constantes.CERO ) {
                        new ZonasDAO().rellenaSpinnerDistrito( spDistrito, m_IdspDepartamento, getActivity(), m_IdspProvincia );
                        spDistrito.setSelection(Utilidades.getCursorSpinnerPositionById( spDistrito, m_IdspDistrito ) );
                    }
                }
            }

        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
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

        etDireccion.setText( m_personaInicializar.getcDirValor1() );
        etNroMz.setText( m_personaInicializar.getcDirValor2() );
        etDptoLt.setText( m_personaInicializar.getcDirValor3() );

        m_IdspDepartamento = Integer.valueOf( m_personaInicializar.getcDepartamento() );
        m_IdspProvincia = Integer.valueOf( m_personaInicializar.getcProvincia() );
        m_IdspDistrito = Integer.valueOf( m_personaInicializar.getcDistrito() );

        if ( m_IdspDepartamento > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinner( spDepartamento, Constantes.UNO, getActivity(), Constantes.UNO );
            spDepartamento.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamento, m_IdspDepartamento ) );
        }
        if ( m_IdspProvincia > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinner( spProvincia, Constantes.DOS, getActivity(), m_IdspDepartamento );
            spProvincia.setSelection( Utilidades.getCursorSpinnerPositionById( spProvincia, m_IdspProvincia ) );
        }
        if ( m_IdspDistrito > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinnerDistrito( spDistrito, m_IdspDepartamento, getActivity(), m_IdspProvincia );
            spDistrito.setSelection(Utilidades.getCursorSpinnerPositionById( spDistrito, m_IdspDistrito ) );
        }

        if ( Integer.valueOf( m_personaInicializar.getnTipoResidencia() ) > 0 )
            spTipoResidencia.setSelection( Utilidades.getCursorSpinnerPositionById( spTipoResidencia, Integer.valueOf( m_personaInicializar.getnTipoResidencia() ) ) );
        if ( Integer.valueOf( m_personaInicializar.getnDirTipo1() ) > 0 )
            spTipoDireccion.setSelection( Utilidades.getCursorSpinnerPositionById( spTipoDireccion, Integer.valueOf( m_personaInicializar.getnDirTipo1() ) ) );


    }

    public interface IEventsListener {
        public void onClickAtrasDireccion();
        public void onClickContinuarDireccion();
    }

    public void setControlador( ActivityDatosPersona sectionsPagerAdapter ) {
        m_actividadPadre = sectionsPagerAdapter;
    }

    public boolean validandoDatos(){
        boolean bValidate = false;

        if ( (int) spDistrito.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( (int) spNroMz.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( etNroMz.getText().toString().trim().equals( "" ) )
        {
            inputlayoutNroMz.setError( "Nro/Mz"  );
            bValidate = true;
            return bValidate;
        }

        if ( (int) spDptoLt.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        /*
        if ( etDptoLt.getText().toString().trim().equals( "" ) )
        {
            inputlayoutDptoLote.setError( "Dpto/Lote"  );
            bValidate = true;
            return bValidate;
        }
        */

        if ( (int) spTipoDireccion.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( etDireccion.getText().toString().trim().equals( "" ) || etDireccion.getText().length() < Constantes.TRES )
        {
            inputlayoutDireccion.setError( "Dirección"  );
            bValidate = true;
            return bValidate;
        }

        if ( (int) spTipoResidencia.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }



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
            if ( (int) spDepartamento.getSelectedItemId() < Constantes.DIEZ  )
                persona.setcDepartamento( "0" + String.valueOf ( (int) spDepartamento.getSelectedItemId() ) );
            else
                persona.setcDepartamento( String.valueOf ( (int) spDepartamento.getSelectedItemId() ) );
            if ( (int) spProvincia.getSelectedItemId() < Constantes.DIEZ  )
                persona.setcProvincia( "0" + String.valueOf ( (int) spProvincia.getSelectedItemId() ) );
            else
                persona.setcProvincia( String.valueOf ( (int) spProvincia.getSelectedItemId() ) );
            if ( (int) spDistrito.getSelectedItemId() < Constantes.DIEZ  )
                persona.setcDistrito( "0" + String.valueOf ( (int) spDistrito.getSelectedItemId() ) );
            else
                persona.setcDistrito( String.valueOf ( (int) spDistrito.getSelectedItemId() ) );
            String cCodZona = ( persona.getcDepartamento() + persona.getcProvincia() + persona.getcDistrito() + "000000" );
            persona.setcCodZona( cCodZona );

            persona.setnDirTipo2( String.valueOf( (int) spNroMz.getSelectedItemId() ) );
            persona.setnDirTipo3( String.valueOf( (int) spDptoLt.getSelectedItemId() ) );
            persona.setcDirValor2( etNroMz.getText().toString() );
            persona.setcDirValor3( etDptoLt.getText().toString() );

            persona.setnDirTipo1( String.valueOf( (int) spTipoDireccion.getSelectedItemId() ) );
            persona.setcDirValor1( etDireccion.getText().toString() );
            persona.setnTipoResidencia( String.valueOf( (int) spTipoResidencia.getSelectedItemId() ) );

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

        etDireccion.setText( persona.getcDirValor1() );
        etNroMz.setText( persona.getcDirValor2() );
        etDptoLt.setText( persona.getcDirValor3() );

        if ( m_bEditar ) {
            m_IdspDepartamento = Integer.valueOf(m_personaInicializar.getcDepartamento());
            m_IdspProvincia = Integer.valueOf(m_personaInicializar.getcProvincia());
            m_IdspDistrito = Integer.valueOf(m_personaInicializar.getcDistrito());
        }
        else
        {
            m_IdspDepartamento = Constantes.UNO;
            m_IdspProvincia = Constantes.UNO;
            m_IdspDistrito = Constantes.UNO;
        }

        if ( m_IdspDepartamento > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinner( spDepartamento, Constantes.UNO, getActivity(), Constantes.UNO );
            spDepartamento.setSelection( Utilidades.getCursorSpinnerPositionById( spDepartamento, m_IdspDepartamento ) );
        }
        if ( m_IdspProvincia > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinner( spProvincia, Constantes.DOS, getActivity(), m_IdspDepartamento );
            spProvincia.setSelection( Utilidades.getCursorSpinnerPositionById( spProvincia, m_IdspProvincia ) );
        }
        if ( m_IdspDistrito > Constantes.CERO ) {
            new ZonasDAO().rellenaSpinnerDistrito( spDistrito, m_IdspDepartamento, getActivity(), m_IdspProvincia );
            spDistrito.setSelection(Utilidades.getCursorSpinnerPositionById( spDistrito, m_IdspDistrito ) );
        }

        if ( Integer.valueOf( m_personaInicializar.getnTipoResidencia() ) > 0 )
            spTipoResidencia.setSelection( Utilidades.getCursorSpinnerPositionById( spTipoResidencia, Integer.valueOf( m_personaInicializar.getnTipoResidencia() ) ) );
        if ( Integer.valueOf( m_personaInicializar.getnDirTipo1() ) > 0 )
            spTipoDireccion.setSelection( Utilidades.getCursorSpinnerPositionById( spTipoDireccion, Integer.valueOf( m_personaInicializar.getnDirTipo1() ) ) );


    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_Listener = null;
    }

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

    private Spinner spDepartamento,spProvincia,spDistrito,spNroMz,spDptoLt,spTipoResidencia,spTipoDireccion;
    private EditText etNroMz,etDptoLt,etDireccion;
    private Button btnAtras,btnContinuar;
    private TextInputLayout inputlayoutNroMz, inputlayoutDptoLote, inputlayoutDireccion;

}
