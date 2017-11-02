package com.tibox.lucas.utilidades;

/**
 * Created by apple on 12/09/16.
 */
public class Constantes {

    // Tablas
    public static final String TABLE_USUARIO = "Usuario";
    public static final String TABLE_CATALAGOCODIGOS = "CatalagoCodigos";
    public static final String TABLE_ZONAS = "Zonas";
    public static final String TABLE_FOTOGARANTE = "FotoGarantia";
    public static final String TABLE_FAMILIAGARANTIA = "FamiliaGarantia";
    public static final String TABLE_GARANTIA = "Garantia";
    public static final String TABLE_FOTODOCUMENTO = "FotoDocumento";
    public static final String TABLE_DOCUMENTO_OBLIGATORIOS = "DocumentoObligatorios";
    public static final String TABLE_DOCUMENTOLISTA = "DocumentoLista";
    public static final String TABLE_CREDITOS = "BandejaCreditos";
    public static final String TABLE_VARNEGOCIO = "Varnegocio";


    public static int ESTADO_PORAGREGAR = 0;
    public static int ESTADO_AGREGADO = 1;
    public static int ESTADO_ELIMINADO = 2;

    public static int VARNEGOCIO_CUOTAMINIMA = 10221;
    //public static int VARNEGOCIO_MONTOMINIMO = 10220;
    public static int VARNEGOCIO_TASAINTERES = 10222;

    public static int VARNEGOCIO_RECHAZOXDIA = 10220;
    public static int VARNEGOCIO_CREDITOSXCLIENTE_FLUJO = 10219;
    public static int VARNEGOCIO_FECHASISTEMA = 1;

    //NUMERO
    public static int CERO = 0;
    public static int UNO = 1;
    public static int DOS = 2;
    public static int TRES = 3;
    public static int CUATRO = 4;
    public static int CINCO = 5;
    public static int SEIS = 6;
    public static int SIETE = 7;
    public static int OCHO = 8;
    public static int NUEVE = 9;
    public static int DIEZ = 10;
    public static int ONCE = 11;
    public static int DOCE = 12;
    public static int TRECE = 13;
    public static int CATORCE = 14;
    public static int QUINCE = 15;
    public static int DIECISEIS = 16;
    public static int DIECISIETE = 17;
    public static int DIECIOCHO = 18;
    public static int DIECINUEVE = 19;
    public static int VEINTE = 20;
    public static int TREINTA = 30;
    public static int CINCUENTA = 50;


    public static String fotoUri = "";

    public static int TIPO_DIRECCION = 800;
    public static int TIPO_SEXO = 2030;
    public static int TIPO_ESTADO_CIVIL = 100;
    public static int TIPO_ACT_ECO = 12025;
    public static int TIPO_EST_LABORAL = 5031;
    public static int TIPO_PROFESION = 5066;
    public static int TIPO_EMPLEO = 9646; // –> tipo de empleo por mientras
    public static int TIPO_RESIDENCIA = 9615;
    public static int TIPO_ENVIO = 11003;

    public static int TIPO_NRO = 500;
    public static int TIPO_DPTO = 501;
    public static int TIPO_CARGO_PUBLICO = 502;
    public static int TIPO_DOC_CRED_ONLINE = 10240;
    public static int TIPO_BANCOS = 10250;
    public static final int MEGABYTE = 1024 * 1024;

    //Lucas
    public static int AGENCIA_LUCAS = 5;
    public static int PRODUCTO = 3;
    public static int SUB_PRODUCTO = 9;
    //Scoring
    public static int PRODUCTO_SCORING = 12;

    //DOCUMENTOS CREDITO ONLINE
    public static int DOC_CRED_ONLINE_HOJA_RESUMEN1 = 1;
    public static int DOC_CRED_ONLINE_HOJA_RESUMEN2 = 2;
    public static int DOC_CRED_ONLINE_VOUCHER = 3;
    public static int DOC_CRED_ONLINE_ESTADO_DE_CUENTA = 4;
    public static int DOC_CRED_ONLINE_CONTRATO = 5;
    public static int DOC_CRED_ONLINE_SOLICITUD = 6;
    public static int DOC_CRED_ONLINE_SEGURO = 7;
    public static int DOC_CRED_ONLINE_PEP = 8;



