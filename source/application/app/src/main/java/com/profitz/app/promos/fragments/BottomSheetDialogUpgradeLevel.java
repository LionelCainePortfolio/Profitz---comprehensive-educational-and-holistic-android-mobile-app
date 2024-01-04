package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.activities.MyInformationsActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import es.dmoral.toasty.Toasty;

public class BottomSheetDialogUpgradeLevel extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    String points;
    String level;
    String promoBonus;
    User user2;
    View v;
String secString;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        Bundle mArgs = getArguments();
        points = mArgs.getString("points");
        level=mArgs.getString("level");
        v = inflater.inflate(R.layout.dialog_confirm_upgrade_level, container, false);

        TextView dialog_confirm_upgrade_which_leveL_text = v.findViewById(R.id.dialog_confirm_upgrade_which_leveL_text);
        dialog_confirm_upgrade_which_leveL_text.setText("Ulepsz do poziomu "+level);

        if (points.contains(".0")){
          points = removeLastChar(points);
        }

        TextView buttonConfirmUpgrade = v.findViewById(R.id.buttonConfirmUpgrade);
        buttonConfirmUpgrade.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("DefaultLocale")
                @TargetApi(Build.VERSION_CODES.CUPCAKE)
                @Override
                public void onClick(View view) {



                    final String url2 = "https://yoururl.com/api/upgrade_level.php?points="+points+"user_id="+user2.getId()+"&level="+level+"&safety_code=A4S3D234S3Aar3K4aC3a3sm3fk2aDNA44SDFA1a35sd5aKASD4033323413KA56DLARO2r55JDAOS24DLCNZ";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.getString("status");
                                        if (status.equals("1")) {
                                            ((MyInformationsActivity)getActivity()).refreshActivity();

                                        } else if (status.equals("0")) {
                                            //Snackbar snackbar = Snackbar.make(getActivity().getWindow().getDecorView().findViewById(android.R.id.content), "Wystąpił nieznany błąd. Spróbuj ponownie.", Snackbar.LENGTH_LONG);
                                          //  snackbar.show();
                                            Toasty.error(mContext, "Wystąpił nieznany błąd. Spróbuj ponownie.").show();

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

                    dismiss();

                }
            });


        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        if ((points.equals("2")) ||(points.equals("3")) || (points.equals("4")) ){

            secString = "punkty";
        }
        else if  (points.equals("1")){
            secString = "punkt";
        }
        else{
            secString = "punktów";
        }
        String sourceString = "Ulepszenie do tego poziomu kosztować Cię będzie <b>"+points+" "+secString+"</b>. Punkty zostaną bezpowrotnie wymienione i niemożliwe będzie ich odzyskanie. Klikając na przycisk <b>'Ulepszam'</b> zgadzasz się z powyższym i zatwierdzasz ulepszenie konta.";

        TextView dialog_confirm_upgrade_points_text = (TextView) v.findViewById(R.id.dialog_confirm_upgrade_points_text);
        dialog_confirm_upgrade_points_text.setText(Html.fromHtml(sourceString));



        return v;
    }
    public static String removeLastChar(String str) {
        return removeLastChars(str, 2);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
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
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Window window = getDialog().getWindow();
            window.findViewById(com.google.android.material.R.id.container).setFitsSystemWindows(false);
            // dark navigation bar icons
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
    }
}
