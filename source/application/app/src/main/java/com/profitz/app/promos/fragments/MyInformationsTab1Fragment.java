package com.profitz.app.promos.fragments;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.profitz.app.R;
import com.profitz.app.promos.CommentCardArrayAdapter;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.AwardsActivity;
import com.profitz.app.promos.activities.ChatsActivity;
import com.profitz.app.promos.activities.PointsActivity;
import com.profitz.app.promos.adapters.AdapterNotification;
import com.profitz.app.promos.adapters.DailyTaskRecyclerViewAdapter;
import com.profitz.app.promos.adapters.ECBackgroundSwitcherView;
import com.profitz.app.promos.adapters.ECCardData;
import com.profitz.app.promos.adapters.ECPagerView;
import com.profitz.app.promos.adapters.ECPagerViewAdapterPro;
import com.profitz.app.promos.data.DataNotification;
import com.profitz.app.promos.pojo.CardData;
import com.profitz.app.promos.pojo.CommentCard;
import com.profitz.app.promos.view.ItemsCountView;
import com.profitz.app.util.CircleTransform;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MyInformationsTab1Fragment extends Fragment  implements DailyTaskRecyclerViewAdapter.ItemClickListener, BottomSheetDialogUpgradeLevel.BottomSheetListener{
    String username;
    RecyclerView recyclerView;
    private DailyTaskRecyclerViewAdapter adapter;
    int actual_day;
    String actual_day_clicked;
    String status_check_update_day_clicked;
    double points_yesterday;
    ConstraintLayout relative_global_my_informations;
    int actual_day_return;
    private Context mContext;
    private String data_getNotificationCount;
    private String data_getPoints;
    private String data_getEarned;
    private String data_getNickname;
    private String data_getLevel;
    private String data_getImage;
    private String data_getDones;
    CircleImageView camera;
    TextView textViewNickname, textViewLevel, textViewId, textViewUsername, textViewEmail, textViewGender, textViewFavouriteCount, textViewDoneCount, textViewPoints, textViewEarned;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private RecyclerView mRVNotificationList;
    private AdapterNotification mAdapter;
    User user2;
    CircleImageView image_avatar_bg_static;
    ImageView chats;

    Animation animation_move_default_left_to_right3;
    Animation animation_move_default_left_to_right;

    Animation animation_move_default_left_to_right_2;
    Animation animation_move_camera_left_to_right;
    Animation animation_move_camera_left_to_right2;
    View view;
    RecyclerView NotificationList;
    String count_notifications;
    ImageView img_dailytask;
    int serverResponseCode = 0;
    public static final String KEY_User_Document1 = "doc1";
    CircleImageView imageView;
    private String Document_img1="";
    String upLoadServerUri = null;
    LinearLayout points_layout_info_rl;
    private static final int LOCATION_REQUEST = 222;
    private final String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int PICK_IMAGE = 2;
    TextView textViewPointsEur;
    ConstraintLayout constraint_global;
    LinearLayout points_layout_info;
    ECPagerViewAdapterPro adapterLevels;
    ImageView CircleimageView;
    String uploadFileName = "";
    private ECPagerView ecPagerView;
    ImageView arrow_back;
    private EditText inputName;
    private TextInputLayout inputLayoutName;
    private EditText inputLastName;
    private TextInputLayout inputLayoutLastName;
    public MyInformationsTab1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.my_informations_tab1_fragment_one, container, false);

            user2 = MyPreferenceManager.getInstance(mContext).getUser();
            username = user2.getName();
            uploadFileName = user2.getUsername()+".png";
            final Animation animShake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
            img_dailytask = view.findViewById(R.id.imageDailyTask);
            img_dailytask.startAnimation(animShake);
            constraint_global = view.findViewById(R.id.scroll_layout);
            points_layout_info_rl = view.findViewById(R.id.points_layout_info_rl);
            chats = view.findViewById(R.id.chats);
            getData2();
            // data to populate the RecyclerView with
            getData3();
            textViewPointsEur = view.findViewById(R.id.text_calculate_earn);
            points_layout_info = view.findViewById(R.id.points_layout_info);
            final Handler handler = new Handler();
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    final Animation animShake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
                    img_dailytask = view.findViewById(R.id.imageDailyTask);
                    img_dailytask.startAnimation(animShake);
                    handler.postDelayed(this, 10000);
                }
            };
            handler.post(run);
            NotificationList = view.findViewById(R.id.NotificationList);
            mRVNotificationList = view.findViewById(R.id.NotificationList);
            getData();
            chats.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChatsActivity.class);
                    startActivity(intent);
                }
            });


            final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user2.getId();

            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                data_getLevel = response.getString("user_level");


                                adapterLevels = new ECPagerViewAdapterPro(mContext, new ExampleDataset(data_getLevel).getDataset()) {


                                    @Override
                                    public void instantiateCard(LayoutInflater inflaterService, ViewGroup head, ListView list, final ECCardData data, int position) {




                                        final CardData cardData = (CardData) data;
                                        // Create adapter for list inside a card and set adapter to card content
                                        CommentCardArrayAdapter commentCardArrayAdapter = new CommentCardArrayAdapter(mContext, cardData.getListItems(), data_getLevel, position);
                                        list.setAdapter(commentCardArrayAdapter);
                                        list.setEnabled(false);
                                                 list.setSelector(R.color.transparent);
                                        list.setBackgroundColor(Color.WHITE);
                                        list.setCacheColorHint(Color.TRANSPARENT);

                                        // Add gradient to root header view
                                        View gradient = new View(mContext);
                                        gradient.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
                                        gradient.setBackgroundResource(R.drawable.not_found_error);
                                        head.addView(gradient);
                                        // inflaterService.inflate(R.layout.simple_head, head);
                                        final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user2.getId();

                                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                                    @Override
                                                    public void onResponse(JSONObject response) {
                                                        try {
                                                            // Inflate main hea     der layout and attach it to header root view
                                                            data_getPoints = response.getString("points");
                                                            DecimalFormat precision = new DecimalFormat("0.00");
                                                            data_getPoints = response.getString("points");
                                                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                                                            textViewPoints.setText(data_getPoints);
                                                            double points_double = Double.parseDouble(data_getPoints);
                                                            double calculate_points_to_eur=(points_double/10);

                                                            String calculated = precision.format(calculate_points_to_eur);
                                                            String calculation = "Planowany przelicznik na EURO = "+ calculated + " eur";
                                                            textViewPointsEur.setText(calculation);

                                                            data_getEarned = response.getString("earned");
                                                            data_getNickname = response.getString("username");
                                                            data_getLevel = response.getString("user_level");
                                                            data_getImage = response.getString("image_url");
                                                            data_getDones = response.getString("done_count");

                                                            int dones_double = Integer.parseInt(data_getDones);
                                                            if (position == 0) {
                                                                inflaterService.inflate(R.layout.simple_head, head);



                                                                TextView needed_text = (TextView) head.findViewById(R.id.needed_text);
                                                                TextView text_points_needed = (TextView) head.findViewById(R.id.text_points_needed);
                                                                TextView text_promos_needed = (TextView) head.findViewById(R.id.text_promos_needed);
                                                                ProgressBar pBarPoints = (ProgressBar) head.findViewById(R.id.pBar3);
                                                                ProgressBar pBarPromos = (ProgressBar) head.findViewById(R.id.pBar4);


                                                                    double calculate_to_percent = points_double*10;
                                                                int points_double_string_to_int = (int) calculate_to_percent;

                                                                 //int points_double_string_to_int;
                                                                    //String points_double_string_new = String.valueOf(points_double_to_int);
                                                                   //points_double_string_new = points_double_string_new.substring(2);
                                                                  //  points_double_string_to_int = points_double_to_int;




                                                                TextView nickname = (TextView) head.findViewById(R.id.nickname);
                                                                TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id);
                                                                CircleimageView = (ImageView) head.findViewById(R.id.image_avatar);

                                                                image_avatar_bg_static = head.findViewById(R.id.image_avatar_bg_static);
                                                                ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus2);
                                                                camera = (CircleImageView) head.findViewById(R.id.image_camera);


                                                                if (data_getLevel.equals("0")){
                                                                    // if level 0
                                                                    //points needed 0.7
                                                                    //promos needed 1


                                                                    nickname.setText(data_getNickname);
                                                                    img_level.setImageResource(R.drawable.level_zero);
                                                                    level_text.setText("Nowy Partner");
                                                                    int lvl_1_points_needed = 10;
                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int lvl1_promos_needed = 1;
                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double; // np. 1-14

                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if (points_double > lvl_1_points_needed)
                                                                    {
                                                                        String text_points_needed_string = lvl_1_points_needed+"/"+lvl_1_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_points_needed_string = points_double+"/"+lvl_1_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }


                                                                    if (calculate_points_needed==0)
                                                                    {
                                                                        pBarPoints.setProgress(100,true);
                                                                    }
                                                                    else{
                                                                        if (points_double <= lvl_1_points_needed)
                                                                        {
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                            pBarPoints.setProgress(points_double_string_to_int,true);
                                                                        }
                                                                        else if (points_double >= lvl_1_points_needed)
                                                                        {
                                                                            pBarPoints.setProgress(100,true);
                                                                        }
                                                                    }


                                                                    if (dones_double > lvl1_promos_needed)
                                                                    {
                                                                        String text_promos_needed_string = lvl1_promos_needed+"/"+lvl1_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_promos_needed_string = dones_double+"/"+lvl1_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }

                                                                    if (calculate_promos_needed==0){
                                                                        pBarPromos.setProgress(100);
                                                                    }
                                                                    else{
                                                                        if (dones_double <= lvl1_promos_needed)
                                                                        {
                                                                            if (dones_double == 0)
                                                                            {
                                                                                pBarPromos.setProgress(0,true);

                                                                            }
                                                                            else if (dones_double == 1)
                                                                            {
                                                                                pBarPromos.setProgress(100,true);
                                                                            }
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else if (dones_double > lvl1_promos_needed)
                                                                        {
                                                                            pBarPromos.setProgress(100,true);
                                                                        }
                                                                    }



                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję aby uzyskać 1 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje aby uzyskać 1 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji aby uzyskać 2 Poziom Partnera!");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, aby uzyskać 2 Poziom Partnera!");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocję aby uzyskać 2 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje aby uzyskać 2 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji aby uzyskać 2 Poziom Partnera!");

                                                                        }
                                                                    }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać 1 Poziom Partnera!");
                                                                    }
                                                                }
                                                                else if (data_getLevel.equals("1")){

                                                                    nickname.setText(data_getNickname);
                                                                    img_level.setImageResource(R.drawable.level_one);
                                                                    level_text.setText("Pierwszy Poziom Partnera");
                                                                    // if level 1
                                                                    //points needed 2
                                                                    //promos needed 3
                                                                    int lvl_2_points_needed = 20;
                                                                    double calculate_points_needed = lvl_2_points_needed - points_double;
                                                                    int lvl2_promos_needed = 3;
                                                                    int calculate_promos_needed = lvl2_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);


                                                                    if (points_double > lvl_2_points_needed)
                                                                    {
                                                                        String text_points_needed_string = lvl_2_points_needed+"/"+lvl_2_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_points_needed_string = points_double+"/"+lvl_2_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }


                                                                    if (calculate_points_needed==0)
                                                                    {
                                                                        pBarPoints.setProgress(100,true);
                                                                    }
                                                                    else{
                                                                        if (points_double <= lvl_2_points_needed)
                                                                        {
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                            pBarPoints.setProgress(points_double_string_to_int,true);
                                                                        }
                                                                        else if (points_double >= lvl_2_points_needed)
                                                                        {
                                                                            pBarPoints.setProgress(100,true);
                                                                        }
                                                                    }


                                                                    if (dones_double > lvl2_promos_needed)
                                                                    {
                                                                        String text_promos_needed_string = lvl2_promos_needed+"/"+lvl2_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_promos_needed_string = dones_double+"/"+lvl2_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }

                                                                    if (calculate_promos_needed==0){
                                                                        pBarPromos.setProgress(100);
                                                                    }
                                                                    else{
                                                                        if (dones_double <= lvl2_promos_needed)
                                                                        {
                                                                            if (dones_double == 0)
                                                                            {
                                                                                pBarPromos.setProgress(0,true);

                                                                            }
                                                                            else if (dones_double == 3)
                                                                            {
                                                                                pBarPromos.setProgress(100,true);
                                                                            }
                                                                            else if (dones_double == 1){
                                                                                pBarPromos.setProgress(33,true);

                                                                            }
                                                                              else if (dones_double == 2){
                                                                                pBarPromos.setProgress(66,true);

                                                                            }
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else if (dones_double > lvl2_promos_needed)
                                                                        {
                                                                            pBarPromos.setProgress(100,true);
                                                                        }
                                                                    }


                                                                    if ((points_double<lvl_2_points_needed) && (dones_double<lvl2_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję aby uzyskać 2 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje aby uzyskać 2 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji aby uzyskać 2 Poziom Partnera!");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_2_points_needed) && (dones_double>=lvl2_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, aby uzyskać 2 Poziom Partnera!");
                                                                    }
                                                                    else if ((points_double>=lvl_2_points_needed) && (dones_double<lvl2_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocję aby uzyskać 2 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje aby uzyskać 2 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji aby uzyskać 2 Poziom Partnera!");

                                                                        }
                                                                    }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać 2 Poziom Partnera!");
                                                                    }
                                                                }
                                                                else if (data_getLevel.equals("2")){

                                                                    nickname.setText(data_getNickname);
                                                                    img_level.setImageResource(R.drawable.level_two);
                                                                    level_text.setText("Drugi Poziom Partnera");
                                                                    // if level 2
                                                                    //points needed 5
                                                                    //promos needed 5
                                                                    int lvl_3_points_needed = 30;

                                                                    double calculate_points_needed = lvl_3_points_needed - points_double;
                                                                    int lvl3_promos_needed = 5;
                                                                    int calculate_promos_needed = lvl3_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);
                                                                    int level3_points_double_string_to_int = points_double_string_to_int/2;


                                                            if (points_double > lvl_3_points_needed)
                                                                    {
                                                                        String text_points_needed_string = lvl_3_points_needed+"/"+lvl_3_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_points_needed_string = points_double+"/"+lvl_3_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }


                                                                    if (calculate_points_needed==0)
                                                                    {
                                                                        pBarPoints.setProgress(100,true);
                                                                    }
                                                                    else{
                                                                        if (points_double <= lvl_3_points_needed)
                                                                        {
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                            pBarPoints.setProgress(level3_points_double_string_to_int,true);
                                                                        }
                                                                        else if (points_double >= lvl_3_points_needed)
                                                                        {
                                                                            pBarPoints.setProgress(100,true);
                                                                        }
                                                                    }


                                                                    if (dones_double > lvl3_promos_needed)
                                                                    {
                                                                        String text_promos_needed_string = lvl3_promos_needed+"/"+lvl3_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_promos_needed_string = dones_double+"/"+lvl3_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }

                                                                    if (calculate_promos_needed==0){
                                                                        pBarPromos.setProgress(100);
                                                                    }
                                                                    else{
                                                                        if (dones_double <= lvl3_promos_needed)
                                                                        {
                                                                            if (dones_double == 0)
                                                                            {
                                                                                pBarPromos.setProgress(0,true);

                                                                            }
                                                                            else if (dones_double == 5)
                                                                            {
                                                                                pBarPromos.setProgress(100,true);
                                                                            }
                                                                            else if (dones_double == 1){
                                                                                pBarPromos.setProgress(20,true);

                                                                            }
                                                                            else if (dones_double == 4){
                                                                                pBarPromos.setProgress(80,true);

                                                                            }
                                                                            else if (dones_double == 3){
                                                                                pBarPromos.setProgress(60,true);

                                                                            }
                                                                              else if (dones_double == 2){
                                                                                pBarPromos.setProgress(40,true);

                                                                            }
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else if (dones_double > lvl3_promos_needed)
                                                                        {
                                                                            pBarPromos.setProgress(100,true);
                                                                        }
                                                                    }




                                                                    if ((points_double<lvl_3_points_needed) && (dones_double<lvl3_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję aby uzyskać 3 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje aby uzyskać 3 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji aby uzyskać 3 Poziom Partnera!");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_3_points_needed) && (dones_double>=lvl3_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, aby uzyskać 3 Poziom Partnera!");
                                                                    }
                                                                    else if ((points_double>=lvl_3_points_needed) && (dones_double<lvl3_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocję aby uzyskać 3 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje aby uzyskać 3 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji aby uzyskać 3 Poziom Partnera!");

                                                                        }                                            }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać 3 Poziom Partnera!");



                                                                    }
                                                                }
                                                                else if (data_getLevel.equals("3")){

                                                                    nickname.setText(data_getNickname);
                                                                    img_level.setImageResource(R.drawable.level_three);
                                                                    level_text.setText("Trzeci Poziom Partnera");
                                                                    // if level 3
                                                                    //points needed 10
                                                                    //promos needed 8
                                                                    int lvl_4_points_needed = 40;
                                                                    double calculate_points_needed = lvl_4_points_needed - points_double;
                                                                    int lvl4_promos_needed = 8;
                                                                    int calculate_promos_needed = lvl4_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);
                                                                    int level4_points_double_string_to_int = points_double_string_to_int/4;


                                                                    if (points_double > lvl_4_points_needed)
                                                                    {
                                                                        String text_points_needed_string = lvl_4_points_needed+"/"+lvl_4_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_points_needed_string = points_double+"/"+lvl_4_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }


                                                                    if (calculate_points_needed==0)
                                                                    {
                                                                        pBarPoints.setProgress(100,true);
                                                                    }
                                                                    else{
                                                                        if (points_double <= lvl_4_points_needed)
                                                                        {
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                            pBarPoints.setProgress(level4_points_double_string_to_int,true);
                                                                        }
                                                                        else if (points_double >= lvl_4_points_needed)
                                                                        {
                                                                            pBarPoints.setProgress(100,true);
                                                                        }
                                                                    }


                                                                    if (dones_double > lvl4_promos_needed)
                                                                    {
                                                                        String text_promos_needed_string = lvl4_promos_needed+"/"+lvl4_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_promos_needed_string = dones_double+"/"+lvl4_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }

                                                                    if (calculate_promos_needed==0){
                                                                        pBarPromos.setProgress(100);
                                                                    }
                                                                    else{
                                                                        if (dones_double <= lvl4_promos_needed)
                                                                        {
                                                                            if (dones_double == 0)
                                                                            {
                                                                                pBarPromos.setProgress(0,true);

                                                                            }
                                                                            else if (dones_double == 8)
                                                                            {
                                                                                pBarPromos.setProgress(100,true);
                                                                            }
                                                                            else if (dones_double == 1){
                                                                                pBarPromos.setProgress(12,true);

                                                                            }
                                                                            else if (dones_double == 4){
                                                                                pBarPromos.setProgress(50,true);

                                                                            }
                                                                            else if (dones_double == 3){
                                                                                pBarPromos.setProgress(37,true);

                                                                            }
                                                                            else if (dones_double == 2){
                                                                                pBarPromos.setProgress(25,true);

                                                                            }
                                                                            else if (dones_double == 5){
                                                                                pBarPromos.setProgress(62,true);

                                                                            }
                                                                            else if (dones_double == 6){
                                                                                pBarPromos.setProgress(75,true);

                                                                            }
                                                                            else if (dones_double == 7){
                                                                                pBarPromos.setProgress(87,true);

                                                                            }
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else if (dones_double > lvl4_promos_needed)
                                                                        {
                                                                            pBarPromos.setProgress(100,true);
                                                                        }
                                                                    }
                                                                    if ((points_double<lvl_4_points_needed) && (dones_double<lvl4_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję aby uzyskać 4 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje aby uzyskać 4 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji aby uzyskać 4 Poziom Partnera!");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_4_points_needed) && (dones_double>=lvl4_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, aby uzyskać 4 Poziom Partnera!");
                                                                    }
                                                                    else if ((points_double>=lvl_4_points_needed) && (dones_double<lvl4_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocję aby uzyskać 4 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje aby uzyskać 4 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji aby uzyskać 4 Poziom Partnera!");

                                                                        }                                            }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać 4 Poziom Partnera!");
                                                                    }
                                                                }
                                                                else if (data_getLevel.equals("4")){
                                                                    // if level 4
                                                                    //points needed 25
                                                                    //promos needed 12
                                                                     nickname.setText(data_getNickname);
                                                                    img_level.setImageResource(R.drawable.level_four);
                                                                    level_text.setText("Czwarty Poziom Partnera");

                                                                    int lvl_5_points_needed = 50;
                                                                    double calculate_points_needed = lvl_5_points_needed - points_double;
                                                                    int lvl5_promos_needed = 12;
                                                                    int calculate_promos_needed = lvl5_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);
                                                                    int level5_points_double_string_to_int = points_double_string_to_int/5;

                                                                    if (points_double > lvl_5_points_needed)
                                                                    {
                                                                        String text_points_needed_string = lvl_5_points_needed+"/"+lvl_5_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_points_needed_string = points_double+"/"+lvl_5_points_needed+" pkt";
                                                                        text_points_needed.setText(text_points_needed_string);

                                                                    }


                                                                    if (calculate_points_needed==0)
                                                                    {
                                                                        pBarPoints.setProgress(100,true);
                                                                    }
                                                                    else{
                                                                        if (points_double <= lvl_5_points_needed)
                                                                        {
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                            pBarPoints.setProgress(level5_points_double_string_to_int,true);
                                                                        }
                                                                        else if (points_double >= lvl_5_points_needed)
                                                                        {
                                                                            pBarPoints.setProgress(100,true);
                                                                        }
                                                                    }


                                                                    if (dones_double > lvl5_promos_needed)
                                                                    {
                                                                        String text_promos_needed_string = lvl5_promos_needed+"/"+lvl5_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }
                                                                    else{
                                                                        String text_promos_needed_string = dones_double+"/"+lvl5_promos_needed;
                                                                        text_promos_needed.setText(text_promos_needed_string);

                                                                    }

                                                                    if (calculate_promos_needed==0){
                                                                        pBarPromos.setProgress(100);
                                                                    }
                                                                    else{
                                                                        if (dones_double <= lvl5_promos_needed)
                                                                        {
                                                                            if (dones_double == 0)
                                                                            {
                                                                                pBarPromos.setProgress(0,true);

                                                                            }
                                                                            else if (dones_double == 12)
                                                                            {
                                                                                pBarPromos.setProgress(100,true);
                                                                            }
                                                                            else if (dones_double == 1){
                                                                                pBarPromos.setProgress(8,true);

                                                                            }
                                                                            else if (dones_double == 2){
                                                                                pBarPromos.setProgress(16,true);

                                                                            }
                                                                            else if (dones_double == 3){
                                                                                pBarPromos.setProgress(25,true);

                                                                            }
                                                                            else if (dones_double == 4){
                                                                                pBarPromos.setProgress(33,true);

                                                                            }
                                                                            else if (dones_double == 5){
                                                                                pBarPromos.setProgress(41,true);

                                                                            }
                                                                            else if (dones_double == 6){
                                                                                pBarPromos.setProgress(50,true);

                                                                            }
                                                                            else if (dones_double == 7){
                                                                                pBarPromos.setProgress(58,true);

                                                                            }
                                                                            else if (dones_double == 8){
                                                                                pBarPromos.setProgress(66,true);

                                                                            }
                                                                            else if (dones_double == 9){
                                                                                pBarPromos.setProgress(75,true);

                                                                            }
                                                                            else if (dones_double == 10){
                                                                                pBarPromos.setProgress(83,true);

                                                                            }
                                                                            else if (dones_double == 11){
                                                                                pBarPromos.setProgress(91,true);

                                                                            }
                                                                            // Toast.makeText(mContext, Html.fromHtml("<font color='#ffffff' ><b>" + points_double_string_to_int + "</b></font>"), Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else if (dones_double > lvl5_promos_needed)
                                                                        {
                                                                            pBarPromos.setProgress(100,true);
                                                                        }
                                                                    }
                                                                    if ((points_double<lvl_5_points_needed) && (dones_double<lvl5_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję aby uzyskać 5 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje aby uzyskać 5 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji aby uzyskać 5 Poziom Partnera!");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_5_points_needed) && (dones_double>=lvl5_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, aby uzyskać 5 Poziom Partnera!");
                                                                    }
                                                                    else if ((points_double>=lvl_5_points_needed) && (dones_double<lvl5_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocję aby uzyskać 5 Poziom Partnera!");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje aby uzyskać 5 Poziom Partnera!");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji aby uzyskać 5 Poziom Partnera!");

                                                                        }                                            }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać 5 Poziom Partnera!");
                                                                    }
                                                                }
                                                                else if (data_getLevel.equals("5")){
                                                                    // if level 5
                                                                    //points needed 0
                                                                    //promos needed 0
                                                                    int lvl_5_points_needed = 25;
                                                                    double calculate_points_needed = lvl_5_points_needed - points_double;
                                                                    int lvl5_promos_needed = 12;

                                                                    pBarPoints.setProgress(100,true);
                                                                    pBarPromos.setProgress(100, true);

                                                                    String text_promos_needed_string = "12/12";
                                                                    text_promos_needed.setText(text_promos_needed_string);
                                                                    String text_points_needed_string = "25/25";
                                                                    text_points_needed.setText(text_points_needed_string);

                                                                    needed_text.setText("Osiągnąłeś 5 poziom partnera!");


                                                                nickname.setText(data_getNickname);
                                                                img_level.setImageResource(R.drawable.level_five);
                                                                level_text.setText("Piąty Poziom Partnera");
                                                                }

                                                                if (data_getImage.contains("default"))
                                                                {

                                                                    animation_move_default_left_to_right3 = AnimationUtils.loadAnimation(mContext,
                                                                            R.anim.move_default_left_to_right3);
                                                                    animation_move_default_left_to_right = AnimationUtils.loadAnimation(mContext,
                                                                            R.anim.move_default_left_to_right);
                                                                    animation_move_default_left_to_right_2 = AnimationUtils.loadAnimation(mContext,
                                                                            R.anim.move_default_left_to_right2);
                                                                    animation_move_camera_left_to_right= AnimationUtils.loadAnimation(mContext,
                                                                            R.anim.move_camera_left_to_right);

                                                                    Handler handler = new Handler();
                                                                    handler.postDelayed(new Runnable() {
                                                                        public void run() {
                                                                            CircleimageView.setVisibility(View.VISIBLE);

                                                                            CircleimageView.startAnimation(animation_move_default_left_to_right);
                                                                        }
                                                                    }, 3500);   //5 seconds

                                                                    animation_move_default_left_to_right.setAnimationListener(new Animation.AnimationListener() {
                                                                        @Override
                                                                        public void onAnimationStart(Animation animation) {

                                                                        }

                                                                        @Override
                                                                        public void onAnimationEnd(Animation animation) {
                                                                            camera.startAnimation(animation_move_camera_left_to_right);
                                                                            CircleimageView.startAnimation(animation_move_default_left_to_right_2);
                                                                            handler.postDelayed(new Runnable() {
                                                                                public void run() {
                                                                                    animation_move_default_left_to_right_2.setFillAfter(true);
                                                                                    animation_move_default_left_to_right_2.cancel();
                                                                                    camera.clearAnimation();
                                                                                    CircleimageView.clearAnimation();
                                                                                    CircleimageView.clearAnimation();
                                                                                    runstopanim();
                                                                                }
                                                                            }, 10000);


                                                                            animation_move_default_left_to_right_2.setAnimationListener(new Animation.AnimationListener() {
                                                                                @Override
                                                                                public void onAnimationStart(Animation animation) {

                                                                                }

                                                                                @Override
                                                                                public void onAnimationEnd(Animation animation) {  handler.postDelayed(new Runnable() {
                                                                                    public void run() {
                                                                                        CircleimageView.startAnimation(animation_move_default_left_to_right);
                                                                                        animation_move_default_left_to_right.setAnimationListener(new Animation.AnimationListener() {
                                                                                            @Override
                                                                                            public void onAnimationStart(Animation animation) {

                                                                                            }

                                                                                            @Override
                                                                                            public void onAnimationEnd(Animation animation) {
                                                                                                camera.startAnimation(animation_move_camera_left_to_right);
                                                                                                CircleimageView.startAnimation(animation_move_default_left_to_right_2);

                                                                                                handler.postDelayed(new Runnable() {
                                                                                                    public void run() {
                                                                                                        animation_move_default_left_to_right_2.setFillAfter(true);
                                                                                                        animation_move_default_left_to_right_2.cancel();
                                                                                                        camera.clearAnimation();
                                                                                                        CircleimageView.clearAnimation();
                                                                                                        CircleimageView.clearAnimation();
                                                                                                    }
                                                                                                }, 3500);   //5 seconds


                                                                                            }

                                                                                            @Override
                                                                                            public void onAnimationRepeat(Animation animation) {

                                                                                            }
                                                                                        });

                                                                                    }
                                                                                }, 3000);   //5 seconds

                                                                                    camera.clearAnimation();
                                                                                    CircleimageView.clearAnimation();
                                                                                    animation_move_camera_left_to_right.cancel();
                                                                                    animation_move_default_left_to_right.cancel();
                                                                                    animation_move_default_left_to_right_2.cancel();
                                                                                }

                                                                                @Override
                                                                                public void onAnimationRepeat(Animation animation) {

                                                                                }
                                                                            });
                                                                        }

                                                                        @Override
                                                                        public void onAnimationRepeat(Animation animation) {

                                                                        }


                                                                    });





                                                                }

                                                                Picasso.with(mContext).load(data_getImage)
                                                                        .transform(new CircleTransform())
                                                                        .placeholder(R.drawable.user_placeholder_avatar)
                                                                        .error(R.drawable.user_placeholder_avatar)
                                                                        .skipMemoryCache()
                                                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                                                        .into(CircleimageView);
                                                                //messageText.setText("Uploading file path :- '/mnt/sdcard/"+uploadFileName+"'");


                                                                upLoadServerUri = "https://www.profitz.app/api/api/UploadToServer.php";

                                                                CircleimageView.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {

                                                                        if (EasyPermissions.hasPermissions(getActivity(), galleryPermissions)) {
                                                                            selectImage();
                                                                        } else {
                                                                            EasyPermissions.requestPermissions(getActivity(), "Dostęp do galerii", 101, galleryPermissions);
                                                                        }

                                                                    }
                                                                });
                                                                //String imageUrl = "https://yoururl.com/api/images/users/" + IDuser + ".png?type=small";
                                /*Picasso.with(mContext).load(data_getImage)
                                        .transform(new CircleTransform())
                                        .placeholder(R.drawable.user_placeholder_avatar)
                                        .error(R.drawable.user_placeholder_avatar)
                                        .skipMemoryCache()
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .into(imageView);*/
                                                                // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                                                                // textViewFavouriteCount.setText(data_getFavoriteCount);
                                                                textViewPoints.setText(data_getPoints);
                                                                // textViewDoneCount.setText(data_getDoneCount);
                                                                // textViewEarned.setText(data_getEarned);
                                                                //textViewNickname.setText(data_getNickname);
                                                                textViewLevel.setText(data_getLevel);

                                                            }

                                                            if (data_getLevel.equals("0")){
                                                                if (position == 1) {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 10;
                                                                    int lvl1_promos_needed = 1;
                                                                    String which_level = "1";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać "+which_level+" Poziom Partnera! Kliknij by ulepszyć.");
                                                                        upgrade_now_text.setText("Ulepsz teraz!");
                                                                        upgrade_level.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                Bundle args = new Bundle();
                                                                                BottomSheetDialogUpgradeLevel bottomSheet = new BottomSheetDialogUpgradeLevel();
                                                                                args.putString("points", String.valueOf(lvl_1_points_needed));
                                                                                args.putString("level", which_level);

                                                                                bottomSheet.setArguments(args);
                                                                                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                                else if (position == 2)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 20;
                                                                    int lvl1_promos_needed = 3;
                                                                    String which_level = "2";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                        
                                                                       if (data_getLevel.equals("0")) {
                                                                            needed_text.setText("Musisz najpierw uzyskać 1 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                        }
                                                                    }


                                                                }
                                                                else if (position == 3)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());
                                                                    double lvl_1_points_needed = 30;
                                                                    int lvl1_promos_needed = 5;
                                                                    String which_level = "3";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                        if (data_getLevel.equals("0")) {
                                                                            needed_text.setText("Musisz najpierw uzyskać 2 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                        }
                                                                    }

                                                                }
                                                                else if (position == 4)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 40;
                                                                    int lvl1_promos_needed = 8;
                                                                    String which_level = "4";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else {
                                                                        if (data_getLevel.equals("0")) {
                                                                            needed_text.setText("Musisz najpierw uzyskać 3 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                        }
                                                                    }

                                                                }
                                                                else if (position == 5)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 50;
                                                                    int lvl1_promos_needed = 12;
                                                                    String which_level = "5";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                       if (data_getLevel.equals("0")) {
                                                                            needed_text.setText("Musisz najpierw uzyskać 4 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                        }
                                                                    }

                                                                }
                                                            }

                                                            else if (data_getLevel.equals("1")){
                                                                if (position == 1) {
                                                                inflaterService.inflate(R.layout.simple_head_other, head);
                                                                TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 20;
                                                                    int lvl1_promos_needed = 3;
                                                                    String which_level = "2";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać "+which_level+" Poziom Partnera! Kliknij by ulepszyć.");
                                                                        upgrade_now_text.setText("Ulepsz teraz!");
                                                                        upgrade_level.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                Bundle args = new Bundle();
                                                                                BottomSheetDialogUpgradeLevel bottomSheet = new BottomSheetDialogUpgradeLevel();
                                                                                args.putString("points", String.valueOf(lvl_1_points_needed));
                                                                                args.putString("level", which_level);

                                                                                bottomSheet.setArguments(args);
                                                                                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                                else if (position == 2)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());

                                                                        double lvl_1_points_needed = 30;
                                                                        int lvl1_promos_needed = 5;
                                                                        String which_level = "3";

                                                                        double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                        int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                        String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                        if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }                                            }
                                                                        else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                        }
                                                                        else{
                                                                            if (data_getLevel.equals("1")) {
                                                                                needed_text.setText("Musisz najpierw uzyskać 2 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                            }
                                                                        }


                                                                }
                                                                else if (position == 3)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());
                                                                        double lvl_1_points_needed = 40;
                                                                        int lvl1_promos_needed = 8;
                                                                        String which_level = "4";

                                                                        double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                        int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                        String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                        if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }                                            }
                                                                        else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                        }
                                                                        else{
                                                                            if (data_getLevel.equals("1")) {
                                                                                needed_text.setText("Musisz najpierw uzyskać 3 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                            }
                                                                        }

                                                                }
                                                                else if (position == 4)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());

                                                                        double lvl_1_points_needed = 50;
                                                                        int lvl1_promos_needed = 12;
                                                                        String which_level = "5";

                                                                        double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                        int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                        String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                        if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }                                            }
                                                                        else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                        }
                                                                        else{
                                                                            if (data_getLevel.equals("1")) {
                                                                                needed_text.setText("Musisz najpierw uzyskać 4 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                            }
                                                                        }

                                                                }
                                                             }
                                                            else if (data_getLevel.equals("2")){
                                                                if (position == 1) {
                                                                inflaterService.inflate(R.layout.simple_head_other, head);
                                                                TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 30;
                                                                    int lvl1_promos_needed = 5;
                                                                    String which_level = "3";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać "+which_level+" Poziom Partnera! Kliknij by ulepszyć.");
                                                                        upgrade_now_text.setText("Ulepsz teraz!");
                                                                        upgrade_level.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                Bundle args = new Bundle();
                                                                                BottomSheetDialogUpgradeLevel bottomSheet = new BottomSheetDialogUpgradeLevel();
                                                                                args.putString("points", String.valueOf(lvl_1_points_needed));
                                                                                args.putString("level", which_level);

                                                                                bottomSheet.setArguments(args);
                                                                                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());

                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                                else if (position == 2)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());

                                                                        double lvl_1_points_needed = 40;
                                                                        int lvl1_promos_needed = 8;
                                                                        String which_level = "4";

                                                                        double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                        int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                        String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                        if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }                                            }
                                                                        else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                        }
                                                                        else{
                                                                            if (data_getLevel.equals("2")) {
                                                                                needed_text.setText("Musisz najpierw uzyskać 3 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                            }
                                                                        }


                                                                }
                                                                else if (position == 3)
                                                                {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());
                                                                        double lvl_1_points_needed = 50;
                                                                        int lvl1_promos_needed = 12;
                                                                        String which_level = "5";

                                                                        double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                        int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                        String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                        if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }                                            }
                                                                        else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                            if (calculate_promos_needed == 1)
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                            else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                            {
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                            }
                                                                            else{
                                                                                needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                            }
                                                                        }
                                                                        else{
                                                                            if (data_getLevel.equals("2")) {
                                                                                needed_text.setText("Musisz najpierw uzyskać 4 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                            }

                                                                        }

                                                                }
                                                             }
                                                            else if (data_getLevel.equals("3")){
                                                                if (position == 1) {
                                                                inflaterService.inflate(R.layout.simple_head_other, head);
                                                                TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 40;
                                                                    int lvl1_promos_needed = 8;
                                                                    String which_level = "4";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać "+which_level+" Poziom Partnera! Kliknij by ulepszyć.");
                                                                        upgrade_now_text.setText("Ulepsz teraz!");
                                                                        upgrade_level.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {

                                                                                Bundle args = new Bundle();
                                                                                BottomSheetDialogUpgradeLevel bottomSheet = new BottomSheetDialogUpgradeLevel();
                                                                                args.putString("points", String.valueOf(lvl_1_points_needed));
                                                                                args.putString("level", which_level);

                                                                                bottomSheet.setArguments(args);
                                                                                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());


                                                                            }
                                                                        });

                                                                    }
                                                                }
                                                                else if (position == 2)
                                                                  {
                                                                inflaterService.inflate(R.layout.simple_head_other, head);
                                                                TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                      LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                      TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 50;
                                                                    int lvl1_promos_needed = 12;
                                                                    String which_level = "5";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                        if (data_getLevel.equals("3")) {
                                                                            needed_text.setText("Musisz najpierw uzyskać 4 Poziom partnera by móc ulepszyć do tego poziomu!");

                                                                        }
                                                                    }


                                                            }
                                                             }
                                                            else if (data_getLevel.equals("4")){
                                                                if (position == 1) {
                                                                    inflaterService.inflate(R.layout.simple_head_other, head);
                                                                    TextView level_text = (TextView) head.findViewById(R.id.nav_header_partnerlevel_id2);
                                                                    ImageView img_level = (ImageView) head.findViewById(R.id.imagePlus3);
                                                                    TextView needed_text = (TextView) head.findViewById(R.id.needed_text2);

                                                                    LinearLayout upgrade_level = head.findViewById(R.id.upgrade_level);
                                                                    TextView upgrade_now_text = head.findViewById(R.id.upgrade_now_text);

                                                                    img_level.setImageResource(cardData.getHeadBackgroundResource());
                                                                    level_text.setText(cardData.getHeadTitle());

                                                                    double lvl_1_points_needed = 50;
                                                                    int lvl1_promos_needed = 12;
                                                                    String which_level = "5";

                                                                    double calculate_points_needed = lvl_1_points_needed - points_double;

                                                                    int calculate_promos_needed = lvl1_promos_needed - dones_double;
                                                                    String calculated_calculate_points_needed = precision.format(calculate_points_needed);

                                                                    if ((points_double<lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak monet, brak ukończonych promocji
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocję, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet i ukończ "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }                                            }
                                                                    else if ((points_double<lvl_1_points_needed) && (dones_double>=lvl1_promos_needed)){ // brak monet, promocje są
                                                                        needed_text.setText("Zdobądź jeszcze "+calculated_calculate_points_needed+" monet, by móc ulepszyć do tego poziomu");
                                                                    }
                                                                    else if ((points_double>=lvl_1_points_needed) && (dones_double<lvl1_promos_needed)){ // brak ukończonych promocji, punkty są
                                                                        if (calculate_promos_needed == 1)
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                        else if ((calculate_promos_needed == 2) || (calculate_promos_needed==3) || (calculate_promos_needed==4))
                                                                        {
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocje, by móc ulepszyć do tego poziomu");
                                                                        }
                                                                        else{
                                                                            needed_text.setText("Ukończ jeszcze "+calculate_promos_needed+" promocji, by móc ulepszyć do tego poziomu");

                                                                        }
                                                                    }
                                                                    else{
                                                                        needed_text.setText("Możesz uzyskać "+which_level+" Poziom Partnera! Kliknij by ulepszyć.");
                                                                        upgrade_now_text.setText("Ulepsz teraz!");
                                                                        upgrade_level.setOnClickListener(new View.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(View v) {
                                                                                Bundle args = new Bundle();
                                                                                BottomSheetDialogUpgradeLevel bottomSheet = new BottomSheetDialogUpgradeLevel();
                                                                                args.putString("points", String.valueOf(lvl_1_points_needed));
                                                                                args.putString("level", which_level);

                                                                                bottomSheet.setArguments(args);
                                                                                bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());


                                                                            }
                                                                        });
                                                                    }
                                                                }
                                                                //promos needed
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




                                        // Set header data from data object
                    /*
                    TextView title = (TextView) head.findViewById(R.id.title);
                    title.setText(cardData.getHeadTitle());
                    ImageView avatar = (ImageView) head.findViewById(R.id.avatar);
                    avatar.setImageResource(cardData.getPersonPictureResource());
                    TextView name = (TextView) head.findViewById(R.id.name);
                    name.setText(cardData.getPersonName() + ":");
                    TextView message = (TextView) head.findViewById(R.id.message);
                    message.setText(cardData.getPersonMessage());
                    TextView viewsCount = (TextView) head.findViewById(R.id.socialViewsCount);
                    viewsCount.setText(" " + cardData.getPersonViewsCount());
                    TextView likesCount = (TextView) head.findViewById(R.id.socialLikesCount);
                    likesCount.setText(" " + cardData.getPersonLikesCount());
                    TextView commentsCount = (TextView) head.findViewById(R.id.socialCommentsCount);
                    commentsCount.setText(" " + cardData.getPersonCommentsCount());
*/



                                        // Add onclick listener to card header for toggle card state
                                        head.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(final View v) {
                                                ecPagerView.toggle();
                                            }
                                        });
                                    }
                                };


                                ecPagerView = (ECPagerView) view.findViewById(R.id.ec_pager_element);

                                ecPagerView.setPagerViewAdapter(adapterLevels);

                                ecPagerView.setBackgroundSwitcherView((ECBackgroundSwitcherView) view.findViewById(R.id.ec_bg_switcher_element));

                                final ItemsCountView itemsCountView = (ItemsCountView) view.findViewById(R.id.items_count_view);
                                ecPagerView.setOnCardSelectedListener(new ECPagerView.OnCardSelectedListener() {
                                    @Override
                                    public void cardSelected(int newPosition, int oldPosition, int totalElements) {
                                        itemsCountView.update(newPosition, oldPosition, totalElements);
                                    }
                                });


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




           // imageView = (CircleImageView) view.findViewById(R.id.image_avatar);
           // ImageView img_level= (ImageView) view.findViewById(R.id.imagePlus2);
            TextView bonus_txt = (TextView) view.findViewById(R.id.user_level_bonus);

            view.findViewById(R.id.buttonDeposit).setOnClickListener(mListener);
            view.findViewById(R.id.buttonWithdraw).setOnClickListener(mListener);
            view.findViewById(R.id.task_of_day).setOnClickListener(mListener);


            textViewFavouriteCount = view.findViewById(R.id.textulubione);
            textViewDoneCount = view.findViewById(R.id.textwykonane);
            textViewPoints = view.findViewById(R.id.text_points);
            textViewEarned = view.findViewById(R.id.txtzarobione);
            textViewNickname = view.findViewById(R.id.nickname);
            textViewLevel = view.findViewById(R.id.user_level);
            //getting the current user



            return view;
        }
