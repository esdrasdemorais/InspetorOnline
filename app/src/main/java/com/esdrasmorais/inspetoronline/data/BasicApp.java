package com.esdrasmorais.inspetoronline.data;

import android.app.Application;
import android.content.Context;

public class BasicApp extends Application {
    private AppExecutors mAppExecutors;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public BasicApp(Context context) {
        this.context = context;
        mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(context, mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getDatabase());
    }
}
