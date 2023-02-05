package com.deal.bytee.vendor.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deal.bytee.vendor.R;

public class NewOrderFragment extends BaseFragment {

    View view;

    public NewOrderFragment() {
        // Required empty public constructor
        super(R.layout.fragment_new_order);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final NewOrderFragment instance() {
            return new NewOrderFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_new_order, container, false);

        return view;
    }
}