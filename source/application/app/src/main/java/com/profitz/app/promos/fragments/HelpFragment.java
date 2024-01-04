package com.profitz.app.promos.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.ArticleActivity;
import com.profitz.app.promos.adapters.AdapterTopics;
import com.profitz.app.promos.data.DataTopics;

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
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HelpFragment extends Fragment {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Context context;
    ArrayList<DataTopics> data;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRVTopicsList;
    private AdapterTopics mAdapter;
    ImageView search_button;
    RelativeLayout click_related1;
    RelativeLayout click_related2;
    RelativeLayout click_related3;
    RelativeLayout click_related4;
    RelativeLayout click_related5;
    private HashMap<String, List<String>> listDataChild;




    User user2;
    String username;
    private Context mContext;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_help, container, false);
            context = inflater.getContext();
            user2 = MyPreferenceManager.getInstance(mContext).getUser();
            int IDuser = Integer.parseInt(user2.getId());
            username = user2.getName();




            search_button = view.findViewById(R.id.search_button);
            search_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    BottomSheetDialogDisabledFuture bottomSheetDialogDisabledFuture = new BottomSheetDialogDisabledFuture();
                    bottomSheetDialogDisabledFuture.setArguments(args);
                    bottomSheetDialogDisabledFuture.show(getChildFragmentManager(), bottomSheetDialogDisabledFuture.getTag());

                }
            });
            mRVTopicsList = (RecyclerView) view.findViewById(R.id.topicsList);

            click_related1 = view.findViewById(R.id.first_quote_rl);
            click_related2 = view.findViewById(R.id.second_quote_rl);
            click_related3 = view.findViewById(R.id.third_quote_rl);
            click_related4 = view.findViewById(R.id.fourth_quote_rl);
            click_related5 = view.findViewById(R.id.fifth_quote_rl);

            click_related1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRelatedArticle(1);
                }
            });
            click_related2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRelatedArticle(7);
                }
            });
            click_related3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRelatedArticle(30);
                }
            });
            click_related4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRelatedArticle(1);
                }
            });
            click_related5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startRelatedArticle(1);
                }
            });



            new AsyncTopics().execute();

            return view;

        }
    public void startRelatedArticle(int articleId){

        String url = "https://yoururl.com/api/get_article_info.php?article_id="+articleId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String articleName = response.getString("article_name");
                            String articleDesc = response.getString("articleDesc");
                            String article_was_helpful_yes = response.getString("article_was_helpful_yes");
                            String article_was_helpful_no = response.getString("article_was_helpful_no");

                            Intent myIntent = new Intent(context, ArticleActivity.class);
                            myIntent.putExtra("article_name",articleName);
                            myIntent.putExtra("article_description",articleDesc);
                            myIntent.putExtra("article_id",articleId);
                            myIntent.putExtra("article_was_helpful_yes",article_was_helpful_yes);
                            myIntent.putExtra("article_was_helpful_no",article_was_helpful_no);
                            context.startActivity(myIntent);

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
        Volley.newRequestQueue(context).add(jsonObjectRequest);
    }

    private class AsyncTopics extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;

        LayoutInflater factory = LayoutInflater.from(context);
        View DialogView = factory.inflate(R.layout.custom_load_layout, null);
        Dialog main_dialog = new Dialog(context);


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
                User user = MyPreferenceManager.getInstance(context).getUser();
                url = new URL("https://yoururl.com/api/get_topics.php?user_id="+user.getId());

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
                    handler.postDelayed(this, 2500);
                }
            }, 2000);


            // pdLoading.dismiss();
            // pdLoading.dismiss();
            data = new ArrayList<>();

            FragmentManager fragmentManager = getChildFragmentManager();

            // Setup and Handover data to recyclerview
            mAdapter = new AdapterTopics(context, data,fragmentManager);
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataTopics topicsData = new DataTopics();
                    topicsData.topicImage= json_data.getString("topic_img");
                    topicsData.topicName= json_data.getString("topic_name");
                    topicsData.topicDesc= json_data.getString("topic_desc");
                    topicsData.topicCategoriesIds= json_data.getString("topic_categories_id");
                    topicsData.topicShowAs= json_data.getInt("topic_show_as");
                    topicsData.topicClicable= json_data.getInt("topic_clicable");
                    topicsData.topicVisibility= json_data.getInt("topic_visibility");
                    topicsData.topicClicks = json_data.getInt("topic_clicks");
                    data.add(topicsData);
                }

                mRVTopicsList.setAdapter(mAdapter);

                mRVTopicsList.setLayoutManager(new GridLayoutManager(context,1));
                mRVTopicsList.setNestedScrollingEnabled(false);


            } catch (JSONException e) {
               // Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                Log.d("error_help", e.toString());
                Toasty.error(context, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

            }

        }

    }

}