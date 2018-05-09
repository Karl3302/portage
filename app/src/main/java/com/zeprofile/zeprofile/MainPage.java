/*
 * Created by Hang on 18-4-12 下午5:45
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午5:45
 */

package com.zeprofile.zeprofile;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.zeprofile.zeprofile.fragment.FragmentDiscount;
import com.zeprofile.zeprofile.fragment.FragmentProfile;
import com.zeprofile.zeprofile.utils.ZeProfileUtils;

public class MainPage extends AppCompatActivity {
    // Components
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

    // Variables
    private String email;

    //TODO 手机一翻转就回到profile界面
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

        //  View decorView = getWindow().getDecorView();
        //  decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initViews();
        initData();
        configViews();
    }

    public void initViews() {
        // Initialization of the Toolbar
        mMainPageToolbar = (Toolbar) findViewById(R.id.mainPageToolbar);
        setSupportActionBar(mMainPageToolbar);
        mMainPageActionBar = (ActionBar) getSupportActionBar();
        mMainPageActionBar.setTitle(null);

        // Initialization of the BottomNavigationView
        mMainPageBottomNavigationView = (BottomNavigationView) findViewById(R.id.mainPageBottomNavigationView);

        // Initialization of the Framelayout(content view)
        mMainPageFrameLayout = findViewById(R.id.mainPageFrameLayout);


        //side navigation
        /*mDrawerLayout = (DrawerLayout) findViewById(R.id.main_menu_drawer_layout);
        mSideNavigationView = (NavigationView) findViewById(R.id.side_navigation_view);
        mSideNavigationHeaderView = this.getLayoutInflater().inflate(R.layout.side_navigation_header, mSideNavigationView);
        mSideNavigationUserNameTextView = (TextView) mSideNavigationHeaderView.findViewById(R.id.sideNavigationUserNameTextView);*/
        // Initialization of the ViewPager
        /*mMainMenuViewPager = (CustomViewPager) findViewById(R.id.mainMenuViewPager);
        mListFragment = new ArrayList<>();
        mListFragment.add(new FragmentProfileRoot());
        mListFragment.add(new FragmentDiscount());*/
    }

    public void initData() {
        // Get the userEmail transferred by the last activity
        email = ZeProfileUtils.getStringFromLastActivity(this, "emailAddress");
    }

    public void configViews() {
        //View decorView = getWindow().getDecorView();
        //decorView.setSystemUiVisibility(SYSTEM_UI_FLAG_LAYOUT_STABLE);

        // ---- Configure the Toolbar ----
        mMainPageActionBar.setDisplayShowHomeEnabled(true);
        mMainPageActionBar.setDisplayHomeAsUpEnabled(false);

        // ---- Loading the Root Fragment ----
        ZeProfileUtils.loadMainFrame(MainPage.this,new FragmentProfile());

        // ---- Configure the Side Navigation Bar ----
        /* if (DatabaseHelper.isValidEmail(email)) mSideNavigationUserNameTextView.setText(email);
         mMainPageActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
         mMainPageActionBar.setDisplayHomeAsUpEnabled(true);
        // Setup click event to side navigation view
        onNavigationViewMenuItemSelected(mSideNavigationView);*/

        // ---- Configure the ViewPager ----
        // Setup the fragment adapter
        /* FragmentAdapter mFragmentAdapter = new FragmentAdapter(this.getFragmentManager(), this, mListFragment);
        mMainMenuViewPager.setAdapter(mFragmentAdapter);
        // Set the animation time
        mMainMenuViewPager.setScrollDurationFactor(4);
        // Setup the cross fade animation
        mMainMenuViewPager.setPageTransformer(false, new CustomViewPager.PageTransformer() {
            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();
                int pageHeight = view.getHeight();
                if (Math.abs(position) >= 1.0F) {
                    view.setTranslationX(pageWidth * position);
                    view.setTranslationY(pageHeight * 0.05F);
                    view.setAlpha(0.1F);
                } else if (position == 0.0F) {
                    view.setTranslationX(pageWidth * position);
                    view.setAlpha(1.0F);
                    idFragmentCenter = view.getId();
                } else {
                    // position is between (-1.0F, 0.0F) || (0.0F, 1.0F)
                    view.setTranslationX(pageWidth * -position);
                    if ((view.getId() != idFragmentCenter)) {
                        view.setTranslationY(0.03F * pageHeight * Math.abs(position));
                    }
                    view.setAlpha(1.0F - (1.9F * Math.abs(position)));
                }
            }
        });*/

        // Configure the BottomNavigationView
        mMainPageBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.leftTabBotNav: // Left Button : Mon Profil
                        ZeProfileUtils.loadMainFrame(MainPage.this, new FragmentProfile());
                        return true;
                    case R.id.rightTabBotNav: // Right Button : Mes Offres
                        ZeProfileUtils.loadMainFrame(MainPage.this, new FragmentDiscount());
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        // Configure the Page Scroll listener
        /*mMainMenuViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                // 当滑动到某一位置,导航栏对应位置被按下
                mMainPageBottomNavigationView.getMenu().getItem(position).setChecked(true);
                // 这里使用mBottomNavigationView.setSelectedItemId(position);无效,
                // setSelectedItemId(position)的官网原句:Set the selected
                // menu item ID. This behaves the same as tapping on an item
                // 未找到原因
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // if (item.getItemId() == android.R.id.home) this.finish();
        // if (item.getItemId() == android.R.id.home) mDrawerLayout.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    /*private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ZeProfileUtils.shortCenterToast(MainPage.this, "touched");
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }
    private void onNavigationViewMenuItemSelected(NavigationView mNav) {
        mNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation view item clicks here.
                final int id = item.getItemId();
                mProfileRootViewPager = findViewById(R.id.profileRootViewPager);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (id) {
                            case R.id.side_navigation_mon_profile_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(0);
                                mMainPageActionBar.setTitle(R.string.title_profile);
                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_mes_offres_tab:
                                mMainMenuViewPager.setCurrentItem(1);
                                mMainPageActionBar.setTitle(R.string.title_discount);
                                mMainPageBottomNavigationView.getMenu().getItem(1).setChecked(true);
                                break;
                            case R.id.side_navigation_public_profile_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(1);
                                mMainPageActionBar.setTitle(R.string.title_manage_public_profile);
                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_visibility_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(2);
                                mMainPageActionBar.setTitle(R.string.title_manage_visibility);
                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_bank_account_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(3);
                                mMainPageActionBar.setTitle(R.string.title_manage_bank_account);
                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_settings_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(4);
                                mMainPageActionBar.setTitle(R.string.title_manage_settings);
                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_about_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(5);
                                mMainPageActionBar.setTitle(R.string.title_manage_about);
                                mMainPageBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            default:
                                break;
                        }
                    }
                }, 100);
                DrawerLayout drawer = findViewById(R.id.main_menu_drawer_layout);
                drawer.closeDrawer(GravityCompat.START);// “right” -- end  "left" -- start
                return true;
            }
        });
    }*/
}
