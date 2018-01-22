package com.example.r205.messanger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.r205.messanger.utils.CurrentUserInfo;
import com.example.r205.messanger.utils.JSONUtils;
import com.example.r205.messanger.utils.MessageInfo;
import com.example.r205.messanger.utils.OnClickHandler;
import com.example.r205.messanger.utils.ServerInfo;
import com.example.r205.messanger.utils.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by r205 on 14.01.2018.
 */

public class FriendsActivity extends AppCompatActivity implements OnClickHandler, View.OnClickListener{
    private RecyclerView mFriendsRecycler;
    private FriendsAdapter adapter;
    private LinearLayout mAddFriendLayout;
    private EditText mFriendLoginEdit;
    private Button mAddFriendButton;
    private Context context = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        setTitle("Friends");
        mFriendsRecycler = (RecyclerView) findViewById(R.id.friends_rv);
        getFriends();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mFriendsRecycler.setLayoutManager(linearLayoutManager);
        adapter = new FriendsAdapter(this);
        mFriendsRecycler.setAdapter(adapter);
        mFriendsRecycler.setHasFixedSize(true);
        mAddFriendLayout = (LinearLayout) findViewById(R.id.search_layout);
        mFriendLoginEdit = (EditText) findViewById(R.id.friend_login_edit);
        mAddFriendButton = (Button) findViewById(R.id.search_friend_button);
        mAddFriendButton.setOnClickListener(this);
        getNewMessages();
    }

    private void getNewMessages() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new GetNewMessagesTask().execute();
            }
        }, 0, 10000);
    }

    private void getFriends(){
        new GetFriendsTask().execute();
    }

    @Override
    public void onClickHandler(int id) {
        Intent intent = new Intent(FriendsActivity.this, MessageActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friens_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search_friend_item){
            mAddFriendLayout.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.search_friend_button){
            new AddFriendTask().execute(mFriendLoginEdit.getText().toString());
        }
    }

    private class GetFriendsTask extends AsyncTask<Void, Void, ArrayList<User>> {

        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter writer = null;
            ArrayList<User> friends = new ArrayList<>();
            try{
                socket = new Socket(ServerInfo.HOST, ServerInfo.PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.println("getFriends");
                writer.println(CurrentUserInfo.userID);
                String messageLine;
                writer.flush();
                while ((messageLine = reader.readLine()) != null){
                    friends.add(JSONUtils.getUserInfoFromJSONString(messageLine));
                }
                writer.close();
                reader.close();
                socket.close();
                return friends;
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<User> friends) {
            adapter.setFriends(friends);
        }
    }
    private class AddFriendTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String ... strings) {
            String friendLogin = strings[0];
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter writer = null;
            try{
                socket = new Socket(ServerInfo.HOST, ServerInfo.PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.println("addFriend");
                writer.println(friendLogin);
                writer.println(CurrentUserInfo.userID);
                writer.flush();
                writer.close();
                reader.close();
                socket.close();
                return 1;
            }catch (IOException e){
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if(result == 1){
                mAddFriendLayout.setVisibility(View.INVISIBLE);
                mFriendsRecycler.setVisibility(View.VISIBLE);
                getFriends();
            }else{
                Toast.makeText(context, "Cant add friend", Toast.LENGTH_LONG).show();
            }
        }
    }
    private class GetNewMessagesTask extends AsyncTask<Void, Void, ArrayList<MessageInfo>> {

        @Override
        protected ArrayList<MessageInfo> doInBackground(Void... voids) {
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter writer = null;
            try{
                socket = new Socket(ServerInfo.HOST, ServerInfo.PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                ArrayList<MessageInfo> messageInfos = new ArrayList<>();
                writer.println("getNewMessages");
                writer.println(CurrentUserInfo.userID);
                String messageLine;
                writer.flush();
                while ((messageLine = reader.readLine()) != null){
                    messageInfos.add(JSONUtils.getMessageInfoFromJSONString(messageLine));
                }

                writer.close();
                reader.close();
                socket.close();
                return messageInfos;
            }catch (IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<MessageInfo> messages) {
            if(messages != null){
                ArrayList<Integer> friendIDs = new ArrayList<>();
                for(MessageInfo messageInfo: messages){
                    if(!friendIDs.contains(messageInfo.getSenderID())){
                        friendIDs.add(messageInfo.getSenderID());
                    }
                }
                adapter.setFriensWithNewMessage(friendIDs);
            }
        }
    }
}
