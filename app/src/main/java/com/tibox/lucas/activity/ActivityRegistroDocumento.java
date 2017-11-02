package com.tibox.lucas.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tibox.lucas.R;
import com.tibox.lucas.adaptadores.listview.AdaptadorLvDocumento;
import com.tibox.lucas.dao.DocumentoListaDAO;
import com.tibox.lucas.dao.DocumentoObligatoriosDAO;
import com.tibox.lucas.dao.FotoDocumentoDAO;
import com.tibox.lucas.dao.UsuarioDAO;
import com.tibox.lucas.entidad.DocumentoLista;
import com.tibox.lucas.entidad.DocumentoObligatorios;
import com.tibox.lucas.entidad.FotoDocumento;
import com.tibox.lucas.entidad.Usuario;
import com.tibox.lucas.network.access.AppCreditoswebApi;
import com.tibox.lucas.network.connections.AppConfig;
import com.tibox.lucas.network.dto.CO_DocumentosDTO;
import com.tibox.lucas.network.dto.DatosEntrada.Documento;
import com.tibox.lucas.network.dto.DatosObligatorioDTO;
import com.tibox.lucas.network.dto.DatosSalida.ELScoringFinalResultado;
import com.tibox.lucas.network.dto.DatosSalida.FlujoMaestro;
import com.tibox.lucas.network.dto.FlujoDTO;
import com.tibox.lucas.network.dto.FlujoLucasDTO;
import com.tibox.lucas.network.dto.PersonaCreditoDTO;
import com.tibox.lucas.network.dto.ResponseSolicitudDTO;
import com.tibox.lucas.network.response.FlujoMaestroResponse;
import com.tibox.lucas.network.response.SolicitudResponse;
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
import java.util.Scanner;

public class ActivityRegistroDocumento extends AppCompatActivity {
    EditText etDocumento,etNombres,etPrimerApellido,etSegundoApellido,etCorreo,etDireccion;
    Spinner spTipoArchivo;
    GridView gvImagesDocumento;
    LinearLayout lyFotosDocumento;
    Button btnAgregarListaDocumento, btnContinuar;
    ListView lvDocumentos;
    FloatingActionButton fabGrabarDocumentos,fabFotoDocumentos;
    ImageButton ibtnTomarFoto;

