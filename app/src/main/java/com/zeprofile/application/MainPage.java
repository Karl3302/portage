/*
 * Created by Hang on 18-4-12 下午5:45
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午5:45
 */

package com.zeprofile.application;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.zeprofile.application.base.UserHobbyBean;
import com.zeprofile.application.base.UserInfoBean;
import com.zeprofile.application.fragment.FragmentDiscount;
import com.zeprofile.application.fragment.FragmentProfile;
import com.zeprofile.application.utils.ApiZeprofile;
import com.zeprofile.application.utils.RetrofitBuilder;
import com.zeprofile.application.utils.ZeProfileUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainPage extends AppCompatActivity {
    // Components
    private GestureDetector mGestureDetector;
    private Toolbar mMainPageToolbar;
    private ActionBar mMainPageActionBar;
    private BottomNavigationView mMainPageBottomNavigationView;
    private FrameLayout mMainPageFrameLayout;

    // ViewPager+Fragments
    // private CustomViewPager mMainMenuViewPager;
    // private ViewPager mProfileRootViewPager;
    // private List<Fragment> mListFragment;
    // private Integer idFragmentCenter; // The fragment that is actually in center

    // Side Navigation
    // private DrawerLayout mDrawerLayout;
    // private NavigationView mSideNavigationView;
    // private View mSideNavigationHeaderView;
    // private TextView mSideNavigationUserNameTextView;

    // Constants
    private static final int FLING_MIN_DISTANCE = 50;
    private static final int FLING_MIN_VELOCITY = 0;
    private static final String TOUCH_INTENT_CLICK = "click";
    private static final String TOUCH_INTENT_SCROLL_HORIZONTAL = "scroll_horizontal";
    private static final String TOUCH_INTENT_SCROLL_VERTICAL = "scroll_vertical";
    private static final String TOUCH_INTENT_SWIPE_LEFT = "swipe_left";
    private static final String TOUCH_INTENT_SWIPE_RIGHT = "swipe_right";

    // Variables
    //private String email,token;
    private int mScreenWidth;
    private int mScreenHeight;
    private float mDownX;
    private float mDownY;
    private String mTouchIntent;
    FragmentManager.OnBackStackChangedListener mListener;

    //TODO 手机一翻转就回到profile界面而且标题也消失了
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
//        View decorView = getWindow().getDecorView();
//        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//
//        mListener=new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//
//            }
//        };
//        getFragmentManager().addOnBackStackChangedListener(mListener);
//
//        Log.d("--- LoadFragment ---","number of frags="+getFragmentManager().getFragments().size()+"fragment in container="+getFragmentManager().findFragmentById(R.id.mainPageFrameLayout));
        initViews();
        initData();
        configViews(savedInstanceState);
    }

    public void initViews() {
        // Initialization of the screen
        getScreenWidthAndHeight();

        // Initialization of the Toolbar
        mMainPageToolbar = (Toolbar) findViewById(R.id.mainPageToolbar);
        setSupportActionBar(mMainPageToolbar);
        mMainPageActionBar = (ActionBar) getSupportActionBar();
        mMainPageActionBar.setTitle(null);

        // Initialization of the BottomNavigationView
        mMainPageBottomNavigationView = (BottomNavigationView) findViewById(R.id.mainPageBottomNavigationView);

        // Initialization of the Framelayout(content view)
        mMainPageFrameLayout = findViewById(R.id.mainPageFrameLayout);

        // Gesture
//        mGestureDetector = new GestureDetector(this);
//        mMainPageFrameLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View arg0, MotionEvent event) {
//                return mGestureDetector.onTouchEvent(event);
//            }
//        });
//        mGestureDetector = new GestureDetector(this);
//        mMainPageFrameLayout.setOnTouchListener(this);
//        mMainPageFrameLayout.setLongClickable(true);
//        mGestureDetector = new GestureDetector(new GestureDetector.OnGestureListener() {
//                @Override
//                public boolean onDown(MotionEvent e) {
//                    return false;
//                }
//                @Override
//                public void onShowPress(MotionEvent e) {
//                }
//                @Override
//                public boolean onSingleTapUp(MotionEvent e) {
//                    return false;
//                }
//                @Override
//                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                    return false;
//                }
//                @Override
//                public void onLongPress(MotionEvent e) {
//                }
//                @Override
//                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                //在滑动的方法里，进行手势滑动事件的判断
//                //e1代表手指按在屏幕上的X轴坐标点，e2代表手指离开屏幕上的X轴坐标点。
//                if (e1.getX() - e2.getX() < 0 && Math.abs((int) (e1.getX() - e2.getX())) > 30) {
//                    //向右滑动的判断，如果手指从左向右滑动，就走这个方法
//                    moveRight();
//                    return true;
//                } else if (e1.getX() - e2.getX() > 0 && Math.abs((int) (e1.getX() - e2.getX())) > 30) {
//                    //向左滑动的判断，如果手指从右向左滑动，就走这个方法
//                    moveLeft();
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

//        //side navigation
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_menu_drawer_layout);
//        mSideNavigationView = (NavigationView) findViewById(R.id.side_navigation_view);
//        mSideNavigationHeaderView = this.getLayoutInflater().inflate(R.layout.side_navigation_header, mSideNavigationView);
//        mSideNavigationUserNameTextView = (TextView) mSideNavigationHeaderView.findViewById(R.id.sideNavigationUserNameTextView);
//        // Initialization of the ViewPager
//        mMainMenuViewPager = (CustomViewPager) findViewById(R.id.mainMenuViewPager);
//        mListFragment = new ArrayList<>();
//        mListFragment.add(new FragmentProfileRoot());
//        mListFragment.add(new FragmentDiscount());
    }

    public void initData() {
        // --- Get the userEmail transferred by the last activity ---
        //email = "test@zeprofile.fr";//ZeProfileUtils.getStringFromLastActivity(this, "emailAddress");
        //token = ZeProfileUtils.getStringFromLastActivity(this, "token");
        //Log.d("--- MainPage ---", "[Data Transfer] Token = "+token);
//        Retrofit retrofit = RetrofitBuilder.getRetrofit();
//        ApiZeprofile netService = retrofit.create(ApiZeprofile.class);
//        Call<UserInfoBean> call = netService.getUserInfo("Bearer "+token);
//        call.enqueue(new Callback<UserInfoBean>() {
//            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
//                if (response.isSuccessful()) {
//                    Log.d("--- MainPage ---", "[Network_getUserInfo] status code = " + response.code() + "\n raw = " + response.raw() +"\n email="+response.body().getEmail());
//                } else {
//                    Log.d("--- MainPage ---", "[Network_getUserInfo] status code = " + response.code() + "\n message = " + response.message() + "\n raw = " + response.raw());
//                }
//            }
//            public void onFailure(Call<UserInfoBean> call, Throwable t) {
//            }
//        });
    }

    public void configViews(Bundle savedInstanceState) {
        //View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE);

        // ---- Configure the Toolbar ----
        mMainPageActionBar.setDisplayShowHomeEnabled(true);
        mMainPageActionBar.setDisplayHomeAsUpEnabled(false);

        // ---- Loading the Root Fragment ----
        if (savedInstanceState == null) { // Load the root fragment after the login activity
            Log.d("--- LoadFragment ---", "[MainPage] Loading the root fragment");
            ZeProfileUtils.loadMainFrame(MainPage.this, FragmentDiscount.class.getSimpleName());
        } else { // Reload the fragment after the SCREEN ORIENTATION has changed (for the title and back button, the view is maintained by fragment itself)
            //TODO 内存重启时调用的逻辑没写
            Log.d("--- LoadFragment ---", "[MainPage] Reload the fragment");
            ZeProfileUtils.loadMainFrame(MainPage.this,ZeProfileUtils.getCurrentFragmentName());
        }

        // --- Configure the BottomNavigationView ---
        mMainPageBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.rightTabBotNav: // Right Button : Profile
                        ZeProfileUtils.loadMainFrame(MainPage.this, FragmentProfile.class.getSimpleName());
                        return true;
                    case R.id.leftTabBotNav: // Left Button : Discount
                        ZeProfileUtils.loadMainFrame(MainPage.this, FragmentDiscount.class.getSimpleName());
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });
//        // --- Configure the Side Navigation Bar ---
//        if (DatabaseHelper.isValidEmail(email)) mSideNavigationUserNameTextView.setText(email);
//        mMainPageActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
//        mMainPageActionBar.setDisplayHomeAsUpEnabled(true);
//        // Setup click event to side navigation view
//        onNavigationViewMenuItemSelected(mSideNavigationView);
//        // --- Configure the ViewPager for loading fragmrnts ---
//        // Setup the fragment adapter
//        FragmentAdapter mFragmentAdapter = new FragmentAdapter(this.getFragmentManager(), this, mListFragment);
//        mMainMenuViewPager.setAdapter(mFragmentAdapter);
//        // Set the animation time
//        mMainMenuViewPager.setScrollDurationFactor(4);
//        // Setup the cross fade animation
//        mMainMenuViewPager.setPageTransformer(false, new CustomViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View view, float position) {
//                int pageWidth = view.getWidth();
//                int pageHeight = view.getHeight();
//                if (Math.abs(position) >= 1.0F) {
//                    view.setTranslationX(pageWidth * position);
//                    view.setTranslationY(pageHeight * 0.05F);
//                    view.setAlpha(0.1F);
//                } else if (position == 0.0F) {
//                    view.setTranslationX(pageWidth * position);
//                    view.setAlpha(1.0F);
//                    idFragmentCenter = view.getId();
//                } else {
//                    // position is between (-1.0F, 0.0F) || (0.0F, 1.0F)
//                    view.setTranslationX(pageWidth * -position);
//                    if ((view.getId() != idFragmentCenter)) {
//                        view.setTranslationY(0.03F * pageHeight * Math.abs(position));
//                    }
//                    view.setAlpha(1.0F - (1.9F * Math.abs(position)));
//                }
//            }
//        });
//        // --- Configure the Page Scroll listener ---
//        mMainMenuViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//            @Override
//            public void onPageSelected(int position) {
//                // 当滑动到某一位置,导航栏对应位置被按下
//                mMainPageBottomNavigationView.getMenu().getItem(position).setChecked(true);
//                // 这里使用mBottomNavigationView.setSelectedItemId(position);无效,
//                // setSelectedItemId(position)的官网原句:Set the selected
//                // menu item ID. This behaves the same as tapping on an item
//                // 未找到原因
//            }
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // if (item.getItemId() == android.R.id.home) this.finish();
        // if (item.getItemId() == android.R.id.home) mDrawerLayout.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    //TODO 根据加速度判断/滑动时结合动画
    // Handle the TouchEvent depending on user's gesture
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Save the original position
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchIntent = TOUCH_INTENT_CLICK;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_MOVE:
                // Calculate the difference between the current position and the original position
                float x = ev.getX();
                float y = ev.getY();
                float xDelta = Math.abs(x - mDownX);
                float yDelta = Math.abs(y - mDownY);
                // Condition of scroll horizontal intention : distance > (1/10 screen size)
                if (xDelta > (mScreenWidth / 10)) {
                    mTouchIntent = TOUCH_INTENT_SCROLL_HORIZONTAL;
                    // Condition of swipe horizontal intention : (distance x) > (1/3 screen size) and (1/2 distance x) > (distance y)
                    if ((xDelta > (mScreenWidth / 3)) && ((xDelta / 2) > yDelta)) {
                        if ((x - mDownX) < 0) mTouchIntent = TOUCH_INTENT_SWIPE_LEFT;
                        else mTouchIntent = TOUCH_INTENT_SWIPE_RIGHT;
                    }
                    /*
                     * Send a "fake cancel event" to stop the other component's reaction of the "click event"
                     * - Because we consider the event "click down" as a "click event",
                     *   other component could be reacting already, after identifying the intention is not "click",
                     *   we need to send this "fake event" to cancel the reaction.
                     */
                    return super.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, mDownX, mDownY, 0));
                } else if (yDelta > (mScreenHeight / 10)) {
                    mTouchIntent = TOUCH_INTENT_SCROLL_VERTICAL;
                }
                break;
            case MotionEvent.ACTION_UP:
                switch (mTouchIntent) {
                    case TOUCH_INTENT_SWIPE_LEFT:
                        // if currentFragment is mainFragment → switch to FragmentProfile (else do nothing)
                        if(ZeProfileUtils.getSuperClass(ZeProfileUtils.getCurrentFragmentName())==null) {
                            mMainPageBottomNavigationView.getMenu().getItem(1).setChecked(true);
                            ZeProfileUtils.loadMainFrame(MainPage.this, FragmentProfile.class.getSimpleName());
                        }
                        break;
                    case TOUCH_INTENT_SWIPE_RIGHT:
                        String currentFragmentName = ZeProfileUtils.getCurrentFragmentName();
                        // if currentFragment is subFragment → switch back to its superclass
                        if(ZeProfileUtils.getSuperClass(currentFragmentName)!=null){
                            String superClassName = ZeProfileUtils.getSuperClass(currentFragmentName);
                            if(superClassName.equals(FragmentProfile.class.getSimpleName())) {
                                mMainPageBottomNavigationView.getMenu().getItem(1).setChecked(true);
                            }else {
                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
                            }
                            ZeProfileUtils.loadMainFrame(MainPage.this, superClassName);

                        }else {//if currentFragment is mainFragment → switch to FragmentDiscount
                            mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
                            ZeProfileUtils.loadMainFrame(MainPage.this, FragmentDiscount.class.getSimpleName());

                        }
                        break;
                    default:
                        break;
                }
                break;
        }
        //Log.d("---TouchEvent---", "Action=" + ev.getAction() + " TouchIntent=" + mTouchIntent);
        if (mTouchIntent.equals(TOUCH_INTENT_CLICK) || mTouchIntent.equals(TOUCH_INTENT_SCROLL_VERTICAL))
            return super.dispatchTouchEvent(ev);
        else return true;
    }

    private void getScreenWidthAndHeight() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
    }

    // Clear the currentFragmentName in Utils and clear the fragmentManager
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //getFragmentManager().removeOnBackStackChangedListener(mListener);
        //ZeProfileUtils.clearCurrentFragmentName();
    }
