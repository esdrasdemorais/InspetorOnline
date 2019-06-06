package br.com.octabus.View.AppFiscalizacao.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import br.com.octabus.R;

/**
 * Created by marcatti on 15/01/17.
 */

public class HomeItemAdapter extends BaseAdapter {
    private Context context;
    private List<Map> listaItem;
    private String tipo = null;

    public HomeItemAdapter(Context context, List<Map> listaItem, String tipo) {
        this.context = context;
        this.listaItem = listaItem;
        this.tipo = tipo;
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

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adapter_home_item, parent, false);
        } else {
            view = convertView;
        }

        if(tipo.equals("servico"))
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 265));

        TextView tituloItem = (TextView) view.findViewById(R.id.tituloItem);
        tituloItem.setText(item.get("titulo").toString());

        ImageView imagemItem = (ImageView) view.findViewById(R.id.imagemItem);
        imagemItem.setImageResource((Integer) item.get("imagem"));

        return view;
    }
}