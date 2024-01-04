package com.profitz.app.promos.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;


public class SignUpFourthFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    ImageView imageView1;
    EditText editText1;
    String mParam1;
    EditText edit;
    ImageView show_pass_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step4, container, false);
      //  textView1 = view.findViewById(R.id.text1_step4);
        textView2 = view.findViewById(R.id.text2_step4);
     //   imageView1 = view.findViewById(R.id.img_step4);
        editText1 = view.findViewById(R.id.editTextPassword);

        init (view);

        if (getArguments() != null) {
            mParam1 = getArguments().getString("step4");
            if (mParam1.equals("step_4_empty")) {
                editText1.setError("Utwórz swoje hasło");
                editText1.requestFocus();
            }
            if (data.equals("wrong_password")) {
                editText1.setError("Minimum 8 znaków.");
                editText1.requestFocus();
            }
            if (data.equals("wrong_password_x")) {
                editText1.setError("Podane hasło jest niedozwolone.");
                editText1.requestFocus();

            }
        }


        show_pass_btn = view.findViewById(R.id.show_pass_btn);

        show_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText1.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    show_pass_btn.setImageResource(R.drawable.hide_password);

                    //Show Password
                    editText1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    show_pass_btn.setImageResource(R.drawable.show_password);

                    //Hide Password
                    editText1.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }

            }
        });


        return view;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    String data;
    public void setData(String yourData){
        data = yourData;
        if (data.equals("step_4_empty")) {
            editText1.setError("Utwórz swoje hasło");
            editText1.requestFocus();
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
        edit = (EditText) view.findViewById(R.id.editTextPassword);

        //cada vez que se modifique texto llamar
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String edit11 = edit.getText().toString();
                if (TextUtils.isEmpty(edit11)) {
                    editText1.setError("Utwórz swoje hasło");
                    editText1.requestFocus();
                    show_pass_btn.setVisibility(View.GONE);

                }
                else if (edit.getText().toString().length() <8 ) {
                    editText1.setError("Minimum 8 znaków");
                    editText1.requestFocus();
                    editListener.onEditPressed4("wrong_password");
                    show_pass_btn.setVisibility(View.GONE);


                }
                else{
                    editText1.setError(null);
                    editListener.onEditPressed4(edit11);
                    show_pass_btn.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



}