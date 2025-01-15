package ro.pub.cs.systems.eim.practicaltest02v2;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v2.Constants;

public class ServerThread extends Thread {

    private ServerSocket serverSocket = null;


    public ServerThread(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
        }

    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }




    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            }
        }
    }

}