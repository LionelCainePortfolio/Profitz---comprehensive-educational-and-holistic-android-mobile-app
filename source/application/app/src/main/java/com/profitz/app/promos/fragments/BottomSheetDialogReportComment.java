package com.profitz.app.promos.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promodetail.PromoDetailActivity;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.AirdropDetailActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public class BottomSheetDialogReportComment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    String promoId;
    String userId;
    String commentId;
    String username;
    String airdropId;
    String isAirdrop;
    View v;

    String url_send_report;
    JsonObjectRequest jsonObjectRequestSendReport;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(dialog);
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return dialog;
    }
    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        Window window = dialog.getWindow();
        // Set the Dialog Background Transparent
// window.findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());

        v = inflater.inflate(R.layout.dialog_report, container, false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);


        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getActivity().getWindow().setNavigationBarColor(Color.rgb(255,255,255));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        Bundle mArgs = getArguments();
        commentId = mArgs.getString("commentId");
        userId = mArgs.getString("userId");
        promoId = mArgs.getString("promoId");
        username = mArgs.getString("username");
        airdropId = mArgs.getString("airdropId");
        isAirdrop = mArgs.getString("isAirdrop");
        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();

        if (!isAirdrop.equals("1")) {
            ((PromoDetailActivity)getActivity()).stopSlider();
        }
        RelativeLayout sendReportSpam = v.findViewById(R.id.sendReportSpam);
        RelativeLayout sendReportAggressive = v.findViewById(R.id.sendReportAggressive);
        RelativeLayout sendReportSexualContent = v.findViewById(R.id.sendReportSexualContent);
        RelativeLayout sendReportWrongInfo = v.findViewById(R.id.sendReportWrongInfo);
        RelativeLayout sendReportTerroism = v.findViewById(R.id.sendReportTerroism);
        RelativeLayout sendReportOther = v.findViewById(R.id.sendReportOther);

        sendReportSpam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAirdrop.equals("1")){
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&airdrop_id="+airdropId+"&username="+username+"&reason=spam";

                }
                else{
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&promo_id="+promoId+"&username="+username+"&reason=spam";
                }

                jsonObjectRequestSendReport = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_send_report, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String responseString = response.getString("response");

                                    if (responseString.equals("1")) {
                                        if (isAirdrop.equals("1")) {
                                            ((AirdropDetailActivity)getActivity()).reportSent();

                                        }
                                        else{
                                            ((PromoDetailActivity)getActivity()).reportSent();
                                        }
                                        dismiss();
                                    }

                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        /*    textViewFavouriteCount.setText(data_getFavoriteCount);
                            textViewPoints.setText(data_getPoints);
                            textViewDoneCount.setText(data_getDoneCount);
                            textViewEarned.setText(data_getEarned);

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

                Volley.newRequestQueue(mContext).add(jsonObjectRequestSendReport);
            }
        });
        sendReportAggressive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAirdrop.equals("1")){
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&airdrop_id="+airdropId+"&username="+username+"&reason=aggressive";

                }
                else{
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&promo_id="+promoId+"&username="+username+"&reason=aggressive";
                }
                jsonObjectRequestSendReport = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_send_report, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String responseString = response.getString("response");

                                    if (responseString.equals("1")) {
                                        if (isAirdrop.equals("1")) {
                                            ((AirdropDetailActivity)getActivity()).reportSent();

                                        }
                                        else{
                                            ((PromoDetailActivity)getActivity()).reportSent();
                                        }                                        dismiss();
                                    }

                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        /*    textViewFavouriteCount.setText(data_getFavoriteCount);
                            textViewPoints.setText(data_getPoints);
                            textViewDoneCount.setText(data_getDoneCount);
                            textViewEarned.setText(data_getEarned);

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

                Volley.newRequestQueue(mContext).add(jsonObjectRequestSendReport);
            }
        });
        sendReportSexualContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAirdrop.equals("1")){
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&airdrop_id="+airdropId+"&username="+username+"&reason=sexualcontent";

                }
                else{
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&promo_id="+promoId+"&username="+username+"&reason=sexualcontent";
                }
                jsonObjectRequestSendReport = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_send_report, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String responseString = response.getString("response");

                                    if (responseString.equals("1")) {
                                        if (isAirdrop.equals("1")) {
                                            ((AirdropDetailActivity)getActivity()).reportSent();

                                        }
                                        else{
                                            ((PromoDetailActivity)getActivity()).reportSent();
                                        }
                                        dismiss();
                                    }

                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        /*    textViewFavouriteCount.setText(data_getFavoriteCount);
                            textViewPoints.setText(data_getPoints);
                            textViewDoneCount.setText(data_getDoneCount);
                            textViewEarned.setText(data_getEarned);

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

                Volley.newRequestQueue(mContext).add(jsonObjectRequestSendReport);
            }
        });
        sendReportWrongInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAirdrop.equals("1")){
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&airdrop_id="+airdropId+"&username="+username+"&reason=wronginfo";

                }
                else{
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&promo_id="+promoId+"&username="+username+"&reason=wronginfo";
                }
                jsonObjectRequestSendReport = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_send_report, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String responseString = response.getString("response");

                                    if (responseString.equals("1")) {
                                        if (isAirdrop.equals("1")) {
                                            ((AirdropDetailActivity)getActivity()).reportSent();

                                        }
                                        else{
                                            ((PromoDetailActivity)getActivity()).reportSent();
                                        }
                                        dismiss();
                                    }

                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        /*    textViewFavouriteCount.setText(data_getFavoriteCount);
                            textViewPoints.setText(data_getPoints);
                            textViewDoneCount.setText(data_getDoneCount);
                            textViewEarned.setText(data_getEarned);

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

                Volley.newRequestQueue(mContext).add(jsonObjectRequestSendReport);
            }
        });


        sendReportTerroism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAirdrop.equals("1")){
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&airdrop_id="+airdropId+"&username="+username+"&reason=terrorism";

                }
                else{
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&promo_id="+promoId+"&username="+username+"&reason=terrorism";
                }

                jsonObjectRequestSendReport = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_send_report, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String responseString = response.getString("response");

                                    if (responseString.equals("1")) {
                                        if (isAirdrop.equals("1")) {
                                            ((AirdropDetailActivity)getActivity()).reportSent();

                                        }
                                        else{
                                            ((PromoDetailActivity)getActivity()).reportSent();
                                        }
                                        dismiss();
                                    }

                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        /*    textViewFavouriteCount.setText(data_getFavoriteCount);
                            textViewPoints.setText(data_getPoints);
                            textViewDoneCount.setText(data_getDoneCount);
                            textViewEarned.setText(data_getEarned);

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

                Volley.newRequestQueue(mContext).add(jsonObjectRequestSendReport);
            }
        });
        sendReportOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAirdrop.equals("1")){
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&airdrop_id="+airdropId+"&username="+username+"&reason=other";

                }
                else{
                    url_send_report = "https://yoururl.com/api/send_report_comment.php?is_airdrop=1&user_id="+userId+"&comment_id="+commentId+"&promo_id="+promoId+"&username="+username+"&reason=other";
                }

                jsonObjectRequestSendReport = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_send_report, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String responseString = response.getString("response");

                                    if (responseString.equals("1")) {
                                        if (isAirdrop.equals("1")) {
                                            ((AirdropDetailActivity)getActivity()).reportSent();

                                        }
                                        else{
                                            ((PromoDetailActivity)getActivity()).reportSent();
                                        }
                                        dismiss();
                                    }

                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                        /*    textViewFavouriteCount.setText(data_getFavoriteCount);
                            textViewPoints.setText(data_getPoints);
                            textViewDoneCount.setText(data_getDoneCount);
                            textViewEarned.setText(data_getEarned);

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

                Volley.newRequestQueue(mContext).add(jsonObjectRequestSendReport);
            }
        });





        return v;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!isAirdrop.equals("1")) {
            ((PromoDetailActivity)getActivity()).runSlider();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
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

}
