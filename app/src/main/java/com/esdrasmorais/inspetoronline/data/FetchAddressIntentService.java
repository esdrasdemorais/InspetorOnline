package com.esdrasmorais.inspetoronline.data;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.esdrasmorais.inspetoronline.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    public static final String TAG = "FetchAddressIS";
    protected ResultReceiver receiver;

    public FetchAddressIntentService() {
        super(TAG);
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        receiver.send(resultCode, bundle);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;

        this.receiver = intent.getParcelableExtra(Constants.RECEIVER);

        if (this.receiver == null) {
            Log.wtf(TAG,
            "No receiver received. There is nowhere to send the results.");
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        Location location = intent.getParcelableExtra(
            Constants.LOCATION_DATA_EXTRA
        );

        if (location == null) {
            String errorMessage = getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            return;
        }

        List<Address> addresses = null;
        String errorMessage = "";
        try {
            addresses = geocoder.getFromLocation(
                location.getLatitude(),
                location.getLongitude(),
                1
            );
        } catch (IOException ioException) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                "Latitude = " + location.getLatitude() +
                ", Longitude = " +
                location.getLongitude(), illegalArgumentException);
        }

        if ((addresses == null || addresses.size() == 0)
            && errorMessage.isEmpty()
        ) {
            errorMessage = getString(R.string.no_address_found);
            Log.e(TAG, errorMessage);
        }

        if (!errorMessage.isEmpty()) {
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        }

        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            List<String> addressFragments = new ArrayList<String>();

            for (Integer i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(
                Constants.SUCCESS_RESULT,
                TextUtils.join(System.getProperty("line.separator"),
                addressFragments)
            );
        }
    }
}