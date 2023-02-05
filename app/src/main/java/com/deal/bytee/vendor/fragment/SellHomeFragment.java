package com.deal.bytee.vendor.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.deal.bytee.vendor.adapter.SellRecordsAdapter;
import com.deal.bytee.vendor.model.SoldVoucherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SellHomeFragment extends BaseFragment {

    View view;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvSellRecords;
    List<SoldVoucherModel> soldVoucherModelList = new ArrayList<>();

    public SellHomeFragment() {
        // Required empty public constructor
        super(R.layout.fragment_sell_home);
    }

    private void initView(View view) {
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        rvSellRecords = (RecyclerView) view.findViewById(R.id.rvSellRecords);
    }


    public static final class Companion {
        private Companion() {
        }

        public static final SellHomeFragment instance() {
            return new SellHomeFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sell_home, container, false);
        initView(view);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                initSellRecords();

            }
        });
        initSellRecords();
        return view;
    }

    private void initSellRecords() {

        MyUtils.showProgressDialog(getContext(), false);

        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            } else {
                js.put("userid", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.GET_SELL_RECORD, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("wishlist>", obj.toString());
                        soldVoucherModelList.clear();
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                if (obj.has("voucherslist") && !obj.isNull("voucherslist")) {
                                    JSONArray DetailedData = obj.getJSONArray("voucherslist");
                                    for (int i = 0; i < DetailedData.length(); i++) {
                                        JSONObject galarydata = DetailedData.getJSONObject(i);
                                        SoldVoucherModel model = new SoldVoucherModel();
                                        model.setCv_id(galarydata.getString("cv_id"));
                                        model.setDiscountamount(galarydata.getInt("discountamount"));
                                        model.setCv_voucher_id(galarydata.getString("cv_voucher_id"));
                                        model.setCv_discount(galarydata.getString("cv_discount"));
                                        model.setCv_status(galarydata.getString("cv_status"));
                                        model.setCv_amount_applied(galarydata.getInt("cv_amount_applied"));
                                        model.setCv_code(galarydata.getString("cv_code"));
                                        model.setCv_price(galarydata.getString("cv_price"));
                                        model.setCv_business_id(galarydata.getString("cv_business_id"));
                                        soldVoucherModelList.add(model);
                                    }
                                    rvSellRecords.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                                    String name = SellHomeFragment.class.getName();
                                    rvSellRecords.setAdapter(new SellRecordsAdapter(getActivity(), soldVoucherModelList));
                                    rvSellRecords.setNestedScrollingEnabled(false);


                                }
                            } else {
                                rvSellRecords.setVisibility(View.GONE);
                                // MyUtils.showTheToastMessage(obj.getString("message"));
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

}