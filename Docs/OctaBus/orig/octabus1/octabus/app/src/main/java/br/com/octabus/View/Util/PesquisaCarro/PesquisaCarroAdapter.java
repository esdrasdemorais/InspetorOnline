package br.com.octabus.View.Util.PesquisaCarro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.octabus.R;

/**
 * Created by marcatti on 30/03/17.
 */

public class PesquisaCarroAdapter  extends BaseAdapter {
    private Context context;
    private List<JSONObject> listaItem;

    public PesquisaCarroAdapter(Context context, List<JSONObject> listaItem) {
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
        try
        {
            final JSONObject item = listaItem.get(position);
            final View view;

            if(convertView == null)
            {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.adapter_pesquisa_carro, parent, false);
            }
            else
            {
                view = convertView;
            }

            TextView txtPrefixo = (TextView) view.findViewById(R.id.txtPrefixo);
            TextView txtLinha = (TextView) view.findViewById(R.id.txtLinha);
            TextView txtMotorista = (TextView) view.findViewById(R.id.txtMotorista);
            TextView txtCobrador = (TextView) view.findViewById(R.id.txtCobrador);

            txtPrefixo.setText(item.getString("prefixoVeiculo"));
            txtLinha.setText(item.getString("descricaoLinha"));
            txtMotorista.setText(item.getString("nomeMotorista"));
            txtCobrador.setText(item.getString("nomeCobrador"));

            return view;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
