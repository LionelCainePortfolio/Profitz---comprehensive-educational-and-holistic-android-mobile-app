package com.profitz.app.promos.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.profitz.app.R;
import com.profitz.app.RichWysiwyg;
import com.profitz.app.promos.CustomLinearLayoutManager;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PromosActivity;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.AirdropDetailActivity;
import com.profitz.app.promos.adapters.AdapterAirdrop;
import com.profitz.app.promos.data.DataAirdrop;
import com.profitz.app.util.UnScrollableViewPager;
import com.profitz.app.util.isAllRequiredTyped;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class AirdropsFragment extends Fragment  {
    User user2;
    String username;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    int IDuser;
    Timer timer4 = new Timer();
    Dialog dialog3;
    List<DataAirdrop> data;
    private AdapterAirdrop mAdapter;
    private RecyclerView mRVAirdropsList;
    private RecyclerView mRVAirdropsFeaturedList;
    LinearLayoutManager linearLayoutManager;
    LinearLayoutManager linearLayoutManagerVertical;
    NestedScrollView nested_scroll_fragment_airdrops;
    RelativeLayout for_snackbar;
    boolean end = false;
    String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    Timer timer = new Timer();
    RelativeLayout rl_featured;
    boolean show_rl1_is_shown = true;
    boolean show_rl2_is_shown = true;
    boolean show_rl3_is_shown = true;
    boolean show_rl4_is_shown = true;
    boolean show_rl5_is_shown = false;
    String question1_picked = "0";
    String question2_picked = "0";
    String question3_picked = "0";
    String question4_picked = "0";
    String question5_picked = "0";
    String question1_type;
    String question2_type;
    String question3_type;
    String question4_type;
    String question5_type;
    String title;
    String short_desc;
    String logo_url;
    String website;
    String form_url;
    String coinmarketcap_url;
    String whitepaper_url;
    String twitter_url;
    String telegram_url;
    String facebook_url;
    String medium_url;
    String reddit_url;
    String discord_url;
    String token;
    String value;
    String exchanges;
    String supply;
    String date_end;
    String date_distribution;
    String desc;
    String instruction;
    String instruction2;
    String fragment1_edit1_typed = "0";
    String fragment1_edit2_typed = "0";
    String fragment1_edit3_typed = "0";
    String fragment1_edit4_typed = "0";
    String fragment1_edit5_typed = "0";
    String fragment1_edit6_typed = "0";
    String fragment2_edit1_typed = "0";
    String fragment2_edit2_typed = "0";
    String fragment2_edit3_typed = "0";
    String fragment2_edit4_typed = "0";
    String fragment2_edit5_typed = "0";
    String fragment2_edit6_typed = "0";
    String fragment2_edit7_typed = "0";
    String fragment3_edit1_typed = "0";
    String fragment3_edit2_typed = "0";
    String fragment3_edit3_typed = "0";
    String fragment3_edit4_typed = "0";
    String fragment3_edit5_typed = "0";
    String fragment3_edit6_typed = "0";
    String fragment5_edit1_typed = "0";
    String fragment5_edit2_typed = "0";
    Runnable mTicker;
    Handler mHandler;
    boolean isRunning = false;
    EditText search_edit_text;
    TextView add_airdrop;
    DotsIndicator dotsIndicator;
    private UnScrollableViewPager viewPagerAddAirdrop;
    private Dialog airdropAddDialog;
    private AddAirdropViewPagerAdapter myViewPagerAirdropAddAdapter;
    isAllRequiredTyped isAllRequiredTypedd = new isAllRequiredTyped();
    Timer timer2;
    TimerTask timerTask;
    FloatingActionButton fab;
    private int[] layouts_airdrop_add;
    TextWatcher textWatcher;
    int show_new = 1;
    int show_hot = 1;
    int show_exclusive = 1;
    int show_upline = 0;
    int show_inactive = 0;

    public AirdropsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_airdrops, container, false);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


            user2 = MyPreferenceManager.getInstance(mContext).getUser();
            IDuser = Integer.parseInt(user2.getId());
            username = user2.getName();


            mRVAirdropsFeaturedList = view.findViewById(R.id.mRVAirdropsFeaturedList);
            mRVAirdropsList = view.findViewById(R.id.mRVAirdropsList);
            linearLayoutManager = new CustomLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            linearLayoutManagerVertical = new CustomLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

            rl_featured = view.findViewById(R.id.rl_featured);

            search_edit_text = view.findViewById(R.id.search_airdrop_edit_text);
            nested_scroll_fragment_airdrops = view.findViewById(R.id.nested_scroll_fragment_airdrops);

            RelativeLayout show_img_rl1 = view.findViewById(R.id.show_img_rl1);
            RelativeLayout show_img_rl2 = view.findViewById(R.id.show_img_rl2);
            RelativeLayout show_img_rl3 = view.findViewById(R.id.show_img_rl3);
            RelativeLayout show_img_rl4 = view.findViewById(R.id.show_img_rl4);
            RelativeLayout show_img_rl5 = view.findViewById(R.id.show_img_rl5);
            TextView add_airdrop = view.findViewById(R.id.add_airdrop);

            add_airdrop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user2.getId();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String premium = response.getString("premium");
                                        if (premium.equals("0")) {
                                            Activity activity = getActivity();
                                            new PromosActivity().showLockedContentOnlyPremiumDialog((AppCompatActivity) activity);
                                        }
                                        else{
                                            startAddAirdrop((AppCompatActivity) getActivity());
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
                    Volley.newRequestQueue(mContext).add(jsonObjectRequest);
                }
            });



            show_img_rl1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                if(show_rl1_is_shown){
                    show_img_rl1.setBackgroundResource(R.drawable.round_cat_grey);
                    show_img_rl1.setTag(R.drawable.round_cat_grey);
                    show_rl1_is_shown = false;
                    show_new = 0;

                    if (!show_rl1_is_shown){
                        show_new = 0;
                    }
                    if (!show_rl2_is_shown){
                        show_hot = 0;
                    }
                    if (!show_rl3_is_shown){
                        show_exclusive = 0;
                    }
                    if (!show_rl4_is_shown){
                        show_upline = 0;
                    }
                    if (!show_rl5_is_shown){
                        show_inactive = 0;
                    }

                    if (show_rl1_is_shown){
                        show_new = 1;
                    }
                    if (show_rl2_is_shown){
                        show_hot = 1;
                    }
                    if (show_rl3_is_shown){
                        show_exclusive = 1;
                    }
                    if (show_rl4_is_shown){
                        show_upline = 1;
                    }
                    if (show_rl5_is_shown){
                        show_inactive = 1;
                    }




                }
                else{
                    show_img_rl1.setBackgroundResource(R.drawable.round_cat_gold);
                    show_img_rl1.setTag(R.drawable.round_cat_gold);

                    show_rl1_is_shown = true;
                    show_new = 1;

                    if (!show_rl1_is_shown){
                        show_new = 0;
                    }
                    if (!show_rl2_is_shown){
                        show_hot = 0;
                    }
                    if (!show_rl3_is_shown){
                        show_exclusive = 0;
                    }
                    if (!show_rl4_is_shown){
                        show_upline = 0;
                    }
                    if (!show_rl5_is_shown){
                        show_inactive = 0;
                    }

                    if (show_rl1_is_shown){
                        show_new = 1;
                    }
                    if (show_rl2_is_shown){
                        show_hot = 1;
                    }
                    if (show_rl3_is_shown){
                        show_exclusive = 1;
                    }
                    if (show_rl4_is_shown){
                        show_upline = 1;
                    }
                    if (show_rl5_is_shown){
                        show_inactive = 1;
                    }

                }
                    new AsyncAirdrop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                }
            });
            show_img_rl2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(show_rl2_is_shown){
                        show_img_rl2.setBackgroundResource(R.drawable.round_cat_grey);
                        show_img_rl1.setTag(R.drawable.round_cat_grey);

                        show_rl2_is_shown = false;
                        show_hot = 0;

                        if (!show_rl1_is_shown){
                            show_new = 0;
                        }
                        if (!show_rl2_is_shown){
                            show_hot = 0;
                        }
                        if (!show_rl3_is_shown){
                            show_exclusive = 0;
                        }
                        if (!show_rl4_is_shown){
                            show_upline = 0;
                        }
                        if (!show_rl5_is_shown){
                            show_inactive = 0;
                        }

                        if (show_rl1_is_shown){
                            show_new = 1;
                        }
                        if (show_rl2_is_shown){
                            show_hot = 1;
                        }
                        if (show_rl3_is_shown){
                            show_exclusive = 1;
                        }
                        if (show_rl4_is_shown){
                            show_upline = 1;
                        }
                        if (show_rl5_is_shown){
                            show_inactive = 1;
                        }






                    }
                    else{
                        show_img_rl2.setBackgroundResource(R.drawable.round_cat_gold);
                        show_img_rl2.setTag(R.drawable.round_cat_gold);

                        show_rl2_is_shown = true;
                        show_hot = 1;



                        if (!show_rl1_is_shown){
                            show_new = 0;
                        }
                        if (!show_rl2_is_shown){
                            show_hot = 0;
                        }
                        if (!show_rl3_is_shown){
                            show_exclusive = 0;
                        }
                        if (!show_rl4_is_shown){
                            show_upline = 0;
                        }
                        if (!show_rl5_is_shown){
                            show_inactive = 0;
                        }

                        if (show_rl1_is_shown){
                            show_new = 1;
                        }
                        if (show_rl2_is_shown){
                            show_hot = 1;
                        }
                        if (show_rl3_is_shown){
                            show_exclusive = 1;
                        }
                        if (show_rl4_is_shown){
                            show_upline = 1;
                        }
                        if (show_rl5_is_shown){
                            show_inactive = 1;
                        }


                    }

                    new AsyncAirdrop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                }
            });
            show_img_rl3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(show_rl3_is_shown){
                        show_img_rl3.setBackgroundResource(R.drawable.round_cat_grey);
                        show_img_rl3.setTag(R.drawable.round_cat_grey);

                        show_rl3_is_shown = false;
                        show_exclusive = 0;


                        if (!show_rl1_is_shown){
                            show_new = 0;
                        }
                        if (!show_rl2_is_shown){
                            show_hot = 0;
                        }
                        if (!show_rl3_is_shown){
                            show_exclusive = 0;
                        }
                        if (!show_rl4_is_shown){
                            show_upline = 0;
                        }
                        if (!show_rl5_is_shown){
                            show_inactive = 0;
                        }

                        if (show_rl1_is_shown){
                            show_new = 1;
                        }
                        if (show_rl2_is_shown){
                            show_hot = 1;
                        }
                        if (show_rl3_is_shown){
                            show_exclusive = 1;
                        }
                        if (show_rl4_is_shown){
                            show_upline = 1;
                        }
                        if (show_rl5_is_shown){
                            show_inactive = 1;
                        }





                    }
                    else{
                        show_img_rl3.setBackgroundResource(R.drawable.round_cat_gold);
                        show_img_rl3.setTag(R.drawable.round_cat_gold);

                        show_rl3_is_shown = true;
                        show_exclusive = 1;


                        if (!show_rl1_is_shown){
                            show_new = 0;
                        }
                        if (!show_rl2_is_shown){
                            show_hot = 0;
                        }
                        if (!show_rl3_is_shown){
                            show_exclusive = 0;
                        }
                        if (!show_rl4_is_shown){
                            show_upline = 0;
                        }
                        if (!show_rl5_is_shown){
                            show_inactive = 0;
                        }

                        if (show_rl1_is_shown){
                            show_new = 1;
                        }
                        if (show_rl2_is_shown){
                            show_hot = 1;
                        }
                        if (show_rl3_is_shown){
                            show_exclusive = 1;
                        }
                        if (show_rl4_is_shown){
                            show_upline = 1;
                        }
                        if (show_rl5_is_shown){
                            show_inactive = 1;
                        }






                    }
                    new AsyncAirdrop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                }
            });
            show_img_rl4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(show_rl4_is_shown){
                        show_img_rl4.setBackgroundResource(R.drawable.round_cat_grey);
                        show_img_rl4.setTag(R.drawable.round_cat_grey);

                        show_rl4_is_shown = false;
                        show_upline = 0;


                        if (!show_rl1_is_shown){
                            show_new = 0;
                        }
                        if (!show_rl2_is_shown){
                            show_hot = 0;
                        }
                        if (!show_rl3_is_shown){
                            show_exclusive = 0;
                        }
                        if (!show_rl4_is_shown){
                            show_upline = 0;
                        }
                        if (!show_rl5_is_shown){
                            show_inactive = 0;
                        }

                        if (show_rl1_is_shown){
                            show_new = 1;
                        }
                        if (show_rl2_is_shown){
                            show_hot = 1;
                        }
                        if (show_rl3_is_shown){
                            show_exclusive = 1;
                        }
                        if (show_rl4_is_shown){
                            show_upline = 1;
                        }
                        if (show_rl5_is_shown){
                            show_inactive = 1;
                        }




                    }
                    else{
                        show_img_rl4.setBackgroundResource(R.drawable.round_cat_gold);
                        show_img_rl4.setTag(R.drawable.round_cat_gold);

                        show_rl4_is_shown = true;
                        show_upline = 1;


                        if (!show_rl1_is_shown){
                            show_new = 0;
                        }
                        if (!show_rl2_is_shown){
                            show_hot = 0;
                        }
                        if (!show_rl3_is_shown){
                            show_exclusive = 0;
                        }
                        if (!show_rl4_is_shown){
                            show_upline = 0;
                        }
                        if (!show_rl5_is_shown){
                            show_inactive = 0;
                        }

                        if (show_rl1_is_shown){
                            show_new = 1;
                        }
                        if (show_rl2_is_shown){
                            show_hot = 1;
                        }
                        if (show_rl3_is_shown){
                            show_exclusive = 1;
                        }
                        if (show_rl4_is_shown){
                            show_upline = 1;
                        }
                        if (show_rl5_is_shown){
                            show_inactive = 1;
                        }




                    }
                    new AsyncAirdrop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                }
            });
            show_img_rl5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if(show_rl5_is_shown){
                        show_img_rl5.setBackgroundResource(R.drawable.round_cat_grey);
                        show_img_rl5.setTag(R.drawable.round_cat_grey);

                        show_rl5_is_shown = false;
                        show_inactive = 0;


                        if (!show_rl1_is_shown){
                            show_new = 0;
                        }
                        if (!show_rl2_is_shown){
                            show_hot = 0;
                        }
                        if (!show_rl3_is_shown){
                            show_exclusive = 0;
                        }
                        if (!show_rl4_is_shown){
                            show_upline = 0;
                        }
                        if (!show_rl5_is_shown){
                            show_inactive = 0;
                        }

                        if (show_rl1_is_shown){
                            show_new = 1;
                        }
                        if (show_rl2_is_shown){
                            show_hot = 1;
                        }
                        if (show_rl3_is_shown){
                            show_exclusive = 1;
                        }
                        if (show_rl4_is_shown){
                            show_upline = 1;
                        }
                        if (show_rl5_is_shown){
                            show_inactive = 1;
                        }


                    }
                    else{
                        show_img_rl5.setBackgroundResource(R.drawable.round_cat_gold);
                        show_img_rl5.setTag(R.drawable.round_cat_gold);

                        show_rl5_is_shown = true;
                        show_inactive = 1;


                        if (!show_rl1_is_shown){
                            show_new = 0;
                        }
                        if (!show_rl2_is_shown){
                            show_hot = 0;
                        }
                        if (!show_rl3_is_shown){
                            show_exclusive = 0;
                        }
                        if (!show_rl4_is_shown){
                            show_upline = 0;
                        }
                       if (!show_rl5_is_shown){
                            show_inactive = 0;
                        }

                        if (show_rl1_is_shown){
                            show_new = 1;
                        }
                        if (show_rl2_is_shown){
                            show_hot = 1;
                        }
                        if (show_rl3_is_shown){
                            show_exclusive = 1;
                        }
                        if (show_rl4_is_shown){
                            show_upline = 1;
                        }
                        if (show_rl5_is_shown){
                            show_inactive = 1;
                        }





                    }
                    new AsyncAirdrop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


                }
            });
            dialog3 = new Dialog(mContext); // Context, this, etc.
            dialog3.setContentView(R.layout.dialog_disable_future);
            dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView click3 = (TextView) dialog3.findViewById(R.id.button_understand_disable_future);
            click3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog3.dismiss();
                }
            });

           // loadAd();
            new AsyncAirdropFeatured().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



            new AsyncAirdrop().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


            return view;

        }







    class AsyncAirdrop extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //gif_loader.setVisibility(View.VISIBLE);


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


                url = new URL("https://yoururl.com/api/get_airdrops.php?featured=false&show_new="+show_new+"&show_hot="+show_hot+"&show_exclusive="+show_exclusive+"&show_upline="+show_upline+"&show_inactive="+show_inactive);

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


            // pdLoading.dismiss();
            data=new ArrayList<>();
            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataAirdrop airdropData = new DataAirdrop();
                    airdropData.airdropId = json_data.getInt("airdrop_id");
                    airdropData.airdropTitle = json_data.getString("airdrop_title");
                    airdropData.airdropDescription = json_data.getString("article_description");
                    airdropData.airdropImage = json_data.getString("airdrop_image");
                    airdropData.airdropValue = json_data.getDouble("airdrop_value");
                    airdropData.airdropWebsite = json_data.getString("airdrop_website");
                    airdropData.airdropExclusive = json_data.getString("airdrop_exclusive");
                    airdropData.airdropHot = json_data.getString("airdrop_hot");
                    airdropData.airdropNew = json_data.getString("airdrop_new");
                    airdropData.airdropUpline = json_data.getString("airdrop_upline");
                    airdropData.airdropIsInActive = json_data.getString("airdrop_is_in_active");
                    airdropData.airdropFeatured = json_data.getString("airdrop_featured");
                    airdropData.airdropLikes = json_data.getString("airdrop_likes");
                    airdropData.airdropViews = json_data.getString("airdrop_views");
                    airdropData.airdropToken = json_data.getString("airdrop_token");
                    airdropData.airdropTotalSupply = json_data.getString("airdrop_total_supply");
                    airdropData.airdropKycRequired = json_data.getString("airdrop_kyc_required");
                    airdropData.airdropMailRequired = json_data.getString("airdrop_mail_required");
                    airdropData.airdropTwitterRequired = json_data.getString("airdrop_twitter_required");
                    airdropData.airdropFacebookRequired = json_data.getString("airdrop_facebook_required");
                    airdropData.airdropTelegramRequired = json_data.getString("airdrop_telegram_required");
                    airdropData.airdropPlatform = json_data.getString("airdrop_platform");
                    airdropData.airdropWhitepaper = json_data.getString("airdrop_whitepaper");
                    airdropData.airdropTwitter = json_data.getString("airdrop_twitter");
                    airdropData.airdropTelegramGroup = json_data.getString("airdrop_telegram_group");
                    airdropData.airdropMedium = json_data.getString("airdrop_medium");
                    airdropData.airdropCoinmarketcap = json_data.getString("airdrop_coinmarketcap");
                    airdropData.airdropReddit = json_data.getString("airdrop_reddit");
                    airdropData.airdropDiscord = json_data.getString("airdrop_discord");
                    airdropData.airdropExchanges= json_data.getString("airdrop_exchanges");
                    airdropData.airdropDateAdd= json_data.getString("airdrop_date_add");
                    airdropData.airdropDateFinish= json_data.getString("airdrop_date_finish");

                    data.add(airdropData);
                }

                // Setup and Handover data to recyclerview
                //  mRVNotificationList = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterAirdrop(mContext, data, String.valueOf(user2.getId()), new AdapterAirdrop.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int id, String airdropTitle, String airdropEarn, String airdropInstruction, String airdropImgUrl, String airdropUrl, String airdropShortDesc, String isExclusive, String airdropWhitepaper, String airdropWebsite, String airdropExchanges, String airdropToken, String airdropPlatform, String airdropTotalSupply, String airdropIsInactive,  String airdropIsUpline, String airdropIsHot, String airdropIsNew, String airdropIsExclusive, String airdropKycRequired, String airdropMailRequired, String airdropFacebookRequired, String airdropTwitterRequired, String airdropTelegramRequired, String airdropTwitter, String airdropTelegram, String airdropMedium, String airdropCoinmarketCap, String airdropReddit, String airdropDiscord) {
                        final String url_check_premium = "https://yoururl.com/api/get_user_data.php?user_id="+user2.getId();

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (com.android.volley.Request.Method.GET, url_check_premium, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            String premium = response.getString("premium");
                                          if ((premium.equals("0")) && (isExclusive.equals("1"))){

                                                    Activity activity = getActivity();
                                                new PromosActivity().showLockedContentDialog((AppCompatActivity) activity);
                                            }
                                            else{
                                                Intent i = new Intent(getActivity(), AirdropDetailActivity.class);
                                                i.putExtra("airdropId",String.valueOf(id));
                                                i.putExtra("airdropTitle",airdropTitle);
                                                i.putExtra("airdropEarn",airdropEarn);
                                                i.putExtra("airdropInstruction",airdropInstruction);
                                                i.putExtra("airdropImgUrl",airdropImgUrl);
                                                i.putExtra("airdropUrl",airdropUrl);
                                                i.putExtra("airdropWhitepaper",airdropWhitepaper);
                                                i.putExtra("airdropWebsite",airdropWebsite);
                                                i.putExtra("airdropExchanges",airdropExchanges);
                                                i.putExtra("airdropToken",airdropToken);
                                                i.putExtra("airdropPlatform",airdropPlatform);
                                                i.putExtra("airdropTotalSupply",airdropTotalSupply);
                                                i.putExtra("airdropIsInactive",airdropIsInactive);
                                                i.putExtra("airdropIsUpline",airdropIsUpline);
                                                i.putExtra("airdropIsHot",airdropIsHot);
                                                i.putExtra("airdropIsNew",airdropIsNew);
                                                i.putExtra("airdropIsExclusive",airdropIsExclusive);
                                                i.putExtra("airdropKycRequired",airdropKycRequired);
                                                i.putExtra("airdropMailRequired",airdropMailRequired);
                                                i.putExtra("airdropFacebookRequired",airdropFacebookRequired);
                                                i.putExtra("airdropTwitterRequired",airdropTwitterRequired);
                                                i.putExtra("airdropTelegramRequired",airdropTelegramRequired);
                                                i.putExtra("airdropTwitter",airdropTwitter);
                                                i.putExtra("airdropTelegram",airdropTelegram);
                                                i.putExtra("airdropMedium",airdropMedium);
                                                i.putExtra("airdropCoinmarketCap",airdropCoinmarketCap);
                                                i.putExtra("airdropReddit",airdropReddit);
                                                i.putExtra("airdropDiscord",airdropDiscord);

                                                startActivity(i);                                            }


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
                });

                mRVAirdropsList.setAdapter(mAdapter);
                mRVAirdropsList.setLayoutManager(linearLayoutManagerVertical);
                textWatcher=new TextWatcher() {
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
                };
                search_edit_text.addTextChangedListener(textWatcher);





            } catch (JSONException e) {
                //Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(mContext, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();
                Log.e("error", String.valueOf(e));
            }

        }

    }

    class AsyncAirdropFeatured extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //gif_loader.setVisibility(View.VISIBLE);


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
                url = new URL("https://yoururl.com/api/get_airdrops.php?featured=yes");

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


            // pdLoading.dismiss();
            data=new ArrayList<>();
            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataAirdrop airdropData = new DataAirdrop();
                    airdropData.airdropId = json_data.getInt("airdrop_id");
                    airdropData.airdropTitle = json_data.getString("airdrop_title");
                    airdropData.airdropDescription = json_data.getString("article_description");
                    airdropData.airdropImage = json_data.getString("airdrop_image");
                    airdropData.airdropValue = json_data.getDouble("airdrop_value");
                    airdropData.airdropWebsite = json_data.getString("airdrop_website");
                    airdropData.airdropExclusive = json_data.getString("airdrop_exclusive");
                    airdropData.airdropHot = json_data.getString("airdrop_hot");
                    airdropData.airdropNew = json_data.getString("airdrop_new");
                    airdropData.airdropUpline = json_data.getString("airdrop_upline");
                    airdropData.airdropIsInActive = json_data.getString("airdrop_is_in_active");
                    airdropData.airdropFeatured = json_data.getString("airdrop_featured");
                    airdropData.airdropLikes = json_data.getString("airdrop_likes");
                    airdropData.airdropViews = json_data.getString("airdrop_views");
                    airdropData.airdropToken = json_data.getString("airdrop_token");
                    airdropData.airdropTotalSupply = json_data.getString("airdrop_total_supply");
                    airdropData.airdropKycRequired = json_data.getString("airdrop_kyc_required");
                    airdropData.airdropMailRequired = json_data.getString("airdrop_mail_required");
                    airdropData.airdropTwitterRequired = json_data.getString("airdrop_twitter_required");
                    airdropData.airdropFacebookRequired = json_data.getString("airdrop_facebook_required");
                    airdropData.airdropTelegramRequired = json_data.getString("airdrop_telegram_required");
                    airdropData.airdropPlatform = json_data.getString("airdrop_platform");
                    airdropData.airdropWhitepaper = json_data.getString("airdrop_whitepaper");
                    airdropData.airdropTwitter = json_data.getString("airdrop_twitter");
                    airdropData.airdropTelegramGroup = json_data.getString("airdrop_telegram_group");
                    airdropData.airdropMedium = json_data.getString("airdrop_medium");
                    airdropData.airdropCoinmarketcap = json_data.getString("airdrop_coinmarketcap");
                    airdropData.airdropReddit = json_data.getString("airdrop_reddit");
                    airdropData.airdropDiscord = json_data.getString("airdrop_discord");
                    airdropData.airdropExchanges= json_data.getString("airdrop_exchanges");
                    airdropData.airdropDateAdd= json_data.getString("airdrop_date_add");
                    airdropData.airdropDateFinish= json_data.getString("airdrop_date_finish");

                    data.add(airdropData);
                }

                // Setup and Handover data to recyclerview
                //  mRVNotificationList = (RecyclerView)findViewById(R.id.fishPriceList);
                mAdapter = new AdapterAirdrop(mContext, data, String.valueOf(user2.getId()), new AdapterAirdrop.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int id, String airdropTitle, String airdropEarn, String airdropInstruction, String airdropImgUrl, String airdropUrl, String airdropShortDesc, String isExclusive, String airdropWhitepaper, String airdropWebsite, String airdropExchanges, String airdropToken, String airdropPlatform, String airdropTotalSupply, String airdropIsInactive,  String airdropIsUpline, String airdropIsHot, String airdropIsNew, String airdropIsExclusive, String airdropKycRequired, String airdropMailRequired, String airdropFacebookRequired, String airdropTwitterRequired, String airdropTelegramRequired, String airdropTwitter, String airdropTelegram, String airdropMedium, String airdropCoinmarketCap, String airdropReddit, String airdropDiscord) {
                        final String url_check_premium = "https://yoururl.com/api/get_user_data.php?user_id="+user2.getId();

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (com.android.volley.Request.Method.GET, url_check_premium, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            String premium = response.getString("premium");
                                            if ((premium.equals("0")) && (isExclusive.equals("1"))){

                                                Activity activity = getActivity();
                                                new PromosActivity().showLockedContentDialog((AppCompatActivity) activity);
                                            }
                                            else{
                                                Intent i = new Intent(getActivity(), AirdropDetailActivity.class);
                                                i.putExtra("airdropId",String.valueOf(id));
                                                i.putExtra("airdropTitle",airdropTitle);
                                                i.putExtra("airdropEarn",airdropEarn);
                                                i.putExtra("airdropInstruction",airdropInstruction);
                                                i.putExtra("airdropImgUrl",airdropImgUrl);
                                                i.putExtra("airdropUrl",airdropUrl);
                                                i.putExtra("airdropWhitepaper",airdropWhitepaper);
                                                i.putExtra("airdropWebsite",airdropWebsite);
                                                i.putExtra("airdropExchanges",airdropExchanges);
                                                i.putExtra("airdropToken",airdropToken);
                                                i.putExtra("airdropPlatform",airdropPlatform);
                                                i.putExtra("airdropTotalSupply",airdropTotalSupply);
                                                i.putExtra("airdropIsInactive",airdropIsInactive);
                                                i.putExtra("airdropIsUpline",airdropIsUpline);
                                                i.putExtra("airdropIsHot",airdropIsHot);
                                                i.putExtra("airdropIsNew",airdropIsNew);
                                                i.putExtra("airdropIsExclusive",airdropIsExclusive);
                                                i.putExtra("airdropKycRequired",airdropKycRequired);
                                                i.putExtra("airdropMailRequired",airdropMailRequired);
                                                i.putExtra("airdropFacebookRequired",airdropFacebookRequired);
                                                i.putExtra("airdropTwitterRequired",airdropTwitterRequired);
                                                i.putExtra("airdropTelegramRequired",airdropTelegramRequired);
                                                i.putExtra("airdropTwitter",airdropTwitter);
                                                i.putExtra("airdropTelegram",airdropTelegram);
                                                i.putExtra("airdropMedium",airdropMedium);
                                                i.putExtra("airdropCoinmarketCap",airdropCoinmarketCap);
                                                i.putExtra("airdropReddit",airdropReddit);
                                                i.putExtra("airdropDiscord",airdropDiscord);

                                                startActivity(i);                             }


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
                });

                mRVAirdropsFeaturedList.setAdapter(mAdapter);
                mRVAirdropsFeaturedList.setLayoutManager(linearLayoutManager);




                if (data.size()>1)
                {
                    runSliderFeatured();
                }
                else if  (data.size()==1){
                        rl_featured.setVisibility(View.VISIBLE);
                 }
                else if (data.size()==0){
                    rl_featured.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Log.d("error_airdrops", e.toString());
              //  Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                Toasty.error(mContext, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

            }

        }

    }
    private class AutoScrollTask extends TimerTask {
        @Override
        public void run() {
            int position = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            isRunning =true;
            if(position == data.size() -1){
                end = true;
            } else if (position == 0) {
                end = false;
            }
            if(!end){
                position++;
            } else {
                position--;
            }
            mRVAirdropsFeaturedList.smoothScrollToPosition(position);
        }
    }
    public void runSliderFeatured(){
        final String url25 = "https://yoururl.com/api/check_airdrops_featured_count.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url25, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String count_airdrops = response.getString("count_airdrops");
                            if (count_airdrops.equals("1")) {
                                timer.scheduleAtFixedRate(new AutoScrollTask(), 2000, 5000);

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    }
    public void stopSliderFeatured(){
        if (isRunning){
            timer.cancel();
            isRunning = false;
        }

    }
    public void startAddAirdrop(AppCompatActivity activity){
        airdropAddDialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        airdropAddDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        airdropAddDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        airdropAddDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        airdropAddDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        airdropAddDialog.getWindow().setBackgroundDrawableResource(R.drawable.white);
        airdropAddDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        // dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        airdropAddDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        airdropAddDialog.setCancelable(false);
        airdropAddDialog.setContentView(R.layout.dialog_add_airdrop);

        airdropAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        airdropAddDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.white));
        airdropAddDialog.getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        airdropAddDialog.getWindow().setStatusBarColor(ContextCompat.getColor(activity,R.color.white));
        airdropAddDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ImageView dismiss_dialog_add_airdrop= airdropAddDialog.findViewById(R.id.arrow_back_add_airdrop);
        Button nextStepAddAirdrop = airdropAddDialog.findViewById(R.id.nextStepAddAirdrop);
        Button finishAddAirdrop = airdropAddDialog.findViewById(R.id.finishAddAirdrop);

        isAllRequiredTypedd.setListener(new isAllRequiredTyped.ChangeListener() {
            @Override
            public void onChange() {
                if (isAllRequiredTypedd.isAllTyped()){
                    nextStepAddAirdrop.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gradient_button_shape));
                    finishAddAirdrop.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gradient_button_shape));

                }
                else{
                    nextStepAddAirdrop.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gradient_grey_button_shape));
                    finishAddAirdrop.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gradient_grey_button_shape));

                }

            }
        });

        finishAddAirdrop.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                airdropAddDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                airdropAddDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.bg_Profitz));
                airdropAddDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropAddDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.bg_Profitz));
                airdropAddDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                airdropAddDialog.dismiss();
                timer4.cancel();
                addAirdrop();
            }
        });

        dismiss_dialog_add_airdrop.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                    int current = getItemAirdrop(-1);
                        // move to prev screen
                        if (current <= 0) {
                        airdropAddDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        airdropAddDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.bg_Profitz));
                        airdropAddDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                        airdropAddDialog.getWindow().setNavigationBarColor(ContextCompat.getColor(mContext, R.color.bg_Profitz));
                        airdropAddDialog.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.white));
                        airdropAddDialog.dismiss();
                    }
                        else{
                            viewPagerAddAirdrop.setCurrentItem(current);
                        }


            }
        });
        dotsIndicator = (DotsIndicator)  airdropAddDialog.findViewById(R.id.dots_indicator);
        viewPagerAddAirdrop = (UnScrollableViewPager) airdropAddDialog.findViewById(R.id.view_pager_airdrop_add);

        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts_airdrop_add = new int[]{
                R.layout.add_airdrop_fragment_1,
                R.layout.add_airdrop_fragment_2,
                R.layout.add_airdrop_fragment_3,
                R.layout.add_airdrop_fragment_4,
                R.layout.add_airdrop_fragment_5
        };


        myViewPagerAirdropAddAdapter = new AddAirdropViewPagerAdapter();
        viewPagerAddAirdrop.setAdapter(myViewPagerAirdropAddAdapter);
        ViewPager.OnPageChangeListener viewPagerAirdropIntroductionPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == layouts_airdrop_add.length - 1) {
                    // last page. make button text to GOT IT
                    nextStepAddAirdrop.setVisibility(View.GONE);
                    finishAddAirdrop.setVisibility(View.VISIBLE);
                } else {
                    // still pages are left
                    finishAddAirdrop.setVisibility(View.GONE);
                    nextStepAddAirdrop.setVisibility(View.VISIBLE);
                }
                nextStepAddAirdrop.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gradient_button_shape));
                finishAddAirdrop.setBackground(ContextCompat.getDrawable(mContext,R.drawable.gradient_button_shape));
                isAllRequiredTypedd.setAllTyped(false);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };
        dotsIndicator.setViewPager(viewPagerAddAirdrop);

        viewPagerAddAirdrop.addOnPageChangeListener(viewPagerAirdropIntroductionPageChangeListener);

        nextStepAddAirdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllRequiredTypedd.isAllTyped()){
                    // checking for last page if true launch MainActivity
                    int current = getItemAirdrop(+1);
                    if (current < layouts_airdrop_add.length) {
                        // move to next screen
                        viewPagerAddAirdrop.setCurrentItem(current);

                    }
                }

            }
        });

        airdropAddDialog.show();
    }

    public void addAirdrop(){

        JSONObject postObject = new JSONObject();
        RequestQueue queue =  Volley.newRequestQueue(mContext);
        JSONObject jobj = new JSONObject();

        String url ="https://yoururl.com/api/insert_airdrop.php";
        try {
            //historyObject.put("id","1");
            jobj.put("user_id", user2.getId());
            jobj.put("question1_type", question1_type);
            jobj.put("question2_type", question2_type);
            jobj.put("question3_type", question3_type);
            jobj.put("question4_type", question4_type);
            jobj.put("question5_type", question5_type);
            if (title == null){
                jobj.put("title", "null");
            }
            else{
                jobj.put("title", title);
            }
            if (short_desc == null){
                jobj.put("short_desc", "null");
            }
            else{
                jobj.put("short_desc", short_desc);
            }
            if (logo_url == null){
                jobj.put("logo_url", "null");
            }
            else{
                jobj.put("logo_url", logo_url);
            }
            if (website == null){
                jobj.put("website", "null");
            }
            else{
                jobj.put("website", website);
            }
            if (form_url == null){
                jobj.put("form_url", "null");
            }
            else{
                jobj.put("form_url", form_url);
            }
            if (telegram_url == null){
                jobj.put("telegram_url", "null");
            }
            else{
                jobj.put("telegram_url", telegram_url);
            }
            if (facebook_url == null){
                jobj.put("facebook_url", "null");
            }
            else{
                jobj.put("facebook_url", facebook_url);
            }
            if (twitter_url  == null){
                jobj.put("twitter_url", "null");
            }
            else{
                jobj.put("twitter_url", twitter_url );
            }
            if (whitepaper_url  == null){
                jobj.put("whitepaper_url", "null");
            }
            else{
                jobj.put("whitepaper_url", whitepaper_url );
            }
            if (coinmarketcap_url   == null){
                jobj.put("coinmarketcap_url", "null");
            }
            else{
                jobj.put("coinmarketcap_url", coinmarketcap_url);
            }
            if (medium_url == null){
                jobj.put("medium_url", "null");
            }
            else{
                jobj.put("medium_url", medium_url);
            }
            if (reddit_url == null){
                jobj.put("reddit_url", "null");
            }
            else{
                jobj.put("reddit_url", reddit_url);
            }
            if (discord_url == null){
                jobj.put("discord_url", "null");
            }
            else{
                jobj.put("discord_url", discord_url);
            }
            if (token == null){
                jobj.put("token", "null");
            }
            else{
                jobj.put("token", token);
            }
            if (value == null){
                jobj.put("value", "null");
            }
            else{
                jobj.put("value", value);
            }
            if (exchanges == null){
                jobj.put("exchanges", "null");
            }
            else{
                jobj.put("exchanges", exchanges);
            }
            if (supply == null){
                jobj.put("supply", "null");
            }
            else{
                jobj.put("supply", supply);
            }
            if (date_end == null){
                jobj.put("date_end", "null");
            }
            else{
                jobj.put("date_end", date_end);
            }
            if (date_distribution == null){
                jobj.put("date_distribution", "null");
            }
            else{
                jobj.put("date_distribution", date_distribution);
            }
            if (desc == null){
                jobj.put("desc", "null");
            }
            else{
                jobj.put("desc", desc);
            }
            if (instruction == null){
                jobj.put("instruction", "null");
            }
            else{
                jobj.put("instruction", instruction);
            }
            postObject.put("data",jobj);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("AddAirdropJsonObject", String.valueOf(postObject));
        JsonObjectRequest objRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.POST, url, postObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("AddAirdrop","OnResponse: "+response);
                        String status_check_insert_airdrop;
                        try {
                            status_check_insert_airdrop = response.getString("status_airdrop");
                            if (status_check_insert_airdrop.equals("1")){
                                ((PromosActivity)getActivity()).snackbarAddAirdrop(1);

                            }
                            else{
                                ((PromosActivity)getActivity()).snackbarAddAirdrop(0);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("OnError", String.valueOf(error.getMessage()));
                        ((PromosActivity)getActivity()).snackbarAddAirdrop(3);
                    }
                });



        queue.add(objRequest);

    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
    private int getItemAirdrop(int i) {
        return viewPagerAddAirdrop.getCurrentItem() + i;
    }

    class AddAirdropViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public AddAirdropViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts_airdrop_add[position], container, false);
            container.addView(view);
            if (position == 0){
                EditText fragment1_edit1 = container.findViewById(R.id.fragment1_edit1);
                EditText fragment1_edit2 = container.findViewById(R.id.fragment1_edit2);
                EditText fragment1_edit3 = container.findViewById(R.id.fragment1_edit3);
                EditText fragment1_edit4 = container.findViewById(R.id.fragment1_edit4);
                EditText fragment1_edit5 = container.findViewById(R.id.fragment1_edit5);
                EditText fragment1_edit6 = container.findViewById(R.id.fragment1_edit6);
                fragment1_edit1.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment1_edit1.getText().toString())) {
                            title=fragment1_edit1.getText().toString();
                            fragment1_edit1_typed="1";
                        }
                        else{
                            fragment1_edit1_typed="0";
                        }
                        isAllRequiredTypedd.setAllTyped((fragment1_edit1_typed.equals("1")) && (fragment1_edit2_typed.equals("1")) && (fragment1_edit3_typed.equals("1")) && (fragment1_edit4_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment1_edit2.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment1_edit2.getText().toString())) {
                            short_desc=fragment1_edit2.getText().toString();
                            fragment1_edit2_typed="1";

                        }
                        else{
                            fragment1_edit2_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment1_edit1_typed.equals("1")) && (fragment1_edit2_typed.equals("1")) && (fragment1_edit3_typed.equals("1")) && (fragment1_edit4_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment1_edit3.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment1_edit3.getText().toString())) {
                            logo_url=fragment1_edit3.getText().toString();
                            fragment1_edit3_typed="1";

                        }
                        else{
                            fragment1_edit3_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment1_edit1_typed.equals("1")) && (fragment1_edit2_typed.equals("1")) && (fragment1_edit3_typed.equals("1")) && (fragment1_edit4_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment1_edit4.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment1_edit4.getText().toString())) {
                            website=fragment1_edit4.getText().toString();
                            fragment1_edit4_typed="1";

                        }
                        else{
                            fragment1_edit4_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment1_edit1_typed.equals("1")) && (fragment1_edit2_typed.equals("1")) && (fragment1_edit3_typed.equals("1")) && (fragment1_edit4_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment1_edit5.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment1_edit5.getText().toString())) {
                            form_url=fragment1_edit5.getText().toString();
                            fragment1_edit5_typed="1";

                        }
                        else{
                            fragment1_edit5_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment1_edit1_typed.equals("1")) && (fragment1_edit2_typed.equals("1")) && (fragment1_edit3_typed.equals("1")) && (fragment1_edit4_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment1_edit6.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment1_edit6.getText().toString())) {
                            coinmarketcap_url=fragment1_edit6.getText().toString();
                            fragment1_edit6_typed="1";

                        }
                        else{
                            fragment1_edit6_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment1_edit1_typed.equals("1")) && (fragment1_edit2_typed.equals("1")) && (fragment1_edit3_typed.equals("1")) && (fragment1_edit4_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
            }
            else if (position == 1){

                EditText fragment2_edit1 = container.findViewById(R.id.fragment2_edit1);
                EditText fragment2_edit2 = container.findViewById(R.id.fragment2_edit2);
                EditText fragment2_edit3 = container.findViewById(R.id.fragment2_edit3);
                EditText fragment2_edit4 = container.findViewById(R.id.fragment2_edit4);
                EditText fragment2_edit5 = container.findViewById(R.id.fragment2_edit5);
                EditText fragment2_edit6 = container.findViewById(R.id.fragment2_edit6);
                EditText fragment2_edit7 = container.findViewById(R.id.fragment2_edit7);

                fragment2_edit1.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment2_edit1.getText().toString())) {
                            whitepaper_url=fragment2_edit1.getText().toString();
                            fragment2_edit1_typed="1";
                        }
                        else{
                            fragment2_edit1_typed="0";
                        }
                        isAllRequiredTypedd.setAllTyped((fragment2_edit1_typed.equals("1")) && (fragment2_edit2_typed.equals("1")) && (fragment2_edit3_typed.equals("1")) && (fragment2_edit4_typed.equals("1")) && (fragment2_edit5_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment2_edit2.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment2_edit2.getText().toString())) {
                            twitter_url=fragment2_edit2.getText().toString();
                            fragment2_edit2_typed="1";

                        }
                        else{
                            fragment2_edit2_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment2_edit1_typed.equals("1")) && (fragment2_edit2_typed.equals("1")) && (fragment2_edit3_typed.equals("1")) && (fragment2_edit4_typed.equals("1")) && (fragment2_edit5_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment2_edit3.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment2_edit3.getText().toString())) {
                            telegram_url=fragment2_edit3.getText().toString();
                            fragment2_edit3_typed="1";

                        }
                        else{
                            fragment2_edit3_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment2_edit1_typed.equals("1")) && (fragment2_edit2_typed.equals("1")) && (fragment2_edit3_typed.equals("1")) && (fragment2_edit4_typed.equals("1")) && (fragment2_edit5_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment2_edit4.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment2_edit4.getText().toString())) {
                            facebook_url=fragment2_edit4.getText().toString();
                            fragment2_edit4_typed="1";

                        }
                        else{
                            fragment2_edit4_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment2_edit1_typed.equals("1")) && (fragment2_edit2_typed.equals("1")) && (fragment2_edit3_typed.equals("1")) && (fragment2_edit4_typed.equals("1")) && (fragment2_edit5_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment2_edit5.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment2_edit5.getText().toString())) {
                            medium_url=fragment2_edit5.getText().toString();
                            fragment2_edit5_typed="1";

                        }
                        else{
                            fragment2_edit5_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment2_edit1_typed.equals("1")) && (fragment2_edit2_typed.equals("1")) && (fragment2_edit3_typed.equals("1")) && (fragment2_edit4_typed.equals("1")) && (fragment2_edit5_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment2_edit6.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment2_edit6.getText().toString())) {
                            reddit_url=fragment2_edit6.getText().toString();
                            fragment2_edit6_typed="1";

                        }
                        else{
                            fragment2_edit6_typed="0";
                            isAllRequiredTypedd.setAllTyped(false);

                        }

                        isAllRequiredTypedd.setAllTyped((fragment2_edit1_typed.equals("1")) && (fragment2_edit2_typed.equals("1")) && (fragment2_edit3_typed.equals("1")) && (fragment2_edit4_typed.equals("1")) && (fragment2_edit5_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment2_edit7.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment2_edit7.getText().toString())) {
                            discord_url=fragment2_edit7.getText().toString();
                            fragment2_edit7_typed="1";

                        }
                        else{
                            fragment2_edit7_typed="0";

                        }

                        isAllRequiredTypedd.setAllTyped((fragment2_edit1_typed.equals("1")) && (fragment2_edit2_typed.equals("1")) && (fragment2_edit3_typed.equals("1")) && (fragment2_edit4_typed.equals("1")) && (fragment2_edit5_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
            }
            else if (position == 2){

                EditText fragment3_edit1 = container.findViewById(R.id.fragment3_edit1);
                EditText fragment3_edit2 = container.findViewById(R.id.fragment3_edit2);
                EditText fragment3_edit3 = container.findViewById(R.id.fragment3_edit3);
                EditText fragment3_edit4 = container.findViewById(R.id.fragment3_edit4);
                EditText fragment3_edit5 = container.findViewById(R.id.fragment3_edit5);
                EditText fragment3_edit6 = container.findViewById(R.id.fragment3_edit6);
                fragment3_edit1.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment3_edit1.getText().toString())) {
                            token=fragment3_edit1.getText().toString();
                            fragment3_edit1_typed="1";
                        }
                        else {
                            fragment3_edit1_typed="0";
                        }
                        isAllRequiredTypedd.setAllTyped((fragment3_edit1_typed.equals("1")) && (fragment3_edit4_typed.equals("1")) && (fragment3_edit5_typed.equals("1")) && (fragment3_edit6_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment3_edit2.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment3_edit2.getText().toString())) {
                            value=fragment3_edit2.getText().toString();
                            fragment3_edit2_typed="1";

                        }
                        else{
                            fragment3_edit2_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment3_edit1_typed.equals("1")) && (fragment3_edit4_typed.equals("1")) && (fragment3_edit5_typed.equals("1")) && (fragment3_edit6_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment3_edit3.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment3_edit3.getText().toString())) {
                            exchanges=fragment3_edit3.getText().toString();
                            fragment3_edit3_typed="1";

                        }
                        else{
                            fragment3_edit3_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment3_edit1_typed.equals("1")) && (fragment3_edit4_typed.equals("1")) && (fragment3_edit5_typed.equals("1")) && (fragment3_edit6_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment3_edit4.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment3_edit4.getText().toString())) {
                            supply=fragment3_edit4.getText().toString();
                            fragment3_edit4_typed="1";

                        }
                        else{
                            fragment3_edit4_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment3_edit1_typed.equals("1")) && (fragment3_edit4_typed.equals("1")) && (fragment3_edit5_typed.equals("1")) && (fragment3_edit6_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });
                fragment3_edit5.setText(date);
                fragment3_edit5.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment3_edit5.getText().toString())) {
                            date_end=fragment3_edit5.getText().toString();
                            fragment3_edit5_typed="1";

                        }
                        else{
                            fragment3_edit5_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment3_edit1_typed.equals("1")) && (fragment3_edit4_typed.equals("1")) && (fragment3_edit5_typed.equals("1")) && (fragment3_edit6_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        String ss = s.toString();
                        if (after==0) {         // when a single character is deleted
                            if (s.charAt(start) == '/') {       // if the character is '/' , restore it and put the cursor at correct position
                                fragment3_edit5.setText(s);
                                fragment3_edit5.setSelection(start);
                            }
                            else if (s.charAt(start) == '-') {  // if the character is '-' , restore it and put the cursor at correct position
                                fragment3_edit5.setText(s);
                                fragment3_edit5.setSelection(start);
                            }
                            else if (ss.charAt(start) >= '0' && ss.charAt(start) <= '9') {  // if the character is a digit, replace it with '-'
                                ss = ss.substring(0, start) + "-" + ss.substring(start +1);
                                fragment3_edit5.setText(ss);
                                fragment3_edit5.setSelection(start);
                            }
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String ss = s.toString();
                        if (before==0 ){    // when a single character is added
                            if (fragment3_edit5.getSelectionStart()==3 || fragment3_edit5.getSelectionStart()==6) {
                                // if the new character was just before '/' character
                                // getSelection value gets incremented by 1, because text has been changed and hence cursor position updated
                                // Log.d("test", ss);
                                ss = ss.substring(0, start) + "/" + ss.charAt(start) + ss.substring(start + 3);
                                // Log.d("test", ss);
                                fragment3_edit5.setText(ss);
                                fragment3_edit5.setSelection(start + 2);
                            }
                            else {
                                if (fragment3_edit5.getSelectionStart()==11){
                                    // if cursor was at last, do not add anything
                                    ss = ss.substring(0,ss.length()-1);
                                    fragment3_edit5.setText(ss);
                                    fragment3_edit5.setSelection(10);
                                }
                                else {
                                    // else replace the next digit with the entered digit
                                    ss = ss.substring(0, start + 1) + ss.substring(start + 2);
                                    fragment3_edit5.setText(ss);
                                    fragment3_edit5.setSelection(start + 1);
                                }
                            }
                        }
                    }
                });
                fragment3_edit6.setText(date);
                fragment3_edit6.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment3_edit6.getText().toString())) {
                            date_distribution=fragment3_edit6.getText().toString();
                            fragment3_edit6_typed="1";

                        }
                        else{
                            fragment3_edit6_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped((fragment3_edit1_typed.equals("1")) && (fragment3_edit4_typed.equals("1")) && (fragment3_edit5_typed.equals("1")) && (fragment3_edit6_typed.equals("1")));
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        String ss = s.toString();
                        if (after==0) {         // when a single character is deleted
                            if (s.charAt(start) == '/') {       // if the character is '/' , restore it and put the cursor at correct position
                                fragment3_edit6.setText(s);
                                fragment3_edit6.setSelection(start);
                            }
                            else if (s.charAt(start) == '-') {  // if the character is '-' , restore it and put the cursor at correct position
                                fragment3_edit6.setText(s);
                                fragment3_edit6.setSelection(start);
                            }
                            else if (ss.charAt(start) >= '0' && ss.charAt(start) <= '9') {  // if the character is a digit, replace it with '-'
                                ss = ss.substring(0, start) + "-" + ss.substring(start +1);
                                fragment3_edit6.setText(ss);
                                fragment3_edit6.setSelection(start);
                            }
                        }
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String ss = s.toString();
                        if (before==0 ){    // when a single character is added
                            if (fragment3_edit6.getSelectionStart()==3 || fragment3_edit6.getSelectionStart()==6) {
                                // if the new character was just before '/' character
                                // getSelection value gets incremented by 1, because text has been changed and hence cursor position updated
                                // Log.d("test", ss);
                                ss = ss.substring(0, start) + "/" + ss.charAt(start) + ss.substring(start + 3);
                                // Log.d("test", ss);
                                fragment3_edit6.setText(ss);
                                fragment3_edit6.setSelection(start + 2);
                            }
                            else {
                                if (fragment3_edit6.getSelectionStart()==11){
                                    // if cursor was at last, do not add anything
                                    ss = ss.substring(0,ss.length()-1);
                                    fragment3_edit6.setText(ss);
                                    fragment3_edit6.setSelection(10);
                                }
                                else {
                                    // else replace the next digit with the entered digit
                                    ss = ss.substring(0, start + 1) + ss.substring(start + 2);
                                    fragment3_edit6.setText(ss);
                                    fragment3_edit6.setSelection(start + 1);
                                }
                            }
                        }
                    }
                });
            }
            else if (position == 3){
                RelativeLayout fragment4_no1 = container.findViewById(R.id.fragment4_no1);
                RelativeLayout fragment4_yes1 = container.findViewById(R.id.fragment4_yes1);
                RelativeLayout fragment4_no2 = container.findViewById(R.id.fragment4_no2);
                RelativeLayout fragment4_yes2 = container.findViewById(R.id.fragment4_yes2);
                RelativeLayout fragment4_no3 = container.findViewById(R.id.fragment4_no3);
                RelativeLayout fragment4_yes3 = container.findViewById(R.id.fragment4_yes3);
                RelativeLayout fragment4_no4 = container.findViewById(R.id.fragment4_no4);
                RelativeLayout fragment4_yes4 = container.findViewById(R.id.fragment4_yes4);
                RelativeLayout fragment4_no5 = container.findViewById(R.id.fragment4_no5);
                RelativeLayout fragment4_yes5 = container.findViewById(R.id.fragment4_yes5);
                fragment4_no1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_no1.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_yes1.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question1_picked="1";
                        question1_type="0";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });
                fragment4_yes1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_yes1.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_no1.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question1_picked="1";
                        question1_type="1";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });
                fragment4_no2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_no2.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_yes2.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question2_picked="1";
                        question2_type="0";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });
                fragment4_yes2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_yes2.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_no2.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question2_picked="1";
                        question2_type="1";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });
                fragment4_no3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_no3.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_yes3.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question3_picked="1";
                        question3_type="0";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("2")) && (question3_picked.equals("3")) && (question4_picked.equals("4")) && (question5_picked.equals("5")));
                    }
                });
                fragment4_yes3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_yes3.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_no3.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question3_picked="1";
                        question3_type="1";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });
                fragment4_no4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_no4.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_yes4.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question4_picked="1";
                        question4_type="0";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });
                fragment4_yes4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_yes4.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_no4.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question4_picked="1";
                        question4_type="1";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });
                fragment4_no5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_no5.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_yes5.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question5_picked="1";
                        question5_type="0";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });
                fragment4_yes5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment4_yes5.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_gold));
                        fragment4_no5.setBackgroundDrawable(ContextCompat.getDrawable(mContext, R.drawable.round_textedit_grey));
                        question5_picked="1";
                        question5_type="1";
                        isAllRequiredTypedd.setAllTyped((question1_picked.equals("1")) && (question2_picked.equals("1")) && (question3_picked.equals("1")) && (question4_picked.equals("1")) && (question5_picked.equals("1")));
                    }
                });



            }
            else if (position==4){
                EditText fragment5_edit1 = container.findViewById(R.id.fragment5_edit1);
                RichWysiwyg fragment5_edit2 = container.findViewById(R.id.fragment5_edit2);

                fragment5_edit2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!TextUtils.isEmpty(fragment5_edit2.getHtml())) {
                            instruction= fragment5_edit2.getHtml();

                        }
                        else{
                            fragment5_edit2_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped(fragment5_edit2_typed.equals("1"));
                    }
                });
                fragment5_edit1.addTextChangedListener(new TextWatcher() {

                    public void afterTextChanged(Editable s) {
                        if(!TextUtils.isEmpty(fragment5_edit1.getText().toString())) {
                            desc=fragment5_edit1.getText().toString();
                            fragment5_edit1_typed="1";

                        }
                        else{
                            fragment5_edit1_typed="0";

                        }
                        if(!TextUtils.isEmpty(fragment5_edit2.getHtml())) {
                            instruction= fragment5_edit2.getHtml();

                        }
                        else{
                            fragment5_edit2_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped(fragment5_edit1_typed.equals("1"));

                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    public void onTextChanged(CharSequence s, int start, int before, int count) {}
                });

                timer4.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if(!TextUtils.isEmpty(fragment5_edit2.getHtml())) {
                            instruction= fragment5_edit2.getHtml();

                        }
                        else{
                            fragment5_edit2_typed="0";

                        }
                        isAllRequiredTypedd.setAllTyped(fragment5_edit1_typed.equals("1"));
                    }
                }, 0, 1000); //wait 0 ms before doing the action and do it evry 1000ms (1second)



            }


            return view;
        }

        @Override
        public int getCount() {
            return layouts_airdrop_add.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }


}