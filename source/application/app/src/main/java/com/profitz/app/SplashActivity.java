package com.profitz.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PromosActivity;
import com.profitz.app.promos.activities.WelcomeActivity;


public class SplashActivity extends AppCompatActivity {
    Intent intent;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.bg_Profitz));

        setContentView(R.layout.splashfile);

        startHeavyProcessing();

    }


    private void startHeavyProcessing(){
        new LongOperation().execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            //some heavy processing resulting in a Data String
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "whatever result you have";
        }

        @Override
        protected void onPostExecute(String result) {
            MyPreferenceManager myPreferenceManager = MyPreferenceManager.getInstance(SplashActivity.this);
            if (myPreferenceManager.isLoggedIn()) {
                intent  = new Intent(SplashActivity.this, PromosActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            }
            intent.putExtra("data", result);
            startActivity(intent);
            finish();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
