package com.ignovate.lectrefymob.userprofile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.dashboard.PendingSingleViewActivity;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.interfaces.DrawableClickListener;
import com.ignovate.lectrefymob.model.GenderModel;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.model.Success;
import com.ignovate.lectrefymob.ui.CustomEditText;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.utils.Pattern;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserActivity extends AppCompatActivity implements DrawableClickListener, View.OnClickListener {


    private static final String TAG = "EditUserActivity";
    private AppCompatEditText
            mFirst,
            mLast,
            mMiddle,
            mPhone,
            mEmail;
    private LinearLayout mSave;
    private AppCompatTextView mRole;
    private CustomEditText mDob;
    private AppCompatSpinner mGender, mEthic;
    private ApiInterface apiInterface;

    private ArrayList<String> aethic = new ArrayList<String>();
    private ArrayList<String> agender = new ArrayList<String>();
    private String sEthir;
    private String sGender;
    private RegisterReqModel list;
    private AwesomeValidation aV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edituser);
        apiInterface = API.getClient().create(ApiInterface.class);
        aV = new AwesomeValidation(ValidationStyle.BASIC);
        initializeViews();
        validation();
        initialToolbar();
        initialWebCallApi();
    }


    private void initialToolbar() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setTitle(getString(R.string.edittitle));
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

    private void initialWebCallApi() {
        try {
            if (ConnectivityReceiver.isConnected()) {
                LoadingIndicator.dismissLoading();
                LoadingIndicator.showLoading(EditUserActivity.this, getString(R.string.please_wait));
                String id = getIntent().getExtras().getString("ID");
                Call<RegisterReqModel> call = apiInterface.getSingleUser(id);
                call.enqueue(new Callback<RegisterReqModel>() {
                    @Override
                    public void onResponse(Call<RegisterReqModel> call, Response<RegisterReqModel> response) {
                        Log.e("Error", response.isSuccessful() + "");
                        if (response.isSuccessful()) {

                            list = response.body();
                            mFirst.setText(response.body().getFirstName());
                            mLast.setText(response.body().getLastName());
                            mMiddle.setText(response.body().getMiddleName());
                            mEmail.setText(response.body().getEmail());
                            mPhone.setText(response.body().getPhone());
                            mRole.setText(response.body().getRole());
                            mDob.setText(response.body().getDateOfBirth());
                            initialEthiniCity(response.body().getEthinicity(), response.body().getGender());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterReqModel> call, Throwable t) {

                    }
                });
            } else {
                LoadingIndicator.alertDialog(EditUserActivity.this, ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    private void initialEthiniCity(final String ethinicity, final String gender) {
        sGender = gender;
        sEthir = ethinicity;

        Call<List<GenderModel>> calls = apiInterface.getSpinnerData();
        calls.enqueue(new Callback<List<GenderModel>>() {
            @Override
            public void onResponse(Call<List<GenderModel>> call, Response<List<GenderModel>> response) {
                LoadingIndicator.dismissLoading();
                for (int i = 0; i < response.body().size(); i++) {
                    GenderModel genderModel = new GenderModel();
                    if (response.body().get(i).getCodeType().equalsIgnoreCase("ethinicity")) {
                        aethic.add(response.body().get(i).getCodeName());
                    } else if (response.body().get(i).getCodeType().equalsIgnoreCase("gender")) {
                        agender.add(response.body().get(i).getCodeName());
                    }

                }
                int position = 0;

                for (int i = 0; i < aethic.size(); i++) {
                    if (aethic.get(i).equalsIgnoreCase(ethinicity)) {
                        position = i;
                    }
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(EditUserActivity.this,
                        R.layout.simple_spinner_item, aethic);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mEthic.setSelection(position);
                mEthic.setAdapter(dataAdapter);
                mEthic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sEthir = aethic.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                int position1 = 0;
                for (int j = 0; j < agender.size(); j++) {
                    if (gender.equalsIgnoreCase(String.valueOf(agender.get(j).charAt(0)))) {
                        position1 = j;
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditUserActivity.this,
                        R.layout.simple_spinner_item, agender);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mGender.setAdapter(adapter);
                mGender.setSelection(position1);
                mGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        sGender = String.valueOf(agender.get(position).charAt(0));
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
    }

    private void initializeViews() {
        try {
            mFirst = (AppCompatEditText) findViewById(R.id.input_firstname);
            mLast = (AppCompatEditText) findViewById(R.id.input_lastname);
            mMiddle = (AppCompatEditText) findViewById(R.id.input_middlename);
            mPhone = (AppCompatEditText) findViewById(R.id.input_phoneno);
            mEmail = (AppCompatEditText) findViewById(R.id.input_email_id);
            mEthic = (AppCompatSpinner) findViewById(R.id.ethinicity);
            mGender = (AppCompatSpinner) findViewById(R.id.gender);
            mDob = (CustomEditText) findViewById(R.id.input_dob);
            mRole = (AppCompatTextView) findViewById(R.id.txtrole);


            mSave = (LinearLayout) findViewById(R.id.save);
            mDob.setDrawableClickListener(this);
            mSave.setOnClickListener(this);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    private void validation() {
        aV.addValidation(this, R.id.input_firstname, RegexTemplate.NOT_EMPTY, R.string.invalid_first);
        aV.addValidation(this, R.id.input_phoneno, Pattern.MOBILE_VALIDE, R.string.invalid_phoned);
        aV.addValidation(this, R.id.input_email_id, Patterns.EMAIL_ADDRESS, R.string.invalid_emaill);

    }

    @Override
    public void onClick(DrawablePosition target) {
        switch (target) {
            case RIGHT:
                initialdatePicker();
                break;
        }

    }

    private void initialdatePicker() {
        try {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            DatePickerDialog picker = new DatePickerDialog(EditUserActivity.this,
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

            case R.id.save:
                updateUserInfo();
                break;
        }
    }

    private void updateUserInfo() {
        try {
            if (aV.validate()) {
                RegisterReqModel registerReqModel = new RegisterReqModel();
                registerReqModel.setUserId(list.getUserId());
                registerReqModel.setFirstName(mFirst.getText().toString().trim());
                registerReqModel.setLastName(mLast.getText().toString().trim());
                registerReqModel.setMiddleName(mMiddle.getText().toString().trim());
                registerReqModel.setPhone(mPhone.getText().toString().trim());
                registerReqModel.setEmail(mEmail.getText().toString().trim());
                registerReqModel.setRole(mRole.getText().toString().trim());//et_role.getText().toString()
                registerReqModel.setDateOfBirth(mDob.getText().toString().trim());//DOB.getText().toString()
                registerReqModel.setGender(sGender);
                registerReqModel.setEthinicity(sEthir);
                registerReqModel.setPassword(list.getPassword());
                registerReqModel.setStatus(list.getStatus());
                registerReqModel.setRegion(list.getRegion());
                registerReqModel.setCreatedBy(list.getCreatedBy());
                registerReqModel.setModifiedBy(list.getModifiedBy());
                registerReqModel.setFcm(list.getFcm());
                registerReqModel.setWorkingDays(list.getWorkingDays());
                registerReqModel.setWorkHoursStart(list.getWorkHoursStart());
                registerReqModel.setWorkHoursEnd(list.getWorkHoursEnd());
                registerReqModel.setAge(list.getAge());
                registerReqModel.setContract(list.getContract());
                LoadingIndicator.dismissLoading();
                LoadingIndicator.showLoading(EditUserActivity.this, getString(R.string.please_wait));
                String id = getIntent().getExtras().getString("ID");
                Call<Success> call = apiInterface.getUserRoleUpdate(id, registerReqModel);
                call.enqueue(new Callback<Success>() {
                    @Override
                    public void onResponse(Call<Success> call, Response<Success> response) {

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
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }
}
