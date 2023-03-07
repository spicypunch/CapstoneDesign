package com.example.capstonedesign;

import java.util.HashMap;
import java.util.Map;

public class ChatItemObject {
    public Map<String,Boolean> users = new HashMap<>(); //채팅방의 유저들
    public Map<String,Comment> comments = new HashMap<>();//채팅방의 대화내용


    public static class Comment {

        public String userid;
        public String message;
        public Object timestamp;
    }
}
