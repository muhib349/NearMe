package com.example.nearme.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nearme.R;
import com.example.nearme.pojos.UserAccount;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<UserAccount> userAccountList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, email, phone,country,residence,jobTitle;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.tv_name);
            email = view.findViewById(R.id.tv_email);
            phone = view.findViewById(R.id.tv_phone);
            country = view.findViewById(R.id.tv_country);
            //residence = view.findViewById(R.id.tv_recidance);
            jobTitle = view.findViewById(R.id.tv_jobTitle);
        }
    }

    public UserAdapter(List<UserAccount> userAccountList) {
        this.userAccountList = userAccountList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        UserAccount user = userAccountList.get(position);
        myViewHolder.name.setText(user.getFirstName()+" "+user.getLastName());
        myViewHolder.country.setText(user.getCurrentCountry());
        myViewHolder.phone.setText(user.getPhone());
        myViewHolder.email.setText(user.getEmail());
        myViewHolder.jobTitle.setText(user.getJobTitle());
        //myViewHolder.residence.setText(user.getResidenceCountry());
    }

    @Override
    public int getItemCount() {
        return userAccountList.size();
    }
}
