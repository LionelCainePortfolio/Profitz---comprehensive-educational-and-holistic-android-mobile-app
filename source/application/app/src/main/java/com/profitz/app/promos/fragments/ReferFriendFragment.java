package com.profitz.app.promos.fragments;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.ReferFriendHowItWorksActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import es.dmoral.toasty.Toasty;

public class ReferFriendFragment extends Fragment {
    String data_getCode;
    TextView textEarn5;
    MenuItem refferalItem;
    ImageView arrow_back;
    ImageView people_button;
    ImageView copy_code;
    TextView how_it_works;
    String data_getRefferal_count;
    String data_getRefferal_success_count;
    String data_getRefferal_earn;
    String data_user_level;
    TextView refferal_count;
    TextView refferal_success_count;
    TextView refferal_earn;
    User user2;
    String username;
    Button button;
    private Context mContext;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public ReferFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_referfriend, container, false);
            textEarn5 = view.findViewById(R.id.textEarn5);  refferal_count = view.findViewById(R.id.pending_refferals);
            refferal_success_count = view.findViewById(R.id.success_refferals);
            refferal_earn = view.findViewById(R.id.tx2);
            user2 = MyPreferenceManager.getInstance(mContext).getUser();
            int IDuser = Integer.parseInt(user2.getId());
            username = user2.getName();
            mSwipeRefreshLayout = view.findViewById(R.id.swipe_container_refferals);

            textEarn5 = view.findViewById(R.id.textEarn5);
        /*    TextView learn_more = view.findViewById(R.id.textViewTerms2);
            learn_more.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final String url_to_rules = "https://profitz.app/invite_friends";
                    Intent ir = new Intent(Intent.ACTION_VIEW);
                    ir.setData(Uri.parse(url_to_rules));
                    startActivity(ir);
                }
            });*/
            getData();


            button = view.findViewById(R.id.buttonShareCode);



            copy_code = view.findViewById(R.id.copy_code);
            how_it_works = view.findViewById(R.id.buttonHowItWorks);



            how_it_works.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(mContext, ReferFriendHowItWorksActivity.class));

                }
            });
            copy_code.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String refferal_code = textEarn5.getText().toString();

                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Skopiowany kod", refferal_code);
                    clipboard.setPrimaryClip(clip);
                   // Toast.makeText(mContext, Html.fromHtml("<font color='#1E1E1E' ><b> Skopiowano. </b></font>"), Toast.LENGTH_SHORT).show();
                    Toasty.success(mContext, "Skopiowano.").show();

                }
            });



            return view;

        }
        public void getData(){

        User user = MyPreferenceManager.getInstance(mContext).getUser();
        final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            data_user_level = response.getString("user_level");
                            data_getCode = response.getString("user_code");
                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                            textEarn5.setText(data_getCode);

                            if ((data_user_level.equals("0")) || (data_user_level.equals("1"))) {
                                float radius = textEarn5.getTextSize() / 3;
                                BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                                textEarn5.getPaint().setMaskFilter(filter);
                            }

                            copy_code.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    if (data_user_level.equals("0")) {
                                        Dialog dialog3;
                                        dialog3 = new Dialog(mContext); // Context, this, etc.
                                        dialog3.setContentView(R.layout.dialog_needed_first_level);
                                        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        Button click3 = dialog3.findViewById(R.id.button_understand);
                                        click3.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                dialog3.dismiss();
                                            }
                                        });

                                        dialog3.show();

                                    }
                                    else{
                                    String refferal_code = textEarn5.getText().toString();

                                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("Skopiowany kod", refferal_code);
                                    clipboard.setPrimaryClip(clip);
                                  //  Toast.makeText(mContext, Html.fromHtml("<font color='#1E1E1E' ><b> Skopiowano. </b></font>"), Toast.LENGTH_SHORT).show();
                                        Toasty.success(mContext, "Skopiowano.").show();

                                    }
                                }
                            });

                            button.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    if (data_user_level.equals("0")) {
                                        Dialog dialog3;
                                        dialog3 = new Dialog(mContext); // Context, this, etc.
                                        dialog3.setContentView(R.layout.dialog_needed_first_level);
                                        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        Button click3 = dialog3.findViewById(R.id.button_understand);
                                        click3.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                dialog3.dismiss();
                                            }
                                        });

                                        dialog3.show();

                                    }
                                    else{
                                        shareCode();
                                    }
                                }
                            });


                            DecimalFormat df = new DecimalFormat("##.00");
                            data_getRefferal_count = response.getString("refferal_count");
                            data_getRefferal_success_count= response.getString("refferal_success_count");
                            data_getRefferal_earn = response.getString("refferal_earn");
                            if (data_getRefferal_earn.equals("0"))
                            {
                                refferal_earn.setText("0.00");

                            }
                            else {
                                double data_getRefferal_earn_double = Double.parseDouble(data_getRefferal_earn);
                                //  username = response.getString("username");
                                // String user_id_string = response.getString("user_id");
                                //user_id = Double.parseDouble(user_id_string);
                                double new_count = (data_getRefferal_earn_double/10)*4.29;
                                String decimal_data_getRefferal_earn_string = df.format(new_count);
                                refferal_earn.setText(decimal_data_getRefferal_earn_string);

                            }

                            refferal_count.setText(data_getRefferal_count);
                            refferal_success_count.setText(data_getRefferal_success_count);

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

    public void shareCode() {
        String mimeType = "text/plain";
        String title = getString(R.string.share_dialog_title);
        String space = " ";

        String message = "Hej, pobierz Profitz, korzystając tego linku i wprowadź ten kod przy rejestracji by otrzymać 10 punktów (planowane 1,00€) po wykonaniu pierwszej promocji: " + data_getCode + " https://play.google.com/store/apps/details?id=com.profitz.app.&hl=pl" +
                space;

        //if (!video.isEmpty()) {
        //   message.append(getString(R.string.youtube_url));
        //  message.append(video);
        //   }
        ShareCompat.IntentBuilder
                .from(getActivity())
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(message)
                .startChooser();
    }


}