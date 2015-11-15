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

    public static class Content {
        public List<File> files;
    }

    public boolean result;
    public Content content;
}
