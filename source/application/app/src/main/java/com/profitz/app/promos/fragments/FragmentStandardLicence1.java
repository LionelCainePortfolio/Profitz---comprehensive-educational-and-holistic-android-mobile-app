package com.profitz.app.promos.fragments;


import android.os.Bundle;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.profitz.app.R;public class FragmentStandardLicence1 extends Fragment {

    public static  FragmentStandardLicence1  newInstance() {
        return new  FragmentStandardLicence1 ();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_standard_licence_1, container, false);
        return view;
    }
}