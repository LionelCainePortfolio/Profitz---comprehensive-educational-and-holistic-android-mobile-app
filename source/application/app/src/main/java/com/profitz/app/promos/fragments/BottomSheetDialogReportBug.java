package com.profitz.app.promos.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.Nullable;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.profitz.app.R;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;

public class BottomSheetDialogReportBug extends SuperBottomSheetFragment {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    VideoView video;
    Bundle mArgs;
    String source;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.dialog_report_error, container, false);
        user2 = MyPreferenceManager.getInstance(mContext).getUser();

        video = (VideoView) v.findViewById(R.id.videoview1);
        video.setBackgroundColor(Color.WHITE); //color what you want as background
        video.setZOrderOnTop(true);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        Uri videoPath = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.report_bug);
        video.setVideoURI(videoPath);
        video.start();
        //  dismiss(); - zamyka modal
        mArgs = getArguments();
        if (mArgs != null) {
            source = mArgs.getString("source");
        }
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
    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        dismiss();
    }



}
