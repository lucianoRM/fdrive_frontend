package com.example.margonari.fdrive;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciano on 15/11/15.
 */
public class AlertDialogManager {

    public static void createShareAlertDialog(final NetworkCallbackClass activityCallback,Context context, final List<String> users,final List<String> alreadySharedUsers){

        final CharSequence[] usersSequence = users.toArray(new CharSequence[users.size()]);

        //Checks already shared values and sets them as checked
        final boolean[] sharedValues = new boolean[users.size()];
        for(int i=0;i<users.size();i++){
            if(alreadySharedUsers.contains(users.get(i))){
                sharedValues[i] = true;
            }else{
                sharedValues[i] = false;
            }
        }




        final List<Integer> toShare = new ArrayList<>();
        final List<Integer> toUnshare = new ArrayList<>();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Share to");
        builder.setMultiChoiceItems(usersSequence, sharedValues, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    //Add selected item
                    toShare.add(which);
                    if (toUnshare.contains(which)) { //If was to unShare, delete it
                        toUnshare.remove(Integer.valueOf(which));
                    }
                } else {
                    //Remove selected item
                    toUnshare.add(which);
                    if (toShare.contains(which)) {
                        toShare.remove(Integer.valueOf(which));
                    }

                }
            }
    }).setPositiveButton("Share", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                List<String> selectedUsersToShare = new ArrayList<String>();
                List<String> selectedUsersToUnshare = new ArrayList<>();
                for (int i = 0; i < toShare.size(); i++) {
                    selectedUsersToShare.add(users.get(toShare.get(i)));
                }
                for (int i = 0; i < toUnshare.size(); i++) {
                    selectedUsersToUnshare.add(users.get(toUnshare.get(i)));
                }
                if (!selectedUsersToShare.isEmpty()) { //if empty dont request
                    activityCallback.share(selectedUsersToShare);
                }
                if (!selectedUsersToUnshare.isEmpty()) { //if empty dont request
                    activityCallback.unshare(selectedUsersToUnshare);
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();//Closes the dialog
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }





    public static void createSearchAlertDialog(final Context context,final NetworkCallbackClass activityCallback) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Search");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.search_alert_dialog, null);
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Spinner searchTypeOptions = (Spinner) dialogView.findViewById(R.id.search_alert_dialog_spinner);
                EditText searchElement = (EditText) dialogView.findViewById(R.id.search_alert_dialog_text);
                String searchSelectedType = searchTypeOptions.getSelectedItem().toString();
                String element = searchElement.getText().toString();

                //Call request method
                activityCallback.search(searchSelectedType, element);


                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    public static void createAddTagAlertDialog(Context context,final NetworkCallbackClass activityCallback){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add tag");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.new_string_alert_dialog, null);
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText newTag = (EditText) dialogView.findViewById(R.id.new_string_alert_dialog_text);
                String newTagString = newTag.getText().toString();

                //Call request method
                activityCallback.addTag(newTagString);

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();


    }

    public static void createAddFolderAlertDialog(Context context,final NetworkCallbackClass activityCallback){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Create folder");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.new_string_alert_dialog, null);
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText newFolder = (EditText) dialogView.findViewById(R.id.new_string_alert_dialog_text);
                String newFolderString = newFolder.getText().toString();

                //Call request method
                activityCallback.createFolder(newFolderString);

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();


    }


    public static void createFileRenameAlertDialog(Context context,final NetworkCallbackClass activityCallback){

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Rename");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.new_string_alert_dialog, null);
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText newName = (EditText) dialogView.findViewById(R.id.new_string_alert_dialog_text);
                String newNameString = newName.getText().toString();

                //Call request method
                activityCallback.renameFile(newNameString);

                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();


    }


}
