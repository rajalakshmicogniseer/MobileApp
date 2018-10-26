package com.ignovate.lectrefymob.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.setOnItemClick;


@SuppressLint("ValidFragment")
public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    private setOnItemClick in;
    private AppCompatEditText appCompatEditText;

    @SuppressLint("ValidFragment")
    public MyDialogFragment(setOnItemClick in) {
        this.in = in;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popup_window, container, false);
        appCompatEditText = (AppCompatEditText) rootView.findViewById(R.id.reject);
        AppCompatButton submit = (AppCompatButton) rootView.findViewById(R.id.dismiss);
        submit.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        String res = appCompatEditText.getText().toString();
        in.onClick(res);
        dismiss();

    }
}
