package com.ignovate.lectrefymob.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.model.Success;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.utils.Pattern;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "Reset_passwordActivity";
    private ApiInterface apiInterface;
    private AwesomeValidation aV;
    private AppCompatEditText mNewpass, mConfirmPass;
    private LinearLayout mSave;

    private RegisterReqModel res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_reset);
            apiInterface = API.getClient().create(ApiInterface.class);
            aV = new AwesomeValidation(ValidationStyle.BASIC);

            initialView();
            addValidationToViews();

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void addValidationToViews() {
        aV.addValidation(this, R.id.input_newpassword, Pattern.PASSWORD_VALIDE, R.string.invalid_password);
        aV.addValidation(this, R.id.input_confirm, R.id.input_newpassword, R.string.invalid_confirm_password);
    }

    private void initialView() {
        mNewpass = (AppCompatEditText) findViewById(R.id.input_newpassword);
        mConfirmPass = (AppCompatEditText) findViewById(R.id.input_confirm);
        mSave = (LinearLayout) findViewById(R.id.save);
        mSave.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String id = intent.getStringExtra("USERID");
        Call<RegisterReqModel> calls = apiInterface.getSingleUser(id);
        calls.enqueue(new Callback<RegisterReqModel>() {
            @Override
            public void onResponse(Call<RegisterReqModel> call, Response<RegisterReqModel> response) {

                Log.e("RESD", response.isSuccessful() + "");
                if (response.isSuccessful()) {
                    res = response.body();
                }
            }

            @Override
            public void onFailure(Call<RegisterReqModel> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                submitReset();
                break;

        }
    }

    private void submitReset() {
        try {
            if (ConnectivityReceiver.isConnected()) {
                if (aV.validate()) {
                    RegisterReqModel model = new RegisterReqModel();
                    model.setFirstName(res.getFirstName());
                    model.setLastName(res.getLastName());
                    model.setMiddleName(res.getMiddleName());
                    model.setEthinicity(res.getEthinicity());
                    model.setGender(res.getGender());
                    model.setPhone(res.getPhone());
                    model.setEmail(res.getEmail());
                    model.setDateOfBirth(res.getDateOfBirth());
                    model.setRegion(res.getRegion());
                    model.setCreatedBy(res.getCreatedBy());
                    model.setModifiedBy(res.getModifiedBy());
                    model.setFcm(res.getFcm());
                    model.setWorkingDays(res.getWorkingDays());
                    model.setContract(res.getContract());
                    model.setPassword(mNewpass.getText().toString().trim());
                    model.setTitle(res.getTitle());
                    model.setSupervisor(res.getSupervisor());
                    model.setBaseSalary(res.getBaseSalary());
                    model.setAge(res.getAge());
                    model.setCreatedOn(res.getCreatedOn());
                    model.setModifiedOn(res.getModifiedOn());
                    model.setPin(res.getPin());
                    model.setProfileImagePath(res.getProfileImagePath());
                    model.setWorkHoursStart(res.getWorkHoursStart());
                    model.setWorkHoursEnd(res.getWorkHoursEnd());
                    model.setStatus(res.getStatus());
                    model.setRole(res.getRole());

                    LoadingIndicator.dismissLoading();
                    LoadingIndicator.showLoading(ResetActivity.this, getString(R.string.please_wait));
                    Call<Success> calls = apiInterface.getUserRoleUpdate(res.getUserId(), model);
                    calls.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            if (response.isSuccessful() && response.body().code.equals("SUCCESS")) {
                                LoadingIndicator.dismissLoading();
                                Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                LoadingIndicator.dismissLoading();
                                Toast.makeText(ResetActivity.this,response.body().message,Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            LoadingIndicator.dismissLoading();
                        }
                    });

                }
            }else {
                LoadingIndicator.alertDialog(ResetActivity.this, ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
