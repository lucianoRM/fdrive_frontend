package com.example.margonari.fdrive.requests.Answers;

import java.util.List;

/**
 * Created by luciano on 15/11/15.
 */
public class GetUsersAnswer {
    public static class User {
        public String email;
        public String name;
        public String lastLocation;
    }
    public List<String> errors;
    public List<User> users;
    public boolean result;




}
