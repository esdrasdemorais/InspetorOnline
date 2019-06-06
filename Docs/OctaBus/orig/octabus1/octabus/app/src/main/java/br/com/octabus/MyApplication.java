package br.com.octabus;

import android.app.Application;
import com.devs.acr.AutoErrorReporter;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(!Constantes.debug)
        {
            AutoErrorReporter.get(this)
                    .setEmailAddresses(Constantes.emailReport)
                    .setEmailSubject(Constantes.emailSubjectReport)
                    .start();
        }
    }


}