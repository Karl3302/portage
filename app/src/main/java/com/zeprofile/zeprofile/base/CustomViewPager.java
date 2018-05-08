package com.zeprofile.zeprofile.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;

import java.lang.reflect.Field;

/**
 * 不可以滑动，但是可以setCurrentItem的ViewPager。
 */
public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context) {
        super(context);
        setCustomDuration();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomDuration();
    }

    /**
     * Set custom animation duration :
     * CustomViewPager mCustomViewPager = (CustomViewPager) findViewById(R.id.myPager);
     * mCustomViewPager.setScrollDurationFactor(2); // make the animation twice as slow
     */
    private CustomDurationScroller mScroller = null;

    /**
     * Override the Scroller instance with our own class so we can change the
     * duration
     */
    private void setCustomDuration() {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new CustomDurationScroller(getContext(),
                    (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception e) {
        }
    }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    /**
     * Disable the touch event
     */
    /*
        @Override
        public boolean onTouchEvent(MotionEvent arg0) {
            return false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent arg0) {
            return false;
        }
    */

}
