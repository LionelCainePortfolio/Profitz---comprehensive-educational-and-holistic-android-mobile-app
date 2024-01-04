package com.profitz.app.promos.fragments;


import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpSecondFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    ImageView imageView1;
    EditText editText1;
    String mParam1;
    EditText edit;
    String email_valid;
    Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step2, container, false);
       // textView1 = view.findViewById(R.id.text1_step2);
        textView2 = view.findViewById(R.id.text2_step2);

        //imageView1 = view.findViewById(R.id.img_step2);
        editText1 = view.findViewById(R.id.editTextEmail);
        context = inflater.getContext();

        init (view);

        if (getArguments() != null) {
            mParam1 = getArguments().getString("step2");
            if (mParam1.equals("step_2_empty")) {
                editText1.setError("Wprowadź swój adres email");
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
        if (data.equals("step_2_empty")) {
            editText1.setError("Wprowadź swój adres email.");
            editText1.requestFocus();
        }
        if (data.equals("wrong_email")) {
            editText1.setError("Wprowadź poprawny adres email.");
            editText1.requestFocus();
        }
        if (data.equals("wrong_email_db")) {
            editText1.setError("Podany adres email istnieje już w bazie danych.");
            editText1.requestFocus();
        }
    }
    OnEditTextChanged editListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            editListener=(OnEditTextChanged) getActivity();
        }catch(ClassCastException e){
            throw new ClassCastException(context +"must implemnt onEditPressed");
        }
    }

    private void init(View view) {
        edit = (EditText) view.findViewById(R.id.editTextEmail);
        //cada vez que se modifique texto llamar
        edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String edit11 = edit.getText().toString();

                if (TextUtils.isEmpty(edit11)) {
                    editText1.setError("Wprowadź swój adres email");
                    editText1.requestFocus();
                }
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edit11).matches()) {
                    editText1.setError("Wprowadź poprawny adres email");
                    editText1.requestFocus();
                    editListener.onEditPressed2("wrong_email");
                }
                else{
                final String url = "https://yoururl.com/api/check_email.php?email="+edit11;
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    email_valid = response.getString("email_valid");
                                    // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                                        if (email_valid.equals("1"))
                                        {
                                            edit.setError("Wprowadzony adres email istnieje już w bazie danych.");
                                            edit.requestFocus();
                                            editListener.onEditPressed2("wrong_email_db");

                                        }
                                        else if (email_valid.equals("0")){
                                            edit.setError(null);
                                            editListener.onEditPressed2(edit11);

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