package com.zeprofile.zeprofile.test;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zeprofile.zeprofile.R;
import com.zeprofile.zeprofile.utils.DatabaseHelper;
import com.zeprofile.zeprofile.utils.ZeProfileUtils;


public class ManageUserSetting extends AppCompatActivity {

    private String email;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mSideNavigationView;
    private View mSideNavigationHeaderView;
    private ActionBar mActionBar;
    private TextView mSideNavigationUserNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_parameters);

        initView();
        initData();
        configView();
    }

    public void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar_manage_user_parameters);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.side_navigation_drawer_layout);
        mSideNavigationView = (NavigationView) findViewById(R.id.side_navigation_view);
        // Get the side navigation header view (in order to get its component)
        mSideNavigationHeaderView = this.getLayoutInflater().inflate(R.layout.side_navigation_header, mSideNavigationView);
        // Get the component from another view
        mSideNavigationUserNameTextView = (TextView) mSideNavigationHeaderView.findViewById(R.id.sideNavigationUserNameTextView);

        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();

        /*ViewPager viewPager = (ViewPager) findViewById(R.id.);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);*/
    }

    public void initData() {
        // Get the userEmail from the last activity and Set subject of the side navigation view
        email = ZeProfileUtils.getStringFromLastActivity(this, "emailAddress");
    }

    public void configView() {
        if (DatabaseHelper.isValidEmail(email)) mSideNavigationUserNameTextView.setText(email);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        if (mSideNavigationView != null) {
            setupDrawerContent(mSideNavigationView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) mDrawerLayout.openDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    /*
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CheeseListFragment(), "Category 1");
        adapter.addFragment(new CheeseListFragment(), "Category 2");
        adapter.addFragment(new CheeseListFragment(), "Category 3");
        viewPager.setAdapter(adapter);
    }
    */
}
