package br.com.octabus.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zanjou.http.request.Request;
import com.zanjou.http.response.JsonResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.octabus.Constantes;
import br.com.octabus.Util.GPSTracker;
import br.com.octabus.Util.Network;
import br.com.octabus.Util.SessionManager;

/**
 * Created by marcatti on 27/02/17.
 */

public class ServicoAlertaEvento extends Service {
    private static final String TAG = "ServicoLocalizacao";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        run();
        return START_STICKY;
    }
    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");

    }

    public void run()
    {
        final Context context = this;
        final GPSTracker gps = new GPSTracker(context);

        SessionManager sessionManager = new SessionManager(context);
        final HashMap _dataUser = sessionManager.getUserDetails();
        sessionManager = null;

        if((int) _dataUser.get("idLogin") > 0)
        {
            Thread thread  = new Thread(new Runnable() {
                public void run () {
                    while(true)
                    {
                        gps.getLocation();

                        SystemClock.sleep(2000);

                        if(Network.isOnline(context))
                        {
                            if(gps.canGetLocation())
                            {
                                Double latitude = gps.getLatitude();
                                Double longitude = gps.getLongitude();

                                if((int) _dataUser.get("idLogin") > 0 && gps.getAccuracy() <= 5)
                                {
                                    Request request = Request.create(Constantes.urlMonitoramento);
                                    request.setMethod("POST")
                                            .setTimeout(15)
                                            .addParameter("tokenLoginService", Constantes.keyApp)
                                            .addParameter("idLogin", (int) _dataUser.get("idLogin"))
                                            .addParameter("latitude", latitude)
                                            .addParameter("longitude", longitude)
                                            .setResponseListener(new JsonResponseListener() {
                                                @Override
                                                public void onOkResponse(JSONObject jsonObject) throws JSONException {

                                                    if((Boolean) jsonObject.get("success"))
                                                    {
                                                    }
                                                    else
                                                    {
                                                        Log.e(TAG, "error");
                                                    }
                                                }

                                                @Override
                                                public void onErrorResponse(JSONObject jsonObject) throws JSONException {
                                                    Log.e(TAG, "fataly");
                                                }

                                                @Override
                                                public void onParseError(JSONException e) {
                                                    Log.e(TAG, "fataly");
                                                    e.printStackTrace();

                                                }
                                            }).execute();
                                }
                            }
                        }
                    }
                }
            });

            thread.start();
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }
}