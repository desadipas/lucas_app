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
import com.tibox.lucas.adaptadores.listview.AdaptadorLvCalendario;
import com.tibox.lucas.network.dto.CalendarioDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabCalendario extends Fragment {

    private List<CalendarioDTO> m_Listcalendario;
    private AdaptadorLvCalendario m_AdaptadorTabCalendario;
    private String m_dato;

    public static TabCalendario newsInstance( String dato, List<CalendarioDTO> Listcalendario  ){
        TabCalendario f = new TabCalendario();
        Bundle args = new Bundle();
        args.putString( "dato", dato );
        args.putParcelableArrayList( "ListCalendario", (ArrayList<? extends Parcelable>) Listcalendario);
        f.setArguments( args );
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_Listcalendario = new ArrayList<>();
        m_Listcalendario = getArguments().getParcelableArrayList( "ListCalendario" );
        m_dato = getArguments().getString("dato");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lvCalendario = (ListView) view.findViewById( R.id.lv_calendario_credito );

        if ( m_Listcalendario != null ){
            m_AdaptadorTabCalendario = new AdaptadorLvCalendario( getActivity(), R.layout.activity_consulta_credito, m_Listcalendario );
            lvCalendario.setAdapter( m_AdaptadorTabCalendario );
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.tab_calendario, container, false );
        return rootView;
    }

}
