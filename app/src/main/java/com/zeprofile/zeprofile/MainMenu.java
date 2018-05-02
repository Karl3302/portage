/*
 * Created by Hang on 18-4-12 下午5:45
 * Copyright (c) 2018 . All rights reserved.
 * Last modified 18-4-12 下午5:45
 */

package com.zeprofile.zeprofile;

import android.content.res.Configuration;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zeprofile.zeprofile.Utils.CustomViewPager;
import com.zeprofile.zeprofile.Utils.DatabaseHelper;
import com.zeprofile.zeprofile.Utils.FragmentAdapter;
import com.zeprofile.zeprofile.Utils.ZeProfileUtils;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    private BottomNavigationView mBottomNavigationView;
    private CustomViewPager mMainMenuViewPager;
    private ViewPager mProfileRootViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mSideNavigationView;
    private View mSideNavigationHeaderView;
    private TextView mSideNavigationUserNameTextView;
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private String email;
    private DatabaseHelper db;
    private List<Fragment> mListFragment;
    private Integer idFragmentCenter; // The fragment that is actually in center


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
        setContentView(R.layout.activity_main_menu);

      //  View decorView = getWindow().getDecorView();
      //  decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        initViews();
        initData();
        configViews();
    }

    public void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbarMainMenu);
        //mDrawerLayout = (DrawerLayout) findViewById(R.id.main_menu_drawer_layout); //side navigation
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        // mSideNavigationView = (NavigationView) findViewById(R.id.side_navigation_view);
        // mSideNavigationHeaderView = this.getLayoutInflater().inflate(R.layout.side_navigation_header, mSideNavigationView);
        // mSideNavigationUserNameTextView = (TextView) mSideNavigationHeaderView.findViewById(R.id.sideNavigationUserNameTextView);

        setSupportActionBar(mToolbar);
        mActionBar = (ActionBar) getSupportActionBar();
        mActionBar.setTitle(null);

        mMainMenuViewPager = (CustomViewPager) findViewById(R.id.mainMenuViewPager);

        // Load the fragments
        mListFragment = new ArrayList<>();
        mListFragment.add(new FragmentProfileRoot());
        mListFragment.add(new FragmentDiscount());
    }

    public void initData() {
        // Initialization of the elements
        db = new DatabaseHelper(this);
        // Get the userEmail transferred by the last activity
        email = ZeProfileUtils.getStringFromLastActivity(this, "emailAddress");
    }

    public void configViews() {
        // Set the back button
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        // if (DatabaseHelper.isValidEmail(email)) mSideNavigationUserNameTextView.setText(email); // Side Navigation Bar
        // mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp); // Side Navigation Bar
        // mActionBar.setDisplayHomeAsUpEnabled(true); // Side Navigation Bar

        // Setup click event to side navigation view
        //onNavigationViewMenuItemSelected(mSideNavigationView); // Side Navigation Bar

        // Setup the fragment adapter
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(this.getFragmentManager(), this, mListFragment);
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
        });

        //导航栏点击事件和ViewPager滑动事件,让两个控件相互关联
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //这里设置为:当点击到某子项,ViewPager就滑动到对应位置
                //mProfileRootViewPager = findViewById(R.id.profileRootViewPager);
                //TextView mTextView;
                switch (item.getItemId()) {
                    case R.id.leftTabBotNav: // Left Button : Mon Profil
                        ZeProfileUtils.loadViewPager(MainMenu.this, R.id.mainMenuViewPager, 0);
                        //mActionBar.setDisplayHomeAsUpEnabled(false);
                        //mTextView=findViewById(R.id.toolbarMainMenu).findViewById(R.id.titleToolbarMainMenu);
                        //mTextView.setText(R.string.title_profile);
                        //mMainMenuViewPager.setCurrentItem(0);
                        //mProfileRootViewPager.setCurrentItem(0, false);
                        //mActionBar.setTitle(R.string.title_profile);
                        return true;
                    case R.id.rightTabBotNav: // Right Button : Mes Offres
                        ZeProfileUtils.loadViewPager(MainMenu.this, R.id.mainMenuViewPager, 1);
                        //mActionBar.setDisplayHomeAsUpEnabled(false);
                        //mTextView=findViewById(R.id.toolbarMainMenu).findViewById(R.id.titleToolbarMainMenu);
                        //mTextView.setText(R.string.title_discount);
                        //mMainMenuViewPager.setCurrentItem(1);
                        //mActionBar.setTitle(R.string.title_discount);
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        /*// Page Scroll listener
        mMainMenuViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 当滑动到某一位置,导航栏对应位置被按下
                mBottomNavigationView.getMenu().getItem(position).setChecked(true);
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
        //       if (item.getItemId() == android.R.id.home) this.finish();
//        if (item.getItemId() == android.R.id.home) mDrawerLayout.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ZeProfileUtils.shortCenterToast(MainMenu.this, "touched");
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }
/*
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
                                mActionBar.setTitle(R.string.title_profile);
                                mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_mes_offres_tab:
                                mMainMenuViewPager.setCurrentItem(1);
                                mActionBar.setTitle(R.string.title_discount);
                                mBottomNavigationView.getMenu().getItem(1).setChecked(true);
                                break;
                            case R.id.side_navigation_public_profile_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(1);
                                mActionBar.setTitle(R.string.title_manage_public_profile);
                                mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_visibility_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(2);
                                mActionBar.setTitle(R.string.title_manage_visibility);
                                mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_bank_account_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(3);
                                mActionBar.setTitle(R.string.title_manage_bank_account);
                                mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_settings_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(4);
                                mActionBar.setTitle(R.string.title_manage_settings);
                                mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                break;
                            case R.id.side_navigation_about_sub_tab:
                                mMainMenuViewPager.setCurrentItem(0);
                                mProfileRootViewPager.setCurrentItem(5);
                                mActionBar.setTitle(R.string.title_manage_about);
                                mBottomNavigationView.getMenu().getItem(0).setChecked(true);
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
