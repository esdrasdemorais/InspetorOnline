package com.esdrasmorais.inspetoronline.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.esdrasmorais.inspetoronline.data.dao.CompanyDao;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private AppDatabase database;

    @Singleton
    @Provides
    RoomDatabase geDb(Application app) {
        return Room.databaseBuilder(
            app, AppDatabase.class, "InspetorOnline.db"
        )
        .addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                database = (AppDatabase) db;
                super.onCreate(db);
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        getCompanyDao(database).insertAll(
                            CsvUtil.getCompanies()
                        );
                    }
                });
            }
        })
        .build();
    }

    @Singleton
    @Provides
    CompanyDao getCompanyDao(AppDatabase db) {
        return db.companyDao();
    }
}