package com.example.chatprograming.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatprograming.Models.MessageModel;
import com.example.chatprograming.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==  SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
         return new RecieverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getUıd().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    MessageModel messageModel = messageModels.get(position);

    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            new AlertDialog.Builder(context)
                    .setTitle("delete")
                    .setMessage("are you sure you want to delete this message?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String sendeRoom = FirebaseAuth.getInstance().getUid() + recId;
                            database.getReference().child("chats").child(sendeRoom)
                                    .child(messageModel.getMessageId())
                                    .setValue(null);
                        }
                    }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();

            return false;
        }
    });


    if(holder.getClass()== SenderViewHolder.class)
    {
        ((SenderViewHolder) holder).senderMsg.setText(messageModel.getMessage());

        Date date = new Date(messageModel.getTimestamp());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        String strDate = simpleDateFormat.format(date);
        ((SenderViewHolder)holder).senderTime.setText(strDate);

    }
    else
    {
        ((RecieverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());

        Date date = new Date(messageModel.getTimestamp());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        String strDate = simpleDateFormat.format(date);
        ((RecieverViewHolder)holder).receiverTime.setText(strDate);
    }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public  class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMsg, receiverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);


            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receivertime);


        }
    }
    public  class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg,senderTime;
     public  SenderViewHolder(@NonNull View itemView){
         super(itemView);

         senderMsg = itemView.findViewById(R.id.senderText);
         senderTime =itemView.findViewById(R.id.senderTime);


     }
    }
}
