package com.tibox.lucas.entidad.expandablegroup;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by desa02 on 12/09/2017.
 */

public class Title extends ExpandableGroup<SubTitle> {
    private String imageUrl;
    private String tvEstado;
    private String tvMonto;

    public Title( String title, List<SubTitle> items, String imageUrl, String tvEstado, String tvMonto ) {
        super( title, items );
        this.imageUrl = imageUrl;
        this.tvEstado = tvEstado;
        this.tvMonto = tvMonto;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTvEstado() {
        return tvEstado;
    }

    public String getTvMonto() {
        return tvMonto;
    }
}
