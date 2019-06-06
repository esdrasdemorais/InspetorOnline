package br.com.octabus.View.Util.PesquisaLocalizacao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zanjou.http.request.Request;
import com.zanjou.http.request.RequestStateListener;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import br.com.octabus.R;
import br.com.octabus.Util.GPSTracker;
import br.com.octabus.Util.Network;

public class PesquisaLocalizacao extends AppCompatActivity {

    EditText inputEndereco;
    TextInputLayout inputLayoutEndereco;
    ImageView imageViewSearchEndereco;
    Button buttonUtilizarEndereco;

    String _endereco;
    Double _latitude, _longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_localizacao);
        setTitle(getResources().getString(R.string.tituloRecuperarEndereco));

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

        recuperarLocalizacaoGPS();
    }

    public void inicializaUi()
    {
        inputLayoutEndereco = (TextInputLayout) findViewById(R.id.inputLayoutEndereco);
        inputEndereco = (EditText) findViewById(R.id.inputEndereco);
        imageViewSearchEndereco = (ImageView) findViewById(R.id.imageViewSearchEndereco);
        buttonUtilizarEndereco = (Button) findViewById(R.id.buttonUtilizarEndereco);

        buttonUtilizarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(_endereco.equals("") || _latitude.equals("") || _longitude.equals(""))
                {
                    Snackbar.make(findViewById(R.id.currentView), getString(R.string.localizacaoNaoEncontrada), Snackbar.LENGTH_LONG).show();
                }
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra("endereco", _endereco);
                    intent.putExtra("latitude", _latitude);
                    intent.putExtra("longitude", _longitude);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        imageViewSearchEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarLocalizacaoPorEndereco();
            }
        });
    }

    public void recuperarLocalizacaoGPS()
    {
        final GPSTracker gps = new GPSTracker(this);

        if(!Network.isOnline(getApplicationContext()))
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
                final Double latitude = gps.getLatitude();
                final Double longitude = gps.getLongitude();

                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage(getString(R.string.aguarde));
                dialog.setCancelable(false);

                Request request = Request.create("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," +longitude + "&sensor=true");
                request.setMethod("GET")
                .setTimeout(60)
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

                        if(jsonObject.get("status").equals("OK"))
                        {
                            JSONArray response = jsonObject.getJSONArray("results");

                            JSONObject jsonObjectAddress = (JSONObject) response.get(0);

                            String address = jsonObjectAddress.get("formatted_address").toString();

                            setLocalizacaoMapa(address, latitude, longitude);
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

    public void recuperarLocalizacaoPorEndereco()
    {
        try
        {
            String endereco =  inputEndereco.getText().toString();

            if(endereco.equals("") || endereco.length() < 5)
            {
                inputLayoutEndereco.setError(getString(R.string.erroParametro));
            }
            else
            {
                final ProgressDialog dialog = new ProgressDialog(this);
                dialog.setMessage(getString(R.string.aguarde));
                dialog.setCancelable(false);

                Request request = null;
                request = Request.create("http://maps.google.com/maps/api/geocode/json?address="+ URLEncoder.encode(endereco, "UTF-8")+"&sensor=false");
                request.setMethod("GET")
                        .setTimeout(60)
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

                                if(jsonObject.get("status").equals("OK"))
                                {
                                    JSONArray response = jsonObject.getJSONArray("results");

                                    JSONObject jsonObjectAddress = (JSONObject) response.get(0);

                                    JSONObject jsonObjectGeometry = (JSONObject) jsonObjectAddress.get("geometry");
                                    JSONObject jsonObjectLocation = (JSONObject) jsonObjectGeometry.get("location");

                                    Double latitude = jsonObjectLocation.getDouble("lat");
                                    Double longitude = jsonObjectLocation.getDouble("lng");

                                    String address = jsonObjectAddress.get("formatted_address").toString();

                                    setLocalizacaoMapa(address, latitude, longitude);
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setLocalizacaoMapa(String endereco, double latitude, double longitude)
    {
        inputEndereco.setText(endereco);

        GoogleMap googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.mapaInfracao)).getMap();

        googleMap.clear();

        LatLng sydney = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(sydney).title(endereco)).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        _endereco = endereco;
        _latitude = latitude;
        _longitude = longitude;
    }
}