    //LOCALES DESARROLLO PERU
/*
    public static String IP_CONEXIONYPUERTO = "http://10.19.150.20:2009";
    public static String IP_CONEXIONYPUERTO_SCORING = "http://10.19.150.20:3000"; */

    //public static String IP_CONEXIONYPUERTO_API = "http://192.168.104.74:4008";
    //public static String IP_CONEXIONYPUERTO_API = "http://10.19.37.32:4007";

    //public static String IP_CONEXIONYPUERTO_LUCAS = "http://10.19.150.20:2101";


    //DESARROLLO PERU IP PUBLICA

    public static String IP_CONEXIONYPUERTO = "http://200.60.88.244:2009"; // ws para WEB lucas
    public static String IP_CONEXIONYPUERTO_SCORING = "http://200.60.88.244:3000"; // ws scoring RALVA
    public static String IP_CONEXIONYPUERTO_API = "http://200.60.88.244:4080"; //puerto de inventory (provisional)
    public static String IP_CONEXION_ENCRIPTAR = "http://200.60.88.244:1101";

    //public static String IP_CONEXIONYPUERTO_LUCAS = "http://200.60.88.244:2101";
    public static String IP_CONEXIONYPUERTO_LUCAS = "http://190.187.249.242:8022/Api";

    //ENVIO SMS
    //public static final String ENVIO_SMS = "http://efitec.adminbulk.com/rest/ws/bulkSms?usr=wscruz&pas=tYFsatT9&num=51";
    //public static final String ENVIO_SMS_2 = "&msg=";
    public static final String ENVIO_SMS = "https://apismsi.aldeamo.com/smsr/r/hcws/smsSendGet/LaCruz/lacruz123*/";
    public static final String ENVIO_SMS_2 = "/51/";

    public static final String URL_LENDDO = "http://s.ilc.pe/";
    public static final String Mensaje_Rechazado = "Hola Soy lucas, lamentamos informarte que te hemos rechazado en una evaluacion. Puedes volver a intentarlo a unos diás, gracias por confiar en nosotros.";
    public static final String Mensaje_Rechazado_PEP = "Hola Soy lucas, en esta oportunidad no podemos procesar su solicitud por ser Persona Expuesta Políticamente, gracias por confiar en nosotros.";
    public static final String Mensaje_Texto_Sms = "Hola, bienvenido (a) a SoyLucas. Tu usuario es el correo que ingresaste y tu contraseña el número de tu DNI. Por seguridad debes cambiar tu contraseña.";
    public static final String Contacto_Lucas = "informes@soylucas.pe";
    public static final String No_Autorizado = "Unauthorized";
    public static final String Mensaje_Sesion_Caducado = "La sesión ha caducado, por favor, pulse sobre el siguiente bóton para volver acceder a la aplicación.";

    //GPS
    public static double LATITUD = 0;
    public static double LONGITUD = 0;

