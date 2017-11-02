package com.tibox.lucas.adaptadores.listview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tibox.lucas.R;
import com.tibox.lucas.network.dto.OperacionesDTO;
import com.tibox.lucas.utilidades.Utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by desa02 on 27/06/2017.
 */

public class AdaptadorLvOperaciones extends ArrayAdapter<OperacionesDTO> {
    private LayoutInflater layoutInflater;

    public AdaptadorLvOperaciones( Context context, int resource, List<OperacionesDTO> objects) {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    static class Holder{
        private TextView tvOperacion;
        private TextView tvFecha;
        private TextView tvCapital;
        private TextView tvInteres;
        private TextView tvGastos;
        private TextView tvMontoTotal;
        private TextView tvNuevoSaldo;

        public TextView getTvOperacion() {
            return tvOperacion;
        }

        public void setTvOperacion(TextView tvOperacion) {
            this.tvOperacion = tvOperacion;
        }

        public TextView getTvFecha() {
            return tvFecha;
        }

        public void setTvFecha(TextView tvFecha) {
            this.tvFecha = tvFecha;
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

        public TextView getTvMontoTotal() {
            return tvMontoTotal;
        }

        public void setTvMontoTotal(TextView tvMontoTotal) {
            this.tvMontoTotal = tvMontoTotal;
        }

        public TextView getTvNuevoSaldo() {
            return tvNuevoSaldo;
        }

        public void setTvNuevoSaldo(TextView tvNuevoSaldo) {
            this.tvNuevoSaldo = tvNuevoSaldo;
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Holder holder = null;
        if ( convertView == null ){
            holder = new Holder();

            convertView = layoutInflater.inflate( R.layout.tab_operaciones_item, parent, false );
            holder.setTvNuevoSaldo( (TextView) convertView.findViewById( R.id.tv_nuevo_saldo ) );
            holder.setTvFecha( (TextView) convertView.findViewById( R.id.tv_fecha ) );
            holder.setTvCapital( (TextView) convertView.findViewById( R.id.tv_capital ) );
            holder.setTvInteres( (TextView) convertView.findViewById( R.id.tv_interes ) );
            holder.setTvGastos( (TextView) convertView.findViewById( R.id.tv_gasto ) );
            holder.setTvMontoTotal( (TextView) convertView.findViewById( R.id.tv_monto_total ) );
            holder.setTvOperacion( (TextView) convertView.findViewById( R.id.tv_operacion ) );

            convertView.setTag(holder);
        }
        else
            holder = (Holder) convertView.getTag();

        final OperacionesDTO lista = getItem( position );
        //26/05/2017|PAGO CUOTA EFECTIVO NORMAL
        //DESCRIPCION
        StringTokenizer st = new StringTokenizer( lista.getdFecha().toString(), "|" );
        String s_fecha = "", s_operacion = "";
        s_fecha = st.nextToken();
        s_operacion = st.nextToken();
        //

        holder.getTvFecha().setText( s_fecha );
        holder.getTvOperacion().setText( s_operacion );
        holder.getTvCapital().setText( String.valueOf( lista.getnCapital() ) );
        holder.getTvInteres().setText( String.valueOf( lista.getnIntComp() ) );
        holder.getTvGastos().setText( String.valueOf( lista.getnIGV() ) );
        holder.getTvMontoTotal().setText( String.valueOf( Utilidades.TruncateDecimal( lista.getnCapital() + lista.getnIntComp() + lista.getnIGV() ) ) );
        holder.getTvNuevoSaldo().setText( String.valueOf( lista.getnMontoNuevoSaldo() ) );

        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getPosition( OperacionesDTO item) {
        return super.getPosition(item);
    }
}
