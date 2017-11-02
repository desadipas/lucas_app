package com.tibox.lucas.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.tibox.lucas.R;
import com.tibox.lucas.adaptadores.listview.AdaptadorLvGarantia;
import com.tibox.lucas.dao.FamiliaGarantiaDAO;
import com.tibox.lucas.dao.FotoGarantiaDAO;
import com.tibox.lucas.dao.GarantiaDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.FamiliaGarantia;
import com.tibox.lucas.entidad.FotoGarantia;
import com.tibox.lucas.entidad.Garantia;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.connections.AppConfig;
import com.tibox.lucas.network.dto.CO_DocumentosDTO;
import com.tibox.lucas.network.dto.CreditoGarantiaDTO;
import com.tibox.lucas.network.dto.DatosSalida.ELScoringFinalResultado;
import com.tibox.lucas.network.dto.ElectrodomesticoDTO;
import com.tibox.lucas.network.dto.ElectrodomesticoFamDTO;
import com.tibox.lucas.network.dto.GarantiasDTO;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.dto.ResponseSolicitudDTO;
import com.tibox.lucas.network.dto.TB_FlujoMaestroDTO;
import com.tibox.lucas.network.webapi.IAppCreditosWebApi;
import com.tibox.lucas.utilidades.Adapter;
import com.tibox.lucas.utilidades.Common;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityRegistroGarantias extends AppCompatActivity {

    Spinner spFamilia, spArticulo, spMarca, spLinea, spModelo;
    private EditText etValorTasadoPorcentaje,etValorTasado,etPrestamoMaximo,etDescripcion,etPrestamo,
            etPrestamoMaximoSolicitud,etItems,etTotalPrestamo,etPrestamoMinimoSolicitud;
    private ListView lvArtIngresados;
    private GridView gvImagesArticulo;
    FloatingActionButton fabGrabarGarantias,fabFotoGarantias;
    Button btnAgregarLista;
    ScrollView scrollViewGarantia;

    private double m_ValorTasado,m_ValorTasadoPorc,m_PrestamoMax,m_PrestamoMaximoSolicitud,m_Prestamo,m_TotalPrestamo,
            m_PrestamoMinimoSolicitud;

    private int accion_updateFotos = 0;
    private ActionMode actionMode;
    public static final int REQUEST_TOMAR_FOTO_CEDULA = 1001;
    private static final int REQUEST_VIEW_IMAGE = 1000;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static String TAG = "PermissionDemo";
    private LinearLayout lyFotosGarantia;

    private int m_IdspFamilia = 0;
    private int m_IdspArticulo = 0;
    private int m_IdspMarca = 0;
    private int m_IdspLinea = 0;
    private int m_IdspModelo = 0;
    private String cDescripcionGarantia = "";
    private ArrayList<Garantia> arrayListGarantia;
    private AdaptadorLvGarantia adaptadorLvGarantia;
    protected Garantia m_garantiaSelect;

    private boolean m_EditarDatos = false;
    private int m_idGarantia = Constantes.CERO; // 1

    protected Usuario m_Sesion;
    private ELScoringFinalResultado m_ElScoringFinalResultado;
    private int m_nIdFlujoMaestro = 0;
    private int m_nCodCred = 0;
    private PersonaCreditoDTO m_PersonaDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_registro_garantias );

        spFamilia = ( Spinner ) findViewById( R.id.spFamilia );
        spArticulo = ( Spinner ) findViewById( R.id.spArticulo );
        spMarca = ( Spinner ) findViewById( R.id.spMarca );
        spLinea = ( Spinner ) findViewById( R.id.spLinea );
        spModelo = ( Spinner ) findViewById( R.id.spModelo );

        etValorTasadoPorcentaje = ( EditText ) findViewById( R.id.etValorTasadoPorcentaje );
        etValorTasado = ( EditText ) findViewById( R.id.etValorTasado );
        etPrestamoMaximo = ( EditText ) findViewById( R.id.etPrestamoMaximo );
        etDescripcion = ( EditText ) findViewById( R.id.etDescripcion );
        etPrestamo = ( EditText ) findViewById( R.id.etPrestamo );
        lvArtIngresados = ( ListView ) findViewById( R.id.lvArtIngresados );
        gvImagesArticulo = ( GridView ) findViewById( R.id.gvImagesArticulo );
        fabGrabarGarantias = ( FloatingActionButton ) findViewById( R.id.fabGrabarGarantias );
        fabFotoGarantias = ( FloatingActionButton ) findViewById( R.id.fabFotoGarantias );
        lyFotosGarantia = (LinearLayout) findViewById(R.id.lyFotosGarantia );
        btnAgregarLista = ( Button ) findViewById( R.id.btnAgregarLista );
        etPrestamoMaximoSolicitud = ( EditText ) findViewById( R.id.etPrestamoMaximoSolicitud );
        etPrestamoMinimoSolicitud = ( EditText ) findViewById( R.id.etPrestamoMinimoSolicitud );
        etItems = ( EditText ) findViewById( R.id.etItems );
        etTotalPrestamo = ( EditText ) findViewById( R.id.etTotalPrestamo );
        scrollViewGarantia = (ScrollView) findViewById( R.id.scrollViewGarantia );


        scrollViewGarantia.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById( R.id.lvArtIngresados ).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                //findViewById( R.id.gvImagesArticulo ).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        gvImagesArticulo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        gvImagesArticulo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return;
            }
        });

        lvArtIngresados.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        lvArtIngresados.setOnItemLongClickListener( lvArtIngresadossetOnItemLongClickListener );

        spFamilia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspFamilia = (int) spFamilia.getSelectedItemId();
                CargarFamilia( m_IdspFamilia, Constantes.DOS, spArticulo, m_IdspFamilia );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spArticulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspArticulo = (int) spArticulo.getSelectedItemId();
                CargarFamilia( m_IdspArticulo, Constantes.TRES, spMarca, m_IdspArticulo );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspMarca = (int) spMarca.getSelectedItemId();
                CargarFamilia( m_IdspMarca ,Constantes.CUATRO, spLinea, m_IdspMarca );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spLinea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspLinea = (int) spLinea.getSelectedItemId();
                CargarFamilia( m_IdspLinea , Constantes.CINCO, spModelo, m_IdspLinea );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spModelo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                m_IdspModelo = (int) spModelo.getSelectedItemId();

                ElectrodomesticoDTO electrodomestico = new ElectrodomesticoDTO();
                electrodomestico = new ElectrodomesticoDTO();
                electrodomestico.setnCodFamilia( m_IdspFamilia );
                electrodomestico.setnCodArticulo( m_IdspArticulo  );
                electrodomestico.setnCodMarca( m_IdspMarca );
                electrodomestico.setnCodLinea( m_IdspLinea );
                electrodomestico.setnCodModelo( m_IdspModelo  );

                ObtenerMontoMaximoPrestamoAsyns obtenerMontoMaximoPrestamoAsyns =
                        new ObtenerMontoMaximoPrestamoAsyns( ActivityRegistroGarantias.this, electrodomestico );
                obtenerMontoMaximoPrestamoAsyns.execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fabGrabarGarantias.setOnClickListener( fabGrabarGarantiassetOnClickListener );
        fabFotoGarantias.setOnClickListener( fabFotoGarantiassetOnClickListener );
        btnAgregarLista.setOnClickListener( btnAgregarListasetOnClickListener );


        int permission = ContextCompat.checkSelfPermission( ActivityRegistroGarantias.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE );
        if ( permission != PackageManager.PERMISSION_GRANTED ) {
            Log.i(TAG, "Permission to record denied");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Se requiere permiso para acceder a la tarjeta SD para esta aplicación.")
                        .setTitle("Permiso necesario");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(TAG, "Clicked");
                        makeRequest();

                        File dire = new File( AppConfig.directorioImagenes );
                        if ( dire.isDirectory() )
                        {
                            String[] children = dire.list();
                            for ( int i = 0; i < children.length; i++ )
                            {
                                new File( dire, children[i] ).delete();
                            }
                        }

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {
                makeRequest();
            }
        }

        // Eliminamos las fotos de la carpeta almacenada en el movil.
        new GarantiaDAO().EliminarGarantia();
        new FotoGarantiaDAO().LimpiarFotoGarantia();

        // end

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;
        m_ElScoringFinalResultado = new ELScoringFinalResultado();
        m_PersonaDatos = new PersonaCreditoDTO();

        Intent intent = getIntent();
        if (intent != null) {
            if ( intent.getExtras().getParcelable("DatosScoringFinal") != null ) {
                m_ElScoringFinalResultado = intent.getExtras().getParcelable("DatosScoringFinal");
                m_nIdFlujoMaestro = intent.getExtras().getInt( "IdFlujoMaestro" );
            }
            if ( intent.getExtras().getParcelable("DatosPersona") != null ) {
                m_PersonaDatos = intent.getExtras().getParcelable("DatosPersona");
            }
        }

        ActualizarCatalogoFamiliarAsync actualizarCatalogoFamiliarAsync =
                new ActualizarCatalogoFamiliarAsync( ActivityRegistroGarantias.this, Constantes.CERO , Constantes.UNO, spFamilia, Constantes.CERO );
        actualizarCatalogoFamiliarAsync.execute();


        m_ValorTasadoPorc = m_ElScoringFinalResultado.getnPorcGarantiaAvaluo();

        m_PrestamoMaximoSolicitud = Utilidades.TruncateDecimal( m_ElScoringFinalResultado.getnPrestamoMaximo() );//3500;
        m_PrestamoMinimoSolicitud = Utilidades.TruncateDecimal( m_ElScoringFinalResultado.getnPrestamoMinimo() );

        etValorTasadoPorcentaje.setText( String.valueOf( m_ValorTasadoPorc ) + " %" );

        etPrestamoMaximoSolicitud.setText( "S/ " + String.valueOf( m_PrestamoMaximoSolicitud ) );
        etPrestamoMinimoSolicitud.setText( "S/ " + String.valueOf( m_PrestamoMinimoSolicitud ) );


    }

    AdapterView.OnItemLongClickListener lvArtIngresadossetOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
            m_garantiaSelect = (Garantia) adapterView.getAdapter().getItem( position );
            registerForContextMenu( adapterView );
            openContextMenu( adapterView );
            return false;
        }
    };

    View.OnClickListener btnAgregarListasetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if ( etPrestamo.getText().toString().trim().length() > 0 )
                m_Prestamo =  Double.parseDouble( etPrestamo.getText().toString() );
            else
                m_Prestamo = 0;

            if ( (int) spModelo.getSelectedItemId() <= 0 ){
                Toast.makeText( ActivityRegistroGarantias.this,"¡Ingresar correctamente los datos!", Toast.LENGTH_SHORT ).show();
                return;
            }
            else if ( m_Prestamo <= 0 || m_Prestamo > m_PrestamoMax ){
                Toast.makeText( ActivityRegistroGarantias.this,"¡Ingresar correctamente el Prestamo!", Toast.LENGTH_SHORT ).show();
                return;
            }
            else if ( !new FotoGarantiaDAO().consultarFotoGarantiaActualizar( m_idGarantia ) ){
                Toast.makeText( ActivityRegistroGarantias.this,"¡Ingresar imagenes de la Garantia!", Toast.LENGTH_SHORT ).show();
                return;
            }

            m_IdspModelo = (int) spModelo.getSelectedItemId();
            cDescripcionGarantia = new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspFamilia, Constantes.UNO );
            cDescripcionGarantia = cDescripcionGarantia  + "/" + new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspArticulo, Constantes.DOS );
            cDescripcionGarantia = cDescripcionGarantia  + "/" + new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspMarca, Constantes.TRES );
            cDescripcionGarantia = cDescripcionGarantia  + "/" + new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspLinea, Constantes.CUATRO );
            cDescripcionGarantia = cDescripcionGarantia  + "/" + new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspModelo, Constantes.CINCO );

            AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityRegistroGarantias.this);
            dialog.setTitle("Aviso");
            if (m_EditarDatos)
                dialog.setMessage("¿Seguro que desea actualizar la Garantia?");
            else
                dialog.setMessage("¿Seguro que desea agregar la Garantia?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    // Se registra la garantia.

                    Garantia garantia = new Garantia();
                    garantia.setFamilia( m_IdspFamilia );
                    garantia.setArticulo( m_IdspArticulo );
                    garantia.setMarca( m_IdspMarca );
                    garantia.setLinea( m_IdspLinea );
                    garantia.setModelo( m_IdspModelo );
                    garantia.setDescripcionGarantia( cDescripcionGarantia );
                    garantia.setDescripcion( etDescripcion.getText().toString() );
                    garantia.setPrestamo( m_Prestamo );
                    garantia.setVTasado( m_ValorTasado );
                    garantia.setcNomFamilia(  new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspFamilia, Constantes.UNO ) );
                    garantia.setcNomArticulo(  new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspArticulo, Constantes.DOS ) );
                    garantia.setcNomMarca( new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspMarca, Constantes.TRES ) );
                    garantia.setcNomLinea( new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspLinea, Constantes.CUATRO ) );
                    garantia.setcNomModelo( new FamiliaGarantiaDAO().ObtenerNombreFamilia( m_IdspModelo, Constantes.CINCO ) );

                    if ( !m_EditarDatos ) {
                        int idGarantia = new GarantiaDAO().insertar( garantia );
                        new FotoGarantiaDAO().ActualizarIdGarantia( idGarantia, Constantes.ESTADO_AGREGADO );
                    }
                    else{
                        new GarantiaDAO().ActualizarGarantia( garantia, m_idGarantia );
                    }

                    CargarListaGarantia();


                }
            });
            dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            dialog.show();

        }
    };

    protected void LimpiarDatos(){

        etDescripcion.setText("");
        etPrestamo.setText("");
        fabGrabarGarantias.setVisibility(View.VISIBLE);

        if ( m_EditarDatos ){
            btnAgregarLista.setText( "Agregar a la Lista" );
            m_idGarantia = Constantes.CERO;
            m_EditarDatos = false;
        }

        ActualizarCatalogoFamiliarAsync actualizarCatalogoFamiliarAsync =
                new ActualizarCatalogoFamiliarAsync( ActivityRegistroGarantias.this, Constantes.CERO , Constantes.UNO, spFamilia, Constantes.CERO );
        actualizarCatalogoFamiliarAsync.execute();
    }

    public void CargarListaGarantia(){

        arrayListGarantia = new GarantiaDAO().ListarGarantias();
        adaptadorLvGarantia = new AdaptadorLvGarantia( this, R.layout.activity_registro_garantias, arrayListGarantia );
        lvArtIngresados.setAdapter( adaptadorLvGarantia );

        int countLista = lvArtIngresados.getAdapter().getCount();
        etItems.setText( String.valueOf( countLista ) );
        m_TotalPrestamo = Utilidades.TruncateDecimal( ObtenerTotalPrestamoGarantia( lvArtIngresados ) );
        etTotalPrestamo.setText( "S/ " + String.valueOf( m_TotalPrestamo ) );

        if ( countLista > 0 )
            lvArtIngresados.setVisibility( View.VISIBLE );
        else
            lvArtIngresados.setVisibility( View.GONE );

        // Limpiamos la galeria y ocultamos.
        lyFotosGarantia.setVisibility( View.GONE );
        gvImagesArticulo.setAdapter( null );

        LimpiarDatos();
    }

    public void CargarFamilia( int idCodigoSuperior, int nNivel, Spinner spinner, int idSpinner ){
        new FamiliaGarantiaDAO().LimpiarFamiliaxNivel( nNivel );
        ActualizarCatalogoFamilia( idCodigoSuperior, spinner, nNivel, idSpinner );
    }

    protected void ActualizarCatalogoFamilia( int CodigoSuperior, Spinner spinner, int Nivel, int idSpinner ){
        try {
            ActualizarCatalogoFamiliarAsync actualizarCatalogoFamiliarAsync = new ActualizarCatalogoFamiliarAsync( ActivityRegistroGarantias.this, CodigoSuperior ,
                    Nivel, spinner, idSpinner );
            actualizarCatalogoFamiliarAsync.execute();
        }
        catch ( Exception ex )
        {
            Toast.makeText( this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission has been denied by user");
                }
                else {

                    Log.i(TAG, "Permission has been granted by user");

                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle( "Garantia : " + m_garantiaSelect.getDescripcionGarantia() );
        inflater.inflate( R.menu.menu_action_selected, menu );

        MenuItem Continuar = menu.findItem(R.id.item_continuar);
        Continuar.setVisible(false);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_eliminar:
                new GarantiaDAO().EliminarGarantiaXid( m_garantiaSelect.getIdGarantia() );
                new FotoGarantiaDAO().LimpiarFotosxIdGarantia( m_garantiaSelect.getIdGarantia() );
                Toast toast = Toast.makeText(getApplicationContext(), "¡Se eliminó correctamente!", Toast.LENGTH_SHORT);
                toast.show();
                CargarListaGarantia();
                return true;
            case R.id.item_editar:
                m_EditarDatos = true;
                m_idGarantia = m_garantiaSelect.getIdGarantia();
                fabGrabarGarantias.setVisibility(View.GONE);
                CargarFamiliaGarantia( m_garantiaSelect.getIdGarantia() );
                return true;
            case R.id.item_cancelar:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       return super.onOptionsItemSelected( item );
    }

    protected void CargarFamiliaGarantia( int IdGarantia ){
        btnAgregarLista.setText( "Actualizar" );
        Garantia datos = new Garantia();
        datos = new GarantiaDAO().ObtenerGarantiaxId( IdGarantia );

        lvArtIngresados.setVisibility( View.GONE );
        etPrestamo.setText( String.valueOf( Utilidades.TruncateDecimal( datos.getPrestamo() ) ) );
        etDescripcion.setText( String.valueOf( datos.getDescripcion() ) );

        m_IdspFamilia = datos.getFamilia();
        m_IdspArticulo = datos.getArticulo();
        m_IdspMarca = datos.getMarca();
        m_IdspLinea = datos.getLinea();
        m_IdspModelo = datos.getModelo();

        ActualizarCatalogoFamiliarAsync actualizarCatalogoFamiliarAsync =
                new ActualizarCatalogoFamiliarAsync( ActivityRegistroGarantias.this, Constantes.CERO, Constantes.UNO, spFamilia, m_IdspFamilia );
        actualizarCatalogoFamiliarAsync.execute();
    }

    protected void CargarFotosGarantia( int IdGarantia ){
        ProcesoMostrarFotos( new FotoGarantiaDAO().ListarFotoGarantia( Constantes.ESTADO_AGREGADO, Constantes.UNO, IdGarantia ) );
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions( ActivityRegistroGarantias.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_STORAGE);
    }

    View.OnClickListener fabFotoGarantiassetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            iniciarCamara();
        }
    };

    View.OnClickListener fabGrabarGarantiassetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if ( m_TotalPrestamo <= Constantes.CERO ){
                Toast.makeText( ActivityRegistroGarantias.this,"¡Agregar garantias!", Toast.LENGTH_SHORT ).show();
                return;
            }

            if ( m_TotalPrestamo < m_PrestamoMinimoSolicitud ){
                Toast.makeText( ActivityRegistroGarantias.this,"¡Total Prestamo es Menor al Prestamo Minimo!", Toast.LENGTH_SHORT ).show();
                return;
            }

            if ( m_TotalPrestamo > m_PrestamoMaximoSolicitud ){
                Toast.makeText( ActivityRegistroGarantias.this,"¡Total Prestamo es Mayor al Prestamo Maximo!", Toast.LENGTH_SHORT ).show();
                return;
            }

            AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityRegistroGarantias.this );
            dialog.setTitle("Aviso");
            dialog.setMessage("¿Esta seguro que desea realizar el proceso?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    //REGISTRAR IMAGEN GARANTIA
                    RegistrarImageGarantiaAsyns registrarImageGarantiaAsyns =
                            new RegistrarImageGarantiaAsyns( ActivityRegistroGarantias.this );
                    registrarImageGarantiaAsyns.execute();
                    //REGISTRAR GARANTIA
                    //ELIMINAR GARANTIAS Y FOTOS.
                    //PASAR A REGISTRO DE ESTADO DE CUENTA.

                }
            });
            dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            dialog.show();


        }
    };

    protected void iniciarCamara() {

        File newdir = new File( AppConfig.directorioImagenes );
        newdir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        Constantes.fotoUri = "";
        Constantes.fotoUri = "Garantia" +"_"+timeStamp+".jpg";
        File newfile = new File(newdir, Constantes.fotoUri);
        try {
            newfile.createNewFile();
        }
        catch (IOException e) {
        }
        Uri outputFileUri = Uri.fromFile( newfile );
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
        takePictureIntent.putExtra( MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        startActivityForResult( takePictureIntent, ActivityRegistroGarantias.REQUEST_TOMAR_FOTO_CEDULA );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( ( requestCode == REQUEST_TOMAR_FOTO_CEDULA ) ) {
            if ( resultCode == Activity.RESULT_OK ){
                String uriCompletoFoto = AppConfig.directorioImagenes + Constantes.fotoUri;
                Bitmap bitmap = this.resizeImage( BitmapFactory.decodeFile( uriCompletoFoto ), 1024) ;
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream( new File( uriCompletoFoto ) ) );
                }
                catch ( FileNotFoundException e ) {
                    e.printStackTrace();
                }
                try {
                    if ( Build.VERSION.SDK_INT >= 19 ) {}
                    else {}

                    FotoGarantia fotoArticulo = new FotoGarantia();
                    fotoArticulo.setIdGarantia( m_idGarantia );
                    fotoArticulo.setDescripcion( "foto garantia" );
                    if ( m_EditarDatos )
                        fotoArticulo.setEstado( Constantes.ESTADO_AGREGADO );
                    else
                        fotoArticulo.setEstado( Constantes.ESTADO_PORAGREGAR );
                    fotoArticulo.setRutaFotoArticulo( uriCompletoFoto );
                    new FotoGarantiaDAO().insertar(fotoArticulo);

                    lyFotosGarantia.setVisibility( View.VISIBLE );

                    if ( m_EditarDatos )
                        createGallery( new FotoGarantiaDAO().ListarFotoGarantia( Constantes.ESTADO_AGREGADO, Constantes.UNO, m_idGarantia ) );
                    else
                        createGallery( new FotoGarantiaDAO().ListarFotoGarantia( Constantes.ESTADO_PORAGREGAR, Constantes.CERO, m_idGarantia ) );
                }
                catch ( Exception ex ) {
                    ex.printStackTrace();
                }

            }
        }
        else if ( ( requestCode == REQUEST_VIEW_IMAGE ) ) {
            if ( resultCode == RESULT_CANCELED ) {

                if ( m_EditarDatos )
                    ProcesoMostrarFotos( new FotoGarantiaDAO().ListarFotoGarantia( Constantes.ESTADO_AGREGADO, Constantes.UNO, m_idGarantia ) );
                else
                    ProcesoMostrarFotos( new FotoGarantiaDAO().ListarFotoGarantia( Constantes.ESTADO_PORAGREGAR, Constantes.CERO, m_idGarantia ) );

                // ProcesoMostrarFotos( new FotoGarantiaDAO().ListarFotoGarantia( Constantes.ESTADO_AGREGADO ) );
            }
        }
    }

    protected void ProcesoMostrarFotos ( List<FotoGarantia> file ) {
        if ( file.size() > 0 )
        {
            lyFotosGarantia.setVisibility( View.VISIBLE );
            createGallery(file);
        }
        else
            lyFotosGarantia.setVisibility(View.GONE);
    }

    public class ActualizarCatalogoFamiliarAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ActualizarCatalogoFamiliarAsync ( Context context, int CodigoSuperior, int nNivel, Spinner sp, int IdSpinner ){
            m_IdSpinner = IdSpinner;
            m_Nivel = nNivel;
            m_Spinner = sp;
            m_CodigoSuperior = CodigoSuperior;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando catalogo ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
           try {
               String resp = "" ;

               if (!Common.isNetworkConnected(m_context)) {
                   return "No se encuentra conectado a internet.";
               }

               ElectrodomesticoDTO electrodomestico = new ElectrodomesticoDTO();
               electrodomestico = new ElectrodomesticoDTO();

               ElectrodomesticoFamDTO famDTO = new ElectrodomesticoFamDTO();
               List<ElectrodomesticoDTO> Lista = new ArrayList<>();
               List<ElectrodomesticoFamDTO> ListaFamDTOs = new ArrayList<>();

               if ( m_Nivel == Constantes.UNO ) {
                   new FamiliaGarantiaDAO().LimpiarFamilia();
                   Lista = m_webApi.obtenerFamilia(electrodomestico);
               }
               else if ( m_Nivel == Constantes.DOS ) {
                   electrodomestico.setnCodFamilia( m_CodigoSuperior );
                   Lista = m_webApi.obtenerFamiliaPost(electrodomestico, m_Nivel);
               }
               else if ( m_Nivel == Constantes.TRES ) {
                   electrodomestico.setnCodArticulo( m_CodigoSuperior );
                   Lista = m_webApi.obtenerFamiliaPost(electrodomestico, m_Nivel);
               }
               else if ( m_Nivel == Constantes.CUATRO ) {
                   electrodomestico.setnCodArticulo( m_IdspArticulo  );
                   electrodomestico.setnCodMarca( m_CodigoSuperior );
                   Lista = m_webApi.obtenerFamiliaPost(electrodomestico, m_Nivel);
               }
               else {
                   electrodomestico.setnCodArticulo( m_IdspArticulo  );
                   electrodomestico.setnCodMarca( m_IdspMarca );
                   electrodomestico.setnCodLinea( m_CodigoSuperior );
                   Lista = m_webApi.obtenerFamiliaPost(electrodomestico, m_Nivel);
               }

               if ( Lista != null ){
                   if ( Lista.size() > 0 ){
                       for ( ElectrodomesticoDTO dto : Lista ){
                           FamiliaGarantia familia = new FamiliaGarantia();

                           if ( m_Nivel == Constantes.UNO ) {

                               familia.setnIdCodigo( dto.getnCodFamilia() );
                               familia.setcDescripcion( dto.getcNomFamilia() );
                               familia.setnNivel( Constantes.UNO );
                           }
                           else if ( m_Nivel == Constantes.DOS ){
                               familia.setnIdCodigo(dto.getnCodArticulo());
                               familia.setcDescripcion(dto.getcNomArticulo());
                               familia.setnNivel( Constantes.DOS );
                           }
                           else if ( m_Nivel == Constantes.TRES ){
                               familia.setnIdCodigo (dto.getnCodMarca() );
                               familia.setcDescripcion(dto.getcNomMarca());

                               familia.setnNivel( Constantes.TRES );
                           }
                           else if ( m_Nivel == Constantes.CUATRO ){
                               familia.setnIdCodigo(dto.getnCodLinea());
                               familia.setcDescripcion(dto.getcNomLinea());
                               familia.setnNivel( Constantes.CUATRO );
                           }
                           else {
                               familia.setnIdCodigo(dto.getnCodModelo());
                               familia.setcDescripcion(dto.getcNomModelo());
                               familia.setnNivel( Constantes.CINCO );
                           }

                           familia.setnIdCodigoSuperior( m_Nivel );
                           familia.setnPorValorTasado( dto.getnPorValorTasado() );
                           familia.setnPrestamoTotal( dto.getnPrestamoTotal() );
                           familia.setnValorTasado( dto.getnValorTasado() );
                           new FamiliaGarantiaDAO().insertar( familia );

                           resp = RESULT_OK;
                       }
                   }
                   else
                       resp = RESULT_FALSE;
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

                new FamiliaGarantiaDAO().rellenaSpinner( m_Spinner, m_CodigoSuperior, m_Nivel, ActivityRegistroGarantias.this );

                if ( !m_EditarDatos ){
                    m_IdSpinner = (int) m_Spinner.getSelectedItemId();
                }

                if ( m_EditarDatos && m_Nivel == Constantes.UNO )
                    m_Spinner.setSelection( Utilidades.getCursorSpinnerPositionById( m_Spinner, m_IdspFamilia ) );
                else if ( m_EditarDatos && m_Nivel == Constantes.DOS )
                    m_Spinner.setSelection( Utilidades.getCursorSpinnerPositionById( m_Spinner, m_IdspArticulo ) );
                else if ( m_EditarDatos && m_Nivel == Constantes.TRES )
                    m_Spinner.setSelection( Utilidades.getCursorSpinnerPositionById( m_Spinner, m_IdspMarca ) );
                else if ( m_EditarDatos && m_Nivel == Constantes.CUATRO )
                    m_Spinner.setSelection( Utilidades.getCursorSpinnerPositionById( m_Spinner, m_IdspLinea ) );
                else if ( m_EditarDatos && m_Nivel == Constantes.CINCO )
                    m_Spinner.setSelection( Utilidades.getCursorSpinnerPositionById( m_Spinner, m_IdspModelo ) );
                else
                    m_Spinner.setSelection( Utilidades.getCursorSpinnerPositionById( m_Spinner, m_IdSpinner ) );

                if ( m_EditarDatos )
                    CargarFotosGarantia( m_idGarantia );

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                Toast.makeText(m_context, "¡No se actualizo data interna!", Toast.LENGTH_SHORT).show();
                new FamiliaGarantiaDAO().rellenaSpinner( m_Spinner, m_CodigoSuperior, m_Nivel, ActivityRegistroGarantias.this );

                for ( int i = m_Nivel + 1 ; i <= 5; i++ ){
                    new FamiliaGarantiaDAO().LimpiarFamiliaxNivel( i );

                    if ( i == 1 )
                        new FamiliaGarantiaDAO().rellenaSpinner( spFamilia, m_CodigoSuperior, i, ActivityRegistroGarantias.this );
                    else if( i == 2 )
                        new FamiliaGarantiaDAO().rellenaSpinner( spArticulo, m_CodigoSuperior, i, ActivityRegistroGarantias.this );
                    else if( i == 3 )
                        new FamiliaGarantiaDAO().rellenaSpinner( spMarca, m_CodigoSuperior, i, ActivityRegistroGarantias.this );
                    else if( i == 4 )
                        new FamiliaGarantiaDAO().rellenaSpinner( spLinea, m_CodigoSuperior, i, ActivityRegistroGarantias.this );
                    else
                        new FamiliaGarantiaDAO().rellenaSpinner( spModelo, m_CodigoSuperior, i, ActivityRegistroGarantias.this );
                }

                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private int m_IdSpinner;
        private Spinner m_Spinner;
        private int m_Nivel;
        private String m_Familia;
        private int m_CodigoSuperior;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    private void createGallery(List<FotoGarantia> List) {
        try {
            gvImagesArticulo.setNumColumns( Constantes.CUATRO );
            gvImagesArticulo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
            final Adapter adapter = new Adapter( ActivityRegistroGarantias.this, R.layout.grid_item, List ) {
                @Override
                public void create(Object item, View view) {
                    LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = 200;
                    layoutParams.height = 140;
                    ((ImageView) view.findViewById(R.id.imagen_articulo)).setImageBitmap(BitmapFactory.decodeFile(((FotoGarantia) item).getRutaFotoArticulo()));
                    view.setPadding(0, 10, 10, 0);
                    view.setLayoutParams(layoutParams);
                    view.setTag(item);
                }
            };
            gvImagesArticulo.setAdapter(adapter);
            if ( accion_updateFotos == Constantes.CERO ) {

                gvImagesArticulo.setChoiceMode( GridView.CHOICE_MODE_MULTIPLE_MODAL );
                gvImagesArticulo.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int posicion, long l, boolean checked) {
                        int checkedCount = gvImagesArticulo.getCheckedItemCount();
                        mode.setTitle(String.valueOf(checkedCount));
                        adapter.toggleSelection( posicion );

                        if (checkedCount > Constantes.CERO) {
                            BloquearControles( false );
                        }
                        else
                            BloquearControles( true );
                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate( R.menu.menu_action_mode, menu );
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_delete:
                                final SparseBooleanArray selected = adapter.getSelectedIds();
                                AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityRegistroGarantias.this );
                                dialog.setTitle("Confirmar");
                                dialog.setMessage("¿Esta seguro que desea eliminar las siguiente(s) " + selected.size() + " foto(s)?");
                                dialog.setCancelable(true);
                                dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        for ( int i = (selected.size() - 1); i >= 0; i-- ) {
                                            if (selected.valueAt(i)) {

                                                FotoGarantia selectedItem = ( FotoGarantia ) adapter.getItem( selected.keyAt(i ) );

                                                if ( m_EditarDatos ) {
                                                    if ( selectedItem.getEstado() == Constantes.ESTADO_AGREGADO )
                                                        new FotoGarantiaDAO().ActualizarEstado(Constantes.ESTADO_ELIMINADO, selectedItem.getIdFotoArticulo());
                                                    else
                                                        new FotoGarantiaDAO().LimpiarFotosxSeleccion(selectedItem.getIdFotoArticulo());
                                                }
                                                else{
                                                    if ( selectedItem.getEstado() == Constantes.ESTADO_PORAGREGAR )
                                                        new FotoGarantiaDAO().ActualizarEstado(Constantes.ESTADO_ELIMINADO, selectedItem.getIdFotoArticulo());
                                                    else
                                                        new FotoGarantiaDAO().LimpiarFotosxSeleccion(selectedItem.getIdFotoArticulo());
                                                }

                                                adapter.remove(selectedItem);

                                            }
                                        }

                                        List<FotoGarantia> actualizado = new ArrayList<FotoGarantia>();

                                        if ( m_EditarDatos )
                                            actualizado = new FotoGarantiaDAO().ListarFotoGarantia( Constantes.ESTADO_AGREGADO, Constantes.UNO, m_idGarantia );
                                        else
                                            actualizado = new FotoGarantiaDAO().ListarFotoGarantia( Constantes.ESTADO_PORAGREGAR, Constantes.CERO, m_idGarantia );


                                        if ( actualizado.size() > Constantes.CERO ) {
                                            createGallery(actualizado);
                                        }
                                        else {
                                            lyFotosGarantia.setVisibility( View.GONE );
                                        }
                                        mode.finish();
                                    }
                                });
                                dialog.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();

                                return true;
                            case R.id.action_select_all:
                                for ( int position = 0; position < gvImagesArticulo.getCount(); position++ ) {
                                    gvImagesArticulo.setItemChecked( position, true);
                                    adapter.singleSelection(position);
                                }
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        BloquearControles(true);
                        adapter.removeSelection();
                    }
                });
            }


        }
        catch (Exception e) {
            Toast.makeText( ActivityRegistroGarantias.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void BloquearControles(Boolean estado) {
        if ( !estado ) {
            fabGrabarGarantias.setVisibility(View.GONE);
            fabFotoGarantias.setVisibility(View.GONE);
            btnAgregarLista.setEnabled( estado );
            etDescripcion.setEnabled(estado);
            etPrestamo.setEnabled(estado);
        }
        else{
            if ( !m_EditarDatos )
                fabGrabarGarantias.setVisibility(View.VISIBLE);
            fabFotoGarantias.setVisibility(View.VISIBLE);
            btnAgregarLista.setEnabled( estado );
            etDescripcion.setEnabled(estado);
            etPrestamo.setEnabled(estado);
        }
    }

    private Bitmap resizeImage(Bitmap image, int refSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width < refSize && height < refSize)
            return image;
        int WidthResult;
        int HeightResult;
        if (width > height) {
            WidthResult = refSize;
            HeightResult = (height * refSize) / width;
        } else {
            HeightResult = refSize;
            WidthResult = (width * refSize) / height;
        }
        return Bitmap.createScaledBitmap(image, WidthResult, HeightResult, true);
    }

    private Double ObtenerTotalPrestamoGarantia( ListView lv ) {
        int count = lv.getAdapter().getCount();
        Double total = 0.00;
        for (int i = 0; i < count; i++) {
            Garantia pedidoDetalle = (Garantia) lv.getAdapter().getItem(i);
            total = total + pedidoDetalle.getPrestamo();
        }
        return total;
    }

    public class RegistrarImageGarantiaAsyns extends AsyncTask<Void, Void, String> {

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public RegistrarImageGarantiaAsyns( Context context ){

            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Registrando imagen garantia ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                List<Garantia> listGarantia = new ArrayList<>();
                List<FotoGarantia> listFotoGarantia = new ArrayList<>();
                CO_DocumentosDTO dtoEnvio = new CO_DocumentosDTO();
                int CodigoFoto = 0;

                listGarantia = new ArrayList<>();
                listGarantia = new GarantiaDAO().ListarGarantias();

                for ( Garantia garantia : listGarantia ){

                    listFotoGarantia = new ArrayList<>();
                    dtoEnvio = new CO_DocumentosDTO();

                    dtoEnvio.setnIdFlujo( m_nIdFlujoMaestro );
                    dtoEnvio.setnCodFamilia( garantia.getFamilia() );
                    dtoEnvio.setnCodArticulo( garantia.getArticulo() );
                    dtoEnvio.setnCodMarca( garantia.getMarca() );
                    dtoEnvio.setnCodLinea( garantia.getLinea() );
                    dtoEnvio.setnModelo( garantia.getModelo() );

                    listFotoGarantia = new FotoGarantiaDAO().ListarFotoxIdGarantia( garantia.getIdGarantia() );

                    if ( listFotoGarantia.size() > 0 ) {
                        for ( FotoGarantia foto : listFotoGarantia ){
                            dtoEnvio.setiImagen("");
                            Bitmap bitmap = BitmapFactory.decodeFile( foto.getRutaFotoArticulo() );
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            Bitmap bm = Utilidades.resizeImage( bitmap );
                            bm.compress( Bitmap.CompressFormat.JPEG, 50, baos );
                            byte[] imageBytes = baos.toByteArray();
                            String byteArray = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                            dtoEnvio.setiImagen( byteArray );
                            if ( bitmap != null && !bitmap.isRecycled() ) {
                                bitmap.recycle();
                            }
                            if ( bm != null && !bm.isRecycled() ) {
                                bm.recycle();
                            }

                            bitmap = null;
                            bm = null;
                            System.gc();

                            CodigoFoto = m_webApi.RegistrarImagenesGarantia( dtoEnvio );

                            if ( CodigoFoto > 0 ) {
                                resp = RESULT_OK;
                                m_mensaje = "Envio correcto de Imagenes";
                            }
                            else
                                resp = RESULT_FALSE;

                        }
                    }
                    else{
                        resp = RESULT_OK;
                        m_mensaje = "No hay Imagenes a enviar";
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
                Toast.makeText( m_context, m_mensaje, Toast.LENGTH_SHORT ).show();
                new FotoGarantiaDAO().LimpiarFotoGarantia();

                //REGISTRAR GARANTIA CREDITO

                RegistrarCreditoGarantiaAsyns registrarCreditoGarantiaAsyns = new RegistrarCreditoGarantiaAsyns( ActivityRegistroGarantias.this );
                registrarCreditoGarantiaAsyns.execute();


            }
            else if (mensaje.equals( RESULT_FALSE )) {
                Toast.makeText( m_context, "Error en registrar Imagen", Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }

        private String m_mensaje;
        private List<TB_FlujoMaestroDTO> m_ListaImagenGarantia;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    public class RegistrarCreditoGarantiaAsyns extends AsyncTask<Void, Void, String>{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public RegistrarCreditoGarantiaAsyns( Context context ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );

        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Registrando credito garantia ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try{
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                CreditoGarantiaDTO envioCredito = new CreditoGarantiaDTO();
                List<GarantiasDTO> listGarantiasDto = new ArrayList<>();
                List<Garantia> listGarantias = new ArrayList<>();
                GarantiasDTO dto = new GarantiasDTO();

                listGarantiasDto = new ArrayList<>();
                listGarantias = new ArrayList<>();
                envioCredito = new CreditoGarantiaDTO();

                envioCredito.setnProd( Constantes.PRODUCTO );
                envioCredito.setnSubProd( Constantes.SUB_PRODUCTO );
                envioCredito.setnCodAge( m_Sesion.getAgencia() );
                envioCredito.setcUsuReg( m_Sesion.getUsuario() );
                envioCredito.setnCodPersReg( m_Sesion.getCodPers() );
                envioCredito.setnIdFlujo( Constantes.ONCE );
                envioCredito.setnOrdenFlujo( Constantes.DOS );
                envioCredito.setcNomForm( "StateGarantia" );
                envioCredito.setnMoneda( Constantes.UNO );
                envioCredito.setnIdFlujoMaestro( m_nIdFlujoMaestro );
                envioCredito.setnPlazo( Constantes.UNO );
                envioCredito.setnTasaComp( m_ElScoringFinalResultado.getnTasa() );

                listGarantias = new GarantiaDAO().ListarGarantias();

                for ( Garantia garantia : listGarantias ){
                    dto = new GarantiasDTO();
                    dto.setnCodFamilia( garantia.getFamilia() );
                    dto.setnCodMarca( garantia.getMarca() );
                    dto.setnCodArticulo( garantia.getArticulo() );
                    dto.setnCodLinea( garantia.getLinea() );
                    dto.setnCodModelo( garantia.getModelo() );
                    dto.setPrestamo( garantia.getPrestamo() );
                    dto.setVCompra( garantia.getVTasado() );
                    dto.setDescripción( garantia.getDescripcion() );
                    dto.setFamilia( garantia.getcNomFamilia() );
                    dto.setArticulo( garantia.getcNomArticulo() );
                    dto.setMarca( garantia.getcNomMarca() );
                    dto.setLinea( garantia.getcNomLinea() );
                    dto.setModelo( garantia.getcNomModelo() );
                    listGarantiasDto.add( dto );
                }
                envioCredito.setoXmlLista( listGarantiasDto );

                ResponseSolicitudDTO retorno = new ResponseSolicitudDTO();
                retorno = m_webApi.RegistrarCreditoGarantiaFlujo( envioCredito );

                if ( retorno != null ){
                    if ( retorno.getnCodCred() > 0 ){
                        m_nCodCred = retorno.getnCodCred();
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
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

                new GarantiaDAO().EliminarGarantia();
                new FotoGarantiaDAO().LimpiarFotoGarantia();

                Intent pase = new Intent( ActivityRegistroGarantias.this, ActivityRegistroEstadoCuenta.class );
                pase.putExtra( "DatosScoringFinal", m_ElScoringFinalResultado );
                pase.putExtra( "IdFlujoMaestro",m_nIdFlujoMaestro );
                pase.putExtra( "nCodCred", m_nCodCred );
                pase.putExtra( "DatosPersona", m_PersonaDatos );
                pase.putExtra( "PrestamoObtenido", m_TotalPrestamo );
                startActivity( pase );
                finish();

            }
            else if (mensaje.equals( RESULT_FALSE )) {
                Toast.makeText( m_context, "Error en registrar Garantias", Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
            else {
                Toast.makeText( m_context, mensaje, Toast.LENGTH_SHORT ).show();
                pd.dismiss();
            }
        }

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;

    }

    public class ObtenerMontoMaximoPrestamoAsyns extends AsyncTask<Void, Void, String>{

        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ObtenerMontoMaximoPrestamoAsyns( Context context, ElectrodomesticoDTO dto ){

            m_ElectrodomesticoDTO = dto;
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Obteniendo monto Garantia ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }

                m_MontoPrestamoMaximo = 0;
                m_MontoPrestamoMaximo = m_webApi.obtenerMontoMaximoPrestamo( m_ElectrodomesticoDTO );

                if ( m_MontoPrestamoMaximo > 0 ){
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

                m_ValorTasado = Utilidades.TruncateDecimal( m_MontoPrestamoMaximo );
                etValorTasado.setText( "S/ " + String.valueOf( m_ValorTasado ) );
                m_PrestamoMax = Utilidades.TruncateDecimal( m_ValorTasado - ( m_ValorTasado * m_ValorTasadoPorc / 100 ) );
                etPrestamoMaximo.setText( "S/ " + String.valueOf(  m_PrestamoMax ) );

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                Toast.makeText(m_context, "¡No se obtubo el monto de la garantia!", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private double m_MontoPrestamoMaximo;
        private ElectrodomesticoDTO m_ElectrodomesticoDTO;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir del flujo?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent solicitudFinal = new Intent( ActivityRegistroGarantias.this, ActivityBandejaCreditos.class );
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
