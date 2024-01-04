package com.profitz.app.promos.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment;
import com.profitz.app.GetReflink;
import com.profitz.app.R;
import com.profitz.app.RefClicked;
import com.profitz.app.promodetail.PromoDetailActivity;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.AdapterReview;
import com.profitz.app.promos.adapters.TabAdapter;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BottomSheetDialogInstructions extends SuperBottomSheetFragment{
    private BottomSheetListener mListener;
    private Context mContext;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    User user2;
    private View bottomSheet;
    private AdapterReview mAdapter;
    int promoIdd;
    String promoName;
    String clickedRef;
    String reflinkUrl;
    View v;
    private TabAdapter adapterTab;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    LottieAnimationView animationView;
    String promoStatus;
    private Swipe swipe;
    private final String[] titles = new String[]{"UPROSZCZONA", "SZCZEGÓŁOWA"};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Bundle mArgs = getArguments();
        promoName = mArgs.getString("promoName");
        clickedRef = mArgs.getString("clickedRef");
        reflinkUrl = mArgs.getString("reflinkUrl");
        user2 = MyPreferenceManager.getInstance(mContext).getUser();
        //promoId = PromoDetailActivity.getId();

        v = inflater.inflate(R.layout.fragment_single_instruction_bottomsheet, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.viewPagerInstructions);
        tabLayout = (TabLayout) v.findViewById(R.id.tabLayoutInstructions);

        adapterTab = new TabAdapter(getChildFragmentManager());
        RefClicked refClicked = com.profitz.app.RefClicked.getInstance();
        GetReflink getReflink = com.profitz.app.GetReflink.getInstance();
        String rrr = getReflink.getReflink();

        Bundle bundle =new Bundle();
        bundle.putString("promoName",promoName);
        bundle.putString("clickedRef", clickedRef);
        bundle.putString("reflink",rrr);
        //new PromoDetailActivity().stopSlider();
        ((PromoDetailActivity)mContext).stopSlider();

        SimplifyInstructionFragment frag_simplify = new  SimplifyInstructionFragment();
        ExtendedInstructionFragment frag_extended = new ExtendedInstructionFragment();
        UrlInstructionFragment frag_url = new UrlInstructionFragment();

        frag_simplify.setArguments(bundle);
        frag_extended.setArguments(bundle);

        adapterTab.addFragment(frag_extended, "SZCZEGÓŁOWA");
        adapterTab.addFragment(frag_simplify, "UPROSZCZONA");
        viewPager.setAdapter(adapterTab);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(mContext, R.color.lbl_name));
//        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.lbl_name));
        //<String> listen = new MutableLiveData<>();

        //listen.setValue(refClicked.getData()); //Initilize with a value

       /* listen.observe(myLifecycleOwner,new Observer<String>() {
            @Override
            public void onChanged(String changedValue) {
                if (Objects.equals(refClicked.getData(), "1")){

                }
                //Do something with the changed value
            }
        });
        */
        refClicked.setListener(new RefClicked.ChangeListener() {
            @Override
            public void onChange() {
                if (Objects.equals(refClicked.getData(), "1")){
                    frag_url.setArguments(bundle);
                adapterTab.addFragment(frag_url, promoName);
                adapterTab.notifyDataSetChanged();
                    tabLayout.setupWithViewPager(viewPager);

                    tabLayout.getTabAt(0).setText("SZCZEGÓŁOWA");
                    tabLayout.getTabAt(1).setText("UPROSZCZONA");
                tabLayout.getTabAt(2).setText(promoName);
                    tabLayout.getTabAt(2).select();
                    refClicked.setListener(null);
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setText("SZCZEGÓŁOWA");
        tabLayout.getTabAt(1).setText("UPROSZCZONA");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });



        return v;
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public interface BottomSheetListener {
        void onButtonClicked(String text);
    }
    public void dispatchSwipe()
    {

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement BottomSheetListener");
        }
    }
    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((PromoDetailActivity)getActivity()).runSlider();

    }
}
