package br.com.octabus.View.AppFiscalizacao.Servicos.InformacaoPontoComunicacao;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.util.List;

import br.com.octabus.Constantes;
import br.com.octabus.R;
import br.com.octabus.Util.FileHelper;
import br.com.octabus.Util.GPSTracker;
import br.com.octabus.Util.Network;
import br.com.octabus.Util.SessionManager;
import br.com.octabus.View.AppFiscalizacao.Servicos.Infracao.InfracaoActivity;
import br.com.octabus.View.AppFiscalizacao.Servicos.InfracaoComunicacao.InfracaoComunicacaoCameraAdapter;
import br.com.octabus.View.Util.PesquisaLocalizacao.PesquisaLocalizacao;

public class InformacaoPontoComunicacaoActivity extends AppCompatActivity {

    TextInputLayout inputLayoutPrefixoCarro;
    EditText inputPrefixoCarro,inputCobrador, inputMotorista, inputLinha, inputHora,inputData, inputLocalizacao;
    ImageView imageViewSearchLocalizacao;
    ArrayList<ListView> listViewArray;
    Double _longitude = null;
    Double _latitude = null;
    String _endereco = null;
    ArrayList<Bitmap> _dataFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacao_ponto_comunicacao);

        setTitle(getResources().getString(R.string.tituloComunicacaoInformacaoPonto));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_backspace_black_24dp));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacaoPontoComunicacaoActivity.this, InfracaoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        inicializaUi();
    }

    public void inicializaUi()
    {
        _dataFoto = new ArrayList<Bitmap>();

        listViewArray = new ArrayList<ListView>();
        inputPrefixoCarro = (EditText) findViewById(R.id.inputPrefixoCarro);
        inputCobrador = (EditText) findViewById(R.id.inputCobrador);
        inputMotorista = (EditText) findViewById(R.id.inputMotorista);
        inputLinha = (EditText) findViewById(R.id.inputLinha);

        inputLayoutPrefixoCarro = (TextInputLayout) findViewById(R.id.inputLayoutPrefixoCarro);

        inputHora = (EditText) findViewById(R.id.inputHora);
        inputData = (EditText) findViewById(R.id.inputData);

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

        imageViewSearchLocalizacao = (ImageView) findViewById(R.id.imageViewSearchLocalizacao);
        imageViewSearchLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformacaoPontoComunicacaoActivity.this, PesquisaLocalizacao.class);
                startActivityForResult(intent, 4);
                startActivity(intent);
            }
        });

        inputLocalizacao = (EditText) findViewById(R.id.inputLocalizacao);
        inputLocalizacao.setInputType(InputType.TYPE_NULL);


        recuperarInformacaoPonto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_informacao_ponto_comunicacao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnSalvar:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.confirmarInformacaoPonto))
                        .setCancelable(false)
                        .setPositiveButton("Aceito", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                gerarInformacaoPonto();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;

                case R.id.btnAdicionarFoto:
                    tirarFoto();
                    break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4) {
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

    public void recuperarInformacaoPonto()
    {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.aguarde));
        dialog.setCancelable(false);

        Request request = Request.create(Constantes.urlRecuperarDadosInformacaoPonto);
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

                            if (response != null) {
                                for (int i=0;i< response.length();i++){
                                    JSONObject item = (JSONObject) response.get(i);

                                    LinearLayout LinearLayoutAreaInformacaoPonto = (LinearLayout)findViewById(R.id.LinearLayoutInformacaoPonto);
                                    LinearLayoutAreaInformacaoPonto.setOrientation(LinearLayout.VERTICAL);
                                    LinearLayoutAreaInformacaoPonto.setPadding(0,0,0,40);

                                    /**************** LINEAR VIEW ****/
                                    LinearLayout linearView = new LinearLayout(getApplicationContext());
                                    linearView.setOrientation(LinearLayout.HORIZONTAL);

                                    View view_ = new View(getApplicationContext());
                                    view_.setBackgroundColor(getResources().getColor(R.color.gray));

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 2, 1f);
                                    params.setMargins(0, 10, 0, 0);
                                    view_.setLayoutParams(params);
                                    /**************** LINEAR VIEW ****/
                                    /**************** TEXT VIEW ****/
                                    TextView textView_= new TextView(getApplicationContext());
                                    textView_.setText(item.get("grupo").toString());
                                    textView_.setPadding(10,0,10,0);
                                    /**************** TEXT VIEW ****/

                                    /**************** LINEAR VIEW ****/
                                    linearView.setOrientation(LinearLayout.HORIZONTAL);

                                    View view_2 = new View(getApplicationContext());
                                    view_2.setBackgroundColor(getResources().getColor(R.color.gray));

                                    LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 2, 1f);
                                    params2.setMargins(0, 10, 0, 0);
                                    view_2.setLayoutParams(params2);
                                    /**************** LINEAR VIEW ****/

                                    /**************** LIST VIEW ****/
                                    ArrayList<JSONObject> lista = new ArrayList<>();

                                    final JSONArray responseItems = item.getJSONArray("items");;
                                    for (int icheck= 0; icheck < responseItems.length();icheck++){
                                        JSONObject itemCheck = (JSONObject) responseItems.get(icheck);
                                        lista.add(itemCheck);
                                    }

                                    ListView listView = new ListView(getApplicationContext());
                                    LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (70 * responseItems.length()));
                                    params3.setMargins(10, 0, 10, 0);
                                    listView.setLayoutParams(params3);

                                    Adapter opcaoAdapter = new InformacaoPontoComunicacaoItemAdapter(getApplicationContext(), lista);
                                    listView.setAdapter((ListAdapter) opcaoAdapter);

                                    view_.setPadding(0,15,0,0);
                                    view_2.setPadding(0,15,0,0);

                                    linearView.addView(view_);
                                    linearView.addView(textView_);
                                    linearView.addView(view_2);

                                    LinearLayoutAreaInformacaoPonto.addView(linearView);
                                    LinearLayoutAreaInformacaoPonto.addView(listView);

                                    listViewArray.add(listView);

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

    public void gerarInformacaoPonto()
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

                    ArrayList<String> lista = new ArrayList<>();

                    for(int i = 0; i < listViewArray.size(); i++)
                    {
                        Adapter adapterList = listViewArray.get(i).getAdapter();
                        List itemCheck = ((InformacaoPontoComunicacaoItemAdapter) adapterList).getList();

                        for(int iCheck = 0; iCheck < itemCheck.size(); iCheck++)
                        {
                            lista.add(itemCheck.get(iCheck).toString());
                        }
                    }

                    String listaString = "["+ TextUtils.join(",", lista) +"]";

                    String _hora = inputHora.getText().toString();
                    String _data = inputData.getText().toString();

                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    HashMap _dataUser = sessionManager.getUserDetails();
                    sessionManager = null;

                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage(getString(R.string.aguarde));
                    dialog.setCancelable(false);

                    Request request = Request.create(Constantes.urlGerarInformacaoPonto);
                    request.setMethod("POST")
                            .setTimeout(60)
                            .addParameter("tokenLoginService", Constantes.keyApp)
                            .addParameter("latitudeEmissor", ""+gps.getLatitude())
                            .addParameter("longitudeEmissor", ""+gps.getLongitude())
                            .addParameter("fk_idFuncionarioEmissor", _dataUser.get("idFuncionario").toString())
                            .addParameter("horaEmissor", _hora)
                            .addParameter("dataEmissor", _data)
                            .addParameter("listaInformacaoPonto", listaString)

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
                                        Snackbar.make(findViewById(R.id.currentView), getString(R.string.informacaoPontoGerada) + " #" + jsonObject.getString("response"), Snackbar.LENGTH_LONG).show();

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

    public void enviarFotos(int idInformacaoPonto)
    {
        for(int i = 0; i < _dataFoto.size(); i++)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileHelper fileHelper = new FileHelper(this);

                fileHelper.saveImage(_dataFoto.get(i));

                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage(getString(R.string.aguardeEnviandoFoto));
                dialog.setCancelable(false);

                Request request = Request.create(Constantes.urlUploadFotoidInformacaoPonto);
                request.setMethod("POST")
                        .setTimeout(60)
                        .addParameter("tokenLoginService", Constantes.keyApp)
                        .addParameter("fk_idInformacaoPonto", idInformacaoPonto)
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

    public void tirarFoto(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, 0);
    }
}
