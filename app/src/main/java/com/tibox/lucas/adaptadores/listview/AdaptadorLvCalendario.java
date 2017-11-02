package com.tibox.lucas.adaptadores.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tibox.lucas.R;
import com.tibox.lucas.network.dto.CalendarioDTO;
import com.tibox.lucas.utilidades.Constantes;
import com.tibox.lucas.utilidades.Utilidades;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by desa02 on 23/06/2017.
 */

public class AdaptadorLvCalendario extends ArrayAdapter<CalendarioDTO> {
    private LayoutInflater layoutInflater;

    public AdaptadorLvCalendario( Context context, int resource,  List<CalendarioDTO> objects) {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);

    }

    static class Holder{
        private TextView tvCuotas;
        private TextView tvFecVenc;
        private TextView tvCapital;
        private TextView tvInteres;
        private TextView tvGastos;
        private TextView tvTotal;
        private TextView tvEstado;
        private TextView tvSeguro;

        public TextView getTvCuotas() {
            return tvCuotas;
        }

        public void setTvCuotas(TextView tvCuotas) {
            this.tvCuotas = tvCuotas;
        }

        public TextView getTvFecVenc() {
            return tvFecVenc;
        }

        public void setTvFecVenc(TextView tvFecVenc) {
            this.tvFecVenc = tvFecVenc;
        }

        public TextView getTvCapital() {
            return tvCapital;
        }

        public void setTvCapital(TextView tvCapital) {
            this.tvCapital = tvCapital;
        }

        public TextView getTvInteres() {
            return tvInteres;
        }

        public void setTvInteres(TextView tvInteres) {
            this.tvInteres = tvInteres;
        }

        public TextView getTvGastos() {
            return tvGastos;
        }

        public void setTvGastos(TextView tvGastos) {
            this.tvGastos = tvGastos;
        }

        public TextView getTvTotal() {
            return tvTotal;
        }

        public void setTvTotal(TextView tvTotal) {
            this.tvTotal = tvTotal;
        }

        public TextView getTvEstado() {
            return tvEstado;
        }

        public void setTvEstado(TextView tvEstado) {
            this.tvEstado = tvEstado;
        }

        public TextView getTvSeguro() {
            return tvSeguro;
        }

        public void setTvSeguro(TextView tvSeguro) {
            this.tvSeguro = tvSeguro;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       Holder holder = null;
        if ( convertView == null ){
            holder = new Holder();
            convertView = layoutInflater.inflate( R.layout.tab_calendario_item, parent, false );
            holder.setTvCuotas( (TextView) convertView.findViewById( R.id.tv_cuota ) );
            holder.setTvFecVenc( (TextView) convertView.findViewById( R.id.tv_fecha_venc ) );
            holder.setTvCapital( (TextView) convertView.findViewById( R.id.tv_capital ) );
            holder.setTvInteres( (TextView) convertView.findViewById( R.id.tv_interes ) );
            holder.setTvGastos( (TextView) convertView.findViewById( R.id.tv_gasto ) );
            holder.setTvTotal( (TextView) convertView.findViewById( R.id.tv_total ) );
            holder.setTvEstado( (TextView) convertView.findViewById( R.id.tv_estado ) );
            holder.setTvSeguro( (TextView) convertView.findViewById( R.id.tv_seguro ) );

            convertView.setTag(holder);
        }
        else
            holder = (Holder) convertView.getTag();

        final CalendarioDTO lista = getItem( position );

        //01/11/2017|0.00|PENDIENTE|5.21
        //DESCRIPCION
        StringTokenizer st = new StringTokenizer( lista.getdFecCob().toString(), "|" );
        String s_estado = "", s_gastos = "";
        double d_gastos, d_seguro;

        st.nextToken();st.nextToken();
        s_estado = st.nextToken();
        s_gastos = st.nextToken();
        d_gastos = Double.parseDouble( s_gastos );
        //
        d_seguro = lista.getnSeguro();

        holder.getTvCuotas().setText( String.valueOf( lista.getnNrosubCuota() ) );
        holder.getTvFecVenc().setText( String.valueOf( lista.getdFecVenc() ) );
        holder.getTvCapital().setText( String.valueOf( lista.getnCapital() ) );
        holder.getTvInteres().setText( String.valueOf( lista.getnIntComp() ) );
        holder.getTvGastos().setText( s_gastos );
        holder.getTvSeguro().setText( String.valueOf( d_seguro ) );
        holder.getTvTotal().setText( String.valueOf( Utilidades.TruncateDecimal( lista.getnCapital() + lista.getnIntComp() + d_gastos + d_seguro ) ) );
        holder.getTvEstado().setText( s_estado );
        return convertView;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getPosition(CalendarioDTO item) {
        return super.getPosition(item);
    }
}
