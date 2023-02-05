package com.deal.bytee.vendor.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import kotlin.jvm.internal.Intrinsics;


public class MainHomeFragment extends BaseFragment {


    View view;
    private TabSellHomeFragment parent;
    private CardView cvMenu;
    private CardView cvStoreDetails;
    private CardView cvSellRecord;
    private CardView cvKYC;
    private CardView cvDiscount;

    public MainHomeFragment() {
        // Required empty public constructor
        super(R.layout.fragment_main_home);
    }

    private void initView(View view) {
        cvMenu = (CardView) view.findViewById(R.id.cvMenu);
        cvStoreDetails = (CardView) view.findViewById(R.id.cvStoreDetails);
        cvSellRecord = (CardView) view.findViewById(R.id.cvSellRecord);
        cvKYC = (CardView) view.findViewById(R.id.cvKYC);
        cvDiscount = (CardView) view.findViewById(R.id.cvDiscount);
    }


    public static final class Companion {
        private Companion() {
        }

        public static final MainHomeFragment instance() {
            return new MainHomeFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_home, container, false);
        initView(view);
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            this.parent = (TabSellHomeFragment) parentFragment;
        }
        fetchBusiness();
        bindClick();
        return view;
    }



    private void fetchBusiness() {

        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            //  Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.FETCH_MY_BUSINESS_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("businessmalyo>>>>", obj.toString());
                        //    MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                                if (obj.has("businesslist") && !obj.isNull("businesslist")) {
                                    JSONArray businesslist = obj.getJSONArray("businesslist");
                                    JSONObject objj = businesslist.getJSONObject(0);
                                    App.sharedPreferences.setKey(MySharedPreferences.B_id,objj.getString("id"));
                                    App.sharedPreferences.setKey(MySharedPreferences.discount,objj.getString("discount"));
                                }
                            } else {
                                App.sharedPreferences.setKey(MySharedPreferences.isAddBusiness, MySharedPreferences.NO);

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

        cvStoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStoreDetailsClick();
            }
        });

        cvSellRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSellRecordClick();
            }
        });

        cvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuClick();
            }
        });

        cvKYC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onKYCCLick();
            }
        }); cvDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialog();
            }
        });




    }

    private void onStoreDetailsClick() {
        TabSellHomeFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = StoreDetailsFragment.Companion.instance();
            String name = StoreDetailsFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "StoreDetailsFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }

    private void onSellRecordClick() {
        TabSellHomeFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = SellHomeFragment.Companion.instance();
            String name = SellHomeFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "SellHomeFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }

    private void onMenuClick() {
        TabSellHomeFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = SubMenuFragment.Companion.instance();
            String name = SubMenuFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "SubMenuFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }

    private void onKYCCLick() {
        TabSellHomeFragment tabHome = this.parent;
        if (tabHome != null) {
            BaseFragment instance = KYCFragment.Companion.instance();
            String name = KYCFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "KYCFragment::class.java.name");
            tabHome.launchChild(instance, true, name);
        }
    }



    private void buildAlertDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Set Discount");

        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(20, 20, 20, 20);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText pricee = new EditText(context);
        pricee.setHint("Enter Discount Persent");
        pricee.setInputType(InputType.TYPE_CLASS_NUMBER);
        pricee.setText(App.sharedPreferences.getKey(MySharedPreferences.discount));
        layout.addView(pricee); // Another add method

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String priceitem = pricee.getText().toString().trim();
                if (TextUtils.isEmpty(priceitem)) {
                    pricee.requestFocus();
                    pricee.setError("Please enter Discount");
                } else {
                    if (MyUtils.isNetworkAvailable()) {
                        MyUtils.hideKeyboard(getActivity());
                        MyUtils.showProgressDialog(getActivity(), false);
                        apicallsetdiscount(priceitem);
                    } else {
                        MyUtils.showTheToastMessage("Please Check Internet Connection!");
                    }
                }

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

    private void apicallsetdiscount(String priceitem) {

        MyUtils.showProgressDialog(getContext(), false);

        String islogin = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
            islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
        }
        JSONObject js = new JSONObject();
        try {
            if (islogin.equals(MySharedPreferences.YES)) {
                js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
                js.put("businessid", App.sharedPreferences.getKey(MySharedPreferences.B_id));
                js.put("discount",priceitem);
            } else {
                js.put("userid", "");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.SET_DICSCOUNT, js,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("discount>", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                fetchBusiness();
                            } else {
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