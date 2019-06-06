package br.com.octabus.View.AppFiscalizacao.Servicos.InformacaoPonto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.com.octabus.R;

/**
 * Created by marcatti on 18/01/17.
 */

public class InformacaoPontoItemAdapter extends BaseAdapter {
    private Context context;
    private List<Map> listaItem;

    public InformacaoPontoItemAdapter(Context context, List<Map> listaItem) {
        this.context = context;
        this.listaItem = listaItem;
    }

    public int getCount() {
        return listaItem.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        final Map item = listaItem.get(position);
        final View view;

        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adapter_infracao_item, parent, false);
        }
        else
        {
            view = convertView;
        }

        TextView tituloOpcao = (TextView) view.findViewById(R.id.tituloOpcao);
        tituloOpcao.setText(item.get("titulo").toString());

        ImageView imagemOpcao = (ImageView) view.findViewById(R.id.imagemOpcao);
        imagemOpcao.setImageResource((Integer) item.get("imagem"));

        return view;
    }

}