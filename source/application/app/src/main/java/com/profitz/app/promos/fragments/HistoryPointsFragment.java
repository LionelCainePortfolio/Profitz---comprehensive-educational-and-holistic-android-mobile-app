package com.profitz.app.promos.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.profitz.app.R;@SuppressLint("ValidFragment")
public class HistoryPointsFragment extends Fragment {
    TextView textView1;
    TextView textView2;
    ImageView imageView1;
    EditText editText1;
    EditText edit;
    String mParam1;
    private EditText mEditText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Test", "hello");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historypoints, container, false);
        //textView1 = view.findViewById(R.id.text1_step1);
        //textView2 = view.findViewById(R.id.text2_step1);
        //imageView1 = view.findViewById(R.id.img_step1);
        //editText1 = view.findViewById(R.id.editTextName);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
