package com.profitz.app.promos.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragmentTransparent;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import es.dmoral.toasty.Toasty;

public class BottomSheetDialogTerms extends SuperBottomSheetFragmentTransparent {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    View v;
    String terms;
    String url;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();

        v = inflater.inflate(R.layout.dialog_terms, container, false);
        Bundle mArgs = getArguments();
        terms = mArgs.getString("terms");

        WebView mWebview = (WebView) v.findViewById(R.id.webview);



        mWebview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
                Toasty.error(mContext, description).show();

            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });
        if (terms.equals("terms_and_conditions")){
            url = "https://profitz.app/terms_and_conditions";
        }
        else if (terms.equals("privacy_policy")){
            url = "https://profitz.app/privacy_policy";

        }
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.loadUrl(url);


        return v;
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
