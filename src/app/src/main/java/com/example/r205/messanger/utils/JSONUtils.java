package com.example.r205.messanger.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by r205 on 13.01.2018.
 */

public class JSONUtils {
    private static final String SENDER_ID_TAG = "senderID";
    private static final String RECEIVER_ID_TAG = "receiverID";
    private static final String RECEIVER_NAME_TAG = "receiverName";
    private static final String SENDER_NAME_TAG = "senderName";
    private static final String MESSAGE_TEXT_TAG = "text";
    private static final String USER_ID_TAG = "userID";
    private static final String USER_NAME_TAG = "userName";
    public static String getJSONStringFromMessageInfo(MessageInfo messageInfo){
        try{
            JSONObject object = new JSONObject();
            object.put(SENDER_ID_TAG, messageInfo.getSenderID());
            object.put(RECEIVER_ID_TAG, messageInfo.getReceiverID());
            if(messageInfo.getReceiverName() != null && !messageInfo.getReceiverName().isEmpty()){
                object.put(SENDER_NAME_TAG, messageInfo.getSenderName());
                object.put(RECEIVER_NAME_TAG, messageInfo.getReceiverName());
            }
            object.put(MESSAGE_TEXT_TAG, messageInfo.getText());
            return object.toString();
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    public static MessageInfo getMessageInfoFromJSONString(String JSONstr){
        try{
            MessageInfo messageInfo = new MessageInfo();
            JSONObject jsonObject = new JSONObject(JSONstr);
            messageInfo.setSenderID(jsonObject.getInt(SENDER_ID_TAG));
            messageInfo.setReceiverID(jsonObject.getInt(RECEIVER_ID_TAG));
            if(jsonObject.has(SENDER_NAME_TAG)){
                messageInfo.setSenderName(jsonObject.getString(SENDER_NAME_TAG));
                messageInfo.setReceiverName(jsonObject.getString(RECEIVER_NAME_TAG));
            }

            messageInfo.setText(jsonObject.getString(MESSAGE_TEXT_TAG));
            return messageInfo;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getJSONStringFromUserInfo(User user){
        try{
            JSONObject object = new JSONObject();
            object.put(USER_ID_TAG, user.getUserID());
            object.put(USER_NAME_TAG, user.getName());
            return object.toString();
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
    public static User getUserInfoFromJSONString(String JSONstr){
        try{
            User user = new User();
            JSONObject jsonObject = new JSONObject(JSONstr);
            user.setUserID(jsonObject.getInt(USER_ID_TAG));
            user.setName(jsonObject.getString(USER_NAME_TAG));
            return user;
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}

