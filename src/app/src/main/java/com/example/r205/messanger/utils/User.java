package com.example.r205.messanger.utils;

import java.util.ArrayList;

/**
 * Created by r205 on 14.01.2018.
 */

public class User {
    private int userID;
    private String name;
    private ArrayList<Integer> friendsID = new ArrayList<>();

    public User(int userID, String name) {
        this.userID = userID;
        this.name = name;
    }
    public User(){
        userID = -1;
        name = "";
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addFriend(User friend){
        friendsID.add(friend.getUserID());
        friend.friendsID.add(this.userID);
    }

    public ArrayList<Integer> getFriendsID() {
        return friendsID;
    }
}
