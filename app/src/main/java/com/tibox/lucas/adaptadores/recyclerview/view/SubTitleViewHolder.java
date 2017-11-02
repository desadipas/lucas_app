package com.tibox.lucas.adaptadores.recyclerview.view;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.tibox.lucas.R;

/**
 * Created by desa02 on 11/09/2017.
 */

public class SubTitleViewHolder  extends ChildViewHolder {

    private TextView tvTituloDetalle;
    private TextView tvSubTituloDetalle;
    private ImageView ivIconoDetalle;

    public SubTitleViewHolder(View itemView) {
        super(itemView);
        tvTituloDetalle = (TextView) itemView.findViewById( R.id.tvTituloDetalle );
        tvSubTituloDetalle = (TextView) itemView.findViewById( R.id.tvSubTituloDetalle );
        ivIconoDetalle = (ImageView) itemView.findViewById( R.id.ivIconoDetalle );
    }

    public void setSubTitletDatos( String titulo, String subtitulo, Drawable image ) {
        tvTituloDetalle.setText( titulo );
        tvSubTituloDetalle.setText( subtitulo );
        ivIconoDetalle.setImageDrawable( image );
    }

}
