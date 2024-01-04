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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.profitz.app.R;import com.profitz.app.promos.IMethodCaller;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;

import com.profitz.app.promos.adapters.AdapterPeopleAll;
import com.profitz.app.promos.data.DataPeople;
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

public class AdminTab1PeopleFragment extends Fragment implements IMethodCaller, BottomSheetDialogDone.BottomSheetListener {
    String username;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVPeopleList;
    private AdapterPeopleAll mAdapter;
    User user2;
    RecyclerView PeopleList;
    String count_people;
    View view;
    IMethodCaller iMethodCaller;
    SwipeRefreshLayout mSwipeRefreshLayout;
    EditText search_edit_text;
    ArrayList<DataPeople> data;
    FragmentManager fragmentManager;

    public AdminTab1PeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRVPeopleList = view.findViewById(R.id.usersAdminRV);

        data = new ArrayList<>();
        fragmentManager = getChildFragmentManager();
        mRVPeopleList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRVPeopleList.setHasFixedSize(true);
        mAdapter = new AdapterPeopleAll(getActivity(), data, iMethodCaller, Integer.parseInt(user2.getId()), fragmentManager);
        mRVPeopleList.setAdapter(mAdapter);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_tab1_fragment_people, container, false);
        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        int IDuser = Integer.parseInt(user2.getId());
        username = user2.getName();
        PeopleList = view.findViewById(R.id.usersAdminRV);
        mRVPeopleList = view.findViewById(R.id.usersAdminRV);
        SwipeableWrapContentViewPager pager = (SwipeableWrapContentViewPager) container;

        mRVPeopleList.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pager.setPagingEnabled(true);

                return false;
            }
        });
        /*
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        new AsyncPeople().execute();
                    }
                }
        );

         */

        search_edit_text = view. findViewById(R.id.search_edit_text);
        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                mAdapter.getFilter().filter(editable.toString());

            }
        });


        new AsyncPeople().execute();

        return view;
    }

    @Override
    public void onButtonClicked(String text) {

    }

    class AsyncPeople extends AsyncTask<String, String, String> {
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
            //mSwipeRefreshLayout.setRefreshing(false);

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
                url = new URL("https://yoururl.com/api/admin_get_people.php?safety_code=HDA2JH461m32l32v2jDV2213ABm52b2NDA41AK141nAND");
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



            JSONArray jArray = null;
            try {
                jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataPeople peopleData = new DataPeople();
                    peopleData.peopleUserId = json_data.getInt("people_user_id");
                    peopleData.peopleUserName = json_data.getString("people_user_name");
                    peopleData.peopleUserNickname = json_data.getString("people_user_nickname");
                    peopleData.peopleUserLevel = json_data.getInt("people_user_level");
                    peopleData.peopleUserPoints = json_data.getDouble("people_user_points");
                    peopleData.peopleUserTotalEarn = json_data.getDouble("people_user_total_earn");
                    peopleData.peopleUserDones = json_data.getInt("people_user_dones");
                    peopleData.peopleUserFavorites = json_data.getInt("people_user_favorites");
                    peopleData.peopleUserComments = json_data.getInt("people_user_comments");
                    peopleData.peopleUserReviews = json_data.getInt("people_user_reviews");
                    peopleData.peopleUserLicences = json_data.getInt("people_user_licences");
                    peopleData.peopleUserBoughts = json_data.getInt("people_user_boughts");
                    peopleData.peopleUserRefferalCount = json_data.getInt("people_user_refferal_count");
                    peopleData.peopleUserRefferalSuccessCount = json_data.getInt("people_user_refferal_success_count");
                    peopleData.peopleUserRefferalEarn = json_data.getDouble("people_user_refferal_earn");
                    peopleData.peopleUserActualDay = json_data.getInt("people_user_actual_day");
                    peopleData.peopleUserImageUrl = json_data.getString("people_user_image_url");
                    peopleData.peopleUserRegisterDate = json_data.getString("people_user_register_date");
                    peopleData.peopleUserLastLogin = json_data.getString("people_user_last_login");

                    data.add(peopleData);
                    mRVPeopleList.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                    mRVPeopleList.setHasFixedSize(true);

                    mAdapter = new AdapterPeopleAll(getActivity(), data, iMethodCaller, Integer.parseInt(user2.getId()), fragmentManager);

                    // Setup and Handover data to recyclerview
                    //  mRVpeopleList = (RecyclerView)findViewById(R.id.fishPriceList);
                    mRVPeopleList.setAdapter(mAdapter);


                   /*  RecyclerHelper touchHelper = new RecyclerHelper<Datapeople>(data, mAdapter);

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
                                                itemTouchHelper.attachToRecyclerView(peopleList);


*/


                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("tagg", String.valueOf(e));
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
