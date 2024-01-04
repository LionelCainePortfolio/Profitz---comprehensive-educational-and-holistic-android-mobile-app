package com.profitz.app.promos.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;
import com.profitz.app.promos.activities.SignUpActivity;


public class SignUpEightFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    ImageView imageView1;
    EditText editText1;
    String mParam1;
    CheckBox checkBox1;
    CheckBox checkBox4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step8, container, false);
        textView1 = view.findViewById(R.id.text1_step8);
        textView2 = view.findViewById(R.id.text2_step8);
        imageView1 = view.findViewById(R.id.img_step8);
        checkBox1 = view.findViewById(R.id.chk1_step8);
        checkBox4 = view.findViewById(R.id.chk4_step8);

        init (view);

        if (getArguments() != null) {
            mParam1 = getArguments().getString("step8");
            if (mParam1.equals("step8_empty_ch1")) {
                checkBox1.setError("Zaznacz zgodę by kontynuuować");
            }
            if (mParam1.equals("step8_empty_ch4")) {
                checkBox4.setError("Zaznacz zgodę by kontynuuować");
            }
            if (mParam1.equals("step8_empty_allch")) {
                checkBox1.setError("Zaznacz zgodę by kontynuuować");
                checkBox4.setError("Zaznacz zgodę by kontynuuować");
            }
        }
        return view;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    String data;
    public void setData(String yourData){
        data = yourData;
        if (data.equals("step8_empty_allch")) {
            if (!checkBox1.isChecked())
            {
                checkBox1.setError("Zaznacz zgodę by kontynuuować");
            }
            else{
                checkBox1.setError(null);
            }
            if (!checkBox4.isChecked())
            {
                checkBox4.setError("Zaznacz zgodę by kontynuuować");
            }
            else{
                checkBox4.setError(null);
            }
        }

        if (data.equals("step8_empty_ch1")) {
            checkBox1.setError("Zaznacz zgodę by kontynuuować");
            checkBox1.requestFocus();

        }
        if (data.equals("step8_empty_ch4")) {
            checkBox4.setError("Zaznacz zgodę by kontynuuować");
            checkBox4.requestFocus();

        }
    }
    OnEditTextChanged editListener;
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            editListener=(OnEditTextChanged) getActivity();
        }catch(ClassCastException e){
            throw new ClassCastException(activity +"must implemnt onEditPressed");
        }
    }

    private void init(View view) {

        CheckBox edit1 = (CheckBox) view.findViewById(R.id.chk1_step8);
        CheckBox edit4 = (CheckBox) view.findViewById(R.id.chk4_step8);

        TextView text3_step7 = (TextView) view.findViewById(R.id.text3_step7);
        TextView text4_step7 = (TextView) view.findViewById(R.id.text4_step7);

        Bundle args = new Bundle();



        text3_step7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                BottomSheetDialogTerms bottomSheetDialogTerms = new BottomSheetDialogTerms();
                args.putString("terms","terms_and_conditions");

                bottomSheetDialogTerms.setArguments(args);
                bottomSheetDialogTerms.show(getParentFragmentManager(), bottomSheetDialogTerms.getTag());

                                          }
                                      });
        text4_step7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogTerms bottomSheetDialogTerms = new BottomSheetDialogTerms();
                args.putString("terms","privacy_policy");

                bottomSheetDialogTerms.setArguments(args);
                bottomSheetDialogTerms.show(getParentFragmentManager(), bottomSheetDialogTerms.getTag());

            }
        });



        edit1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (!isChecked) {
                    editListener.onEditPressed81("step8_empty_ch1");
                    edit1.setError("Zaznacz zgodę by kontynuuować");
                    edit1.requestFocus();

                }
                else {
                    editListener.onEditPressed84("step8_full_ch1");
                    edit1.setError(null);
                }
                if ((checkBox1.isChecked()) && (checkBox4.isChecked()))
                {
                    SignUpActivity.visibleButton();
                }
                else{
                    SignUpActivity.hideButton();
                }
            }
        });
        edit4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (!isChecked) {
                    editListener.onEditPressed84("step8_empty_ch4");
                    edit4.setError("Zaznacz zgodę by kontynuuować");
                    edit4.requestFocus();
                } else {
                    editListener.onEditPressed84("step8_full_ch4");
                    edit4.setError(null);
                }
                if ((checkBox1.isChecked()) && (checkBox4.isChecked()))
                {
                    SignUpActivity.visibleButton();
                }
                else{
                    SignUpActivity.hideButton();
                }
            }
        });
    }
}
