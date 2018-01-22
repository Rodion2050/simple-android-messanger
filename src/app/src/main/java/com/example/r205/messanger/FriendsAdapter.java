package com.example.r205.messanger;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.r205.messanger.utils.OnClickHandler;
import com.example.r205.messanger.utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by r205 on 14.01.2018.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>{
    private ArrayList<User> friends = new ArrayList<>();
    private ArrayList<Integer> friensWithNewMessage = new ArrayList<>();
    private OnClickHandler onClickHandler;

    public void setFriensWithNewMessage(ArrayList<Integer> friensWithNewMessage) {
        this.friensWithNewMessage = friensWithNewMessage;
        notifyDataSetChanged();
    }

    public FriendsAdapter(OnClickHandler onClickHandler){
        this.onClickHandler = onClickHandler;
    }
    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.friends_layout, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(friends == null){
            return 0;
        }
        return friends.size();
    }


    public void setFriends(ArrayList<User> friends){
        this.friends = friends;
        this.notifyDataSetChanged();
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mFriendTextView;
        private TextView mNewMessageView;
        private LinearLayout mFriendLayout;
        public FriendViewHolder(View itemView) {
            super(itemView);
            mFriendTextView = itemView.findViewById(R.id.friend_tv);
            mFriendLayout = (LinearLayout) itemView.findViewById(R.id.friend_item_layout);
            mFriendLayout.setOnClickListener(this);
            mNewMessageView = itemView.findViewById(R.id.new_message_view);
        }
        public void bind(int index){
            mFriendTextView.setText(friends.get(index).getName());
            if(friensWithNewMessage.contains(friends.get(index).getUserID())){
                mNewMessageView.setVisibility(View.VISIBLE);
            }else {
                mNewMessageView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            if(onClickHandler != null){
                onClickHandler.onClickHandler(friends.get(getLayoutPosition()).getUserID());
            }
        }
    }

}
