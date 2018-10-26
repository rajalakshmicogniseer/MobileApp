package com.ignovate.lectrefymob.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActiveUserSingleViewActivity extends AppCompatActivity {


    private static final String TAG = "ActiveUserSingleView";
    private ApiInterface apiInterface;
    private RegisterReqModel res;
    private AppCompatTextView mName, mPhone, mFirst, mMiddel, mLast, mDob, mGender, mEthinicity, mEmail, mRole, mRoles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activeuser_single_view);
        apiInterface = API.getClient().create(ApiInterface.class);
        initialToolbar();
        initialView();
        webcallApi();
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

    private void webcallApi() {
        try {
            if (ConnectivityReceiver.isConnected()) {
                LoadingIndicator.dismissLoading();
                LoadingIndicator.showLoading(ActiveUserSingleViewActivity.this, getString(R.string.please_wait));
                String id = getIntent().getExtras().getString("ID");
                Call<RegisterReqModel> calls = apiInterface.getSingleUser(id);
                calls.enqueue(new Callback<RegisterReqModel>() {
                    @Override
                    public void onResponse(Call<RegisterReqModel> call, Response<RegisterReqModel> response) {
                        Log.e("RESD", response.isSuccessful() + "");
                        if (response.isSuccessful()) {
                            LoadingIndicator.dismissLoading();
                            //  mRoles.setText(response.body().getRole());
                            mName.setText(response.body().getFirstName());
                            mPhone.setText(response.body().getPhone());


                            mFirst.setText(response.body().getFirstName());
                            mLast.setText(response.body().getLastName());
                            mMiddel.setText(response.body().getMiddleName());
                            mDob.setText(response.body().getDateOfBirth());
                            mGender.setText(response.body().getGender());
                            mRole.setText(response.body().getRole());
                            mEthinicity.setText(response.body().getEthinicity());
                            mEmail.setText(response.body().getEmail());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterReqModel> call, Throwable t) {

                    }
                });
            }else {
                LoadingIndicator.alertDialog(ActiveUserSingleViewActivity.this, ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }

    private void initialView() {
        try {
            mName = (AppCompatTextView) findViewById(R.id.txtname);
           // mRoles = (AppCompatTextView) findViewById(R.id.txtroles);
            mPhone = (AppCompatTextView) findViewById(R.id.txtphone_number);


            mFirst = (AppCompatTextView) findViewById(R.id.txtfirst_name);
            mMiddel = (AppCompatTextView) findViewById(R.id.txtmiddlename);
            mLast = (AppCompatTextView) findViewById(R.id.txtlastname);
            mDob = (AppCompatTextView) findViewById(R.id.txtdob);
            mGender = (AppCompatTextView) findViewById(R.id.txtgender);
            mEthinicity = (AppCompatTextView) findViewById(R.id.txtethinicity);
            mEmail = (AppCompatTextView) findViewById(R.id.txtemail);
            mRole = (AppCompatTextView) findViewById(R.id.txtrole);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
