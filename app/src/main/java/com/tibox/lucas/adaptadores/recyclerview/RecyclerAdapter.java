package com.tibox.lucas.adaptadores.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.tibox.lucas.R;
import com.tibox.lucas.adaptadores.recyclerview.view.SubTitleViewHolder;
import com.tibox.lucas.adaptadores.recyclerview.view.TitleViewHolder;
import com.tibox.lucas.entidad.BandejaCreditos;
import com.tibox.lucas.entidad.expandablegroup.SubTitle;
import com.tibox.lucas.entidad.expandablegroup.Title;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by desa02 on 11/09/2017.
 */

public class RecyclerAdapter extends ExpandableRecyclerViewAdapter<TitleViewHolder, SubTitleViewHolder> implements TitleViewHolder.RVListenerGroup{

    private ArrayList<BandejaCreditos> alSolicitudes;
    private Context m_context;
    private RVListener m_RVListener;

    @Override
    public void ieliminarCitaGroup(BandejaCreditos datos, int position) {
        if ( m_RVListener != null ){
            m_RVListener.iAnularFlujo( datos, position );
        }
        //Toast.makeText( m_context, "eliminar RV..."+ datos.getcFechaReg(), Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void iContinuarGroup(BandejaCreditos datos, int position) {
        if ( m_RVListener != null ){
            m_RVListener.iContinuarFlujo( datos, position );
        }
        //Toast.makeText( m_context, "continuar RV..."+ datos.getcFechaReg(), Toast.LENGTH_SHORT ).show();
    }

    public interface RVListener{
        void iAnularFlujo( BandejaCreditos datos, int position );
        void iContinuarFlujo( BandejaCreditos bandejaCreditos, int position );
    }

    public RecyclerAdapter( Context context, ArrayList<BandejaCreditos> solicitudes, List<? extends ExpandableGroup> groups, RVListener listener ) {
        super(groups);
        m_context = context;
        alSolicitudes = solicitudes;
        m_RVListener = listener;
    }

    @Override
    public TitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.item_recyclerview_solicitud, parent, false);
        return new TitleViewHolder( view, m_context, alSolicitudes, RecyclerAdapter.this );

    }

    @Override
    public SubTitleViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.list_item_detalle, parent, false );
        return new SubTitleViewHolder(view);

    }

    @Override
    public void onBindChildViewHolder(SubTitleViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {
        final SubTitle subTitle = ((Title) group).getItems().get(childIndex);
        holder.setSubTitletDatos( subTitle.getName(), subTitle.getSubname() , subTitle.getImage() );


    }

    @Override
    public void onBindGroupViewHolder(final TitleViewHolder holder, int flatPosition, ExpandableGroup group ) {
        holder.setDatosSolicitud( m_context, group );

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }
}
