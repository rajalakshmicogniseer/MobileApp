package com.ignovate.lectrefymob.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.model.Forget;
import com.ignovate.lectrefymob.model.Otp;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.model.Success;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ValidateOtpActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "ValidateOtpActivity";
    private ApiInterface apiInterface;
    private AwesomeValidation aV;
    private AppCompatEditText mOtp;
    private AppCompatTextView mResend;
    private LinearLayout mSubmit;
    private String nOtp;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_validate_otp);
            apiInterface = API.getClient().create(ApiInterface.class);
            aV = new AwesomeValidation(ValidationStyle.BASIC);
            // aV.setColor(getResources().getColor(R.color.red));
            initialView();
            addValidationToViews();
            intent = getIntent();

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void addValidationToViews() {
        aV.addValidation(this, R.id.otp_id, RegexTemplate.NOT_EMPTY, R.string.invalid_otp);
    }

    private void initialView() {
        mOtp = (AppCompatEditText) findViewById(R.id.otp_id);
        mResend = (AppCompatTextView) findViewById(R.id.resend);
        mSubmit = (LinearLayout) findViewById(R.id.submit);
        mSubmit.setOnClickListener(this);
        mResend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submitValidate();
                break;
            case R.id.resend:
                resendOtp();
                break;
        }
    }

    private void resendOtp() {
        try {
            String phone = intent.getStringExtra("PHONE");
            String email = intent.getStringExtra("EMAIL");

            Forget fm = new Forget();
            fm.setPhone(phone);
            fm.setEmail(email);
            fm.setRefNo(phone);
            LoadingIndicator.dismissLoading();
            LoadingIndicator.showLoading(ValidateOtpActivity.this, getString(R.string.please_wait));
            Call<Success> otp = apiInterface.otpGenerate(fm);
            otp.enqueue(new Callback<Success>() {
                @Override
                public void onResponse(Call<Success> call, Response<Success> response) {
                    if (response.isSuccessful() && response.body().code.equals("SUCCESS")) {
                        LoadingIndicator.dismissLoading();
                    }
                }

                @Override
                public void onFailure(Call<Success> call, Throwable t) {
                    LoadingIndicator.dismissLoading();
                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }


    private void submitValidate() {
        try {
            if (ConnectivityReceiver.isConnected()) {
                if (aV.validate()) {
                    String phone = intent.getStringExtra("PHONE");
                    Otp otp = new Otp();
                    otp.setOtp(mOtp.getText().toString().trim());
                    otp.setRefNo(phone);
                    LoadingIndicator.dismissLoading();
                    LoadingIndicator.showLoading(ValidateOtpActivity.this, getString(R.string.please_wait));
                    Call<Success> call = apiInterface.validateOTP(otp);
                    call.enqueue(new Callback<Success>() {
                        @Override
                        public void onResponse(Call<Success> call, Response<Success> response) {
                            if (response.isSuccessful() && response.body().message.equals("SUCCESS")) {
                                boolean re = intent.getBooleanExtra("BOOL", false);
                                if (re) {
                                    RegisterReqModel ob = (RegisterReqModel) intent.getSerializableExtra("OBJECT");
                                    if (ob != null) {
                                        Call<RegisterReqModel> calls = apiInterface.createUser(ob);
                                        calls.enqueue(new Callback<RegisterReqModel>() {
                                            @Override
                                            public void onResponse(Call<RegisterReqModel> call, Response<RegisterReqModel> responses) {

                                                Log.e("Response code", responses.body() + "");
                                                LoadingIndicator.dismissLoading();
                                                if (responses.isSuccessful()) {
                                                    Intent intent1 = new Intent(ValidateOtpActivity.this, LoginActivity.class);
                                                    startActivity(intent1);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<RegisterReqModel> call, Throwable t) {
                                                Log.e("Response", t.getMessage());
                                            }
                                        });
                                    }
                                } else {
                                    LoadingIndicator.dismissLoading();
                                    String user_id = intent.getStringExtra("USERID");
                                    Intent intent1 = new Intent(ValidateOtpActivity.this, ResetActivity.class);
                                    intent1.putExtra("USERID", user_id);
                                    startActivity(intent1);
                                }
                            } else {
                                LoadingIndicator.dismissLoading();
                                mOtp.setError(getString(R.string.otp_wrong));
                                mOtp.requestFocus();
                            }
                        }

                        @Override
                        public void onFailure(Call<Success> call, Throwable t) {
                            LoadingIndicator.dismissLoading();
                        }
                    });
                }
            } else {
                LoadingIndicator.alertDialog(ValidateOtpActivity.this, ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
