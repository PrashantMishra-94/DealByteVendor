package com.deal.bytee.vendor.fragment;

import android.app.AlertDialog;
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

import androidx.appcompat.widget.AppCompatTextView;
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
import com.deal.bytee.vendor.activities.LoginActivity;
import com.deal.bytee.vendor.adapter.MainMenuAdapter;
import com.deal.bytee.vendor.adapter.SubMenuAdapter;
import com.deal.bytee.vendor.listnerr.menuEditClickListner;
import com.deal.bytee.vendor.listnerr.menuItemClickListner;
import com.deal.bytee.vendor.model.MenuModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SubMenuFragment extends BaseFragment {

    View view;
    private AppCompatTextView tvtitlle;
    private SwipeRefreshLayout swipe;
    private RecyclerView rvMenu;
    private FloatingActionButton btnAdd;
    String businessid = "";
    List<MenuModel> menuModelList = new ArrayList<>();

    public SubMenuFragment() {
        // Required empty public constructor
        super(R.layout.fragment_sub_menu);
    }

    private void initView(View view) {
        tvtitlle = (AppCompatTextView) view.findViewById(R.id.tvtitlle);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        rvMenu = (RecyclerView) view.findViewById(R.id.rvMenu);
        btnAdd = (FloatingActionButton) view.findViewById(R.id.btnAdd);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final SubMenuFragment instance() {
            return new SubMenuFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sub_menu, container, false);
        initView(view);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
            }
        });
        fetchBusiness();
        initMenu();
        bindClick();
        return view;
    }

    private void bindClick() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertDialog(false, "", "","");
            }
        });
    }

    private void buildAlertDialog(boolean isFromEdit, String name, String price,String itemId) {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        if (isFromEdit) {
            builder.setTitle("Edit Item");
        } else {
            builder.setTitle("Add Item");
        }

        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(20, 20, 20, 20);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText namee = new EditText(context);
        namee.setHint("Enter Name");
        layout.addView(namee); // Notice this is an add method

        final EditText pricee = new EditText(context);
        pricee.setHint("Enter price");
        pricee.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(pricee); // Another add method

        if (isFromEdit) {
            namee.setText(name);
            pricee.setText(price);
        }

        builder.setView(layout);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nameitem = namee.getText().toString().trim();
                String priceitem = pricee.getText().toString().trim();

                if (TextUtils.isEmpty(nameitem)) {
                    namee.requestFocus();
                    namee.setError("Please enter item name");
                } else if (TextUtils.isEmpty(priceitem)) {
                    pricee.requestFocus();
                    pricee.setError("Please enter item price");
                } else {
                    if (isFromEdit) {
                        if (MyUtils.isNetworkAvailable()) {
                            MyUtils.hideKeyboard(getActivity());
                            MyUtils.showProgressDialog(getActivity(), false);
                            apicalledititem(nameitem, priceitem,itemId);
                        } else {
                            MyUtils.showTheToastMessage("Please Check Internet Connection!");
                        }
                    } else {
                        if (MyUtils.isNetworkAvailable()) {
                            MyUtils.hideKeyboard(getActivity());
                            MyUtils.showProgressDialog(getActivity(), false);
                            apicalladditem(nameitem, priceitem);
                        } else {
                            MyUtils.showTheToastMessage("Please Check Internet Connection!");
                        }
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

    private void apicalledititem(String nameitem, String priceitem, String itemId) {

        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            js.put("businessid", businessid);
            js.put("item_id", itemId);
            js.put("item_name", nameitem);
            js.put("item_price", priceitem);
          //  Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.UPPDATE_ITEM_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("userdataa>>>>", obj.toString());
                        //    MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                fetchBusiness();
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

    private void apicalladditem(String nameitem, String priceitem) {

        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            js.put("businessid", businessid);
            js.put("item_name", nameitem);
            js.put("item_price", priceitem);
           // Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.ADD_ITEM_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("userdataa>>>>", obj.toString());
                            MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                fetchBusiness();
                            } else {
                                  MyUtils.showTheToastMessage(obj.getString("message"));
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
                        Log.d("userdataa>>>>", obj.toString());
                        //    MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                                if (obj.has("businesslist") && !obj.isNull("businesslist")) {
                                    JSONArray businesslist = obj.getJSONArray("businesslist");
                                    JSONObject objj = businesslist.getJSONObject(0);
                                    businessid = objj.getString("id");
                                    fetchMenu(businessid);
                                }
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

    private void fetchMenu(String businessid) {
        menuModelList.clear();
        MyUtils.showProgressDialog(getContext(), false);
        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            js.put("businessid", businessid);
          //  Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.FETCH_MY_MENU_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("userdataa>>>>", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                                if (obj.has("menulist") && !obj.isNull("menulist")) {
                                    JSONArray menulist = obj.getJSONArray("menulist");
                                    for (int i = 0; i < menulist.length(); i++) {
                                        JSONObject objj = menulist.getJSONObject(i);

                                        MenuModel model = new MenuModel();
                                        model.setId(objj.getString("m_id"));
                                        model.setTitle(objj.getString("m_title"));
                                        model.setPrice(objj.getString("m_price"));
                                        menuModelList.add(model);
                                    }
                                    initMenu();
                                }
                            } else {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            MyUtils.dismisProgressDialog();
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

    private void initMenu() {

        rvMenu.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        String name = SubMenuFragment.class.getName();
        SubMenuAdapter subMenuAdapter = new SubMenuAdapter(getActivity(), menuModelList);
        subMenuAdapter.setMenuItemClickListner(new menuItemClickListner() {
            @Override
            public void menuitemclick(int pos) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Remove?");
                builder.setIcon(R.drawable.ic_close);
                builder.setMessage("Are you sure you remove this item?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        apicallRemoveItem(menuModelList.get(pos).getId());
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
        subMenuAdapter.setMenuEditClickListner(new menuEditClickListner() {
            @Override
            public void menuitemclick(int pos) {
                    buildAlertDialog(true,menuModelList.get(pos).getTitle(),menuModelList.get(pos).getPrice(),menuModelList.get(pos).getId());
            }
        });
        rvMenu.setAdapter(subMenuAdapter);
        rvMenu.setNestedScrollingEnabled(false);
    }

    private void apicallRemoveItem(String item_id) {

        MyUtils.showProgressDialog(getContext(), false);
        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            js.put("businessid", businessid);
            js.put("item_id", item_id);
          //  Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.REMOVE_MENU_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("userdataa>>>>", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                fetchBusiness();
                            } else {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                            }
                        } catch (JSONException e) {
                            MyUtils.dismisProgressDialog();
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