package com.brainscode.nearcommunication;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LunchConnection {
    private Handler mUpdateHandler;
    private LunchServer mLunchServer;
    private LunchClient mLunchClient;
    private static final String TAG = "LunchConnection";
    private Socket mSocket;
    private int mPort = -1;
    public LunchConnection(Handler handler) {
        mUpdateHandler = handler;
        mLunchServer = new LunchServer(handler);
    }
    public void tearDown() {
        mLunchServer.tearDown();
        if (mLunchClient != null) {
            mLunchClient.tearDown();
        }
    }
    public void connectToServer(InetAddress address, int port) {
        mLunchClient = new LunchClient(address, port);
        Log.d("Listening", "the king is dead long live the king: " + mLunchClient);
    }
    public void sendMessage(String msg) {
        if (mLunchClient != null) {
            mLunchClient.sendMessage(msg);
        }
    }
    public int getLocalPort() {
        return mPort;
    }
    public void setLocalPort(int port) {
        mPort = port;
    }
    public synchronized void updateMessages(String msg, boolean local) {
        Log.d(TAG, "Updating message: " + msg);
        if (local) {
            msg = "me: " + msg;
        } else {
            msg = "them: " + msg;
        }
        Bundle messageBundle = new Bundle();
        messageBundle.putString("msg", msg);
        Message message = new Message();
        message.setData(messageBundle);
        mUpdateHandler.sendMessage(message);
    }
    public synchronized void updateSystemMessages(String msg) {
        msg = "System: " + msg;

        Bundle messageBundle = new Bundle();
        messageBundle.putString("msg", msg);
        Message message = new Message();
        message.setData(messageBundle);
        mUpdateHandler.sendMessage(message);
    }
    private synchronized void setSocket(Socket socket) {
        Log.d(TAG, "setSocket being called.");
        if (socket == null) {
            Log.d(TAG, "Setting a null socket.");
        }
        if (mSocket != null) {
            if (mSocket.isConnected()) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    // TODO(alexlucas): Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        mSocket = socket;
    }
    private Socket getSocket() {
        return mSocket;
    }
    private class LunchServer {
        ServerSocket mServerSocket = null;
        Thread mThread = null;
        public LunchServer(Handler handler) {
            mThread = new Thread(new ServerThread());
            mThread.start();
        }
        public void tearDown() {
            Log.e(TAG, "Listening: tear down - closing mServerSocket");
            mThread.interrupt();
            try {
                mServerSocket.close();
            } catch (IOException ioe) {
                Log.e(TAG, "Error when closing server socket.");
            }
        }
        class ServerThread implements Runnable {
            @Override
            public void run() {
                try {
                    // Since discovery will happen via Nsd, we don't need to care which port is
                    // used.  Just grab an available one  and advertise it via Nsd.
                    mServerSocket = new ServerSocket(0);
                    setLocalPort(mServerSocket.getLocalPort());
                    while (!Thread.currentThread().isInterrupted()) {
                        Log.d(TAG, "Listening: ServerSocket Created, awaiting connection" + mServerSocket);
                        Log.d(TAG, "Listening: I'm waiting here: " + mServerSocket.getLocalPort());

                        setSocket(mServerSocket.accept());
                        updateSystemMessages("New member has connected: " + mSocket.getInetAddress());
                        Log.d(TAG, "Listening: Connected.");
                        // TODO: multiple threads for every new client?
//                        if (mLunchClient == null) {
                            int port = mSocket.getPort();
                            InetAddress address = mSocket.getInetAddress();
                            connectToServer(address, port);
//                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Listening: Error creating ServerSocket: ", e);
                    e.printStackTrace();
                }
            }
        }
    }
    private class LunchClient {
        private InetAddress mAddress;
        private int PORT;
        private final String CLIENT_TAG = "LunchClient";
        private Thread mSendThread;
        private Thread mRecThread;
        public LunchClient(InetAddress address, int port) {
            Log.d(CLIENT_TAG, "Creating lunchClient");
            this.mAddress = address;
            this.PORT = port;
            mSendThread = new Thread(new SendingThread());
            mSendThread.start();
        }
        class SendingThread implements Runnable {
            BlockingQueue<String> mMessageQueue;
            private int QUEUE_CAPACITY = 10;
            public SendingThread() {
                mMessageQueue = new ArrayBlockingQueue<String>(QUEUE_CAPACITY);
            }
            @Override
            public void run() {
                try {
                    if (getSocket() == null) {
                        setSocket(new Socket(mAddress, PORT));
                        Log.d(CLIENT_TAG, "Client-side socket initialized.");
                    } else {
                        Log.d(CLIENT_TAG, "Socket already initialized. skipping!");
                    }
                    mRecThread = new Thread(new ReceivingThread());
                    mRecThread.start();
                } catch (UnknownHostException e) {
                    Log.d(CLIENT_TAG, "Initializing socket failed, UHE", e);
                } catch (IOException e) {
                    Log.d(CLIENT_TAG, "Initializing socket failed, IOE.", e);
                }
                while (true) {
                    try {
                        String msg = mMessageQueue.take();
                        sendMessage(msg);
                    } catch (InterruptedException ie) {
                        Log.d(CLIENT_TAG, "Message sending loop interrupted, exiting");
                    }
                }
            }
        }
        class ReceivingThread implements Runnable {
            @Override
            public void run() {
                BufferedReader input;
                try {
                    Log.d(CLIENT_TAG, "The Listening: here is socket: " + mSocket);
                    input = new BufferedReader(new InputStreamReader(
                            mSocket.getInputStream()));
                    while (!Thread.currentThread().isInterrupted()) {
                        String messageStr = null;
                        messageStr = input.readLine();
                        if (messageStr != null) {
                            Log.d(CLIENT_TAG, "Read from the stream: " + messageStr);
                            updateMessages(messageStr, false);
                        } else {
                            Log.d(CLIENT_TAG, "The nulls!1 The nulls!");
                            break;
                        }
                    }
                    input.close();
                } catch (IOException e) {
                    Log.e(CLIENT_TAG, "Server loop error: ", e);
                }
            }
        }
        public void tearDown() {
            try {
                getSocket().close();
            } catch (IOException ioe) {
                Log.e(CLIENT_TAG, "Error when closing server socket.");
            }
        }
        public void sendMessage(String msg) {
            try {
                Socket socket = getSocket();
                if (socket == null) {
                    Log.d(CLIENT_TAG, "Socket is null, wtf?");
                } else if (socket.getOutputStream() == null) {
                    Log.d(CLIENT_TAG, "Socket output stream is null, wtf?");
                }
                PrintWriter out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(getSocket().getOutputStream())), true);
                out.println(msg);
                out.flush();
                updateMessages(msg, true);
            } catch (UnknownHostException e) {
                Log.d(CLIENT_TAG, "Unknown Host", e);
            } catch (IOException e) {
                Log.d(CLIENT_TAG, "I/O Exception", e);
            } catch (Exception e) {
                Log.d(CLIENT_TAG, "Error3", e);
            }
            Log.d(CLIENT_TAG, "Client sent message: " + msg);
        }
    }
}
