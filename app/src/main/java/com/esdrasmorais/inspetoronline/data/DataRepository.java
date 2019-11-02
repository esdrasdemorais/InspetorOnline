package com.esdrasmorais.inspetoronline.data;

import android.media.browse.MediaBrowser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.esdrasmorais.inspetoronline.data.model.Company;
import com.esdrasmorais.inspetoronline.data.model.Line;
import com.esdrasmorais.inspetoronline.data.model.Vehicle;

import java.util.List;

public class DataRepository {
    private static volatile DataRepository dataRepository;

    private final AppDatabase appDatabase;
    private MediatorLiveData<List<Company>> observableCompanies;
    private MediatorLiveData<List<Line>> observableLines;
    private MediatorLiveData<List<Vehicle>> observableVehicles;

    private void setObservableCompanies() {
        observableCompanies = new MediatorLiveData<>();
        observableCompanies.addSource(
            appDatabase.getCompanyDao().getAll(),
            companies -> {
                if (appDatabase.getDatabaseCreated().getValue() != null) {
                    observableCompanies.postValue(companies);
                }
            }
        );
    }

    private void setObservableLines() {
        observableLines = new MediatorLiveData<>();
        observableLines.addSource(
            appDatabase.getLineDao().getAll(),
            lines -> {
                if (appDatabase.getDatabaseCreated().getValue() != null) {
                    observableLines.postValue(lines);
                }
            }
        );
    }

    private void setObservableVehicles() {
        observableVehicles = new MediatorLiveData<>();
        observableVehicles.addSource(
            appDatabase.getVehicleDao().getAll(),
            vehicles -> {
                if (appDatabase.getDatabaseCreated().getValue() != null) {
                    observableVehicles.postValue(vehicles);
                }
            }
        );
    }

    private DataRepository(final AppDatabase appDatabase) {
        if (dataRepository != null) {
            throw new RuntimeException("Prevent Reflection Api!");
        }
        this.appDatabase = appDatabase;
        setObservableCompanies();
        setObservableLines();
        setObservableVehicles();
    }

    public static DataRepository getInstance(final AppDatabase appDatabase) {
        if (dataRepository == null) {
            synchronized (DataRepository.class) {
                if (dataRepository == null) {
                    dataRepository = new DataRepository(appDatabase);
                }
            }
        }
        return dataRepository;
    }

    public LiveData<List<Company>> getCompanies() {
        return observableCompanies;
    }

    public LiveData<List<Line>> getLines() {
        return observableLines;
    }

    public LiveData<Company> findByCompanyReferenceCode(
        final Integer companyReferenceCode
    ) {
        return appDatabase.getCompanyDao().findByCompanyReferenceCode(
            companyReferenceCode
        );
    }
}