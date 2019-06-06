package br.com.octabus.View.AppFiscalizacao.Home;

import android.content.Context;
import android.content.Intent;
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
import br.com.octabus.View.AppFiscalizacao.Servicos.Checklist.ChecklistActivity;
import br.com.octabus.View.AppFiscalizacao.Servicos.Evento.EventoActivity;
import br.com.octabus.View.AppFiscalizacao.Servicos.InformacaoPonto.InformacaoPontoActivity;
import br.com.octabus.View.AppFiscalizacao.Servicos.Infracao.InfracaoActivity;

/**
 * Created by marcatti on 15/01/17.
 */

public class ServicoFragment extends Fragment {

    View view;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frament_home_servico, container, false);
        context = inflater.getContext();

        TypedArray imagem = getResources().obtainTypedArray(R.array.homeServicosImagens);
        String[] titulo = getResources().getStringArray(R.array.homeServicosTitulos);

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
        gridview.setAdapter(new HomeItemAdapter(context, lista, "servico"));

        final Intent[] intent = new Intent[1];

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                switch (position)
                {
                    case 0:
                        intent[0] = new Intent(getActivity(), InfracaoActivity.class);
                        startActivity(intent[0]);

                        break;
                    case 1:

                        intent[0] = new Intent(getActivity(), ChecklistActivity.class);
                        startActivity(intent[0]);
                        break;
                    case 7:

                        intent[0] = new Intent(getActivity(), EventoActivity.class);
                        startActivity(intent[0]);
                        break;
                    case 8:

                        intent[0] = new Intent(getActivity(), InformacaoPontoActivity.class);
                        startActivity(intent[0]);
                        break;
                }
            }
        });

        return view;
    }
}