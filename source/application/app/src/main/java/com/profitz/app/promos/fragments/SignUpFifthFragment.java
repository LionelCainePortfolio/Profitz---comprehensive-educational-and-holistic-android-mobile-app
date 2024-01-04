package com.profitz.app.promos.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpFifthFragment extends Fragment {
    Context context;
    OnEditTextChanged editListener;
    CountryCodePicker countryCodePicker;
    String phone_number;
    EditText phone_number_edit;
    String mParam1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step5, container, false);
        context = inflater.getContext();
        countryCodePicker = view.findViewById(R.id.editNumberCode);
        phone_number_edit = view.findViewById(R.id.editTextPhone);
        //phone_number_edit = view.findViewById(R.id.editNumber);
        countryCodePicker.registerCarrierNumberEditText(phone_number_edit);
        editListener.onEditPressed5(phone_number);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("step5");
            if (mParam1.equals("step_5_empty")) {
                phone_number_edit.setError("Wprowadź swój numer telefonu.");
                phone_number_edit.requestFocus();


            }
            if (data.equals("wrong_number")) {
                phone_number_edit.setError("Wprowadzony numer jest nieprawidłowy.");
                phone_number_edit.requestFocus();
            }
            if (data.equals("wrong_phone_db")) {
                phone_number_edit.setError("Wprowadzony numer telefonu istnieje już w bazie danych.");
                phone_number_edit.requestFocus();
            }
        }
        phone_number_edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (phone_number_edit.getText().toString().trim().length() == 0)
                {
                    //edit11 = "ZARABIAJONLINE2020";
                    // Toast.makeText(context, "2 -"+edit11, Toast.LENGTH_SHORT).show();
                }
                else{
                    editListener.onEditPressed5("wrong_number");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //edit11 = phone_number_edit.getText().toString();

                if (countryCodePicker.isValidFullNumber()){
                    phone_number_edit.setError(null);
                    phone_number = countryCodePicker.getFullNumberWithPlus();

                    //Get complete phone number
                    String _getUserEnteredPhoneNumber = phone_number_edit.getText().toString().trim();
                    if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
                        phone_number_edit.setError("Wprowadzony numer jest nieprawidłowy.");
                    }
                    else{
                        final String _phoneNo = countryCodePicker.getFullNumberWithPlus();
                        editListener.onEditPressed5(_phoneNo);
                        String number_string_text = countryCodePicker.getFormattedFullNumber();
                        String number_string_text_wo_spacebars = number_string_text.replaceAll("\\s+","");

                        final String edit11 = phone_number_edit.getText().toString();

                        final String url = "https://yoururl.com/api/check_phone.php?phone="+number_string_text_wo_spacebars;
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                           String phone_valid = response.getString("phone_valid");
                                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                                                if (phone_valid.equals("1"))
                                                {
                                                    phone_number_edit.setError("Wprowadzony numer telefonu jest już w bazie danych.");
                                                    phone_number_edit.requestFocus();
                                                    editListener.onEditPressed5("wrong_phone_db");

                                                }
                                                else if (phone_valid.equals("0")){
                                                    phone_number_edit.setError(null);
                                                    editListener.onEditPressed5(_phoneNo);
                                                    editListener.onEditPressed51(number_string_text);

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
                else{
                    phone_number_edit.setError("Wprowadzony numer jest nieprawidłowy.");
                    phone_number_edit.requestFocus();
                    editListener.onEditPressed5("wrong_number");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });



        countryCodePicker.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                if (isValidNumber){
                    phone_number_edit.setError(null);
                    phone_number = countryCodePicker.getFullNumberWithPlus();

                    //Get complete phone number
                    String _getUserEnteredPhoneNumber = phone_number_edit.getText().toString().trim();
                    if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
                        phone_number_edit.setError("Wprowadzony numer jest nieprawidłowy.");
                    }
                    else{
                        final String _phoneNo = countryCodePicker.getFullNumberWithPlus();
                        editListener.onEditPressed5(_phoneNo);
                        String number_string_text = countryCodePicker.getFormattedFullNumber();


                        final String edit11 = phone_number_edit.getText().toString();

                        final String url = "https://yoururl.com/api/check_phone.php?phone="+number_string_text;
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            String phone_valid = response.getString("phone_valid");
                                            // Toast.makeText(context, response.getString("status"), Toast.LENGTH_SHORT).show();
                                            if (phone_valid.equals("1"))
                                            {
                                                phone_number_edit.setError("Wprowadzony numer telefonu jest już w bazie danych.");
                                                phone_number_edit.requestFocus();
                                                editListener.onEditPressed5("wrong_phone_db");

                                            }
                                            else if (phone_valid.equals("0")){
                                                phone_number_edit.setError(null);
                                                editListener.onEditPressed5(_phoneNo);
                                                editListener.onEditPressed51(number_string_text);

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
                else {
                       // phone_number_edit.setError("Wprowadzony numer jest nieprawidłowy.");
                       // phone_number_edit.requestFocus();
                       // editListener.onEditPressed5("wrong_number");
                }

            }
        });
       /* countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {

            }
        }); */

        return view;


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    String data;
    public void setData(String yourData){
        data = yourData;
        //phone_number_edit.setError("Wprowadzony numer jest nieprawidłowy.");
      //  phone_number_edit.requestFocus();
    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            editListener=(OnEditTextChanged) getActivity();
        }catch(ClassCastException e){
            throw new ClassCastException(activity +"must implemnt onEditPressed");
        }
    }

}