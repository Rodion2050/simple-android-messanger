package com.example.r205.messanger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.r205.messanger.utils.CurrentUserInfo;
import com.example.r205.messanger.utils.JSONUtils;
import com.example.r205.messanger.utils.MessageInfo;
import com.example.r205.messanger.utils.OnClickHandler;
import com.example.r205.messanger.utils.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by r205 on 15.01.2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mLoginEdit;
    private EditText mPasswordEdit;
    private Button mLogInButton;
    private EditText mLoginEditRegister;
    private EditText mPasswordEditRegister;
    private EditText mPasswordEditConfirmRegister;
    private Button mRegisterButton;
    private EditText mNameEdit;
    private Context context = this;

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_login);
        mLoginEdit = (EditText) findViewById(R.id.login_edit);
        mPasswordEdit = (EditText) findViewById(R.id.password_edit);
        mLogInButton = (Button) findViewById(R.id.login_button);
        mLogInButton.setOnClickListener(this);
        mLoginEditRegister = (EditText) findViewById(R.id.login_edit_for_register);
        mPasswordEditRegister = (EditText) findViewById(R.id.password_edit_for_register);
        mPasswordEditConfirmRegister = (EditText) findViewById(R.id.confirm_password_edit);
        mNameEdit = (EditText) findViewById(R.id.name_edit);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_button:
                login();
                break;
            case R.id.register_button:
                register();
                break;
        }
    }
    private void login(){
        String loginText = mLoginEdit.getText().toString();
        String passwordText = mPasswordEdit.getText().toString();
        new LoginTask().execute(loginText, passwordText);
    }
    private void register(){
        String name = mNameEdit.getText().toString();
        String loginText = mLoginEditRegister.getText().toString();
        String passwordText = mPasswordEditRegister.getText().toString();
        String confirmPassword = mPasswordEditConfirmRegister.getText().toString();
        if(!passwordText.equals(confirmPassword)){
            Toast.makeText(this, "Password are not the same", Toast.LENGTH_LONG).show();
            return;
        }
        new RegisterTask().execute(name, loginText, passwordText);
    }
    private class LoginTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {
            String login = strings[0];
            String password = strings[1];
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter writer = null;
            try{
                socket = new Socket(ServerInfo.HOST, ServerInfo.PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.println("logIn");
                writer.println(login);
                writer.println(password);
                writer.flush();
                String answerLine;
                do{
                    answerLine = reader.readLine();
                }while (answerLine == null);
                int userID = Integer.parseInt(answerLine);
                writer.close();
                reader.close();
                socket.close();
                return userID;
            }catch (IOException e){
                e.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer userID) {
            if(userID != -1){
                CurrentUserInfo.userID = userID;
                Intent intent = new Intent(context, FriendsActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(context, "Login error", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class RegisterTask extends AsyncTask<String, Void, String > {
        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String login = strings[1];
            String password = strings[2];
            Socket socket = null;
            BufferedReader reader = null;
            PrintWriter writer = null;
            try{
                socket = new Socket(ServerInfo.HOST, ServerInfo.PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                writer.println("addUser");
                writer.println(name);
                writer.println(login);
                writer.println(password);
                writer.flush();
                String answerLine;
                do{
                    answerLine = reader.readLine();
                }while (answerLine == null);
                writer.close();
                reader.close();
                socket.close();
                return answerLine;
            }catch (IOException e){
                e.printStackTrace();
                return "IO Error";
            }
        }

        @Override
        protected void onPostExecute(String answer) {
            if(answer.equals("OK")){
                Toast.makeText(context, "Registration Finished", Toast.LENGTH_LONG).show();
            }else if(answer.equals("Error: login already exist")) {
                Toast.makeText(context, "Error: login already exist", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(context, "Error: try later", Toast.LENGTH_LONG).show();
            }
        }
    }
}
