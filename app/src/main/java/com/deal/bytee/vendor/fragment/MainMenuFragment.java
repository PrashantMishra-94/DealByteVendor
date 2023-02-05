package com.deal.bytee.vendor.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.adapter.MainMenuAdapter;
import com.deal.bytee.vendor.listnerr.menuItemClickListner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import kotlin.jvm.internal.Intrinsics;


public class MainMenuFragment extends BaseFragment {

    View view;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvMenu;
    private AppCompatTextView tvtitlle;
    private FloatingActionButton btnAdd;
    private Object TabProfileFragment;

    public MainMenuFragment() {
        // Required empty public constructor
        super(R.layout.fragment_main_menu);
    }

    private void initView(View view) {
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        rvMenu = (RecyclerView) view.findViewById(R.id.rvMenu);
        tvtitlle = (AppCompatTextView) view.findViewById(R.id.tvtitlle);
        btnAdd = (FloatingActionButton) view.findViewById(R.id.btnAdd);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final MainMenuFragment instance() {
            return new MainMenuFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        initView(view);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
            }
        });
        initMenu();
        bindClick();
        return view;
    }

    private void bindClick() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialog();
            }
        });
    }

    private void initMenu() {
        rvMenu.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String name = MainMenuFragment.class.getName();
        MainMenuAdapter mainMenuAdapter = new MainMenuAdapter(getActivity());
        rvMenu.setAdapter(mainMenuAdapter);
        mainMenuAdapter.setMenuItemClickListner(new menuItemClickListner() {
            @Override
            public void menuitemclick(int pos) {
                onMenuItemClick();
            }
        });
        rvMenu.setNestedScrollingEnabled(false);
    }

    TabSellHomeFragment parent;

    private void onMenuItemClick() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            this.parent = (TabSellHomeFragment) parentFragment;
        }
        TabSellHomeFragment tabHome = this.parent ;
        if (tabHome != null) {
            BaseFragment instance = SubMenuFragment.Companion.instance();
            String name = SubMenuFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "SubMenuFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }

    private void buildAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Item Name");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        final EditText price = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(price);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              String  m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}