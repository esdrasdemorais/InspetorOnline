package com.esdrasmorais.inspetoronline.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.esdrasmorais.inspetoronline.data.model.Company;

import java.util.List;

public class DataRepository {
    private static volatile DataRepository dataRepository;

    private final AppDatabase appDatabase;
    private MediatorLiveData<List<Company>> observableProducts;

    private DataRepository(final AppDatabase appDatabase) {
        if (dataRepository != null) {
            throw new RuntimeException("Prevent Reflection Api!");
        }

        this.appDatabase = appDatabase;
        observableProducts = new MediatorLiveData<>();

        observableProducts.addSource(
            appDatabase.getCompanyDao().getAll(),
            companies -> {
                if (appDatabase.getDatabaseCreated().getValue() != null) {
                    observableProducts.postValue(companies);
                }
            }
        );
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
        return observableProducts;
    }

    public LiveData<Company> findByCompanyReferenceCode(
        final Integer companyReferenceCode
    ) {
        return appDatabase.getCompanyDao().findByCompanyReferenceCode(
            companyReferenceCode
        );
    }
}
