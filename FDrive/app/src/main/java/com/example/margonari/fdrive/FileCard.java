package com.example.margonari.fdrive;

import java.util.List;

/**
 * Created by luciano on 18/10/15.
 */
public class FileCard {

    public String name;
    public String extension;
    public String size;
    public FileMetadata metadata;

    FileCard(FileMetadata newMetadata){

        this.name = newMetadata.name;
        this.extension = newMetadata.extension;
        this.size = Integer.toString(newMetadata.size);
        this.metadata = newMetadata;
    }




}
