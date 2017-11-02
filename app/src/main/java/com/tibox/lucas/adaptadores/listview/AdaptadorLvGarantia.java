package com.tibox.lucas.adaptadores.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tibox.lucas.R;
import com.tibox.lucas.entidad.Garantia;

import java.util.List;

/**
 * Created by desa02 on 21/03/2017.
 */

public class AdaptadorLvGarantia extends ArrayAdapter<Garantia> {
    private LayoutInflater layoutInflater;

    public AdaptadorLvGarantia(Context context, int resource, List<Garantia> objects) {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    static class Holder{
        private TextView CodigoGarantia;
        private TextView DescripcionGarantia;
        private TextView DescripcionDescuento;
        private TextView Prestamo;

        public TextView getCodigoGarantia() {
            return CodigoGarantia;
        }

        public void setCodigoGarantia(TextView codigoGarantia) {
            CodigoGarantia = codigoGarantia;
        }

        public TextView getDescripcionGarantia() {
            return DescripcionGarantia;
        }

        public void setDescripcionGarantia(TextView descripcionGarantia) {
            DescripcionGarantia = descripcionGarantia;
        }

        public TextView getDescripcionDescuento() {
            return DescripcionDescuento;
        }

        public void setDescripcionDescuento(TextView descripcionDescuento) {
            DescripcionDescuento = descripcionDescuento;
        }

        public TextView getPrestamo() {
            return Prestamo;
        }

        public void setPrestamo(TextView prestamo) {
            Prestamo = prestamo;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if ( convertView == null ){
            holder = new Holder();
            convertView = layoutInflater.inflate( R.layout.activity_garantias_item, parent, false );
            holder.setCodigoGarantia( (TextView) convertView.findViewById( R.id.tvCodigoGarantia ) );
            holder.setDescripcionDescuento( (TextView) convertView.findViewById( R.id.tvDescripcionDescuento ) );
            holder.setDescripcionGarantia( (TextView) convertView.findViewById( R.id.tvDescripcionGarantia ) );
            holder.setPrestamo( (TextView) convertView.findViewById( R.id.tvPrestamo ) );
            convertView.setTag(holder);
        }
        else
        {
            holder = ( Holder ) convertView.getTag();
        }

        final Garantia garantia = getItem( position );
        holder.getCodigoGarantia().setText( String.valueOf( garantia.getIdGarantia() ) );
        holder.getDescripcionDescuento().setText( garantia.getDescripcion() );
        holder.getDescripcionGarantia().setText( garantia.getDescripcionGarantia() );
        holder.getPrestamo().setText(  "S/ " + String.valueOf( garantia.getPrestamo() ) );
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public int getPosition(Garantia item) {
        return super.getPosition(item);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }
}
