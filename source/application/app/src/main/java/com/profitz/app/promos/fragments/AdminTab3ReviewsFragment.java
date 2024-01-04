package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.IMethodCaller;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.AdapterNotificationAll;
import com.profitz.app.promos.data.DataNotification;
import com.profitz.app.util.SwipeToDeleteCallback;
import com.profitz.app.util.SwipeableWrapContentViewPager;

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
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AdminTab3ReviewsFragment extends Fragment implements IMethodCaller {
    String username;
    private Context mContext;
    private String data_getNotificationCount;
    private String data_getPoints;
    private String data_getEarned;
    private String data_getNickname;
    private String data_getLevel;
    private String data_getImage;
    TextView textViewNickname, textViewLevel, textViewId, textViewUsername, textViewEmail, textViewGender, textViewFavouriteCount, textViewDoneCount, textViewPoints, textViewEarned;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVNotificationList;
    private AdapterNotificationAll mAdapter;
    User user2;
    RecyclerView NotificationList;
    String count_notifications;
    View view;
    IMethodCaller iMethodCaller;
    TextView deleteallnotifications;
    private static RelativeLayout zero_notifications;
    private static TextView delete_all_notifications;
    public AdminTab3ReviewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_informations_tab2_fragment_two, container, false);
        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        int IDuser = Integer.parseInt(user2.getId());
        username = user2.getName();
        zero_notifications = view.findViewById(R.id.zero_notifications);
        delete_all_notifications = view.findViewById(R.id.delete_all_notifications);
        deleteallnotifications = view.findViewById(R.id.delete_all_notifications);
        NotificationList = view.findViewById(R.id.NotificationListAll);
        mRVNotificationList = view.findViewById(R.id.NotificationListAll);
        SwipeableWrapContentViewPager pager = (SwipeableWrapContentViewPager) container;
        deleteallnotifications.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogConfirm();
            }
        });

        mRVNotificationList.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pager.setPagingEnabled(true);

                return false;
            }
        });

        getData();
        return view;
    }
    public void openDialogConfirm(){
        final Dialog dialog_confirm_delete_all = new Dialog(mContext); // Context, this, etc.
        dialog_confirm_delete_all .setContentView(R.layout.dialog_confirm_delete_all);
        dialog_confirm_delete_all .getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Context context_dialog = dialog_confirm_delete_all.getContext();
        TextView button_dialog_confirm_delete_all  = dialog_confirm_delete_all.findViewById(R.id.button_delete_confirm);
        TextView dialog_confirm_delete_all_info = dialog_confirm_delete_all.findViewById(R.id.dialog_confirm_delete_all_info);
        TextView dialog_confirm_delete_all_info2 = dialog_confirm_delete_all.findViewById(R.id.dialog_confirm_delete_all_info2);


        if (username.substring(username.length() - 1).equals("a")) {
            dialog_confirm_delete_all_info.setText("Jesteś pewna?");
            dialog_confirm_delete_all_info2.setText("Jesteś pewna, że chcesz usunąć wszystkie powiadomienia?");
        }
        else
        {
            dialog_confirm_delete_all_info.setText("Jesteś pewien?");
            dialog_confirm_delete_all_info2.setText("Jesteś pewien, że chcesz usunąć wszystkie powiadomienia?");
        }


        dialog_confirm_delete_all.show();

        button_dialog_confirm_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.deleteAllItems(Integer.parseInt(user2.getId()));
                dialog_confirm_delete_all.dismiss();
            }
        });
    }
    public static void zeroNotifications(){
        zero_notifications.setVisibility(View.VISIBLE);
        delete_all_notifications.setVisibility(View.GONE);
    }
    public static void oneOrMoreNotifications(){
        zero_notifications.setVisibility(View.GONE);
        delete_all_notifications.setVisibility(View.VISIBLE);
    }

    public void getData() {

        final String url2 = "https://yoururl.com/api/check_notification_count.php?user_id=" + user2.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            count_notifications = response.getString("count_notifications");
                            if (count_notifications.equals("0")) {


                                zero_notifications.setVisibility(View.VISIBLE);
                                delete_all_notifications.setVisibility(View.GONE);

                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();

                            } else if (count_notifications.equals("1")) {

                                new AdminTab3ReviewsFragment.AsyncLogin().execute();

                            }
                            else if (count_notifications.equals("2")) {

                                RelativeLayout zero_notifications = view.findViewById(R.id.zero_notifications);
                                TextView delete_all_notifications = view.findViewById(R.id.delete_all_notifications);

                                zero_notifications.setVisibility(View.VISIBLE);
                                delete_all_notifications.setVisibility(View.GONE);
                            }
                             //Toast.makeText(mContext, response.getString("status"), Toast.LENGTH_SHORT).show();
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

    }



    class AsyncLogin extends AsyncTask<String, String, String> {
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
                url = new URL("https://yoururl.com/api/get_notifications.php?user_id=" + user2.getId() + "&limit=no&notification_type=other");
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
            ArrayList<DataNotification> data = new ArrayList<>();
            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataNotification notificationData = new DataNotification();
                    notificationData.notificationId = json_data.getInt("notification_id");
                    notificationData.notificationTitle = json_data.getString("notification_title");
                    notificationData.notificationDescription = json_data.getString("notification_description");
                    notificationData.notificationEarn = json_data.getString("notification_earn");
                    notificationData.notificationEarnType = json_data.getString("notification_earn_type");
                    notificationData.notificationImage = json_data.getString("notification_img");
                    notificationData.notificationDateAdd = json_data.getString("notification_date_add");
                    data.add(notificationData);
                }
                // Setup and Handover data to recyclerview
                //  mRVNotificationList = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterNotificationAll(getActivity(), data, iMethodCaller, Integer.parseInt(user2.getId()));
                mRVNotificationList.setAdapter(mAdapter);
                mRVNotificationList.setLayoutManager(new GridLayoutManager(getActivity(), 1));

                   /*  RecyclerHelper touchHelper = new RecyclerHelper<DataNotification>(data, mAdapter);

                touchHelper.setRecyclerItemDragEnabled(false);
                touchHelper.setRecyclerItemSwipeEnabled(true);
                //  callback for recyclerview item dragged from one position to other
                touchHelper.setOnDragItemListener(new OnDragListener() {
                    @Override
                    public void onDragItemListener(int fromPosition, int toPosition) {
                        Log.d("TAG", "onDragItemListener: callback after dragging recycler view item");
                    }
                });
                touchHelper.setOnSwipeItemListener(new OnSwipeListener() {
                    @Override
                    public void onSwipeItemListener() {
                        mAdapter.removeItem();
                    }
                });
                                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(touchHelper);
                                                itemTouchHelper.attachToRecyclerView(NotificationList);


*/
                ItemTouchHelper itemTouchHelper = new
                        ItemTouchHelper(new SwipeToDeleteCallback(mAdapter));
                itemTouchHelper.attachToRecyclerView(NotificationList);


            } catch (JSONException e) {
            //   Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }

    public static CharSequence getCountdownText(Context context, Date futureDate) {
        StringBuilder countdownText = new StringBuilder();

        // Calculate the time between now and the future date.
        long timeRemaining = new Date().getTime() - futureDate.getTime();

        // If there is no time between (ie. the date is now or in the past), do nothing
        if (timeRemaining > 0) {
            Resources resources = context.getResources();

            // Calculate the days/hours/minutes/seconds within the time difference.
            //
            // It's important to subtract the value from the total time remaining after each is calculated.
            // For example, if we didn't do this and the time was 25 hours from now,
            // we would get `1 day, 25 hours`.
            int days = (int) TimeUnit.MILLISECONDS.toDays(timeRemaining);
            timeRemaining -= TimeUnit.DAYS.toMillis(days);
            int hours = (int) TimeUnit.MILLISECONDS.toHours(timeRemaining);
            timeRemaining -= TimeUnit.HOURS.toMillis(hours);
            int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeRemaining);
            timeRemaining -= TimeUnit.MINUTES.toMillis(minutes);
            //    int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(timeRemaining);

            // For each time unit, add the quantity string to the output, with a space.
            if (days > 0 && hours > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.days, days, days) + " i " + resources.getQuantityString(R.plurals.hours, hours, hours) + " temu");
                countdownText.append(" ");
            } else if (days > 0 && hours == 0) {
                countdownText.append(resources.getQuantityString(R.plurals.days, days, days) + " temu");
                countdownText.append(" ");
            } else if (days == 0 && hours > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.hours, hours, hours) + " temu");
                countdownText.append(" ");
            } else if (days == 0 && hours == 0 && minutes > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.minutes, minutes, minutes) + " temu");
                countdownText.append(" ");
            } else if (hours > 0 && minutes > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.hours, hours, hours) + " i " + resources.getQuantityString(R.plurals.minutes, minutes, minutes) + " temu");
                countdownText.append(" ");
            }
         /*   if (days > 0 || hours > 0 || minutes > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.minutes, minutes, minutes));
                countdownText.append(" ");
            }
            if (days > 0 || hours > 0 || minutes > 0 || seconds > 0) {
                countdownText.append(resources.getQuantityString(R.plurals.seconds, seconds, seconds));
                countdownText.append(" ");
            } */
        }

        return countdownText.toString();
    }
    public void deleteItemDatabase(int itemId) {
        Log.d("TAG", String.valueOf(itemId));

    }
}