    // URL
    public static final String URL_AUTENTICACION = "/ServiceSeguridad.svc/rest/Usuario/Autentificacion";
    public static final String URL_CONSULTA_CATALOGO_VARNEGOCIO = "/ServiceVarnegocio.svc/rest/Varnegocio/ObtenerPorVar?id=";
    public static final String URL_CONSULTA_MONTOCUOTA = "/ServiceDesembolso.svc/rest/Desembolso/ObtenerMontoCuota?nPrestamo=";
    public static final String URL_CONSULTA_MONTOCUOTA2 = "&nCuotas=";
    public static final String URL_CONSULTA_MONTOCUOTA3 = "&nPeriodicidad=30&nTasa=";
    public static final String URL_DEPARTAMENTOS = "/ServiceZona.svc/rest/ListaDepartamento";
    public static final String URL_PROVICIASPORID = "/ServiceZona.svc/rest/ListaProvincia?cDepartamento=";
    public static final String URL_DISTROPORIDS = "/ServiceZona.svc/rest/ListaDistrito?cDepartamento=";
    public static final String URL_DISTROPORIDS2 = "&cProvincia=";
    public static final String URL_CONSULTA_CATALOGOCODIGO = "/ServiceCatalogoCodigo.svc/rest/ListaCatalogo?nCodigo=";
    public static final String URL_REGISTRO_CLIENTE = "/ServiceCliente.svc/rest/Cliente/GuardarPersonaScoringFlujo";
    public static final String URL_LLENA_COMBO_FAMILIA = "/ServiceElectrodomestico.svc/rest/Electro/LlenaComboFamilia";
    public static final String URL_LLENA_COMBO_ARTICULO = "/ServiceElectrodomestico.svc/rest/Electro/LlenaComboArticulo";
    public static final String URL_LLENA_COMBO_MARCA = "/ServiceElectrodomestico.svc/rest/Electro/LlenaComboMarca";
    public static final String URL_LLENA_COMBO_LINEA = "/ServiceElectrodomestico.svc/rest/Electro/LlenaComboLinea";
    public static final String URL_LLENA_COMBO_MODELO = "/ServiceElectrodomestico.svc/rest/Electro/LlenaComboModelo";
    public static final String URL_LLENA_COMBO_DOCOBLIGATORIO = "/ServiceCredito.svc/rest/Credito/SelLlenaComboDocObligatorio";
    //public static final String URL_OBTENER_DATOS_FLUJO_ID = "/ServiceCredito.svc/rest/Credito/ObtenerDatosFlujoId?nIdFlujoMaestro=";

    public static final String URL_REGISTRO_IMAGE_GARANTIA = "/ServiceCredito.svc/rest/Credito/InsImagenesArticulos";
    public static final String URL_REGISTRO_GARANTIA = "/ServiceCredito.svc/rest/Credito/RegistraCreditoGarantiaFlujo";
    public static final String URL_REGISTRO_ESTADODECUENTA = "/ServiceCredito.svc/rest/Credito/RegistroEstadoCuentaFlujo";
    public static final String URL_REGISTRO_DOCUMENTO = "/ServiceCredito.svc/rest/Credito/RegistrarDocumentoCreditoFlujo";
    public static final String URL_REGISTRO_IMAGE_DOCUMENTO = "/ServiceCredito.svc/rest/Credito/RegistrarDocumentoCreditoFlujo";
    public static final String URL_MONTO_MAX_PRESTAMO = "/ServiceElectrodomestico.svc/rest/Electrodomestico/MontoMaximoPrestamo";

    public static final String URL_REGISTRO_PERSONA = "/api/PostRegistroClienteCredito";
    public static final String URL_BUSQUEDA_PERSONA = "/api/GetBuscarClienteCredito?cDocumento=";
    public static final String URL_INS_IMAGEN_GARANTIA = "/api/PostinsImagenesArticulos";
    public static final String URL_INS_CREDITO_GARANTIA = "/api/PostInsertSimuladorPrendatodoFlujo";
    public static final String URL_INS_ESTADO_CUENTA_FLUJO = "/api/PostInsertEstadoCuentaFlujo";
    public static final String URL_REGISTRAR_DOCUMENTO_FLUJO = "/api/PostRegistrarDocumentoCreditoFlujo";
    public static final String URL_REGISTRAR_CREDITO_GARANTIA_FLUJO  = "/api/PostInsertCreditoGarantiaFlujo";
    public static final String URL_OBTENER_TEA = "/api/GetObtenerTEA?nTasa=";
    public static final String URL_INS_SEGUIMIENTOCRED = "/api/PostRegistraSeguimientoDatos";
    public static final String URL_OBTENER_CREDITOS = "/api/GetObtenerCreditos";
    public static final String URL_OBTENER_DATOS_CREDITO_FLUJO = "/api/GetObtenerFlujoCredito?nIdFlujoMaestro=";

