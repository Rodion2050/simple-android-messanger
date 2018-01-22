package com.example.r205.messanger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.r205.messanger.utils.CurrentUserInfo;
import com.example.r205.messanger.utils.JSONUtils;
import com.example.r205.messanger.utils.MessageInfo;
import com.example.r205.messanger.utils.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView messagesRecycler;
    private EditText currMessageEdit;
    private Button sendButton;
    private Timer timer;
    private int receiverID;
    private MessagesAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Messages");

        messagesRecycler = (RecyclerView) findViewById(R.id.messages_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        messagesRecycler.setLayoutManager(linearLayoutManager);
        adapter = new MessagesAdapter();
        messagesRecycler.setAdapter(adapter);

        currMessageEdit = (EditText) findViewById(R.id.message_et);
        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        Intent intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT)){
            receiverID = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new GetMessagesTask().execute();
            }
        }, 0, 10000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_button:
                String message = currMessageEdit.getText().toString();
                try{
                    sendMessage(message);
                }catch (IOException e){
                    e.printStackTrace();
                }

        }
    }
    private void sendMessage(String message) throws IOException{
        SendMessageTask messageTask = new SendMessageTask();
        messageTask.execute(message);
        new GetMessagesTask().execute();
    }
    private class SendMessageTask extends AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            String message = strings[0];
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter writer = null;
            try{
                socket = new Socket(ServerInfo.HOST, ServerInfo.PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.println("sendMessage");
                writer.println(JSONUtils.getJSONStringFromMessageInfo(new MessageInfo(CurrentUserInfo.userID, receiverID , message)));
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
    }
    private class GetMessagesTask extends AsyncTask<Void, Void, ArrayList<MessageInfo>>{

        @Override
        protected ArrayList<MessageInfo> doInBackground(Void... voids) {
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter writer = null;
            ArrayList<MessageInfo> messageInfos = new ArrayList<>();
            try{
                socket = new Socket(ServerInfo.HOST, ServerInfo.PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.println("getMessages");
                writer.println(CurrentUserInfo.userID);
                writer.println(receiverID);
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
        protected void onPostExecute(ArrayList<MessageInfo> messageInfos) {
            if(messageInfos != null){
                adapter.setMessageInfos(messageInfos);
            }
        }
    }
}
