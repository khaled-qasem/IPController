package com.example.khaled.ipcontroller;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    public static WifiManager wifiManager;
    WifiConfiguration wifiConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        Button staticButton = (Button) findViewById(R.id.STATIC);
        Button dhcpButton= (Button) findViewById(R.id.dhcp);
        Button statusButton= (Button) findViewById(R.id.ip_status);
        final TextView resultView = (TextView) findViewById(R.id.result);

        wifiConfiguration = null;
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration conf : configuredNetworks) {
                if (conf.networkId == connectionInfo.getNetworkId()) {
//            if (conf.networkId == netId) {
                wifiConfiguration = conf;
                break;
            }
        }
        Log.d(TAG, "onCreate: get wifi configuration ");

        staticButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "onClick: handle static ip ");
                    IPHandler.setIpAssignment("STATIC", wifiConfiguration); //or "DHCP" for dynamic setting
                    IPHandler.setIpAddress(InetAddress.getByName("192.168.0.10"), 24, wifiConfiguration);
                    IPHandler.setGateway(InetAddress.getByName("192.168.1.1"), wifiConfiguration);
                    IPHandler.setDNS(InetAddress.getByName("8.8.8.8"), wifiConfiguration);

                    int updateResult =wifiManager.updateNetwork(wifiConfiguration); //apply the setting
                    wifiManager.saveConfiguration(); //Save it

                 /*   if (updateResult != -1) {
//                    AndroidLauncher.wifiManager.disconnect();
                        if (wifiManager.isWifiEnabled()) { //---wifi is turned on---
                            //---disconnect it first---
                            wifiManager.disconnect();
                        } else { //---wifi is turned off---
                            //---turn on wifi---
                            wifiManager.setWifiEnabled(true);
                        }
                        wifiManager.saveConfiguration(); //Save it
                        if (wifiConfiguration != null) {
                            wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                        }
//                    // reconnect with the new static IP
                    wifiManager.reconnect();
                    }*/
                } catch (Exception e) {
                    Log.e(TAG, "onClick: Exception while changing ip to static",e );
                }

            }
        });

        dhcpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "onClick: handle dynamic ip ");
                    IPHandler.setIpAssignment("DHCP", wifiConfiguration); //or "DHCP" for dynamic setting
//                    IPHandler.setIpAddress(InetAddress.getByName("192.168.0.10"), 24, wifiConfiguration);
//                    IPHandler.setGateway(InetAddress.getByName("192.168.1.1"), wifiConfiguration);
//                    IPHandler.setDNS(InetAddress.getByName("8.8.8.8"), wifiConfiguration);

                    int updateResult =wifiManager.updateNetwork(wifiConfiguration); //apply the setting
//                    wifiManager.saveConfiguration(); //Save it
                    if (updateResult != -1) {
//                    AndroidLauncher.wifiManager.disconnect();
                        if (wifiManager.isWifiEnabled()) { //---wifi is turned on---
                            //---disconnect it first---
                            wifiManager.disconnect();
                        } else { //---wifi is turned off---
                            //---turn on wifi---
                            wifiManager.setWifiEnabled(true);
                        }
                        wifiManager.saveConfiguration(); //Save it
                        if (wifiConfiguration != null) {
                            wifiManager.enableNetwork(wifiConfiguration.networkId, true);
                        }
//                    // reconnect with the new static IP
//                    wifiManager.reconnect();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onClick: Exception while changing ip to static",e );
                }
            }
        });


        statusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: in status button");
                wifiConfiguration = null;
                WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
                for (WifiConfiguration conf : configuredNetworks) {
                    if (conf.networkId == connectionInfo.getNetworkId()) {
//            if (conf.networkId == netId) {
                        wifiConfiguration = conf;
                        break;
                    }
                }
                try {
                    Log.d(TAG, "onClick: handle dynamic ip ");
                    String result = IPHandler.getIpAssignment(wifiConfiguration);
                    resultView.setText(result);

                } catch (Exception e) {
                    Log.e(TAG, "onClick: Exception while changing ip to static",e );
                }
            }
        });





    }
}

