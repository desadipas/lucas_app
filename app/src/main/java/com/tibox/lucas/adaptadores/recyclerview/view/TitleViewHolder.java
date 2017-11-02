package com.tibox.lucas.adaptadores.recyclerview.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;
import com.tibox.lucas.R;
import com.tibox.lucas.entidad.BandejaCreditos;
import com.tibox.lucas.entidad.expandablegroup.Title;
import com.tibox.lucas.utilidades.Constantes;

import java.util.ArrayList;

/**
 * Created by desa02 on 11/09/2017.
 */

public class TitleViewHolder extends GroupViewHolder {

    private TextView tvEstado,tvCreditoOnline,tvCintaItemCredito;
    private Button btnContinuar;
    private ImageView ivEliminar, ivList_Item_Detalle_Arrow;
    private ImageButton ibtnEliminar;
    private Context appContext;
    private ArrayList<BandejaCreditos> alSolicitudes;
    private TextView tvMontoSolicitud;
    private CardView cardViewMonto;
    private LinearLayout lyItemBandejaCreditos;

    private RVListenerGroup m_RVListenerGroup;

    public interface RVListenerGroup{
        void ieliminarCitaGroup( BandejaCreditos datos, int position );
        void iContinuarGroup( BandejaCreditos bandejaCreditos, int position );
    }

    public TitleViewHolder(View itemView, final Context appContext, ArrayList<BandejaCreditos> solicitudes, RVListenerGroup listenerGroup ) {
        super(itemView);
        this.appContext = appContext;
        this.alSolicitudes = solicitudes;
        this.m_RVListenerGroup = listenerGroup;

        tvEstado = (TextView) itemView.findViewById(R.id.tvEstado);
        tvCreditoOnline = (TextView) itemView.findViewById( R.id.tv_credito_online );
        tvCintaItemCredito = (TextView) itemView.findViewById( R.id.tv_cinta_item_credito );
        btnContinuar = (Button) itemView.findViewById(R.id.btnContinuar);
        ibtnEliminar = (ImageButton) itemView.findViewById(R.id.ibtnEliminar);
        ivList_Item_Detalle_Arrow = (ImageView) itemView.findViewById( R.id.ivList_item_detalle_arrow );
        tvMontoSolicitud = (TextView) itemView.findViewById( R.id.tv_monto_cuota );
        cardViewMonto = (CardView) itemView.findViewById( R.id.card_view_monto );
        lyItemBandejaCreditos = (LinearLayout) itemView.findViewById( R.id.ly_item_bandeja_creditos );

        btnContinuar.setOnClickListener( continuar );
        ibtnEliminar.setOnClickListener( eliminar );
    }


