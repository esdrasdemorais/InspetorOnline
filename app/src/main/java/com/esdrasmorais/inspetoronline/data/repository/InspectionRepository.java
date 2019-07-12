package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Default;
import com.esdrasmorais.inspetoronline.data.model.Inspection;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class InspectionRepository
    extends Repository<Inspection>
    implements IInspectionRepository
{
    public InspectionRepository(Class<Inspection> clas) {
        super(clas);
    }

    @Override
    public List<Inspection> findByLocation(Location location) {
        db.collectionGroup(clas.getName())
            .whereEqualTo("address", location).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    return;
                    }
                }
            );
        return objects;
    }


}
