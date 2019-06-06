package br.com.octabus.View.Util.PesquisaFuncionario;

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
 * Created by marcatti on 19/04/17.
 */

public class PesquisaFuncionarioAdapter   extends BaseAdapter {
    private Context context;
    private List<JSONObject> listaItem;

    public PesquisaFuncionarioAdapter(Context context, List<JSONObject> listaItem) {
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
        try {
            final JSONObject item = listaItem.get(position);
            final View view;

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                view = inflater.inflate(R.layout.adapter_pesquisa_funcionario, parent, false);
            } else {
                view = convertView;
            }

            TextView txtNome = (TextView) view.findViewById(R.id.txtNome);

            txtNome.setText(item.getString("nome"));

            return view;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
