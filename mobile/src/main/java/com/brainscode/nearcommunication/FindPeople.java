package com.brainscode.nearcommunication;

import android.app.Fragment;
import android.content.Context;
import android.net.nsd.NsdServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class FindPeople extends Fragment{

    ArrayList<Bros> bros = new ArrayList<>();
    NsdHelper mNsdHelper;
    private TextView mStatusView;
    private Handler mUpdateHandler;

    public static final String TAG = "nsdLunchme";
    LunchConnection mConnection;
    ListView brosLV;

    ServerService selectedServer;

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return bros.size();
        }

        @Override
        public Object getItem(int position) {
            return bros.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.bro_list_element, parent, false);
            TextView name = (TextView) rowView.findViewById(R.id.name);
            TextView up = (TextView) rowView.findViewById(R.id.upForIt);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.photo);

            name.setText(bros.get(position).getHost());
            up.setText("Hell YEAH");
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.cook));

            return rowView;
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.find_people, container);

        NsdHelper.gimmeBrosListener = new NsdHelper.gimmeBrosListener() {
            @Override
            public void onBroFound(Bros bro) {
                if (!bros.contains(bro)) {
                    Log.d("Adding", "inside" + bros.contains(bro));
                    bros.add(bro);
                    showBros();
                }
            }
        };

        View view = super.onCreateView(inflater, container, savedInstanceState);

        brosLV = (ListView) container.findViewById(R.id.broContainer);
        brosLV.setAdapter(adapter);
        Button advertiseButton = (Button) container.findViewById(R.id.advertise_btn);
        advertiseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Register service
                if (mConnection.getLocalPort() > -1) {
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
                InetAddress host = null;
                int port = 0;

                if (selectedServer == null) {
                    NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();

                    host = service.getHost();
                    port = service.getPort();
                }
                else {
                    try {
                        host = InetAddress.getByName(selectedServer.getHost());
                        port = selectedServer.getPort();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }

                }


                if (host != null) {
                    Log.d(TAG, "talking Connecting.");
                    mConnection.connectToServer(host, port);
                    mConnection.updateSystemMessages("Connected to new lunch crew: " + host);
                } else {
                    Log.d(TAG, "No service to connect to!");
                }
            }
        });


        Button sendButton = (Button) container.findViewById(R.id.send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Connect", "talking sending here");
                EditText messageView = (EditText) container.findViewById(R.id.chatInput);
                if (messageView != null) {

                    String messageString = messageView.getText().toString();
                    if (!messageString.isEmpty()) {
                        mConnection.sendMessage(messageString);
                        Log.d("Connect", "talking sending inside" + messageString);
                    }
                    messageView.setText("");
                }
            }
        });


        mStatusView = (TextView) container.findViewById(R.id.status);
        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("Connect", "talking: adding line to the chatline");

                String chatLine = msg.getData().getString("msg");
                addChatLine(chatLine);
            }
        };

        brosLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                ((ListView) parent).clearChoices();
                view.setSelected(true);

                Log.d("Selected", "Selected new server" + bros.get(position));
                selectedServer = new ServerService(bros.get(position).getHost(),
                        bros.get(position).getPort());


            }
        });



        return view;

    }

    public void showBros() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                brosLV.setAdapter(adapter);
            }
        });
    }

    public void addChatLine(String line) {
        mStatusView.append("\n" + line);
        Log.d(TAG, "Talking adding text here." + mStatusView.getText());
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
