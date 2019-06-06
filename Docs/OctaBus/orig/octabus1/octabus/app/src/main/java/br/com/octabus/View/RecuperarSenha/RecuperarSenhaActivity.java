
package br.com.octabus.View.RecuperarSenha;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import br.com.octabus.Constantes;
import br.com.octabus.R;
import br.com.octabus.View.TrocarSenha.TrocarSenhaActivity;

public class RecuperarSenhaActivity extends AppCompatActivity {

    String re, codigoRecuperacao;
    int count;
    CountDownTimer yourCountDownTimer;
    Boolean countDownTimerInitied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        Bundle bundle = getIntent().getExtras();
        re = bundle.getString("re");

        ImageView imageViewRefresh = (ImageView) findViewById(R.id.imageViewRefresh);

        gerarCodigo();

        imageViewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarCodigo();
            }
        });

        final TextView _tv = (TextView) findViewById( R.id.textViewContador );

        yourCountDownTimer = new CountDownTimer(600000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                _tv.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                _tv.setText(getResources().getString(R.string.codigoNaoValidado));
            }
        };
    }

    public void gerarCodigo()
    {
        if(re != null)
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.aguarde));
            dialog.setCancelable(false);

            Request request = Request.create(Constantes.urlCodigoRecuperacaoSenha);
            request.setMethod("POST")
                    .setTimeout(60)
                    .addParameter("tokenLoginService", Constantes.keyApp)
                    .addParameter("re", re)

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
                                EditText inputCodigo = (EditText) findViewById(R.id.inputCodigo);
                                codigoRecuperacao = jsonObject.get("response").toString();
                                inputCodigo.setText(codigoRecuperacao);

                                iniciarContador();
                                verificaCodigoRecuperacao();
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
        else
        {
            Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
        }

    }


    public void iniciarContador()
    {
        if(!countDownTimerInitied)
        {
            yourCountDownTimer.start();
            countDownTimerInitied = true;
        }
        else
        {
            yourCountDownTimer.onFinish();
            yourCountDownTimer.start();
        }
    }


    public void verificaCodigoRecuperacao()
    {
        Thread thread = new Thread(new Runnable() {
            public void run () {
                    SystemClock.sleep(10000);

                    Request request = Request.create(Constantes.urlVerificaCodigoRecuperacao);
                    request.setMethod("POST")
                            .setTimeout(60)
                            .addParameter("tokenLoginService", Constantes.keyApp)
                            .addParameter("codigoRecuperacao", getCodigoRecuperacao())

                            .setRequestStateListener(new RequestStateListener() {
                                @Override
                                public void onStart() {
                                }

                                @Override
                                public void onFinish() {

                                }

                                @Override
                                public void onConnectionError(Exception e) {

                                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.erroProcessamento), Snackbar.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            })
                            .setResponseListener(new JsonResponseListener() {
                                @Override
                                public void onOkResponse(JSONObject jsonObject) throws JSONException {

                                    if((Boolean) jsonObject.get("success"))
                                    {
                                        if((int) jsonObject.get("response") == 1)
                                        {
                                            Intent intent = new Intent(RecuperarSenhaActivity.this, TrocarSenhaActivity.class);
                                            intent.putExtra("codigoRecuperacao", getCodigoRecuperacao());
                                            intent.putExtra("re", re);
                                            startActivity(intent);
                                            overridePendingTransition(R.animator.animation_translate_left_in, R.animator.animation_translate_left_out);
                                        }
                                        else
                                        {
                                            verificaCodigoRecuperacao();
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
        });
        thread.start();
    }

    public String getCodigoRecuperacao()
    {
        return codigoRecuperacao;
    }
}
