package com.ignovate.lectrefymob.login;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.interfaces.DrawableClickListener;
import com.ignovate.lectrefymob.model.Forget;
import com.ignovate.lectrefymob.model.GenderModel;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.model.Success;
import com.ignovate.lectrefymob.ui.CustomEditText;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ignovate.lectrefymob.utils.Pattern.MOBILE_VALIDE;
import static com.ignovate.lectrefymob.utils.Pattern.PASSWORD_VALIDE;

public class SignUpActivity extends AppCompatActivity implements DrawableClickListener, View.OnClickListener {

    private final static String TAG = "RegistrationActivity";
    private LinearLayout mSingup;
    private ApiInterface apiInterface;
    private AppCompatEditText
            mFirst,
            mLast,
            mMiddle,
            mPhone,
            mEmail,
            mPass,
            mConfirm;


    private AppCompatTextView mLogin;
    private CustomEditText mDob;
    private AppCompatSpinner mGender, mEthinicity;
    private ArrayList<String> gender = new ArrayList<String>();
    private ArrayList<String> ethic = new ArrayList<String>();
    private DatePickerDialog picker;
    private AwesomeValidation aV;
    private String sGender = "";
    private String sEthir = "";


    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_sign_up);
            apiInterface = API.getClient().create(ApiInterface.class);
            aV = new AwesomeValidation(ValidationStyle.BASIC);
            initialView();
            addValidationToViews();
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        initialAdapter();
    }

    private void initialAdapter() {
        try {
            Call<List<GenderModel>> calls = apiInterface.getSpinnerData();
            calls.enqueue(new Callback<List<GenderModel>>() {
                @Override
                public void onResponse(Call<List<GenderModel>> call, Response<List<GenderModel>> response) {
                    Log.e("REs", response.body().get(0).codeName + "");

                    for (int i = 0; i < response.body().size(); i++) {
                        GenderModel genderModel = new GenderModel();
                        if (response.body().get(i).codeType.equals("ethinicity")) {
                            ethic.add(response.body().get(i).getCodeName());
                        } else if (response.body().get(i).codeType.equals("gender")) {
                            gender.add(response.body().get(i).getCodeName());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUpActivity.this,
                            R.layout.simple_spinner_item, ethic);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mEthinicity.setAdapter(adapter);
                    mEthinicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            sEthir = ethic.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SignUpActivity.this,
                            R.layout.simple_spinner_item, gender);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mGender.setAdapter(dataAdapter);
                    mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            sGender = String.valueOf(gender.get(position).charAt(0));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<GenderModel>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private void initialdatePicker() {
        try {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(SignUpActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mDob.setText(year + "-" + ((monthOfYear + 1) > 9 ? (monthOfYear + 1) : "0" + (monthOfYear + 1)) + "-" + (dayOfMonth > 9 ? dayOfMonth : "0" + dayOfMonth));
                        }
                    }, year, month, day);
            picker.show();

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                submitForm();
                break;
            case R.id.login:
                fLogin();
                break;
        }
    }

    private void fLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void initialView() {
        try {
            mFirst = (AppCompatEditText) findViewById(R.id.input_firstname);
            mLast = (AppCompatEditText) findViewById(R.id.input_lastname);
            mMiddle = (AppCompatEditText) findViewById(R.id.input_middlename);
            mPhone = (AppCompatEditText) findViewById(R.id.input_phoneno);
            mEmail = (AppCompatEditText) findViewById(R.id.input_email_id);
            mPass = (AppCompatEditText) findViewById(R.id.input_password);
            mConfirm = (AppCompatEditText) findViewById(R.id.input_confirmpass);
            mSingup = (LinearLayout) findViewById(R.id.signup);
            mLogin = (AppCompatTextView) findViewById(R.id.login);
            mEthinicity = (AppCompatSpinner) findViewById(R.id.ethinicity);
            mGender = (AppCompatSpinner) findViewById(R.id.gender);
            mDob = (CustomEditText) findViewById(R.id.input_dob);
            mDob.setDrawableClickListener(this);
            mSingup.setOnClickListener(this);
            mLogin.setOnClickListener(this);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void addValidationToViews() {
        try {

            aV.addValidation(this, R.id.input_firstname, RegexTemplate.NOT_EMPTY, R.string.invalid_first);
            aV.addValidation(this, R.id.input_lastname, RegexTemplate.NOT_EMPTY, R.string.invalid_last);
            aV.addValidation(this, R.id.input_dob, RegexTemplate.NOT_EMPTY, R.string.invalid_dob);
            aV.addValidation(this, R.id.input_email_id, Patterns.EMAIL_ADDRESS, R.string.invalid_emaill);
            aV.addValidation(this, R.id.input_password, PASSWORD_VALIDE, R.string.invalid_password);
            aV.addValidation(this, R.id.input_confirmpass, R.id.input_password, R.string.invalid_confirm_password);
            aV.addValidation(this, R.id.input_phoneno, MOBILE_VALIDE, R.string.invalid_phoned);
            aV.addValidation(this, R.id.input_phoneno, RegexTemplate.NOT_EMPTY, R.string.invalid_phone);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void submitForm() {
        try {
            if (ConnectivityReceiver.isConnected()) {
                if (aV.validate()) {
                    final RegisterReqModel model = new RegisterReqModel();
                    model.setFirstName(mFirst.getText().toString().trim());
                    model.setLastName(mLast.getText().toString().trim());
                    model.setMiddleName(mMiddle.getText().toString().trim());
                    model.setEthinicity(sEthir);
                    model.setGender(sGender);
                    model.setPhone(mPhone.getText().toString().trim());
                    model.setEmail(mEmail.getText().toString().trim());
                    model.setPassword(mPass.getText().toString().trim());
                    model.setDateOfBirth(mDob.getText().toString().trim());
                    model.setStatus("P");
                    model.setRegion("12");
                    model.setCreatedBy("123");
                    model.setModifiedBy("123");
                    model.setFcm("");
                    model.setWorkingDays("");
                    model.setContract("");
                    LoadingIndicator.dismissLoading();
                    LoadingIndicator.showLoading(SignUpActivity.this, getString(R.string.please_wait));

                    Call<List<RegisterReqModel>> call = apiInterface.roleUsers(mEmail.getText().toString().trim());

                    call.enqueue(new Callback<List<RegisterReqModel>>() {
                        @Override
                        public void onResponse(Call<List<RegisterReqModel>> call, Response<List<RegisterReqModel>> response) {

                            if (response.isSuccessful() && response.body().size() == 0) {

                                Call<List<RegisterReqModel>> calls = apiInterface.phoneVerify(mPhone.getText().toString().trim());

                                calls.enqueue(new Callback<List<RegisterReqModel>>() {
                                    @Override
                                    public void onResponse(Call<List<RegisterReqModel>> call, Response<List<RegisterReqModel>> respon) {
                                        if (respon.isSuccessful() && respon.body().size() == 0) {
                                            Forget fm = new Forget();
                                            fm.setPhone(mPhone.getText().toString().trim());
                                            fm.setEmail(mEmail.getText().toString().trim());
                                            fm.setRefNo(mPhone.getText().toString().trim());
                                            Call<Success> otp = apiInterface.otpGenerate(fm);
                                            otp.enqueue(new Callback<Success>() {
                                                @Override
                                                public void onResponse(Call<Success> call, Response<Success> response) {
                                                    if (response.isSuccessful() && response.body().code.equals("SUCCESS")) {

                                                        LoadingIndicator.dismissLoading();
                                                        Intent intent = new Intent(SignUpActivity.this, ValidateOtpActivity.class);
                                                        intent.putExtra("OBJECT", model);
                                                        intent.putExtra("BOOL", true);
                                                        intent.putExtra("PHONE", mPhone.getText().toString().trim());
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<Success> call, Throwable t) {

                                                }
                                            });
                                        } else {
                                            LoadingIndicator.dismissLoading();
                                            mPhone.setError(getString(R.string.invalid_mobile));
                                            mPhone.requestFocus();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<RegisterReqModel>> call, Throwable t) {

                                    }
                                });
                                Log.e("Error", response.toString() + "" + response.body().size());

                            } else {
                                LoadingIndicator.dismissLoading();
                                mEmail.setError(getString(R.string.invalid_emails));
                                mEmail.requestFocus();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<RegisterReqModel>> call, Throwable t) {

                        }
                    });
                }
            }else {
                LoadingIndicator.alertDialog(SignUpActivity.this, ConnectivityReceiver.isConnected());
            }
        } catch (NullPointerException e) {
            Log.d(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(DrawablePosition target) {
        switch (target) {
            case RIGHT:
                initialdatePicker();
                break;
        }

    }
}