    public static final String URL_SCORE = "/SoftBoxApi/Score";
    public static final String URL_INGRESOPREDECIDO = "/SoftBoxApi/IngresoPredecido";
    public static final String URL_SCORING = "/SoftBoxApi/Scoring";

    public static final String URL_TERMINOS_CONDICIONES = "https://www.soylucas.pe/Inicio/Images/landing/pdf/TerminosCondicionesCliente.pdf";
    public static final String URL_OBTENER_DETALLE_PDF_CREDITO = "/api/GetImageDetalleLucas";
    public static final String URL_OBTENER_PDF_CREDITO = "/api/GetImageLucas";
    public static final String URL_REGISTRAR_FIRMA_DIGITAL = "/api/PostRegistrarFirmaDigitalLucas";
    public static final String URL_REGISTRAR_NUMERO_CUENTA = "/api/PostRegistrarNumCuentaLucas";
    public static final String URL_OBTENER_FLUJO_LUCAS = "/api/GetFlujoLucas?nIdFlujoMaestro=";
    public static final String URL_ANULAR_CREDITO = "/api/PostAnularCreditoLucas";
    public static final String URL_ENCRIPTAR_DATO = "/usuario/Encriptar/";
    public static final String URL_ACTUALIZAR_CONTRASENIA = "/api/PostActualizarContraseniaLucas";
    public static final String URL_AUTENTICACION_LUCAS = "/api/PostAutenticacion";
    public static final String URL_OBTENER_CALENDARIO_CREDITO_LUCAS = "/api/GetConsultaCalendarioCreditoLucas?nCodCred=";
    public static final String URL_OBTENER_CALENDARIO_CREDITO_LUCAS_2 = "&nCodAge=";
    public static final String URL_OBTENER_OPERACIONES_CREDITO_LUCAS = "/api/GetConsultaOperacionesCreditoLucas?nCodCred=";
    public static final String URL_OBTENER_OPERACIONES_CREDITO_LUCAS_2 = "&nCodAge=";
    public static final String URL_REGISTRO_FLUJO_LUCAS = "/api/PostRegistroFlujoLucas";

    //LENDDO
    public static final String APPLICATION_ID = "LENDDOLUCAS";
    public static final String PARNET_SCRIPT_ID_LENDDO = "593ad7e8f7a5791f9a00f86d";
    public static final String API_SECRET = "wZlnPzgpLrccjGtxLdJWY/Fc+Jtl9DBMhCypaFxHHByDHtiUfs/KHKdZuwtElh4cnh8KT2IieuVeCVXme20geg==";
    public static final String AUTHORIZE_DATA_ENDPOINT = "https://authorize-api%s.partner-service.link";
    public static final String API_GATEWAY_URL = "https://gateway.partner-service.link";

    public static final String ACTIVITY_UNO = "InicioSesion";
    public static final String ACTIVITY_DOS = "BandejaCreditos";
    public static final String ACTIVITY_TRES = "Lenddo";
    public static final String ACTIVITY_CUATRO = "SimuladorCredito";
    public static final String ACTIVITY_CINCO = "RegistroDocumento";
    public static final String ACTIVITY_SEIS = "DatosOperacion";
    public static final String ACTIVITY_SIETE = "DatosContrato";
    public static final String ACTIVITY_OCHO = "GestionSolicitud";
    public static final String ACTIVITY_NUEVE = "DatosPersona";
    public static final String ACTIVITY_DIEZ = "CambioContraseña";
    public static final String ACTIVITY_ONCE = "ConsultaCredito";

