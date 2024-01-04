package com.profitz.app.promos.fragments;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.profitz.app.R;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.PromosActivity;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.AdminActivity;
import com.profitz.app.promos.activities.ChatsActivity;
import com.profitz.app.promos.activities.DoneActivity;
import com.profitz.app.promos.activities.FavoriteActivity;
import com.profitz.app.promos.activities.MyDataActivity;
import com.profitz.app.promos.activities.PointsActivity;
import com.profitz.app.promos.activities.SettingsActivity;
import com.profitz.app.util.CircleTransform;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.thefinestartist.finestwebview.FinestWebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MyProfileFragment extends Fragment implements BottomSheetDialogUpgradeLevel.BottomSheetListener{
    String username;
    private Context mContext;
    TextView unreadMessage, textViewNickname, textViewLevel, textViewId, textViewUsername, textViewEmail, textViewGender, textViewFavouriteCount, textViewDoneCount, textViewPoints, textViewEarned;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user;
    CircleImageView image_avatar_bg_static;
    ImageView chats;
    View view;
    RecyclerView NotificationList;
    String count_notifications;
    int serverResponseCode = 0;
    private String Document_img1="";
    private ReviewManager reviewManager;
    private Handler mHandler;
    private static final int LOCATION_REQUEST = 222;
    private final String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int PICK_IMAGE = 2;
    String uploadFileName = "";
    String upLoadServerUri = null;
    Animation animation_move_default_left_to_right3;
    Animation animation_move_default_left_to_right;

    Animation animation_move_default_left_to_right_2;
    Animation animation_move_camera_left_to_right;
    Animation animation_move_camera_left_to_right2;
    ConstraintLayout constPremium, constAdmin, constMyData, constMessages, constPoints, constLicences, constFavorites, constDones, constSettings, constTerms, constPrivacyPolicy, constSocials, constReview, constLogout;
    CircleImageView camera;
    CircleImageView circleimageView;
    RelativeLayout removePhotoRL;
    ReviewInfo reviewInfo = null;
    RelativeLayout rl_my_profile_fragment;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_my_profile, container, false);
            mHandler = new Handler();
            user = MyPreferenceManager.getInstance(mContext).getUser();
            username = user.getUsername();
            uploadFileName = user.getUsername()+".png";

          //  chats = view.findViewById(R.id.chats);
       /*     getData3();

            NotificationList = view.findViewById(R.id.NotificationList);
            mRVNotificationList = view.findViewById(R.id.NotificationList);


*/
            reviewManager = ReviewManagerFactory.create(getActivity());
            rl_my_profile_fragment = view.findViewById(R.id.rl_my_profile_fragment);
            circleimageView =  view.findViewById(R.id.image_avatar);
            camera = view.findViewById(R.id.image_camera);
            image_avatar_bg_static = view.findViewById(R.id.image_avatar_bg_static);
            unreadMessage = view.findViewById(R.id.unreadMessage);
            textViewNickname = view.findViewById(R.id.username);
            //    textViewLevel = view.findViewById(R.id.user_level);
            constPremium = view.findViewById(R.id.rl_global_my_profile_premium);
            constAdmin = view.findViewById(R.id.rl_global_my_profile_0);
            constMyData = view.findViewById(R.id.rl_global_my_profile_1);
            constMessages = view.findViewById(R.id.rl_global_my_profile_2);
            constPoints = view.findViewById(R.id.rl_global_my_profile_3);
            constLicences = view.findViewById(R.id.rl_global_my_profile_4);
            constFavorites = view.findViewById(R.id.rl_global_my_profile_5);
            constDones = view.findViewById(R.id.rl_global_my_profile_6);
            constSettings = view.findViewById(R.id.rl_global_my_profile_7);
            constTerms = view.findViewById(R.id.rl_global_my_profile_8);
            constPrivacyPolicy = view.findViewById(R.id.rl_global_my_profile_9);
            constSocials = view.findViewById(R.id.rl_global_my_profile_10);
            constReview = view.findViewById(R.id.rl_global_my_profile_11);
            constLogout = view.findViewById(R.id.const_logout);
            removePhotoRL = view.findViewById(R.id.removePhotoRL);
            final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();
            final String url_unread_messages = "https://yoururl.com/api/get_unread_messages.php?user_id="+user.getId();
            JsonObjectRequest jsonObjectRequest_unread_messages = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url_unread_messages, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                String response_unread_messages = response.getString("unread_messages");

                                if (response_unread_messages.equals("0")) {
                                    unreadMessage.setText("0");
                                    unreadMessage.setVisibility(View.GONE);
                                }
                                else{
                                    unreadMessage.setVisibility(View.VISIBLE);
                                    unreadMessage.setText(response_unread_messages);
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
            Volley.newRequestQueue(mContext).add(jsonObjectRequest_unread_messages);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                String username = response.getString("username");
                                String premium = response.getString("premium");
                                String user_level = response.getString("user_level");
                                String image_url = response.getString("image_url");
                                String active_yearly_discount = response.getString("active_yearly_discount");
                                textViewNickname.setText(username);

                                if(premium.equals("1")){
                                    constPremium.setVisibility(View.GONE);
                                }
                                else{
                                    constPremium.setVisibility(View.VISIBLE);
                                }
                                ImageView user_level_image = view.findViewById(R.id.userLevelImage);

                                if (user_level.equals("0")){
                                    user_level_image.setImageResource(R.drawable.level_zero);

                                }
                                else if (user_level.equals("1")){
                                    user_level_image.setImageResource(R.drawable.level_one);

                                }
                                else if (user_level.equals("2")){
                                    user_level_image.setImageResource(R.drawable.level_two);

                                }
                                else if (user_level.equals("3")){
                                    user_level_image.setImageResource(R.drawable.level_three);

                                }
                                else if (user_level.equals("4")){
                                    user_level_image.setImageResource(R.drawable.level_four);

                                }
                                else if (user_level.equals("5")){
                                    user_level_image.setImageResource(R.drawable.level_five);
                                }




                                if (image_url.contains("default"))
                                {
                                    removePhotoRL.setVisibility(View.GONE);
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
                                            circleimageView.setVisibility(View.VISIBLE);

                                            circleimageView.startAnimation(animation_move_default_left_to_right);
                                        }
                                    }, 3500);   //5 seconds

                                    animation_move_default_left_to_right.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            camera.startAnimation(animation_move_camera_left_to_right);
                                            circleimageView.startAnimation(animation_move_default_left_to_right_2);
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    animation_move_default_left_to_right_2.setFillAfter(true);
                                                    animation_move_default_left_to_right_2.cancel();
                                                    camera.clearAnimation();
                                                    circleimageView.clearAnimation();
                                                    circleimageView.clearAnimation();
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
                                                        circleimageView.startAnimation(animation_move_default_left_to_right);
                                                        animation_move_default_left_to_right.setAnimationListener(new Animation.AnimationListener() {
                                                            @Override
                                                            public void onAnimationStart(Animation animation) {

                                                            }

                                                            @Override
                                                            public void onAnimationEnd(Animation animation) {
                                                                camera.startAnimation(animation_move_camera_left_to_right);
                                                                circleimageView.startAnimation(animation_move_default_left_to_right_2);

                                                                handler.postDelayed(new Runnable() {
                                                                    public void run() {
                                                                        animation_move_default_left_to_right_2.setFillAfter(true);
                                                                        animation_move_default_left_to_right_2.cancel();
                                                                        camera.clearAnimation();
                                                                        circleimageView.clearAnimation();
                                                                        circleimageView.clearAnimation();
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
                                                    circleimageView.clearAnimation();
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
                                else{
                                    circleimageView.setVisibility(View.GONE);
                                    camera.setVisibility(View.GONE);
                                    removePhotoRL.setVisibility(View.VISIBLE);
                                    Picasso.with(mContext).load(image_url)
                                            .transform(new CircleTransform())
                                            .placeholder(R.drawable.default1)
                                            .error(R.drawable.default1)
                                            .skipMemoryCache()
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                            .into(image_avatar_bg_static);
                                    image_avatar_bg_static.setVisibility(View.VISIBLE);

                                }

                                //messageText.setText("Uploading file path :- '/mnt/sdcard/"+uploadFileName+"'");


                                upLoadServerUri = "https://www.profitz.app/api/api/UploadToServer.php";

                                circleimageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (EasyPermissions.hasPermissions(getActivity(), galleryPermissions)) {
                                            selectImage();
                                        } else {
                                            EasyPermissions.requestPermissions(getActivity(), "Dostęp do galerii", 101, galleryPermissions);
                                        }

                                    }
                                });


                                user_level_image.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {



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
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);
            hideItemAdmin();


            constPremium.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ((PromosActivity)getActivity()).showDiscountPromoDialog((PromosActivity)getActivity(), "40", "2024-01-5");


                  /*  Bundle args22 = new Bundle();
                    BottomSheetDialogDisabledFuture bottomSheetDialogDisabledFuture = new BottomSheetDialogDisabledFuture();
                    args22.putString("source","promosActivity");
                    bottomSheetDialogDisabledFuture.setArguments(args22);
                    bottomSheetDialogDisabledFuture.show(getChildFragmentManager(), bottomSheetDialogDisabledFuture.getTag());

                   */
                }
            });

            constAdmin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AdminActivity.class);
                    startActivity(intent);
                }
            });

            constMyData.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MyDataActivity.class);
                    startActivity(intent);
                }
            });
            constMessages.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChatsActivity.class);
                    startActivity(intent);
                }
            });
            constPoints.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PointsActivity.class);
                    startActivity(intent);
                }
            });

            constLicences.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
               //     Intent intent = new Intent(getActivity(), LicencesActivity.class);
                //    startActivity(intent);
                    Bundle args = new Bundle();

                    BottomSheetDialogDisabledFuture bottomSheetDialogDisabledFuture = new BottomSheetDialogDisabledFuture();
                    args.putString("source","promosActivity");
                    bottomSheetDialogDisabledFuture.setArguments(args);
                    bottomSheetDialogDisabledFuture.show(getChildFragmentManager(), bottomSheetDialogDisabledFuture.getTag());
                }
            });


            constFavorites.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                    startActivity(intent);
                }
            });
            constDones.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DoneActivity.class);
                    startActivity(intent);
                }
            });
            constSettings.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                }
            });

            constTerms.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String terms_url = "https://profitz.app/terms_and_conditions.html";
                    new FinestWebView.Builder(getActivity()).iconDefaultColor(getResources().getColor(android.R.color.white)).show(terms_url);

                }
            });
            constPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String privacy_url = "https://profitz.app/privacy_policy.html";
                    new FinestWebView.Builder(getActivity()).iconDefaultColor(getResources().getColor(android.R.color.white)).show(privacy_url);
                }
            });
            constSocials.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });
            constReview.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                         new AppRatingDialog.Builder()
                                    .setPositiveButtonText(R.string.rate_now)
                                    .setNeutralButtonText(R.string.rate_later)
                                    .setNoteDescriptions(Arrays.asList("Słabo", "Ujdzie", "Okej", "Dobrze", "Wyśmienicie !!!"))
                                    .setDefaultRating(5)
                                    .setTitle(R.string.rate_title)
                                    .setDescription(R.string.rate_message)
                                    .setCommentInputEnabled(true)
                                    .setDefaultComment("Aplikacja jest super!")
                                    .setStarColor(R.color.colorPrimaryMid)
                                    .setNoteDescriptionTextColor(R.color.colorListDivider)
                                    .setTitleTextColor(R.color.bg_Profitz)
                                    .setDescriptionTextColor(R.color.darkGray)
                                    .setHint("Jeżeli chcesz, możesz dodać kilka słów od siebie, wpisując je tutaj...")
                                    .setHintTextColor(R.color.colorListDivider)
                                    .setCommentTextColor(R.color.darkGray)
                                    .setCommentBackgroundColor(R.color.gray)
                                    .setWindowAnimation(R.style.MyDialogFadeAnimation)
                                    .setCancelable(false)
                                    .setCanceledOnTouchOutside(false)
                                    .create(Objects.requireNonNull(getActivity()))
                                    .show();
                            //AppRate.with(PromosActivity.this)
                                  //  .clearAgreeShowDialog();
                          //  AppRate.with(PromosActivity.this)
                                  //  .showRateDialog(PromosActivity.this);

                }
            });
            constLogout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MyPreferenceManager.getInstance(mContext).logout();
                }
            });
            return view;
        }

    private void hideItemAdmin()
    {
        if (!(user.getId().equals("1")))
        {
            constAdmin.setVisibility(View.GONE);
        }
    }
    public void showRateApp() {

        com.google.android.play.core.tasks.Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                com.google.android.play.core.tasks.Task<Void> flow = reviewManager.launchReviewFlow(getActivity(), reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, continue regardless of the result.
                // show native rate app dialog on error
           //     Snackbar snackbar = Snackbar.make(rl_my_profile_fragment, Html.fromHtml("<font color=\"#ffffff\">Wystąpił problem. Spróbuj ponownie później, lub sprawdź połączenie z internetem.</font>"), Snackbar.LENGTH_LONG);
            //    snackbar.show();
                Toasty.error(mContext, "Wystąpił problem. Spróbuj ponownie później, lub sprawdź połączenie z internetem.").show();

            }
        });
    }
