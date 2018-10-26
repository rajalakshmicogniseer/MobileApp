package com.ignovate.lectrefymob.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.dashboard.ActiveUserSingleViewActivity;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.interfaces.OnItemClickListeners;
import com.ignovate.lectrefymob.interfaces.setTitle;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.utils.SessionManager;
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
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    private static String id;
    private ApiInterface apiInterface = null;

    private AppCompatTextView
            mFirstName,
            mPhone_number,
            mMiddlename,
            mLastname,
            mEthinicity,
            mEmail_id,
            mDob,
            mGender,
            mRole;
    private setTitle settitle;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param id     Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String id) {
        MyProfileFragment fragment = new MyProfileFragment();
        MyProfileFragment.id = id;

        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settitle.title(getString(R.string.usertitle));

    }

    @Override
    public void onStart() {
        super.onStart();
        getUserDetails();
    }

    private void getUserDetails() {
        try {
            if (ConnectivityReceiver.isConnected()) {
                LoadingIndicator.dismissLoading();
                LoadingIndicator.showLoading(getActivity(), getString(R.string.please_wait));
                Call<RegisterReqModel> call = apiInterface.getSingleUser(id);

                call.enqueue(new Callback<RegisterReqModel>() {
                    @Override
                    public void onResponse(Call<RegisterReqModel> call, Response<RegisterReqModel> response) {
                        if (response.isSuccessful()) {
                            LoadingIndicator.dismissLoading();
                            mFirstName.setText(response.body().getFirstName());
                            mPhone_number.setText(response.body().getPhone());
                            mMiddlename.setText(response.body().getMiddleName());
                            mLastname.setText(response.body().getLastName());
                            mEthinicity.setText(response.body().getEthinicity());
                            mEmail_id.setText(response.body().getEmail());
                            mGender.setText(response.body().getGender());
                            mRole.setText(response.body().getRole());
                            mDob.setText(response.body().getDateOfBirth());
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterReqModel> call, Throwable t) {

                    }
                });
            }else {
                LoadingIndicator.alertDialog(getActivity(), ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        try {
            setHasOptionsMenu(true);
            view = inflater.inflate(R.layout.fragment_my_profile, container, false);
            initializeViews(view);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.edit) {
            if(ConnectivityReceiver.isConnected()) {
                Intent intent = new Intent(getActivity(), EditUserActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", MyProfileFragment.id);
                intent.putExtras(bundle);
                startActivity(intent);
            }else {
                LoadingIndicator.alertDialog(getActivity(), ConnectivityReceiver.isConnected());
            }
        }


        return super.onOptionsItemSelected(item);
    }

    private void initializeViews(View view) {

        mFirstName = (AppCompatTextView) view.findViewById(R.id.txtfirst_name);
        mMiddlename = (AppCompatTextView) view.findViewById(R.id.txtmiddlename);
        mLastname = (AppCompatTextView) view.findViewById(R.id.txtlastname);
        mDob = (AppCompatTextView) view.findViewById(R.id.txtdob);
        mEthinicity = (AppCompatTextView) view.findViewById(R.id.txtethinicity);
        mGender = (AppCompatTextView) view.findViewById(R.id.txtgender);
        mEmail_id = (AppCompatTextView) view.findViewById(R.id.txtemail);
        mPhone_number = (AppCompatTextView) view.findViewById(R.id.txtphone_number);
        mRole = (AppCompatTextView) view.findViewById(R.id.txtrole);
    }

    @Override
    public void onAttach(Context context) {
        apiInterface = API.getClient().create(ApiInterface.class);
        if (!(context instanceof setTitle))
            throw new ClassCastException();
        else
            settitle = (setTitle) getActivity();
        super.onAttach(context);

    }

}
