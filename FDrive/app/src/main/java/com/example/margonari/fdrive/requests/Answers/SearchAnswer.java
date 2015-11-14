package com.example.margonari.fdrive.requests.Answers;

import java.util.List;

/**
 * Created by luciano on 14/11/15.
 */
public class SearchAnswer {

    public static class Content {
        public List<Integer> files;
    }

    public boolean result;
    public Content content;
}
