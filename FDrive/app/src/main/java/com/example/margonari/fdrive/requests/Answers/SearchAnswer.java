package com.example.margonari.fdrive.requests.Answers;

import java.util.List;

/**
 * Created by luciano on 14/11/15.
 */
public class SearchAnswer {

    public static class File{
        public int id;
        public String path;
    }


    public boolean result;
    public List<File> files;
}
