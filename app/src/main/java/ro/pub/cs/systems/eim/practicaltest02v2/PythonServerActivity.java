package ro.pub.cs.systems.eim.practicaltest02v2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PythonServerActivity extends AppCompatActivity {

    private TextView timeTextView;
    private Button connectToServerButton;
    private RealTimeClientThread realTimeClientThread;
    private static final String SERVER_IP = "172.30.80.1"; // Adresa serverului
    private static final int SERVER_PORT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_python_server);

        timeTextView = findViewById(R.id.time_text_view);
        connectToServerButton = findViewById(R.id.connect_to_server_button);

        connectToServerButton.setOnClickListener(v -> {
            if (realTimeClientThread == null || !realTimeClientThread.isAlive()) {
                realTimeClientThread = new RealTimeClientThread();
                realTimeClientThread.start();
                Toast.makeText(PythonServerActivity.this, "Conectare începută...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realTimeClientThread != null) {
            realTimeClientThread.stopThread();
        }
    }

    private class RealTimeClientThread extends Thread {
        private boolean isRunning = true;

        public void stopThread() {
            isRunning = false;
        }

        @Override
        public void run() {
            while (isRunning) {
                try {
                    // Conectare la server
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);

                    // Citire răspuns de la server
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    final String time = in.readLine();

                    // Actualizare interfață grafică
                    runOnUiThread(() -> timeTextView.setText("Ora primită: " + time));

                    // Închidere conexiune
                    socket.close();

                    // Așteaptă 1 secundă înainte de următoarea cerere
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(PythonServerActivity.this, "Eroare la conectare!", Toast.LENGTH_SHORT).show());
                    isRunning = false; // Întrerupe thread-ul în caz de eroare
                }
            }
        }
    }
}
