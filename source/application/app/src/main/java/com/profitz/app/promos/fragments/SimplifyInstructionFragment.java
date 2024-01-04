package com.profitz.app.promos.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.profitz.app.R;import com.profitz.app.promodetail.PromoDetailActivity;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.adapters.SimplifiedInstructionsAdapter;
import com.profitz.app.promos.data.DataInstructions;
import com.profitz.app.util.PremiumRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SimplifyInstructionFragment extends Fragment{
    String username;
  // DiscreteScrollView mRvSimplifiedInstructions;
    private PremiumRecyclerView mRvSimplifiedInstructions;
    private SimplifiedInstructionsAdapter mSimplifiedInstructionsAdapter;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String promoName;
    private Context mContext;
    private Bundle bundle;
    User user;
    public SimplifyInstructionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simplified_instruction, container, false);
        user = MyPreferenceManager.getInstance(mContext).getUser();


        mRvSimplifiedInstructions = view.findViewById(R.id.rv_simplified_instructions);
      //  Bundle mArgs = getArguments();
       // promoId=mArgs.getInt("promoId");

        bundle = this.getArguments();
        promoName = bundle.getString("promoName");
        new AsyncInstructions().execute();
        return view;
    }
public void openChat(){
    ((PromoDetailActivity) getActivity()).openChat();

}
    private class AsyncInstructions extends AsyncTask<String, String, String> {
        // ProgressDialog pdLoading = new ProgressDialog(DoneActivity.this);
        HttpURLConnection conn;
        URL url = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                User user = MyPreferenceManager.getInstance(mContext).getUser();
                url = new URL("https://yoururl.com/api/get_instructions.php?type=simplified&simply=true&promo_name="+promoName);

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            // gif_loader.setVisibility(View.GONE);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    handler.postDelayed(this, 2500);
                }
            }, 2000);


            // pdLoading.dismiss();
            List<DataInstructions> data=new ArrayList<>();
            // pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    DataInstructions instructionsData = new DataInstructions();
                    instructionsData.instructionsName= json_data.getString("promo_name");
                    instructionsData.instructionsTitle= json_data.getString("promo_title");
                    instructionsData.instructionsPromoId= json_data.getInt("promo_id");
                    instructionsData.instructionsStep= json_data.getString("promo_step_number");
                    instructionsData.instructionsStepDescription= json_data.getString("promo_step_description");
                    instructionsData.instructionsStepDescription2= json_data.getString("promo_step_description2");
                    instructionsData.instructionsStepDescription3= json_data.getString("promo_step_description3");
                    instructionsData.instructionsUrlScreen1= json_data.getString("promo_url_screen1");
                    instructionsData.instructionsUrlScreen2= json_data.getString("promo_url_screen2");

                    data.add(instructionsData);
                }

                mSimplifiedInstructionsAdapter = new SimplifiedInstructionsAdapter(mContext, data, 1,getChildFragmentManager(), SimplifyInstructionFragment.this);
                // mRvMovies.setLayoutManager(layoutManager);
                //mRvMovies.setHasFixedSize(true);
                ///InfiniteScrollAdapter wrapper = InfiniteScrollAdapter.wrap(mPromosAdapter);
                mRvSimplifiedInstructions.setAdapter(mSimplifiedInstructionsAdapter);
                mRvSimplifiedInstructions.setNestedScrollingEnabled(false);
                SnapHelper snapHelper = new PagerSnapHelper();
                snapHelper.attachToRecyclerView(mRvSimplifiedInstructions);
                mRvSimplifiedInstructions.setLayoutManager(new GridLayoutManager(mContext,1,GridLayoutManager.HORIZONTAL, false));
                // mRvSimplifiedInstructions.setSlideOnFling(true);
              /* mRvSimplifiedInstructions.setItemTransformer(new ScaleTransformer.Builder()
                     .setMaxScale(1.05f)
                               .setMinScale(0.8f)
                       .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                      .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                       .build());
*/
                mRvSimplifiedInstructions.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                        int action = e.getAction();
                        if (action == MotionEvent.ACTION_MOVE) {
                            rv.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });
            } catch (JSONException e) {
                //Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }



}