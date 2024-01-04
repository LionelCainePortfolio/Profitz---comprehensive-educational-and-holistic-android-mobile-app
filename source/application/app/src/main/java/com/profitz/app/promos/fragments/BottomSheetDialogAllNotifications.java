package com.profitz.app.promos.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.AdapterNotification;
import com.profitz.app.promos.data.DataNotification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class BottomSheetDialogAllNotifications extends SuperBottomSheetFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    private RecyclerView mRVNotificationList;
    private AdapterNotification mAdapter;
    RecyclerView NotificationList;
    int userId;
    String notifType;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_notifications_all, container, false);
        NotificationList = v.findViewById(R.id.NotificationListAll);
        mRVNotificationList = v.findViewById(R.id.NotificationListAll);
        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        userId = Integer.parseInt(user2.getId());
        Bundle mArgs = getArguments();
        notifType = mArgs.getString("notifType");
        new AsyncNotificationsAll().execute();
        //  dismiss(); - zamyka modal

        return v;
    }

    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement BottomSheetListener");
        }
    }
    class AsyncNotificationsAll extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;

        LayoutInflater factory = LayoutInflater.from(getActivity());
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(getActivity());


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //gif_loader.setVisibility(View.VISIBLE);
            main_dialog.setContentView(DialogView);
            main_dialog.setCancelable(false);
            main_dialog.setCanceledOnTouchOutside(false);

            main_dialog.show();
            //main_dialog.getWindow().setLayout(300, 300);
            main_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //this method will be running on UI thread
            //pdLoading.setMessage("\tŁadowanie...");
            //   pdLoading.setCancelable(false);
            //   pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {



            try {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                User user = MyPreferenceManager.getInstance(getActivity()).getUser();
                url = new URL("https://yoururl.com/api/get_notifications.php?user_id="+userId+"&limit=no&notification_type="+notifType);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {
                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            // gif_loader.setVisibility(View.GONE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    main_dialog.dismiss();
                    handler.postDelayed(this, 1500);
                }
            }, 1000);


            // pdLoading.dismiss();
            List<DataNotification> data=new ArrayList<>();
            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataNotification NotificationData = new DataNotification();
                    NotificationData.notificationId= json_data.getInt("notification_id");
                    NotificationData.notificationTitle= json_data.getString("notification_title");
                    NotificationData.notificationDescription= json_data.getString("notification_description");
                    NotificationData.notificationImage= json_data.getString("notification_img");
                    NotificationData.notificationEarn= json_data.getString("notification_earn");
                    NotificationData.notificationEarnType= json_data.getString("notification_earn_type");
                    NotificationData.notificationDateAdd= json_data.getString("notification_date_add");
                    data.add(NotificationData);
                }

                // Setup and Handover data to recyclerview
                //  mRVNotificationList = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterNotification(getActivity(), data);
                mRVNotificationList.setAdapter(mAdapter);
                mRVNotificationList.setLayoutManager(new GridLayoutManager(getActivity(),1));

            } catch (JSONException e) {
               // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(mContext, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

            }

        }

    }

}
