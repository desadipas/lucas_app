package com.tibox.lucas.network.dto;

/**
 * Created by desa02 on 06/04/2017.
 */

public class ResponseSolicitudDTO {
    private int nIdFlujoMaestro;
    private int nValorRetorno;
    private int nIdSimulacion;
    private String cIdToken;
    private int nIdCliente;
    private int nIdPaso;
    private int nTipoPaso;
    private int nCodPers;
    private int nCodAge;
    private int nCodCred;
    private int nCodVerificacion;
    private int nRespuesta;
    private boolean bExistePersona;
    private int nTipoAlerta;
    private String cMensajeCliente;
    private String cError;
    private int nCodigo;

    public int getnIdFlujoMaestro() {
        return nIdFlujoMaestro;
    }

    public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
        this.nIdFlujoMaestro = nIdFlujoMaestro;
    }

    public int getnValorRetorno() {
        return nValorRetorno;
    }

    public void setnValorRetorno(int nValorRetorno) {
        this.nValorRetorno = nValorRetorno;
    }

    public int getnIdSimulacion() {
        return nIdSimulacion;
    }

    public void setnIdSimulacion(int nIdSimulacion) {
        this.nIdSimulacion = nIdSimulacion;
    }

    public String getcIdToken() {
        return cIdToken;
    }

    public void setcIdToken(String cIdToken) {
        this.cIdToken = cIdToken;
    }

    public int getnIdCliente() {
        return nIdCliente;
    }

    public void setnIdCliente(int nIdCliente) {
        this.nIdCliente = nIdCliente;
    }

    public int getnIdPaso() {
        return nIdPaso;
    }

    public void setnIdPaso(int nIdPaso) {
        this.nIdPaso = nIdPaso;
    }

    public int getnTipoPaso() {
        return nTipoPaso;
    }

    public void setnTipoPaso(int nTipoPaso) {
        this.nTipoPaso = nTipoPaso;
    }

    public int getnCodPers() {
        return nCodPers;
    }

    public void setnCodPers(int nCodPers) {
        this.nCodPers = nCodPers;
    }

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }

    public int getnCodCred() {
        return nCodCred;
    }

    public void setnCodCred(int nCodCred) {
        this.nCodCred = nCodCred;
    }

    public int getnCodVerificacion() {
        return nCodVerificacion;
    }

    public void setnCodVerificacion(int nCodVerificacion) {
        this.nCodVerificacion = nCodVerificacion;
    }

    public int getnRespuesta() {
        return nRespuesta;
    }

    public void setnRespuesta(int nRespuesta) {
        this.nRespuesta = nRespuesta;
    }

    public boolean isbExistePersona() {
        return bExistePersona;
    }

    public void setbExistePersona(boolean bExistePersona) {
        this.bExistePersona = bExistePersona;
    }

    public int getnTipoAlerta() {
        return nTipoAlerta;
    }

    public void setnTipoAlerta(int nTipoAlerta) {
        this.nTipoAlerta = nTipoAlerta;
    }

    public String getcMensajeCliente() {
        return cMensajeCliente;
    }

    public void setcMensajeCliente(String cMensajeCliente) {
        this.cMensajeCliente = cMensajeCliente;
    }

    public String getcError() {
        return cError;
    }

    public void setcError(String cError) {
        this.cError = cError;
    }

    public int getnCodigo() {
        return nCodigo;
    }

    public void setnCodigo(int nCodigo) {
        this.nCodigo = nCodigo;
    }
}
