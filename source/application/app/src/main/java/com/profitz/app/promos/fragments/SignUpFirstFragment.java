package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;
import com.google.android.material.textfield.TextInputLayout;

@SuppressLint("ValidFragment")
public class SignUpFirstFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    ImageView imageView1;
    EditText editText1;
    EditText editText2;
    EditText edit;
    EditText edit2;

    String mParam1;
    String mParam12;

    int anim = 1;
    private EditText inputName;
    private TextInputLayout inputLayoutName;
    private EditText inputLastName;
    private TextInputLayout inputLayoutLastName;
    private EditText mEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step1, container, false);
        textView2 = view.findViewById(R.id.text2_step1);
        editText1 = view.findViewById(R.id.editTextName);
        editText2 = view.findViewById(R.id.editTextLastName);

        init (view);

        if (getArguments() != null) {
            mParam1 = getArguments().getString("step1");
            mParam12 = getArguments().getString("step12");

            if (mParam1.equals("step_1_empty")) {
                editText1.setError("Podaj swoje imię");
                editText1.requestFocus();
            }
            else if (mParam12.equals("step_1_lastname_empty")) {
                editText2.setError("Podaj swoje nazwisko");
                editText2.requestFocus();
            }
        }

        inputName = (EditText) view.findViewById(R.id.editTextName);
        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));


        inputLastName = (EditText) view.findViewById(R.id.editTextLastName);
        inputLayoutLastName = (TextInputLayout) view.findViewById(R.id.input_layout_lastname);
        inputLastName.addTextChangedListener(new MyTextWatcher(inputLastName));


        return view;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        if (inputLastName.getText().toString().trim().isEmpty()) {
            inputLayoutLastName.setError(getString(R.string.err_msg_lastname));
            requestFocus(inputLastName);
            return false;
        } else {
            inputLayoutLastName.setErrorEnabled(false);
        }

        return true;
    }
    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editTextName:
                    validateName();
                    break;
                case R.id.editTextLastName:
                    validateLastName();
                    break;
            }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    String data;
    public void setData(String yourData){
        data = yourData;
        if (data.equals("step_1_empty")) {
            editText1.setError("Podaj swoje imię");
            editText1.requestFocus();
        }
        else if (data.equals("step_1_lastname_empty")) {
            editText2.setError("Podaj swoje nazwisko");
            editText2.requestFocus();
        }
        if (data.equals("wrong_name_signs")){
            editText1.setError("Imię zawiera niedozwolone znaki.");
            editText1.requestFocus();
        }
        if (data.equals("wrong_lastname_signs")){
            editText2.setError("Nazwisko zawiera niedozwolone znaki.");
            editText2.requestFocus();
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
        edit = (EditText) view.findViewById(R.id.editTextName);
        edit2 = (EditText) view.findViewById(R.id.editTextLastName);

        //cada vez que se modifique texto llamar

        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String edit11 = edit.getText().toString();
                if (TextUtils.isEmpty(edit11)) {
                    editText1.setError("Podaj swoje imię");
                    editText1.requestFocus();

                }
                else if (edit.getText().toString().length() <3 ) {
                    editText1.setError("Minimum 3 znaki");
                    editText1.requestFocus();
                    editListener.onEditPressed1("wrong_name");

                }
                else if ((edit.getText().toString().contains(" ")) || (edit.getText().toString().contains("!")) || (edit.getText().toString().contains("@")) || (edit.getText().toString().contains("$")) || (edit.getText().toString().contains("!")) || (edit.getText().toString().contains("%")) || (edit.getText().toString().contains("#")) || (edit.getText().toString().contains("^"))|| (edit.getText().toString().contains("&")) || (edit.getText().toString().contains("*")) || (edit.getText().toString().contains("(")) || (edit.getText().toString().contains(")")) || (edit.getText().toString().contains("+")) || (edit.getText().toString().contains("=")) || (edit.getText().toString().contains("'")) || (edit.getText().toString().contains("|")) || (edit.getText().toString().contains("[")) || (edit.getText().toString().contains("]")) || (edit.getText().toString().contains("?")) || (edit.getText().toString().contains(">")) || (edit.getText().toString().contains("<")) || (edit.getText().toString().contains(",")) || (edit.getText().toString().contains("-")))
                {
                    editText1.setError("Imię zawiera niedozwolone znaki.");
                    editText1.requestFocus();
                    editListener.onEditPressed1("wrong_name_signs");
                }
                else{
                    editText1.setError(null);
                    editListener.onEditPressed1(edit11);

                }
            }
        });

        edit2.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String edit12 = edit2.getText().toString();
                if (TextUtils.isEmpty(edit12)) {
                    editText2.setError("Podaj swoje nazwisko");
                    editText2.requestFocus();

                }
                else if (edit2.getText().toString().length() <3 ) {
                    editText2.setError("Minimum 3 znaki");
                    editText2.requestFocus();
                    editListener.onEditPressed12("wrong_last_name");

                }
                else if ((edit2.getText().toString().contains(" ")) || (edit2.getText().toString().contains("!")) || (edit2.getText().toString().contains("@")) || (edit2.getText().toString().contains("$")) || (edit2.getText().toString().contains("!")) || (edit2.getText().toString().contains("%")) || (edit2.getText().toString().contains("#")) || (edit2.getText().toString().contains("^"))|| (edit2.getText().toString().contains("&")) || (edit2.getText().toString().contains("*")) || (edit2.getText().toString().contains("(")) || (edit2.getText().toString().contains(")")) || (edit2.getText().toString().contains("+")) || (edit2.getText().toString().contains("=")) || (edit2.getText().toString().contains("'")) || (edit2.getText().toString().contains("|")) || (edit2.getText().toString().contains("[")) || (edit2.getText().toString().contains("]")) || (edit2.getText().toString().contains("?")) || (edit2.getText().toString().contains(">")) || (edit2.getText().toString().contains("<")) || (edit2.getText().toString().contains(",")) || (edit2.getText().toString().contains("-")))
                {
                    editText2.setError("Nazwisko zawiera niedozwolone znaki.");
                    editText2.requestFocus();
                    editListener.onEditPressed12("wrong_lastname_signs");
                }
                else{
                    editText1.setError(null);
                    editListener.onEditPressed12(edit12);

                }
            }
        });

    }
}
