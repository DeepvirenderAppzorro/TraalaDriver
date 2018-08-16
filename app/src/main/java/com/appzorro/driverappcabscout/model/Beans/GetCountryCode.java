package com.appzorro.driverappcabscout.model.Beans;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.appzorro.driverappcabscout.R;

/**
 * Created by vijay on 1/8/18.
 */

public class GetCountryCode {
    public static GetCountryCode mInstance;

    public GetCountryCode() {
        mInstance = this;
    }

    public static GetCountryCode getmInstance() {

        return mInstance;
    }

    public static String GetCountryZipCode(Context context) {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return CountryZipCode;
    }
}
