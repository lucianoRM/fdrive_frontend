package com.example.margonari.fdrive;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        fileViewHolder.namePlusExtension.setText(files.get(i).name + files.get(i).extension);
        fileViewHolder.size.setText(files.get(i).size);
        fileViewHolder.icon.setImageResource(R.mipmap.ic_folder_black_24dp);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
