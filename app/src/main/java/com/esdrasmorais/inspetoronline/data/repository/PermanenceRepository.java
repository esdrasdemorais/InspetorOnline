package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Inspection;
import com.esdrasmorais.inspetoronline.data.model.Permanence;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PermanenceRepository
    extends Repository<Permanence>
    implements IPermanenceRepository
{
    public PermanenceRepository(Class<Permanence> clas) {
        super(clas);
    }

    @Override
    public List<Permanence> findByLocation(Location location) {
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
