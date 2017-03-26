package com.android.boles.roommate;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by boles on 2/6/2017.
 */

public class WiFiActivity extends AppCompatActivity
{
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mTextView = (TextView) findViewById(R.id.wifi_text_view);

        if (checkConnectedToDesiredWifi()) {
            mTextView.setText("WIFI");
        }
    }

    private boolean checkConnectedToDesiredWifi() {
        boolean connected = false;

        String desiredMacAddress = "a0:63:91:b8:44:e7";
        Context context = getApplicationContext();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifi = wifiManager.getConnectionInfo();
        if (wifi != null) {
            // get current router Mac address
            String bssid = wifi.getBSSID();
            connected = desiredMacAddress.equals(bssid);
        }

        return connected;
    }
}