//    @Override
//    public void onBackPressed() {
//        Log.d("--- LoadFragment ---","MainPage.onBackPressed()");
//        if (getFragmentManager().getBackStackEntryCount() > 0) {
//            getFragmentManager().popBackStack();
//        } else {
//            super.onBackPressed();
//        }
//    }

    //   // ---  Side Navigation View ---
//   private void setupDrawerContent(NavigationView navigationView) {
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                ZeProfileUtils.shortCenterToast(MainPage.this, "touched");
//                item.setChecked(true);
//                mDrawerLayout.closeDrawers();
//                return true;
//            }
//        });
//    }
//    private void onNavigationViewMenuItemSelected(NavigationView mNav) {
//        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(MenuItem item) {
//                // Handle navigation view item clicks here.
//                final int id = item.getItemId();
//                mProfileRootViewPager = findViewById(R.id.profileRootViewPager);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        switch (id) {
//                            case R.id.side_navigation_mon_profile_tab:
//                                mMainMenuViewPager.setCurrentItem(0);
//                                mProfileRootViewPager.setCurrentItem(0);
//                                mMainPageActionBar.setTitle(R.string.title_profile);
//                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
//                                break;
//                            case R.id.side_navigation_mes_offres_tab:
//                                mMainMenuViewPager.setCurrentItem(1);
//                                mMainPageActionBar.setTitle(R.string.title_discount);
//                                mMainPageBottomNavigationView.getMenu().getItem(1).setChecked(true);
//                                break;
//                            case R.id.side_navigation_public_profile_sub_tab:
//                                mMainMenuViewPager.setCurrentItem(0);
//                                mProfileRootViewPager.setCurrentItem(1);
//                                mMainPageActionBar.setTitle(R.string.title_manage_public_profile);
//                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
//                                break;
//                            case R.id.side_navigation_visibility_sub_tab:
//                                mMainMenuViewPager.setCurrentItem(0);
//                                mProfileRootViewPager.setCurrentItem(2);
//                                mMainPageActionBar.setTitle(R.string.title_manage_visibility);
//                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
//                                break;
//                            case R.id.side_navigation_bank_account_sub_tab:
//                                mMainMenuViewPager.setCurrentItem(0);
//                                mProfileRootViewPager.setCurrentItem(3);
//                                mMainPageActionBar.setTitle(R.string.title_manage_bank_account);
//                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
//                                break;
//                            case R.id.side_navigation_settings_sub_tab:
//                                mMainMenuViewPager.setCurrentItem(0);
//                                mProfileRootViewPager.setCurrentItem(4);
//                                mMainPageActionBar.setTitle(R.string.title_manage_settings);
//                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
//                                break;
//                            case R.id.side_navigation_about_sub_tab:
//                                mMainMenuViewPager.setCurrentItem(0);
//                                mProfileRootViewPager.setCurrentItem(5);
//                                mMainPageActionBar.setTitle(R.string.title_manage_about);
//                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
//                                break;
//                            default:
//                                break;
//                        }
//                    }
//                }, 100);
//                DrawerLayout drawer = findViewById(R.id.main_menu_drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);// “right” -- end  "left" -- start
//                return true;
//            }
//        });
//    }
//
//    // --- 第一套触摸方案(弃用) ---
//    //覆写Activity的onTouchEvent方法
//    //将Touch事件交给GestureDetector处理
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return mGestureDetector.onTouchEvent(event);
//    }
//    //以下为OnGestureListener的实现
//    private class GestureListenerImpl implements GestureDetector.OnGestureListener {
//        //触摸屏幕时均会调用该方法
//        @Override
//        public boolean onDown(MotionEvent e) {
//            System.out.println("---> 手势中的 onDown 方法");
//            return false;
//        }
//        //手指在屏幕上拖动时会调用该方法
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
//            System.out.println("---> 手势中的 onFling 方法");
//            System.out.println("e1.getAction()="+e1.getAction()+",e2.getAction()="+e2.getAction());
//            System.out.println("velocityX="+velocityX+",velocityY="+velocityY);
//            return false;
//        }
//        //长按触摸屏幕时均会调用该方法
//        @Override
//        public void onLongPress(MotionEvent e) {
//            System.out.println("---> 手势中的 onLongPress 方法");
//        }
//        //手指在屏幕上滚动时会调用该方法
//        @Override
//        public boolean onScroll(MotionEvent e1, MotionEvent e2,float distanceX, float distanceY) {
//            System.out.println("---> 手势中的 onScroll 方法");
//            System.out.println("e1.getAction()="+e1.getAction()+",e2.getAction()="+e2.getAction());
//            System.out.println("distanceX="+distanceX+",distanceY="+distanceY);
//            return false;
//        }
//        //在触摸屏上按下,且未移动和松开时调用该方法
//        @Override
//        public void onShowPress(MotionEvent e) {
//            System.out.println("---> 手势中的 onShowPress 方法");
//        }
//        //轻击屏幕时调用该方法
//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            System.out.println("---> 手势中的 onSingleTapUp 方法");
//            return false;
//        }
//    }
//
//    // --- 第二套触摸方案(弃用) ---
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        Log.d("touch", "touch");
//        return mGestureDetector.onTouchEvent(event);
//    }
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return mGestureDetector.onTouchEvent(event);
//    }
//    @Override
//    public boolean onDown(MotionEvent e) {
//        return false;
//    }
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
//                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { // Fling left
//            ZeProfileUtils.shortCenterToast(this, "Slide Left");
//            mMainPageBottomNavigationView.getMenu().getItem(1).setChecked(true);
//            ZeProfileUtils.loadMainFrame(MainPage.this, new FragmentDiscount());
//        } else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
//                && Math.abs(velocityX) > FLING_MIN_VELOCITY) { // Fling right
//            ZeProfileUtils.shortCenterToast(this, "Slide Right");
//            mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
//            ZeProfileUtils.loadMainFrame(MainPage.this, new FragmentProfile());
//        }
//        return false;
//    }
//    @Override
//    public void onLongPress(MotionEvent e) {
//    }
//    @Overide
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        return false;
//    }
//    @Override
//    public void onShowPress(MotionEvent e) {
//    }
//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        return false;
//    }
//
//    //首先有一个触摸监听事件
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        //触摸监听事件被拦截，然后调用自定义的手势事件
//        return mGestureDetector.onTouchEvent(event);
//    }
}
