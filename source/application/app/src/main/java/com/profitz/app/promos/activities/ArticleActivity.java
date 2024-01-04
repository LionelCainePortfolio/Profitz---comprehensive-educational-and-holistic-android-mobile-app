package com.profitz.app.promos.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.fragments.BottomSheetDialogReportBug;
import com.squareup.seismic.ShakeDetector;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ArticleActivity extends AppCompatActivity implements  BottomSheetDialogReportBug.BottomSheetListener  {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Context context = this;
    ImageView arrow_back;
    static User user;
    String user_id_contains_article_yes1;
    String user_id_contains_article_no1;
    int new_article_was_helpful_yess_new;
    int new_article_was_helpful_noo_new;
    int article_id;
    TextView articleHelpfulNo;
    TextView articleHelpfulYes;
    String article_name;
    ImageView click_yes;
    ImageView click_no;
    String article_desc;
    int article_was_helpful_yess =0;
    int article_was_helpful_noo=0;
    String count_no;
    String count_yes;
    public BottomSheetDialogReportBug report_dialog;
    private com.profitz.app.promos.ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBarDetail);

        super.onCreate(savedInstanceState);
        context = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        //getWindow().setStatusBarColor(Color.gold_gradient_shape);
        //Drawable background = MyInformationsActivity.getResources().getDrawable(R.drawable.gold_gradient_shape);
        Drawable background = ResourcesCompat.getDrawable(getResources(), R.drawable.gold_gradient_shape, null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(context, R.color.transparent));
        // getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.transparent));
        getWindow().setBackgroundDrawable(background);
        setContentView(R.layout.activity_help_article);


        Intent myIntent = getIntent(); // gets the previously created intent
        article_id = myIntent.getIntExtra("article_id", 0);
        article_name= myIntent.getStringExtra("article_name");
        article_desc = myIntent.getStringExtra("article_description");
        article_was_helpful_yess = myIntent.getIntExtra("article_was_helpful_yes", 0);
        article_was_helpful_noo = myIntent.getIntExtra("article_was_helpful_no", 0);

        TextView articleTitle = findViewById(R.id.articleTitle);
        articleTitle.setText(article_name);
        TextView articleDesc = findViewById(R.id.articleDesc);
        articleDesc.setText(Html.fromHtml(article_desc));

        TextView click_help = findViewById(R.id.text_2_rl2);


        articleHelpfulYes = findViewById(R.id.count_yes);
        count_yes = String.valueOf(article_was_helpful_yess);
        articleHelpfulYes.setText(count_yes);
        count_no = String.valueOf(article_was_helpful_noo);
        articleHelpfulNo = findViewById(R.id.count_no);
        articleHelpfulNo.setText(count_no);
        click_yes = findViewById(R.id.click_yes);
        click_no = findViewById(R.id.click_no);

        click_help.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(context, ChatSupportActivity.class));
            }
        });

        report_dialog = new BottomSheetDialogReportBug();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new com.profitz.app.promos.ShakeDetector();
        mShakeDetector.setOnShakeListener(new com.profitz.app.promos.ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                //tvShake.setText("Shake Action is just detected!!");

                if (report_dialog != null
                        && report_dialog.getDialog() != null
                        && report_dialog.getDialog().isShowing()
                        && !report_dialog.isRemoving()) {
                    //dialog is showing so do something
                } else {
                    Bundle args = new Bundle();
                    args.putString("source","ArticleActivity");
                    report_dialog.setArguments(args);
                    report_dialog.show(getSupportFragmentManager(), report_dialog.getTag());
                }
            }
        });


        click_yes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkArticle("yes");

            }
        });
        click_no.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkArticle("no");

            }
        });

        arrow_back = findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        updateViews();

    }
