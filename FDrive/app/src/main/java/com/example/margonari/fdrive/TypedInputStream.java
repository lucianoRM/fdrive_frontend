package com.example.margonari.fdrive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by luciano on 11/11/15.
 */
public class TypedInputStream implements TypedInput, TypedOutput {
    private static final int BUFFER_SIZE = 4096;

    private final String mimeType;
    private final InputStream stream;
    private final String name;
    private final long size;

    /**
     * Constructs a new typed file.
     *
     * @throws NullPointerException if file or mimeType is null
     */
    public TypedInputStream(String name,String mimeType,long size, InputStream newInputStream) {
        if (mimeType == null) {
            throw new NullPointerException("mimeType");
        }
        if (newInputStream == null) {
            throw new NullPointerException("is");
        }
        if (name == null) {
            throw new NullPointerException("name");
        }
        if(size <= 0){
            throw new NullPointerException("size");
        }
        this.mimeType = mimeType;
        this.stream = newInputStream;
        this.name = name;
        this.size = size;
    }


    @Override public String mimeType() {
        return mimeType;
    }

    @Override public long length() {
        return this.size;
    }

    @Override public String fileName() {
        return this.name;
    }

    @Override public InputStream in() throws IOException {
        return this.stream;
    }

    @Override public void writeTo(OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        InputStream in = this.stream;
        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
}