    private int accion_updateFotos = 0;
    private ActionMode actionMode;
    public static final int REQUEST_TOMAR_FOTO_DOCUMENTO = 1001;
    private static final int REQUEST_VIEW_IMAGE = 1000;
    private static final int REQUEST_WRITE_STORAGE = 112;
    private static String TAG = ActivityRegistroDocumento.class.getName();
    private ArrayList<DocumentoLista> arrayListDocumentoLista;
    private AdaptadorLvDocumento adaptadorLvDocumento;
    protected DocumentoLista m_Select;
    protected Usuario m_Sesion;
    private PersonaCreditoDTO m_PersonaDatos;
    private ELScoringFinalResultado m_ElScoringFinalResultado;
    private int m_nIdFlujoMaestro = 0;
    private int m_nCodCred = 0;
    ScrollView scrollViewDocumento;
    private int m_OrdenFlujo = 0;
    private boolean bPermisos;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private FlujoMaestro m_Flujo;
    private Toolbar toolbar;
    private String m_Url_Foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_documento);

        etDocumento = (EditText) findViewById( R.id.etDocumento );
        etNombres = (EditText) findViewById( R.id.etNombres );
        etPrimerApellido = (EditText) findViewById( R.id.etPrimerApellido );
        etSegundoApellido = (EditText) findViewById( R.id.etSegundoApellido );
        etCorreo = (EditText) findViewById( R.id.etCorreo );
        etDireccion = (EditText) findViewById( R.id.etDireccion );
        spTipoArchivo = (Spinner) findViewById( R.id.spTipoArchivo );
        gvImagesDocumento = (GridView) findViewById( R.id.gvImagesDocumento );
        lyFotosDocumento = (LinearLayout) findViewById( R.id.lyFotosDocumento );
        btnAgregarListaDocumento = (Button) findViewById( R.id.btnAgregarListaDocumento );
        lvDocumentos = (ListView) findViewById( R.id.lvDocumentos );
        //fabFotoDocumentos = (FloatingActionButton) findViewById( R.id.fabFotoDocumentos );
        scrollViewDocumento = (ScrollView) findViewById( R.id.scrollViewDocumento ) ;

        btnContinuar = (Button) findViewById( R.id.btn_continuar );
        ibtnTomarFoto = (ImageButton) findViewById( R.id.ibtn_tomar_foto );

        lvDocumentos.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        gvImagesDocumento.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar(toolbar);
        setTitle( "Obten tu crédito" );
        toolbar.setTitleTextColor( getResources().getColor( R.color.White ) );
        toolbar.setSubtitleTextColor( getResources().getColor( R.color.White ));
        toolbar.setLogo( R.drawable.logo_lucas );

        btnContinuar.setOnClickListener( fabGrabarDocumentossetOnClickListener );
        ibtnTomarFoto.setOnClickListener( fabFotoDocumentossetOnClickListener );
        btnAgregarListaDocumento.setOnClickListener( btnAgregarListaDocumentosetOnClickListener );

        int permission = ContextCompat.checkSelfPermission( this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE );

        if ( permission != PackageManager.PERMISSION_GRANTED ) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale( this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) )
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Se requiere permiso para acceder a la tarjeta SD para esta aplicación.")
                        //"Se requiere permiso para que escriba en un almacenamiento externo para esta aplicación.")
                        .setTitle("SoyLucas!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        ObtenerPermisosParaElMovil();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                Toast.makeText(this,"Se requiere permiso para acceder a la tarjeta SD para esta aplicación.",Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else{
            bPermisos = true;

            File dire = null;
            if ( Build.VERSION.SDK_INT >= 19 ) {
                dire = new File( AppConfig.directorioImagenes );
            }else{
                //dire = new File( Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ) + "/.lucasPictures/");
                dire = new File( Environment.getExternalStorageDirectory() + "/Pictures/lucasPictures/" );
                //dire = new File( AppConfig.directorioImagenes_api19menor );
            }

            //File dire = new File( AppConfig.directorioImagenes );
            if ( dire.isDirectory() )
            {
                String[] children = dire.list();
                for ( int i = 0; i < children.length; i++ )
                {
                    new File( dire, children[i] ).delete();
                }
            }
        }


        spTipoArchivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvDocumentos.setOnItemLongClickListener( lvDocumentossetOnItemLongClickListener );

        new FotoDocumentoDAO().LimpiarFotoDocumento();
        new DocumentoListaDAO().LimpiarDocumentoLista();

        m_Sesion = new Usuario();
        m_Sesion = UsuarioDAO.instanciaSesion;

        m_ElScoringFinalResultado = new ELScoringFinalResultado();
        m_PersonaDatos = new PersonaCreditoDTO();

        Intent intent = getIntent();
        if (intent != null) {

            m_nIdFlujoMaestro = intent.getExtras().getInt( "IdFlujoMaestro" );
            m_OrdenFlujo = intent.getExtras().getInt("OrdenFlujo");

            /*
            if ( intent.getExtras().getParcelable("DatosScoringFinal") != null ) {
                m_ElScoringFinalResultado = intent.getExtras().getParcelable("DatosScoringFinal");
            }
            if ( intent.getExtras().getParcelable("DatosPersona") != null ) {
                m_PersonaDatos = intent.getExtras().getParcelable("DatosPersona");
            }
            */

        }

        //Send Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.CINCO ) );
        bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_CINCO );
        bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Activity");
        bundle.putString( FirebaseAnalytics.Param.ORIGIN, "OpenActivity" );
        mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
        //


        ObtenerFlujoAsyns obtenerFlujoAsyns =
                new ObtenerFlujoAsyns( ActivityRegistroDocumento.this, m_nIdFlujoMaestro );
        obtenerFlujoAsyns.execute();

        if (savedInstanceState != null) {
            m_Url_Foto = savedInstanceState.getString( "UrlFoto" );
        }

    }

    AdapterView.OnItemLongClickListener lvDocumentossetOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
            m_Select = (DocumentoLista) adapterView.getAdapter().getItem( position );
            registerForContextMenu( adapterView );
            openContextMenu( adapterView );
            return false;
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle( ": " + m_Select.getDescripcion() );
        inflater.inflate( R.menu.menu_action_selected, menu );

        MenuItem editar = menu.findItem(R.id.item_editar);
        editar.setVisible(false);

        MenuItem Continuar = menu.findItem(R.id.item_continuar);
        Continuar.setVisible(false);

        MenuItem detalle = menu.findItem(R.id.item_detalle);
        detalle.setVisible(false);

        MenuItem anular = menu.findItem(R.id.item_anular);
        anular.setVisible(false);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_eliminar:
                new DocumentoListaDAO().LimpiarListaxIdDocumentoLista( m_Select.getIdDocumentoLista() );
                new FotoDocumentoDAO().LimpiarFotosxIdDocumentoLista( m_Select.getIdDocumentoLista() );
                Toast toast = Toast.makeText(getApplicationContext(), "¡Se eliminó correctamente!", Toast.LENGTH_SHORT);
                toast.show();
                CargarListaDocumentoLista();
                return true;
            case R.id.item_cancelar:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    View.OnClickListener fabGrabarDocumentossetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Send Analytics
            Bundle bundle = new Bundle();
            bundle.putString( FirebaseAnalytics.Param.ITEM_ID, String.valueOf( Constantes.CINCO ) );
            bundle.putString( FirebaseAnalytics.Param.ITEM_NAME, Constantes.ACTIVITY_CINCO );
            bundle.putString( FirebaseAnalytics.Param.CONTENT_TYPE, "Button");
            bundle.putString( FirebaseAnalytics.Param.ORIGIN, btnContinuar.getText().toString() );
            mFirebaseAnalytics.logEvent( FirebaseAnalytics.Event.SELECT_CONTENT, bundle );
            //

            List<DocumentoObligatorios> ListObligatorios = new ArrayList<>();
            ListObligatorios = new DocumentoObligatoriosDAO().ObtenerDocumentosObligatorios( Constantes.UNO );
            int NroDocObligatorios = ListObligatorios.size();

            List<DocumentoLista> ListDocumento = new ArrayList<>();
            ListDocumento = new DocumentoListaDAO().ListarDocumentoLista( Constantes.CERO );
            int NroDocAgregados = ListDocumento.size();
            int nroDocAgregadosObligatorios = Constantes.CERO;
            for ( DocumentoLista doc : ListDocumento ){
                if ( doc.getObligatorio() == Constantes.UNO )
                    nroDocAgregadosObligatorios += 1 ;
            }

            if ( NroDocObligatorios > Constantes.CERO ){
                if ( NroDocAgregados > Constantes.CERO ){
                    if ( nroDocAgregadosObligatorios != NroDocObligatorios ){
                        Toast.makeText( ActivityRegistroDocumento.this,"¡Agregar documentos Obligatorios!", Toast.LENGTH_SHORT ).show();
                        return;
                    }
                }
                else{
                    Toast.makeText( ActivityRegistroDocumento.this,"¡Agregar documentos!", Toast.LENGTH_SHORT ).show();
                    return;
                }
            }

            RegistrarDocumentosAsync registrarDocumentosAsync =
                    new RegistrarDocumentosAsync(ActivityRegistroDocumento.this);
            registrarDocumentosAsync.execute();

        }
    };

    View.OnClickListener fabFotoDocumentossetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int IdSpinnerDocumento = 0;
            IdSpinnerDocumento = (int) spTipoArchivo.getSelectedItemId();

            if ( new DocumentoListaDAO().BuscarTipoDocumento( (int) spTipoArchivo.getSelectedItemId() ) ){
                Toast.makeText( ActivityRegistroDocumento.this,"¡Ya existe el tipo documento en lista!", Toast.LENGTH_SHORT ).show();
                return;
            }

            if ( new FotoDocumentoDAO().BuscarExisteFotoPorgregar( Constantes.ESTADO_PORAGREGAR, IdSpinnerDocumento ) ) {
                Toast.makeText( ActivityRegistroDocumento.this,"¡No se puede agregar mas fotos!", Toast.LENGTH_SHORT ).show();
                return;
            }

            //mdipas
            if ( bPermisos )
                iniciarCamara();
            else
                ObtenerPermisosParaElMovil();
        }
    };

    View.OnClickListener btnAgregarListaDocumentosetOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            int IdSpinnerDocumento = 0;
            IdSpinnerDocumento = (int) spTipoArchivo.getSelectedItemId();

            if ( IdSpinnerDocumento > 0 )
            {

                if ( new DocumentoListaDAO().BuscarTipoDocumento( (int) spTipoArchivo.getSelectedItemId() ) ){ //LISTA
                    Toast.makeText( ActivityRegistroDocumento.this,"¡Ya existe el tipo documento en lista!", Toast.LENGTH_SHORT ).show();
                    return;
                }

                //mdipas 06.09.2017 probando validaciones
                if ( !new FotoDocumentoDAO().BuscarExisteFotoPorgregar( Constantes.ESTADO_PORAGREGAR, IdSpinnerDocumento ) ) {
                //if ( !new FotoDocumentoDAO().BuscarExisteFotoPorgregarView( Constantes.ESTADO_PORAGREGAR ) ) { // FOTOS AGREGADOS
                    Toast.makeText( ActivityRegistroDocumento.this,"¡Agregar foto del Documento!", Toast.LENGTH_SHORT ).show();
                    return;
                }


            }
            else{
                Toast.makeText( ActivityRegistroDocumento.this,"¡No se ha cargado los documentos!", Toast.LENGTH_SHORT ).show();
                return;}

            AlertDialog.Builder dialog = new AlertDialog.Builder( ActivityRegistroDocumento.this);
            dialog.setTitle("Aviso");
            dialog.setMessage("¿Seguro que desea agregar el Documento?");
            dialog.setCancelable(false);
            dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    DocumentoLista documentoLista = new DocumentoLista();
                    documentoLista.setDescripcion( new DocumentoObligatoriosDAO().ObtenerDatosTipoDoc( (int) spTipoArchivo.getSelectedItemId() ).getcNombre() );
                    documentoLista.setObligatorio( new DocumentoObligatoriosDAO().ObtenerDatosTipoDoc( (int) spTipoArchivo.getSelectedItemId() ).getnObligatorio() );
                    documentoLista.setTipoDocumento( (int) spTipoArchivo.getSelectedItemId() );
                    documentoLista.setEstado( Constantes.ESTADO_AGREGADO );

                    int IdDocumentoLista = new DocumentoListaDAO().insertar( documentoLista ); // INSERTO A LISTA DE DOC

                    //ACTUALIZA LA FOTO A ESTADO AGREGADO POR TIPO DE ARCHIVO.
                    new FotoDocumentoDAO().ActualizarIdDocumentoListaxTipoDoc( IdDocumentoLista,
                            (int) spTipoArchivo.getSelectedItemId(), Constantes.ESTADO_AGREGADO,
                            new DocumentoObligatoriosDAO().ObtenerDatosTipoDoc( (int) spTipoArchivo.getSelectedItemId() ).getcNombre());

                    CargarListaDocumentoLista();


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

    protected void CargarListaDocumentoLista(){
        arrayListDocumentoLista = new DocumentoListaDAO().ListarDocumentoLista( Constantes.ESTADO_AGREGADO );
        adaptadorLvDocumento = new AdaptadorLvDocumento( this, R.layout.activity_registro_documento, arrayListDocumentoLista );
        lvDocumentos.setAdapter( adaptadorLvDocumento );

        int countLista = lvDocumentos.getAdapter().getCount();
        if ( countLista > 0 )
            lvDocumentos.setVisibility( View.VISIBLE );
        else
            lvDocumentos.setVisibility( View.GONE );

        // Limpiamos la galeria y ocultamos.
        lyFotosDocumento.setVisibility( View.GONE );
        gvImagesDocumento.setAdapter( null );
    }

    private boolean ObtenerPermisosParaElMovil() {

        bPermisos = false;
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.WRITE_EXTERNAL_STORAGE )
                != PackageManager.PERMISSION_GRANTED )
        {

            ActivityCompat.requestPermissions( this,new String[] {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE }, //Escribir en memoria del telefono
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

    protected void iniciarCamara() {

        File newdir = null;
        if ( Build.VERSION.SDK_INT >= 19 ) {
            newdir = new File( AppConfig.directorioImagenes );
        }else{
            //newdir = new File( Environment.getExternalStorageDirectory().toString() + "/.lucasPictures/" );
            newdir = new File( Environment.getExternalStorageDirectory() + "/Pictures/lucasPictures/" );
        }

        //File newdir = new File( AppConfig.directorioImagenes );
        newdir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String tipoDoc = String.valueOf(  (int) spTipoArchivo.getSelectedItemId() );

        //Constantes.fotoUri = "";
        //Constantes.fotoUri = "Documento_"+tipoDoc+"_"+timeStamp+".jpg";
        m_Url_Foto = "Documento_"+tipoDoc+"_"+timeStamp+".jpg";

        //File newfile = new File( newdir, Constantes.fotoUri );
        File newfile = new File( newdir, m_Url_Foto );
        try {
            newfile.createNewFile();
        }
        catch (IOException e) {
        }

        Uri outputFileUri = Uri.fromFile( newfile );
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
        takePictureIntent.putExtra( MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        startActivityForResult( takePictureIntent, ActivityRegistroDocumento.REQUEST_TOMAR_FOTO_DOCUMENTO );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString( "UrlFoto", m_Url_Foto );
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( ( requestCode == REQUEST_TOMAR_FOTO_DOCUMENTO ) ) {
            if ( resultCode == Activity.RESULT_OK ) {
                String uriCompletoFoto = "";
                Bitmap bitmap = null;
                File newdir = null;

                try {
                    if ( Build.VERSION.SDK_INT >= 19 ) {
                        uriCompletoFoto = AppConfig.directorioImagenes + m_Url_Foto;
                        bitmap = this.resizeImage( BitmapFactory.decodeFile( uriCompletoFoto ), 1024) ;
                    }
                    else
                    {
                        uriCompletoFoto = Environment.getExternalStorageDirectory() + "/Pictures/lucasPictures/" + m_Url_Foto;
                        bitmap = this.resizeImage( BitmapFactory.decodeFile( uriCompletoFoto ), 1024) ;
                    }
                }
                catch ( Exception e ){
                    e.getMessage();
                }

                try {
                    bitmap.compress( Bitmap.CompressFormat.JPEG, 100, new FileOutputStream( new File( uriCompletoFoto ) ) );
                }
                catch ( FileNotFoundException e ) {
                    e.printStackTrace();
                }

                try {

                    if ( Build.VERSION.SDK_INT >= 19 ) {

                    }
                    else {

                    }

                    int ncodigospinner = (int) spTipoArchivo.getSelectedItemId();

                    FotoDocumento fotoDocumento = new FotoDocumento();
                    fotoDocumento.setDescripcion( new DocumentoObligatoriosDAO().ObtenerDatosTipoDoc( ncodigospinner ).getcNombre() );
                    fotoDocumento.setRutaFotoDocumento( uriCompletoFoto );
                    fotoDocumento.setEstado( Constantes.ESTADO_PORAGREGAR );

                    fotoDocumento.setTipoDocumento( ncodigospinner );

                    new FotoDocumentoDAO().insertar( fotoDocumento ); //INSERTA FOTO CON TIPO DE ARCHIVO ACTUAL.

                    lyFotosDocumento.setVisibility( View.VISIBLE );
                    createGallery( new FotoDocumentoDAO().ListarFotoDocumento( Constantes.ESTADO_PORAGREGAR ) );

                }
                catch ( Exception ex ) {
                    ex.printStackTrace();
                }

            }
        }
        else if ( ( requestCode == REQUEST_VIEW_IMAGE ) ) {
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void createGallery(List<FotoDocumento> List) {
        try{
            gvImagesDocumento.setNumColumns( Constantes.CUATRO );
            gvImagesDocumento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
            final Adapter adapter = new Adapter( ActivityRegistroDocumento.this, R.layout.grid_item, List ) {
                @Override
                public void create(Object item, View view) {
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = 200;
                    layoutParams.height = 140;
                    ((ImageView) view.findViewById(R.id.imagen_articulo)).setImageBitmap(BitmapFactory.decodeFile(((FotoDocumento) item).getRutaFotoDocumento()));
                    view.setPadding(0, 10, 10, 0);
                    view.setLayoutParams( layoutParams );
                    view.setTag(item);
                }
            };
            gvImagesDocumento.setAdapter(adapter);
            if ( accion_updateFotos == Constantes.CERO ) {
                gvImagesDocumento.setChoiceMode( GridView.CHOICE_MODE_MULTIPLE_MODAL );
                gvImagesDocumento.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int posicion, long l, boolean checked) {
                        int checkedCount = gvImagesDocumento.getCheckedItemCount();
                        mode.setTitle(String.valueOf(checkedCount));
                        adapter.toggleSelection( posicion );

                        if ( checkedCount > Constantes.CERO )
                            BloquearControles( false );
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
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ActivityRegistroDocumento.this);
                                dialog.setTitle("Confirmar");
                                dialog.setMessage("¿Esta seguro que desea eliminar las siguiente(s) " + selected.size() + " foto(s)?");
                                dialog.setCancelable(true);
                                dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        for (int i = (selected.size() - 1); i >= 0; i--) {
                                            if (selected.valueAt(i)) {
                                                FotoDocumento selectedItem = (FotoDocumento) adapter.getItem(selected.keyAt(i));
                                                new FotoDocumentoDAO().LimpiarFotosxIdFotoDocumento( selectedItem.getIdFotoDocumento() );
                                                adapter.remove(selectedItem);
                                            }
                                        }

                                        List<FotoDocumento> actualizado = new ArrayList<FotoDocumento>();

                                        actualizado = new FotoDocumentoDAO().ListarFotoDocumento( Constantes.ESTADO_PORAGREGAR );

                                        if ( actualizado.size() > Constantes.CERO ) {
                                            createGallery(actualizado);
                                        } else {
                                            lyFotosDocumento.setVisibility( View.GONE );
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
                                for (int position = 0; position < gvImagesDocumento.getCount(); position++) {
                                    gvImagesDocumento.setItemChecked(position, true);
                                    adapter.singleSelection(position);
                                }
                                return true;
                            default:
                                return false;
                        }
                    }
                        @Override
                        public void onDestroyActionMode (ActionMode mode){
                            BloquearControles(true);
                            adapter.removeSelection();
                        }
                    });
                }
            }
        catch (Exception e) {
            Toast.makeText( ActivityRegistroDocumento.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void BloquearControles( Boolean estado ) {

        btnAgregarListaDocumento.setEnabled( estado );
        lvDocumentos.setEnabled( estado );
        spTipoArchivo.setEnabled( estado );

        if ( !estado ){
            //fabFotoDocumentos.setVisibility(View.GONE);
            //fabGrabarDocumentos.setVisibility(View.GONE);

            ibtnTomarFoto.setEnabled( false );
            btnContinuar.setEnabled( false );
        }
        else{
            //fabFotoDocumentos.setVisibility(View.VISIBLE);
            //fabGrabarDocumentos.setVisibility(View.VISIBLE);

            ibtnTomarFoto.setEnabled( true );
            btnContinuar.setEnabled( true );
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

    public class ObtenerDocumentosObligatoriosAsync extends AsyncTask<Void, Void, String> {
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public ObtenerDocumentosObligatoriosAsync( Context context ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Actualizando documentos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
           try {
               String resp = "" ;

               if (!Common.isNetworkConnected(m_context)) {
                   return "No se encuentra conectado a internet.";
               }

               List<DatosObligatorioDTO> Listdto = new ArrayList<>();
               Listdto = m_webApi.obtenerDocObligatorios( m_Sesion.getToken() );
               new DocumentoObligatoriosDAO().LimpiarDocumentoObligatorios();

               if ( Listdto != null ){
                   if ( Listdto.size() > 0 ){
                       for ( DatosObligatorioDTO dto : Listdto ){
                           DocumentoObligatorios obligatorios = new DocumentoObligatorios();
                           obligatorios.setnCodigo( dto.getnCodigo() );
                           obligatorios.setcNombre( dto.getcNombre() );

                           String texto =  dto.getcNombre();
                           Scanner scannerTexto = new Scanner( texto );
                           int contador = 0;
                           while( scannerTexto.hasNextLine() ) {
                               String linea = scannerTexto.nextLine();
                               contador += linea.split("Obligatorio").length;
                           }

                           obligatorios.setnObligatorio( contador > 0 ? Constantes.UNO : Constantes.CERO );
                           new DocumentoObligatoriosDAO().insertar( obligatorios );

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
                CargarTiposDocumento();
            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();
                Toast.makeText(m_context, "¡No se actualizo data interna!", Toast.LENGTH_SHORT).show();
                CargarTiposDocumento();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
    }

    public class RegistrarDocumentosAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        public RegistrarDocumentosAsync( Context context ){

            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Registrando documentos ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";

                if (!Common.isNetworkConnected(m_context)) {
                    return "No se encuentra conectado a internet.";
                }


                CO_DocumentosDTO envioDocumentos = new CO_DocumentosDTO();
                Documento documento = new Documento();

                List<FotoDocumento> ListaDoc = new ArrayList<>();
                FotoDocumento Documento = new FotoDocumento();
                Documento = new FotoDocumento();
                ListaDoc = new ArrayList<>();
                ResponseSolicitudDTO retorno = new ResponseSolicitudDTO();

                ListaDoc = new FotoDocumentoDAO().ListarFotoDocumento( Constantes.ESTADO_AGREGADO );

                if ( ListaDoc.size() > 0 ){

                    //envioDocumentos = new CO_DocumentosDTO();
                    documento = new Documento();

                    documento.setnIdFlujoMaestro( m_nIdFlujoMaestro );
                    documento.setcExtencion( ".jpg" );

                    /*
                    envioDocumentos.setnProd( Constantes.PRODUCTO );
                    envioDocumentos.setnSubProd( Constantes.SUB_PRODUCTO );
                    envioDocumentos.setnIdFlujoMaestro( m_nIdFlujoMaestro );
                    envioDocumentos.setnCodAge( m_Sesion.getAgencia() );
                    envioDocumentos.setcUsuReg( m_Sesion.getUsuario() );
                    envioDocumentos.setnCodCred( m_nCodCred );
                    envioDocumentos.setnCodPersReg( m_Sesion.getCodPers() );
                    envioDocumentos.setcNomForm( "/StateDocumento" );
                    envioDocumentos.setnIdFlujo( 1029 );
                    envioDocumentos.setnOrdenFlujo( m_OrdenFlujo );
                    */

                    for ( FotoDocumento doc : ListaDoc ){

                        /*
                        envioDocumentos.setiImagen( "" );
                        envioDocumentos.setcNomArchivo( doc.getRutaFotoDocumento() );
                        envioDocumentos.setcExtension( ".JPEG" );
                        envioDocumentos.setcTipoArchivo( doc.getDescripcion() );
                        envioDocumentos.setcComentarioAnt( "DESDE MOVIL" );
                        */

                        documento.setcNomArchivo( doc.getRutaFotoDocumento());
                        documento.setcTipoArchivo( doc.getDescripcion() );


                        Bitmap bitmap = BitmapFactory.decodeFile( doc.getRutaFotoDocumento() );
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bm = Utilidades.resizeImage( bitmap );
                        bm.compress( Bitmap.CompressFormat.JPEG, 50, baos );
                        byte[] imageBytes = baos.toByteArray();
                        String byteArray = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        //envioDocumentos.setiImagen( byteArray );
                        documento.setoDocumento( Base64.encodeToString( imageBytes, Base64.DEFAULT ) );

                        if ( bitmap != null && !bitmap.isRecycled() ) {
                            bitmap.recycle();
                        }
                        if ( bm != null && !bm.isRecycled() ) {
                            bm.recycle();
                        }
                        bitmap = null;
                        bm = null;
                        System.gc();

                        //retorno = m_webApi.RegistrarDocumentosFlujo( documento, m_Sesion.getToken() );
                        m_SolicitudResponse = m_webApi.RegistrarDocumentosFlujo( documento, m_Sesion.getToken() );
                        if( !m_SolicitudResponse.isM_success() ){
                            resp = RESULT_FALSE;
                        }
                        else{
                            resp = RESULT_OK;
                            if ( m_SolicitudResponse.getM_data() != null ){
                                if ( m_SolicitudResponse.getM_data().getnValorRetorno() > Constantes.CERO ){
                                    resp = RESULT_OK;
                                    m_mensaje = "Envio correcto de Imagenes";
                                }
                                else
                                    resp = RESULT_FALSE;
                            }
                            else
                                resp = RESULT_FALSE;
                        }


                        /*
                        if ( retorno.getnValorRetorno() > Constantes.CERO ){
                            resp = RESULT_OK;
                            m_mensaje = "Envio correcto de Imagenes";}
                        else{
                            resp = RESULT_FALSE;
                            m_mensaje = "No se envio Imagenes correctamente";}
                        */

                    }
                }
                else{
                    resp = RESULT_OK;
                    m_mensaje = "No hay Imagenes a enviar";
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
                Toast.makeText( m_context, m_mensaje, Toast.LENGTH_SHORT ).show();

                //INSERTAR EL FLUJO DE REGISTRO DE DOCUMENTO
                RegistrarFlujoLucasAsync registrarFlujoLucasAsync = new RegistrarFlujoLucasAsync( m_context );
                registrarFlujoLucasAsync.execute();
                //

            }
            else if (mensaje.equals(RESULT_FALSE)) {
                pd.dismiss();
                //Toast.makeText( m_context, m_mensaje, Toast.LENGTH_SHORT ).show();

                if ( m_SolicitudResponse.getM_response().equals( Constantes.No_Autorizado ) ){

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
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }

        private String m_mensaje;
        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private SolicitudResponse m_SolicitudResponse;
    }

    protected void CargarTiposDocumento(){
        new DocumentoObligatoriosDAO().rellenaSpinner( spTipoArchivo, ActivityRegistroDocumento.this );
    }

    public class ObtenerFlujoAsyns extends AsyncTask<Void, Void,String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private int m_iD;
        private Context m_Context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;
        private FlujoMaestro m_Respuesta;
        private FlujoMaestroResponse m_FlujoMaestroResponse;

        public ObtenerFlujoAsyns( Context context, int iD ){
            m_iD = iD;
            m_Context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_Context, "Por Favor, Espere", "Obteniendo flujo ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "";
                if (!Common.isNetworkConnected( m_Context )) {
                    return "No se encuentra conectado a internet.";
                }
                m_Respuesta = new FlujoMaestro();
                m_Flujo = new FlujoMaestro();
                //m_Respuesta = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );

                m_FlujoMaestroResponse = m_webApi.obtenerFlujo( m_iD, m_Sesion.getToken() );
                if( !m_FlujoMaestroResponse.isM_success() ){
                    resp = RESULT_FALSE;
                }
                else {
                    resp = RESULT_OK;
                    if ( m_FlujoMaestroResponse.getM_data() != null  ){
                        if ( m_FlujoMaestroResponse.getM_data().getnIdFlujo() > Constantes.CERO ){
                            m_Flujo = m_FlujoMaestroResponse.getM_data();
                            resp = RESULT_OK;

                            //OBTENGO EL PASO DEL FLUJOO
                            m_FlujoMaestroResponse = m_webApi.obtenerPasosFlujo(  m_iD, m_Sesion.getToken() );
                            if( !m_FlujoMaestroResponse.isM_success() ){
                                resp = RESULT_FALSE;
                            }
                            else {
                                if ( m_FlujoMaestroResponse.getM_data() != null) {
                                    resp = RESULT_OK;
                                }
                            }
                            //END

                        }
                        else
                            resp = RESULT_FALSE;
                    }
                    else
                        resp = RESULT_FALSE;
                }


                /*
                if ( m_Respuesta != null  ){
                    if ( m_Respuesta.getnIdFlujo() > Constantes.CERO ){
                        m_Flujo = m_Respuesta;
                        resp = RESULT_OK;
                    }
                    else
                        resp = RESULT_FALSE;
                }
                else
                    resp = RESULT_FALSE;
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

                // agrego el nombre y paso del flujo
                toolbar.setSubtitle( m_FlujoMaestroResponse.getM_data().getcComentario() );
                //end

                ObtenerDocumentosObligatoriosAsync obtenerDocumentosObligatoriosAsync =
                        new ObtenerDocumentosObligatoriosAsync( m_Context );
                obtenerDocumentosObligatoriosAsync.execute();

            }
            else if ( mensaje.equals(RESULT_FALSE) ) {
                pd.dismiss();

                // agrego el nombre y paso del flujo
                toolbar.setSubtitle( m_FlujoMaestroResponse.getM_data().getcComentario() );
                //end

                if ( m_FlujoMaestroResponse.getM_response().equals( Constantes.No_Autorizado ) ){

                    Intent caducado = new Intent( m_Context, ActivityCaducidadAutorizacion.class );
                    startActivity( caducado );
                    finish();

                    /*
                    AlertDialog.Builder builder = new AlertDialog.Builder( m_Context );
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

                                            Intent solicitudFinal = new Intent( m_Context, ActivityLogin.class );
                                            startActivity( solicitudFinal );
                                            finish();

                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    */

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

    public class RegistrarFlujoLucasAsync extends AsyncTask<Void, Void, String>{
        private static final String RESULT_OK = "TRUE";
        private static final String RESULT_FALSE = "FALSE";

        private Context m_context;
        private ProgressDialog pd;
        private IAppCreditosWebApi m_webApi;


        public RegistrarFlujoLucasAsync( Context context ){
            m_context = context;
            m_webApi = new AppCreditoswebApi( context );
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show( m_context, "Por Favor, Espere", "Registrando flujo ...");
            pd.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                String resp = "" ;
                int nRespuesta = 0;

                nRespuesta = m_webApi.IngresarFlujoLucas( m_Flujo, m_Sesion.getToken() );

                if ( nRespuesta > Constantes.CERO ){
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

                // enviar a simulacion
                Intent solicitudFinal = new Intent( ActivityRegistroDocumento.this, ActivitySimulador.class );
                solicitudFinal.putExtra( "Solicitud" , "OK" );
                solicitudFinal.putExtra( "IdFlujoMaestro",m_nIdFlujoMaestro );
                startActivity( solicitudFinal );
                finish();

            }
            else if ( mensaje.equals( RESULT_FALSE ) ) {
                pd.dismiss();
                Toast.makeText(m_context, "¡No se actualizo data interna!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(m_context, mensaje, Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setTitle("Aviso");
        dialog.setMessage("¿Esta seguro que desea salir del flujo?");
        dialog.setCancelable(false);
        dialog.setNegativeButton("SI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //Intent solicitudFinal = new Intent( ActivityRegistroDocumento.this, ActivitySplashScreen.class );
                Intent solicitudFinal = new Intent( ActivityRegistroDocumento.this, ActivityBandejaCreditos.class );
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
