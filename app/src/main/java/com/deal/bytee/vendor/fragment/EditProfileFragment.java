package com.deal.bytee.vendor.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

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

import org.json.JSONException;
import org.json.JSONObject;


public class EditProfileFragment extends BaseFragment {


    View view;
    private AppCompatImageView ivBack;
    private AppCompatTextView tvSave;
    private AppCompatEditText edtName;
    private AppCompatEditText edtMobile;
    private AppCompatEditText edtEmail;
    private AppCompatEditText edtAddress;
    boolean isEditMode=false;

    public EditProfileFragment() {
        // Required empty public constructor
        super(R.layout.fragment_edit_profile);
    }

    private void initView(View view) {
        ivBack = (AppCompatImageView) view.findViewById(R.id.ivBack);
        tvSave = (AppCompatTextView) view.findViewById(R.id.tvSave);
        edtName = (AppCompatEditText) view.findViewById(R.id.edtName);
        edtMobile = (AppCompatEditText) view.findViewById(R.id.edtMobile);
        edtEmail = (AppCompatEditText) view.findViewById(R.id.edtEmail);
        edtAddress = (AppCompatEditText) view.findViewById(R.id.edtAddress);
    }

    public static final class Companion {
        private Companion() {
        }

        public static final EditProfileFragment instance() {
            return new EditProfileFragment();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initView(view);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        initData();
        bindClick();
        return view;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            // do your thing
            Log.d("aawiyo>>>edit","0");
            return true;
        }else {
            Log.d("aawiyo>>>edit","1");
        }
        return false;
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
                if (isEditMode){
                    ValidateEdit();
                }else {
                    isEditMode=true;
                    tvSave.setText("Save");
                    edtName.setEnabled(true);
                    edtEmail.setEnabled(true);
                    edtMobile.setEnabled(true);
                    edtAddress.setEnabled(true);
                }

            }
        });
    }

    private void ValidateEdit() {

        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String pass = edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            edtName.requestFocus();
            edtName.setError("Please enter name!");
        } else if (!MyUtils.isValidEmail(email)) {
            edtEmail.requestFocus();
            edtEmail.setError("Please enter valid email!");
        } else if (!MyUtils.isValidMobile(mobile)) {
            edtMobile.requestFocus();
            edtMobile.setError("Please enter valid mobile number!");
        } else if (TextUtils.isEmpty(pass)) {
            edtAddress.requestFocus();
            edtAddress.setError("Please enter Address!");
        } /*else if (TextUtils.isEmpty(cpass)) {
            edtSignUpCPsw.requestFocus();
            edtSignUpCPsw.setError("Please enter pass!");
        } else if (!pass.equals(cpass)) {
            MyUtils.showTheToastMessage("Password Don't Match");
        }*/ else {
            if (MyUtils.isNetworkAvailable()) {
                MyUtils.hideKeyboard(getActivity());
                MyUtils.showProgressDialog(getActivity(), false);
                apicalledit(name, email, mobile, pass);
            } else {
                MyUtils.showTheToastMessage("Please Check Internet Connection!");
            }
        }

    }



    private void apicalledit(String name, String email, String mobile, String address) {
        //TODO need to update token
        JSONObject js = new JSONObject();
        try {
            js.put("userid", App.sharedPreferences.getKey(MySharedPreferences.id));
            js.put("name", name);
            js.put("mobile", mobile);
            js.put("email", email);
            js.put("token", "");
            js.put("password", App.sharedPreferences.getKey(MySharedPreferences.pass));
            js.put("address", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Endpoints.UPDATE_PROFILE_API, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginMethod", response.toString());
                        MyUtils.dismisProgressDialog();
                        try {
                            if (response.getInt("status") == 1) {
                                MyUtils.showTheToastMessage(response.getString("message"));
                                App.sharedPreferences.setKey(MySharedPreferences.id, response.getString("userid"));
                                App.sharedPreferences.setKey(MySharedPreferences.name, response.getString("name"));
                                App.sharedPreferences.setKey(MySharedPreferences.mobile, response.getString("mobile"));
                                App.sharedPreferences.setKey(MySharedPreferences.email, response.getString("email"));
                                App.sharedPreferences.setKey(MySharedPreferences.pass, response.getString("password"));
                                App.sharedPreferences.setKey(MySharedPreferences.address, response.getString("address"));
                                initData();
                            } else {
                                MyUtils.showTheToastMessage(response.getString("message"));
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

    private void initData() {
        edtName.setText(App.sharedPreferences.getKey(MySharedPreferences.name));
        edtEmail.setText(App.sharedPreferences.getKey(MySharedPreferences.email));
        edtMobile.setText(App.sharedPreferences.getKey(MySharedPreferences.mobile));
        edtAddress.setText(App.sharedPreferences.getKey(MySharedPreferences.address));
        tvSave.setText("Edit");
        edtName.setEnabled(false);
        edtEmail.setEnabled(false);
        edtMobile.setEnabled(false);
        edtAddress.setEnabled(false);
        isEditMode=false;
        ProfileFragment.tvBusinessName.setText(App.sharedPreferences.getKey(MySharedPreferences.name));
        ProfileFragment.tvAddress.setText(App.sharedPreferences.getKey(MySharedPreferences.address));
    }
}