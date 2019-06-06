package br.com.octabus.View.AppFiscalizacao.MinhaProgramacao;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Adapter;
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
import br.com.octabus.Util.GPSTracker;
import br.com.octabus.Util.Network;
import br.com.octabus.Util.SessionManager;
import br.com.octabus.View.AppFiscalizacao.Home.HomeActivity;
import br.com.octabus.View.Login.LoginActivity;

public class MinhaProgramacaoActivity extends AppCompatActivity{
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_programacao);

        setTitle(getResources().getString(R.string.tituloProgramacao));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        if(!Network.isOnline(context))
        {
            Snackbar.make(findViewById(R.id.currentView), getString(R.string.semConexao), Snackbar.LENGTH_LONG).show();
        }
        else
        {
            SessionManager sessionManager = new SessionManager(getApplicationContext());
            Boolean usuarioLogado = sessionManager.isLoggedIn();
            sessionManager = null;

            if (usuarioLogado)
            {
                sessionManager = new SessionManager(getApplicationContext());
                HashMap _dataUser = sessionManager.getUserDetails();
                sessionManager = null;

                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage(getString(R.string.aguarde));
                dialog.setCancelable(false);

                Request request = Request.create(Constantes.urlProgramacaoFuncionario);
                request.setMethod("POST")
                .setTimeout(60)
                .addParameter("tokenLoginService", Constantes.keyApp)
                .addParameter("fk_idFuncionario", (int) _dataUser.get("idFuncionario"))

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
                            JSONArray response = jsonObject.getJSONArray("rows");

                            ArrayList<JSONObject> listaProgramacao = new ArrayList<JSONObject>();

                            if (response != null) {
                                for (int i=0;i< response.length();i++){
                                    listaProgramacao.add((JSONObject) response.get(i));
                                }
                            }

                            Adapter opcaoAdapter = new MinhaProgramacaoAdapter(context, listaProgramacao);
                            final ListView adapterListView = (ListView) findViewById(R.id.listagemMarcacao);
                            adapterListView.setAdapter((ListAdapter) opcaoAdapter);
                        }
                        else
                        {
                            Snackbar.make(findViewById(R.id.currentView), jsonObject.getString("response"), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(JSONObject jsonObject) throws JSONException {
                        Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroRecuperarListagemRotina), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onParseError(JSONException e) {
                        Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
                        e.printStackTrace();

                    }
                }).execute();
            }
            else
            {
                Intent intent = new Intent(MinhaProgramacaoActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.animator.animation_translate_left_in, R.animator.animation_translate_left_out);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_minha_programacao, menu);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap _dataUser = sessionManager.getUserDetails();
        sessionManager = null;

        if((Boolean) _dataUser.get("localConfirmado"))
        {
            MenuItem item = menu.findItem(R.id.btnConfirmar);
            item.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnLogout:
                deslogar();
                break;
            case R.id.btnConfirmar:

                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.confirmarProgramacao();
                sessionManager = null;

                Intent intent = new Intent(MinhaProgramacaoActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();

                break;
        }
        return true;
    }

    public void deslogar()
    {
        final GPSTracker gps = new GPSTracker(context);

        if(!Network.isOnline(context))
        {
            Snackbar.make(findViewById(R.id.currentView), getString(R.string.semConexao), Snackbar.LENGTH_LONG).show();
        }
        else
        {
            if(!gps.canGetLocation())
            {
                gps.showSettingsAlert();
            }
            else
            {
                Double latitude = gps.getLatitude();
                Double longitude = gps.getLongitude();

                SessionManager sessionManager = new SessionManager(context);
                HashMap _dataUser = sessionManager.getUserDetails();
                sessionManager = null;

                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage(getString(R.string.aguarde));
                dialog.setCancelable(false);

                Request request = Request.create(Constantes.urlDeslogar);
                request.setMethod("POST")
                        .setTimeout(60) //2 Minutes
                        .addParameter("tokenLoginService", Constantes.keyApp)
                        .addParameter("idLogin", (int) _dataUser.get("idLogin"))
                        .addParameter("latitude", latitude)
                        .addParameter("longitude", longitude)
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
                                    SessionManager sessionManager = new SessionManager(context);
                                    sessionManager.logoutUser();
                                    sessionManager = null;

                                    Intent intent = new Intent(MinhaProgramacaoActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Snackbar.make(findViewById(R.id.currentView), jsonObject.getString("response"), Snackbar.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onErrorResponse(JSONObject jsonObject) throws JSONException {
                                Snackbar.make(findViewById(R.id.currentView), getString(R.string.naoFoiPossivelDeslogar), Snackbar.LENGTH_LONG).show();
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
}
