package com.deal.bytee.vendor.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deal.bytee.vendor.R;

import kotlin.jvm.internal.Intrinsics;


public class TabSellHomeFragment extends BaseFragment {

    View view;
    private FrameLayout homeSellContainer;

    public TabSellHomeFragment() {
        // Required empty public constructor
        super(R.layout.fragment_tab_sell_home);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final TabSellHomeFragment instance() {
            return new TabSellHomeFragment();
        }
    }

    private final void loadHome() {
        BaseFragment instance = MainHomeFragment.Companion.instance();
        String name = MainHomeFragment.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "MainHomeFragment.javaClass.name");
        launchChild(instance, false, name);
    }

    public final void launchChild(BaseFragment fragment, boolean isPostBack, String tag) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(tag, "tag");
        startChildFragment(R.id.homeSellContainer, fragment, isPostBack, tag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_sell_home, container, false);
        initView(view);
        loadHome();
        return view;
    }

    private void initView(View view) {
        homeSellContainer = (FrameLayout) view.findViewById(R.id.homeSellContainer);
    }


}