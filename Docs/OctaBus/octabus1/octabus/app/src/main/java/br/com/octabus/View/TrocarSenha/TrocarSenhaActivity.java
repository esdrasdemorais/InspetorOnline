package br.com.octabus.View.TrocarSenha;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.octabus.Constantes;
import br.com.octabus.R;
import br.com.octabus.View.Login.LoginActivity;

public class TrocarSenhaActivity extends AppCompatActivity {

    TextInputLayout inputLayoutRe, inputLayoutSenha, inputLayoutConfirmarSenha;
    EditText inputRe,inputConfirmarSenha, inputSenha;
    Button buttonTrocarSenha;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trocar_senha);

        context = getApplicationContext();

        setTitle(getResources().getString(R.string.tituloTrocarSenha));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        inputLayoutRe = (TextInputLayout) findViewById(R.id.inputLayoutRe);
        inputLayoutSenha = (TextInputLayout) findViewById(R.id.inputLayoutSenha);
        inputLayoutConfirmarSenha = (TextInputLayout) findViewById(R.id.inputLayoutConfirmarSenha);

        inputRe = (EditText) findViewById(R.id.inputRe);
        inputSenha = (EditText) findViewById(R.id.inputSenha);
        inputConfirmarSenha = (EditText) findViewById(R.id.inputConfirmarSenha);
        buttonTrocarSenha = (Button) findViewById(R.id.buttonTrocarSenha);

        inputRe.setText(bundle.getString("re"));

        buttonTrocarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String login = inputRe.getText().toString().trim();
                final String senha = inputSenha.getText().toString().trim();
                final String senhaConfirmada = inputConfirmarSenha.getText().toString().trim();

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

                        if (senhaConfirmada.isEmpty())
                        {
                            inputLayoutConfirmarSenha.setError(getString(R.string.erroParametroSenha));

                            if (inputConfirmarSenha.requestFocus())
                            {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            }
                        }
                        else
                        {
                            inputLayoutSenha.setError(null);

                            if(!senhaConfirmada.equals(senha))
                            {
                                inputLayoutConfirmarSenha.setError(getString(R.string.senhasDiferentes));
                            }
                            else
                            {
                                atualizarSenha();
                            }
                        }
                    }
                }
            }
        });
    }

    public void atualizarSenha()
    {
        final String login = inputRe.getText().toString().trim();
        final String senhaConfirmada = inputConfirmarSenha.getText().toString().trim();

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.aguarde));
        dialog.setCancelable(false);

        final AlertDialog.Builder adb = new AlertDialog.Builder(this);

        Request request = Request.create(Constantes.urlAlteracaoSenha);
        request.setMethod("POST")
                .setTimeout(60)
                .addParameter("tokenLoginService", Constantes.keyApp)
                .addParameter("re", login)
                .addParameter("novaSenha", senhaConfirmada)

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

                            adb.setTitle("Mensagem");
                            adb.setMessage(getResources().getString(R.string.senhaAtualizada));
                            adb.setIcon(android.R.drawable.ic_dialog_alert);
                            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(TrocarSenhaActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.animator.animation_translate_left_in, R.animator.animation_translate_left_out);
                                    finish();
                                } });

                            adb.show();
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
