package com.example.margonari.fdrive.requests;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciano on 18/10/15.
 */
public class RequestAnswer {

    public boolean result;
    public List<String> errors = new ArrayList<String>();
    public String token;
    public String name;
    public String extension;
    public String lastUser;
    public String lastModDate;
    public List<String> tags;

}