public void runstopanim(){
    image_avatar_bg_static.setVisibility(View.VISIBLE);

}

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(LOCATION_REQUEST)
    private void checkLocationRequest() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(mContext, perms)) {
            // Already have permission, do the thing

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,"Udziel uprawnień, by móc zmienić avatar",
                    LOCATION_REQUEST, perms);
        }
    }
    private void selectImage() {
       // final CharSequence[] options = { "Zrób zdjęcie", "Wybierz z galerii","Anuluj" };
        final CharSequence[] options = {"Wybierz z galerii","Anuluj" };
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogCustom);
       // AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Dodaj avatar");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
               /* if (options[item].equals("Zrób zdjęcie"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } */
                if (options[item].equals("Wybierz z galerii"))
                {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Wybierz zdjęcie"), PICK_IMAGE);
                }
                else if (options[item].equals("Anuluj")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

        @SuppressLint("NewApi")
        public String getRealPathFromURI_API19(Context context, Uri uri) {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        }
    Bitmap bitmap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("requestCode", String.valueOf(requestCode));
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap=getResizedBitmap(bitmap, 400);
                    image_avatar_bg_static.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, System.currentTimeMillis() + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode == 2) {
                Uri selectedImageUri = data.getData();

                try {
                     bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(selectedImageUri));
                    image_avatar_bg_static.setImageBitmap(bitmap);
                   // uploadFile(selectedImageUri);
                    new Thread(new Runnable() {
                        public void run() {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    // messageText.setText("uploading started.....");
                                }
                            });


                    String fileName = getRealPathFromURI_API19(mContext,selectedImageUri);
                    String path = getRealPathFromURI_API19(mContext,selectedImageUri);
                    HttpURLConnection conn = null;
                    DataOutputStream dos = null;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBufferSize = 1024 * 1024;
                    File sourceFile = new File(path);

                    if (!sourceFile.isFile()) {

                        Log.e("uploadFile", "Source File not exist :"+path);


                    }
                    else
                    {
                        try {

                            // open a URL connection to the Servlet
                            FileInputStream fileInputStream = new FileInputStream(sourceFile);
                            URL url = new URL("https://yoururl.com/api/UploadToServer.php?username="+ user2.getUsername());

                            // Open a HTTP  connection to  the URL
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setDoInput(true); // Allow Inputs
                            conn.setDoOutput(true); // Allow Outputs
                            conn.setUseCaches(false); // Don't use a Cached Copy
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Connection", "Keep-Alive");
                            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                            conn.setRequestProperty("uploaded_file", fileName);
                            dos = new DataOutputStream(conn.getOutputStream());

                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                    + fileName + "\"" + lineEnd);

                            dos.writeBytes(lineEnd);

                            // create a buffer of  maximum size
                            bytesAvailable = fileInputStream.available();

                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];

                            // read file and write it into form...
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            while (bytesRead > 0) {

                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fileInputStream.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                            }

                            // send multipart form data necesssary after file data...
                            dos.writeBytes(lineEnd);
                            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                            // Responses from the server (code and message)
                            serverResponseCode = conn.getResponseCode();
                            String serverResponseMessage = conn.getResponseMessage();
                            URL url22 = new URL("https://yoururl.com/api/images/users/"+ user2.getUsername()+".png");
                           // bitmap = BitmapFactory.decodeStream(url22.openConnection().getInputStream());
                        //   image_avatar_bg_static.setImageBitmap(bitmap);
                            Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);
                           // PromosActivity.init();




                            //close the streams //
                            fileInputStream.close();
                            dos.flush();
                            dos.close();

                        } catch (MalformedURLException ex) {


                            ex.printStackTrace();
                              //  Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                             //   snackbar.show();
                            Toasty.error(mContext, "Wystąpił błąd podczas dodawania. Spróbuj ponownie.").show();

                            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                        } catch (Exception e) {
                            Log.e("Upload file to server", "error: " + e);
                             //   Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                               // snackbar.show();
                            Toasty.error(mContext, "Wystąpił błąd podczas dodawania. Spróbuj ponownie.").show();

                            e.printStackTrace();


                        }


                    } // End else block

                        }
                    }).start();
                } catch (FileNotFoundException e) {
               //         Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
               //         snackbar.show();
                    Toasty.error(mContext, "Wystąpił błąd podczas dodawania. Spróbuj ponownie.").show();

                    e.printStackTrace();
                }


            }
        }
    }
    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }



    public void getData3(){

        final String url2 = "https://yoururl.com/api/check_notification_count.php?user_id="+user2.getId()+"&limit=yes&notification_type=other";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            count_notifications = response.getString("count_notifications");
                            if (count_notifications.equals("0")) {
                                points_layout_info_rl.setVisibility(View.GONE);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams. MATCH_PARENT ,
                                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                                layoutParams.setMargins( 2 , 30 , 2 , 20 ) ;
                               // hide_if_zero.setVisibility(View.VISIBLE);
                                //Toast.makeText(context, "zero", Toast.LENGTH_SHORT).show();
                                //points_layout_info.addView(points_layout_info, layoutParams);
                            } else if (count_notifications.equals("1") || count_notifications.equals("2")) {

                                new AsyncMyInformations1().execute();

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

    @Override
    public void onButtonClicked(String text) {

    }


    class AsyncMyInformations1 extends AsyncTask<String, String, String> {
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
            url = new URL("https://yoururl.com/api/get_notifications.php?user_id="+user2.getId()+"&limit=yes&notification_type=other");

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
                DataNotification notificationData = new DataNotification();
                notificationData.notificationId= json_data.getInt("notification_id");
                notificationData.notificationTitle= json_data.getString("notification_title");
                notificationData.notificationDescription= json_data.getString("notification_description");
                notificationData.notificationEarn= json_data.getString("notification_earn");
                notificationData.notificationEarnType= json_data.getString("notification_earn_type");
                notificationData.notificationImage= json_data.getString("notification_img");
                notificationData.notificationDateAdd= json_data.getString("notification_date_add");
                data.add(notificationData);
            }

            // Setup and Handover data to recyclerview
          //  mRVNotificationList = (RecyclerView)findViewById(R.id.fishPriceList);
            mAdapter = new AdapterNotification(getActivity(), data);
            mRVNotificationList.setAdapter(mAdapter);
            mRVNotificationList.setLayoutManager(new GridLayoutManager(getActivity(),1));

        } catch (JSONException e) {
          //  Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }

    }

}
        private final View.OnClickListener mListener = new View.OnClickListener() {
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.buttonDeposit:
                        Intent intent = new Intent(getActivity(), PointsActivity.class);
                        intent.putExtra("openTopUp", 1); //for example
                        startActivity(intent);
                        break;
                    case R.id.buttonWithdraw:
                        //Intent intent2 = new Intent(getActivity(), PointsActivity.class);
                        //intent2.putExtra("openWithdraw", 1); //for example
                        //startActivity(intent2);
                        LottieAnimationView animationView;
                        final Dialog dialog3 = new Dialog(mContext); // Context, this, etc.
                        dialog3.setContentView(R.layout.dialog_disable_future);
                        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                        TextView click3 = (TextView) dialog3.findViewById(R.id.button_understand_disable_future);
                        click3.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                dialog3.dismiss();
                            }
                        });
                        animationView = dialog3.findViewById(R.id.anim_declined);

                        animationView.setVisibility(View.VISIBLE);

                        animationView
                                .addAnimatorUpdateListener(
                                        (animation) -> {
                                            // Do something.

                                        });
                        animationView
                                .playAnimation();
                        animationView.setVisibility(View.VISIBLE);

                        dialog3.show();
                        break;
                    case R.id.task_of_day:
                        Intent intent3 = new Intent(getActivity(), AwardsActivity.class);
                        startActivity(intent3);
                        break;
                }
            }
        };


    public void getData(){
        ArrayList<Integer> viewColors = new ArrayList<>();
        for (int i=1; i<32; i++)
        {
            viewColors.add(Color.TRANSPARENT);
        }


        ArrayList<String> animalNames = new ArrayList<>();
        for (int i=1; i<32; i++)
        {
            if (i<31)
            {
                animalNames.add("Dzień "+i);
            }
            else
            {
                animalNames.add("BONUS");
            }
        }
        relative_global_my_informations = view.findViewById(R.id.scroll_layout);
        User user = MyPreferenceManager.getInstance(mContext).getUser();
        final String url_get_day = "https://yoururl.com/api/get_actual_day.php?user_id="+user.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url_get_day, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            actual_day_return = response.getInt("actual_day");
                            points_yesterday = response.getDouble("points_yesterday");

                            recyclerView = view.findViewById(R.id.rvAnimals);
                            LinearLayoutManager horizontalLayoutManager
                                    = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
                            recyclerView.setLayoutManager(horizontalLayoutManager);

                            adapter = new DailyTaskRecyclerViewAdapter(mContext, viewColors, animalNames, actual_day_return);
                            adapter.setClickListener((DailyTaskRecyclerViewAdapter.ItemClickListener) MyInformationsTab1Fragment.this);
                            int scrollto=actual_day_return-1;
                            recyclerView.scrollToPosition(scrollto);
                            recyclerView.setAdapter(null);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            Log.e("RecyclerviewDays", "loaded");

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

    public void getData2(){
        User user = MyPreferenceManager.getInstance(mContext).getUser();
        final String url_get_day_clicked = "https://yoururl.com/api/get_actual_day_clicked.php?user_id="+user.getId();
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url_get_day_clicked, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            actual_day_clicked = response.getString("actual_day_clicked");
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


    public void onItemClick(View view, int position) {
        if((position==actual_day_return) && (actual_day_clicked.equals("no")))
        {
            view.findViewById(R.id.rl_img_day_bg).setBackgroundResource(R.drawable.circle_shape_done);
            view.findViewById(R.id.img_day).setBackgroundResource(0);
            view.findViewById(R.id.img_day).setBackgroundResource(R.drawable.ic_done_green_24dp);

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
            },1000);
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
                String textViewDialogS = "Otrzymujesz "+random+" monet za dzienne zalogowanie.";
                TextView textViewDialog = (TextView) dialog_clicked_day.findViewById(R.id.dialog_done_info2);
                textViewDialog.setText(textViewDialogS);
            }
            else {
                String textViewDialogS = "Otrzymujesz "+random+" monet za dzienne zalogowanie. To o "+points_yesterday_new_decimal+" monet więcej niż wczoraj!";
                TextView textViewDialog = (TextView) dialog_clicked_day.findViewById(R.id.dialog_done_info2);
                textViewDialog.setText(textViewDialogS);
            }

            dialog_clicked_day.show();
            sendData(random);
            getData2();

        }
        else if((position==actual_day_return) && (actual_day_clicked.equals("yes")))

        {

            if (username.substring(username.length() - 1).equals("a")){
              //  Snackbar snackbar = Snackbar.make(relative_global_my_informations, Html.fromHtml("<font color='#ffffff' ><b>" + "Odebrałaś już dzisiejszy prezent. Wróć jutro :)"+ "</b></font>"), Snackbar.LENGTH_LONG);
              //  snackbar.show();
                Toasty.normal(mContext, "Odebrałaś już dzisiejszy prezent. Wróć jutro :)").show();

            }
            else{
             //   Snackbar snackbar = Snackbar.make(relative_global_my_informations, Html.fromHtml("<font color='#ffffff' ><b>" + "Odebrałeś już dzisiejszy prezent. Wróć jutro :)"+ "</b></font>"), Snackbar.LENGTH_LONG);
               // snackbar.show();
                Toasty.normal(mContext, "Odebrałeś już dzisiejszy prezent. Wróć jutro :)").show();

            }


        }
        else{
            // Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
        }


    }


    }