public void runstopanim(){
  //  image_avatar_bg_static.setVisibility(View.VISIBLE);

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
            EasyPermissions.requestPermissions(this,"Udziel uprawnień, by móc zmienić avatar.",
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
                    image_avatar_bg_static.setVisibility(View.VISIBLE);
                    BitMapToString(bitmap);
                    String path = Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, System.currentTimeMillis() + ".png");
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
                    image_avatar_bg_static.setVisibility(View.VISIBLE);

                    // uploadFile(selectedImageUri);
                    new Thread(new Runnable() {
                        public void run() {
                            Looper.prepare();//Call looper.prepare()

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
                            URL url = new URL("https://yoururl.com/api/UploadToServer.php?username="+ user.getUsername());

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
                            URL url22 = new URL("https://yoururl.com/api/images/users/"+ user.getUsername()+".png");
                            bitmap = BitmapFactory.decodeStream(url22.openConnection().getInputStream());
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                            image_avatar_bg_static.setImageBitmap(bitmap);
                                    image_avatar_bg_static.setVisibility(View.VISIBLE);

                                    Toasty.success(mContext, "Avatar został zaktualizowany. Zmiany w niektórych miejsach mogą pojawić się do kilku godzin.", 300).show();

                                }
                            });

                            Log.i("uploadFile", "HTTP Response is : "
                            + serverResponseMessage + ": " + serverResponseCode);
                         //   Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Avatar został zaktualizowany. Zmiany w niektórych miejsach mogą pojawić się do kilku godzin.", Snackbar.LENGTH_LONG);
                          //  snackbar.show();



                            //close the streams //
                            fileInputStream.close();
                            dos.flush();
                            dos.close();

                        } catch (MalformedURLException ex) {


                            ex.printStackTrace();
                             //   Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                           //     snackbar.show();

                                            Toasty.error(mContext, "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", 300).show();

                            Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                        } catch (Exception e) {
                            Log.e("Upload file to server", "error: " + e);
                            //    Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                          //      snackbar.show();

                                    Toasty.error(mContext, "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", 300).show();



                            e.printStackTrace();


                        }


                    } // End else block

                        }
                    }).start();
                } catch (FileNotFoundException e) {
                //        Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił błąd podczas dodawania. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
             //           snackbar.show();
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

    @Override
    public void onButtonClicked(String text) {

    }





    }
