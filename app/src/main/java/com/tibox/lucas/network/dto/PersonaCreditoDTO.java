package com.tibox.lucas.network.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.tibox.lucas.network.dto.DatosSalida.*;

/**
 * Created by desa02 on 27/03/2017.
 */

public class PersonaCreditoDTO implements Parcelable {
    private int nCodPers;
    private int nIdFlujoMaestro;
    private String cNombres;
    private String cApePat;
    private String cApeMat;
    private int nTipoDoc;
    private String nNroDoc;
    private String cCelular;
    private String cEmail;
    private String cCodZona;
    private int nTipoResidencia;
    private int nSexo;
    private String cTelefono;
    private String dFechaNacimiento;
    private int nEstadoCivil;
    private double nIngresos;
    private double nGastos;
    private int nDirTipo1;
    private int nDirTipo2;
    private int nDirTipo3;
    private String cCodigoCiudad;
    private String cDirValor1;
    private String cDirValor2;
    private String cDirValor3;
    private int nCodAge;
    private String cDniConyuge;
    private String cNomConyuge ;
    private String cApeConyuge;
    private String cRuc;
    private double nIngresoDeclado;
    private String cDirEmpleo;
    private String cTelfEmpleo;
    private String dFecIngrLab ;
    private int nProfes;
    private int nTipoEmp;
    private int nCargoPublico;
    private int nCodigoVerificacion ;
    private int nProd;
    private int nSubProd;
    private String cNomForm;
    private int nCodCred;
    private String cUsuReg;
    private int nIdFlujo;
    private int nCodPersReg;
    private int nOrdenFlujo ;
    private int nCUUI ;
    private int nSitLab ;
    private ELScoreLenddoResultado lenddoResultado;
    private ELIngresoPredecidoDemograficoResultado ingresoPredecidoDemograficoResultado;
    private ELIngresoPredecidoRCCResultado ingresoPredecidoRCCResultado;
    private ELIngresoPredecidoResultado ingresoPredecidoResultado;
    private ELScoringBuroCuotaUtilizadaResultado scoringBuroCuotaUtilizadaResultado;
    private ELScoringBuroResultado scoringBuroResultado;
    private ELScoreDemograficoResultado scoreDemograficoResultado;
    private ELScoringDemograficoResultado scoringDemograficoResultado;
    private ELScoreBuroResultado scoreBuroResultado;
    private ELMoraComercialResultado moraComercialResultado;
    private int nRechazado;
    private String cClienteLenddo;
    private String cDatoEncriptado;
    private double nIngFinal3;

    public PersonaCreditoDTO(){}

    public int getnCodPers() {
        return nCodPers;
    }

    public void setnCodPers(int nCodPers) {
        this.nCodPers = nCodPers;
    }

    public int getnIdFlujoMaestro() {
        return nIdFlujoMaestro;
    }

