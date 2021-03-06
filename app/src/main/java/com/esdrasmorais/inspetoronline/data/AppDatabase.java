package com.esdrasmorais.inspetoronline.data;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.esdrasmorais.inspetoronline.data.converters.DateConverter;
import com.esdrasmorais.inspetoronline.data.converters.DirectionConverter;
import com.esdrasmorais.inspetoronline.data.converters.LineTypeConverter;
import com.esdrasmorais.inspetoronline.data.dao.CompanyDao;
import com.esdrasmorais.inspetoronline.data.dao.LineDao;
import com.esdrasmorais.inspetoronline.data.dao.VehicleDao;
import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.LineType;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bolts.Continuation;
import bolts.Task;

@Database(entities = {Company.class, Line.class, Vehicle.class}, version = 1)
@TypeConverters({DateConverter.class, DirectionConverter.class, LineTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase appDatabase;

    private static final String TAG = AppDatabase.class.getSimpleName();

//    public AppDatabase() {
//        if (appDatabase != null) {
//            throw new RuntimeException("Prevent Reflection Api");
//        }
//    }

    @VisibleForTesting
    public static final String DATABASE_NAME = "InspetorOnline.db";

    public abstract CompanyDao getCompanyDao();

    public abstract LineDao getLineDao();

    public abstract VehicleDao getVehicleDao();

    private final MutableLiveData<Boolean> isDatabaseCreated =
        new MutableLiveData<>();

    private static SpTrans spTrans = null;

    private static CsvUtil csvUtil = new CsvUtil();

    private static List<Company> companies;

    private static List<Line> lines;

    private static List<Vehicle> vehicles;

    private void setDatabaseCreated() {
        isDatabaseCreated.postValue(true);
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    public static AppDatabase getInstance(
        final Context appContext, final AppExecutors executors
    ) {
        if (appDatabase != null) {
            return appDatabase;
        }
        synchronized (AppDatabase.class) {
            if (appDatabase == null) {
                spTrans = new SpTrans(appContext, null);
                appDatabase = buildDatabase(appContext, executors);
                appDatabase.updateDatabaseCreated(
                    appContext.getApplicationContext()
                );
            }
            return appDatabase;
        }
    }

    //Make singleton from serialize and deserialize operation.
    protected AppDatabase readResolve(
        Application appContext, final AppExecutors executors
    ) {
        return getInstance(appContext, executors);
    }

    private static void addDelay(Integer minsecs) {
        try {
            Thread.sleep(minsecs);
        } catch (InterruptedException ex) {
            Log.e("AppDatabase", "Sleep failed!");
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return isDatabaseCreated;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("");
        }
    };

    private static void insertData(
        final AppDatabase appDatabase, final List<Company> companies,
        final List<Line> lines, final List<Vehicle> prefixes
    ) {
        appDatabase.runInTransaction(() -> {
            if (companies != null && companies.size() > 0)
                appDatabase.getCompanyDao().insertAll(companies);
            if (lines != null && lines.size() > 0)
                appDatabase.getLineDao().insertAll(lines);
            if (prefixes != null && prefixes.size() > 0)
                appDatabase.getVehicleDao().insertAll(prefixes);
        });
    }

    private static List<Line> getLines() {
        List<Line> lines = null;
        try {
           //lines = spTrans.getLineList();
           if (lines == null)
               lines = csvUtil.getLines();
        } catch (Exception ex) {
            Log.e("AppDatabase", ex.getMessage());
            lines = csvUtil.getLines();
        }
        return lines;
    }

    private static List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = null;
        try {
            //vehicles = spTrans.getVehicleList();
            if (vehicles == null)
                vehicles = csvUtil.getVehicles();
        } catch (Exception ex) {
            Log.e("AppDatabase", ex.getMessage());
            vehicles = csvUtil.getVehicles();
        }
        return vehicles;
    }

    private static AppDatabase buildDatabase(
        final Context appContext, final AppExecutors executors
    ) {
        return Room.databaseBuilder(
            appContext, AppDatabase.class, DATABASE_NAME
        )
        .addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            executors.diskIO().execute(() -> {
                addDelay(1700);
                AppDatabase appDatabase = AppDatabase.getInstance(
                    appContext, executors
                );
                //Task.callInBackground((Callable<Void>) () -> {
//                    ExecutorService service =  Executors.newSingleThreadExecutor();
//                    service.submit(new Runnable() {
//                       @Override
//                       public void run() {
//                           csvUtil.getCompanies().observeForever(new Observer<List<Company>>() {
//                               @Override
//                               public void onChanged(List<Company> companiesL) {
//                                   companies = companiesL;
//                                   if (companies != null)
//                                       insertData(appDatabase, companies, null, null);
//                               }
//                           });
//                       }
//                    });
                /*    return null;
                }).continueWith((Continuation<Void, Void>) task -> {
                    if (task.isFaulted())
                        Log.e(TAG, "find failed", task.getError());
                    return null;
                });*/
                companies = csvUtil.getCompanies();
                lines = getLines();
                vehicles = getVehicles();
                insertData(appDatabase, companies, lines, vehicles);
                appDatabase.setDatabaseCreated();
            });
            }
        })
        .addMigrations(MIGRATION_1_2)
        .build();
    }
}