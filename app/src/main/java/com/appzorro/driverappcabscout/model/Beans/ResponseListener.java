/*
package com.appzorro.driverappcabscout.model.Beans;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

*/
/**
 * Created by vijay on 4/8/18.
 *//*


public class ResponseListener implements DialogListener {
    Bitmap bitmap;
    String message;

    public ResponseListener(Bitmap bitmap, String message) {
        this.bitmap = bitmap;
        this.message = message;
    }

    @Override
    public void onComplete(final Bundle values) {
        try {
            socialAuthAdapter.uploadImageAsync(message, "The AppGuruz.png",
                    bitmap, 100, new UploadImageListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(SocialAuthError error) {
        if (pd != null && pd.isShowing())
            Log.d("ShareTwitter", "Authentication Error: " + error.getMessage());
    }

    @Override
    public void onCancel() {
        Log.d("ShareTwitter", "Authentication Cancelled");
    }

    @Override
    public void onBack() {
        Log.d("ShareTwitter", "Dialog Closed by pressing Back Key");
    }
}*/