class ExampleDataset {

    private List<ECCardData> dataset;

    public ExampleDataset(String user_level) {
             if (user_level.equals("0")){
            dataset = new ArrayList<>(6);

            CardData item1 = new CardData();

            item1.setMainBackgroundResource(R.drawable.level0_3);
            item1.setHeadBackgroundResource(R.drawable.level_zero);
            item1.setHeadTitle("Nowy Partner");
            item1.setPersonMessage("Cur adelphis studere?");
            item1.setPersonName("Wallace Sutton");
            item1.setPersonPictureResource(R.drawable.logo);
            item1.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item1);

            CardData item2 = new CardData();
            item2.setMainBackgroundResource(R.drawable.level1_3);
            item2.setHeadBackgroundResource(R.drawable.level_one);
            item2.setHeadTitle("Pierwszy Poziom Partnera");
            item2.setPersonName("Tina Caldwell");
            item2.setPersonMessage("Nunquam perdere clabulare.");
            item2.setListItems(prepareCommentsArray());
            item2.setPersonPictureResource(R.drawable.logo);
            dataset.add((ECCardData) item2);


            CardData item3 = new CardData();
            item3.setMainBackgroundResource(R.drawable.level4_3);
            item3.setHeadBackgroundResource(R.drawable.level_two);
            item3.setHeadTitle("Drugi Poziom Partnera");
            item3.setPersonMessage("Magnum lacteas ducunt ad orexis.");
            item3.setPersonName("Ross Rodriguez");
            item3.setPersonPictureResource(R.drawable.logo);
            item3.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item3);

