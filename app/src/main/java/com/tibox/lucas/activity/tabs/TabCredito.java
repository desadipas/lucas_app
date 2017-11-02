package com.tibox.lucas.activity.tabs;

/**
 * Created by desa02 on 21/06/2017.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.tibox.lucas.R;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.connections.AppConfig;
import com.tibox.lucas.network.dto.DocumentosPdfDTO;
import com.tibox.lucas.network.response.FileResponse;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

public class TabCredito extends Fragment {

    private String m_dato;
    private String m_datosCredito;
    private int m_nCodCred;
    private int m_nCodAge;
    private double m_nTasa;

    private IEventsListener m_Listener; // Declaramos nuestra interface de onclick's

    public static TabCredito newsInstance( String dato, String datosCredito, int Agencia, int Credito, double TasaInteres  ){
        TabCredito f = new TabCredito();
        Bundle args = new Bundle();
        args.putString( "dato", dato );
        args.putString( "datosCredito", datosCredito );

        args.putInt( "Agencia", Agencia );
        args.putInt( "Credito", Credito );
        args.putDouble( "Tasa", TasaInteres );


        f.setArguments( args );
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_dato = getArguments().getString("dato");
        m_datosCredito = getArguments().getString("datosCredito");

        m_nCodAge = getArguments().getInt( "Agencia" );
        m_nCodCred = getArguments().getInt( "Credito" );
        m_nTasa = getArguments().getDouble( "Tasa" );

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView Contrato = (TextView) view.findViewById( R.id.tv_contrato );
        TextView Prestamo = (TextView) view.findViewById( R.id.tv_prestamo );
        TextView Cuotas = (TextView) view.findViewById( R.id.tv_cuotas );
        TextView MontoCuota = (TextView) view.findViewById( R.id.tv_monto_cuota );
        TextView TasaInteres = (TextView) view.findViewById( R.id.tv_tasa_interes );

        final Button btnSolicitud = (Button) view.findViewById( R.id.btn_solicitud );
        final Button btnHR1 = (Button) view.findViewById( R.id.btn_hr1 );
        final Button btnHR2 = (Button) view.findViewById( R.id.btn_hr2 );
        final Button btnContrato = (Button) view.findViewById( R.id.btn_contrato );
        final Button btnVoucher = (Button) view.findViewById( R.id.btn_voucher );
        final Button btnSeguro = (Button) view.findViewById( R.id.btn_seguro );

        btnSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( m_Listener != null) {
                    m_Listener.onClickSolicitud( btnSolicitud.getText().toString() );
                }

            }
        });

        btnHR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( m_Listener != null) {
                    m_Listener.onClickHojaResumen1( btnHR1.getText().toString() );
                }
            }
        });

        btnHR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( m_Listener != null) {
                    m_Listener.onClickHojaResumen2( btnHR2.getText().toString() );
                }

            }
        });


        btnContrato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( m_Listener != null) {
                    m_Listener.onClickContrato( btnContrato.getText().toString() );
                }

            }
        });

        btnVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( m_Listener != null) {
                    m_Listener.onClickVoucher( btnVoucher.getText().toString() );
                }

            }
        });

        btnSeguro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ( m_Listener != null) {
                    m_Listener.onClickSeguro( btnSeguro.getText().toString() );
                }

            }
        });

        if ( m_datosCredito != null ){

            //G0050000000445|850.00|16|78.58|7.93
            //DESCRIPCION
            StringTokenizer st = new StringTokenizer( m_datosCredito, "/" );
            int result = 0;
            result = st.countTokens();
            String st_contrato = "", st_prestamo = "", st_cuotas = "", st_monto_cuota="", st_tasa_interes="";

            if ( result == Constantes.CINCO ) {

                st_contrato = st.nextToken();
                st_prestamo = st.nextToken();
                st_cuotas = st.nextToken();
                st_monto_cuota = st.nextToken();
                st_tasa_interes = String.valueOf( m_nTasa );//st.nextToken();
                //

                Contrato.setText(st_contrato);
                Prestamo.setText("S/ " + st_prestamo);
                Cuotas.setText(st_cuotas);
                MontoCuota.setText("S/ " + st_monto_cuota);
                TasaInteres.setText(st_tasa_interes + "%");
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View rootView = inflater.inflate( R.layout.tab_credito, container, false );
        //return rootView;
        return inflater.inflate( R.layout.tab_credito, container, false );
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
                nResp = m_webApi.ObtenerDetalleImagePDF( m_param );

                //if ( nResp == Constantes.UNO ) {
                    m_namePdf = m_namePdf + "_" + m_param.getnAgencia() + "-" + m_param.getnCredito();
                    //m_File = m_webApi.ObtenerImagePDF( m_param, m_namePdf, "" ); // mdipas

                m_FileResponse = m_webApi.ObtenerImagePDF( m_param, m_namePdf, "" );
                if ( !m_FileResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else{
                    resp = RESULT_OK;
                }

                /*
                    if ( m_File != null ) {
                        resp = RESULT_OK;
                    } else{
                        m_Mensaje = "¡Error en cargar el PDF!";
                        resp = RESULT_FALSE;
                    }
                    */
                /*
                }
                else {
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
                Toast.makeText(m_context, m_Mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
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
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf"); startActivity(intent);
    }

    public interface IEventsListener {
        public void onClickSolicitud( String nameButton );
        public void onClickHojaResumen1( String nameButton );
        public void onClickHojaResumen2( String nameButton );
        public void onClickContrato( String nameButton );
        public void onClickVoucher( String nameButton );
        public void onClickSeguro( String nameButton );
    }

    public void setEventListener( IEventsListener Listener ) {
        m_Listener = Listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_Listener = null;
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

    }
}
