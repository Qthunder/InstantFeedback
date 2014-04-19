package com.InstantFeedback.Lecturer;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import com.InstantFeedback.Library.Variables;

/**
 * Created by Jasiek on 18/04/2014.
 */
public class NsdHelperLecturer {

    private static final String TAG = "NadHelper";
    private NsdManager nsdManager;
    private NsdManager.RegistrationListener registrationListener;
    private String serviceName;

    public NsdHelperLecturer(Context context) {
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);

        initializeRegistrationListener();
    }

    private void initializeRegistrationListener() {
        registrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Registration failed.");
            }

            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {

            }

            @Override
            public void onServiceRegistered(NsdServiceInfo serviceInfo) {
                serviceName = serviceInfo.getServiceName();
                Log.d(TAG, "Service registered.");
            }

            @Override
            public void onServiceUnregistered(NsdServiceInfo serviceInfo) {

            }
        };
    }

    public void registerService(int port) {
        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(Variables.SERVICE_NAME);
        serviceInfo.setServiceType(Variables.SERVICE_TYPE);

        nsdManager.registerService(serviceInfo,NsdManager.PROTOCOL_DNS_SD, registrationListener);
    }

    public void endService() {
        nsdManager.unregisterService(registrationListener);
    }
}
