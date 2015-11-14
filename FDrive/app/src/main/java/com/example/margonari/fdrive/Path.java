package com.example.margonari.fdrive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciano on 09/11/15.
 */
public class Path {

    List<String> path = new ArrayList<>();

    public Path(String root){

        path.add(root);

    }

    public String toAbsolutePath(){
        String absolutePath = "";
        for(int i = 0; i < path.size();i++){
            absolutePath+=path.get(i);
            absolutePath+="/";
        }
        return absolutePath.substring(0,absolutePath.length() - 1);

    }

    public String goTo(String newFolder){

        if(newFolder == ".."){

            if(path.size() == 1){ //Esta en el root
                return path.get(0);
            }else{
                path.remove(path.size() - 1); //Removes actual path
            }

        }else{
            if(path.get(path.size() - 1) != "search") path.add(newFolder);
        }
        return toAbsolutePath();
    }

}
