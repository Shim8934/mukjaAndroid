package com.kosmo.mukja.fcm;

import android.content.Context;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kosmo.mukja.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public  static final int MSG_TYPE_LEFT=0;
    public  static final int MSG_TYPE_RIGT=1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;
    String userName;
    String nick;
    public MessageAdapter(Context mContext,List<Chat> mChat,String imageUrl){
        this.mChat=mChat;
        this.mContext=mContext;
        this.imageUrl=imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SharedPreferences preferences = mContext.getSharedPreferences("loginInfo", mContext.MODE_PRIVATE);
        userName = preferences.getString("username", null);
        nick = preferences.getString("nick", null);
        if(viewType==MSG_TYPE_RIGT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat=mChat.get(position);
        holder.show_message.setText(chat.getMessage());
        if(imageUrl==null){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else{
            Glide.with(mContext).load(imageUrl).into(holder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);
            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mChat.get(position).getDetachNo()==1){
            return  MSG_TYPE_RIGT;
        }
        else{
            return  MSG_TYPE_LEFT;
        }
    }
}