package com.esdrasmorais.inspetoronline.data;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.esdrasmorais.inspetoronline.R;

public class AddressResultReceiver extends ResultReceiver {
    protected String addressOutput;
    protected String messageResult;

    public AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultData == null) {
            return;
        }

        // Display the address string
        // or an error message sent from the intent service.
        this.addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
        if (addressOutput == null) {
            addressOutput = "";
        }
        //displayAddressOutput();

        // Show a toast message if an address was found.
        if (resultCode == Constants.SUCCESS_RESULT) {
            //showToast(getString(R.string.address_found));
            this.messageResult = "" + R.string.address_found;
        }
    }
}