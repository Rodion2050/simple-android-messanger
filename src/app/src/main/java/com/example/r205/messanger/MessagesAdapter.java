package com.example.r205.messanger;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.r205.messanger.utils.CurrentUserInfo;
import com.example.r205.messanger.utils.MessageInfo;
import com.example.r205.messanger.utils.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r205 on 17.01.2018.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {
    private ArrayList<MessageInfo> messageInfos;
    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.message_layout, parent, false);
        return new MessageHolder(view);
    }

    public void setMessageInfos(ArrayList<MessageInfo> messageInfos) {
        this.messageInfos = messageInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(messageInfos == null){
            return 0;
        }else {
            return messageInfos.size();
        }
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        holder.bind(position);
    }

    public class MessageHolder extends RecyclerView.ViewHolder{
        private TextView mFriendNameView;
        private TextView mFriendText;
        private TextView mCurrUserNameView;
        private TextView mCurrUserText;
        private LinearLayout mFriendLayout;
        private LinearLayout mCurrUserLayout;

        public MessageHolder(View itemView) {
            super(itemView);
            mFriendNameView = itemView.findViewById(R.id.name_curr_tv);
            mFriendText = itemView.findViewById(R.id.text_curr_tv);
            mCurrUserNameView = itemView.findViewById(R.id.name_tv);
            mCurrUserText = itemView.findViewById(R.id.text_tv);
            mFriendLayout = (LinearLayout) itemView.findViewById(R.id.friend_message_layout);
            mCurrUserLayout = (LinearLayout) itemView.findViewById(R.id.curr_user_message_layout);
        }

        public void bind(int index) {
            MessageInfo messageInfo = messageInfos.get(index);
            if(messageInfo.getSenderID() == CurrentUserInfo.userID){
                mCurrUserLayout.setVisibility(View.VISIBLE);
                mFriendLayout.setVisibility(View.INVISIBLE);
                mCurrUserText.setText(messageInfo.getText());
                mCurrUserNameView.setText(messageInfo.getSenderName());
            }else{
                mCurrUserLayout.setVisibility(View.INVISIBLE);
                mFriendLayout.setVisibility(View.VISIBLE);
                mFriendText.setText(messageInfo.getText());
                mFriendNameView.setText(messageInfo.getSenderName());
            }
        }
    }
}
