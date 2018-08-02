package com.zeprofile.application.test;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.zeprofile.application.MainPage;
import com.zeprofile.application.fragment.FragmentProfile;
import com.zeprofile.application.utils.ZeProfileUtils;

public class CustomFrameLayout extends FrameLayout implements  OnGestureListener{
    private Context mContext;
    private int mCurrentView;
    private int ANIM_DURATION=3000;
    private View mChild, mHistoryView;
    private View children[];
    private int mWidth;
    private int mPreviousMove;
    private GestureDetector mGestureDetector;
    private TranslateAnimation inLeft;
    private TranslateAnimation outLeft;
    private TranslateAnimation inRight;
    private TranslateAnimation outRight;
    private AlphaAnimation alpha1;
    private AlphaAnimation alpha2;
    private static final int NONE  = 1;
    private static final int LEFT  = 2;
    private static final int RIGHT = 3;

    public CustomFrameLayout(Context context){
        super(context);
        mContext = context;
        init();
    }
    public CustomFrameLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        mContext = context;
        init();

    }
    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }
    public void init(){
        mCurrentView=0;
        mGestureDetector=new GestureDetector(mContext, this);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int window_width = wm.getDefaultDisplay().getWidth();
        mWidth = wm.getDefaultDisplay().getWidth();
        inLeft   = new TranslateAnimation(-mWidth,0,0,0);
        outLeft  = new TranslateAnimation(0,-mWidth,0,0);
        alpha1=new AlphaAnimation(1,0);
        alpha2=new AlphaAnimation(0,1);
        inLeft.setDuration(ANIM_DURATION);
        outLeft.setDuration(ANIM_DURATION);
        alpha1.setDuration(3000);
        alpha2.setDuration(5000);
    }
    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {


        int verticalMinDistance =0;
        int minVelocity= 0;
        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
            // 切换Activity
            //Intent intent = new Intent(MainActivity.this, CardListActivity.class);
            //startActivity(intent);
            //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            //Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
            moveLeft();
            return true;
        } else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {

            // 切换Activity
            //Intent intent = new Intent(MainActivity.this, CreateActivity.class);
            //startActivity(intent);
            // mContext.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            //Toast.makeText(mContext, "向右手势", Toast.LENGTH_SHORT).show();
            moveRight();
            return true;
        }else{

        }
        return false;
    }
    protected void onFinishInflate() {
        int count = getChildCount();
        children = new View[count];
        for (int i = 0; i < count; ++i) {
            children[i] = getChildAt(i);
            if (i != mCurrentView) {
                children[i].setVisibility(View.GONE);
            }
        }
    }
    public void moveLeft() {
        if(mCurrentView==0){
            children[mCurrentView].startAnimation(outLeft);
            children[mCurrentView+1].setVisibility(View.VISIBLE);
            children[mCurrentView+1].startAnimation(alpha2);
            children[mCurrentView].setVisibility(View.GONE);
            mCurrentView=1;
        }
           /* if (mCurrentView < children.length - 1 && mPreviousMove != LEFT) {
                children[mCurrentView+1].setVisibility(View.VISIBLE);
                children[mCurrentView+1].startAnimation(inLeft);
                children[mCurrentView].startAnimation(outLeft);
                children[mCurrentView].setVisibility(View.GONE);
                mCurrentView++;
                mPreviousMove = LEFT;
            }**/
    }
    public void moveRight() {
        System.out.println("向右滑动");
        if(mCurrentView==1){
            //children[mCurrentView-1].setVisibility(View.VISIBLE);
            children[mCurrentView-1].startAnimation(inLeft);
            children[mCurrentView].setVisibility(View.GONE);
            children[mCurrentView].startAnimation(alpha1);
            children[mCurrentView-1].setVisibility(View.VISIBLE);
            mCurrentView=0;
        }
           /* if (mCurrentView > 0 && mPreviousMove != RIGHT) {
                children[mCurrentView-1].setVisibility(View.VISIBLE);
                children[mCurrentView-1].startAnimation(inRight);
                children[mCurrentView].startAnimation(outRight);
                children[mCurrentView].setVisibility(View.GONE);
                mCurrentView--;
                mPreviousMove = RIGHT;
            }**/
    }
    @Override
    public void onLongPress(MotionEvent e) {


    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {

        return false;
    }
    @Override
    public void onShowPress(MotionEvent e) {


    }
    @Override
    public boolean onSingleTapUp(MotionEvent e) {

        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

}