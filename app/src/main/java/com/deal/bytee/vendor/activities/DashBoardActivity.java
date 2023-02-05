package com.deal.bytee.vendor.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.Utils.App;
import com.deal.bytee.vendor.Utils.MySharedPreferences;
import com.deal.bytee.vendor.Utils.NonSwipeableViewPager;
import com.deal.bytee.vendor.adapter.TabAdapter;
import com.deal.bytee.vendor.fragment.TabNewOrderFragment;
import com.deal.bytee.vendor.fragment.TabProfileFragment;
import com.deal.bytee.vendor.fragment.TabSellHomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import kotlin.jvm.internal.Intrinsics;

public class DashBoardActivity extends BaseActivity {


    private FrameLayout bottomLayout;
    private BottomNavigationView bottomNavigation;
    private NonSwipeableViewPager viewPager;
    private ImageView fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        changeStatusBarColor(ContextCompat.getColor(this, R.color.main));
        initView();
        initTabs();
    }

    private void initTabs() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull((Object) fragmentManager, (String) "supportFragmentManager");
        TabAdapter tabAdapter = new TabAdapter(fragmentManager);
        tabAdapter.addFragment(new TabSellHomeFragment(), "Sell");
        tabAdapter.addFragment(new TabNewOrderFragment(), "Add");
        tabAdapter.addFragment(new TabProfileFragment(), "Profile");
        Intrinsics.checkExpressionValueIsNotNull((Object) ((Object) viewPager), (String) "viewPager");
        viewPager.setOffscreenPageLimit(tabAdapter.getCount());
        Intrinsics.checkExpressionValueIsNotNull((Object) ((Object) viewPager), (String) "viewPager");
        viewPager.setAdapter((PagerAdapter) tabAdapter);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intrinsics.checkParameterIsNotNull((Object) menuItem, (String) "item");
                switch (menuItem.getItemId()) {
                    default: {
                        return true;
                    }
                    case R.id.nav_sell: {
                        Intrinsics.checkExpressionValueIsNotNull((Object) ((Object) viewPager), (String) "viewPager");
                        viewPager.setCurrentItem(0);
                        return true;
                    }
                    case R.id.nav_new_order: {
                        Intrinsics.checkExpressionValueIsNotNull((Object) ((Object) viewPager), (String) "viewPager");
                        viewPager.setCurrentItem(1);
                        return true;
                    }
                    case R.id.nav_profile: {
                        Intrinsics.checkExpressionValueIsNotNull((Object) ((Object) viewPager), (String) "viewPager");
                        viewPager.setCurrentItem(2);
                        /*String islogin = "";
                        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
                            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
                        }
                        if (!islogin.equals(MySharedPreferences.YES)) {
                            startActivity(new Intent(DashBoardActivity.this, LoginActivity.class));
                            //DashBoardActivity.this.finish();
                        }else {
                            Intrinsics.checkExpressionValueIsNotNull((Object) ((Object) viewPager), (String) "viewPager");
                            viewPager.setCurrentItem(2);
                        }*/
                        return true;
                    }
                }
            }
        });

    }

    private void initView() {
        bottomLayout = (FrameLayout) findViewById(R.id.bottomLayout);
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        fab = (ImageView) findViewById(R.id.fab);
    }
}