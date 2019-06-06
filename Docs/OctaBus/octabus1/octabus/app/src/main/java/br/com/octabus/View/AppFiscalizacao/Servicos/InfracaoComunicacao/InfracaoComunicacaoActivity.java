package br.com.octabus.View.AppFiscalizacao.Servicos.InfracaoComunicacao;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import br.com.octabus.Constantes;
import br.com.octabus.R;
import br.com.octabus.Util.FileHelper;
import br.com.octabus.Util.GPSTracker;
import br.com.octabus.Util.Network;
import br.com.octabus.Util.SessionManager;
import br.com.octabus.View.AppFiscalizacao.Servicos.Infracao.InfracaoActivity;
import br.com.octabus.View.Util.PesquisaCarro.PesquisaCarroActivity;
import br.com.octabus.View.Util.PesquisaFuncionario.PesquisaFuncionarioActivity;
import br.com.octabus.View.Util.PesquisaInfracao.PesquisaInfracaoActivity;
import br.com.octabus.View.Util.PesquisaLocalizacao.PesquisaLocalizacao;

public class InfracaoComunicacaoActivity extends AppCompatActivity {

    TextInputLayout inputLayoutPrefixoCarro,inputLayoutLinha, inputLayoutCobrador, inputLayoutMotorista;
    EditText inputHora,inputData, inputPrefixoCarro,inputLinha,inputCodInfracao, inputCobrador, inputMotorista, inputComplementoInfracao,inputFuncionario, inputLocalizacao;
    Button buttonGerarInfracao;
    ImageView imageViewSearchPrefixoCarro,imageViewSearchCodInfracao, imageViewSearchFuncionario, imageViewSearchLocalizacao;
    TextView complementoInfracao;
    CheckBox checkBoxInfratorMotorista, checkBoxInfratorCobrador;
    MaterialBetterSpinner spinnerTipoInfracao, spinnerCargoFuncionario;

    ArrayList<Bitmap> _dataFoto;
    int  _idInfracao, _idProgramacaoHorario, _idTipoInfracao, _idCargoFuncionario, _idFuncionarioInfracao, _sentidoTP, _sentidoTS,
            _infratorMotorista,_infratorCobrador;

