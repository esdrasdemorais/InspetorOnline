package com.esdrasmorais.inspetoronline.data;

import android.app.Application;
import android.content.Context;

public class BasicApp extends Application {
    private AppExecutors mAppExecutors;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        mAppExecutors = new AppExecutors();
    }

    public BasicApp() {
    }

    public BasicApp(Context context) {
        this.context = context;
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(context, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
