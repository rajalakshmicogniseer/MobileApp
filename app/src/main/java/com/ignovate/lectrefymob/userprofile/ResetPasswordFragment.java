package com.ignovate.lectrefymob.userprofile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.interfaces.setTitle;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.model.Success;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.utils.Pattern;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String id;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private AppCompatEditText oldPassword, newPassword, confirmPassword;
    private ApiInterface apiInterface;
    private RegisterReqModel list;
    private AwesomeValidation aV;
    private LinearLayout resetPassword;
    private setTitle settitle;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        ResetPasswordFragment.id = param2;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settitle.title(getString(R.string.reset_password));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        webApiCall();
    }

    private void webApiCall() {
        if (ConnectivityReceiver.isConnected()) {
            list = new RegisterReqModel();
            LoadingIndicator.dismissLoading();
            LoadingIndicator.showLoading(getActivity(), getString(R.string.please_wait));
            Call<RegisterReqModel> call = apiInterface.getSingleUser(id);
            call.enqueue(new Callback<RegisterReqModel>() {
                @Override
                public void onResponse(Call<RegisterReqModel> call, Response<RegisterReqModel> response) {
                    Log.e("Error", response.isSuccessful() + "");
                    if (response.isSuccessful()) {
                        LoadingIndicator.dismissLoading();
                        list = response.body();
                    }
                }

                @Override
                public void onFailure(Call<RegisterReqModel> call, Throwable t) {

                }
            });
        }else {
            LoadingIndicator.alertDialog(getActivity(), ConnectivityReceiver.isConnected());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        initializeViews(view);
        return view;
    }

    private void addValidationToViews() {
        aV.addValidation(getActivity(), R.id.input_oldpass,RegexTemplate.NOT_EMPTY, R.string.invalid_old_passempty);
        aV.addValidation(getActivity(), R.id.input_newpass, Pattern.PASSWORD_VALIDE, R.string.invalid_password);
        aV.addValidation(getActivity(), R.id.input_confirmpass, R.id.input_newpass, R.string.invalid_confirm_password);
    }


    private void resetPass() {
        if (ConnectivityReceiver.isConnected()) {
            addValidationToViews();

            try {
                if (aV.validate()) {
                    if(list.getPassword().equalsIgnoreCase(oldPassword.getText().toString())) {


                        RegisterReqModel registerReqModel = new RegisterReqModel();
                        registerReqModel.setFirstName(list.getFirstName());
                        registerReqModel.setLastName(list.getLastName());
                        registerReqModel.setMiddleName(list.getMiddleName());
                        registerReqModel.setPhone(list.getPhone());
                        registerReqModel.setEmail(list.getEmail());
                        registerReqModel.setRole(list.getRole());//et_role.getText().toString()
                        registerReqModel.setDateOfBirth(list.getDateOfBirth());//DOB.getText().toString()
                        registerReqModel.setGender(list.getGender());
                        registerReqModel.setEthinicity(list.getEthinicity());
                        registerReqModel.setPassword(newPassword.getText().toString().trim());
                        registerReqModel.setStatus(list.getStatus());
                        registerReqModel.setRegion(list.getRegion());
                        registerReqModel.setCreatedBy(list.getCreatedBy());
                        registerReqModel.setModifiedBy(list.getModifiedBy());
                        registerReqModel.setFcm(list.getFcm());
                        registerReqModel.setWorkingDays(list.getWorkingDays());
                        registerReqModel.setContract(list.getContract());
                        LoadingIndicator.dismissLoading();
                        LoadingIndicator.showLoading(getActivity(), getString(R.string.please_wait));
                        Call<Success> call = apiInterface.getUserRoleUpdate(id, registerReqModel);
                        call.enqueue(new Callback<Success>() {
                            @Override
                            public void onResponse(Call<Success> call, Response<Success> response) {
                                Log.e("responsePrint", "updationResponse-->" + response.code() + "," + response.message());
                                if (response.isSuccessful()) {
                                    LoadingIndicator.dismissLoading();
                                    Toast.makeText(getActivity(), "Your Password Changed", Toast.LENGTH_LONG).show();
                                    oldPassword.setText("");
                                    newPassword.setText("");
                                    confirmPassword.setText("");
                                }
                            }

                            @Override
                            public void onFailure(Call<Success> call, Throwable t) {
                                Log.d("responsePrint", "updationResponse-->");
                            }
                        });
                    }else {
                      oldPassword.setError(getString(R.string.invalid_old_passwords));
                        oldPassword.requestFocus();
                    }
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        }else {
            LoadingIndicator.alertDialog(getActivity(), ConnectivityReceiver.isConnected());
        }

    }

    private void initializeViews(View view) {
        oldPassword = (AppCompatEditText) view.findViewById(R.id.input_oldpass);
        newPassword = (AppCompatEditText) view.findViewById(R.id.input_newpass);
        confirmPassword = (AppCompatEditText) view.findViewById(R.id.input_confirmpass);

        resetPassword = (LinearLayout) view.findViewById(R.id.resetPassword);
        resetPassword.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        aV = new AwesomeValidation(ValidationStyle.BASIC);
        apiInterface = API.getClient().create(ApiInterface.class);
        if (!(context instanceof setTitle))
            throw new ClassCastException();
        else
            settitle = (setTitle) getActivity();
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetPassword:
                resetPass();
                break;
        }
    }
}
