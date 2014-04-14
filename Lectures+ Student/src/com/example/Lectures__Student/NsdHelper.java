package com.example.Lectures__Student;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;
import com.example.Lectures__Library.StudentID;

import java.util.ArrayList;

/**
 * Created by Jasiek on 14/04/2014.
 */
public class NsdHelper {

    private static final String TAG = "NsdHelper";
    private static final String SERVICE_TYPE = "LectureBroadcast";
    private NsdManager nsdManager;
    private NsdManager.DiscoveryListener discoveryListener;
    private ArrayList<Lecture> availableLectures;
    private StudentID studentID;
    private NsdManager.ResolveListener resolveListener;
    private NsdServiceInfo service;

    public NsdHelper(Context context) {
        nsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
        availableLectures = new ArrayList<Lecture>();
        initializeDiscoveryListener();
    }

    public NsdServiceInfo getService() {
        return service;
    }

    public ArrayList<Lecture> getListOfLectures() {
        //TODO - Improvement: I think it'll be better to have available Lectures be fetched by main activity periodically, rather than passed in as a batch - it'll make things smoother and more reliable. Jan
        availableLectures.clear();
        discoverServices();
        return availableLectures;
    }

    private void discoverServices() {
        if (discoveryListener != null) nsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    private void initializeDiscoveryListener() {
        //Instantiates a new DiscoveryListener
        discoveryListener = new NsdManager.DiscoveryListener() {

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error Code: " + errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error Code: " + errorCode);
            }

            @Override
            public void onDiscoveryStarted(String serviceType) {
                Log.d(TAG, "Service discovery started.");
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo serviceInfo) {
                if (serviceInfo.getServiceName().equals(SERVICE_TYPE)) {
                    availableLectures.add(new Lecture(serviceInfo));
                    Log.d(TAG, "Discovered service: " + serviceInfo);
                } else {
                    Log.d(TAG, "Unknown serviceType: " + serviceInfo.getServiceType());
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo serviceInfo) {
                Log.e(TAG, "Service lost: " + serviceInfo);
            }
        };
    }

    private void initializeResolveListener() {
        //Instantiates a new ResolveListener
        resolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "Resolving lecture failed: " + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Resolve succeeded. " + serviceInfo);

                service = serviceInfo;
            }
        };
    }

    public void chooseLecture(Lecture lecture, StudentID id) {
        studentID = id;
        nsdManager.resolveService(lecture.getLectureInfo(), resolveListener);
    }
}