    //SERVICIO LUCAS
    public static final String Lucas_WebApi_Usuario = "/Usuario";
    public static final String Lucas_WebApi_Token = "/token";
    public static final String Lucas_WebApi_Usuario_Login = "/Usuario/DatosLogin";
    public static final String Lucas_WebApi_Usuario_Verificar = "/Usuario/Verificar";
    public static final String Lucas_WebApi_Usuario_Desencriptar = "/Usuario/Desencriptar";
    public static final String Lucas_WebApi_Usuario_CambioPass = "/Usuario/CambioPass";
    public static final String Lucas_WebApi_Usuario_Encriptar = "/Usuario/Encriptar";
    public static final String Lucas_WebApi_Persona = "/Persona";
    public static final String Lucas_WebApi_Persona_Put = "/Persona/Put";
    public static final String Lucas_WebApi_Persona_Datos = "/Persona/Datos";
    public static final String Lucas_WebApi_Persona_Verifica = "/Persona/Verifica/";
    public static final String Lucas_WebApi_Persona_Celular = "/Persona/Celular/";

    public static final String Lucas_WebApi_CatalogoCodigo = "/CatalogoCodigo/";
    public static final String Lucas_WebApi_Zona_Departamento = "/Zona/Departamento";
    public static final String Lucas_WebApi_Zona_Provincia = "/Zona/Provincia/";
    public static final String Lucas_WebApi_Zona_Distrito = "/Zona/Distrito/";
    public static final String Lucas_WebApi_Credito = "/Credito";
    public static final String Lucas_WebApi_Credito_Bandeja = "/Credito/Bandeja";
    public static final String Lucas_WebApi_Credito_Modalidad = "/Credito/Modalidad";
    public static final String Lucas_WebApi_Credito_DatosPrestamo = "/Credito/DatosPrestamo/";
    public static final String Lucas_WebApi_Credito_Firma = "/Credito/Firma";
    public static final String Lucas_WebApi_Credito_Calendario_Lista = "/Credito/Calendario/Lista/";
    public static final String Lucas_WebApi_Credito_Tcea = "/Credito/TCEA";
    public static final String Lucas_WebApi_Credito_Kardex_Lista = "/Credito/Kardex/Lista/";
    public static final String Lucas_WebApi_Credito_RechazadoPorDia = "/Credito/RechazadoPorDia/";
    public static final String Lucas_WebApi_Credito_AnulaxActualizacion = "/Credito/AnulaxActualizacion/";
    public static final String Lucas_WebApi_Credito_CreditoxFlujo = "/Credito/CreditoxFlujo/";
    public static final String Lucas_WebApi_Credito_Calendario= "/Credito/Calendario";
    public static final String Lucas_WebApi_VarNegocio = "/VarNegocio/";
    public static final String Lucas_WebApi_Evaluacion = "/Evaluacion";
    public static final String Lucas_WebApi_Evaluacion_PreEvaluacion = "/Evaluacion/PreEvaluacion";
    public static final String Lucas_WebApi_Flujo = "/Flujo/";
    public static final String Lucas_WebApi_Flujo_Wizard = "/Flujo/Wizard/";
    public static final String Lucas_WebApi_Flujo_Eliminar = "/Flujo/Eliminar";
    public static final String Lucas_WebApi_Post_Flujo = "/Flujo";
    public static final String Lucas_WebApi_Flujo_Solicitud = "/Flujo/Solicitud/";
    public static final String Lucas_WebApi_Documento_Subir = "/Documento/Subir";
    public static final String Lucas_WebApi_Documento_Tipo = "/Documento/Tipo";
    public static final String Lucas_WebApi_Catalogo = "/Catalogocodigo/";
    public static final String Lucas_WebApi_Reporte = "/Reporte/";
    public static final String Lucas_WebApi_Reporte_Envio = "/Reporte/Envio";
    public static final String Lucas_WebApi_Reporte_Generar = "/Reporte/Generar/";
    public static final String Lucas_WebApi_Alerta_SMS = "/Alerta/SMS";
    public static final String Lucas_WebApi_Alerta_Email = "/Alerta/Email";




}

