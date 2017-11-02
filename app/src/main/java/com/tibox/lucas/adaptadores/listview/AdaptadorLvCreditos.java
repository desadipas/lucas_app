package com.tibox.lucas.adaptadores.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tibox.lucas.R;
import com.tibox.lucas.entidad.BandejaCreditos;

import java.util.List;

/**
 * Created by desa02 on 17/04/2017.
 */

public class AdaptadorLvCreditos extends ArrayAdapter<BandejaCreditos>{
    private LayoutInflater layoutInflater;

    public AdaptadorLvCreditos(Context context, int resource, List<BandejaCreditos> objects) {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    static class Holder{
        private TextView tvCliente;
        private TextView tvPrestamo;
        private TextView tvProducto;
        private TextView tvMoneda;
        private TextView tvProceso;
        private TextView tvEstado;
        private TextView tvFechaReg;

        public TextView getTvCliente() {
            return tvCliente;
        }

        public void setTvCliente(TextView tvCliente) {
            this.tvCliente = tvCliente;
        }

        public TextView getTvPrestamo() {
            return tvPrestamo;
        }

        public void setTvPrestamo(TextView tvPrestamo) {
            this.tvPrestamo = tvPrestamo;
        }

        public TextView getTvProducto() {
            return tvProducto;
        }

        public void setTvProducto(TextView tvProducto) {
            this.tvProducto = tvProducto;
        }

        public TextView getTvMoneda() {
            return tvMoneda;
        }

        public void setTvMoneda(TextView tvMoneda) {
            this.tvMoneda = tvMoneda;
        }

        public TextView getTvProceso() {
            return tvProceso;
        }

        public void setTvProceso(TextView tvProceso) {
            this.tvProceso = tvProceso;
        }

        public TextView getTvEstado() {
            return tvEstado;
        }

        public void setTvEstado(TextView tvEstado) {
            this.tvEstado = tvEstado;
        }

        public TextView getTvFechaReg() {
            return tvFechaReg;
        }

        public void setTvFechaReg(TextView tvFechaReg) {
            this.tvFechaReg = tvFechaReg;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if ( convertView == null ){
            holder = new Holder();
            convertView = layoutInflater.inflate( R.layout.activity_bandeja_item, parent, false );

            holder.setTvCliente( (TextView) convertView.findViewById( R.id.tvCliente ) );
            holder.setTvPrestamo( (TextView) convertView.findViewById( R.id.tvPrestamo ) );
            holder.setTvProducto( (TextView) convertView.findViewById( R.id.tvProducto ) );
            holder.setTvMoneda( (TextView) convertView.findViewById( R.id.tvMoneda ) );
            holder.setTvProceso( (TextView) convertView.findViewById( R.id.tvProceso ) );
            holder.setTvEstado( (TextView) convertView.findViewById( R.id.tvEstado ) );
            holder.setTvFechaReg( (TextView) convertView.findViewById( R.id.tvFechaReg ) );

            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder) convertView.getTag();
        }

        final BandejaCreditos lista = getItem( position );

        holder.getTvCliente().setText( lista.getcCliente() );
        holder.getTvPrestamo().setText( String.valueOf( lista.getnPrestamo() ) );
        holder.getTvProducto().setText( lista.getcSubProducto() );
        holder.getTvMoneda().setText( lista.getcMoneda() );
        holder.getTvProceso().setText( lista.getcNombreProceso() );
        holder.getTvEstado().setText( lista.getcEstado() );
        holder.getTvFechaReg().setText( lista.getcFechaReg() );
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPosition(BandejaCreditos item) {
        return super.getPosition(item);
    }
}
