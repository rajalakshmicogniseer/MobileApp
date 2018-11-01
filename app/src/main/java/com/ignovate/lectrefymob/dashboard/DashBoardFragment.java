package com.ignovate.lectrefymob.dashboard;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.interfaces.OnItemClickListeners;
import com.ignovate.lectrefymob.interfaces.setTitle;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.utils.SessionManager;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link DashBoardActivity}
 * in two-pane mode (on tablets) or a {@link }
 * on handsets.
 */
public class DashBoardFragment extends Fragment implements View.OnClickListener {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnItemClickListeners mListener;
    private ApiInterface apiInterface;
    private AppCompatTextView mActive;
    private AppCompatTextView mPending;
    private AppCompatTextView mReject;


    private AppCompatImageView mLogout;

    private DashBoardActivity activity;
    private setTitle settitle;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActiveUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settitle.title(getString(R.string.dash));
        if (getArguments() != null) {

            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);
        mActive = (AppCompatTextView) view.findViewById(R.id.active);
        mPending = (AppCompatTextView) view.findViewById(R.id.pending);
        mReject = (AppCompatTextView) view.findViewById(R.id.reject);
        LinearLayout bActive = (LinearLayout) view.findViewById(R.id.active_btn);
        LinearLayout bPending = (LinearLayout) view.findViewById(R.id.pendingbtn);
        LinearLayout breject = (LinearLayout) view.findViewById(R.id.reject_btn);
        bActive.setOnClickListener(this);
        bPending.setOnClickListener(this);
        breject.setOnClickListener(this);
        return view;
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
                Call<ArrayList<RegisterReqModel>> calls = apiInterface.getAllUser();
                calls.enqueue(new Callback<ArrayList<RegisterReqModel>>() {
                    @Override
                    public void onResponse(Call<ArrayList<RegisterReqModel>> call, Response<ArrayList<RegisterReqModel>> response) {
                        LoadingIndicator.dismissLoading();
                        if (response.isSuccessful()) {
                            int a = 0;
                            int b = 0;
                            int c = 0;
                            for (int i = 0; i < response.body().size(); i++) {
                                if (response.body().get(i).getStatus() != null) {
                                    if ((response.body().get(i).getStatus()).equalsIgnoreCase("A")) {
                                        a++;

                                    } else if ((response.body().get(i).getStatus()).equalsIgnoreCase("P")) {
                                        b++;
                                    } else if ((response.body().get(i).getStatus()).equalsIgnoreCase("R")) {
                                        c++;
                                    }
                                }
                            }
                            mActive.setText(String.valueOf(a));
                            mPending.setText(String.valueOf(b));
                            mReject.setText(String.valueOf(c));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<RegisterReqModel>> call, Throwable t) {

                    }
                });
            } else {
                LoadingIndicator.alertDialog(getActivity(), ConnectivityReceiver.isConnected());
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }


    @Override
    public void onAttach(Context context) {
        apiInterface = API.getClient().create(ApiInterface.class);
        activity = (DashBoardActivity) context;

        if (!(context instanceof setTitle))
            throw new ClassCastException();
        else
            settitle = (setTitle) getActivity();
        super.onAttach(context);

    }


    @Override
    public void onClick(View v) {
        if (ConnectivityReceiver.isConnected()) {
            Fragment fragment = null;
            FragmentTransaction fts = getFragmentManager().beginTransaction();
            switch (v.getId()) {
                case R.id.active_btn:
                    fragment = new ActiveUserFragment();
                    break;
                case R.id.pendingbtn:
                    fragment = new PendingApprovalsFragment();
                    break;
                case R.id.reject_btn:
                    fragment = new RejectUserFragment();
                    break;
            }
            fts.replace(R.id.content_frame, fragment);
            fts.commit();
        } else {
            LoadingIndicator.alertDialog(getActivity(), ConnectivityReceiver.isConnected());
        }
    }


}