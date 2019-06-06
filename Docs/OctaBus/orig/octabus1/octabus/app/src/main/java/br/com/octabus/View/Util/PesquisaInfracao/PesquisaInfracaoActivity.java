package br.com.octabus.View.Util.PesquisaInfracao;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.octabus.Constantes;
import br.com.octabus.R;

public class PesquisaInfracaoActivity extends AppCompatActivity {

    TextInputLayout inputLayoutPrefixoInfracao;

    EditText inputPrefixoInfracao;
    Button buttonBuscar;
    int fk_idTipoInfracao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_infracao);

        setTitle(getResources().getString(R.string.tituloPesquisaInfracao));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();

        if(bundle.getInt("fk_idTipoInfracao") != 0)
        {
            fk_idTipoInfracao = bundle.getInt("fk_idTipoInfracao");
        }

        inicializaUI();
    }


    public void inicializaUI()
    {

        inputLayoutPrefixoInfracao = (TextInputLayout) findViewById(R.id.inputLayoutPrefixoInfracao);
        inputPrefixoInfracao = (EditText) findViewById(R.id.inputPrefixoInfracao);

        buttonBuscar = (Button) findViewById(R.id.buttonBuscar);
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisarLinhaPorPrefixo(false);
            }
        });

        inputPrefixoInfracao.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    pesquisarLinhaPorPrefixo(false);
                    return true;
                }
                return false;
            }
        });

        pesquisarLinhaPorPrefixo(true);
    }

    private void pesquisarLinhaPorPrefixo(boolean inicializa)
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String filtroInfracao = inputPrefixoInfracao.getText().toString().trim();

        if(filtroInfracao.isEmpty() && !inicializa)
        {
            inputLayoutPrefixoInfracao.setError(getString(R.string.erroParametro));
        }
        else
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.aguarde));
            dialog.setCancelable(false);

            Request request = Request.create(Constantes.urlRecuperaInfracao);
            request.setMethod("POST")
            .setTimeout(60)
            .addParameter("tokenLoginService", Constantes.keyApp)
            .addParameter("filtroInfracao", filtroInfracao)
            .addParameter("fk_idTipoInfracao", fk_idTipoInfracao)

            .setRequestStateListener(new RequestStateListener() {
                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onFinish() {
                    dialog.dismiss();
                }

                @Override
                public void onConnectionError(Exception e) {
                    dialog.dismiss();

                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            })
            .setResponseListener(new JsonResponseListener() {
                @Override
                public void onOkResponse(JSONObject jsonObject) throws JSONException {

                    if((Boolean) jsonObject.get("success"))
                    {
                        final JSONArray response = jsonObject.getJSONArray("rows");

                        ArrayList<JSONObject> lista = new ArrayList<JSONObject>();

                        if (response != null) {
                            for (int i=0;i< response.length();i++){
                                lista.add((JSONObject) response.get(i));
                            }
                        }

                        /* ADAPTER */
                        Adapter opcaoAdapter = new PesquisaInfracaoAdapter(getApplicationContext(), lista);
                        final ListView listviewItem = (ListView) findViewById(R.id.listaInfracao);
                        listviewItem.setAdapter((ListAdapter) opcaoAdapter);
                        /* ADAPTER */
                        /* EVENTO ONCLICK */
                        listviewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                                try
                                {
                                    JSONObject item = (JSONObject) response.get(position);

                                    Intent intent = new Intent();
                                    intent.putExtra("idInfracao", item.getString("idInfracao"));
                                    intent.putExtra("codigoInfracao", item.getString("codigoInfracao"));
                                    intent.putExtra("descricaoInfracao", item.getString("codigoInfracao") +" - " + item.getString("descricaoInfracao"));
                                    intent.putExtra("complementoInfracao", item.getString("complementoInfracao"));
                                    intent.putExtra("nivelInfracao", item.getString("nivelInfracao"));
                                    setResult(RESULT_OK, intent);
                                    finish();



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        /* EVENTO ONCLICK */
                    }
                    else
                    {
                        Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onErrorResponse(JSONObject jsonObject) throws JSONException {
                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onParseError(JSONException e) {
                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();

                }
            }).execute();
        }
    }

}
