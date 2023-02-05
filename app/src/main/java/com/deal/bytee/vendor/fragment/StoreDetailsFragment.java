package com.deal.bytee.vendor.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.Utils.App;
import com.deal.bytee.vendor.Utils.Endpoints;
import com.deal.bytee.vendor.Utils.MySharedPreferences;
import com.deal.bytee.vendor.Utils.MyUtils;
import com.deal.bytee.vendor.activities.DashBoardActivity;
import com.deal.bytee.vendor.activities.LoginActivity;
import com.deal.bytee.vendor.activities.SplashActivity;
import com.deal.bytee.vendor.model.CatagoryModel;
import com.deal.bytee.vendor.model.CityAreaCountryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StoreDetailsFragment extends BaseFragment {

    View view;
    private AppCompatImageView ivBack;
    private AppCompatTextView tvSave;
    private AppCompatImageView ivStore;
    private AppCompatEditText edtStoreName;
    private AppCompatEditText edtStoreMobile;
    private AppCompatEditText edtStoreEmail;
    private AppCompatEditText edtStoreDescription;
    private AppCompatEditText edtStoreEmployee;
    private AppCompatSpinner spCityArea;
    private AppCompatSpinner spCatagory;
    private AppCompatEditText edtStartTime;
    private AppCompatEditText edtEndTime;
    String businessid = "";


    public StoreDetailsFragment() {
        // Required empty public constructor
        super(R.layout.fragment_store_details);
    }


    boolean isEditMode = false;

    private void initView(View view) {
        ivBack = (AppCompatImageView) view.findViewById(R.id.ivBack);
        tvSave = (AppCompatTextView) view.findViewById(R.id.tvSave);
        ivStore = (AppCompatImageView) view.findViewById(R.id.ivStore);
        edtStoreName = (AppCompatEditText) view.findViewById(R.id.edtStoreName);
        edtStoreMobile = (AppCompatEditText) view.findViewById(R.id.edtStoreMobile);
        edtStoreEmail = (AppCompatEditText) view.findViewById(R.id.edtStoreEmail);
        edtStoreDescription = (AppCompatEditText) view.findViewById(R.id.edtStoreDescription);
        edtStoreEmployee = (AppCompatEditText) view.findViewById(R.id.edtStoreEmployee);
        spCityArea = (AppCompatSpinner) view.findViewById(R.id.spCityArea);
        spCatagory = (AppCompatSpinner) view.findViewById(R.id.spCatagory);
        edtStartTime = (AppCompatEditText) view.findViewById(R.id.edtStartTime);
        edtEndTime = (AppCompatEditText) view.findViewById(R.id.edtEndTime);

    }

    public static final class Companion {
        private Companion() {
        }

        public static final StoreDetailsFragment instance() {
            return new StoreDetailsFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store_details, container, false);
        initView(view);
        bindClick();

        edtStoreMobile.setText(App.sharedPreferences.getKey(MySharedPreferences.mobile));
        edtStoreEmail.setText(App.sharedPreferences.getKey(MySharedPreferences.email));
        fetchAreaCity();
        fetchCatagory();
        fetchBusiness();
        Glide.with(getActivity()).load("https://www.collinsdictionary.com/images/full/restaurant_135621509.jpg")
                .placeholder(R.drawable.vendor_logo)
                .into(ivStore);
        return view;
    }


    List<String> catsubcatNameStrList = new ArrayList<>();
    List<CatagoryModel> catagoryModelArrayList = new ArrayList<>();


    private void fetchCatagory() {
        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            //   Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.FETCH_CAT_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("userdataa>>>>", obj.toString());
                        catsubcatNameStrList.clear();
                        catagoryModelArrayList.clear();
                        //    MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                                if (obj.has("maincat_subcat_list") && !obj.isNull("maincat_subcat_list")) {
                                    JSONArray businesslist = obj.getJSONArray("maincat_subcat_list");
                                    for (int i = 0; i < businesslist.length(); i++) {
                                        JSONObject galarydata = businesslist.getJSONObject(i);
                                        CatagoryModel model = new CatagoryModel();
                                        model.setMainid(galarydata.getString("maincat_id"));
                                        model.setMainname(galarydata.getString("maincat_name"));
                                        model.setSubid(galarydata.getString("subcat_id"));
                                        model.setSubname(galarydata.getString("subcat_name"));
                                        catagoryModelArrayList.add(model);
                                        catsubcatNameStrList.add(galarydata.getString("maincat_name") + "/ " + galarydata.getString("subcat_name") + ".");
                                        setSpinnerCat();
                                    }
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


    public static String catid = "";
    public static String subcatid = "";


    private void setSpinnerCat() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                R.layout.lyt_spinner_text,
                catsubcatNameStrList);
        spCatagory.setAdapter(spinnerArrayAdapter);
        spCatagory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catid = catagoryModelArrayList.get(position).getMainid();
                subcatid = catagoryModelArrayList.get(position).getSubid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    List<String> citystateNameStrList = new ArrayList<>();
    List<CityAreaCountryModel> cityAreaCountryModelArrayList = new ArrayList<>();


    private void fetchAreaCity() {

        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            //   Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.FETCH_ACC_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("userdataa>>>>", obj.toString());
                        citystateNameStrList.clear();
                        cityAreaCountryModelArrayList.clear();
                        //    MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                                if (obj.has("area_city_country_list") && !obj.isNull("area_city_country_list")) {
                                    JSONArray businesslist = obj.getJSONArray("area_city_country_list");
                                    for (int i = 0; i < businesslist.length(); i++) {
                                        JSONObject galarydata = businesslist.getJSONObject(i);
                                        CityAreaCountryModel model = new CityAreaCountryModel();
                                        model.setAreaid(galarydata.getString("area_id"));
                                        model.setArrea(galarydata.getString("area_name"));
                                        model.setCountryid(galarydata.getString("country_id"));
                                        model.setCountry(galarydata.getString("country"));
                                        model.setCityid(galarydata.getString("city_id"));
                                        model.setCity(galarydata.getString("city"));
                                        cityAreaCountryModelArrayList.add(model);
                                        citystateNameStrList.add(galarydata.getString("area_name") + ", " + galarydata.getString("city") + ", " + galarydata.getString("country") + ".");
                                        setSpinnerVoucher();
                                    }
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

    public static String cityid = "";
    public static String areaid = "";
    public static String countryid = "";

    private void setSpinnerVoucher() {

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                R.layout.lyt_spinner_text,
                citystateNameStrList);
        spCityArea.setAdapter(spinnerArrayAdapter);
        spCityArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityid = cityAreaCountryModelArrayList.get(position).getCityid();
                areaid = cityAreaCountryModelArrayList.get(position).getAreaid();
                countryid = cityAreaCountryModelArrayList.get(position).getCountryid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

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
                                    businessid = objj.getString("id");
                                    fetchBusinessDetails(businessid);
                                    App.sharedPreferences.setKey(MySharedPreferences.isAddBusiness, MySharedPreferences.YES);
                                }else {
                                    App.sharedPreferences.setKey(MySharedPreferences.isAddBusiness, MySharedPreferences.NO);
                                }

                            } else {
                                App.sharedPreferences.setKey(MySharedPreferences.isAddBusiness, MySharedPreferences.NO);

                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                            }
                            initDataa();
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

    private void initDataa() {

        String isAddBusiness = "";
        if (App.sharedPreferences.chk(MySharedPreferences.isAddBusiness)) {
            isAddBusiness = App.sharedPreferences.getKey(MySharedPreferences.isAddBusiness);
        }
        if (isAddBusiness.equals(MySharedPreferences.YES)) {
            tvSave.setText("Edit");
            edtStoreName.setEnabled(false);
            edtStoreMobile.setEnabled(false);
            edtStoreEmail.setEnabled(false);
            edtStoreDescription.setEnabled(false);
            edtStoreEmployee.setEnabled(false);
            isEditMode=false;
        }else {
            tvSave.setText("Add");

        }

    }

    private void fetchBusinessDetails(String businessid) {

        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            js.put("businessid", businessid);
            //  Log.d("userid>>>>", App.sharedPreferences.getKey(MySharedPreferences.id));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.FETCH_FULL_BUSINESS_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("userdataa>>>>", obj.toString());
                        //    MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                //  MyUtils.showTheToastMessage(obj.getString("message"));
                                if (obj.has("DetailedData") && !obj.isNull("DetailedData")) {
                                    JSONArray businesslist = obj.getJSONArray("DetailedData");
                                    JSONObject objj = businesslist.getJSONObject(0);
                                    edtStoreName.setText(objj.getString("title"));
                                    edtStoreDescription.setText(objj.getString("description"));
                                    edtStoreEmployee.setText(objj.getString("total_employees"));
                                    edtStoreMobile.setText(App.sharedPreferences.getKey(MySharedPreferences.mobile));
                                    edtStoreEmail.setText(App.sharedPreferences.getKey(MySharedPreferences.email));
                                    for (int i = 0; i < cityAreaCountryModelArrayList.size(); i++) {
                                        if (objj.getString("area_id").equals(cityAreaCountryModelArrayList.get(i).getAreaid())) {
                                            spCityArea.setSelection(i);
                                        }
                                    }
                                    for (int i = 0; i < catagoryModelArrayList.size(); i++) {
                                        if (objj.getString("sub_category_id").equals(catagoryModelArrayList.get(i).getSubid())) {
                                            spCatagory.setSelection(i);
                                        }
                                    }

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


    private void bindClick() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackStack();
            }
        });


        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isAddBusiness = "";
                if (App.sharedPreferences.chk(MySharedPreferences.isAddBusiness)) {
                    isAddBusiness = App.sharedPreferences.getKey(MySharedPreferences.isAddBusiness);
                }
                if (isAddBusiness.equals(MySharedPreferences.YES)) {
                    if (isEditMode){
                        ValidateEdit();
                    }else {
                        isEditMode=true;
                        tvSave.setText("Save");
                        edtStoreName.setEnabled(true);
                        edtStoreMobile.setEnabled(false);
                        edtStoreEmail.setEnabled(false);
                        edtStoreDescription.setEnabled(true);
                        edtStoreEmployee.setEnabled(true);
                    }
                } else {
                    validateData();
                }
            }
        });
    }

    private void ValidateEdit() {

        String name = edtStoreName.getText().toString().trim();
        String email = edtStoreEmail.getText().toString().trim();
        String Description = edtStoreDescription.getText().toString().trim();
        String Employee = edtStoreEmployee.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtStoreName.requestFocus();
            edtStoreName.setError("Please enter name!");
        } else if (!MyUtils.isValidEmail(email)) {
            edtStoreEmail.requestFocus();
            edtStoreEmail.setError("Please enter valid email!");
        } else if (TextUtils.isEmpty(Description)) {
            edtStoreDescription.requestFocus();
            edtStoreDescription.setError("Please enter Description!");
        } else if (TextUtils.isEmpty(Employee)) {
            edtStoreEmployee.requestFocus();
            edtStoreEmployee.setError("Please enter Employee!");
        } else {
            if (MyUtils.isNetworkAvailable()) {
                MyUtils.hideKeyboard(getActivity());
                MyUtils.showProgressDialog(getActivity(), false);
                //apicalledit(name, email, Description, Employee);
                apicalladdbusiness(name,Description,Employee);
            } else {
                MyUtils.showTheToastMessage("Please Check Internet Connection!");
            }
        }

    }


    private void validateData() {
        String storename = edtStoreName.getText().toString().trim();
        String description = edtStoreDescription.getText().toString().trim();
        String totalemployees = edtStoreEmployee.getText().toString().trim();

        if (TextUtils.isEmpty(storename)){
            edtStoreName.requestFocus();
            edtStoreName.setError("Please enter business name");
        }else if (TextUtils.isEmpty(description)){
            edtStoreDescription.requestFocus();
            edtStoreDescription.setError("Please enter business Description");
        }else if (TextUtils.isEmpty(totalemployees)){
            edtStoreEmployee.requestFocus();
            edtStoreEmployee.setError("Please enter business Employee");
        }else {
            if (MyUtils.isNetworkAvailable()) {
                MyUtils.hideKeyboard(getActivity());
                MyUtils.showProgressDialog(getActivity(), false);
                apicalladdbusiness(storename, description,totalemployees);
            } else {
                MyUtils.showTheToastMessage("Please Check Internet Connection!");
            }
        }


    }

    private void apicalladdbusiness(String storename, String description, String totalemployees) {

        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            if (isEditMode){
                js.put("businessid", businessid);
            }
            js.put("countryid", countryid);
            js.put("cityid", cityid);
            js.put("areaid", areaid);
            js.put("maincatid", catid);
            js.put("subcatid", subcatid);
            js.put("business_title", storename);
            js.put("lat", "5555");
            js.put("long", "6666");
            js.put("discount", "8");
            js.put("facebook", "facebook");
            js.put("twitter", "twitter");
            js.put("youtube", "youtube");
            js.put("instagram", "instagram");
            js.put("established_year", "1988");
            js.put("totalemployees", totalemployees);
            js.put("business_email",  App.sharedPreferences.getKey(MySharedPreferences.email));
            js.put("description",  description);
            js.put("faqs",  "faqs");
            js.put("servicedetails",  "servicedetails");
            js.put("maplink",  "maplink");
            js.put("website",  "website");
            js.put("fax",  "fax");
            js.put("tollfree",  "tollfree");
            js.put("turnover",  "turnover");
            js.put("certification",  "iso");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url ="";

        if (isEditMode){
            url =  Endpoints.EDIT_BUSINESS_API;
        }else {
            url =  Endpoints.ADD_BUSINESS_API;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        Log.d("addbusiness>>>>", obj.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (obj.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(obj.getString("message"));
                                App.sharedPreferences.setKey(MySharedPreferences.isAddBusiness, MySharedPreferences.YES);
                                fetchBusiness();
                                tvSave.setText("Edit");
                                edtStoreName.setEnabled(false);
                                edtStoreMobile.setEnabled(false);
                                edtStoreEmail.setEnabled(false);
                                edtStoreDescription.setEnabled(false);
                                edtStoreEmployee.setEnabled(false);
                                isEditMode=false;
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
}