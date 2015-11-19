package com.example.margonari.fdrive.requests.Answers;

import com.example.margonari.fdrive.requests.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciano on 12/11/15.
 */
public class SaveFileAnswer {

    public boolean result;
    public int fileID;
    public int version;
    public List<String> errors = new ArrayList<String>();


}
