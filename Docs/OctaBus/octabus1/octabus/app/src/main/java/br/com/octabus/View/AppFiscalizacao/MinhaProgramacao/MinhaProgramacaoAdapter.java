package br.com.octabus.View.AppFiscalizacao.MinhaProgramacao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.octabus.R;

/**
 * Created by marcatti on 18/01/17.
 */

public class MinhaProgramacaoAdapter extends BaseAdapter {
    private Context context;
    private List<JSONObject> listaItem;

    public MinhaProgramacaoAdapter(Context context, List<JSONObject> listaItem) {
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
                view = inflater.inflate(R.layout.card_minha_programacao, parent, false);
            }
            else
            {
                view = convertView;
            }

            EditText inputLinha = (EditText) view.findViewById(R.id.inputLinha);
            EditText inputTabela = (EditText) view.findViewById(R.id.inputTabela);
            EditText inputSentidoBairro = (EditText) view.findViewById(R.id.inputSentidoBairro);
            EditText inputSentidoCentro = (EditText) view.findViewById(R.id.inputSentidoCentro);
            RadioButton radioTP = (RadioButton) view.findViewById(R.id.radioTP);
            RadioButton radioTS = (RadioButton) view.findViewById(R.id.radioTS);

            if(item.getInt("marcacaoTP") == 1)
            {
                inputLinha.setText(item.getString("descricaoLinhaTP"));
                radioTP.setChecked(true);
            }
            else if(item.getInt("marcacaoTS") == 1)
            {
                inputLinha.setText(item.getString("descricaoLinhaTS"));
                radioTS.setChecked(true);
            }

            inputSentidoBairro.setText(item.getString("descricaoLinhaTP"));
            inputSentidoCentro.setText(item.getString("descricaoLinhaTS"));
            inputTabela.setText(item.getString("tipoTabela"));

            return view;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}