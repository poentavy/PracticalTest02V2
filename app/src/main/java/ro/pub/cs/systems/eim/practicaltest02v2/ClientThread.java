package ro.pub.cs.systems.eim.practicaltest02v2;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import ro.pub.cs.systems.eim.practicaltest02v2.Utilities;

public class ClientThread extends Thread{
    private final String address;
    private final int port;
    private final String word;
    private final TextView infoTextView;

    private Socket socket;

    public ClientThread(String address, int port, String word, TextView infoTextView) {
        this.address = address;
        this.port = port;
        this.word = word;
        this.infoTextView = infoTextView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            printWriter.println(word);
            printWriter.flush();
//            String wordInformation;
            final String finalizedWordInformation = bufferedReader.readLine();
            infoTextView.post(() -> infoTextView.setText(finalizedWordInformation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
