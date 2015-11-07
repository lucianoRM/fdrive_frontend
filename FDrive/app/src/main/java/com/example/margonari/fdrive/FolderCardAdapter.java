package com.example.margonari.fdrive;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by luciano on 06/11/15.
 */
public class FolderCardAdapter extends RecyclerView.Adapter<FolderCardAdapter.FolderViewHolder> {

    public static class FolderViewHolder extends RecyclerView.ViewHolder{
        View view;
        FolderCard currentItem;
        TextView name;

        FolderViewHolder(View itemView){
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.folder_name);

            this.view = itemView;
        }
    }

    List<FolderCard> folders;
    FolderCardAdapter(List<FolderCard> folders){
        this.folders = folders;
    }

    @Override
    public int getItemCount(){
        return this.folders.size();
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.folder_card_layout, viewGroup, false);
        FolderViewHolder folderViewHolder = new FolderViewHolder(v);
        return folderViewHolder;
    }

    @Override
    public void onBindViewHolder(FolderViewHolder folderViewHolder, int i) {
        folderViewHolder.name.setText(folders.get(i).name);
        folderViewHolder.currentItem = folders.get(i);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



}
