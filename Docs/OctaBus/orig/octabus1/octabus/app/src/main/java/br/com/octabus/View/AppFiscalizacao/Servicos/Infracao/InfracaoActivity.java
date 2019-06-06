package br.com.octabus.View.AppFiscalizacao.Servicos.Infracao;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.octabus.R;
import br.com.octabus.View.AppFiscalizacao.Home.HomeActivity;
import br.com.octabus.View.AppFiscalizacao.Servicos.InfracaoComunicacao.InfracaoComunicacaoActivity;

public class InfracaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infracao);

        setTitle(getResources().getString(R.string.tituloInfracao));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfracaoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        inicializaUi();
    }

    public void inicializaUi()
    {
        TypedArray imagem = getResources().obtainTypedArray(R.array.imagemServicoInfracao);
        String[] titulo = getResources().getStringArray(R.array.tituloServicoInfracao);

        Map<String, Object> itemMap;
        List<Map> lista = new ArrayList<Map>();

        for(int i = 0; i < titulo.length; i++ )
        {
            itemMap = new HashMap<String, Object>();
            itemMap.put("titulo" , titulo[i]);
            itemMap.put("imagem" , imagem.getResourceId(i, -1));
            lista.add(itemMap);
        }

        /* ADAPTER */
        Adapter opcaoAdapter = new InfracaoItemAdapter(getApplicationContext(), lista);
        final ListView listviewItem = (ListView) findViewById(R.id.listaOpcao);
        listviewItem.setAdapter((ListAdapter) opcaoAdapter);
        /* ADAPTER */
        /* EVENTO ONCLICK */
        listviewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                if(position == 0)
                {
                    Intent intent = new Intent(InfracaoActivity.this, InfracaoComunicacaoActivity.class);
                    startActivity(intent);
                }
                else if(position == 1)
                {
//                    Intent intent = new Intent(InfracaoActivity.this, InfracaoComunicacaoActivity.class);
//                    startActivity(intent);
                }
            }
        });
    }
}
