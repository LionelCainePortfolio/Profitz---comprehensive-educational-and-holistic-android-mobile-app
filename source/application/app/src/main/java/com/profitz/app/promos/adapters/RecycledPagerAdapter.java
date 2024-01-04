package com.profitz.app.promos.adapters;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.profitz.app.R;import java.util.LinkedList;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class RecycledPagerAdapter<VH extends RecycledPagerAdapter.ViewHolder> extends PagerAdapter {
    @BindView(R.id.img_poster)
    CircleImageView mImgPoster;
    @BindView(R.id.text_title)
    TextView mTextTitle;
    @BindView(R.id.releaseDate) TextView mTextReleaseDate;
    @BindView(R.id.text_rating_score) TextView mTextRatingScore;
    @BindView(R.id.text_Price) TextView mTextPrice;
    @BindView(R.id.text_PriceType) TextView mTextPriceType;
    //   @BindView(R.id.text_difficult) TextView mTextDifficult;
    @BindView(R.id.text_how_long) TextView mTextHowLong;
    @BindView(R.id.text_short_desc) TextView mTextShortDesc;
    @BindView(R.id.rl_bg)
    ImageView rl_bg;
    @BindView(R.id.text_reviews) TextView mTextReviews;
    @BindView(R.id.text_comments) TextView mTextComments;
    @BindView(R.id.text_likes) TextView mTextLikes;
    @BindView(R.id.textPlusXp) TextView mTextPlusXp;
    @BindView(R.id.which_promo) TextView mWhichPromo;
    @BindView(R.id.all_promo) TextView mAllPromo;
    private Toast mToast;

    public abstract int getItemCount();

    public static class ViewHolder {
        final View itemView;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);

            this.itemView = itemView;
        }
    }

    Queue<VH> destroyedItems = new LinkedList<>();

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        VH viewHolder = destroyedItems.poll();

        viewHolder = (VH) onCreateViewHolder(container,getItemViewType(position));
        onBindViewHolder(viewHolder, position);
        container.addView(viewHolder.itemView);

        return viewHolder;
    }

    @Override
    public final void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((VH) object).itemView);
        destroyedItems.add((VH) object);
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        return ((VH) object).itemView == view;
    }

    /**
     * Create a new view holder
     * @param parent
     * @return view holder
     */
    public abstract ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);


    /**
     * Bind data at position into viewHolder
     * @param viewHolder
     * @param position
     */
    public abstract void onBindViewHolder(VH viewHolder, int position);


    /**
     * get Item Type
     * @param position
     */
    public abstract int getItemViewType(int position);


}
