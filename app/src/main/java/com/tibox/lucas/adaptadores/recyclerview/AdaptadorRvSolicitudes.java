package com.tibox.lucas.adaptadores.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tibox.lucas.R;
import com.tibox.lucas.dao.BandejaCreditosDAO;
import com.tibox.lucas.entidad.BandejaCreditos;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 08/09/2017.
 */

public class AdaptadorRvSolicitudes extends RecyclerView.Adapter<AdaptadorRvSolicitudes.ViewHolder > {

    private ArrayList<BandejaCreditos> alSolicitudes;
    private Context m_context;

    public interface ARVListaSolicitudesListener{

    }

    public AdaptadorRvSolicitudes( Context context, ArrayList<BandejaCreditos> solicitudes ){
        alSolicitudes = solicitudes;
        m_context = context;
        listarSolicitudes();
    }

    //Patron Holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEstado;
        Button btnContinuar;
        ImageView ivEliminar, ivList_item_detalle_arrow;
        ImageButton ibtnEliminar;
        TextView tvMontoSolicitud;
        CardView cardViewMonto;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEstado = (TextView)itemView.findViewById(R.id.tvEstado);
            btnContinuar = (Button) itemView.findViewById(R.id.btnContinuar);
            ibtnEliminar = (ImageButton)itemView.findViewById(R.id.ibtnEliminar);
            ivList_item_detalle_arrow = (ImageView)itemView.findViewById(R.id.ivList_item_detalle_arrow);
            tvMontoSolicitud = (TextView) itemView.findViewById( R.id.tv_monto_cuota );
            cardViewMonto = (CardView) itemView.findViewById( R.id.card_view_monto );
        }
    }

    //Inflamos la vista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(parent.getContext()).inflate( R.layout.item_recyclerview_solicitud, parent, false ));
    }

    //Llenamos datos de cada item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BandejaCreditos solicitudes = alSolicitudes.get( position );
        //holder.tvEstado.setText( solicitudes.getcEstado() );
        holder.btnContinuar.setOnClickListener( clickpruebas );
        holder.tvMontoSolicitud.setText( String.valueOf( solicitudes.getnPrestamo() ) );

        if ( solicitudes.getnEstado() >= Constantes.TREINTA ){
            holder.cardViewMonto.setCardBackgroundColor( ContextCompat.getColor( m_context, R.color.soylucasverde_dos85Opacity ) );
            holder.tvEstado.setText( "Solicitado" );
            holder.btnContinuar.setTextColor( ContextCompat.getColor(m_context, R.color.soylucasverde_dos ) );
            holder.btnContinuar.setText("detalles");

        }
        else {
            holder.cardViewMonto.setCardBackgroundColor( ContextCompat.getColor(m_context, R.color.soylucasamarillo_doso85Opacity ) );
            holder.tvEstado.setText( "En proceso" );
            holder.btnContinuar.setTextColor(  ContextCompat.getColor(m_context, R.color.soylucasamarillo_dos ) );
        }

    }

    View.OnClickListener clickpruebas = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText( m_context, "HOLAPRUEBAS CLICK", Toast.LENGTH_SHORT ).show();
        }
    };

    public int getItemCount() {
        return alSolicitudes.size();
    }

    public void listarSolicitudes(){
        alSolicitudes = new ArrayList<>();
        alSolicitudes.addAll( new BandejaCreditosDAO().listBandejaCreditos() );
        notifyDataSetChanged();
    }

    public void eliminarSolicitud( int idFLujo, int position ){
        new BandejaCreditosDAO().eliminarXidFlujoMaestro( idFLujo );
        notifyItemRemoved(position);
        listarSolicitudes();
    }

}
