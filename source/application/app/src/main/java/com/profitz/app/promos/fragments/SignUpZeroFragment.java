package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.profitz.app.R;import com.profitz.app.promos.OnEditTextChanged;
import com.hbb20.CountryCodePicker;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("ValidFragment")
public class SignUpZeroFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    ImageView imageView1;
    EditText editText1;
    EditText edit;
    String mParam1;
    String country;
    int anim = 1;
    private EditText mEditText;
    OnEditTextChanged editListener;
    CountryCodePicker countryCodePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_step0, container, false);
        countryCodePicker = view.findViewById(R.id.editCountry);

        editListener.onEditPressed0("Poland");

        RelativeLayout country1 = view.findViewById(R.id.country1);
        RelativeLayout country2 = view.findViewById(R.id.country2);
        RelativeLayout country3 = view.findViewById(R.id.country3);
        RelativeLayout country4 = view.findViewById(R.id.country4);
        RelativeLayout country5 = view.findViewById(R.id.country5);


        RelativeLayout country1_unpicked = view.findViewById(R.id.country1_unpicked);
        RelativeLayout country2_unpicked = view.findViewById(R.id.country2_unpicked);
        RelativeLayout country3_unpicked = view.findViewById(R.id.country3_unpicked);
        RelativeLayout country4_unpicked = view.findViewById(R.id.country4_unpicked);
        RelativeLayout country5_unpicked = view.findViewById(R.id.country5_unpicked);

        CircleImageView country_img5_picked = view.findViewById(R.id.country_img5);

        ImageView country_img5_unpicked = view.findViewById(R.id.country_img5_unpicked);




        country1_unpicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country1.setVisibility(View.VISIBLE);
                country2.setVisibility(View.GONE);
                country3.setVisibility(View.GONE);
                country4.setVisibility(View.GONE);
                country5.setVisibility(View.GONE);
                country1_unpicked.setVisibility(View.GONE);
                country2_unpicked.setVisibility(View.VISIBLE);
                country3_unpicked.setVisibility(View.VISIBLE);
                country4_unpicked.setVisibility(View.VISIBLE);
                country5_unpicked.setVisibility(View.VISIBLE);
                country = "Poland";
                editListener.onEditPressed0(country);
                country_img5_unpicked.setVisibility(View.VISIBLE);
                country_img5_picked.setBackgroundResource(R.drawable.eu_flag);


            }
        });

        country2_unpicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country1.setVisibility(View.GONE);
                country2.setVisibility(View.VISIBLE);
                country3.setVisibility(View.GONE);
                country4.setVisibility(View.GONE);
                country5.setVisibility(View.GONE);
                country1_unpicked.setVisibility(View.VISIBLE);
                country2_unpicked.setVisibility(View.GONE);
                country3_unpicked.setVisibility(View.VISIBLE);
                country4_unpicked.setVisibility(View.VISIBLE);
                country5_unpicked.setVisibility(View.VISIBLE);
                country = "Lativia";
                editListener.onEditPressed0(country);
                country_img5_unpicked.setVisibility(View.VISIBLE);
                country_img5_picked.setBackgroundResource(R.drawable.eu_flag);

            }
        });
        country3_unpicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country1.setVisibility(View.GONE);
                country2.setVisibility(View.GONE);
                country3.setVisibility(View.VISIBLE);
                country4.setVisibility(View.GONE);
                country5.setVisibility(View.GONE);
                country1_unpicked.setVisibility(View.VISIBLE);
                country2_unpicked.setVisibility(View.VISIBLE);
                country3_unpicked.setVisibility(View.GONE);
                country4_unpicked.setVisibility(View.VISIBLE);
                country5_unpicked.setVisibility(View.VISIBLE);
                country = "Ukraine";
                editListener.onEditPressed0(country);
                country_img5_unpicked.setVisibility(View.VISIBLE);
                country_img5_picked.setBackgroundResource(R.drawable.eu_flag);

            }
        });
        country4_unpicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                country1.setVisibility(View.GONE);
                country2.setVisibility(View.GONE);
                country3.setVisibility(View.GONE);
                country4.setVisibility(View.VISIBLE);
                country5.setVisibility(View.GONE);
                country1_unpicked.setVisibility(View.VISIBLE);
                country2_unpicked.setVisibility(View.VISIBLE);
                country3_unpicked.setVisibility(View.VISIBLE);
                country4_unpicked.setVisibility(View.GONE);
                country5_unpicked.setVisibility(View.VISIBLE);
                country = "Germany";
                editListener.onEditPressed0(country);
                country_img5_unpicked.setVisibility(View.VISIBLE);
                country_img5_picked.setBackgroundResource(R.drawable.eu_flag);

            }
        });
        country5_unpicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryCodePicker.launchCountrySelectionDialog();
                country1.setVisibility(View.GONE);
                country2.setVisibility(View.GONE);
                country3.setVisibility(View.GONE);
                country4.setVisibility(View.GONE);
                country5.setVisibility(View.VISIBLE);
                country1_unpicked.setVisibility(View.VISIBLE);
                country2_unpicked.setVisibility(View.VISIBLE);
                country3_unpicked.setVisibility(View.VISIBLE);
                country4_unpicked.setVisibility(View.VISIBLE);
                country5_unpicked.setVisibility(View.GONE);
                country = countryCodePicker.getSelectedCountryEnglishName();
                editListener.onEditPressed0(country);
            }
        });

        country5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryCodePicker.launchCountrySelectionDialog();
            }
        });




        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                country = countryCodePicker.getSelectedCountryEnglishName();
                editListener.onEditPressed0(country);
                String flag = countryCodePicker.getSelectedCountryNameCode();
                country_img5_unpicked.setVisibility(View.GONE);
                if (flag.equals("AL")){
                    country_img5_picked.setImageResource(R.drawable.flag_albania);

                }
                else if (flag.equals("AR")){
                    country_img5_picked.setImageResource(R.drawable.flag_argentina);
                }
                else if (flag.equals("AU")){
                    country_img5_picked.setImageResource(R.drawable.flag_australia);
                }
                else if (flag.equals("AT")){
                    country_img5_picked.setImageResource(R.drawable.flag_austria);
                }
                else if (flag.equals("BE")){
                    country_img5_picked.setImageResource(R.drawable.flag_belgium);
                }
                else if (flag.equals("BY")){
                    country_img5_picked.setImageResource(R.drawable.flag_belarus);
                }
                else if (flag.equals("BG")){
                    country_img5_picked.setImageResource(R.drawable.flag_bulgaria);
                }
                else if (flag.equals("HR")){
                    country_img5_picked.setImageResource(R.drawable.flag_croatia);
                }
                else if (flag.equals("CZ")){
                    country_img5_picked.setImageResource(R.drawable.flag_czech_republic);
                }
                else if (flag.equals("DK")){
                    country_img5_picked.setImageResource(R.drawable.flag_denmark);
                }
                else if (flag.equals("EE")){
                    country_img5_picked.setImageResource(R.drawable.flag_estonia);
                }
                else if (flag.equals("FI")){
                    country_img5_picked.setImageResource(R.drawable.flag_finland);
                }
                else if (flag.equals("FR")){
                    country_img5_picked.setImageResource(R.drawable.flag_france);
                }
                else if (flag.equals("GR")){
                    country_img5_picked.setImageResource(R.drawable.flag_greece);
                }
                else if (flag.equals("GE")){
                    country_img5_picked.setImageResource(R.drawable.flag_georgia);
                }
                else if (flag.equals("ES")){
                    country_img5_picked.setImageResource(R.drawable.flag_spain);
                }
                else if (flag.equals("NL")){
                    country_img5_picked.setImageResource(R.drawable.flag_netherlands);
                }
                else if (flag.equals("IE")){
                    country_img5_picked.setImageResource(R.drawable.flag_ireland);
                }
                else if (flag.equals("IS")){
                    country_img5_picked.setImageResource(R.drawable.flag_iceland);
                }
                else if (flag.equals("LU")){
                    country_img5_picked.setImageResource(R.drawable.flag_luxembourg);
                }
                else if (flag.equals("LV")){
                    country_img5_picked.setImageResource(R.drawable.flag_latvia);
                }
                else if (flag.equals("NO")){
                    country_img5_picked.setImageResource(R.drawable.flag_norway);
                }
                else if (flag.equals("PT")){
                    country_img5_picked.setImageResource(R.drawable.flag_portugal);
                }
                else if (flag.equals("RU")){
                    country_img5_picked.setImageResource(R.drawable.flag_russian_federation);
                }
                else if (flag.equals("RO")){
                    country_img5_picked.setImageResource(R.drawable.flag_romania);
                }
                else if (flag.equals("SK")){
                    country_img5_picked.setImageResource(R.drawable.flag_slovakia);
                }
                else if (flag.equals("SI")){
                    country_img5_picked.setImageResource(R.drawable.flag_slovenia);
                }
                else if (flag.equals("CH")){
                    country_img5_picked.setImageResource(R.drawable.flag_switzerland);
                }
                else if (flag.equals("SE")){
                    country_img5_picked.setImageResource(R.drawable.flag_sweden);
                }
                else if (flag.equals("HU")){
                    country_img5_picked.setImageResource(R.drawable.flag_hungary);
                }
                else if (flag.equals("GB")){
                    country_img5_picked.setImageResource(R.drawable.flag_united_kingdom);
                }
                else if (flag.equals("IT")){
                    country_img5_picked.setImageResource(R.drawable.flag_italy);
                }

            }
        });


        return view;


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
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
    String data;
    public void setData(String yourData){
        data = yourData;

    }


}
