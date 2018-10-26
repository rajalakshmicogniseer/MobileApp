package com.ignovate.lectrefymob.dashboard;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;


import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.adapter.CustomSpinnerAdapter;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.interfaces.setOnItemClick;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.model.RoleModel;
import com.ignovate.lectrefymob.model.Success;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingSingleViewActivity extends AppCompatActivity implements View.OnClickListener, setOnItemClick {

    private static final String TAG = "PendingSingleView";
    private AppCompatTextView mRoles, mName, mPhone, mFirst, mMiddel, mLast, mDob, mGender, mEthinicity, mEmail;
    private ApiInterface apiInterface;
    private String selectedDiv;
    private AppCompatButton mApprove, mReject;
    private RegisterReqModel data;
    private ArrayList<RegisterReqModel> list = new ArrayList<RegisterReqModel>();
    private boolean selectbutton;
    private boolean webcall;
    private LinearLayout layout;
    private String rejectReason;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_single_view);
        apiInterface = API.getClient().create(ApiInterface.class);
        String id = getIntent().getExtras().getString("ID");

        initialToolbar();
        initialView();
        initCustomSpinner();
        webApiCall(id);
    }

    private void initialToolbar() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setTitle(getString(R.string.userview));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void webApiCall(String id) {
        try {
            if (ConnectivityReceiver.isConnected()) {
                LoadingIndicator.dismissLoading();
                LoadingIndicator.showLoading(PendingSingleViewActivity.this, getString(R.string.please_wait));

                Call<RegisterReqModel> calls = apiInterface.getSingleUser(id);
                calls.enqueue(new Callback<RegisterReqModel>() {
                    @Override
                    public void onResponse(Call<RegisterReqModel> call, Response<RegisterReqModel> response) {

                        if (response.isSuccessful()) {
                            LoadingIndicator.dismissLoading();
                            list.add(response.body());

                            // mRoles.setText(response.body().getRole());
                            mName.setText(response.body().getFirstName() + " " + response.body().getMiddleName() + " " + response.body().getLastName());
                            mPhone.setText(response.body().getPhone());

                            mFirst.setText(response.body().getFirstName());
                            mLast.setText(response.body().getLastName());
                            mMiddel.setText(response.body().getMiddleName());
                            mDob.setText(response.body().getDateOfBirth());
                            mGender.setText(response.body().getGender());
                            //   mRole.setText(response.body().getRole());
                            mEthinicity.setText(response.body().getEthinicity());
                            mEmail.setText(response.body().getEmail());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterReqModel> call, Throwable t) {
                        LoadingIndicator.dismissLoading();
                    }
                });
            }else {
                LoadingIndicator.alertDialog(PendingSingleViewActivity.this, ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void initCustomSpinner() {

        try {
            final Spinner spinnerCustom = (Spinner) findViewById(R.id.spinnerCustom);

            Call<ArrayList<RoleModel>> calls = apiInterface.getRole();
            calls.enqueue(new Callback<ArrayList<RoleModel>>() {
                @Override
                public void onResponse(Call<ArrayList<RoleModel>> call, final Response<ArrayList<RoleModel>> response) {
                    CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(PendingSingleViewActivity.this, response.body());
                    spinnerCustom.setAdapter(customSpinnerAdapter);
                    spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedDiv = response.body().get(position).getRoleName();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

                @Override
                public void onFailure(Call<ArrayList<RoleModel>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void initialView() {
        layout = (LinearLayout) findViewById(R.id.layout);

        mName = (AppCompatTextView) findViewById(R.id.txtname);
        //mRoles = (AppCompatTextView) findViewById(R.id.txtroles);
        mPhone = (AppCompatTextView) findViewById(R.id.txtphone_number);
        mFirst = (AppCompatTextView) findViewById(R.id.txtfirst_name);
        mMiddel = (AppCompatTextView) findViewById(R.id.txtmiddlename);
        mLast = (AppCompatTextView) findViewById(R.id.txtlastname);
        mDob = (AppCompatTextView) findViewById(R.id.txtdob);
        mEthinicity = (AppCompatTextView) findViewById(R.id.txtethinicity);
        mGender = (AppCompatTextView) findViewById(R.id.txtgender);
        mEmail = (AppCompatTextView) findViewById(R.id.txtemail);
        // mRole = (AppCompatTextView) findViewById(R.id.txtrole);
        mApprove = (AppCompatButton) findViewById(R.id.approve);
        mReject = (AppCompatButton) findViewById(R.id.reject);
        mApprove.setOnClickListener(this);
        mReject.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.approve:
                selectbutton = true;
                approveData();
                break;
            case R.id.reject:
                selectbutton = false;
                popWindow();
                break;
        }
    }


    private void popWindow() {
        FragmentManager fm = getSupportFragmentManager();
        MyDialogFragment dialogFragment = new MyDialogFragment(this);
        dialogFragment.show(fm, "Sample Fragment");
    }

    private void approveData() {

        try {
            RegisterReqModel model = new RegisterReqModel();
            for (int i = 0; i < list.size(); i++) {
                model.setFirstName(mFirst.getText().toString().trim());
                model.setLastName(mLast.getText().toString().trim());
                model.setMiddleName(mMiddel.getText().toString().trim());
                model.setEthinicity(mEthinicity.getText().toString());
                model.setGender(mGender.getText().toString());
                model.setPhone(mPhone.getText().toString().trim());
                model.setEmail(mEmail.getText().toString().trim());
                model.setDateOfBirth(mDob.getText().toString().trim());
                model.setRegion(list.get(i).getRegion());
                model.setCreatedBy(list.get(i).getCreatedBy());
                model.setModifiedBy(list.get(i).getModifiedBy());
                model.setFcm(list.get(i).getFcm());
                model.setWorkingDays(list.get(i).getWorkingDays());
                model.setContract(list.get(i).getContract());
                model.setPassword(list.get(i).getPassword());
                model.setTitle(list.get(i).getTitle());
                model.setSupervisor(list.get(i).getSupervisor());
                model.setBaseSalary(list.get(i).getBaseSalary());
                model.setAge(list.get(i).getAge());
                model.setCreatedOn(list.get(i).getCreatedOn());
                model.setModifiedOn(list.get(i).getModifiedOn());
                model.setPin(list.get(i).getPin());
                model.setProfileImagePath(list.get(i).getProfileImagePath());
                model.setWorkHoursStart(list.get(i).getWorkHoursStart());
                model.setWorkHoursEnd(list.get(i).getWorkHoursEnd());
                if (selectbutton) {
                    if (selectedDiv != null) {
                        model.setStatus("A");
                        model.setRole(selectedDiv);
                        webcall = true;
                    } else {
                        webcall = false;

                    }

                } else {
                    webcall = true;
                    model.setStatus("R");
                    model.setReasonOfReject(rejectReason);
                }
            }
            if (webcall) {
                LoadingIndicator.dismissLoading();
                LoadingIndicator.showLoading(PendingSingleViewActivity.this, getString(R.string.please_wait));
                String id = getIntent().getExtras().getString("ID");
                Call<Success> calls = apiInterface.getUserRoleUpdate(id, model);
                calls.enqueue(new Callback<Success>() {
                    @Override
                    public void onResponse(Call<Success> call, Response<Success> response) {
                        Log.e("HBhuer", response.isSuccessful() + "");
                        if (response.isSuccessful()) {
                            LoadingIndicator.dismissLoading();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Success> call, Throwable t) {

                    }
                });
            }
        } catch (NullPointerException e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onClick(String userd) {
        this.rejectReason = userd;
        approveData();
    }
}
