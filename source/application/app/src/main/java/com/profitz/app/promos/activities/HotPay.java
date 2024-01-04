package com.profitz.app.promos.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.squareup.seismic.ShakeDetector;

import org.json.JSONException;
import org.json.JSONObject;

public class HotPay extends AppCompatActivity implements ShakeDetector.Listener, BottomSheetDialogReportBug.BottomSheetListener  {

    private WebView webView;
    static Context mContext;
    public BottomSheetDialogReportBug report_dialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        mContext = this;
        Intent intent = getIntent();
        String SEKRET = intent.getStringExtra("SEKRET");
        String KWOTA = intent.getStringExtra("KWOTA");
        String NAZWA_USLUGI = intent.getStringExtra("NAZWA_USLUGI");
        String ADRES_WWW = intent.getStringExtra("ADRES_WWW");
        String ID_ZAMOWIENIA = intent.getStringExtra("ID_ZAMOWIENIA");

        User user = MyPreferenceManager.getInstance(this).getUser();
        final String url = "https://yoururl.com/api/send_payment_data_to_database.php?user_id="+user.getId();
        /////Shake to report bug
        report_dialog = new BottomSheetDialogReportBug();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        int sensorDelay = SensorManager.SENSOR_DELAY_GAME;
        sd.start(sensorManager, sensorDelay);

        //////
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String response_payment = response.getString("response");

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


        webView = (WebView) findViewById(R.id.webView1);
        String html = "<!DOCTYPE html>" +
                "<html>" +
                "<body onload='document.frm1.submit()'>" +
                "<form id='order' action='https://platnosc.hotpay.pl/' method='post' name='frm1'>" +
                "  <input required type='hidden' name='SEKRET' value='"+SEKRET+"'><br>" +
                "  <input required type='hidden' name='KWOTA' value='"+KWOTA+"'><br>" +
                "  <input required type='hidden' name='NAZWA_USLUGI' value='"+NAZWA_USLUGI+"'><br>" +
                "  <input required type='hidden' name='ADRES_WWW' value='"+ADRES_WWW+"'><br>" +
                "  <input required type='hidden' name='ID_ZAMOWIENIA' value='"+ID_ZAMOWIENIA+"'><br>" +
                "  <input required type='hidden' name='EMAIL' value=''><br>" +
                "  <input required type='hidden' name='DANE_OSOBOWE' value=''><br>" +
                "</form>" +
                "</body>" +
                "</html>";


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");
       // webView.loadUrl("http://www.google.com");

    }
    @Override public void hearShake() {
       // report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
    }

    @Override
    public void onButtonClicked(String text) {

    }
}