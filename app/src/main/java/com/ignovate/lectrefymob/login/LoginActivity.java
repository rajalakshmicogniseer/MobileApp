package com.ignovate.lectrefymob.login;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import com.ignovate.lectrefymob.dashboard.DashBoardActivity;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.model.LoginReqModel;
import com.ignovate.lectrefymob.model.LoginSuccess;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.userprofile.UserProfileActivity;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.utils.SessionManager;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;
import com.ignovate.lectrefymob.webservice.MyApplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = "LoginActivity";
    private AppCompatEditText mUser_id, mPassword;
    private ApiInterface apiInterface;
    private AwesomeValidation aV;
    private SessionManager session;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            session = new SessionManager(getApplicationContext());
            if (session.isLoggedIn()) {
                if (session.getUser() == 0) {
                    Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                    startActivity(intent);
                    finish();
                } else if (session.getUser() == 1) {
                    Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                    intent.putExtra("USER_ID", session.getUserId());
                    startActivity(intent);
                    finish();
                }
            }
            setContentView(R.layout.activity_login);
            apiInterface = API.getClient().create(ApiInterface.class);
            aV = new AwesomeValidation(ValidationStyle.BASIC);
            initialView();
            addValidationToViews();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void initialView() {
        LinearLayout signin = (LinearLayout) findViewById(R.id.signin);
        AppCompatTextView forgotbtn = (AppCompatTextView) findViewById(R.id.forgotbtn);
        AppCompatTextView signupbtn = (AppCompatTextView) findViewById(R.id.signupbtn);
        mUser_id = (AppCompatEditText) findViewById(R.id.input_user_id);
        mPassword = (AppCompatEditText) findViewById(R.id.input_password);
        signin.setOnClickListener(this);
        forgotbtn.setOnClickListener(this);
        signupbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signin:
                submitLogin();
                break;
            case R.id.forgotbtn:
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
                break;
            case R.id.signupbtn:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                break;
        }
    }

    private void addValidationToViews() {
        aV.addValidation(this, R.id.input_user_id, RegexTemplate.NOT_EMPTY, R.string.invalid_emaill);
        aV.addValidation(this, R.id.input_password, RegexTemplate.NOT_EMPTY, R.string.invalid_passl);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void submitLogin() {
        try {
            if (ConnectivityReceiver.isConnected()) {
                if (aV.validate()) {
                    LoadingIndicator.dismissLoading();
                    LoadingIndicator.showLoading(LoginActivity.this, getString(R.string.please_wait));

                    Call<List<RegisterReqModel>> calsl = apiInterface.roleUsers(mUser_id.getText().toString().trim());

                    calsl.enqueue(new Callback<List<RegisterReqModel>>() {
                        @Override
                        public void onResponse(Call<List<RegisterReqModel>> call, Response<List<RegisterReqModel>> res) {
                            if (res.isSuccessful()) {
                                List<RegisterReqModel> ss = res.body();
                                LoadingIndicator.dismissLoading();
                                if (ss.size() == 1) {
                                    String s = ss.get(0).getStatus();
                                    if (s.equals("A")) {
                                        LoginReqModel model = new LoginReqModel();
                                        model.setlogin_id(mUser_id.getText().toString().trim());
                                        model.setpassword(mPassword.getText().toString().trim());
                                        Call<LoginSuccess> calld = apiInterface.loginUser(model);
                                        calld.enqueue(new Callback<LoginSuccess>() {
                                            @Override
                                            public void onResponse(Call<LoginSuccess> call, Response<LoginSuccess> response) {

                                                if (response.isSuccessful()) {
                                                    if (response.body().getMessage() != null) {
                                                        LoadingIndicator.dismissLoading();
                                                        mUser_id.setError(getString(R.string.invalid_email));
                                                        mPassword.setError(getString(R.string.invalid_pass));
                                                    } else {
                                                        Call<List<RegisterReqModel>> call1 = apiInterface.roleUsers(mUser_id.getText().toString().trim());
                                                        call1.enqueue(new Callback<List<RegisterReqModel>>() {
                                                            @Override
                                                            public void onResponse(Call<List<RegisterReqModel>> calls, Response<List<RegisterReqModel>> responses) {

                                                                if (responses.isSuccessful()) {
                                                                    LoadingIndicator.dismissLoading();
                                                                    if (responses.body().get(0).getRole().equals("Super Admin")) {
                                                                        session.createLoginSession(0, mUser_id.getText().toString().trim(), "");
                                                                        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                                                                        startActivity(intent);
                                                                        finish();

                                                                    } else {
                                                                        Intent intent = new Intent(LoginActivity.this, UserProfileActivity.class);
                                                                        intent.putExtra("USER_ID", responses.body().get(0).getUserId());
                                                                        startActivity(intent);
                                                                        session.createLoginSession(1, mUser_id.getText().toString().trim(), responses.body().get(0).getUserId());
                                                                        finish();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<List<RegisterReqModel>> call, Throwable t) {

                                                            }
                                                        });
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<LoginSuccess> call, Throwable t) {

                                            }
                                        });
                                    } else {
                                        mUser_id.setError("Your Account is not Activated");
                                        mUser_id.requestFocus();
                                    }
                                } else {
                                    mUser_id.setError("Email Id not registered. please Signup");
                                    mUser_id.requestFocus();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RegisterReqModel>> call, Throwable t) {

                        }
                    });


                }
            } else {
                LoadingIndicator.alertDialog(LoginActivity.this, ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        LoadingIndicator.alertDialog(LoginActivity.this, isConnected);
    }
}
