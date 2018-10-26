package com.ignovate.lectrefymob.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.ignovate.lectrefymob.R;
import com.ignovate.lectrefymob.interfaces.setOnItemClick;
import com.ignovate.lectrefymob.model.RegisterReqModel;

import java.util.ArrayList;

public class ActiveUserAdapter extends RecyclerView.Adapter<ActiveUserAdapter.MyHolder> {
    private Context activity;
    private ArrayList<RegisterReqModel> userModels;
    private setOnItemClick mListener;

    public ActiveUserAdapter(Context activity, ArrayList<RegisterReqModel> userModels, setOnItemClick mListener) {
        this.activity = activity;
        this.userModels = userModels;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activeuseradapter, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.role.setText(userModels.get(position).getRole());
        holder.name.setText(userModels.get(position).getFirstName());
        holder.phone_number.setText(userModels.get(position).getPhone());
        holder.email.setText(userModels.get(position).getEthinicity());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(userModels.get(position).getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return userModels.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        AppCompatTextView role, name, phone_number,email;
        LinearLayout item;

        public MyHolder(View itemView) {
            super(itemView);
            role = (AppCompatTextView) itemView.findViewById(R.id.role);
            name = (AppCompatTextView) itemView.findViewById(R.id.name);
            email = (AppCompatTextView) itemView.findViewById(R.id.email);
            phone_number = (AppCompatTextView) itemView.findViewById(R.id.phone_number);
            item = (LinearLayout) itemView.findViewById(R.id.itemclick);
        }
    }
}
