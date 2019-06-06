package br.com.octabus.View.Util.PesquisaFuncionario;

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

import br.com.octabus.Constantes;
import br.com.octabus.R;

public class PesquisaFuncionarioActivity extends AppCompatActivity {

    TextInputLayout inputLayoutFuncionario;

    EditText inputFuncionario;
    Button buttonBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_funcionario);

        setTitle(getResources().getString(R.string.tituloPesquisaFuncionario));

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

        inputLayoutFuncionario = (TextInputLayout) findViewById(R.id.inputLayoutFuncionario);
        inputFuncionario = (EditText) findViewById(R.id.inputFuncionario);

        inputFuncionario.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    pesquisarFuncionario();
                    return true;
                }
                return false;
            }
        });

        buttonBuscar = (Button) findViewById(R.id.buttonBuscar);
        buttonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pesquisarFuncionario();
            }
        });
    }

    private void pesquisarFuncionario()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        String funcionario = inputFuncionario.getText().toString().trim();

        if(funcionario.isEmpty())
        {
            inputLayoutFuncionario.setError(getString(R.string.erroParametro));
        }
        else
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.aguarde));
            dialog.setCancelable(false);

            Request request = Request.create(Constantes.urlPesquisarFuncionario);
            request.setMethod("POST")
                    .setTimeout(60)
                    .addParameter("tokenLoginService", Constantes.keyApp)
                    .addParameter("query", funcionario)

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
                                Adapter opcaoAdapter = new PesquisaFuncionarioAdapter(getApplicationContext(), lista);
                                final ListView listviewItem = (ListView) findViewById(R.id.listaFuncionario);
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
                                            intent.putExtra("nomeFuncionario", item.getString("nome"));
                                            intent.putExtra("idFuncionario", item.getString("idFuncionario"));
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
