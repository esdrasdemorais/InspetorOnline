package br.com.octabus.View.Util.PesquisaInfracao;

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

public class PesquisaInfracaoAdapter extends BaseAdapter {
    private Context context;
    private List<JSONObject> listaItem;

    public PesquisaInfracaoAdapter(Context context, List<JSONObject> listaItem) {
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
                view = inflater.inflate(R.layout.adapter_pesquisa_infracao, parent, false);
            }
            else
            {
                view = convertView;
            }

            TextView txtCodigo = (TextView) view.findViewById(R.id.txtCodigo);
            TextView txtDescricao = (TextView) view.findViewById(R.id.txtDescricao);
            TextView txtNivel = (TextView) view.findViewById(R.id.txtNivel);
            TextView txtComplemento = (TextView) view.findViewById(R.id.txtComplemento);

            txtCodigo.setText(item.getString("codigoInfracao"));
            txtDescricao.setText(item.getString("descricaoInfracao"));
            txtNivel.setText(item.getString("nivelInfracao"));
            txtComplemento.setText(item.getString("complementoInfracao"));

            return view;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
