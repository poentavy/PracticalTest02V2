package ro.pub.cs.systems.eim.practicaltest02v2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import ro.pub.cs.systems.eim.practicaltest02v2.Constants;
import ro.pub.cs.systems.eim.practicaltest02v2.Utilities;
import ro.pub.cs.systems.eim.practicaltest02v2.WordInfo;

public class CommunicationThread extends Thread {
    private final ServerThread serverThread;
    private final Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (word!");
            String word = bufferedReader.readLine();

            if (word == null || word.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (city / information type!");
                return;
            }

            WordInfo wordInfo;
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
            String url = Constants.WEB_SERVICE_ADDRESS + word;
            URL urlAddress = new URL(url);
            URLConnection urlConnection = urlAddress.openConnection();
            BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String pageSourceCode;
            StringBuilder stringBuilder = new StringBuilder();
            String currentLine;
            while ((currentLine = bufferedReader1.readLine()) != null) {
                stringBuilder.append(currentLine);
            }
            bufferedReader1.close();
            pageSourceCode = stringBuilder.toString();
            JSONArray contentArray = new JSONArray(pageSourceCode);
            JSONObject content = contentArray.getJSONObject(0);
            JSONArray meaningsArray = content.getJSONArray("meanings");
            JSONObject meaningsInfo = meaningsArray.getJSONObject(0);
            JSONArray definitionsArray = meaningsInfo.getJSONArray("definitions");
            JSONObject definition = definitionsArray.getJSONObject(0);
            String result = definition.getString("definition");

            wordInfo = new WordInfo(result);

            if (wordInfo == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Weather Forecast Information is null!");
                return;
            }
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + e.getMessage());
            }
        }
    }

}
