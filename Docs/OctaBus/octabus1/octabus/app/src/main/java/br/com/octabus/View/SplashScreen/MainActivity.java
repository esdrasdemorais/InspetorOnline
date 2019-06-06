package br.com.octabus.View.SplashScreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.octabus.Constantes;
import br.com.octabus.R;
import br.com.octabus.Services.ServicoLocalizacao;
import br.com.octabus.Util.DateHelper;
import br.com.octabus.Util.GPSTracker;
import br.com.octabus.Util.Network;
import br.com.octabus.Util.PermissionApp;
import br.com.octabus.Util.SessionManager;
import br.com.octabus.View.AppFiscalizacao.Home.HomeActivity;
import br.com.octabus.View.Login.LoginActivity;
import br.com.octabus.View.AppFiscalizacao.MinhaProgramacao.MinhaProgramacaoActivity;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Boolean permission = PermissionApp.checkPermissionsManifest(this, this);

        if(!permission)
        {
            TextView textViewErroVersao = (TextView) findViewById(R.id.textViewErroVersao);
            textViewErroVersao.setText("Você não aplicou todas as permissões necessárias. Feche o aplicativo e tente novamente");
            textViewErroVersao.setVisibility(View.VISIBLE);
        }
        else
        {

//            Intent intent = new Intent(MainActivity.this, InfracaoComunicacaoActivity.class);
//            startActivity(intent);
//            finish();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    verificarVersao();
                }
            }, 2000);
        }

    }

    protected void verificarVersao()
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.aguardeVerificandoVersao));
        dialog.setCancelable(false);

        Request request = Request.create(Constantes.urlVerificacaoVersao);
        request.setMethod("POST")
                .setTimeout(60) //2 Minutes
                .addParameter("tokenLoginService", Constantes.keyApp)

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
                            if(!Constantes.versaoApp.equals(jsonObject.get("response")))
                            {
                                String text = String.format(getResources().getString(R.string.erroVersao), jsonObject.get("response"));

                                TextView textViewErroVersao = (TextView) findViewById(R.id.textViewErroVersao);
                                textViewErroVersao.setText(text);
                                textViewErroVersao.setVisibility(View.VISIBLE);
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

                                    if((int) DateHelper.diferenceHour((String) _dataUser.get("dataLogin"), DateHelper.getCurrentDate()) >= 8)
                                    {
                                        deslogar();
                                    }
                                    else
                                    {

                                        if(!(Boolean) _dataUser.get("localConfirmado"))
                                        {
                                            Intent intent = new Intent(MainActivity.this, MinhaProgramacaoActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                                else
                                {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.animator.animation_translate_left_in, R.animator.animation_translate_left_out);
                                    finish();
                                }
                            }
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


    public void deslogar()
    {
        final Context context = getApplicationContext();

        final GPSTracker gps = new GPSTracker(this);

        if(!Network.isOnline(context))
        {
            Snackbar.make(findViewById(R.id.currentView), getString(R.string.semConexao), Snackbar.LENGTH_LONG).show();
        }
        else
        {
            if(!gps.canGetLocation())
            {
                gps.showSettingsAlert();
                Snackbar.make(findViewById(R.id.currentView), getString(R.string.semConexaoGps), Snackbar.LENGTH_LONG).show();
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

                                    MainActivity.this.stopService(new Intent(MainActivity.this ,ServicoLocalizacao.class));

                                    Intent intent = new Intent(MainActivity.this , LoginActivity.class);
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
