package com.profitz.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;

import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;
import com.google.firebase.appcheck.safetynet.SafetyNetAppCheckProviderFactory;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.profitz.app.data.model.Reply;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.service.SetUserOnlineService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class ProfitzApp extends Application {
    public static final String BASE_URL = "https://yoururl.com/api/";
    public static final String TAG = ProfitzApp.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private AppCompatActivity activity;
    private MyPreferenceManager pref;
    private static ProfitzApp singleton;
    static User user;
    public ArrayList<Reply> mGlobalVarValue = new ArrayList<>();
    public void setGlobalVarValue(ArrayList<Reply> str) {
        mGlobalVarValue = str;
    }


    public ArrayList<Reply> getGlobalVarValue() {

        return mGlobalVarValue;
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }




    @Override
    public void onCreate() {
        super.onCreate();
        setupActivityListener();

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
        /*FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:619431596886:android:908adbe04eabd6512a7cf7") // Required for Analytics.
                .setProjectId("619431596886") // Required for Firebase Installations.
                .setApiKey("AIzaSyBqBxkzRTIcnQHmoT7MFlG15CL8j8Y8U7U") // Required for Auth.
                .build();

        FirebaseApp.initializeApp(this, options, "com.profitz.app.");*/
        //Customerly.configure(this, "f277ca2d", this.getResources().getColor(R.color.colorPrimaryMid));
        singleton = this;
       FirebaseApp.initializeApp(getApplicationContext());
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
         activity = getActivity(this);



    }





    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain,
                                       String authType) throws CertificateException {
        }
    }};

    HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };



    private void setupActivityListener() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                MyPreferenceManager myPreferenceManager = MyPreferenceManager.getInstance(ProfitzApp.this);
                if (myPreferenceManager.isLoggedIn()) {
                    user = MyPreferenceManager.getInstance(ProfitzApp.this).getUser();
                    int premium_status = user.getPremium();
                    final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();

                    Context mContext = getContext();
                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                            (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {

                                        Intent serviceIntent = new Intent(mContext,SetUserOnlineService.class);
                                        serviceIntent.putExtra("userId", user.getId());
                                        mContext.startService(serviceIntent);
                                        String premium_status_from_api = response.getString("premium");

                                       /* if (premium_status_from_api.equals("0"))
                                        {
                                            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
                                        }
                                        else{
                                            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);

                                        }
                                        */

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
                    Volley.newRequestQueue(mContext).add(jsonObjectRequest2);



                }
            }
            @Override
            public void onActivityStarted(Activity activity) {

            }
            @Override
            public void onActivityResumed(Activity activity) {


            }
            @Override
            public void onActivityPaused(Activity activity) {

            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }
public static Context getContext(){
        return  singleton.getApplicationContext();
}


    public static ProfitzApp getInstance() {
        return singleton;
    }

    public static boolean isNetowrk() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager)singleton.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
    public AppCompatActivity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof AppCompatActivity)
            {
                return (AppCompatActivity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }




}