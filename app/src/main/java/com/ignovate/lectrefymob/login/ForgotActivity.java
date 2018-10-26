package com.ignovate.lectrefymob.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.model.Forget;
import com.ignovate.lectrefymob.model.Forgot_passwordmodel;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.model.Success;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.utils.Pattern;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ForgotActivity";
    private ApiInterface apiInterface;
    private AwesomeValidation aV;
    private AppCompatEditText mPhone, mEmailID;
    private LinearLayout mSubmit, mCancel;
    private String nPhone, nEmailID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_forgot);
            apiInterface = API.getClient().create(ApiInterface.class);
            aV = new AwesomeValidation(ValidationStyle.BASIC);
            //  aV.setColor(getResources().getColor(R.color.red));
            initialView();
            addValidationToViews();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void addValidationToViews() {
        aV.addValidation(this, R.id.input_email_id, RegexTemplate.NOT_EMPTY, R.string.invalid_emaill);
        aV.addValidation(this, R.id.input_mobile, Pattern.MOBILE_VALIDE, R.string.invalid_phone);
    }

    private void initialView() {
        try {
            mPhone = (AppCompatEditText) findViewById(R.id.input_mobile);
            mEmailID = (AppCompatEditText) findViewById(R.id.input_email_id);
            mSubmit = (LinearLayout) findViewById(R.id.submit);
            mCancel = (LinearLayout) findViewById(R.id.cancel_btn);
            mSubmit.setOnClickListener(this);
            mCancel.setOnClickListener(this);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                submitForget();
                break;
            case R.id.cancel_btn:
                Intent intent = new Intent(ForgotActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void submitForget() {
        try {
            if (ConnectivityReceiver.isConnected()) {
                if (aV.validate()) {
                    Forgot_passwordmodel model = new Forgot_passwordmodel();
                    model.setEmail(mEmailID.getText().toString().trim());
                    model.setPhone(mPhone.getText().toString().trim());
                    nPhone = mPhone.getText().toString().trim();
                    nEmailID = mEmailID.getText().toString().trim();
                    LoadingIndicator.dismissLoading();
                    LoadingIndicator.showLoading(ForgotActivity.this, getString(R.string.please_wait));


                    Call<List<RegisterReqModel>> call = apiInterface.forgetUser(nPhone, nEmailID);
                    call.enqueue(new Callback<List<RegisterReqModel>>() {
                        @Override
                        public void onResponse(Call<List<RegisterReqModel>> call, final Response<List<RegisterReqModel>> responses) {
                            if (responses.isSuccessful() && responses.body().size() > 0) {
                                LoadingIndicator.dismissLoading();
                                if ((responses.body().get(0).getStatus()).equals("A")) {
                                    Forget fm = new Forget();
                                    fm.setPhone(responses.body().get(0).getPhone());
                                    fm.setEmail(responses.body().get(0).getEmail());
                                    fm.setRefNo(responses.body().get(0).getPhone());
                                    Call<Success> otp = apiInterface.otpGenerate(fm);
                                    otp.enqueue(new Callback<Success>() {
                                        @Override
                                        public void onResponse(Call<Success> call, Response<Success> response) {
                                            if (response.isSuccessful() && response.body().code.equals("SUCCESS")) {
                                                LoadingIndicator.dismissLoading();
                                                Intent intent = new Intent(ForgotActivity.this, ValidateOtpActivity.class);
                                                intent.putExtra("PHONE", responses.body().get(0).getPhone());
                                                intent.putExtra("EMAIL", responses.body().get(0).getEmail());
                                                intent.putExtra("USERID", responses.body().get(0).getUserId());
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Success> call, Throwable t) {
                                            LoadingIndicator.dismissLoading();
                                        }
                                    });
                                }
                            } else {
                                LoadingIndicator.dismissLoading();
                                mPhone.setError(getString(R.string.invalid_phoned));
                                mPhone.requestFocus();

                                mEmailID.setError(getString(R.string.invalid_email));
                                mEmailID.requestFocus();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RegisterReqModel>> call, Throwable t) {
                            LoadingIndicator.dismissLoading();
                        }
                    });
                }
            }else {
                LoadingIndicator.alertDialog(ForgotActivity.this, ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }
}
