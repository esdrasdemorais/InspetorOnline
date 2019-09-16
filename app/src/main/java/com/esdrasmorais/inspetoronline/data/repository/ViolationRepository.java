package com.esdrasmorais.inspetoronline.data.repository;

import android.location.Location;

import com.esdrasmorais.inspetoronline.data.model.Violation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ViolationRepository
    extends Repository<Violation>
    implements IViolationRepository
{
    @Override
    public List<Violation> findByLocation(Location location) {
        db.collectionGroup(clas.getName())
            .whereEqualTo("address", location).get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      return;
                  }
            });
        return objects;
    }
}
