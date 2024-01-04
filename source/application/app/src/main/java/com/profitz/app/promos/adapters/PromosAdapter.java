
package com.profitz.app.promos.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.DecimalFormatSymbols;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.profitz.app.ProfitzApp;
import com.profitz.app.R;import com.profitz.app.data.model.Promo;
import com.profitz.app.promos.MyPreferenceManager;
import com.profitz.app.promos.User;
import com.profitz.app.promos.PromosActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.core.content.ContextCompat.getColor;
import static java.lang.Integer.parseInt;

public class PromosAdapter extends RecyclerView.Adapter<PromosAdapter.ViewHolder> {

    private static final String TAG = PromosAdapter.class.getSimpleName();

    private final PromoClickListener mOnClickListener;

    private List<Promo> mPromos;
    private final Context mContext;
    private int mViewIdAdapter;
    private final ViewPager2 viewPager2;
    String url_exchange;
    String str;
    String user_lvl;
    User user2;

    double promo_earn_before;
    double promo_earn_bonus;
    double promo_before_calculate;
    boolean dontexchange = false;
    public void changeView(int viewId)
    {
        mViewIdAdapter= viewId;
        notifyDataSetChanged();
    }

    public PromosAdapter(Context context, PromoClickListener listener, int viewIdAdapter, ViewPager2 viewPager2) {
        mContext = context;
        mOnClickListener = listener;
        mViewIdAdapter = viewIdAdapter;
        this.viewPager2 = viewPager2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (mViewIdAdapter==0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo, parent, false);
        }
        else if (mViewIdAdapter==1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_promo_premium, parent, false);
           /* Drawable source = ContextCompat.getDrawable(mContext, R.drawable.promo_item_diagonal);
            Drawable mask = ContextCompat.getDrawable(mContext, R.drawable.promo_item_diagonal);
            int[] colors = {
                    Color.RED, Color.BLACK
            };
            Drawable shadow = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
            Shape s = new ShadowCard(mContext, source, mask, shadow, 8);
            view.setBackground(new ShapeDrawable(s));*/
        }
        return new ViewHolder(view);

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
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (position == mPromos.size() - 2)
        {
            viewPager2.post(runnable);
        }
        final Promo promo = mPromos.get(position);
        Picasso.with( mContext )
                .load( mContext.getString( R.string.tmdb_image_url, mPromos.get( position ).getPosterPath() ) )
                .error( mContext.getDrawable( R.mipmap.empty_view ) )
                .into( holder.mImgPoster );

      /*  Random rnd = new Random();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
            int a = rnd.nextInt(12); // this will give numbers between 1 and 50.
            if (!arrayList.contains(a)) {
                arrayList.add(a);
               str = "img_" + a;
            }

        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        // holder.rl_bg.setBackgroundColor(color);
        //holder.rl_bg.setImageDrawable
          //      (
            //            mContext.getResources().getDrawable(getResourceID(str, "drawable", mContext))
              //  );

        holder.rl_bg.setImageBitmap(
                decodeSampledBitmapFromResource(mContext.getResources(), getResourceID(str, "drawable", mContext), 300, 300));
        holder.rl_bg.setClipToOutline(true);
*/
        Context context = ProfitzApp.getContext();
        holder.mTextTitle.setText(promo.getTitle());
        //holder.mTextReleaseDate.setText(promo.getReleaseDate());

        String dtStart = promo.getReleaseDate();

        //String dtStart = "2020-05-15 09";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");
        try {
            Date futureDate = format.parse(dtStart);
            System.out.println(futureDate);
            holder.mTextReleaseDate.setText(PromosActivity.getCountdownText(context, futureDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (String.valueOf(promo.getVoteAverage()).equals("0.0")){
            holder.mTextRatingScore.setText("5.0");
            holder.reviews_count_rl.setVisibility(View.GONE);
        }
        else{
            holder.mTextRatingScore.setText(String.valueOf(promo.getVoteAverage()));
            holder.reviews_count_rl.setVisibility(View.VISIBLE);
        }

        holder.mTextRatingScore.setText(String.valueOf(promo.getVoteAverage()));
        if(promo.getPrice().contains("."))
        {
            holder.mTextPrice.setText(promo.getPrice());
        }
        else{
            holder.mTextPrice.setText(promo.getPrice()+".00");
        }
        holder.mTextPriceType.setText(promo.getPriceType());
        // holder.mTextDifficult.setText(promo.getDifficult());
        holder.mTextHowLong.setText(promo.getHow_long());
        holder.mTextShortDesc.setText(Html.fromHtml(promo.getShortDesc()));
        holder.mTextComments.setText(String.valueOf(promo.getComments()));
        holder.mTextReviews.setText(String.valueOf(promo.getReviews()));
        holder.mTextLikes.setText(String.valueOf(promo.getLikes()));
        //holder.mTextPlusXp.setText(String.valueOf(promo.getTotalXp())+" xp");
        user2 = MyPreferenceManager.getInstance(context).getUser();
        promo_earn_before = Double.parseDouble(promo.getPrice());

        final String url_check_promo_star_status = "https://yoururl.com/api/check_promo_star_status.php?promo_id="+promo.getId();
        JsonObjectRequest jsonObjectRequestx = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, url_check_promo_star_status, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            final String url2 = "https://yoururl.com/api/get_count_all_active_promos.php";

                            JsonObjectRequest jsonObjectRequest4 = new JsonObjectRequest
                                    (com.android.volley.Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {

                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                String get_count_all_promos = response.getString("count");
                                                holder.mAllPromo.setText(get_count_all_promos);
                                                int int_get_count_all_promos = parseInt(get_count_all_promos);
                                                if ((position >= int_get_count_all_promos) && (position <20))
                                                {
                                                    holder.mWhichPromo.setText(((position+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=20) && (position<30)){
                                                    holder.mWhichPromo.setText((((position-10)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=30) && (position<40)){
                                                    holder.mWhichPromo.setText((((position-20)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=40) && (position<50)){
                                                    holder.mWhichPromo.setText((((position-30)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=50) && (position<60)){
                                                    holder.mWhichPromo.setText((((position-40)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=60) && (position<70)){
                                                    holder.mWhichPromo.setText((((position-50)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=70) && (position<80)){
                                                    holder.mWhichPromo.setText((((position-60)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=80) && (position<90)){
                                                    holder.mWhichPromo.setText((((position-70)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=90) && (position<100)){
                                                    holder.mWhichPromo.setText((((position-80)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else if ((position>=100) && (position<110)){
                                                    holder.mWhichPromo.setText((((position-90)+1)-int_get_count_all_promos)+"/");
                                                }
                                                else{
                                                    holder.mWhichPromo.setText((position+1)+"/");
                                                }


                                                if (promo.getPriceType().equals("€")){

                                                    dontexchange = false;


                                                    url_exchange = "https://yoururl.com/api/exchange.php?currency=EUR";
                                                    JsonObjectRequest jsonObjectRequest5 = new JsonObjectRequest
                                                            (com.android.volley.Request.Method.GET, url_exchange, null, new Response.Listener<JSONObject>() {

                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        String getexchange = response.getString("exchange_cur");
                                                                        double getexchangedouble = Double.parseDouble(getexchange);
                                                                        double getPrice = Double.parseDouble(promo.getPrice());
                                                                        double newprice = new BigDecimal(getexchangedouble).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                                                        double exchangetopln = getPrice * newprice;
                                                                        double exchangetopln2 = new BigDecimal(exchangetopln).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                                                        DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));
                                                                        float twoDigitsF = Float.parseFloat(decimalFormat.format(exchangetopln2));
                                                                        String toshow = Float.toString(twoDigitsF);
                                                                        String exchangestring = "≈ "+ toshow + " zł";
                                                                        holder.mToPLN.setVisibility(View.VISIBLE);
                                                                        holder.mToPLN.setText(exchangestring);


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

                                                    Volley.newRequestQueue(context).add(jsonObjectRequest5);

                                                }
                                                else if (promo.getPriceType().equals("$")){
                                                    dontexchange = false;

                                                    url_exchange = "https://yoururl.com/api/exchange.php?currency=USD";
                                                    JsonObjectRequest jsonObjectRequest5 = new JsonObjectRequest
                                                            (com.android.volley.Request.Method.GET, url_exchange, null, new Response.Listener<JSONObject>() {

                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        String getexchange = response.getString("exchange_cur");
                                                                        double getexchangedouble = Double.parseDouble(getexchange);
                                                                        double getPrice = Double.parseDouble(promo.getPrice());
                                                                        double newprice = new BigDecimal(getexchangedouble).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                                                        double exchangetopln = getPrice * newprice;
                                                                        double exchangetopln2 = new BigDecimal(exchangetopln).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                                                        DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));
                                                                        float twoDigitsF = Float.parseFloat(decimalFormat.format(exchangetopln2));
                                                                        String toshow = Float.toString(twoDigitsF);
                                                                        String exchangestring = "≈ "+ toshow + " zł";
                                                                        holder.mToPLN.setVisibility(View.VISIBLE);

                                                                        holder.mToPLN.setText(exchangestring);



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

                                                    Volley.newRequestQueue(context).add(jsonObjectRequest5);
                                                }
                                                else if (promo.getPriceType().equals("£")){
                                                    dontexchange = false;

                                                    url_exchange = "https://yoururl.com/api/exchange.php?currency=GBP";
                                                    JsonObjectRequest jsonObjectRequest5 = new JsonObjectRequest
                                                            (com.android.volley.Request.Method.GET, url_exchange, null, new Response.Listener<JSONObject>() {

                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    try {
                                                                        String getexchange = response.getString("exchange_cur");
                                                                        double getexchangedouble = Double.parseDouble(getexchange);
                                                                        double getPrice = Double.parseDouble(promo.getPrice());
                                                                        double newprice = new BigDecimal(getexchangedouble).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                                                        double exchangetopln = getPrice * newprice;
                                                                        double exchangetopln2 = new BigDecimal(exchangetopln).setScale(2, RoundingMode.HALF_UP).doubleValue();
                                                                        DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US));
                                                                        float twoDigitsF = Float.parseFloat(decimalFormat.format(exchangetopln2));
                                                                        String toshow = Float.toString(twoDigitsF);
                                                                        String exchangestring = "≈ "+ toshow + " zł";
                                                                        holder.mToPLN.setVisibility(View.VISIBLE);
                                                                        holder.mToPLN.setText(exchangestring);
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

                                                    Volley.newRequestQueue(context).add(jsonObjectRequest5);
                                                }
                                                else{

                                                    dontexchange = true;
                                                    holder.mToPLN.setVisibility(View.GONE);
                                                }


                                                //Sentry.captureMessage("from link");

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





                           String  status_check_stars = response.getString("status");
                            if (status_check_stars.equals("0")){
                                holder.mImgStarPromo.setVisibility(View.GONE);

                            }
                            else{
                                holder.mImgStarPromo.setVisibility(View.VISIBLE);
                                final String url = "https://yoururl.com/api/get_user_data.php?user_id="+user2.getId();

                                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest
                                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {

                                                    user_lvl = response.getString("user_level");
                                                    // Log.d("promo_earn_before", String.valueOf(promo_earn_before));
                                                    if (promo.getPriceType().equals("€")) {

                                                        if (user_lvl.equals("0")){
                                                            promo_before_calculate = promo_earn_before*0.01;
                                                            promo_earn_bonus = promo_before_calculate*10;
                                                        }
                                                        else if (user_lvl.equals("1")){
                                                            promo_before_calculate = promo_earn_before*0.10;
                                                            promo_earn_bonus = promo_before_calculate*10;
                                                        }
                                                        else if (user_lvl.equals("2")){
                                                            promo_before_calculate = promo_earn_before*0.20;
                                                            promo_earn_bonus = promo_before_calculate*10;
                                                        }
                                                        else if (user_lvl.equals("3")){
                                                            promo_before_calculate = promo_earn_before*0.30;
                                                            promo_earn_bonus = promo_before_calculate*10;
                                                        }
                                                        else if (user_lvl.equals("4")){
                                                            promo_before_calculate = promo_earn_before*0.40;
                                                            promo_earn_bonus = promo_before_calculate*10;
                                                        }
                                                        else if (user_lvl.equals("5")){
                                                            promo_before_calculate = promo_earn_before*0.50;
                                                            promo_earn_bonus = promo_before_calculate*10;
                                                        }
                                                        DecimalFormat precision = new DecimalFormat("0.00");
                                                        double promo_earn_bonus_decimal = promo_earn_bonus;
                                                        String calculated = precision.format(promo_earn_bonus_decimal);
                                                        holder.mHowMuchBonus.setText("+ "+calculated+" pkt");
                                                    }
                                                    else if (promo.getPriceType().equals("$")) {


                                                        if (user_lvl.equals("0")){
                                                            promo_before_calculate = promo_earn_before*0.01;
                                                            promo_earn_bonus = (promo_before_calculate*0.85)*10;
                                                        }
                                                        else if (user_lvl.equals("1")){
                                                            promo_before_calculate = promo_earn_before*0.10;
                                                            promo_earn_bonus = (promo_before_calculate*0.85)*10;
                                                        }
                                                        else if (user_lvl.equals("2")){
                                                            promo_before_calculate = promo_earn_before*0.20;
                                                            promo_earn_bonus = (promo_before_calculate*0.85)*10;
                                                        }
                                                        else if (user_lvl.equals("3")){
                                                            promo_before_calculate = promo_earn_before*0.30;
                                                            promo_earn_bonus = (promo_before_calculate*0.85)*10;
                                                        }
                                                        else if (user_lvl.equals("4")){
                                                            promo_before_calculate = promo_earn_before*0.40;
                                                            promo_earn_bonus = (promo_before_calculate*0.85)*10;
                                                        }
                                                        else if (user_lvl.equals("5")){
                                                            promo_before_calculate = promo_earn_before*0.50;
                                                            promo_earn_bonus = (promo_before_calculate*0.85)*10;
                                                        }
                                                        DecimalFormat precision = new DecimalFormat("0.00");
                                                        double promo_earn_bonus_decimal = promo_earn_bonus;
                                                        String calculated = precision.format(promo_earn_bonus_decimal);
                                                        holder.mHowMuchBonus.setText("+ "+calculated+" pkt");
                                                    }
                                                    else if (promo.getPriceType().equals("£")) {
                                                        if (user_lvl.equals("0")){
                                                            promo_before_calculate = promo_earn_before*0.01;
                                                            promo_earn_bonus = (promo_before_calculate*1.18)*10;
                                                        }
                                                        else if (user_lvl.equals("1")){
                                                            promo_before_calculate = promo_earn_before*0.10;
                                                            promo_earn_bonus = (promo_before_calculate*1.18)*10;
                                                        }
                                                        else if (user_lvl.equals("2")){
                                                            promo_before_calculate = promo_earn_before*0.20;
                                                            promo_earn_bonus = (promo_before_calculate*1.18)*10;
                                                        }
                                                        else if (user_lvl.equals("3")){
                                                            promo_before_calculate = promo_earn_before*0.30;
                                                            promo_earn_bonus = (promo_before_calculate*1.18)*10;
                                                        }
                                                        else if (user_lvl.equals("4")){
                                                            promo_before_calculate = promo_earn_before*0.40;
                                                            promo_earn_bonus = (promo_before_calculate*1.18)*10;
                                                        }
                                                        else if (user_lvl.equals("5")){
                                                            promo_before_calculate = promo_earn_before*0.50;
                                                            promo_earn_bonus = (promo_before_calculate*1.18)*10;
                                                        }
                                                        DecimalFormat precision = new DecimalFormat("0.00");
                                                        double promo_earn_bonus_decimal = promo_earn_bonus;
                                                        String calculated = precision.format(promo_earn_bonus_decimal);
                                                        holder.mHowMuchBonus.setText("+ "+calculated+" pkt");

                                                    }
                                                    else {
                                                        if (user_lvl.equals("0")){
                                                            promo_before_calculate = promo_earn_before*0.01;
                                                            promo_earn_bonus = (promo_before_calculate*0.22)*10;
                                                        }
                                                        else if (user_lvl.equals("1")){
                                                            promo_before_calculate = promo_earn_before*0.10;
                                                            promo_earn_bonus = (promo_before_calculate*0.22)*10;
                                                        }
                                                        else if (user_lvl.equals("2")){
                                                            promo_before_calculate = promo_earn_before*0.20;
                                                            promo_earn_bonus = (promo_before_calculate*0.22)*10;
                                                        }
                                                        else if (user_lvl.equals("3")){
                                                            promo_before_calculate = promo_earn_before*0.30;
                                                            promo_earn_bonus = (promo_before_calculate*0.22)*10;
                                                        }
                                                        else if (user_lvl.equals("4")){
                                                            promo_before_calculate = promo_earn_before*0.40;
                                                            promo_earn_bonus = (promo_before_calculate*0.22)*10;
                                                        }
                                                        else if (user_lvl.equals("5")){
                                                            promo_before_calculate = promo_earn_before*0.50;
                                                            promo_earn_bonus = (promo_before_calculate*0.22)*10;
                                                        }
                                                        DecimalFormat precision = new DecimalFormat("0.00");
                                                        double promo_earn_bonus_decimal = promo_earn_bonus;
                                                        String calculated = precision.format(promo_earn_bonus_decimal);
                                                        holder.mHowMuchBonus.setText("+ "+calculated+" pkt");

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
                                Volley.newRequestQueue(mContext).add(jsonObjectRequest2);


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
        Volley.newRequestQueue(context).add(jsonObjectRequestx);






    }

    @Override
    public int getItemCount() {
        return mPromos==null?0:mPromos.size();

    }


    public void setPromosData(List<Promo> promosData) {
        mPromos = promosData;
        notifyDataSetChanged();
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public List<Promo> getList() {
        return mPromos;
    }

    // Provide a reference to the views for each data item
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_poster)
        CircleImageView mImgPoster;
        @BindView(R.id.text_title) TextView mTextTitle;
        @BindView(R.id.releaseDate) TextView mTextReleaseDate;
        @BindView(R.id.text_rating_score) TextView mTextRatingScore;
        @BindView(R.id.text_Price) TextView mTextPrice;
        @BindView(R.id.text_PriceType) TextView mTextPriceType;
        //   @BindView(R.id.text_difficult) TextView mTextDifficult;
        @BindView(R.id.text_how_long) TextView mTextHowLong;
        @BindView(R.id.text_short_desc) TextView mTextShortDesc;
      //  @BindView(R.id.rl_bg) ImageView rl_bg;
        @BindView(R.id.text_reviews) TextView mTextReviews;
        @BindView(R.id.text_comments) TextView mTextComments;
        @BindView(R.id.text_likes) TextView mTextLikes;
        @BindView(R.id.textPlusXp) TextView mTextPlusXp;
        @BindView(R.id.which_promo) TextView mWhichPromo;
        @BindView(R.id.how_much_bonus_points) TextView mHowMuchBonus;

        @BindView(R.id.all_promo) TextView mAllPromo;
        @BindView(R.id.text_Price_to_pln) TextView mToPLN;
        @BindView(R.id.item_star) RelativeLayout mImgStarPromo;
        @BindView(R.id.reviews_count_rl)
        LinearLayout reviews_count_rl;
        private Toast mToast;
        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            mImgPoster.setBorderColor(getColor(mContext, R.color.black));
            mImgPoster.setBorderWidth(3);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnClickListener.onPromoClick(mPromos.get(getAdapterPosition()));
        }
    }

    public interface PromoClickListener {
        void onPromoClick(Promo promo);
    }
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mPromos.addAll(mPromos);
            notifyDataSetChanged();
        }
    };
}
