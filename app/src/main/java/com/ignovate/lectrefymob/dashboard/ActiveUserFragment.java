package com.ignovate.lectrefymob.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.adapter.ActiveUserAdapter;
import com.ignovate.lectrefymob.interfaces.ApiInterface;
import com.ignovate.lectrefymob.interfaces.OnItemClickListeners;
import com.ignovate.lectrefymob.interfaces.setOnItemClick;
import com.ignovate.lectrefymob.interfaces.setTitle;
import com.ignovate.lectrefymob.login.ForgotActivity;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.utils.LoadingIndicator;
import com.ignovate.lectrefymob.webservice.API;
import com.ignovate.lectrefymob.webservice.ConnectivityReceiver;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActiveUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<RegisterReqModel> userModels = new ArrayList<RegisterReqModel>();

    private ApiInterface apiInterface;
    private RecyclerView rv;
    private setTitle settitle;

    public ActiveUserFragment() {

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
    public static ActiveUserFragment newInstance(String param1, String param2) {
        ActiveUserFragment fragment = new ActiveUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settitle.title(getString(R.string.activeuser));
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
                            userModels.clear();
                            for (int i = 0; i < response.body().size(); i++) {
                                if ((response.body().get(i).getStatus()).equals("A")) {
                                    RegisterReqModel re = new RegisterReqModel();
                                    re.setUserId(response.body().get(i).getUserId());
                                    re.setFirstName(response.body().get(i).getFirstName());
                                    re.setMiddleName(response.body().get(i).getMiddleName());
                                    re.setLastName(response.body().get(i).getLastName());
                                    re.setPhone(response.body().get(i).getPhone());
                                    re.setRole(response.body().get(i).getRole());
                                    re.setEthinicity(response.body().get(i).getEthinicity());
                                    userModels.add(re);
                                }
                            }
                            ActiveUserAdapter adapter = new ActiveUserAdapter(getActivity(), userModels, new setOnItemClick() {
                                @Override
                                public void onClick(String userid) {
                                    if (ConnectivityReceiver.isConnected()) {
                                        Intent intent = new Intent(getActivity(), ActiveUserSingleViewActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("ID", userid);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    } else {
                                        LoadingIndicator.alertDialog(getActivity(), ConnectivityReceiver.isConnected());
                                    }
                                }
                            });
                            rv.setAdapter(adapter);
                        }else {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_user, container, false);

        recycleview(view);
        return view;
    }

    private void recycleview(View view) {
        rv = (RecyclerView) view.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

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