            CardData item4 = new CardData();
            item4.setMainBackgroundResource(R.drawable.level3_3);
            item4.setHeadBackgroundResource(R.drawable.level_three);
            item4.setHeadTitle("Trzeci Poziom Partnera");
            item4.setPersonMessage("Solems manducare, tanquam neuter verpa.");
            item4.setPersonName("Mattew Jordan");
            item4.setPersonPictureResource(R.drawable.logo);
            item4.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item4);

            CardData item5 = new CardData();
            item5.setMainBackgroundResource(R.drawable.level4_3);
            item5.setHeadBackgroundResource(R.drawable.level_four);
            item5.setHeadTitle("Czwarty Poziom Partnera");
            item5.setPersonMessage("Usus de bassus buxum, desiderium index!");
            item5.setPersonName("Marjorie Ellis");
            item5.setPersonPictureResource(R.drawable.logo);
            item5.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item5);

            CardData item6 = new CardData();
            item6.setMainBackgroundResource(R.drawable.level5_3);
            item6.setHeadBackgroundResource(R.drawable.level_five);
            item6.setHeadTitle("Piąty Poziom Partnera");
            item6.setPersonMessage("Usus de bassus buxum, desiderium index!");
            item6.setPersonName("Marjorie Ellis");
            item6.setPersonPictureResource(R.drawable.logo);
            item6.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item6);
        }
      else if (user_level.equals("1")){
            dataset = new ArrayList<>(5);

            CardData item2 = new CardData();
            item2.setMainBackgroundResource(R.drawable.level1_3);
            item2.setHeadBackgroundResource(R.drawable.level_one);
            item2.setHeadTitle("Drugi Poziom Partnera");
            item2.setPersonMessage("Magnum lacteas ducunt ad orexis.");
            item2.setPersonName("Ross Rodriguez");
            item2.setPersonPictureResource(R.drawable.logo);
            item2.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item2);

            CardData item3 = new CardData();
            item3.setMainBackgroundResource(R.drawable.level4_3);
            item3.setHeadBackgroundResource(R.drawable.level_two);
            item3.setHeadTitle("Drugi Poziom Partnera");
            item3.setPersonMessage("Magnum lacteas ducunt ad orexis.");
            item3.setPersonName("Ross Rodriguez");
            item3.setPersonPictureResource(R.drawable.logo);
            item3.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item3);

            CardData item4 = new CardData();
            item4.setMainBackgroundResource(R.drawable.level3_3);
            item4.setHeadBackgroundResource(R.drawable.level_three);
            item4.setHeadTitle("Trzeci Poziom Partnera");
            item4.setPersonMessage("Solems manducare, tanquam neuter verpa.");
            item4.setPersonName("Mattew Jordan");
            item4.setPersonPictureResource(R.drawable.logo);
            item4.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item4);

            CardData item5 = new CardData();
            item5.setMainBackgroundResource(R.drawable.level4_3);
            item5.setHeadBackgroundResource(R.drawable.level_four);
            item5.setHeadTitle("Czwarty Poziom Partnera");
            item5.setPersonMessage("Usus de bassus buxum, desiderium index!");
            item5.setPersonName("Marjorie Ellis");
            item5.setPersonPictureResource(R.drawable.logo);
            item5.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item5);

            CardData item6 = new CardData();
            item6.setMainBackgroundResource(R.drawable.level5_3);
            item6.setHeadBackgroundResource(R.drawable.level_five);
            item6.setHeadTitle("Piąty Poziom Partnera");
            item6.setPersonMessage("Usus de bassus buxum, desiderium index!");
            item6.setPersonName("Marjorie Ellis");
            item6.setPersonPictureResource(R.drawable.logo);
            item6.setListItems(prepareCommentsArray());
            dataset.add((ECCardData) item6);
        }
 else if (user_level.equals("2")){
                                dataset = new ArrayList<>(4);

                                CardData item3 = new CardData();
                                item3.setMainBackgroundResource(R.drawable.level4_3);
                                item3.setHeadBackgroundResource(R.drawable.level_two);
                                item3.setHeadTitle("Drugi Poziom Partnera");
                                item3.setPersonMessage("Magnum lacteas ducunt ad orexis.");
                                item3.setPersonName("Ross Rodriguez");
                                item3.setPersonPictureResource(R.drawable.logo);
                                item3.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item3);

                                CardData item4 = new CardData();
                                item4.setMainBackgroundResource(R.drawable.level3_3);
                                item4.setHeadBackgroundResource(R.drawable.level_three);
                                item4.setHeadTitle("Trzeci Poziom Partnera");
                                item4.setPersonMessage("Solems manducare, tanquam neuter verpa.");
                                item4.setPersonName("Mattew Jordan");
                                item4.setPersonPictureResource(R.drawable.logo);
                                item4.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item4);

                                CardData item5 = new CardData();
                                item5.setMainBackgroundResource(R.drawable.level4_3);
                                item5.setHeadBackgroundResource(R.drawable.level_four);
                                item5.setHeadTitle("Czwarty Poziom Partnera");
                                item5.setPersonMessage("Usus de bassus buxum, desiderium index!");
                                item5.setPersonName("Marjorie Ellis");
                                item5.setPersonPictureResource(R.drawable.logo);
                                item5.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item5);

                                CardData item6 = new CardData();
                                item6.setMainBackgroundResource(R.drawable.level5_3);
                                item6.setHeadBackgroundResource(R.drawable.level_five);
                                item6.setHeadTitle("Piąty Poziom Partnera");
                                item6.setPersonMessage("Usus de bassus buxum, desiderium index!");
                                item6.setPersonName("Marjorie Ellis");
                                item6.setPersonPictureResource(R.drawable.logo);
                                item6.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item6);
                            }
   else if (user_level.equals("3")){
                                dataset = new ArrayList<>(3);

                                CardData item4 = new CardData();
                                item4.setMainBackgroundResource(R.drawable.level3_3);
                                item4.setHeadBackgroundResource(R.drawable.level_three);
                                item4.setHeadTitle("Trzeci Poziom Partnera");
                                item4.setPersonMessage("Solems manducare, tanquam neuter verpa.");
                                item4.setPersonName("Mattew Jordan");
                                item4.setPersonPictureResource(R.drawable.logo);
                                item4.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item4);

                                CardData item5 = new CardData();
                                item5.setMainBackgroundResource(R.drawable.level4_3);
                                item5.setHeadBackgroundResource(R.drawable.level_four);
                                item5.setHeadTitle("Czwarty Poziom Partnera");
                                item5.setPersonMessage("Usus de bassus buxum, desiderium index!");
                                item5.setPersonName("Marjorie Ellis");
                                item5.setPersonPictureResource(R.drawable.logo);
                                item5.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item5);

                                CardData item6 = new CardData();
                                item6.setMainBackgroundResource(R.drawable.level5_3);
                                item6.setHeadBackgroundResource(R.drawable.level_five);
                                item6.setHeadTitle("Piąty Poziom Partnera");
                                item6.setPersonMessage("Usus de bassus buxum, desiderium index!");
                                item6.setPersonName("Marjorie Ellis");
                                item6.setPersonPictureResource(R.drawable.logo);
                                item6.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item6);
                            }
    else if (user_level.equals("4")){
                                dataset = new ArrayList<>(2);

                                CardData item5 = new CardData();
                                item5.setMainBackgroundResource(R.drawable.level4_3);
                                item5.setHeadBackgroundResource(R.drawable.level_three);
                                item5.setHeadTitle("Czwarty Poziom Partnera");
                                item5.setPersonMessage("Usus de bassus buxum, desiderium index!");
                                item5.setPersonName("Marjorie Ellis");
                                item5.setPersonPictureResource(R.drawable.logo);
                                item5.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item5);

                                CardData item6 = new CardData();
                                item6.setMainBackgroundResource(R.drawable.level5_3);
                                item6.setHeadBackgroundResource(R.drawable.level_four);
                                item6.setHeadTitle("Piąty Poziom Partnera");
                                item6.setPersonMessage("Usus de bassus buxum, desiderium index!");
                                item6.setPersonName("Marjorie Ellis");
                                item6.setPersonPictureResource(R.drawable.logo);
                                item6.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item6);
                            }
    else if (user_level.equals("5")){
                                dataset = new ArrayList<>(1);

                                CardData item6 = new CardData();
                                item6.setMainBackgroundResource(R.drawable.level5_3);
                                item6.setHeadBackgroundResource(R.drawable.level_five);
                                item6.setHeadTitle("Piąty Poziom Partnera");
                                item6.setPersonMessage("Usus de bassus buxum, desiderium index!");
                                item6.setPersonName("Marjorie Ellis");
                                item6.setPersonPictureResource(R.drawable.logo);
                                item6.setListItems(prepareCommentsArray());
                                dataset.add((ECCardData) item6);
                            }


    }

    public List<ECCardData> getDataset() {
        //Collections.shuffle(dataset);
        return dataset;
    }

    private List<CommentCard> prepareCommentsArray() {
        Random random = new Random();
        List<CommentCard> comments = new ArrayList<>();
        comments.addAll(Collections.singletonList(
                new CommentCard(R.drawable.logo, "Aaron Bradley", "When the sensor experiments for deep space, all mermaids accelerate mysterious, vital moons.", "jan 12, 2014")));

        // Collections.shuffle(comments)
        return comments.subList(0, 1);
    }
}
