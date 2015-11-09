package com.example.margonari.fdrive.requests.Answers;

import java.util.List;

/**
 * Created by luciano on 07/11/15.
 */
public class GetUserFilesAnswer {

    public static class Content {
        public List<Integer> files;
        public List<String> folders;
    }

    public boolean result;
    public Content content;

}