public void updateViews(){
    user = MyPreferenceManager.getInstance(ArticleActivity.this).getUser();

    final String urlUpdate = "https://yoururl.com/api/update_views_article.php?article_id="+article_id+"&user_id="+user.getId();
    JsonObjectRequest jsonObjectRequestUpdate = new JsonObjectRequest
            (com.android.volley.Request.Method.GET, urlUpdate, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String response_update = response.getString("response");
                        if (response_update.equals("1")) {
                            click_yes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_up_gold_24));
                            click_no.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_down_gold_24_deactive));
                            new_article_was_helpful_yess_new=article_was_helpful_yess;
                            articleHelpfulYes.setVisibility(View.VISIBLE);
                            articleHelpfulNo.setVisibility(View.VISIBLE);
                        }
                        else if(response_update.equals("2")) {
                            click_yes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_up_gold_24_deactive));
                            click_no.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_down_gold_24));
                            new_article_was_helpful_noo_new=article_was_helpful_noo;
                            articleHelpfulYes.setVisibility(View.VISIBLE);
                            articleHelpfulNo.setVisibility(View.VISIBLE);
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO: Handle error

                }
            });
                    Volley.newRequestQueue(context).add(jsonObjectRequestUpdate);
}
    public void checkArticle(String yes_no){
        user = MyPreferenceManager.getInstance(ArticleActivity.this).getUser();

        final String url2 = "https://yoururl.com/api/check_article.php?type="+yes_no+"&article_id="+article_id+"&user_id="+user.getId();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (yes_no.equals("yes")){
                                user_id_contains_article_yes1 = response.getString("user_id_contains_article_yes");
                                if (user_id_contains_article_yes1.equals("1")) { // kliknieto juz tak

                                    click_yes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_up_gold_24));
                                    click_no.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_down_gold_24));
                                    articleHelpfulYes.setVisibility(View.VISIBLE);
                                    articleHelpfulNo.setVisibility(View.VISIBLE);
                                    final String url3 = "https://yoururl.com/api/update_article.php?type_article=yes&type=down&article_id="+article_id;
                                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                            (com.android.volley.Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        String response_back = response.getString("response");
                                                        if (response_back.equals("1")){
                                                           // Toast.makeText(context, "Dziękujemy za opinię!", Toast.LENGTH_SHORT).show();

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
                                    Volley.newRequestQueue(context).add(jsonObjectRequest2);

                                    final String url332 = "https://yoururl.com/api/update_article_users_id.php?type_article=yes&article_id="+article_id+"&user_id="+user.getId();
                                    JsonObjectRequest jsonObjectRequest332 = new JsonObjectRequest
                                            (com.android.volley.Request.Method.GET, url332, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        String response_back = response.getString("response");
                                                        if (response_back.equals("1")){

                                                            new_article_was_helpful_yess_new = new_article_was_helpful_yess_new-1;
                                                            String new_count_yes = String.valueOf(new_article_was_helpful_yess_new);
                                                            articleHelpfulYes.setText(new_count_yes);
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
                                    Volley.newRequestQueue(context).add(jsonObjectRequest332);


                                }
                                else if (user_id_contains_article_yes1.equals("0")) {



                                    final String url33 = "https://yoururl.com/api/check_article.php?type=no&article_id="+article_id+"&status=dontaddid&user_id="+user.getId();
                                    JsonObjectRequest jsonObjectRequest33 = new JsonObjectRequest
                                            (com.android.volley.Request.Method.GET, url33, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                            String user_id_contains_article_no2 = response.getString("user_id_contains_article_no");

                                                        if (user_id_contains_article_no2.equals("1")) {
                                                                Log.d("sada","5");

                                                                //kliknieto nie
                                                                new_article_was_helpful_noo_new = new_article_was_helpful_noo_new-1;
                                                                String new_count_no = String.valueOf(new_article_was_helpful_noo_new);
                                                                articleHelpfulNo.setText(new_count_no);
                                                                    //odejmij 1 z no i  //usun z nie ID
                                                                    final String url331 = "https://yoururl.com/api/update_article.php?type_article=no&type=down&article_id=" + article_id;
                                                                    JsonObjectRequest jsonObjectRequest331 = new JsonObjectRequest
                                                                            (com.android.volley.Request.Method.GET, url331, null, new Response.Listener<JSONObject>() {

                                                                                @Override
                                                                                public void onResponse(JSONObject response) {
                                                                                    try {
                                                                                        String response_back = response.getString("response");
                                                                                        if (response_back.equals("1")) {

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
                                                                    Volley.newRequestQueue(context).add(jsonObjectRequest331);
                                                                final String url334 = "https://yoururl.com/api/update_article_users_id.php?type_article=no&article_id="+article_id+"&user_id="+user.getId();
                                                                JsonObjectRequest jsonObjectRequest334 = new JsonObjectRequest
                                                                        (com.android.volley.Request.Method.GET, url334, null, new Response.Listener<JSONObject>() {

                                                                            @Override
                                                                            public void onResponse(JSONObject response) {
                                                                                try {
                                                                                    String response_back = response.getString("response");
                                                                                    if (response_back.equals("1")){

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
                                                                Volley.newRequestQueue(context).add(jsonObjectRequest334);
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
                                    Volley.newRequestQueue(context).add(jsonObjectRequest33);



                                    new_article_was_helpful_yess_new = article_was_helpful_yess+1;
                                    String new_count_yes = String.valueOf(new_article_was_helpful_yess_new);
                                    articleHelpfulYes.setText(new_count_yes);
                                    click_yes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_up_gold_24));
                                    click_no.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_down_gold_24_deactive));
                                    articleHelpfulYes.setVisibility(View.VISIBLE);
                                    articleHelpfulNo.setVisibility(View.VISIBLE);

                                    final String url3 = "https://yoururl.com/api/update_article.php?type_article=yes&type=up&article_id="+article_id;
                                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                            (com.android.volley.Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        String response_back = response.getString("response");
                                                        if (response_back.equals("1")){
                                                          //  Toast.makeText(context, "Dziękujemy za opinię!", Toast.LENGTH_SHORT).show();
                                                            Toasty.success(context, "Dziękujemy za Twoją opinię!").show();

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
                                    Volley.newRequestQueue(context).add(jsonObjectRequest2);
                                }
                            }
                            else if (yes_no.equals("no")){
                                user_id_contains_article_no1 = response.getString("user_id_contains_article_no");

                                if (user_id_contains_article_no1.equals("1")) { // kliknieto juz nie

                                    click_yes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_up_gold_24));
                                    click_no.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_down_gold_24));
                                    articleHelpfulYes.setVisibility(View.VISIBLE);
                                    articleHelpfulNo.setVisibility(View.VISIBLE);

                                    final String url3 = "https://yoururl.com/api/update_article.php?type_article=no&type=down&article_id="+article_id;
                                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                            (com.android.volley.Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        String response_back = response.getString("response");
                                                        Log.d("sada","11");

                                                        if (response_back.equals("1")){
                                                            Log.d("sada","12");

                                                            new_article_was_helpful_noo_new= new_article_was_helpful_noo_new-1;
                                                            String new_count_no = String.valueOf(new_article_was_helpful_noo_new);
                                                            articleHelpfulNo.setText(new_count_no);
                                                           // Toast.makeText(context, "Dziękujemy za opinię!", Toast.LENGTH_SHORT).show();
                                                            Toasty.success(context, "Dziękujemy za Twoją opinię!").show();

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
                                    Volley.newRequestQueue(context).add(jsonObjectRequest2);
                                    final String url332 = "https://yoururl.com/api/update_article_users_id.php?type_article=no&article_id="+article_id+"&user_id="+user.getId();
                                    JsonObjectRequest jsonObjectRequest332 = new JsonObjectRequest
                                            (com.android.volley.Request.Method.GET, url332, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        String response_back = response.getString("response");

                                                        if (response_back.equals("1")){

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
                                    Volley.newRequestQueue(context).add(jsonObjectRequest332);


                                }
                                else if (user_id_contains_article_no1.equals("0")) {


                                    final String url33 = "https://yoururl.com/api/check_article.php?type=yes&status=dontaddid&article_id="+article_id+"&user_id="+user.getId();
                                    JsonObjectRequest jsonObjectRequest33 = new JsonObjectRequest
                                            (com.android.volley.Request.Method.GET, url33, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        Log.d("sada","16");

                                                        String user_id_contains_article_yes2 = response.getString("user_id_contains_article_yes");
                                                        if (user_id_contains_article_yes2.equals("1")) {
                                                            Log.d("sada","17");

                                                            new_article_was_helpful_yess_new = new_article_was_helpful_yess_new-1;
                                                            String new_count_yes = String.valueOf(new_article_was_helpful_yess_new);
                                                            articleHelpfulYes.setText(new_count_yes);
                                                            final String url331 = "https://yoururl.com/api/update_article.php?type_article=yes&type=down&article_id=" + article_id;
                                                            JsonObjectRequest jsonObjectRequest331 = new JsonObjectRequest
                                                                    (com.android.volley.Request.Method.GET, url331, null, new Response.Listener<JSONObject>() {

                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
                                                                            try {
                                                                                String response_back = response.getString("response");
                                                                                if (response_back.equals("1")) {

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
                                                            Volley.newRequestQueue(context).add(jsonObjectRequest331);
                                                            final String url334 = "https://yoururl.com/api/update_article_users_id.php?type_article=yes&article_id="+article_id+"&user_id="+user.getId();
                                                            JsonObjectRequest jsonObjectRequest334 = new JsonObjectRequest
                                                                    (com.android.volley.Request.Method.GET, url334, null, new Response.Listener<JSONObject>() {

                                                                        @Override
                                                                        public void onResponse(JSONObject response) {
                                                                            try {
                                                                                String response_back = response.getString("response");
                                                                                if (response_back.equals("1")){

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
                                                            Volley.newRequestQueue(context).add(jsonObjectRequest334);
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
                                    Volley.newRequestQueue(context).add(jsonObjectRequest33);



                                    new_article_was_helpful_noo_new = article_was_helpful_noo+1;
                                    String new_count_no = String.valueOf(new_article_was_helpful_noo_new);
                                    articleHelpfulNo.setText(new_count_no);
                                    click_yes.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_up_gold_24_deactive));
                                    click_no.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_baseline_thumb_down_gold_24));
                                    articleHelpfulYes.setVisibility(View.VISIBLE);
                                    articleHelpfulNo.setVisibility(View.VISIBLE);
                                    final String url3 = "https://yoururl.com/api/update_article.php?type_article=no&type=up&article_id="+article_id;
                                    JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                            (com.android.volley.Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {

                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        String response_back = response.getString("response");
                                                        if (response_back.equals("1")){
                                                           // Toast.makeText(context, "Dziękujemy za opinię!", Toast.LENGTH_SHORT).show();
                                                            Toasty.success(context, "Dziękujemy za Twoją opinię!").show();

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
                                    Volley.newRequestQueue(context).add(jsonObjectRequest2);
                                }

                            }
                           // String user_id_contains_article = response.getString("user_id_contains_article");

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
    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
