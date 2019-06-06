package br.com.octabus.View.Util.PesquisaCarro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.HashMap;

import br.com.octabus.Constantes;
import br.com.octabus.R;
import br.com.octabus.Util.SessionManager;

public class PesquisaCarroActivity extends AppCompatActivity {

    TextInputLayout inputLayoutPrefixoCarro;

    EditText inputPrefixoCarro;
    Button buttonBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_carro);
        setTitle(getResources().getString(R.string.tituloPesquisaCarro));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        inicializaUi();
    }

    public void inicializaUi()
    {

        inputLayoutPrefixoCarro = (TextInputLayout) findViewById(R.id.inputLayoutPrefixoCarro);
        inputPrefixoCarro = (EditText) findViewById(R.id.inputPrefixoCarro);

        inputPrefixoCarro.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    pesquisarLinhaPorPrefixo();
                    return true;
                }
                return false;
            }
        });

        buttonBuscar = (Button) findViewById(R.id.buttonBuscar);
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisarLinhaPorPrefixo();
            }
        });
    }

    private void pesquisarLinhaPorPrefixo()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String prefixoCarro = inputPrefixoCarro.getText().toString().trim();

        if(prefixoCarro.isEmpty())
        {
            inputLayoutPrefixoCarro.setError(getString(R.string.erroParametro));
        }
        else
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.aguarde));
            dialog.setCancelable(false);

            SessionManager sessionManager = new SessionManager(getApplicationContext());
            HashMap _dataUser = sessionManager.getUserDetails();
            sessionManager = null;

            Request request = Request.create(Constantes.urlProgramacaoFuncionario);
            request.setMethod("POST")
            .setTimeout(60)
            .addParameter("tokenLoginService", Constantes.keyApp)
            .addParameter("fk_idFuncionario", (int) _dataUser.get("idFuncionario"))
            .addParameter("filtroCarro", prefixoCarro)
            .addParameter("origem", "infracao")

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
                        final JSONArray response = jsonObject.getJSONArray("response");

                        ArrayList<JSONObject> lista = new ArrayList<JSONObject>();

                        if (response != null) {
                            for (int i=0;i< response.length();i++){
                                lista.add((JSONObject) response.get(i));
                            }
                        }

                        /* ADAPTER */
                        Adapter opcaoAdapter = new PesquisaCarroAdapter(getApplicationContext(), lista);
                        final ListView listviewItem = (ListView) findViewById(R.id.listaCarro);
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
                                    intent.putExtra("idHorario", item.getString("idHorario"));
                                    intent.putExtra("descricaoLinha", item.getString("descricaoLinha"));
                                    intent.putExtra("nomeMotorista", item.getString("nomeMotorista"));
                                    intent.putExtra("nomeCobrador", item.getString("nomeCobrador"));
                                    intent.putExtra("prefixoVeiculo", item.getString("prefixoVeiculo"));
                                    intent.putExtra("sentidoTP", item.getString("sentidoTP"));
                                    intent.putExtra("sentidoTS", item.getString("sentidoTS"));
                                    setResult(Activity.RESULT_OK, intent);
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
                        Snackbar.make(findViewById(R.id.currentView), jsonObject.getString("response"), Snackbar.LENGTH_LONG).show();
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
