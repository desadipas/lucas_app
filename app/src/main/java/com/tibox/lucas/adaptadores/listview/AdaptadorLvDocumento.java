package com.tibox.lucas.adaptadores.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tibox.lucas.R;
import com.tibox.lucas.entidad.DocumentoLista;

import java.util.List;

/**
 * Created by desa02 on 23/03/2017.
 */

public class AdaptadorLvDocumento extends ArrayAdapter<DocumentoLista> {
    private LayoutInflater layoutInflater;

    public AdaptadorLvDocumento(Context context, int resource, List<DocumentoLista> objects) {
        super(context, 0, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    static class Holder{
        private TextView tvCodigoDocumentoLista;
        private TextView tvDescripcionTipoDoc;

        public TextView getTvCodigoDocumentoLista() {
            return tvCodigoDocumentoLista;
        }

        public void setTvCodigoDocumentoLista(TextView tvCodigoDocumentoLista) {
            this.tvCodigoDocumentoLista = tvCodigoDocumentoLista;
        }

        public TextView getTvDescripcionTipoDoc() {
            return tvDescripcionTipoDoc;
        }

        public void setTvDescripcionTipoDoc(TextView tvDescripcionTipoDoc) {
            this.tvDescripcionTipoDoc = tvDescripcionTipoDoc;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if ( convertView == null ){
            holder = new Holder();
            convertView = layoutInflater.inflate( R.layout.activity_documentos_item, parent, false );
            holder.setTvCodigoDocumentoLista( (TextView) convertView.findViewById( R.id.tvCodigoDocumentoLista ) );
            holder.setTvDescripcionTipoDoc( (TextView) convertView.findViewById( R.id.tvDescripcionTipoDoc ) );
            convertView.setTag(holder);
        }
        else
        {
            holder = ( Holder ) convertView.getTag();
        }

        final DocumentoLista lista = getItem( position );
        holder.getTvCodigoDocumentoLista().setText( String.valueOf( lista.getIdDocumentoLista() ) );
        holder.getTvDescripcionTipoDoc().setText( lista.getDescripcion() );
        return convertView;

    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public int getPosition(DocumentoLista item) {
        return super.getPosition(item);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }
}
