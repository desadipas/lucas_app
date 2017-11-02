package com.tibox.lucas.activity.tabs;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.tibox.lucas.R;
import com.tibox.lucas.activity.ActivityDatosPersona;
import com.tibox.lucas.dao.CatalagoCodigosDAO;
import com.tibox.lucas.network.dto.DatosSalida.Persona;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by desa02 on 18/09/2017.
 */

public class DatosPersonaTabDatosEmpleo extends Fragment {

    protected boolean m_isVistasInicializadas;
    protected boolean m_isPersonaInicializadas;

    protected Persona m_personaInicializar;
    protected ActivityDatosPersona m_actividadPadre;
    private IEventsListener m_Listener;

    private int dayL, monthL ,yearL;
    protected Boolean m_bEditar;

    public static DatosPersonaTabDatosEmpleo newsInstance( boolean bEditar, Persona persona ){
        DatosPersonaTabDatosEmpleo f = new DatosPersonaTabDatosEmpleo();
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
        return inflater.inflate( R.layout.tab_datos_personales_datos_empleo, container, false );
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        m_isVistasInicializadas = true;
        Calendar calendar = Calendar.getInstance();
        yearL = calendar.get( Calendar.YEAR );
        monthL = calendar.get( Calendar.MONTH );
        dayL = calendar.get( Calendar.DAY_OF_MONTH );

        spActEconomica = (Spinner) view.findViewById( R.id.sp_act_eco );
        spSitLaboral = (Spinner) view.findViewById( R.id.sp_situ_lab );
        spProfesion = (Spinner) view.findViewById( R.id.sp_profesion );
        spTipoEmpleo = (Spinner) view.findViewById( R.id.sp_tipo_empleo );

        etRuc = (EditText) view.findViewById( R.id.et_ruc );
        etCentroLaboral = (EditText) view.findViewById( R.id.et_centro_laboral );
        etFechaIngLaboral = (EditText) view.findViewById( R.id.et_fecha_ing_laboral );
        etTelefonoEmpleo = (EditText) view.findViewById( R.id.et_telefono_empleo );
        etIngresoDeclaradoEmpleo = (EditText) view.findViewById( R.id.et_ingreso_declarado_empleo );

        ibtnFechaLaboral = (ImageButton) view.findViewById( R.id.ibtn_fecha_laboral );

        btnAtras = (Button) view.findViewById( R.id.btn_atras_datos_empleo );
        btnContinuar = (Button) view.findViewById( R.id.btn_continuar_datos_empleo );

        inputlayoutRuc = (TextInputLayout) view.findViewById( R.id.input_layout_ruc );
        inputlayoutCentroLaboral = (TextInputLayout) view.findViewById( R.id.input_layout_centro_laboral );
        inputlayoutTelefonoEmpleo = (TextInputLayout) view.findViewById( R.id.input_layout_telefono_empleo );
        inputlayoutIngresoDeclarado = (TextInputLayout) view.findViewById( R.id.input_layout_ingreso_declarado );
        inputlayoutFechaIngLaboral = (TextInputLayout) view.findViewById( R.id.input_layout_fecha_ing_laboral );

        new CatalagoCodigosDAO().rellenaSpinner( spActEconomica, Constantes.TIPO_ACT_ECO, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spSitLaboral, Constantes.TIPO_EST_LABORAL, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spProfesion, Constantes.TIPO_PROFESION, view.getContext() );
        new CatalagoCodigosDAO().rellenaSpinner( spTipoEmpleo, Constantes.TIPO_EMPLEO, view.getContext() );

        ibtnFechaLaboral.setOnClickListener( ibtnFechaLaboralsetOnClickListener );
        btnAtras.setOnClickListener( btnAtrassetOnClickListener );
        btnContinuar.setOnClickListener( btnContinuarsetOnClickListener );

        etRuc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutRuc.setError(null); // hide error
            }
        });
        etCentroLaboral.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutCentroLaboral.setError(null); // hide error
            }
        });
        etFechaIngLaboral.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutFechaIngLaboral.setError(null); // hide error
            }
        });
        etTelefonoEmpleo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutTelefonoEmpleo.setError(null); // hide error
            }
        });
        etIngresoDeclaradoEmpleo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                inputlayoutIngresoDeclarado.setError(null); // hide error
            }
        });

        //if ( m_bEditar )
          //  CargarDatos( m_bEditar );

        spTipoEmpleo.setOnItemSelectedListener( spTipoEmpleosetOnItemSelectedListener );

        if ( ( m_personaInicializar != null ) && !m_isPersonaInicializadas ) {
            procesarPersonaInicializar( m_personaInicializar );
        }

    }

    AdapterView.OnItemSelectedListener spTipoEmpleosetOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if( (int) spTipoEmpleo.getSelectedItemId() == Constantes.DOS )
                inputlayoutRuc.setError( null );
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    View.OnClickListener btnAtrassetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( m_Listener != null) {
                m_Listener.onClickAtrasDatosEmpleo();
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
                m_Listener.onClickContinuarDatosEmpleo();
            }
        }
    };

    View.OnClickListener ibtnFechaLaboralsetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DatePickerDialog fecha = new DatePickerDialog( view.getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int Year, int monthOfYear, int dayOfMonth) {
                    etFechaIngLaboral.setText( dayOfMonth + "/" + ( monthOfYear + 1 ) + "/" + Year );
                    yearL = Year;
                    monthL = monthOfYear;
                    dayL = dayOfMonth;
                }
            }, yearL, monthL, dayL );
            fecha.getDatePicker().setMaxDate( new Date().getTime() );
            fecha.show();
        }
    };

    protected void CargarDatos( boolean estado ){

        etRuc.setText( m_personaInicializar.getcRuc() );
        etCentroLaboral.setText( m_personaInicializar.getcNomEmpresa() );//
        etFechaIngLaboral.setText( m_personaInicializar.getdFecIngrLab() );
        etTelefonoEmpleo.setText( m_personaInicializar.getcTelfEmpleo() );
        etIngresoDeclaradoEmpleo.setText( String.valueOf( m_personaInicializar.getnIngresoDeclado() ) );

        if ( m_personaInicializar.getnCUUI() != null )
            if ( Integer.parseInt( m_personaInicializar.getnCUUI() ) > Constantes.CERO )
                spActEconomica.setSelection( Utilidades.getCursorSpinnerPositionById( spActEconomica, Integer.parseInt( m_personaInicializar.getnCUUI() ) ) );
        if ( Integer.valueOf( m_personaInicializar.getnSitLab() ) > Constantes.CERO )
            spSitLaboral.setSelection( Utilidades.getCursorSpinnerPositionById( spSitLaboral, Integer.valueOf( m_personaInicializar.getnSitLab() ) ) );
        if ( Integer.valueOf( m_personaInicializar.getnProfes() ) > Constantes.CERO )
            spProfesion.setSelection( Utilidades.getCursorSpinnerPositionById( spProfesion, Integer.valueOf( m_personaInicializar.getnProfes() ) ) );
        if ( Integer.valueOf( m_personaInicializar.getnTipoEmp() ) > Constantes.CERO )
            spTipoEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spTipoEmpleo, Integer.valueOf( m_personaInicializar.getnTipoEmp() ) ) );

    }

    public interface IEventsListener {
        public void onClickAtrasDatosEmpleo();
        public void onClickContinuarDatosEmpleo();
    }

    public void setControlador( ActivityDatosPersona sectionsPagerAdapter ) {
        m_actividadPadre = sectionsPagerAdapter;
    }

    public boolean validandoDatos() {
        boolean bValidate = false;

        if ( (int) spActEconomica.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( (int) spSitLaboral.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( (int) spProfesion.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( (int) spTipoEmpleo.getSelectedItemId() == 0 )
        {
            bValidate = true;
            return bValidate;
        }

        if ( (int) spTipoEmpleo.getSelectedItemId() == Constantes.UNO )
        {
            if ( etRuc.getText().toString().trim().equals( "" ) )
            {
                inputlayoutRuc.setError( "N° de RUC."  );
                bValidate = true;
                return bValidate;
            }
            else if ( etRuc.getText().length() < Constantes.ONCE )
            {
                inputlayoutRuc.setError( "N° de RUC debe de tener 11 digitos."  );
                bValidate = true;
                return bValidate;
            }

            String ruc2digitos = etRuc.getText().toString().substring(0, 2);
            if ( ruc2digitos.trim().equals( "10" ) || ruc2digitos.trim().equals( "20" ) ) {}
            else{
                inputlayoutRuc.setError( "N° de RUC debe de empezar con 10 o 20." );
                bValidate = true;
                return bValidate;
            }
        }
        else
            inputlayoutRuc.setError( null );

        if ( etFechaIngLaboral.getText().toString().trim().equals( "" ) )
        {
            inputlayoutFechaIngLaboral.setError( "Fecha ing. laboral"  );
            bValidate = true;
            return bValidate;
        }

        if ( etTelefonoEmpleo.getText().toString().trim().equals( "" ) )
        {
            inputlayoutTelefonoEmpleo.setError( "Telefono empleo"  );
            bValidate = true;
            return bValidate;
        }

        if ( etIngresoDeclaradoEmpleo.getText().toString().trim().equals( "" ) )
        {
            inputlayoutIngresoDeclarado.setError( "Ingreso declarado"  );
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
            persona.setnCUUI( String.valueOf( (int) spActEconomica.getSelectedItemId() ) );
            persona.setnSitLab( String.valueOf( (int) spSitLaboral.getSelectedItemId() ) );
            persona.setnProfes( String.valueOf( (int) spProfesion.getSelectedItemId() ) );
            persona.setnTipoEmp( String.valueOf( (int) spTipoEmpleo.getSelectedItemId() ) );

            persona.setcRuc( etRuc.getText().toString() );
            persona.setcNomEmpresa( etCentroLaboral.getText().toString() );
            persona.setdFecIngrLab( etFechaIngLaboral.getText().toString() );
            persona.setcTelfEmpleo( etTelefonoEmpleo.getText().toString() );
            persona.setnIngresoDeclado( Double.parseDouble( etIngresoDeclaradoEmpleo.getText().toString() ) );
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

        etRuc.setText( persona.getcRuc() );
        etCentroLaboral.setText( persona.getcNomEmpresa() );//
        etFechaIngLaboral.setText( persona.getdFecIngrLab() );
        etTelefonoEmpleo.setText( persona.getcTelfEmpleo() );
        etIngresoDeclaradoEmpleo.setText( String.valueOf( persona.getnIngresoDeclado() ) );

        if ( persona.getnCUUI() != null )
            if ( Integer.parseInt( persona.getnCUUI() ) > Constantes.CERO )
                spActEconomica.setSelection( Utilidades.getCursorSpinnerPositionById( spActEconomica, Integer.parseInt( persona.getnCUUI() ) ) );
        if ( Integer.valueOf( persona.getnSitLab() ) > Constantes.CERO )
            spSitLaboral.setSelection( Utilidades.getCursorSpinnerPositionById( spSitLaboral, Integer.valueOf( persona.getnSitLab() ) ) );
        if ( Integer.valueOf( persona.getnProfes() ) > Constantes.CERO )
            spProfesion.setSelection( Utilidades.getCursorSpinnerPositionById( spProfesion, Integer.valueOf( persona.getnProfes() ) ) );
        if ( Integer.valueOf( persona.getnTipoEmp() ) > Constantes.CERO )
            spTipoEmpleo.setSelection( Utilidades.getCursorSpinnerPositionById( spTipoEmpleo, Integer.valueOf( persona.getnTipoEmp() ) ) );

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

    private Spinner spActEconomica,spSitLaboral,spProfesion,spTipoEmpleo;
    private EditText etRuc,etCentroLaboral,etFechaIngLaboral,etTelefonoEmpleo,etIngresoDeclaradoEmpleo;
    private ImageButton ibtnFechaLaboral;
    private Button btnContinuar, btnAtras;
    private TextInputLayout inputlayoutRuc, inputlayoutCentroLaboral, inputlayoutTelefonoEmpleo, inputlayoutIngresoDeclarado, inputlayoutFechaIngLaboral;

}
