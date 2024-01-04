
package com.profitz.app.promos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.ProfitzApp;
import com.profitz.app.R;import com.profitz.app.data.model.Promo;
import com.profitz.app.promos.fragments.CardFragment;
import com.profitz.app.promos.PromosActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.sentry.Sentry;

public class PromosAdapterTest extends RecycledPagerAdapter<RecycledPagerAdapter.ViewHolder> implements CardAdapter  {

    private static final String TAG = PromosAdapterTest.class.getSimpleName();

    private final PromoClickListener mOnClickListener;

    private final List<CardFragment> fragments;
    private final float baseElevation;
    private List<Promo> mPromos;
    private final Context mContext;
    private int mViewIdAdapter;

    public void changeView(int viewId)
    {
        mViewIdAdapter= viewId;
        notifyDataSetChanged();
    }

    public PromosAdapterTest(Context context, PromoClickListener listener, int viewIdAdapter, FragmentManager fm, float baseElevation) {
        mContext = context;
        mOnClickListener = listener;
        mViewIdAdapter = viewIdAdapter;
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;

        for(int i = 0; i< 8; i++){
            addCardFragment(new CardFragment());
        }
    }
    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return CardFragment.getInstance(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (mViewIdAdapter==0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo, parent, false);
        }
        else if (mViewIdAdapter==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_promo, parent, false);
        }
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


            final Promo promo = mPromos.get(position);
            Picasso.with( mContext )
                    .load( mContext.getString( R.string.tmdb_image_url, mPromos.get( position ).getPosterPath() ) )
                    .error( mContext.getDrawable( R.mipmap.empty_view ) )
                    .into(mImgPoster );

            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            // holder.rl_bg.setBackgroundColor(color);
            final String str = "img_" + rnd.nextInt(12);
            rl_bg.setImageDrawable
                    (
                            mContext.getResources().getDrawable(getResourceID(str, "drawable", mContext))
                    );
           rl_bg.setClipToOutline(true);
            Context context = ProfitzApp.getContext();
            mTextTitle.setText(promo.getTitle());
            //holder.mTextReleaseDate.setText(promo.getReleaseDate());

            String dtStart = promo.getReleaseDate();

            //String dtStart = "2020-05-15 09";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
            try {
                Date futureDate = format.parse(dtStart);
                System.out.println(futureDate);
                mTextReleaseDate.setText(PromosActivity.getCountdownText(context, futureDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mTextRatingScore.setText(String.valueOf(promo.getVoteAverage()));
            if(promo.getPrice().contains("."))
            {
                mTextPrice.setText(promo.getPrice());
            }
            else{
               mTextPrice.setText(promo.getPrice()+".00");
            }
            //holder.mTextPriceType.setText(promo.getPriceType());
            // holder.mTextDifficult.setText(promo.getDifficult());
            mTextHowLong.setText(promo.getHow_long());
            mTextShortDesc.setText(Html.fromHtml(promo.getShortDesc()));
           mTextComments.setText(String.valueOf(promo.getComments()));
            mTextReviews.setText(String.valueOf(promo.getReviews()));
            mTextLikes.setText(String.valueOf(promo.getLikes()));
            mTextPlusXp.setText(promo.getTotalXp() +" xp");
            mWhichPromo.setText((position+1)+"/");

            final String url = "https://yoururl.com/api/get_count_all_active_promos.php";

            JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest
                    (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String get_count_all_promos = response.getString("count");
                                mAllPromo.setText(get_count_all_promos);
                                Sentry.captureMessage("from link");

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
            Volley.newRequestQueue(context).add(jsonObjectRequest4);

    }

    protected final static int getResourceID
            (final String resName, final String resType, final Context ctx)
    {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0)
        {
            throw new IllegalArgumentException
                    (
                            "No resource string found with name " + resName
                    );
        }
        else
        {
            return ResourceID;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (null == mPromos) return 0;
        return mPromos.size();
    }

    public void setPromosData(List<Promo> promosData) {
        mPromos = promosData;
        notifyDataSetChanged();
    }



    public List<Promo> getList() {
        return mPromos;
    }

    // Provide a reference to the views for each data item

    public interface PromoClickListener {
        void onPromoClick(Promo promo);
    }
}
