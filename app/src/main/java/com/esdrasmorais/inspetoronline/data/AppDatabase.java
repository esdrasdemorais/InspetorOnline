package com.esdrasmorais.inspetoronline.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
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

import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Company.class, Line.class, Vehicle.class}, version = 1)
@TypeConverters({DateConverter.class, DirectionConverter.class, LineTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase appDatabase;

    public AppDatabase() {
        if (appDatabase != null) {
            throw new RuntimeException("Prevent Reflection Api");
        }
    }

    @VisibleForTesting
    public static final String DATABASE_NAME = "InspetorOnline.db";

    public abstract CompanyDao getCompanyDao();

    public abstract LineDao getLineDao();

    public abstract VehicleDao getVehicleDao();

    private final MutableLiveData<Boolean> isDatabaseCreated =
        new MutableLiveData<>();

    private static SpTrans spTrans = null;

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

    private static void addDelay() {
        try {
            Thread.sleep(4000);
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
            //database.execSQL("");
        }
    };

    private static void insertData(
        final AppDatabase appDatabase, final List<Company> companies,
        final List<Line> lines, final List<Vehicle> prefixes
    ) {
        appDatabase.runInTransaction(() -> {
            appDatabase.getCompanyDao().insertAll(companies);
            appDatabase.getLineDao().insertAll(lines);
            appDatabase.getVehicleDao().insertAll(prefixes);
        });
    }

    private static List<Line> getLines() {
        List<Line> lines = null;
        try {
           lines = spTrans.getLineList();
        } catch (Exception ex) {
            Log.e("AppDatabase", ex.getMessage());
            lines = CsvUtil.getLines();
        }
        return lines;
    }

    private static List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = null;
        try {
            vehicles = spTrans.getVehicle();
        } catch (Exception ex) {
            Log.e("AppDatabase", ex.getMessage());
            vehicles = CsvUtil.getVehicles();
        }
        return vehicles;
    }

    private static AppDatabase buildDatabase(
        final Context appContext, final AppExecutors executors
    ) {
        return Room.databaseBuilder(
            appContext, AppDatabase.class, DATABASE_NAME
        )
        .addCallback(new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                executors.diskIO().execute(() -> {
                    AppDatabase appDatabase = AppDatabase.getInstance(
                        appContext, executors
                    );
                    List<Company> companies = CsvUtil.getCompanies();
                    List<Line> lines = getLines();
                    List<Vehicle> vehicles = getVehicles();
                    insertData(appDatabase, companies, lines, vehicles);
                    appDatabase.setDatabaseCreated();
                    //addDelay();
                });
            }
        })
        //.addMigrations(MIGRATION_1_2)
        .build();
    }
}
