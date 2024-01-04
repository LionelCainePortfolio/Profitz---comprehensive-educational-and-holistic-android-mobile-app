package com.profitz.app.promos.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.R;import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.data.DataAirdrop;
import com.joooonho.SelectableRoundedImageView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AdapterAirdrop extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private final Context context;
    private final LayoutInflater inflater;
    List<DataAirdrop> data= Collections.emptyList();
    private final List<DataAirdrop> dataListFull;
    private final OnItemClickListener mOnItemClickListener;
    private final String userId;

    int currentPos=0;
    public interface OnItemClickListener {
        void onItemClick(View view, int id, String airdropTitle, String airdropEarn, String airdropInstruction, String airdropImgUrl, String airdropUrl, String airdropShortDesc, String isExclusive, String airdropWhitepaper, String airdropWebsite, String airdropExchanges, String airdropToken, String airdropPlatform, String airdropTotalSupply, String airdropIsInactive,  String airdropIsUpline, String airdropIsHot, String airdropIsNew, String airdropIsExclusive, String airdropKycRequired, String airdropMailRequired, String airdropFacebookRequired, String airdropTwitterRequired, String airdropTelegramRequired, String airdropTwitter, String airdropTelegram, String airdropMedium, String airdropCoinmarketCap, String airdropReddit, String airdropDiscord);
    }
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterAirdrop(Context context, List<DataAirdrop> data, String user_id, OnItemClickListener onItemClickListener){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
        dataListFull = new ArrayList<>(data);
        userId = user_id;
        mOnItemClickListener = onItemClickListener;


    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_airdrop, parent,false);
        MyHolder holder=new MyHolder(view);


        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        DataAirdrop current=data.get(position);

        myHolder.textDescription.setText(current.airdropDescription);
        String imgUrl = current.airdropImage;
        String value = "$ "+current.airdropValue;
        myHolder.textValue.setText(value);
        myHolder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                final String url_to_check_promo = "https://yoururl.com/api/get_airdrop_url.php?airdrop_id="+ current.airdropId+"&user_id="+userId;

                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                        (com.android.volley.Request.Method.GET, url_to_check_promo, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String get_airdrop_url = response.getString("url");
                                    mOnItemClickListener.onItemClick(v, current.airdropId, current.airdropTitle, String.valueOf(current.airdropValue), current.airdropDescription, current.airdropImage, get_airdrop_url, current.airdropShortDesc, current.airdropExclusive, current.airdropWhitepaper, current.airdropWebsite, current.airdropExchanges, current.airdropToken, current.airdropPlatform, current.airdropTotalSupply, current.airdropIsInActive, current.airdropUpline, current.airdropHot, current.airdropNew, current.airdropExclusive, current.airdropKycRequired, current.airdropMailRequired, current.airdropFacebookRequired, current.airdropTwitterRequired, current.airdropTelegramRequired, current.airdropTwitter, current.airdropTelegramRequired, current.airdropMedium, current.airdropCoinmarketcap, current.airdropReddit, current.airdropDiscord);

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
                Volley.newRequestQueue(context).add(jsonObjectRequest2);
            }
        });



        if (current.airdropKycRequired.equals("1")){
            myHolder.kyc_airdrop_required.setVisibility(View.VISIBLE);
        }
        if (current.airdropMailRequired.equals("1")){
            myHolder.mail_airdrop_required.setVisibility(View.VISIBLE);
        }
        if (current.airdropFacebookRequired.equals("1")){
            myHolder.fb_airdrop_required.setVisibility(View.VISIBLE);
        }
        if (current.airdropTwitterRequired.equals("1")){
            myHolder.twitter_airdrop_required.setVisibility(View.VISIBLE);
        }
        if (current.airdropTelegramRequired.equals("1")){
            myHolder.telegram_airdrop_required.setVisibility(View.VISIBLE);
        }



        if (current.airdropNew.equals("1")){
            myHolder.ivNew.setVisibility(View.VISIBLE);
        }
        if (current.airdropHot.equals("1")){
            myHolder.ivHot.setVisibility(View.VISIBLE);
        }

        myHolder.progressAnimation =    (AnimationDrawable) myHolder.ivAirdropAnim.getDrawable();
        myHolder.progressAnimation.setCallback(myHolder.ivAirdropAnim);
        myHolder.progressAnimation.setVisible(true, true);
        myHolder.ivAirdropAnim.post(new Runnable() {
            @Override
            public void run() {
                myHolder.progressAnimation.start();
            }
        });

        if (current.airdropExclusive.equals("1")){
            myHolder.ivExclusive.setVisibility(View.VISIBLE);
            myHolder.bg_exclusive.setVisibility(View.VISIBLE);
            User user = MyPreferenceManager.getInstance(context).getUser();
            final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user.getId();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                String premium = response.getString("premium");

                                if (premium.equals("0")) {
                                    myHolder.textTitle.setVisibility(View.GONE);
                                    myHolder.textTitle.setText(current.airdropTitle);
                                    float radius = myHolder.textTitle.getTextSize() / 3;
                                    BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
                                    myHolder.textTitle.getPaint().setMaskFilter(filter);


                                    Picasso.with( context )
                                            .load( "https://yoururl.com/api/images/airdrops/locked_content.png" )
                                            .fit()
                                            .config(Bitmap.Config.ARGB_4444)
                                            .centerCrop()
                                            .noFade()
                                            .error( context.getDrawable( R.drawable.progresscircle ) )
                                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                                            .networkPolicy(NetworkPolicy.NO_CACHE)
                                            .into( myHolder.ivAirdrop ,new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    myHolder.ivAirdropAnim.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onError() {
                                                    // TODO Auto-generated method stub

                                                }
                                            });
                                    myHolder.textTitle.setVisibility(View.VISIBLE);

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
        else{
            Picasso.with( context )
                    .load( imgUrl )
                    .fit()
                    .config(Bitmap.Config.ARGB_4444)
                    .centerCrop()
                    .noFade()
                    .error( context.getDrawable( R.drawable.progresscircle ) )
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into( myHolder.ivAirdrop,new Callback() {
                        @Override
                        public void onSuccess() {
                            myHolder.ivAirdropAnim.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            // TODO Auto-generated method stub

                        }
                    });
            myHolder.textTitle.setText(current.airdropTitle);
            myHolder.bg_exclusive.setVisibility(View.GONE);
        }




        if (current.airdropUpline.equals("1")){
            myHolder.ivUpline.setVisibility(View.VISIBLE);
        }
        if (current.airdropIsInActive.equals("1")){
            myHolder.ivInactive.setVisibility(View.VISIBLE);
            myHolder.bg_inactive.setVisibility(View.VISIBLE);

        }











    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }
    private final Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DataAirdrop> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(dataListFull);

            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DataAirdrop item : dataListFull) {
                    if (item.airdropTitle.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            data.clear();
            data.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

/*
    public Filter getFilter() {
        return dataFilter;
    }
    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DataAirdrop> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(dataListFull);

            }
            else
            {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DataAirdrop item : dataListFull){
                    if (item.airdropTitle.toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
         //   data.clear();
            //data.addAll((List) filterResults.values);
            data = (List<DataAirdrop>) filterResults.values;
            notifyDataSetChanged();

        }
    };
*/
    class MyHolder extends RecyclerView.ViewHolder{

        TextView textDescription;
        TextView textTitle;
        TextView textValue;

        SelectableRoundedImageView ivAirdrop;
        ImageView ivNew;
        ImageView ivHot;
        ImageView ivExclusive;
        ImageView ivInactive;
        ImageView ivUpline;
        RelativeLayout globalRL;
        RoundedImageView bg_exclusive;
        RoundedImageView bg_inactive;
        SelectableRoundedImageView ivAirdropBlur;
        ImageView ivAirdropAnim;

        RelativeLayout kyc_airdrop_required;
        RelativeLayout mail_airdrop_required;
        RelativeLayout fb_airdrop_required;
        RelativeLayout twitter_airdrop_required;
        RelativeLayout telegram_airdrop_required;
        public View container;
    AnimationDrawable progressAnimation;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textDescription= (TextView) itemView.findViewById(R.id.description);
            textTitle= (TextView) itemView.findViewById(R.id.title);
            textValue = (TextView) itemView.findViewById(R.id.value);
            ivAirdrop= (SelectableRoundedImageView) itemView.findViewById(R.id.ivAirdrop);
            ivNew = itemView.findViewById(R.id.airdrop_new_img);
            ivHot = itemView.findViewById(R.id.airdrop_hot_img);
            ivExclusive = itemView.findViewById(R.id.airdrop_exclusive_img);
            ivInactive = itemView.findViewById(R.id.airdrop_inactive_img);
            ivUpline = itemView.findViewById(R.id.airdrop_time_img);
            globalRL = itemView.findViewById(R.id.rl_global_airdrop);
            bg_exclusive = itemView.findViewById(R.id.bg_exclusive);
            bg_inactive = itemView.findViewById(R.id.bg_inactive);
            kyc_airdrop_required = itemView.findViewById(R.id.kyc_airdrop_required);
            mail_airdrop_required = itemView.findViewById(R.id.mail_airdrop_required);
            fb_airdrop_required = itemView.findViewById(R.id.fb_airdrop_required);
            twitter_airdrop_required = itemView.findViewById(R.id.twitter_airdrop_required);
            telegram_airdrop_required = itemView.findViewById(R.id.telegram_airdrop_required);
            ivAirdropAnim =  itemView.findViewById(R.id.ivAirdropAnim);
            container = itemView;

        }

    }

}
