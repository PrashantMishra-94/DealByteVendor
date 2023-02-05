package com.deal.bytee.vendor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deal.bytee.vendor.R;

import kotlin.jvm.internal.Intrinsics;


public class TabNewOrderFragment extends BaseFragment {

    View view;
    private FrameLayout addOrderContainer;

    public TabNewOrderFragment() {
        // Required empty public constructor
        super(R.layout.fragment_tab_new_order);
    }

    private void initView(View view) {
        addOrderContainer = (FrameLayout) view.findViewById(R.id.addOrderContainer);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final TabNewOrderFragment instance() {
            return new TabNewOrderFragment();
        }
    }

    private final void loadHome() {
        BaseFragment instance = NewOrderFragment.Companion.instance();
        String name = NewOrderFragment.class.getName();
        Intrinsics.checkExpressionValueIsNotNull(name, "NewOrderFragment.javaClass.name");
        launchChild(instance, false, name);
    }

    public final void launchChild(BaseFragment fragment, boolean isPostBack, String tag) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(tag, "tag");
        startChildFragment(R.id.addOrderContainer, fragment, isPostBack, tag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab_new_order, container, false);
        initView(view);
        loadHome();
        return view;
    }
}