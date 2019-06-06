package br.com.octabus.View.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.com.octabus.Constantes;
import br.com.octabus.R;
import br.com.octabus.Util.GPSTracker;
import br.com.octabus.Util.Network;
import br.com.octabus.Util.SessionManager;
import br.com.octabus.View.AppFiscalizacao.MinhaProgramacao.MinhaProgramacaoActivity;
import br.com.octabus.View.RecuperarSenha.RecuperarSenhaActivity;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout inputLayoutRe, inputLayoutSenha;
    EditText inputRe,inputSenha;
    Button buttonEntrar;
    TextView textEsqueceuSenha;

    Context context;

    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        final GPSTracker gps = new GPSTracker(this);

        inputLayoutRe = (TextInputLayout) findViewById(R.id.inputLayoutRe);
        inputLayoutSenha = (TextInputLayout) findViewById(R.id.inputLayoutSenha);

        inputRe = (EditText) findViewById(R.id.inputRe);
        inputSenha = (EditText) findViewById(R.id.inputSenha);
        buttonEntrar = (Button) findViewById(R.id.buttonEntrar);

        textEsqueceuSenha = (TextView) findViewById(R.id.textViewEsqueceuSenha);

//        inputRe.setText("42046186850");
//        inputSenha.setText("123456");

        textEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Network.isOnline(context))
                {
                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.semConexao), Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    String login = inputRe.getText().toString().trim();

                    if (login.isEmpty())
                    {
                        inputLayoutRe.setError(getString(R.string.erroParametroRE));

                        if (inputRe.requestFocus())
                        {
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                    else
                    {
                        Intent intent = new Intent(LoginActivity.this, RecuperarSenhaActivity.class);
                        intent.putExtra("re", login);
                        startActivity(intent);
                        overridePendingTransition(R.animator.animation_translate_left_in, R.animator.animation_translate_left_out);
                    }

                }
            }
        });

        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!Network.isOnline(context))
                {
                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.semConexao), Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    gps.getLocation();

                    if(!gps.canGetLocation())
                    {
                        gps.showSettingsAlert();
                    }
                    else
                    {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        validarLogin();
                    }
                }
            }
        });


    }

    private void validarLogin()
    {
        try
        {
            final String login = inputRe.getText().toString().trim();
            final String senha = inputSenha.getText().toString().trim();

            if (login.isEmpty())
            {
                inputLayoutRe.setError(getString(R.string.erroParametroRE));

                if (inputRe.requestFocus())
                {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
            else
            {
                inputLayoutRe.setError(null);

                if (senha.isEmpty())
                {
                    inputLayoutSenha.setError(getString(R.string.erroParametroSenha));

                    if (inputSenha.requestFocus())
                    {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
                else
                {
                    inputLayoutSenha.setError(null);

                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage(getString(R.string.aguarde));
                    dialog.setCancelable(false);

                    Request request = Request.create(Constantes.urlLogin);
                    request.setMethod("POST")
                            .setTimeout(60) //2 Minutes
                            .addParameter("tokenLoginService", Constantes.keyApp)
                            .addParameter("re", login)
                            .addParameter("senha", senha)
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
                                        JSONObject response = jsonObject.getJSONObject("response");

                                        ArrayList<String> listaMenuPermitido = new ArrayList<String>();
                                        JSONArray jArray = (JSONArray) response.getJSONArray("menuPermitido");
                                        if (jArray != null) {
                                            for (int i=0;i<jArray.length();i++){
                                                listaMenuPermitido.add(jArray.getString(i));
                                            }
                                        }

                                        SessionManager sessionManager = new SessionManager(context);
                                        sessionManager.createLoginSession(response.getInt("idLogin"),login,response.getInt("idFuncionario") , response.getString("nomeFuncionario"), response.getInt("idGrupo"), response.getString("nomeGrupo"),listaMenuPermitido, response.getString("dataLogin"));
                                        sessionManager = null;

                                        Intent intent = new Intent(LoginActivity.this, MinhaProgramacaoActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.animator.animation_translate_left_in, R.animator.animation_translate_left_out);
                                        finish();
                                    }
                                    else
                                    {
                                        Snackbar.make(findViewById(R.id.currentView), jsonObject.getString("response"), Snackbar.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onErrorResponse(JSONObject jsonObject) throws JSONException {
                                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroUsuarioSenhaIncorreto), Snackbar.LENGTH_LONG).show();
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
