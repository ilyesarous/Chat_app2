package com.example.messanger;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Usersadapter extends RecyclerView.Adapter<Usersadapter.UserHolder> {

    private ArrayList<User> Users;
    private Context contexte;
    private OnUserClickListener onUserClickListener;

    public Usersadapter(ArrayList<User> users, Context contexte, OnUserClickListener onUserClickListener) {
        this.Users = users;
        this.contexte = contexte;
        this.onUserClickListener = onUserClickListener;
    }

    interface OnUserClickListener{
        void onUserclicked(int position);
    }
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(contexte).inflate(R.layout.user_holder,parent,false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.txtUsername.setText(Users.get(position).getUsername());
        Glide.with(contexte).load(Users.get(position).getUsername()).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return Users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder{

        TextView txtUsername;
        ImageView imageView;

        public UserHolder(@NonNull View itemView)
        {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            txtUsername = itemView.findViewById(R.id.txtUsername);
            imageView = itemView.findViewById(R.id.img_pro);
        }
    }
}
