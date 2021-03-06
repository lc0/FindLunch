package com.brainscode.nearcommunication;

/**
 * Created by khomenkos on 18/09/15.
 */

import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.net.nsd.NsdManager;
import android.util.Log;

public class NsdHelper {
    Context mContext;
    NsdManager mNsdManager;
    NsdManager.ResolveListener mResolveListener;
    NsdManager.DiscoveryListener mDiscoveryListener;
    NsdManager.RegistrationListener mRegistrationListener;
    public static final String SERVICE_TYPE = "_http._tcp.";
    public static final String TAG = "NsdHelper";
    public String mServiceName = "nsdLunchme";
    NsdServiceInfo mService;

    public void setGimmeBrosListener(NsdHelper.gimmeBrosListener gimmeBrosListener) {
        this.gimmeBrosListener = gimmeBrosListener;
    }

    public static gimmeBrosListener gimmeBrosListener;

    public interface gimmeBrosListener{
        void onBroFound(Bros bro);
    }
    public NsdHelper(Context context) {
        mContext = context;
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    }
    public void initializeNsd() {
        initializeResolveListener();
        //mNsdManager.init(mContext.getMainLooper(), this);
    }
    public void initializeDiscoveryListener() {
        Log.d(TAG, "initializeDiscoveryListener" + mDiscoveryListener);

        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }
            @Override
            public void onServiceFound(NsdServiceInfo service) {

                Log.d(TAG, "Service discovery success" + service);
                if (!service.getServiceType().equals(SERVICE_TYPE)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
//                } else if (service.getServiceName().equals(mServiceName)) {
//                    Log.d(TAG, "Bros: Same machine: " + mServiceName + service);
                } else if (service.getServiceName().contains(mServiceName)){

                    try {
                        Log.d(TAG, "Bros: somebody is here: " + mServiceName + service);
                        mNsdManager.resolveService(service, mResolveListener);
                    }
                    catch (IllegalArgumentException ex) {
                        Log.e(TAG, "Can't resolve - listener already in use");
                    }
                }
            }
            @Override
            public void onServiceLost(NsdServiceInfo service) {
//                Log.e(TAG, "service lost" + service);
                if (mService == service) {
                    mService = null;
                }
            }
            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }
            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            }
            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            }
        };
    }
    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }
            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
                if (serviceInfo.getServiceName().equals(mServiceName)) {

                    Bros bro = new Bros(mServiceName, "Dev", true,
                            serviceInfo.getHost().toString(), serviceInfo.getPort());
                    if(gimmeBrosListener!=null){
                        gimmeBrosListener.onBroFound(bro);
                    }

//                    Log.d(TAG, "Same IP.");
//                    return;
                }
                mService = serviceInfo;
            }
        };
    }
    public void initializeRegistrationListener() {
        mRegistrationListener = new NsdManager.RegistrationListener() {
            @Override
            public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
//                mService = NsdServiceInfo;
                Log.d(TAG, "nsd Service: " + mServiceName);
                mServiceName = NsdServiceInfo.getServiceName();
                Log.d(TAG, "Service registered: " + mServiceName);
            }
            @Override
            public void onRegistrationFailed(NsdServiceInfo arg0, int arg1) {
                Log.d(TAG, "Service registration failed: " + arg1);
            }
            @Override
            public void onServiceUnregistered(NsdServiceInfo arg0) {
                Log.d(TAG, "Service unregistered: " + arg0.getServiceName());
            }
            @Override
            public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.d(TAG, "Service unregistration failed: " + errorCode);
            }
        };
    }
    public void registerService(int port) {
        Log.d(TAG, "Listening - 0");
        tearDown();  // Cancel any previous registration request
        initializeRegistrationListener();
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        Log.d(TAG, "Listening - 1");
        serviceInfo.setPort(port);
        serviceInfo.setServiceName(mServiceName);
        serviceInfo.setServiceType(SERVICE_TYPE);
        Log.d(TAG, "Listening - 3");
        mNsdManager.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
        Log.d(TAG, "Listening - 4");
    }
    public void discoverServices() {
        stopDiscovery();  // Cancel any existing discovery request
        initializeDiscoveryListener();
        mNsdManager.discoverServices(
                SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
    }
    public void stopDiscovery() {
        if (mDiscoveryListener != null) {
            try {
                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            } finally {
            }
            mDiscoveryListener = null;
        }
    }
    public NsdServiceInfo getChosenServiceInfo() {
        return mService;
    }
    public void tearDown() {
        if (mRegistrationListener != null) {
            try {
                mNsdManager.unregisterService(mRegistrationListener);
            } finally {
            }
            mRegistrationListener = null;
        }
    }
}
