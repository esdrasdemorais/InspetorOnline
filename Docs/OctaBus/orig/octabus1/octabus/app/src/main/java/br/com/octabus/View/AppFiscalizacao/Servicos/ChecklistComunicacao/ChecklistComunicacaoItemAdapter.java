package br.com.octabus.View.AppFiscalizacao.Servicos.ChecklistComunicacao;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.octabus.R;

public class ChecklistComunicacaoItemAdapter extends BaseAdapter {
    private Context context;
    public List listaItem;
    public String complemento;
    public String checked;

    public ChecklistComunicacaoItemAdapter(Context context, List listaItem) {
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

    public List getList()
    {
        return listaItem;
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, final ViewGroup parent) {

        final JSONObject item = (JSONObject) listaItem.get(position);

        final View view;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.adapter_checklist_item, parent, false);
        } else {
            view = convertView;
        }

        try {
            ((JSONObject) listaItem.get(position)).put("complemento", "");
            ((JSONObject) listaItem.get(position)).put("checked", "0");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView tituloOpcao = (TextView) view.findViewById(R.id.tituloOpcao);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        //RadioButton radioButtonNao = (RadioButton) view.findViewById(R.id.radioButtonNao);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(v.getRootView().getContext());
                View promptsView = li.inflate(R.layout.prompt_check_list, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        v.getRootView().getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                    if(userInput.getText().length() > 0)
                                    {
                                        checkBox.setChecked(true);
                                        try {
                                            ((JSONObject) listaItem.get(position)).put("checked", "1");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    else
                                    {
                                        checkBox.setChecked(false);
                                        try {
                                            ((JSONObject) listaItem.get(position)).put("checked", "0");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    try {
                                        ((JSONObject) listaItem.get(position)).put("complemento", userInput.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();

                                        checkBox.setChecked(false);
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });
        try {
            tituloOpcao.setText(item.get("descricao").toString());

            if(Integer.parseInt(item.get("obrigatorio_id").toString()) == 1)
            {
                tituloOpcao.setTextColor(context.getResources().getColor(R.color.blue));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }
}
