package ro.pub.cs.systems.eim.practicaltest02v2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest02V2MainActivity extends AppCompatActivity {
    private EditText serverPortEditText = null;
    private EditText clientPortEditText = null;
    private EditText wordEditText = null;
    private TextView wordTextView = null;

    private ServerThread serverThread = null;

    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private final GetWordButtonClickListener getWordButtonClickListener = new GetWordButtonClickListener();
    private class GetWordButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String clientPort = clientPortEditText.getText().toString();

            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }
            String word = wordEditText.getText().toString();
            if (word.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] City should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            wordTextView.setText(" ");

            ClientThread clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), word, wordTextView
            );
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02v2_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port_edit_text);
        Button connectButton = (Button)findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientPortEditText = (EditText)findViewById(R.id.client_port_edit_text);
        wordEditText = (EditText)findViewById(R.id.word_edit_text);
        Button getWordButton = (Button)findViewById(R.id.get_info);
        getWordButton.setOnClickListener(getWordButtonClickListener);
        wordTextView = (TextView)findViewById(R.id.info_view);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}