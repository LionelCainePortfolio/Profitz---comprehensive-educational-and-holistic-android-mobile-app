package com.profitz.app.promos.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragmentTransparent;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class BottomSheetDialogHelp extends SuperBottomSheetFragmentTransparent {
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    String groupPosition;
    String childPosition;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle mArgs = getArguments();
        groupPosition = mArgs.getString("groupPosition");
        childPosition = mArgs.getString("childPosition");
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        String  input = childPosition;
        input = input.replace(" ", "_");
        input = input.replace("?", "");
        String answer = getString(getResources().getIdentifier("com.profitz.app.:string/" + input, null, null));

        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();

        v = inflater.inflate(R.layout.dialog_help, container, false);
        TextView dialog_question_info = v.findViewById(R.id.dialog_question_info);
        dialog_question_info.setText(childPosition);

        TextView dialog_question_info2 = v.findViewById(R.id.dialog_question_info2);
        dialog_question_info2.setText(answer);

        Button button_info_dismiss = v.findViewById(R.id.button_info_dismiss);
        button_info_dismiss.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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
