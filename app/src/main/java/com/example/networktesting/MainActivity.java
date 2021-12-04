package com.example.networktesting;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    private TextView displayTxt;
    private StringBuilder stringBuilder = new StringBuilder("");

    private void getWlanLocalAddresses() {
        try {
            NetworkInterface inf = NetworkInterface.getByName("wlan0");
            if (inf.isUp()) {
                Enumeration<InetAddress> inetAddressEnumeration = inf.getInetAddresses();
                for (InetAddress inetAdd : Collections.list(inetAddressEnumeration)) {
                    stringBuilder.append(inetAdd.getHostAddress() + " \n");
                }
            }
            displayTxt.setText(stringBuilder.toString());
        } catch (SocketException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            displayTxt.setText(e.getMessage());
        }
    }

    private void getLocalAddresses() {
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();

            for (NetworkInterface networkInterface : Collections.list(nets)) {

                Log.d("test", "Name of interface : " + networkInterface.getDisplayName());
                if (networkInterface.isUp()) {
                    Log.d("test", "Hardware addr : " + networkInterface.getHardwareAddress());
                }

                /*
                Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
                for(InetAddress inetAddress : Collections.list(inetAddressEnumeration)){
                    Log.d("test",inetAddress.getHostAddress());
                }*/

                for (InterfaceAddress inf : networkInterface.getInterfaceAddresses()) {
                    Log.d("test", "Address " + inf.getAddress().getHostAddress());
                }
            }
        } catch (SocketException e) {
            Log.d("test", e.getMessage());
        }
    }

    private boolean checkInternetConnection() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            if (address.getHostAddress() != null && address.getHostAddress().length() != 0) {
                toast(getApplicationContext(), "Internet connection connected");
                return true;
            }
        } catch (Exception e) {
            toast(getApplicationContext(), "Not have internet connection");
        }
        return false;
    }

    public void toast(final Context context, final String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class Runnable implements java.lang.Runnable {
        @Override
        public void run() {
            if (checkInternetConnection()) {
               getWlanLocalAddresses();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayTxt = findViewById(R.id.display_txt);

        new Thread(new Runnable()).start();

    }
}