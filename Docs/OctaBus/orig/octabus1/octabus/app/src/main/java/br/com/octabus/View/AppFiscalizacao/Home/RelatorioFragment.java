package br.com.octabus.View.AppFiscalizacao.Home;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.octabus.R;

/**
 * Created by marcatti on 15/01/17.
 */

public class RelatorioFragment extends Fragment {

    View view;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frament_home_servico, container, false);
        context = inflater.getContext();

        TypedArray imagem = getResources().obtainTypedArray(R.array.homeRelatorioImagens);
        String[] titulo = getResources().getStringArray(R.array.homeRelatorioTitulos);

        Map<String, Object> itemMap;
        List<Map> lista = new ArrayList<Map>();

        for(int i = 0; i < titulo.length; i++ )
        {
            itemMap = new HashMap<String, Object>();
            itemMap.put("titulo" , titulo[i]);
            itemMap.put("imagem" , imagem.getResourceId(i, -1));
            lista.add(itemMap);
        }

        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new HomeItemAdapter(context, lista, "relatorio"));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position)
                {
                    case 0:
//                        Intent intent = new Intent(getActivity(), InfracaoActivity.class);
//                        startActivity(intent);

                        break;
                }
            }
        });

        return view;
    }
}