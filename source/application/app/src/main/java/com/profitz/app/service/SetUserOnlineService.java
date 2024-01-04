package com.profitz.app.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.ProfitzApp;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;

import org.json.JSONException;
import org.json.JSONObject;

import static com.profitz.app.ProfitzApp.getContext;

public class SetUserOnlineService extends Service {
    Handler handler;
    Runnable setOnline;
    String userId;
    static User user;

    public SetUserOnlineService() {
        handler = new Handler();
        setOnline = new Runnable() {
            @Override
            public void run() {
                user = MyPreferenceManager.getInstance(SetUserOnlineService.this).getUser();
                Context mContext = ProfitzApp.getContext();
                final String url = "https://yoururl.com/api/update_active_user.php?user_id="+userId;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String date = response.getString("last_scene");
                                    user.setLast_scene(date);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error

                            }
                        });
                Volley.newRequestQueue(mContext).add(jsonObjectRequest);

                handler.postDelayed(setOnline, 60000); //100 ms you should do it 4000
            }
        };

        handler.postDelayed(setOnline, 1000);
    }
    @Override
    public int onStartCommand (Intent intent, int flags, int startId) {
       userId = intent.getStringExtra("userId");


        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
