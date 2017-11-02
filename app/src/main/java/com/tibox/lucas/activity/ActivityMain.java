package com.tibox.lucas.activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.dto.DatosEntrada.MontoCuota;
import com.tibox.lucas.network.dto.DatosSalida.Calendario;
import com.tibox.lucas.network.dto.calendario.CalendarioObjt;
import com.tibox.lucas.network.dto.calendario.ObjFecha;
import com.tibox.lucas.R;
import com.tibox.lucas.network.response.CalendarioCreditoResponse;
import com.tibox.lucas.network.response.CalendarioListCreditoResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    Button btnJava;
    TextView tvTea,tvTcea;

    private IAppCreditosWebApi m_webApi;
    private CalendarioListCreditoResponse m_CalendarioListCreditoResponse;


    //String[] ArrayPruebalist;

    List<ObjFecha> ArrayFechasCalendario = new ArrayList<>();
    List<ObjFecha> ArrayPruebalist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJava = (Button) findViewById( R.id.btn_javascript );
        btnJava.setOnClickListener( btnJavasetOnClickListener );
        tvTea = (TextView) findViewById( R.id.tv_tea );
        tvTcea = (TextView) findViewById( R.id.tv_tcea );

    }

    View.OnClickListener btnJavasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //obtenerTCEA();

            if ( 1==1 ){

                //LayoutInflater factory = LayoutInflater.from( ActivityMain.this );
                //final View viewI = factory.inflate(R.layout.image_logo, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage( "No podemos otorgarte un prestamo por ser persona con Cargo Publico." )
                        .setTitle("SoyLucas!")
                        .setCancelable(false)
                        .setNeutralButton("Aceptar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        Toast.makeText( ActivityMain.this, "No podemos otorgar un prestamo por ser Persona con Cargo Publico.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                return;

            }else{

                Toast.makeText( ActivityMain.this, "REGISTRAR TODO LO NECESARIO", Toast.LENGTH_SHORT).show();

            }


        }
    };

    public void CalcularCalendario(){

        Object MatFechas = new Object();

        String[] parts = "09.10.2017".split( "/" );
        Date mydate = new Date( parts[1] + "/" + parts[0] + "/" + parts[2] );
        boolean valor = false;

        Date dFechaDesembolso = mydate;
        Date dFechaParaCalen = dFechaDesembolso;
        dFechaParaCalen =  dFechaParaCalen;

        double nPrestamo = 500;//vm.ParametrosSimulacion.nPrestamo;
        double nMonto = 0.00;
        nMonto = nPrestamo;
        boolean bOK = false;
        boolean bOK1 = false;
        double nMontoCuotaBK = 0.00;
        Date dFecha = dFechaDesembolso;
        double nCapital = 0.00;
        double nPrestamoC = 500;//vm.ParametrosSimulacion.nPrestamo;
        double nValorInc = 0.00;
        double nIGV = 0;
        double pnFinCuotaGracia = 0;
        double pnTipoGracia = 1;
        double tasaCOm = 5.00;
        int nCuotas = 2;//parseInt( vm.ParametrosSimulacion.nCuotas );
        int day = 30;

        if (!valor) {
            ArrayFechasCalendario = ArrayFechas( dFechaDesembolso, nPrestamo, nCuotas, day );
            List<ObjFecha> AFechasCalendario = new ArrayList<>();
            AFechasCalendario = CalendarioFechasCuotaFija( dFechaDesembolso, day, nCuotas );
        }

        int cont = 0;
        double nInteres = 0;
        double nTasaCom = 9;

        double nMontoCuota = 120.00;

        List<ObjFecha> arrays = new ArrayList<>();
        String[][] matDatos = new String[nCuotas + 1][8];
        double MontoCuotaReturn = 0;
        double nMontoNegativo = 0;
        double nMontoDiferenciaNeto = 0;
        double nPendIntComp = 0;
        double nPendComision = 0;
        double nMontoComisionCalculado = 0;
        double nMontoNetoICCOM = 0;
        double pnMontoComision = 0;

        do{

            nMonto = nPrestamo;
            nMontoCuotaBK = nMontoCuota;
            dFecha = dFechaDesembolso;

            int vart = 0;
            for ( int k = 1; (k <= nCuotas); k++ ) {
                nMontoCuota = nMontoCuotaBK;


            }




        } while (!bOK);
        //var calendario = [{ monto: vm.ParametrosSimulacion.nPrestamo * -1, fecha: parts[2] + '-' + parts[1] + '-' + parts[0] }];
    }

    public List<ObjFecha> ArrayFechas( Date dFechaDesembolso, double nPrestamo, int nCuota, int nDias ) {
        String[][] arrayFechas1 = new String[nCuota][2];
        nCuota = nCuota;
        Date dFecha = dFechaDesembolso;
        ObjFecha objFecha = new ObjFecha();//{ nId: 0, dFechaProgamada: null };
        objFecha.setnId( 0 );
        objFecha.setdFechaProgamada( null );
        ObjFecha objFecha1 = new ObjFecha();
        //var objFecha1 = [{ nId: 0, dFechaProgamada: null }];
        objFecha1.setnId( 0 );
        objFecha1.setdFechaProgamada( null );
        for (int i = 1; i <= nCuota; i++) {
            Date dateFecha = new Date( String.valueOf(dFecha) );
            Date fechaP = new Date( String.valueOf(dateFecha) );
            Date NuevaFecha = new Date( String.valueOf(dateFecha) );
            nDias = 30;
            fechaP.setDate( fechaP.getDate() + nDias );
            /*
            for (int j = 0; j < FechaCalendario.length; j++) {
                if (FechaCalendario[j] == fechaP) {
                    nDias = nDias + 1;
                }
            }
            */
            if ( fechaP.getDay() == 0 ) {
                nDias = nDias + 1;
            }

            if ( fechaP.getDay() == 6 ) {
                nDias = nDias + 2;
            }
            /*
            for ( int j = 0; j < FechaCalendario.length; j++ ) {
                if ( FechaCalendario[j] == fechaP) {
                    nDias = nDias + 1;
                }
            }
            */
            NuevaFecha.setDate(NuevaFecha.getDate() + nDias);
            objFecha.setnId( i );//objFecha.nId = i;
            objFecha.setdFechaProgamada( (NuevaFecha) );//objFecha.dFechaProgamada = getDateFormat(NuevaFecha);
            arrayFechas1[i][1] = NuevaFecha.toString();
            dFecha = NuevaFecha;
            ObjFecha objs = new ObjFecha();
            objs.setnId( objFecha.getnId() );
            objs.setdFechaProgamada( objFecha.getdFechaProgamada() );
            //var objs = { id: objFecha.nId, fechaProgr: objFecha.dFechaProgamada }
            ArrayPruebalist.add( objs );//push( objs );
        }
        return ArrayPruebalist;
    }

    public List<ObjFecha> CalendarioFechasCuotaFija( Date dFechaDesembolso, int nDias, int nCuota ) {
        List<ObjFecha> CalendarioFechas = new ArrayList<>();//var CalendarioFechas = [{}];
        Date dFecha = dFechaDesembolso;
        ObjFecha objs = new ObjFecha();

        for ( int i = 1; i <= nCuota; i++ ) {
            objs.setnId( i );
            CalendarioFechas.add( objs ); //{ nId: i } );
            Date dateFecha = new Date( String.valueOf(dFecha) );
            Date fechaP = new Date( String.valueOf(dateFecha) );
            Date NuevaFecha = new Date( String.valueOf(dateFecha) );
            nDias = 30;
            fechaP.setDate(fechaP.getDate() + nDias);
            /*
            for ( int j = 0; j < FechaCalendario.length; j++) {
                if (FechaCalendario[j] == fechaP) {
                    nDias = nDias + 1;
                }
            }
            */
            if (fechaP.getDay() == 0) {
                nDias = nDias + 1;
            }
            if (fechaP.getDay() == 6) {
                nDias = nDias + 2;
            }
            /*
            for ( int j = 0; j < FechaCalendario.length; j++) {
                if (FechaCalendario[j] == fechaP) {
                    nDias = nDias + 1;
                }
            }
            */
            NuevaFecha.setDate( NuevaFecha.getDate() + nDias );
            //CalendarioFechas[i].nId = i;
            //CalendarioFechas[i].dFechaProgamada = getDateFormat(NuevaFecha);
            CalendarioFechas.get(i).setnId( i );
            CalendarioFechas.get(i).setdFechaProgamada( NuevaFecha );
            dFecha = NuevaFecha;
        }
        return CalendarioFechas;//vm.ArrayPruebalist;
    }

    public double obtenerTCEA(){
        double ntcea = 0;

        DateFormat formaterFecha ;
        formaterFecha = new SimpleDateFormat("dd/MM/yyyy");
        List<CalendarioObjt> listcalendario = new ArrayList<>();
        List<CalendarioObjt> CalendarioGenList = new ArrayList<>();
        Date date = null;

        CalendarioObjt calendario = new CalendarioObjt();
        calendario.setMonto( 1000 * -1 );
        try {
            date = formaterFecha.parse( "12/10/2017" );
        }catch ( Exception e ){
            e.getMessage();
        }
        calendario.setFecha( date );

        listcalendario.add( calendario );

        MontoCuota datos = new MontoCuota();
        datos.setdFechaSistema( "12/10/2017" );
        datos.setnNroCuotas( 12 );
        datos.setnPrestamo( 1000 );
        datos.setnPeriodo( 30 );
        datos.setnTasa( 9 );
        datos.setnSeguro( 0.245 );

        ObtenerCalendarioAsync obtenerMontoCuotaAsync = new ObtenerCalendarioAsync( ActivityMain.this, datos, listcalendario );
        obtenerMontoCuotaAsync.execute();





        /*
        try {
            m_CalendarioListCreditoResponse = m_webApi.ObtenerCalendarioList( datos,
                    "I1KgtjkepqR-NIjgsu8hNIsBJLd4THs5sc18Onxexx8bf_HGsP7UY6hUfXZW76AiSYCtS212A7Rmq6_8u_DBBqChRU7PaVXK8PIQndn_HViZ9jsCVDOCSqyqufhviE3udOC6H23-F6dyplbPDCKElhb8Gbp2rWvyj5CQ9BYUoobfxZc9LzIlOeYdxKcz7Zf5Karjq7Vk-_s-8X9Awn0yTBZj5Tv4XKHt0zxd72YPgBlvOSdUOMU1fh6rnuHj9XE-");
            if( !m_CalendarioListCreditoResponse.isM_success() ){
                return 0;
            }
            else{
                for ( int k = 0; k < m_CalendarioListCreditoResponse.getM_listdata().size() ; k++ ){
                    calendario = new CalendarioObjt();
                    calendario.setMonto( Double.parseDouble( m_CalendarioListCreditoResponse.getM_listdata().get( k ).getMontoCuota() ) );
                    calendario.setFecha( formaterFecha.parse( m_CalendarioListCreditoResponse.getM_listdata().get( k ).getcFechaPago() ) );
                    listcalendario.add( calendario );
                }
            }
        }
        catch ( Exception e ){
            e.getMessage();
        }

        for ( int i = 0; i <  CalendarioGenList.size(); i++ ) {
            calendario = new CalendarioObjt();
            calendario.setMonto( CalendarioGenList.get( i ).getMonto() );
            calendario.setFecha( CalendarioGenList.get( i ).getFecha() );
            listcalendario.add( calendario );
        }
        ntcea = calcularTCEA( listcalendario );
        */
        return ntcea;
    }

    public double calcularTCEA( List<CalendarioObjt> calendario ) {
        float irr = Float.parseFloat( "0.1" );
        float pvPrev = hallapvPRev( calendario );
        float pv = hallaPV( irr, 360, calendario );
        float t = 0;
        float irrPrev = 0;

        while ( Math.abs( pv ) >= 0.0001 ) {
            t = irrPrev;

            //irrPrev = Float.parseFloat( String.valueOf( Utilidades.TruncateDecimalOcho( irr ) ) );
            irrPrev = irr;

            irr = irr + ( t - irr ) * pv / ( pv - pvPrev );
            //irr = Float.parseFloat( String.valueOf( Utilidades.TruncateDecimalOcho( irr + ( t - irr ) * pv / ( pv - pvPrev ) ) ) );

            pvPrev = pv;

            pv = hallaPV( irr, 365, calendario );
            //pv = Float.parseFloat( String.valueOf( Utilidades.TruncateDecimalOcho( hallaPV( irr, 365, calendario ) ) ) );
        }

        return( irr * 100.00 );
    }

    public float hallapvPRev( List<CalendarioObjt> calendario ) {
        float suma = 0;
        float amt = 0;
        for ( int index = 0; index < calendario.size(); index++) {
            amt = Float.parseFloat( String.valueOf( calendario.get( index ).getMonto()  ) );
            suma = suma + amt;
        }
        return suma;
    }

    public float hallaPV( double irr, int dias, List<CalendarioObjt> calendario ) {
        DateFormat formater ;
        formater = new SimpleDateFormat("dd-MM-yyyy");

        float suma = 0;
        float amt = 0;
        float exponente = 0;
        float base = 0;
        float potencia = 0;

        for ( int i = 0; i < calendario.size(); i++ ) {
            amt =  Float.parseFloat( String.valueOf( calendario.get( i ).getMonto() ) );
            base = Float.parseFloat( String.valueOf( 1.00 + irr ) );
            exponente = Float.parseFloat( String.valueOf( Utilidades.hallaRestaFecha( calendario.get(i).getFecha(), calendario.get(0).getFecha()  ) / dias ) );
            potencia = Float.parseFloat( String.valueOf( Math.pow( base, exponente ) ) );
            suma = suma + Float.parseFloat( String.valueOf( amt / potencia ) );
        }
        return suma;
    }

    public class ObtenerCalendarioAsync extends AsyncTask<Void, Void, String> {

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";
        private MontoCuota m_datos;
        private CalendarioListCreditoResponse m_CalendarioListCreditoResponse;
        private List<Calendario> m_ListCalendario;
        private List<CalendarioObjt> m_ListCalendarioTotal;

        public ObtenerCalendarioAsync( Context context, MontoCuota datos, List<CalendarioObjt> lista ){
            m_datos = datos;
            m_context = context;
            m_ListCalendarioTotal = lista;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Obteniendo calendario...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            try{
                String resp = "" ;

                if ( !Common.isNetworkConnected(m_context) ) {
                    return "No se encuentra conectado a internet.";
                }

                m_CalendarioListCreditoResponse =  m_webApi.ObtenerCalendarioList( m_datos,
                        "3vJAKOmZCYHJtHhjDh8NE-vSXF4bpUWGVDaKApu2BwAJYXyqqnb7GO3-FJ38J5t-v8smogVsgd6NK97PyxvWYLE0dCDIjubjxOR9-qoVOMb9-OFJ5RIX65m_Mw-8LM7tSaumStw0YpEVTgfJESQCrchlpVOX7vD6j4et0X0vJn5vHHEIVeZ6PGJxdKhYOa33rDAXYRjYnhKuRXnFmMTncDtbRM0eEWjnEObLvyeju35pM7YEMFpQXunwzbNtILCA" );
                if( !m_CalendarioListCreditoResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                    m_ListCalendario = m_CalendarioListCreditoResponse.getM_listdata();
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
                pd.dismiss();
                CalendarioObjt calendario = new CalendarioObjt();
                DateFormat formaterFecha ;
                formaterFecha = new SimpleDateFormat("dd/MM/yyyy");

                for ( int k = 0; k < m_ListCalendario.size() ; k++ ){
                    calendario = new CalendarioObjt();

                    Date date = null;
                    try{
                        date = formaterFecha.parse( m_ListCalendario.get( k ).getcFechaPago().toString() );
                    }catch (Exception e){

                    }
                    calendario.setMonto( Double.parseDouble( m_ListCalendario.get( k ).getMontoCuota() ) );
                    calendario.setFecha( date );
                    m_ListCalendarioTotal.add( calendario );
                }

                double ntcea = 0;
                ntcea = calcularTCEA( m_ListCalendarioTotal );
                tvTcea.setText( String.valueOf( ntcea ) );





            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();

                if ( m_CalendarioListCreditoResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                }
                else
                    Toast.makeText( m_context, "Hubo en error en consultar", Toast.LENGTH_SHORT ).show();

            }
            else{
                pd.dismiss();
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
            }
        }

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

}