    int _idTipoInfracaoFuncionario = 8;
    Double _longitude = null;
    Double _latitude = null;
    String _endereco = null;
    Boolean listaCargoCarregado = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infracao_comunicacao);

        setTitle(getResources().getString(R.string.tituloComunicacaoInfracao1));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfracaoComunicacaoActivity.this, InfracaoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final RelativeLayout currentView = (RelativeLayout) findViewById(R.id.currentView);
        currentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ImageView splash_screen = (ImageView) findViewById(R.id.splash_screen);
                int heightDiff = currentView.getRootView().getHeight() - currentView.getHeight();

                if (heightDiff > 200) {
                    splash_screen.setVisibility(View.GONE);
                } else {
                    splash_screen.setVisibility(View.VISIBLE);
                }
            }
        });

        inicializaUI();
    }

    public void inicializaUI()
    {
        _dataFoto = new ArrayList<Bitmap>();

        checkBoxInfratorMotorista = (CheckBox) findViewById(R.id.checkBoxInfratorMotorista);
        checkBoxInfratorCobrador = (CheckBox) findViewById(R.id.checkBoxInfratorCobrador);

        checkBoxInfratorMotorista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_infratorMotorista == 0) _infratorMotorista = 1;
                else _infratorMotorista = 0;
            }
        });
        checkBoxInfratorCobrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_infratorCobrador == 0) _infratorCobrador = 1;
                else _infratorCobrador = 0;
            }
        });
        complementoInfracao = (TextView) findViewById(R.id.complementoInfracao);

        buttonGerarInfracao = (Button) findViewById(R.id.buttonGerarInfracao);

        inputHora = (EditText) findViewById(R.id.inputHora);
        inputData = (EditText) findViewById(R.id.inputData);

        inputPrefixoCarro = (EditText) findViewById(R.id.inputPrefixoCarro);
        inputLinha = (EditText) findViewById(R.id.inputLinha);
        inputCodInfracao = (EditText) findViewById(R.id.inputCodInfracao);
        inputComplementoInfracao = (EditText) findViewById(R.id.inputComplementoInfracao);
        inputFuncionario = (EditText) findViewById(R.id.inputFuncionario);
        inputLocalizacao = (EditText) findViewById(R.id.inputLocalizacao);

        inputCobrador = (EditText) findViewById(R.id.inputCobrador);
        inputMotorista = (EditText) findViewById(R.id.inputMotorista);

        inputLayoutPrefixoCarro = (TextInputLayout) findViewById(R.id.inputLayoutPrefixoCarro);

        imageViewSearchPrefixoCarro = (ImageView) findViewById(R.id.imageViewSearchPrefixoCarro);
        imageViewSearchCodInfracao = (ImageView) findViewById(R.id.imageViewSearchCodInfracao);
        imageViewSearchFuncionario = (ImageView) findViewById(R.id.imageViewSearchFuncionario);
        imageViewSearchLocalizacao = (ImageView) findViewById(R.id.imageViewSearchLocalizacao);

        inputPrefixoCarro.setInputType(InputType.TYPE_NULL);
        inputCodInfracao.setInputType(InputType.TYPE_NULL);
        inputFuncionario.setInputType(InputType.TYPE_NULL);

        inputCobrador.setInputType(InputType.TYPE_NULL);
        inputMotorista.setInputType(InputType.TYPE_NULL);
        inputFuncionario.setInputType(InputType.TYPE_NULL);
        inputLocalizacao.setInputType(InputType.TYPE_NULL);

        imageViewSearchLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfracaoComunicacaoActivity.this, PesquisaLocalizacao.class);
                startActivityForResult(intent, 4);
                startActivity(intent);
            }
        });

        imageViewSearchPrefixoCarro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfracaoComunicacaoActivity.this, PesquisaCarroActivity.class);
                startActivityForResult(intent, 1);
                startActivity(intent);
            }
        });

        imageViewSearchFuncionario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(_idCargoFuncionario == 0)
                {
                    spinnerCargoFuncionario.setError(getString(R.string.erroParametro));
                }
                else
                {
                    Intent intent = new Intent(InfracaoComunicacaoActivity.this, PesquisaFuncionarioActivity.class);
                    intent.putExtra("fK_idCargo",_idCargoFuncionario);
                    startActivityForResult(intent, 3);
                    startActivity(intent);
                }
            }
        });

        imageViewSearchCodInfracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(_idTipoInfracao == 0)
                {
                    spinnerTipoInfracao.setError(getString(R.string.erroParametro));
                }
                else
                {
                    Intent intent = new Intent(InfracaoComunicacaoActivity.this, PesquisaInfracaoActivity.class);
                    intent.putExtra("fk_idTipoInfracao",_idTipoInfracao);
                    startActivityForResult(intent, 2);
                    startActivity(intent);
                }
            }
        });

        buttonGerarInfracao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gerarInfracao();
            }
        });

        SimpleDateFormat timeStampHourFormat = new SimpleDateFormat("HH:mm");
        Date myHour = new Date();

        String horaAtual = timeStampHourFormat.format(myHour);

        inputHora.setText(horaAtual);
        inputHora .setInputType(InputType.TYPE_NULL);
        inputHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker();
            }
        });

        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        String dataAtual = formatDate.format(myHour);


        inputData.setInputType(InputType.TYPE_NULL);
        inputData.setText(dataAtual);
        inputData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayPicker();
            }
        });

        spinnerTipoInfracao = (MaterialBetterSpinner) findViewById(R.id.spinnerTipoInfracao);
        recuperaTipoInfracao();

        spinnerCargoFuncionario = (MaterialBetterSpinner) findViewById(R.id.spinnerCargoFuncionario);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {

                _idProgramacaoHorario = Integer.parseInt(data.getStringExtra("idHorario"));

                inputPrefixoCarro.setText(data.getStringExtra("prefixoVeiculo"));
                inputLinha.setText(data.getStringExtra("descricaoLinha"));

                inputCobrador.setText(data.getStringExtra("nomeCobrador"));
                inputMotorista.setText(data.getStringExtra("nomeMotorista"));
                _sentidoTP = Integer.parseInt(data.getStringExtra("sentidoTP"));
                _sentidoTS = Integer.parseInt(data.getStringExtra("sentidoTS"));
            }
        }
        else if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                inputCodInfracao.setText(data.getStringExtra("descricaoInfracao"));

                String complemento = data.getStringExtra("complementoInfracao");

                if(!complemento.isEmpty())
                {
                    complementoInfracao.setText(complemento);
                    complementoInfracao.setVisibility(View.VISIBLE);
                }

                _idInfracao = Integer.parseInt(data.getStringExtra("idInfracao"));
            }
        }
        else if (requestCode == 3) {
            if(resultCode == RESULT_OK) {
                inputFuncionario.setText(data.getStringExtra("nomeFuncionario"));
                _idFuncionarioInfracao = Integer.parseInt(data.getStringExtra("idFuncionario"));
            }
        }
        else if (requestCode == 4) {
            if(resultCode == RESULT_OK) {

                inputLocalizacao.setText(data.getStringExtra("endereco"));

                _longitude = data.getDoubleExtra("longitude",0);
                _latitude = data.getDoubleExtra("latitude",0);
                _endereco = data.getStringExtra("endereco");
            }
        }
        else
        {
            if(data != null){
                Bundle bundle = data.getExtras();
                if(bundle != null){
                    Bitmap img = (Bitmap) bundle.get("data");
                    _dataFoto.add(img);

                    final GridView gridview = (GridView) findViewById(R.id.gridview);
                    gridview.setAdapter(new InfracaoComunicacaoCameraAdapter(getApplicationContext(), _dataFoto));
                    setDynamicHeight(gridview);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_infracao_comunicacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnAdicionarFoto:
                tirarFoto();
                break;
        }
        return true;
    }

    private void timePicker()
    {
        int mHour, mMinute;

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                       String stringMinute = String.valueOf(minute);
                       if(String.valueOf(minute).length() == 1) stringMinute = "0" + String.valueOf(minute);

                       inputHora.setText(hourOfDay + ":" + stringMinute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void dayPicker()
    {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
                inputData.setText(formatDate.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDatePickerDialog.show();
    }

    public void tirarFoto(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }

    public void recuperaTipoInfracao()
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.aguarde));
        dialog.setCancelable(false);

        Request request = Request.create(Constantes.urlTipoInfracao);
        request.setMethod("POST")
        .setTimeout(60)
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
                    final JSONArray response = jsonObject.getJSONArray("rows");

                    ArrayList<String> lista = new ArrayList<String>();

                    if (response != null) {
                        for (int i=0;i< response.length();i++){
                            JSONObject item = (JSONObject) response.get(i);

                            lista.add(item.get("tipoInfracao").toString());
                        }
                    }

                    spinnerTipoInfracao.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, lista));
                    spinnerTipoInfracao.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {}

                        @Override
                        public void afterTextChanged(Editable s) {

                            for (int i=0;i< response.length();i++){
                                try {
                                    JSONObject item = (JSONObject) response.get(i);
                                    if(item.get("tipoInfracao").toString().equals(s.toString()))
                                    {
                                        _idTipoInfracao = Integer.parseInt(item.get("idTipoInfracao").toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            spinnerTipoInfracao.setError(null);

                            inputCodInfracao.setText("");
                            _idInfracao = 0;

                            if(_idTipoInfracao == _idTipoInfracaoFuncionario)
                            {
                                showInfracaoFuncionario();
                            }
                            else
                            {
                                showInfracaoProgramacao();
                            }
                        }
                    });
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

    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();

        float x = 1;
        if( items > 3){
            x = items/3;
            rows = (int) (x + 1);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

    public void adicionarLocalizacao()
    {
        Intent intent = new Intent(InfracaoComunicacaoActivity.this, PesquisaLocalizacao.class);
        startActivityForResult(intent, 4);
        startActivity(intent);
    }

    public void showInfracaoProgramacao()
    {
        LinearLayout infracaoProgramacao = (LinearLayout) findViewById(R.id.infracaoProgramacao);
        infracaoProgramacao.setVisibility(View.VISIBLE);


        LinearLayout infracaoFuncionario = (LinearLayout) findViewById(R.id.infracaoFuncionario);
        infracaoFuncionario.setVisibility(View.GONE);
    }

    public void showInfracaoFuncionario()
    {
        LinearLayout infracaoProgramacao = (LinearLayout) findViewById(R.id.infracaoProgramacao);
        infracaoProgramacao.setVisibility(View.GONE);


        LinearLayout infracaoFuncionario = (LinearLayout) findViewById(R.id.infracaoFuncionario);
        infracaoFuncionario.setVisibility(View.VISIBLE);

        if(listaCargoCarregado == false)
        {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(getString(R.string.aguarde));
            dialog.setCancelable(false);

            Request request = Request.create(Constantes.urlFuncionarioCargo);
            request.setMethod("POST")
                    .setTimeout(60)
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
                                listaCargoCarregado = true;
                                final JSONArray response = jsonObject.getJSONArray("rows");

                                ArrayList<String> lista = new ArrayList<String>();

                                if (response != null) {
                                    for (int i=0;i< response.length();i++){
                                        JSONObject item = (JSONObject) response.get(i);

                                        lista.add(item.get("cargo").toString());
                                    }
                                }

                                spinnerCargoFuncionario.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_spinner_dropdown_item, lista));
                                spinnerCargoFuncionario.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        for (int i=0;i< response.length();i++){
                                            try {
                                                JSONObject item = (JSONObject) response.get(i);
                                                if(item.get("cargo").toString().equals(s.toString()))
                                                {
                                                    _idCargoFuncionario = Integer.parseInt(item.get("idCargo").toString());
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        spinnerCargoFuncionario.setError(null);
                                    }
                                });
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

    public void enviarFotos(int idInfracao)
    {
        for(int i = 0; i < _dataFoto.size(); i++)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileHelper fileHelper = new FileHelper(this);

                fileHelper.saveImage(_dataFoto.get(i));

                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage(getString(R.string.aguardeEnviandoFoto));
                dialog.setCancelable(false);

                Request request = Request.create(Constantes.urlUploadFotoInfracao);
                request.setMethod("POST")
                        .setTimeout(60)
                        .addParameter("tokenLoginService", Constantes.keyApp)
                        .addParameter("fk_idInfracao", idInfracao)
                        .addParameter("foto", new File(fileHelper.getDirFile()))
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
                                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.infracaoGerada) + " #" + jsonObject.getString("response"), Snackbar.LENGTH_LONG).show();
                                    finish();
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

            }
        }
    }

    public void gerarInfracao()
    {
        if(!Network.isOnline(getApplicationContext()))
        {
            Snackbar.make(findViewById(R.id.currentView), getString(R.string.semConexao), Snackbar.LENGTH_LONG).show();
        }
        else
        {
            final GPSTracker gps = new GPSTracker(this);

            gps.getLocation();

            if(!gps.canGetLocation())
            {
                gps.showSettingsAlert();
            }
            else
            {
                String complemento = inputComplementoInfracao.getText().toString();
                String _hora = inputHora.getText().toString();
                String _data = inputData.getText().toString();

                if(_idTipoInfracao == _idTipoInfracaoFuncionario)
                {
                    _idProgramacaoHorario = 0;
                    _sentidoTP = 0;
                    _sentidoTS = 0;
                }
                else
                {
                    _idFuncionarioInfracao = 0;
                }

                if(_idInfracao == 0)
                {
                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.selecioneInfracao), Snackbar.LENGTH_LONG).show();
                }
                else if(_idProgramacaoHorario == 0)
                {
                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.selecioneAProgramacao), Snackbar.LENGTH_LONG).show();
                }
                else if(_infratorMotorista == 0 && _infratorCobrador == 0)
                {
                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.selecioneInfrator), Snackbar.LENGTH_LONG).show();
                }
                else
                {

                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    HashMap _dataUser = sessionManager.getUserDetails();
                    sessionManager = null;

                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage(getString(R.string.aguarde));
                    dialog.setCancelable(false);

                    Request request = Request.create(Constantes.urlGerarInfracao);
                    request.setMethod("POST")
                            .setTimeout(60)
                            .addParameter("tokenLoginService", Constantes.keyApp)
                            .addParameter("fk_idHorario", _idProgramacaoHorario)
                            .addParameter("fk_idFuncionario", _idFuncionarioInfracao)
                            .addParameter("sentidoTP", _sentidoTP)
                            .addParameter("sentidoTS", _sentidoTS)
                            .addParameter("fk_idInfracao", _idInfracao)
                            .addParameter("complemento", complemento)
                            .addParameter("latitude", ""+_latitude)
                            .addParameter("longitude", ""+_longitude)
                            .addParameter("endereco", ""+_endereco)
                            .addParameter("infratorMotorista", ""+_infratorMotorista)
                            .addParameter("infratorCobrador", ""+_infratorCobrador)
                            .addParameter("latitudeEmissor", ""+gps.getLatitude())
                            .addParameter("longitudeEmissor", ""+gps.getLongitude())
                            .addParameter("fk_idFuncionarioEmissor", _dataUser.get("idFuncionario").toString())
                            .addParameter("hora", _hora)
                            .addParameter("data", _data)
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
                                        Snackbar.make(findViewById(R.id.currentView), getString(R.string.infracaoGerada) + " #" + jsonObject.getString("response"), Snackbar.LENGTH_LONG).show();

                                        if(_dataFoto.size() > 0)
                                        {
                                            enviarFotos(Integer.parseInt(jsonObject.getString("response")));
                                        }
                                        else
                                        {
                                            finish();
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
            }
        }
    }

}
