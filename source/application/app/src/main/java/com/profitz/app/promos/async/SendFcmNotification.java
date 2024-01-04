package com.profitz.app.promos.async;

import android.os.AsyncTask;


import com.profitz.app.data.model.MessageModel;
import com.profitz.app.util.Utility;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendFcmNotification extends AsyncTask<Void, Void, Void> {
    private final MessageModel messageModel;
    private final Runnable successCallBack;
    private final String receiverToken;

    public SendFcmNotification(String receiverToken, MessageModel messageModel, Runnable successCallBack) {
        this.messageModel = messageModel;
        this.receiverToken = receiverToken;
        this.successCallBack = successCallBack;
    }

    @Override
    protected Void doInBackground(Void... params) {
        // TODO: Enable this for production
        String authKey = Utility.FCM_SERVER_KEY;
        String FMCurl = Utility.FCM_URL;

        URL url = null;
        try {
            url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject data = new JSONObject();
            data.put("to", receiverToken);
            JSONObject info = new JSONObject();

            info.put("data", new Gson().toJson(messageModel));
            data.put("data", info);
            data.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void feed) {
        if (successCallBack != null) {
            successCallBack.run();
        }
    }
}
