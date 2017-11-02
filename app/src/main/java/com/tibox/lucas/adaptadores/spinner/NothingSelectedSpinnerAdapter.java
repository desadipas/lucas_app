package com.tibox.lucas.adaptadores.spinner;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

/**
 * Created by desa02 on 17/05/2017.
 */

public class NothingSelectedSpinnerAdapter implements SpinnerAdapter, ListAdapter {

    protected static final int EXTRA = 1;
    protected SpinnerAdapter adapter;
    protected Context context;
    protected int nothingSelectedLayout;
    protected int nothingSelectedDropdownLayout;
    protected LayoutInflater layoutInflater;

    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter, int nothingSelectedLayout, Context context) {
        this (spinnerAdapter, nothingSelectedLayout, -1, context );
    }

    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter, int nothingSelectedLayout,
                                         int nothingSelectedDropdownLayout, Context context) {
        this.adapter = spinnerAdapter;
        this.context = context;
        this.nothingSelectedLayout = nothingSelectedLayout;
        this.nothingSelectedDropdownLayout = nothingSelectedDropdownLayout;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position == 0 ? false : true;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if ( position == 0 ) {
            return nothingSelectedDropdownLayout == -1 ? new View(context) : getNothingSelectedDropdownView(parent);
        }
        return adapter.getDropDownView(position - EXTRA, null, parent);
    }

    protected View getNothingSelectedDropdownView(ViewGroup parent) {
        return layoutInflater.inflate( nothingSelectedDropdownLayout, parent, false );
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        adapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        adapter.unregisterDataSetObserver(dataSetObserver);
    }

    @Override
    public int getCount() {
        int count = adapter.getCount();
        return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public Object getItem(int position) {
        return position == 0 ? null : adapter.getItem(position - EXTRA);
    }

    @Override
    public long getItemId(int position) {
        return adapter.getItemId(position - EXTRA);
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            return getNothingSelectedView(parent);
        }
        return adapter.getView( position - EXTRA, null, parent );
    }

    protected View getNothingSelectedView(ViewGroup parent) {
        return layoutInflater.inflate( nothingSelectedLayout, parent, false );
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? getViewTypeCount() - EXTRA : adapter.getItemViewType(position - EXTRA);
    }

    @Override
    public int getViewTypeCount() {
        return adapter.getViewTypeCount() + EXTRA;
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }
}
