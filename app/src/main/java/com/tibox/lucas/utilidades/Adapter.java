package com.tibox.lucas.utilidades;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by apple on 25/10/16.
 */
public abstract class Adapter extends BaseAdapter{

    private final List<?> mList;
    private final int mResource;
    private final Context mContext;
    private SparseBooleanArray mSelectedItemsIds;

    /**
     *
     * @param context
     *            Activity
     * @param resource
     *            layout (item)
     * @param list
     *            datos
     *
     */
    public Adapter(Context context, int resource, List<?> list) {
        super();
        this.mContext = context;
        this.mResource = resource;
        this.mList = list;
        this.mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(this.mResource, parent, false);
        }
        create(getItem(position), view);
        return view;
    }

    @Override
    public int getCount() {
        return this.mList != null ? this.mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void remove(Object item) {
        this.mList.remove(item);
        this.notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        selectView(position, !this.mSelectedItemsIds.get(position));
    }

    public void singleSelection(int position) {
        if (!this.mSelectedItemsIds.get(position))
            selectView(position, true);
    }

    public void removeSelection() {
        this.mSelectedItemsIds = new SparseBooleanArray();
        this.notifyDataSetChanged();
    }

    private void selectView(int position, boolean value) {
        if (value)
            this.mSelectedItemsIds.put(position, value);
        else
            this.mSelectedItemsIds.delete(position);
        this.notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return this.mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return this.mSelectedItemsIds;
    }

    public abstract void create(Object item, View view);
}
