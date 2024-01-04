package com.profitz.app.promos.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
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
import com.profitz.app.promos.activities.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpSeventhFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    ImageView imageView1;
    EditText editText1;
    EditText edit;
    String promo_valid;
    Context context;
    String edit11;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step7, container, false);
        //textView1 = view.findViewById(R.id.text1_step6);
        textView2 = view.findViewById(R.id.text2_step7);
        //imageView1 = view.findViewById(R.id.img_step6);
        //editText1 = view.findViewById(R.id.editPromoCode);
        String mParam1;
        // edit = (EditText) view.findViewById(R.id.editPromoCode);

        context = inflater.getContext();

        if (getArguments() != null) {
            mParam1 = getArguments().getString("step7");
            if (mParam1.equals("wrong_code")) {
                edit.setError("Wprowadzony kod jest nieprawidłowy.");
                edit.requestFocus();
            }
            if (data.equals("wrong_code")) {
                edit.setError("Wprowadzony kod jest nieprawidłowy.");
                edit.requestFocus();
            }

        }
        init (view);

        return view;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    String data;
    public void setData(String yourData){
        data = yourData;
        // editText1.setError("Wprowadzony kod jest nieprawidłowy.");
        // editText1.requestFocus();
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
        edit = (EditText) view.findViewById(R.id.editPromoCode);
        edit11 = edit.getText().toString();
        edit11 = "ZARABIAJONLINE2020";
        edit.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        if (edit.getText().toString().trim().length() == 0)
        {
            ((SignUpActivity)getActivity()).skip();
            edit11 = "ZARABIAJONLINE2020";
        }

        editListener.onEditPressed7(edit11);
        //cada vez que se modifique texto llamar
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (edit.getText().toString().trim().length() == 0)
                {
                    //edit11 = "ZARABIAJONLINE2020";
                   // Toast.makeText(context, "2 -"+edit11, Toast.LENGTH_SHORT).show();
                    ((SignUpActivity)getActivity()).skip();

                }
                else{
                    ((SignUpActivity)getActivity()).approve_code();

                    editListener.onEditPressed7("wrong_code");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edit11 = edit.getText().toString();
                if (edit.getText().toString().trim().length() == 0) {
                    edit.setError(null);
                    ((SignUpActivity)getActivity()).skip();
                    editListener.onEditPressed7("ZARABIAJONLINE2020");
                }
                else{
                    ((SignUpActivity)getActivity()).approve_code();


                final String url = "https://yoururl.com/api/check_promo_code.php?promo_code="+ edit11;

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    promo_valid = response.getString("promo_valid");
                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                                    if (promo_valid.equals("0"))
                                    {
                                        edit.setError("Wprowadzony kod jest nieprawidłowy.");
                                        edit.requestFocus();
                                        editListener.onEditPressed7("wrong_code");

                                    }
                                    else if (promo_valid.equals("1")){
                                        edit.setError(null);
                                        edit11 = edit.getText().toString();
                                        editListener.onEditPressed7(edit11);

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