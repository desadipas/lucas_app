package com.tibox.lucas.entidad;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desa02 on 17/04/2017.
 */

public class BandejaCreditos implements Parcelable {

        private int nIdFlujoMaestro;
        private int nIdFlujo;
        private int nCodCred;
        private int nCodAge;
        private int nCodPersTitular;
        private double nPrestamo;
        private int nEstado;
        private int nProd;
        private int nSubProd;
        private int nOrdenFlujo;
        private int nNecesario;
        private int nActibo;
        private String cNomAge;
        private String cCliente;
        private String cSubProducto;
        private String cEstado;
        private String cNombreProceso;
        private String cDocumento;
        private String cFechaReg;
        private String cMoneda;

        public BandejaCreditos(){}


        public int getnIdFlujoMaestro() {
            return nIdFlujoMaestro;
        }

        public void setnIdFlujoMaestro(int nIdFlujoMaestro) {
            this.nIdFlujoMaestro = nIdFlujoMaestro;
        }

        public int getnIdFlujo() {
            return nIdFlujo;
        }

        public void setnIdFlujo(int nIdFlujo) {
            this.nIdFlujo = nIdFlujo;
        }

        public int getnCodCred() {
            return nCodCred;
        }

        public void setnCodCred(int nCodCred) {
            this.nCodCred = nCodCred;
        }

        public int getnCodAge() {
            return nCodAge;
        }

        public void setnCodAge(int nCodAge) {
            this.nCodAge = nCodAge;
        }

        public int getnCodPersTitular() {
            return nCodPersTitular;
        }

        public void setnCodPersTitular(int nCodPersTitular) {
            this.nCodPersTitular = nCodPersTitular;
        }

        public double getnPrestamo() {
            return nPrestamo;
        }

        public void setnPrestamo(double nPrestamo) {
            this.nPrestamo = nPrestamo;
        }

        public int getnEstado() {
            return nEstado;
        }

        public void setnEstado(int nEstado) {
            this.nEstado = nEstado;
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

        public int getnOrdenFlujo() {
            return nOrdenFlujo;
        }

        public void setnOrdenFlujo(int nOrdenFlujo) {
            this.nOrdenFlujo = nOrdenFlujo;
        }

        public int getnNecesario() {
            return nNecesario;
        }

        public void setnNecesario(int nNecesario) {
            this.nNecesario = nNecesario;
        }

        public int getnActibo() {
            return nActibo;
        }

        public void setnActibo(int nActibo) {
            this.nActibo = nActibo;
        }

        public String getcNomAge() {
            return cNomAge;
        }

        public void setcNomAge(String cNomAge) {
            this.cNomAge = cNomAge;
        }

        public String getcCliente() {
            return cCliente;
        }

        public void setcCliente(String cCliente) {
            this.cCliente = cCliente;
        }

        public String getcSubProducto() {
            return cSubProducto;
        }

        public void setcSubProducto(String cSubProducto) {
            this.cSubProducto = cSubProducto;
        }

        public String getcEstado() {
            return cEstado;
        }

        public void setcEstado(String cEstado) {
            this.cEstado = cEstado;
        }

        public String getcNombreProceso() {
            return cNombreProceso;
        }

        public void setcNombreProceso(String cNombreProceso) {
            this.cNombreProceso = cNombreProceso;
        }

        public String getcDocumento() {
            return cDocumento;
        }

        public void setcDocumento(String cDocumento) {
            this.cDocumento = cDocumento;
        }

        public String getcFechaReg() {
            return cFechaReg;
        }

        public void setcFechaReg(String cFechaReg) {
            this.cFechaReg = cFechaReg;
        }

        public String getcMoneda() {
            return cMoneda;
        }

        public void setcMoneda(String cMoneda) {
            this.cMoneda = cMoneda;
        }

        protected BandejaCreditos(Parcel in) {
            nIdFlujoMaestro = in.readInt();
            nIdFlujo = in.readInt();
            nCodCred = in.readInt();
            nCodAge = in.readInt();
            nCodPersTitular = in.readInt();
            nPrestamo = in.readDouble();
            nEstado = in.readInt();
            nProd = in.readInt();
            nSubProd = in.readInt();
            nOrdenFlujo = in.readInt();
            nNecesario = in.readInt();
            nActibo = in.readInt();
            cNomAge = in.readString();
            cCliente = in.readString();

            cSubProducto = in.readString();
            cEstado = in.readString();
            cNombreProceso = in.readString();
            cDocumento = in.readString();
            cFechaReg = in.readString();
            cMoneda = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(nIdFlujoMaestro);
            dest.writeInt(nIdFlujo);
            dest.writeInt(nCodCred);
            dest.writeInt(nCodAge);
            dest.writeInt(nCodPersTitular);
            dest.writeDouble(nPrestamo);
            dest.writeInt(nEstado);
            dest.writeInt(nProd);
            dest.writeInt(nSubProd);
            dest.writeInt(nOrdenFlujo);
            dest.writeInt(nNecesario);
            dest.writeInt(nActibo);
            dest.writeString(cNomAge);
            dest.writeString(cCliente);
            dest.writeString(cSubProducto);
            dest.writeString(cEstado);
            dest.writeString(cNombreProceso);
            dest.writeString(cDocumento);
            dest.writeString(cFechaReg);
            dest.writeString(cMoneda);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<BandejaCreditos> CREATOR = new Parcelable.Creator<BandejaCreditos>() {
            @Override
            public BandejaCreditos createFromParcel(Parcel in) {
                return new BandejaCreditos(in);
            }

            @Override
            public BandejaCreditos[] newArray(int size) {
                return new BandejaCreditos[size];
            }
        };

}


