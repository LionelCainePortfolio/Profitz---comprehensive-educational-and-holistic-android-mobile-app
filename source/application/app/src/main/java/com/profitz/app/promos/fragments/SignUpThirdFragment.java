package com.profitz.app.promos.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpThirdFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    ImageView imageView1;
    EditText editText1;
    String mParam1;
    EditText edit;
    String username_valid;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step3, container, false);
       // textView1 = view.findViewById(R.id.text1_step3);
        textView2 = view.findViewById(R.id.text2_step3);
        //imageView1 = view.findViewById(R.id.img_step3);
        editText1 = view.findViewById(R.id.editTextUsername);
        context = inflater.getContext();
        init (view);

        if (getArguments() != null) {
            mParam1 = getArguments().getString("step3");
            if (mParam1.equals("step_3_empty")) {
                editText1.setError("Utwórz swój login");
                editText1.requestFocus();
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
        if (data.equals("step_3_empty")) {
            editText1.setError("Utwórz swój login.");
            editText1.requestFocus();
        }
        if (data.equals("wrong_username")) {
            editText1.setError("Minimum 5 znaków.");
            editText1.requestFocus();
        }
        if (data.equals("wrong_username_max")) {
            editText1.setError("Maksimum 15 znaków.");
            editText1.requestFocus();
        }
        if (data.equals("wrong_username_name")) {
            editText1.setError("Niedozwolona nazwa użytkownika.");
            editText1.requestFocus();
        }
        if (data.equals("wrong_username_db")) {
            editText1.setError("Nazwa użytkownika istnieje już w bazie danych.");
            editText1.requestFocus();
        }
        if (data.equals("wrong_username_signs")){
            editText1.setError("Nazwa użytkownika zawiera niedozwolone znaki.");
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
        edit = (EditText) view.findViewById(R.id.editTextUsername);

        //cada vez que se modifique texto llamar
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String edit11 = edit.getText().toString();
                if (TextUtils.isEmpty(edit11)) {
                    editText1.setError("Utwórz swój login.");
                    editText1.requestFocus();
                }
                else if (edit.getText().toString().length() <5 ) {
                    editText1.setError("Minimum 5 znaków.");
                    editText1.requestFocus();
                    editListener.onEditPressed3("wrong_username");

                }
                else if ((edit.getText().toString().contains(" ")) || (edit.getText().toString().contains("!")) || (edit.getText().toString().contains("@")) || (edit.getText().toString().contains("$")) || (edit.getText().toString().contains("!")) || (edit.getText().toString().contains("%")) || (edit.getText().toString().contains("#")) || (edit.getText().toString().contains("^"))|| (edit.getText().toString().contains("&")) || (edit.getText().toString().contains("*")) || (edit.getText().toString().contains("(")) || (edit.getText().toString().contains(")")) || (edit.getText().toString().contains("+")) || (edit.getText().toString().contains("=")) || (edit.getText().toString().contains("'")) || (edit.getText().toString().contains("|")) || (edit.getText().toString().contains("[")) || (edit.getText().toString().contains("]")) || (edit.getText().toString().contains("?")) || (edit.getText().toString().contains(">")) || (edit.getText().toString().contains("<")) || (edit.getText().toString().contains(",")) || (edit.getText().toString().contains("-")))
                {
                    editText1.setError("Nazwa użytkownika zawiera niedozwolone znaki.");
                    editText1.requestFocus();
                    editListener.onEditPressed3("wrong_username_signs");
                }
                else if (edit.getText().toString().length() >15 ) {
                    editText1.setError("Maksimum 15 znaków.");
                    editText1.requestFocus();
                    editListener.onEditPressed3("wrong_username_max");

                }
                else if ((edit.getText().toString().equals("Admin")) ||(edit.getText().toString().equals("Administrator")) || (edit.getText().toString().equals("Adminek"))||(edit.getText().toString().equals("Profitz1")) || (edit.getText().toString().equals("Profitz123"))|| (edit.getText().toString().equals("Dupa123"))||(edit.getText().toString().equals("Dupa")) || (edit.getText().toString().equals("dupa"))|| (edit.getText().toString().equals("admin")) )
                {
                    editText1.setError("Niedozwolona nazwa użytkownika.");
                    editText1.requestFocus();
                    editListener.onEditPressed3("wrong_username_name");

                }
                else{
                final String url = "https://yoururl.com/api/check_username.php?username="+edit11;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    username_valid = response.getString("username_valid");
                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();


                                        if (username_valid.equals("1"))
                                        {
                                            edit.setError("Podana nazwa użytkownika istnieje już w bazie danych.");
                                            edit.requestFocus();
                                            editListener.onEditPressed3("wrong_username_db");

                                        }
                                        else if (username_valid.equals("0")){
                                            edit.setError(null);
                                            editListener.onEditPressed3(edit11);

                                        }

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
                Volley.newRequestQueue(context).add(jsonObjectRequest);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {




            }
        });
    }
}