package com.profitz.app.promos.adapters;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ECPagerCardContentListPro extends ListView {

    private boolean scrollDisabled;
    private int mPosition;

    private ECCardContentListItemAdapterPro contentListItemAdapter;

    private ECPagerCardHead headView;

    public ECPagerCardContentListPro(Context context) {
        super(context);
        init(context);
    }

    public ECPagerCardContentListPro(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ECPagerCardContentListPro(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        headView = new ECPagerCardHead(context);
    //    headView.setBackgroundColor(Color.RED);
        headView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addHeaderView(headView);
 //     this.setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public ECCardContentListItemAdapterPro getContentListItemAdapter() {
        return contentListItemAdapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof ECCardContentListItemAdapterPro) {
            this.contentListItemAdapter = (ECCardContentListItemAdapterPro) adapter;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;

        // Save the event initial position
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
            return super.dispatchTouchEvent(ev);
        }
        // Check if we are still in the same position, otherwise cancel event
        int eventPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
        if (actionMasked == MotionEvent.ACTION_UP) {
            if (eventPosition != mPosition) {
                ev.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.dispatchTouchEvent(ev);
    }



    public void scrollToTop() {
        this.smoothScrollToPosition(0);
    }

    ECPagerCardHead getHeadView() {
        return headView;
    }

    void animateWidth(int targetWidth, int duration, int delay) {
        // reset own width for smooth animation and avoid values like 'MATCH_PARENT'
        this.getLayoutParams().width = this.getWidth();

        ValueAnimator widthAnimation = new ValueAnimator();
        widthAnimation.setInterpolator(new DecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams pagerLayoutParams = getLayoutParams();
                pagerLayoutParams.width = (int) animation.getAnimatedValue();

                setLayoutParams(pagerLayoutParams);
            }
        });
        widthAnimation.setIntValues(getWidth(), targetWidth);
        widthAnimation.setStartDelay(delay);
        widthAnimation.setDuration(duration);
        widthAnimation.start();
    }

    final void hideListElements() {
        getContentListItemAdapter().enableZeroItemsModePro();
    }

    final void showListElements() {
        getContentListItemAdapter().disableZeroItemsModePro();
    }

}