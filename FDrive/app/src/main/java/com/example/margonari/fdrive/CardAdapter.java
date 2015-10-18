package com.example.margonari.fdrive;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by luciano on 18/10/15.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.FileViewHolder> {

    public static class FileViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView icon;
        TextView namePlusExtension;
        TextView size;

        FileViewHolder(View itemView){
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.extension_photo);
            namePlusExtension = (TextView) itemView.findViewById(R.id.file_name);
            size = (TextView) itemView.findViewById(R.id.file_size);

        }
    }

    List<File> files;
    CardAdapter(List<File> files){
        this.files = files;
    }

    @Override
    public int getItemCount(){
        return this.files.size();
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.file_card_layout, viewGroup, false);
        FileViewHolder fileViewHolder = new FileViewHolder(v);
        return fileViewHolder;
    }

    @Override
    public void onBindViewHolder(FileViewHolder fileViewHolder, int i) {
        List<String> imageExtensionList = Arrays.asList(".jpg",".bmp",".gif",".png",".psd",".pspimage",".thm",".tiff",".yuv");
        List<String> sourceExtensionList = Arrays.asList(".c",".cpp",".java",".py",".sh",".pl");
        List<String> musicExtensionList = Arrays.asList(".mp3",".wav",".mid",".wma");
        List<String> videoExtensionList = Arrays.asList(".3gp",".avi",".mp4",".mkv",".3g2",".asf",".asx",".mov",".mpg",".wmv");
        fileViewHolder.namePlusExtension.setText(files.get(i).name + files.get(i).extension);
        fileViewHolder.size.setText(files.get(i).size);

        String extension = files.get(i).extension;
        int resource = R.mipmap.ic_insert_drive_file_black_24dp;
        if( imageExtensionList.contains(extension) ) {
            resource = R.mipmap.ic_photo_black_24dp;
        }else if( sourceExtensionList.contains(extension)){
            resource = R.mipmap.ic_code_black_24dp;
        }else if( musicExtensionList.contains(extension)){
            resource = R.mipmap.ic_music_note_black_24dp;
        }else if( videoExtensionList.contains(extension)){
            resource = R.mipmap.ic_videocam_black_24dp;
        }


        fileViewHolder.icon.setImageResource(resource);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
