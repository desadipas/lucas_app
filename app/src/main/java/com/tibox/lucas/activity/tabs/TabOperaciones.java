package com.tibox.lucas.activity.tabs;

/**
 * Created by desa02 on 21/06/2017.
 */
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import com.tibox.lucas.R;
import com.tibox.lucas.adaptadores.listview.AdaptadorLvOperaciones;
import com.tibox.lucas.network.dto.OperacionesDTO;

import java.util.ArrayList;
import java.util.List;

public class TabOperaciones extends Fragment {

    private List<OperacionesDTO> m_ListaOperaciones;
    private AdaptadorLvOperaciones m_AdaptadorTabOperaciones;
    private String m_dato;

    public static TabOperaciones newsInstance(String dato, List<OperacionesDTO> Listoperaciones ){
        TabOperaciones f = new TabOperaciones();
        Bundle args = new Bundle();
        args.putString( "dato", dato );
        args.putParcelableArrayList( "ListOperaciones", (ArrayList<? extends Parcelable>) Listoperaciones);
        f.setArguments( args );
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_ListaOperaciones = new ArrayList<>();
        m_ListaOperaciones = getArguments().getParcelableArrayList( "ListOperaciones" );
        m_dato = getArguments().getString("dato");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lvOperaciones = (ListView) view.findViewById( R.id.lv_operaciones_credito );

        if ( m_ListaOperaciones != null ){
            m_AdaptadorTabOperaciones = new AdaptadorLvOperaciones( getActivity(), R.layout.activity_consulta_credito, m_ListaOperaciones );
            lvOperaciones.setAdapter( m_AdaptadorTabOperaciones );
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.tab_operaciones, container, false );
        return rootView;
    }

}