    View.OnClickListener eliminar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ( m_RVListenerGroup != null ){
                int posicion = 0;
                if ( alSolicitudes.size() >= ( getAdapterPosition() + Constantes.UNO ) )
                    posicion = getAdapterPosition();
                else
                    posicion = getAdapterPosition() == Constantes.CERO ? Constantes.CERO : getAdapterPosition() - Constantes.CUATRO;

                m_RVListenerGroup.ieliminarCitaGroup( alSolicitudes.get( posicion ), posicion );
            }
        }
    };

    View.OnClickListener continuar = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if ( m_RVListenerGroup != null ){
                int posicion = 0;
                if ( alSolicitudes.size() >= ( getAdapterPosition() + Constantes.UNO ) )
                    posicion = getAdapterPosition();
                else
                    posicion = getAdapterPosition() == Constantes.CERO ? Constantes.CERO : getAdapterPosition() - Constantes.CUATRO;

                m_RVListenerGroup.iContinuarGroup( alSolicitudes.get( posicion ), posicion );
            }
        }
    };

    public void setContinuar( View.OnClickListener continuar ) {
        this.continuar = continuar;
    }

    public void setDatosSolicitud( Context context, ExpandableGroup datos ){

        if ( datos instanceof Title ) {

            tvMontoSolicitud.setText( ((Title) datos).getTvMonto() );

            if ( ( ( Title ) datos ).getTvEstado().equals( "detalles" ) ){
                cardViewMonto.setCardBackgroundColor( ContextCompat.getColor( context, R.color.soylucasverde_dos85Opacity ) );
                tvEstado.setText( datos.getTitle() );
                btnContinuar.setTextColor( ContextCompat.getColor( context, R.color.soylucasverde_dos ) );
                btnContinuar.setText("detalles");
                //tvMontoSolicitud.setText( ((Title) datos).getTvMonto() );
                ibtnEliminar.setVisibility( View.INVISIBLE );
                btnContinuar.setBackgroundColor( ContextCompat.getColor( context, R.color.soylucasverde_dos10Opacity ) );
                tvCintaItemCredito.setBackgroundColor( ContextCompat.getColor( context, R.color.soylucasverde_dos85Opacity ) );

            }
            else {
                cardViewMonto.setCardBackgroundColor( ContextCompat.getColor( context, R.color.soylucasamarillo_doso85Opacity ) );
                tvEstado.setText( datos.getTitle() );
                btnContinuar.setTextColor(  ContextCompat.getColor( context, R.color.soylucasamarillo_dos ) );
                btnContinuar.setBackgroundColor( ContextCompat.getColor( context, R.color.soylucasamarillo_dos10Opacity ) );
                btnContinuar.setText( ( ( Title ) datos ).getTvEstado() );
                ibtnEliminar.setImageResource( R.drawable.ic_delete_amarillo_24px );
                //tvMontoSolicitud.setText( ((Title) datos).getTvMonto() );
                tvCintaItemCredito.setBackgroundColor( ContextCompat.getColor( context, R.color.soylucasamarillo_doso85Opacity ) );
            }
        }
    }

    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate = new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        ivList_Item_Detalle_Arrow.setAnimation(rotate);

        lyItemBandejaCreditos.setBackgroundColor( ContextCompat.getColor( appContext, R.color.White ) );
        tvEstado.setTextColor( ContextCompat.getColor( appContext, R.color.Black55Opacity ) );
        tvCreditoOnline.setTextColor( ContextCompat.getColor( appContext, R.color.Black85Opacity ) );

        ivList_Item_Detalle_Arrow.setImageResource( R.drawable.boton_circular );

        if( btnContinuar.getText().equals( "detalles" ) ) {
            btnContinuar.setBackgroundColor( ContextCompat.getColor( appContext, R.color.soylucasverde_dos10Opacity ) );
            cardViewMonto.setCardBackgroundColor( ContextCompat.getColor( appContext, R.color.soylucasverde_dos85Opacity ) );
        }
        else{
            btnContinuar.setBackgroundColor( ContextCompat.getColor( appContext, R.color.soylucasamarillo_dos10Opacity ) );
            ibtnEliminar.setImageResource( R.drawable.ic_delete_amarillo_24px );
            cardViewMonto.setCardBackgroundColor( ContextCompat.getColor( appContext, R.color.soylucasamarillo_doso85Opacity ) );
        }

    }

    private void animateCollapse() {
        RotateAnimation rotate = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        ivList_Item_Detalle_Arrow.setAnimation( rotate );

        cardViewMonto.setCardBackgroundColor( ContextCompat.getColor( appContext, R.color.White25Opacity ) );
        tvCreditoOnline.setTextColor( ContextCompat.getColor( appContext, R.color.White ) );
        tvEstado.setTextColor( ContextCompat.getColor( appContext, R.color.White85Opacity ) );
        btnContinuar.setBackgroundColor( ContextCompat.getColor( appContext, R.color.White ) );

        if( btnContinuar.getText().equals( "detalles" ) ) {
            lyItemBandejaCreditos.setBackgroundColor( ContextCompat.getColor( appContext, R.color.soylucasverde ) );
            ivList_Item_Detalle_Arrow.setImageResource( R.drawable.boton_circular_desplegado_verde );
        }
        else {
            lyItemBandejaCreditos.setBackgroundColor(ContextCompat.getColor( appContext, R.color.soylucasamarillo));
            ibtnEliminar.setImageResource( R.drawable.ic_delete_blanco_24px );
            ivList_Item_Detalle_Arrow.setImageResource( R.drawable.boton_circular_desplegado );
        }

    }


}
