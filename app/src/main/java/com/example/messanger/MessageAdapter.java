package com.example.messanger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {


    private ArrayList<Message> messages;
    private String senderImg,recierverImg;
    private Context context;

    public MessageAdapter(ArrayList<Message> messages, String senderImg, String recierverImg, Context context) {
        this.messages = messages;
        this.senderImg = senderImg;
        this.recierverImg = recierverImg;
        this.context = context;
    }


    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.message_holder,parent,false);
            return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.txtMessage.setText(messages.get(position).getContent());

        ConstraintLayout constraintLayout = holder.ccll;

        if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
        {
            //bech nara chkoun eli b3adh msg ken ena donc msg bech yet7awel lel imin
            Glide.with(context).load(senderImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profilImg);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profil_cardview,ConstraintSet.LEFT);
            constraintSet.clear(R.id.txt_message_content,ConstraintSet.LEFT);
            constraintSet.connect(R.id.profil_cardview,ConstraintSet.RIGHT,R.id.cclayout,ConstraintSet.RIGHT,0);
            constraintSet.connect(R.id.txt_message_content,ConstraintSet.RIGHT,R.id.profil_cardview,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);
        }else
        {
            Glide.with(context).load(recierverImg).error(R.drawable.account_img).placeholder(R.drawable.account_img).into(holder.profilImg);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.clear(R.id.profil_cardview,ConstraintSet.RIGHT);
            constraintSet.clear(R.id.txt_message_content,ConstraintSet.RIGHT);
            constraintSet.connect(R.id.profil_cardview,ConstraintSet.LEFT,R.id.cclayout,ConstraintSet.LEFT,0);
            constraintSet.connect(R.id.txt_message_content,ConstraintSet.LEFT,R.id.profil_cardview,ConstraintSet.RIGHT,0);
            constraintSet.applyTo(constraintLayout);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder{
        ConstraintLayout ccll;
        TextView txtMessage;
        ImageView profilImg;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            ccll=itemView.findViewById(R.id.cclayout);
            txtMessage=itemView.findViewById(R.id.txt_message_content);
            profilImg=itemView.findViewById(R.id.small_image_profil);
        }

    }
}
