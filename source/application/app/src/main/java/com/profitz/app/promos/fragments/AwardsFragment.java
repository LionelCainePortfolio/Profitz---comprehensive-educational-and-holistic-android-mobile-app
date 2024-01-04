package com.profitz.app.promos.fragments;

import static java.lang.Integer.parseInt;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.AdapterNotification;
import com.profitz.app.promos.adapters.AdapterTasks;
import com.profitz.app.promos.data.DataNotification;
import com.profitz.app.promos.data.DataTask;
import com.profitz.app.util.SliderTransformer;
import com.profitz.app.util.SliderTransformerTasks;

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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import es.dmoral.toasty.Toasty;

public class AwardsFragment extends Fragment {
    User user;
    String username;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVNotificationList;
    private AdapterNotification mAdapter;
    private AdapterTasks mTasksAdapter;
    private ViewPager2 mRvTasks;
    private int actual_day;
    private String actual_day_clicked;
    private  String status_check_update_day_clicked;
    private double points_yesterday;
    private RelativeLayout relative_global_my_awards;
    private int actual_day_return;
    RecyclerView NotificationList;
    String count_notifications;
    private String showUndoneDone;
    private TextView show_all_notifications_awards;
    private LinearLayout nothings_here_awards_lr;
    private ImageView imageDailyTask;
    private TextView get_daily_present_text;
    private LinearLayout task_of_day;
    private TextView textEarnTasks;
    private ImageView changeTaskShowType;
    private TextView changeTaskShowTypeText;
    private RelativeLayout rl_change_TaskShow;
    public AwardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_awards, container, false);

            user = MyPreferenceManager.getInstance(mContext).getUser();
            int IDuser = Integer.parseInt(user.getId());
            username = user.getName();

            NotificationList = view.findViewById(R.id.NotificationList);
            mRVNotificationList = view.findViewById(R.id.NotificationList);
            TextView learn_more = view.findViewById(R.id.textViewTerms2);
            learn_more.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final String url_to_rules = "https://profitz.app/invite_friends";
                    Intent ir = new Intent(Intent.ACTION_VIEW);
                    ir.setData(Uri.parse(url_to_rules));
                    startActivity(ir);
                }
            });
            getData();
            mRvTasks = view.findViewById(R.id.rv_tasks);
            getData2();
            show_all_notifications_awards = view.findViewById(R.id.show_all_notifications_awards);
            nothings_here_awards_lr = view.findViewById(R.id.nothings_here_awards_lr);
            get_daily_present_text = view.findViewById(R.id.get_daily_present_text);
            task_of_day = view.findViewById(R.id.task_of_day);
            imageDailyTask = view.findViewById(R.id.imageDailyTask);
            changeTaskShowType = view.findViewById(R.id.changeTaskShowType);
            changeTaskShowTypeText = view.findViewById(R.id.changeTaskShowTypeText);
            textEarnTasks = view.findViewById(R.id.textEarnTasks);
            relative_global_my_awards = view.findViewById(R.id.relative_global_my_awards);
            rl_change_TaskShow = view.findViewById(R.id.rl_change_TaskShow);

            task_of_day.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (actual_day_clicked.equals("no")) {
                        imageDailyTask.setBackgroundResource(0);
                        imageDailyTask.setBackgroundResource(R.drawable.ic_done_green_24dp);

                        double MinRandomDay = (double) actual_day_return/100;
                        double MaxRandomDay = (double) actual_day_return/20;
                        if (actual_day_return == 0)
                        {
                            MinRandomDay = 0.01;
                            MaxRandomDay = 0.05;
                        }

                        double random = getRandom(MinRandomDay, MaxRandomDay);
                        final Dialog dialog_clicked_day = new Dialog(mContext); // Context, this, etc.
                        dialog_clicked_day.setContentView(R.layout.dialog_clicked_day);
                        dialog_clicked_day.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        Button click2 = (Button) dialog_clicked_day.findViewById(R.id.button_done_clicked_day);
                        click2.setEnabled(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                click2.setEnabled(true);
                                getData();
                            }
                        },500);
                        click2.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog_clicked_day.dismiss();
                            }
                        });

                        double points_yesterday_new = random - points_yesterday;
                        DecimalFormat twoDForm2 = new DecimalFormat("#.##");
                        DecimalFormatSymbols dfs2 = new DecimalFormatSymbols();
                        dfs2.setDecimalSeparator('.');
                        twoDForm2.setDecimalFormatSymbols(dfs2);
                        double points_yesterday_new_decimal = Double.parseDouble(twoDForm2.format(points_yesterday_new));

                        if (points_yesterday > (random+0.01)){
                            String textViewDialogS = "Otrzymujesz "+random+" punktów za dzienne zalogowanie.";
                            TextView textViewDialog = (TextView) dialog_clicked_day.findViewById(R.id.dialog_done_info2);
                            textViewDialog.setText(textViewDialogS);
                        }
                        else {
                            String textViewDialogS = "Otrzymujesz "+random+" punktów za dzienne zalogowanie. To o "+points_yesterday_new_decimal+" punktów więcej niż wczoraj!";
                            TextView textViewDialog = (TextView) dialog_clicked_day.findViewById(R.id.dialog_done_info2);
                            textViewDialog.setText(textViewDialogS);
                        }
                        getData2();
                        dialog_clicked_day.show();
                        sendData(random);
                        actual_day_clicked = "yes";

                    }
                 else if(actual_day_clicked.equals("yes"))
                    {

                        if (username.substring(username.length() - 1).equals("a")){
                          //  Snackbar snackbar = Snackbar.make(relative_global_my_awards, Html.fromHtml("<font color='#ffffff' ><b>" + "Odebrałaś już dzisiejszy prezent. Wróć jutro :)"+ "</b></font>"), Snackbar.LENGTH_LONG);
                           // snackbar.show();
                            Toasty.success(mContext, "Odebrałaś już dzisiejszy prezent. Wróć jutro :)").show();

                        }
                        else{
                            //Snackbar snackbar = Snackbar.make(relative_global_my_awards, Html.fromHtml("<font color='#ffffff' ><b>" + "Odebrałeś już dzisiejszy prezent. Wróć jutro :)"+ "</b></font>"), Snackbar.LENGTH_LONG);
                           // snackbar.show();
                            Toasty.success(mContext, "Odebrałeś już dzisiejszy prezent. Wróć jutro :)").show();

                        }

                    }
                    else{
                        // Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
                    }

                }
            });



            final String url_count_done_tasks = "https://yoururl.com/api/check_tasks_done_count.php?user_id="+user.getId();
            JsonObjectRequest jsonObjectRequestx = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url_count_done_tasks, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                            String count_done_tasks = response.getString("count_tasks_done");
                            if (count_done_tasks.equals("1")){
                                rl_change_TaskShow.setVisibility(View.VISIBLE);
                            }
                            else{
                                rl_change_TaskShow.setVisibility(View.GONE);
                            }


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
            Volley.newRequestQueue(mContext).add(jsonObjectRequestx);





            changeTaskShowType.setBackground(ContextCompat.getDrawable(mContext, R.drawable.eye_opened));

            showUndoneDone = "undone";
            changeTaskShowTypeText.setText("Pokaż ukończone");
            changeTaskShowType.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                   if (showUndoneDone.equals("undone")){
                       showUndoneDone = "done";
                       changeTaskShowTypeText.setText("Pokaż dostępne");
                       textEarnTasks.setText("Ukończone zadania");
                       changeTaskShowType.setBackground(null);
                       changeTaskShowType.setBackground(ContextCompat.getDrawable(mContext, R.drawable.eye_closed));
                       new AsyncTasks().execute();

                   }
                   else if (showUndoneDone.equals("done")){
                       showUndoneDone = "undone";
                       changeTaskShowTypeText.setText("Pokaż ukończone");
                       textEarnTasks.setText("Aktywne zadania");
                       changeTaskShowType.setBackground(null);
                       changeTaskShowType.setBackground(ContextCompat.getDrawable(mContext, R.drawable.eye_opened));
                       new AsyncTasks().execute();

                   }
                }
            });
            //  mRvPromos.setSlideOnFling(false);
            // mRvPromos.setSlideOnFlingThreshold(1500);
       /*mRvPromos.setItemTransformer(new ScaleTransformer.Builder()
                .setMaxScale(1.05f)
                .setMinScale(0.8f)
                .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                .build());*/


            // SnapHelper snapHelper = new GravitySnapHelper(Gravity.END);
            //snapHelper.attachToRecyclerView(mRvPromos);

            new AsyncTasks().execute();

            return view;

        }
    public void getData(){
        final String url_get_day = "https://yoururl.com/api/get_actual_day.php?user_id="+user.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url_get_day, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            actual_day_return = response.getInt("actual_day");
                            points_yesterday = response.getDouble("points_yesterday");


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

        final String url2 = "https://yoururl.com/api/check_notification_count.php?user_id="+user.getId()+"&limit=yes&type=awards";
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            count_notifications = response.getString("count_notifications");
                            if (count_notifications.equals("0")) {
                                NotificationList.setVisibility(View.GONE);
                                show_all_notifications_awards.setVisibility(View.GONE);
                                nothings_here_awards_lr.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();

                            } else if (count_notifications.equals("1")) {
                                nothings_here_awards_lr.setVisibility(View.GONE);
                                NotificationList.setVisibility(View.VISIBLE);
                                show_all_notifications_awards.setVisibility(View.GONE);
                                new AsyncNotificationAwards().execute();

                            }
                            else if (count_notifications.equals("2")){
                                nothings_here_awards_lr.setVisibility(View.GONE);
                                NotificationList.setVisibility(View.VISIBLE);
                                show_all_notifications_awards.setVisibility(View.VISIBLE);
                                new AsyncNotificationAwards().execute();
                            }
                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
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

    public void getData2(){
        User user = MyPreferenceManager.getInstance(mContext).getUser();
        final String url_get_day_clicked = "https://yoururl.com/api/get_actual_day_clicked.php?user_id="+user.getId();
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url_get_day_clicked, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            actual_day_clicked = response.getString("actual_day_clicked");
                            if (actual_day_clicked.equals("no")) {
                                get_daily_present_text.setText("Odbierz prezent");
                                imageDailyTask.setBackgroundResource(0);
                                imageDailyTask.setBackgroundResource(R.drawable.prezent_task);
                            }
                            else if (actual_day_clicked.equals("yes")) {
                                get_daily_present_text.setText("Wróć jutro");
                                imageDailyTask.setBackgroundResource(0);
                                imageDailyTask.setBackgroundResource(R.drawable.ic_done_green_24dp);
                            }

                            if (actual_day_clicked.equals("no")) {
                                final Handler handler = new Handler();
                                Runnable run = new Runnable() {
                                    @Override
                                    public void run() {
                                        final Animation animShake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
                                        imageDailyTask.startAnimation(animShake);
                                        handler.postDelayed(this, 10000);
                                    }
                                };
                                handler.post(run);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();
                        Log.e("Volly Error", error.toString());

                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Status code", String.valueOf(networkResponse.statusCode));
                        }
                    }
                });
        Volley.newRequestQueue(mContext).add(jsonObjectRequest2);
    }
    public void sendData(double random_points){
        User user = MyPreferenceManager.getInstance(mContext).getUser();

        final String url = "https://yoururl.com/api/update_day_clicked.php?user_id="+user.getId()+"&random_points="+random_points;
        JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            status_check_update_day_clicked = response.getString("status_check_update_day_clicked");


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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest3);

    }

    public double getRandom (double rangeMin, double rangeMax){
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();

        DecimalFormat twoDForm = new DecimalFormat("#.##");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);

        return Double.parseDouble(twoDForm.format(randomValue));
    }


    class AsyncTasks extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;

        LayoutInflater factory = LayoutInflater.from(mContext);
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(mContext);


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
                User user = MyPreferenceManager.getInstance(mContext).getUser();
                url = new URL("https://yoururl.com/api/get_tasks.php?show="+showUndoneDone+"&user_id=" + user.getId());
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
                Log.d("testttt", String.valueOf(response_code));
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
                    //hide_if_zero.setVisibility(View.VISIBLE);
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
            //hide_if_zero.setVisibility(View.GONE);
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
            List<DataTask> data=new ArrayList<>();
            //mSwipeRefreshLayout.setRefreshing(false);
            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);
                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataTask taskData = new DataTask();
                    taskData.taskId= json_data.getInt("task_id");
                    taskData.taskImage= json_data.getString("task_image");
                    taskData.taskTitle= json_data.getString("task_title");
                    taskData.taskDescription= json_data.getString("task_description");
                    taskData.taskPoints= json_data.getDouble("task_points");
                    taskData.taskShortDesc= json_data.getString("task_shortdesc");
                    taskData.taskIsActive= json_data.getString("task_isactive");
                    taskData.taskIsDone= json_data.getString("task_isdone");

                    data.add(taskData);
                }
                Log.d("testD", data.toString());

                // Setup and Handover data to recyclerview
                mRvTasks.setAdapter(mTasksAdapter);
                mTasksAdapter = new AdapterTasks(mContext, data, mRvTasks, showUndoneDone);
                mRvTasks.setNestedScrollingEnabled(true);
                //  mRvPromos.setHasFixedSize(true);
                mRvTasks.setFocusable(false);
                //  PagerSnapHelper snapHelper = new PagerSnapHelper();
                //snapHelper.attachToRecyclerView(mRvPromos);
                //mRvPromos.setItemViewCacheSize(20);
                mRvTasks.setDrawingCacheEnabled(true);
                mRvTasks.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                // mRvPromos.setLayoutManager(layoutManager);
                //mRvPromos.setHasFixedSize(true);
                ///InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(mPromosAdapter);
                mRvTasks.setOffscreenPageLimit(4);
                mRvTasks.setPageTransformer(new SliderTransformerTasks(4));
                mRvTasks.setAdapter(mTasksAdapter);
                mRvTasks.setClipToPadding(false);
                mRvTasks.setClipChildren(false);
                mRvTasks.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            } catch (JSONException e) {
                Log.d("error_tasks", e.toString());
                //  Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(mContext, "Wystąpił nieznany błąd podczas pobierania zadań. Spróbuj ponownie.").show();

            }

        }

    }



    class AsyncNotificationAwards extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;

        LayoutInflater factory = LayoutInflater.from(mContext);
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(mContext);


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
                url = new URL("https://yoururl.com/api/get_notifications.php?user_id="+user.getId()+"&limit=yes&notification_type=tasks");
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
            List<DataNotification> data_notification=new ArrayList<>();
            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataNotification notificationData = new DataNotification();
                    notificationData.notificationId= json_data.getInt("notification_id");
                    notificationData.notificationTitle= json_data.getString("notification_title");
                    notificationData.notificationDescription= json_data.getString("notification_description");
                    notificationData.notificationEarn= json_data.getString("notification_earn");
                    notificationData.notificationEarnType= json_data.getString("notification_earn_type");
                    notificationData.notificationImage= json_data.getString("notification_img");
                    notificationData.notificationDateAdd= json_data.getString("notification_date_add");
                    data_notification.add(notificationData);
                }

                // Setup and Handover data to recyclerview
                //  mRVNotificationList = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterNotification(mContext, data_notification);
                mRVNotificationList.setAdapter(mAdapter);
                mRVNotificationList.setLayoutManager(new GridLayoutManager(mContext,1));

            } catch (JSONException e) {
                //Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                Log.d("error_tasks_notifications", e.toString());
                Toasty.error(mContext, "Wystąpił nieznany błąd podczas pobierania historii zadań. Spróbuj ponownie.").show();

            }

        }

    }
}