    public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
        this.nIdFlujoMaestro = nIdFlujoMaestro;
    }

    public String getcNombres() {
        return cNombres;
    }

    public void setcNombres(String cNombres) {
        this.cNombres = cNombres;
    }

    public String getcApePat() {
        return cApePat;
    }

    public void setcApePat(String cApePat) {
        this.cApePat = cApePat;
    }

    public String getcApeMat() {
        return cApeMat;
    }

    public void setcApeMat(String cApeMat) {
        this.cApeMat = cApeMat;
    }

    public int getnTipoDoc() {
        return nTipoDoc;
    }

    public void setnTipoDoc(int nTipoDoc) {
        this.nTipoDoc = nTipoDoc;
    }

    public String getnNroDoc() {
        return nNroDoc;
    }

    public void setnNroDoc(String nNroDoc) {
        this.nNroDoc = nNroDoc;
    }

    public String getcCelular() {
        return cCelular;
    }

    public void setcCelular(String cCelular) {
        this.cCelular = cCelular;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcCodZona() {
        return cCodZona;
    }

    public void setcCodZona(String cCodZona) {
        this.cCodZona = cCodZona;
    }

    public int getnTipoResidencia() {
        return nTipoResidencia;
    }

    public void setnTipoResidencia(int nTipoResidencia) {
        this.nTipoResidencia = nTipoResidencia;
    }

    public int getnSexo() {
        return nSexo;
    }

    public void setnSexo(int nSexo) {
        this.nSexo = nSexo;
    }

    public String getcTelefono() {
        return cTelefono;
    }

    public void setcTelefono(String cTelefono) {
        this.cTelefono = cTelefono;
    }

    public String getdFechaNacimiento() {
        return dFechaNacimiento;
    }

    public void setdFechaNacimiento(String dFechaNacimiento) {
        this.dFechaNacimiento = dFechaNacimiento;
    }

    public int getnEstadoCivil() {
        return nEstadoCivil;
    }

    public void setnEstadoCivil(int nEstadoCivil) {
        this.nEstadoCivil = nEstadoCivil;
    }

    public double getnIngresos() {
        return nIngresos;
    }

    public void setnIngresos(double nIngresos) {
        this.nIngresos = nIngresos;
    }

    public double getnGastos() {
        return nGastos;
    }

    public void setnGastos(double nGastos) {
        this.nGastos = nGastos;
    }

    public int getnDirTipo1() {
        return nDirTipo1;
    }

    public void setnDirTipo1(int nDirTipo1) {
        this.nDirTipo1 = nDirTipo1;
    }

    public int getnDirTipo2() {
        return nDirTipo2;
    }

    public void setnDirTipo2(int nDirTipo2) {
        this.nDirTipo2 = nDirTipo2;
    }

    public int getnDirTipo3() {
        return nDirTipo3;
    }

    public void setnDirTipo3(int nDirTipo3) {
        this.nDirTipo3 = nDirTipo3;
    }

    public String getcCodigoCiudad() {
        return cCodigoCiudad;
    }

    public void setcCodigoCiudad(String cCodigoCiudad) {
        this.cCodigoCiudad = cCodigoCiudad;
    }

    public String getcDirValor1() {
        return cDirValor1;
    }

    public void setcDirValor1(String cDirValor1) {
        this.cDirValor1 = cDirValor1;
    }

    public String getcDirValor2() {
        return cDirValor2;
    }

    public void setcDirValor2(String cDirValor2) {
        this.cDirValor2 = cDirValor2;
    }

    public String getcDirValor3() {
        return cDirValor3;
    }

    public void setcDirValor3(String cDirValor3) {
        this.cDirValor3 = cDirValor3;
    }

    public int getnCodAge() {
        return nCodAge;
    }

    public void setnCodAge(int nCodAge) {
        this.nCodAge = nCodAge;
    }

    public String getcDniConyuge() {
        return cDniConyuge;
    }

    public void setcDniConyuge(String cDniConyuge) {
        this.cDniConyuge = cDniConyuge;
    }

    public String getcNomConyuge() {
        return cNomConyuge;
    }

    public void setcNomConyuge(String cNomConyuge) {
        this.cNomConyuge = cNomConyuge;
    }

    public String getcApeConyuge() {
        return cApeConyuge;
    }

    public void setcApeConyuge(String cApeConyuge) {
        this.cApeConyuge = cApeConyuge;
    }

    public String getcRuc() {
        return cRuc;
    }

    public void setcRuc(String cRuc) {
        this.cRuc = cRuc;
    }

    public double getnIngresoDeclado() {
        return nIngresoDeclado;
    }

    public void setnIngresoDeclado(double nIngresoDeclado) {
        this.nIngresoDeclado = nIngresoDeclado;
    }

    public String getcDirEmpleo() {
        return cDirEmpleo;
    }

    public void setcDirEmpleo(String cDirEmpleo) {
        this.cDirEmpleo = cDirEmpleo;
    }

    public String getcTelfEmpleo() {
        return cTelfEmpleo;
    }

    public void setcTelfEmpleo(String cTelfEmpleo) {
        this.cTelfEmpleo = cTelfEmpleo;
    }

    public String getdFecIngrLab() {
        return dFecIngrLab;
    }

    public void setdFecIngrLab(String dFecIngrLab) {
        this.dFecIngrLab = dFecIngrLab;
    }

    public int getnProfes() {
        return nProfes;
    }

    public void setnProfes(int nProfes) {
        this.nProfes = nProfes;
    }

    public int getnTipoEmp() {
        return nTipoEmp;
    }

    public void setnTipoEmp(int nTipoEmp) {
        this.nTipoEmp = nTipoEmp;
    }

    public int getnCargoPublico() {
        return nCargoPublico;
    }

    public void setnCargoPublico(int nCargoPublico) {
        this.nCargoPublico = nCargoPublico;
    }

    public int getnCodigoVerificacion() {
        return nCodigoVerificacion;
    }

    public void setnCodigoVerificacion(int nCodigoVerificacion) {
        this.nCodigoVerificacion = nCodigoVerificacion;
    }

    public int getnProd() {
        return nProd;
    }

    public void setnProd(int nProd) {
        this.nProd = nProd;
    }

    public int getnSubProd() {
        return nSubProd;
    }

    public void setnSubProd(int nSubProd) {
        this.nSubProd = nSubProd;
    }

    public String getcNomForm() {
        return cNomForm;
    }

    public void setcNomForm(String cNomForm) {
        this.cNomForm = cNomForm;
    }

    public int getnCodCred() {
        return nCodCred;
    }

    public void setnCodCred(int nCodCred) {
        this.nCodCred = nCodCred;
    }

    public String getcUsuReg() {
        return cUsuReg;
    }

    public void setcUsuReg(String cUsuReg) {
        this.cUsuReg = cUsuReg;
    }

    public int getnIdFlujo() {
        return nIdFlujo;
    }

    public void setnIdFlujo(int nIdFlujo) {
        this.nIdFlujo = nIdFlujo;
    }

    public int getnCodPersReg() {
        return nCodPersReg;
    }

    public void setnCodPersReg(int nCodPersReg) {
        this.nCodPersReg = nCodPersReg;
    }

    public int getnOrdenFlujo() {
        return nOrdenFlujo;
    }

    public void setnOrdenFlujo(int nOrdenFlujo) {
        this.nOrdenFlujo = nOrdenFlujo;
    }

    public int getnCUUI() {
        return nCUUI;
    }

    public void setnCUUI(int nCUUI) {
        this.nCUUI = nCUUI;
    }

    public int getnSitLab() {
        return nSitLab;
    }

    public void setnSitLab(int nSitLab) {
        this.nSitLab = nSitLab;
    }

    public int getnRechazado() {
        return nRechazado;
    }

    public void setnRechazado(int nRechazado) {
        this.nRechazado = nRechazado;
    }

    public ELScoreLenddoResultado getLenddoResultado() {
        return lenddoResultado;
    }

    public void setLenddoResultado(ELScoreLenddoResultado lenddoResultado) {
        this.lenddoResultado = lenddoResultado;
    }

    public ELIngresoPredecidoDemograficoResultado getIngresoPredecidoDemograficoResultado() {
        return ingresoPredecidoDemograficoResultado;
    }

    public void setIngresoPredecidoDemograficoResultado(ELIngresoPredecidoDemograficoResultado ingresoPredecidoDemograficoResultado) {
        this.ingresoPredecidoDemograficoResultado = ingresoPredecidoDemograficoResultado;
    }

    public ELIngresoPredecidoRCCResultado getIngresoPredecidoRCCResultado() {
        return ingresoPredecidoRCCResultado;
    }

    public void setIngresoPredecidoRCCResultado(ELIngresoPredecidoRCCResultado ingresoPredecidoRCCResultado) {
        this.ingresoPredecidoRCCResultado = ingresoPredecidoRCCResultado;
    }

    public ELIngresoPredecidoResultado getIngresoPredecidoResultado() {
        return ingresoPredecidoResultado;
    }

    public void setIngresoPredecidoResultado(ELIngresoPredecidoResultado ingresoPredecidoResultado) {
        this.ingresoPredecidoResultado = ingresoPredecidoResultado;
    }

    public ELScoringBuroCuotaUtilizadaResultado getScoringBuroCuotaUtilizadaResultado() {
        return scoringBuroCuotaUtilizadaResultado;
    }

    public void setScoringBuroCuotaUtilizadaResultado(ELScoringBuroCuotaUtilizadaResultado scoringBuroCuotaUtilizadaResultado) {
        this.scoringBuroCuotaUtilizadaResultado = scoringBuroCuotaUtilizadaResultado;
    }

    public ELScoringBuroResultado getScoringBuroResultado() {
        return scoringBuroResultado;
    }

    public void setScoringBuroResultado(ELScoringBuroResultado scoringBuroResultado) {
        this.scoringBuroResultado = scoringBuroResultado;
    }

    public ELScoreDemograficoResultado getScoreDemograficoResultado() {
        return scoreDemograficoResultado;
    }

    public void setScoreDemograficoResultado(ELScoreDemograficoResultado scoreDemograficoResultado) {
        this.scoreDemograficoResultado = scoreDemograficoResultado;
    }

    public ELScoringDemograficoResultado getScoringDemograficoResultado() {
        return scoringDemograficoResultado;
    }

    public void setScoringDemograficoResultado(ELScoringDemograficoResultado scoringDemograficoResultado) {
        this.scoringDemograficoResultado = scoringDemograficoResultado;
    }

    public ELScoreBuroResultado getScoreBuroResultado() {
        return scoreBuroResultado;
    }

    public void setScoreBuroResultado(ELScoreBuroResultado scoreBuroResultado) {
        this.scoreBuroResultado = scoreBuroResultado;
    }

    public ELMoraComercialResultado getMoraComercialResultado() {
        return moraComercialResultado;
    }

    public void setMoraComercialResultado(ELMoraComercialResultado moraComercialResultado) {
        this.moraComercialResultado = moraComercialResultado;
    }
    //protected
    protected PersonaCreditoDTO(Parcel in) {
        nCodPers = in.readInt();
        nIdFlujoMaestro = in.readInt();
        cNombres = in.readString();
        cApePat = in.readString();
        cApeMat = in.readString();
        nTipoDoc = in.readInt();
        nNroDoc = in.readString();
        cCelular = in.readString();
        cEmail = in.readString();
        cCodZona = in.readString();
        nTipoResidencia = in.readInt();
        nSexo = in.readInt();
        cTelefono = in.readString();
        dFechaNacimiento = in.readString();
        nEstadoCivil = in.readInt();
        nIngresos = in.readDouble();
        nGastos = in.readDouble();
        nDirTipo1 = in.readInt();
        nDirTipo2 = in.readInt();
        nDirTipo3 = in.readInt();
        cCodigoCiudad = in.readString();
        cDirValor1 = in.readString();
        cDirValor2 = in.readString();
        cDirValor3 = in.readString();
        nCodAge = in.readInt();
        cDniConyuge = in.readString();
        cNomConyuge = in.readString();
        cApeConyuge = in.readString();
        cRuc = in.readString();
        nIngresoDeclado = in.readDouble();
        cDirEmpleo = in.readString();
        cTelfEmpleo = in.readString();
        dFecIngrLab = in.readString();
        nProfes = in.readInt();
        nTipoEmp = in.readInt();
        nCargoPublico = in.readInt();
        nCodigoVerificacion = in.readInt();
        nProd = in.readInt();
        nSubProd = in.readInt();
        cNomForm = in.readString();
        nCodCred = in.readInt();
        cUsuReg = in.readString();
        nIdFlujo = in.readInt();
        nCodPersReg = in.readInt();
        nOrdenFlujo = in.readInt();
        nCUUI = in.readInt();
        nSitLab = in.readInt();
        lenddoResultado = (ELScoreLenddoResultado) in.readValue(ELScoreLenddoResultado.class.getClassLoader());
        ingresoPredecidoDemograficoResultado = (ELIngresoPredecidoDemograficoResultado) in.readValue(ELIngresoPredecidoDemograficoResultado.class.getClassLoader());
        ingresoPredecidoRCCResultado = (ELIngresoPredecidoRCCResultado) in.readValue(ELIngresoPredecidoRCCResultado.class.getClassLoader());
        ingresoPredecidoResultado = (ELIngresoPredecidoResultado) in.readValue(ELIngresoPredecidoResultado.class.getClassLoader());
        scoringBuroCuotaUtilizadaResultado = (ELScoringBuroCuotaUtilizadaResultado) in.readValue(ELScoringBuroCuotaUtilizadaResultado.class.getClassLoader());
        scoringBuroResultado = (ELScoringBuroResultado) in.readValue(ELScoringBuroResultado.class.getClassLoader());
        scoreDemograficoResultado = (ELScoreDemograficoResultado) in.readValue(ELScoreDemograficoResultado.class.getClassLoader());
        scoringDemograficoResultado = (ELScoringDemograficoResultado) in.readValue(ELScoringDemograficoResultado.class.getClassLoader());
        scoreBuroResultado = (ELScoreBuroResultado) in.readValue(ELScoreBuroResultado.class.getClassLoader());
        moraComercialResultado = (ELMoraComercialResultado) in.readValue(ELMoraComercialResultado.class.getClassLoader());
        nRechazado = in.readInt();
        cClienteLenddo = in.readString();
        cDatoEncriptado = in.readString();
        nIngFinal3 = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nCodPers);
        dest.writeInt(nIdFlujoMaestro);
        dest.writeString(cNombres);
        dest.writeString(cApePat);
        dest.writeString(cApeMat);
        dest.writeInt(nTipoDoc);
        dest.writeString(nNroDoc);
        dest.writeString(cCelular);
        dest.writeString(cEmail);
        dest.writeString(cCodZona);
        dest.writeInt(nTipoResidencia);
        dest.writeInt(nSexo);
        dest.writeString(cTelefono);
        dest.writeString(dFechaNacimiento);
        dest.writeInt(nEstadoCivil);
        dest.writeDouble(nIngresos);
        dest.writeDouble(nGastos);
        dest.writeInt(nDirTipo1);
        dest.writeInt(nDirTipo2);
        dest.writeInt(nDirTipo3);
        dest.writeString(cCodigoCiudad);
        dest.writeString(cDirValor1);
        dest.writeString(cDirValor2);
        dest.writeString(cDirValor3);
        dest.writeInt(nCodAge);
        dest.writeString(cDniConyuge);
        dest.writeString(cNomConyuge);
        dest.writeString(cApeConyuge);
        dest.writeString(cRuc);
        dest.writeDouble(nIngresoDeclado);
        dest.writeString(cDirEmpleo);
        dest.writeString(cTelfEmpleo);
        dest.writeString(dFecIngrLab);
        dest.writeInt(nProfes);
        dest.writeInt(nTipoEmp);
        dest.writeInt(nCargoPublico);
        dest.writeInt(nCodigoVerificacion);
        dest.writeInt(nProd);
        dest.writeInt(nSubProd);
        dest.writeString(cNomForm);
        dest.writeInt(nCodCred);
        dest.writeString(cUsuReg);
        dest.writeInt(nIdFlujo);
        dest.writeInt(nCodPersReg);
        dest.writeInt(nOrdenFlujo);
        dest.writeInt(nCUUI);
        dest.writeInt(nSitLab);
        dest.writeValue(lenddoResultado);
        dest.writeValue(ingresoPredecidoDemograficoResultado);
        dest.writeValue(ingresoPredecidoRCCResultado);
        dest.writeValue(ingresoPredecidoResultado);
        dest.writeValue(scoringBuroCuotaUtilizadaResultado);
        dest.writeValue(scoringBuroResultado);
        dest.writeValue(scoreDemograficoResultado);
        dest.writeValue(scoringDemograficoResultado);
        dest.writeValue(scoreBuroResultado);
        dest.writeValue(moraComercialResultado);
        dest.writeInt(nRechazado);
        dest.writeString(cClienteLenddo);
        dest.writeString(cDatoEncriptado);
        dest.writeDouble(nIngFinal3);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PersonaCreditoDTO> CREATOR = new Parcelable.Creator<PersonaCreditoDTO>() {
        @Override
        public PersonaCreditoDTO createFromParcel(Parcel in) {
            return new PersonaCreditoDTO(in);
        }

        @Override
        public PersonaCreditoDTO[] newArray(int size) {
            return new PersonaCreditoDTO[size];
        }
    };

    public String getcClienteLenddo() {
        return cClienteLenddo;
    }

    public void setcClienteLenddo(String cClienteLenddo) {
        this.cClienteLenddo = cClienteLenddo;
    }

    public String getcDatoEncriptado() {
        return cDatoEncriptado;
    }

    public void setcDatoEncriptado(String cDatoEncriptado) {
        this.cDatoEncriptado = cDatoEncriptado;
    }

    public double getnIngFinal3() {
        return nIngFinal3;
    }

    public void setnIngFinal3(double nIngFinal3) {
        this.nIngFinal3 = nIngFinal3;
    }
}
