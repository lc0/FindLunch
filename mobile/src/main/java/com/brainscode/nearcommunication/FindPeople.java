package com.brainscode.nearcommunication;

import android.app.Fragment;
import android.content.Intent;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FindPeople extends Fragment {

    NsdHelper mNsdHelper;
    private TextView mStatusView;
    private Handler mUpdateHandler;
    public static final String TAG = "nsdLunchme";
    LunchConnection mConnection;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.find_people, container);
        View view = super.onCreateView(inflater, container, savedInstanceState);


        Button advertiseButton = (Button) container.findViewById(R.id.advertise_btn);
        advertiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Register service
                if(mConnection.getLocalPort() > -1) {
                    mNsdHelper.registerService(mConnection.getLocalPort());
                    Log.d(TAG, "Server is here.");
                } else {
                    Log.d(TAG, "ServerSocket isn't bound.");
                }
            }
        });


        Button discoverButton = (Button) container.findViewById(R.id.discover_btn);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNsdHelper.discoverServices();
            }
        });

        Button connectButton = (Button) container.findViewById(R.id.connect_btn);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
                Log.d("Connect", "beforeconnect." + service);

                if (service != null) {
                    Log.d(TAG, "Connecting.");
                    mConnection.connectToServer(service.getHost(),
                            service.getPort());
                } else {
                    Log.d(TAG, "No service to connect to!");
                }
            }
        });


        mStatusView = (TextView) container.findViewById(R.id.status);
        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String chatLine = msg.getData().getString("msg");
                addChatLine(chatLine);
            }
        };

        return view;

    }

    public void clickSend(View v) {
        EditText messageView = (EditText) v.findViewById(R.id.chatInput);
        if (messageView != null) {
            String messageString = messageView.getText().toString();
            if (!messageString.isEmpty()) {
                mConnection.sendMessage(messageString);
            }
            messageView.setText("");
        }
    }
    public void addChatLine(String line) {
        mStatusView.append("\n" + line);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "Starting.");

        Log.d(TAG, "STAAAART");

        mConnection = new LunchConnection(mUpdateHandler);
        mNsdHelper = new NsdHelper(getActivity().getApplicationContext());
        mNsdHelper.initializeNsd();
        super.onStart();
    }
    @Override
    public void onPause() {
        Log.d(TAG, "Pausing.");
        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }
        super.onPause();
    }
    @Override
    public void onResume() {
        Log.d(TAG, "Resuming.");
        super.onResume();
        if (mNsdHelper != null) {
            mNsdHelper.discoverServices();
        }
    }
    // For KitKat and earlier releases, it is necessary to remove the
    // service registration when the application is stopped.  There's
    // no guarantee that the onDestroy() method will be called (we're
    // killable after onStop() returns) and the NSD service won't remove
    // the registration for us if we're killed.
    // In L and later, NsdService will automatically unregister us when
    // our connection goes away when we're killed, so this step is
    // optional (but recommended).
    @Override
    public void onStop() {
        Log.d(TAG, "Being stopped.");
        mNsdHelper.tearDown();
        mConnection.tearDown();
        mNsdHelper = null;
        mConnection = null;
        super.onStop();
    }
}
