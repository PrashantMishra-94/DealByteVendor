package com.deal.bytee.vendor.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.Utils.App;
import com.deal.bytee.vendor.Utils.Endpoints;
import com.deal.bytee.vendor.Utils.MySharedPreferences;
import com.deal.bytee.vendor.Utils.MyUtils;
import com.deal.bytee.vendor.activities.LoginActivity;
import com.deal.bytee.vendor.adapter.MyCustomPagerAdapter;
import com.deal.bytee.vendor.customViews.AutoScrollViewPager;
import com.deal.bytee.vendor.model.BannerItem;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.Intrinsics;


public class ProfileFragment extends BaseFragment {

    View view;
    private CoordinatorLayout mainContent;
    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private AutoScrollViewPager viewPager;
    private LinearLayout llprofile;
    private RoundedImageView ivBusinessImage;
    public static AppCompatTextView tvBusinessName;
    public static AppCompatTextView tvAddress;
    private AppCompatTextView tvEdit;
    List<BannerItem> bannerDatumList = new ArrayList<>();
    private NestedScrollView profielyt;
    private RelativeLayout rlStoreDetails;
    private AppCompatImageView ivSell;
    private RelativeLayout rlMenu;
    private AppCompatImageView ivMenu;
    private RelativeLayout rlKYC;
    private AppCompatImageView ivKyc;
    private RelativeLayout rlSetting;
    private AppCompatImageView ivSetting;
    private RelativeLayout rlPrivacy;
    private AppCompatImageView ivPrivacy;
    private RelativeLayout rlAbout;
    private AppCompatImageView ivAbout;
    private RelativeLayout rlLogout;
    private AppCompatImageView ivLogout;
    private TabProfileFragment parent;

    public ProfileFragment() {
        // Required empty public constructor
        super(R.layout.fragment_profile);
    }

    private void initView(View view) {
        mainContent = (CoordinatorLayout) view.findViewById(R.id.main_content);
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        viewPager = (AutoScrollViewPager) view.findViewById(R.id.viewPager);
        llprofile = (LinearLayout) view.findViewById(R.id.llprofile);
        ivBusinessImage = (RoundedImageView) view.findViewById(R.id.ivBusinessImage);
        tvBusinessName = (AppCompatTextView) view.findViewById(R.id.tvBusinessName);
        tvAddress = (AppCompatTextView) view.findViewById(R.id.tvAddress);
        tvEdit = (AppCompatTextView) view.findViewById(R.id.tvEdit);
        profielyt = (NestedScrollView) view.findViewById(R.id.profielyt);
        rlStoreDetails = (RelativeLayout) view.findViewById(R.id.rlStoreDetails);
        ivSell = (AppCompatImageView) view.findViewById(R.id.ivSell);
        rlMenu = (RelativeLayout) view.findViewById(R.id.rlMenu);
        ivMenu = (AppCompatImageView) view.findViewById(R.id.ivMenu);
        rlKYC = (RelativeLayout) view.findViewById(R.id.rlKYC);
        ivKyc = (AppCompatImageView) view.findViewById(R.id.ivKyc);
        rlSetting = (RelativeLayout) view.findViewById(R.id.rlSetting);
        ivSetting = (AppCompatImageView) view.findViewById(R.id.ivSetting);
        rlPrivacy = (RelativeLayout) view.findViewById(R.id.rlPrivacy);
        ivPrivacy = (AppCompatImageView) view.findViewById(R.id.ivPrivacy);
        rlAbout = (RelativeLayout) view.findViewById(R.id.rlAbout);
        ivAbout = (AppCompatImageView) view.findViewById(R.id.ivAbout);
        rlLogout = (RelativeLayout) view.findViewById(R.id.rlLogout);
        ivLogout = (AppCompatImageView) view.findViewById(R.id.ivLogout);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final ProfileFragment instance() {
            return new ProfileFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            this.parent = (TabProfileFragment) parentFragment;
        }
        apicallfetchdata();
        initSlider();
        bindClick();
        return view;
    }

    public void apicallfetchdata() {
        // MyUtils.showProgressDialog(DashBoardActivity.this, false);
        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
          //  Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.FETCH_PROFILE_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("userdataa>>>>", obj.toString());
                        //    MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                                App.sharedPreferences.setKey(MySharedPreferences.id, obj.getString("userid"));
                                App.sharedPreferences.setKey(MySharedPreferences.name, obj.getString("name"));
                                App.sharedPreferences.setKey(MySharedPreferences.mobile, obj.getString("mobile"));
                                App.sharedPreferences.setKey(MySharedPreferences.email, obj.getString("email"));
                                App.sharedPreferences.setKey(MySharedPreferences.pass, obj.getString("password"));
                                App.sharedPreferences.setKey(MySharedPreferences.address, obj.getString("address"));
                                tvBusinessName.setText(App.sharedPreferences.getKey(MySharedPreferences.name));
                                tvAddress.setText(App.sharedPreferences.getKey(MySharedPreferences.address));
                            } else {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyUtils.dismisProgressDialog();
                String error_type = MyUtils.simpleVolleyRequestError("TAG", error);
                MyUtils.showTheToastMessage(error_type);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        App.mRequestQue.add(request);
    }

    private void bindClick() {

        rlStoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStoreDetailsClick();
            }
        });


        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileEditClick();
            }
        });

        rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuClick();
            }
        });

        rlKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKYCCLick();
            }
        });

        rlLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout?");
                builder.setIcon(R.drawable.ic_close);
                builder.setMessage("Are you sure you want to Logout?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        App.sharedPreferences.setKey(MySharedPreferences.isLogin, MySharedPreferences.NO);
                        App.sharedPreferences.setKey(MySharedPreferences.isSubscribe, MySharedPreferences.NO);
                        App.sharedPreferences.ClearAllData();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void onMenuClick() {
        TabProfileFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = SubMenuFragment.Companion.instance();
            String name = SubMenuFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "SubMenuFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }

    private void onKYCCLick() {
        TabProfileFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = KYCFragment.Companion.instance();
            String name = KYCFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "KYCFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }


    private void onStoreDetailsClick() {

        TabProfileFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = StoreDetailsFragment.Companion.instance();
            String name = StoreDetailsFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "StoreDetailsFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }

    private void onProfileEditClick() {
        TabProfileFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = EditProfileFragment.Companion.instance();
            String name = EditProfileFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "EditProfileFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }


    private void initSlider() {
        bannerDatumList.clear();
        bannerDatumList.add(new BannerItem("https://www.collinsdictionary.com/images/full/restaurant_135621509.jpg", "0"));
        bannerDatumList.add(new BannerItem("https://img.etimg.com/thumb/width-1200,height-900,imgsize-829462,resizemode-1,msid-82666514/industry/services/hotels-/-restaurants/staggered-lockdowns-start-to-bite-battered-restaurants.jpg", "1"));
        bannerDatumList.add(new BannerItem("https://cdn.dnaindia.com/sites/default/files/styles/full/public/2020/04/10/901434-speciality-restaurant.jpg", "2"));
        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(getActivity(), bannerDatumList);
        viewPager.setAdapter(myCustomPagerAdapter);
        viewPager.startAutoScroll();
        viewPager.setInterval(3000);
        viewPager.setCycle(true);
        viewPager.setStopScrollWhenTouch(true);
    }
}