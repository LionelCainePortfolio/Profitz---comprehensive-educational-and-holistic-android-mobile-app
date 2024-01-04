package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promodetail.PromoDetailActivity;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.AirdropDetailActivity;
import com.profitz.app.promos.adapters.AdapterReview;
import com.profitz.app.promos.adapters.TabAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetDialogAddComment extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    private View bottomSheet;
    private AdapterReview mAdapter;
    int promoIdd;
    String promoName;
    String promoId;
    String airdropId;
    String isAirdrop;
    String userId;
    String commentId;
    String replyId;
    String username;
    String global_comm;
    String is_reply;
    String reply_username;
    String reply_username_true;
    View v;
    private TabAdapter adapterTab;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String comment_action;
    String comment_content;
    String url_comment;
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

        v = inflater.inflate(R.layout.dialog_add_comment, container, false);

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
        replyId = mArgs.getString("replyId");
        userId = mArgs.getString("userId");
        promoId = mArgs.getString("promoId");
        airdropId = mArgs.getString("airdropId");
        isAirdrop = mArgs.getString("isAirdrop");
        username = mArgs.getString("username");
        global_comm = mArgs.getString("global_comm");
        is_reply = mArgs.getString("is_reply");
        reply_username = mArgs.getString("reply_username");
        reply_username_true = mArgs.getString("reply_username_true");
        comment_action = mArgs.getString("comment_action");
        comment_content = mArgs.getString("comment_content");
        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (265*scale + 0.5f);
        int dpAsPixels2 = (int) (15*scale + 0.5f);

NestedScrollView global_comm_nest = v.findViewById(R.id.global_comm_nest);
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {

if (isOpen){

    global_comm_nest.setPadding(0,0,0,dpAsPixels);
}
else{
    global_comm_nest.setPadding(0,0,0,dpAsPixels2);
}


                    }
                });
        if (!isAirdrop.equals("1")) {
            ((PromoDetailActivity)getActivity()).stopSlider();

        }

        CircleImageView user_img = v.findViewById(R.id.post_detail_currentuser_img);

        String imgUrl = "https://yoururl.com/api/images/users/"+user2.getUsername()+".png";
        Picasso.with( mContext )
                .load( imgUrl )
                .placeholder( mContext.getDrawable(  R.drawable.default_avatar ) )
                .error( mContext.getDrawable( R.drawable.default_avatar ) )
                .into( user_img );






        ConstraintLayout global_comm_const = v.findViewById(R.id.global_comm_const);
        EditText post_detail_comment = v.findViewById(R.id.post_detail_comment);

        if (comment_action.equals("edit")){
            post_detail_comment.setText(comment_content);

        }



        post_detail_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    global_comm_nest.scrollBy(0, 100);

            }});


        ImageView sendComment = v.findViewById(R.id.sendComment);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = post_detail_comment.getText().toString();

                if (comment_action.equals("edit")){
                    if (isAirdrop.equals("1")){
                        url_comment = "https://yoururl.com/api/edit_comment.php?comment_action=edit&is_airdrop=1&comment_id="+commentId+"&airdrop_id="+airdropId+"&username="+username+"&content="+comment;

                    }
                    else{
                        url_comment = "https://yoururl.com/api/edit_comment.php?comment_action=edit&is_airdrop=0&comment_id="+commentId+"&promo_id="+promoId+"&username="+username+"&content="+comment;
                    }

                }
                else{
                    if (isAirdrop.equals("1")){
                        url_comment = "https://yoururl.com/api/add_comment.php?comment_action=add&is_airdrop=1&comment_id="+commentId+"&airdrop_id="+airdropId+"&username="+username+"&content="+comment+"&is_reply="+is_reply+"&global_comm="+global_comm+"&user_id="+userId+"&reply_username="+reply_username+"&reply_username_true="+reply_username_true+"&reply_id="+replyId;

                    }
                    else{
                        url_comment = "https://yoururl.com/api/add_comment.php?comment_action=add&is_airdrop=0&comment_id="+commentId+"&promo_id="+promoId+"&username="+username+"&content="+comment+"&is_reply="+is_reply+"&global_comm="+global_comm+"&user_id="+userId+"&reply_username="+reply_username+"&reply_username_true="+reply_username_true+"&reply_id="+replyId;
                    }
                }



                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_comment, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String responseString = response.getString("response");

                                    if (responseString.equals("1")) {
                                        if (isAirdrop.equals("1")){
                                            ((AirdropDetailActivity)getActivity()).commentAdded(Integer.parseInt(airdropId));
                                        }
                                        else{
                                            ((PromoDetailActivity)getActivity()).commentAdded(Integer.parseInt(promoId));
                                        }
                                        dismiss();
                                    }
                                    else if (responseString.equals("2")) {
                                        if (isAirdrop.equals("1")){
                                            ((AirdropDetailActivity)getActivity()).commentEdited(Integer.parseInt(airdropId));
                                        }
                                        else{
                                            ((PromoDetailActivity)getActivity()).commentEdited(Integer.parseInt(promoId));
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
                Volley.newRequestQueue(mContext).add(jsonObjectRequest);



            }
        });


        Bundle bundle =new Bundle();
        bundle.putString("promoName",promoName);

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
