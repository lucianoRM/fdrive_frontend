package com.example.margonari.fdrive;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;
import com.example.margonari.fdrive.requests.RequestMaker;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciano on 13/10/15.
 */
public class DriveActivity extends AppCompatActivity implements NetworkCallbackClass.NetworkCallback {

    //View
    private DrawerLayout drawerLayout;
    private NavigationView leftDrawerView;
    private NavigationView rightDrawerView;
    private ProgressBar progressBar;
    private RecyclerView recyclerFilesView,recyclerFoldersView;
    private ProgressBar horizontalProgressBar;
    private TextView progressBarPercentage,progressBarMethod;
    private LinearLayout progressBarLayout;
    private com.getbase.floatingactionbutton.FloatingActionButton uploadFileButton;

    //Cards
    private List<FileCard> fileCards = new ArrayList<FileCard>(); //Where all file cards are stored
    private List<FolderCard> folderCards = new ArrayList<FolderCard>();



    //Info
    private int totFiles = 0;
    private String email,token,name,surname;


    //Aux
    private Context context;
    private View view;
    private Path path;
    private FileCard selectedFileCard;
    public NetworkCallbackClass activityCallback;
    public TypedInputStream fileToUpload;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        //Sets the progress bar as invisible
        progressBar = (ProgressBar) findViewById(R.id.drive_circular_progress_bar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);

        horizontalProgressBar = (ProgressBar) findViewById(R.id.drive_horizontal_progress_bar);
        progressBarLayout = (LinearLayout) findViewById(R.id.progress_bar_layout);
        progressBarPercentage = (TextView) findViewById(R.id.horizontal_progress_bar_percentage);
        progressBarMethod = (TextView) findViewById(R.id.horizontal_progress_bar_method);
        progressBarLayout.setVisibility(View.GONE);

        //Set drawer layout and shown email
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        TextView drawerEmail = (TextView)findViewById(R.id.email);
        drawerEmail.setText(email);


        leftDrawerView = (NavigationView)findViewById(R.id.left_drawer_view);
        rightDrawerView = (NavigationView)findViewById(R.id.right_drawer_view);

        //Disable right drawer gestures
        configRightDrawerGestures();


        //Set drawer listener
        setLeftDrawerListener();
        setRightDrawerListener();

        //Enable action bar
        setToolbar();

        //Set floating button listener
        uploadFileButton = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_file_floating_button);
        setOnActionButtonClickListener();

        //Display user information
        setUserInformation();

        //Initialize recyclerView for showing file cards
        this.recyclerFilesView = (RecyclerView) findViewById(R.id.recycler_files_view);
        LinearLayoutManager filesLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerFilesView.setLayoutManager(filesLayoutManager);

        //Initialize recyclerView for showing file cards
        this.recyclerFoldersView = (RecyclerView) findViewById(R.id.recycler_folders_view);
        LinearLayoutManager foldersLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        this.recyclerFoldersView.setLayoutManager(foldersLayoutManager);

        //Sets the cards listener
        setCardsListeners();


        //Saves context
        context = this;
        view = this.findViewById(android.R.id.content);


        activityCallback = new NetworkCallbackClass(this);


        //Gets root information
        path = new Path(getResources().getString(R.string.root_folder));
        getUserFiles();



    }








    /*###########################################################################
    ###################     DRAWERS     #########################################
    #############################################################################
     */

    private void configRightDrawerGestures(){

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, rightDrawerView);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (drawerView == rightDrawerView) {
                    //enable gestures
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, rightDrawerView);
                }

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (drawerView == rightDrawerView) {
                    //disable gestures
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, rightDrawerView);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });


    }


    private void setLeftDrawerListener(){

        NavigationView.OnNavigationItemSelectedListener listener = new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawer(leftDrawerView);

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.settings_button:
                        startActivity(new Intent(DriveActivity.this, ConfigurationActivity.class));
                        return true;
                    case R.id.logout_button:
                        RequestMaker.getInstance().logout(activityCallback, email, token);
                        toggleUi(false);
                        return true;
                    case R.id.my_drive_button:
                        path = new Path(getResources().getString(R.string.root_folder));
                        getUserFiles();
                        return true;
                    case R.id.shared_whith_me_button:
                        path = new Path(getResources().getString(R.string.shared_folder));
                        getUserFiles();
                        return true;
                    case R.id.trash_folder_icon:
                        path = new Path(getResources().getString(R.string.trash_folder));
                        getUserFiles();
                        return true;
                }
                return false;
            }

        };
        leftDrawerView.setNavigationItemSelectedListener(listener);
    }


    private void setRightDrawerListener(){

        ImageButton deleteFile = (ImageButton)findViewById(R.id.right_drawer_delete_button);
        ImageButton shareFile = (ImageButton)findViewById(R.id.right_drawer_share_button);
        ImageButton uploadFile = (ImageButton)findViewById(R.id.right_drawer_upload_button);
        ImageButton editFile = (ImageButton)findViewById(R.id.right_drawer_edit_button);
        ImageButton dowloadFile = (ImageButton)findViewById(R.id.right_drawer_download_button);
        ImageButton addTag = (ImageButton) findViewById(R.id.right_drawer_add_tag_button);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Closing drawer on item click
                drawerLayout.closeDrawer(rightDrawerView);
                Log.d("test","Entra");

                //Check to see which item was being clicked and perform appropriate action
                switch (v.getId()) {
                    case R.id.right_drawer_delete_button:
                        deleteSelectedFile();
                        break;
                    case R.id.right_drawer_edit_button:
                        Log.d("test","Edit button");
                        break;
                    case R.id.right_drawer_download_button:
                        FileMetadata metadata = selectedFileCard.metadata;
                        RequestMaker.getInstance().downloadFile(activityCallback,email,token,metadata.id,metadata.name,metadata.extension);
                        onFileDownloadToggleUI(false);
                        break;
                    case R.id.right_drawer_share_button:
                        Log.d("test","Share button");
                        break;
                    case R.id.right_drawer_upload_button:
                        Log.d("test","Upload button");
                        break;
                    case R.id.right_drawer_add_tag_button:
                        Log.d("test","Add tag");
                        break;
                }
            }

        };

        deleteFile.setOnClickListener(listener);
        shareFile.setOnClickListener(listener);
        uploadFile.setOnClickListener(listener);
        dowloadFile.setOnClickListener(listener);
        editFile.setOnClickListener(listener);
        addTag.setOnClickListener(listener);

    }

     /*###########################################################################
    ###################     SHOWN INFORMATION     ################################
    #############################################################################
     */

    private void updateFileCards(){
        FileCardAdapter adapter = new FileCardAdapter(fileCards);
        recyclerFilesView.setAdapter(adapter);

    }

    private void updateFolderCards(){
        FolderCardAdapter adapter  = new FolderCardAdapter(folderCards);
        recyclerFoldersView.setAdapter(adapter);
    }




    private void toggleUi(boolean enable){
        if(enable){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(ProgressBar.VISIBLE);
        }
    }



    //Sets the user information shown in the screen
    private void setUserInformation(){
        email = Database.getInstance().get("email", "email");
        name = Database.getInstance().get("name","name");
        surname = Database.getInstance().get("surname","surname");
        token = Database.getInstance().get("token","token");


        //Show information
        TextView uText = (TextView)drawerLayout.findViewById(R.id.username);
        TextView eText = (TextView)drawerLayout.findViewById(R.id.email);

        uText.setText(name + " " + surname);
        eText.setText(email);

    }

    private void onFileUploadToggleUI(boolean enable){

        if(enable){
            //Hide progress bar
            progressBarLayout.setVisibility(View.GONE);
            //Enable floating action button
            uploadFileButton.setClickable(true);
        }else{
            //Set method
            progressBarMethod.setText("Uploading file: ");
            //Show progress bar
            progressBarLayout.setVisibility(View.VISIBLE);
            //Disable floating action button
            uploadFileButton.setClickable(false);
        }

    }

    private void onFileDownloadToggleUI(boolean enable){

        if(enable){
            //Hide progress bar
            progressBarLayout.setVisibility(View.GONE);

        }else{
            //Set method
            progressBarMethod.setText("Downloading file: ");

            //Show progress bar
            progressBarLayout.setVisibility(View.VISIBLE);

        }

    }


    private void setCardsListeners(){
        this.recyclerFilesView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                //Reads from clicked fileCard
                TextView clickedFileName = (TextView) view.findViewById(R.id.file_name);
                TextView clickedFileSize = (TextView) view.findViewById(R.id.file_size);
                ImageView clickedFileIcon = (ImageView) view.findViewById(R.id.extension_photo);

                //Writes to drawer file card
                TextView rightDrawerFileName = (TextView) findViewById(R.id.right_drawer_file_name);
                TextView rightDrawerFileSize = (TextView) findViewById(R.id.right_drawer_file_size);
                ImageView rightDrawerExtensionPhoto = (ImageView) findViewById(R.id.right_drawer_extension_photo);

                rightDrawerExtensionPhoto.setImageDrawable(clickedFileIcon.getDrawable());
                rightDrawerFileName.setText(clickedFileName.getText().toString());
                rightDrawerFileSize.setText(clickedFileSize.getText().toString());

                //sets selected file card
                selectedFileCard = fileCards.get(position);

                drawerLayout.openDrawer(rightDrawerView);

            }

        }));

        this.recyclerFoldersView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                TextView clickedFolder = (TextView) view.findViewById(R.id.folder_name);
                RequestMaker.getInstance().getUserFiles(activityCallback,email,token,path.goTo(clickedFolder.getText().toString()));
                toggleUi(false);

            }

        }));


    }



     /*###########################################################################
    ###################     TOOLBAR      #########################################
    #############################################################################
     */

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.drive_action_bar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            // Poner ícono del drawer toggle
            ab.setHomeAsUpIndicator(R.mipmap.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerLayout.isDrawerOpen(leftDrawerView)) {
            getMenuInflater().inflate(R.menu.drive_action_bar_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(leftDrawerView);
                return true;
            case R.id.search_action_bar_menu:
                createSearchDialog();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private void createSearchDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.search_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        setupSearchDialogButtonListener(dialogView, alertDialog);
        alertDialog.show();


    }

    private void setupSearchDialogButtonListener(final View dialogView,final AlertDialog alertDialog){

        Button alertDialogSearchButton = (Button) dialogView.findViewById(R.id.search_alert_dialog_button);
        alertDialogSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner searchTypeOptions = (Spinner) dialogView.findViewById(R.id.search_alert_dialog_spinner);
                EditText searchElement = (EditText) dialogView.findViewById(R.id.search_alert_dialog_text);
                String searchSelectedType = searchTypeOptions.getSelectedItem().toString();
                String element = searchElement.getText().toString();
                path.goTo("search");
                RequestMaker.getInstance().search(activityCallback,email,token,searchSelectedType.toLowerCase(),element);
                alertDialog.hide();


            }
        });
    }

     /*###########################################################################
    ###################     FLOATING BUTTON      #################################
    #############################################################################
     */

    public void setOnActionButtonClickListener(){
        final FloatingActionsMenu floatingMenu = (FloatingActionsMenu) findViewById(R.id.add_file_folder_floating_button);
        FloatingActionButton addFileButton = (FloatingActionButton) findViewById(R.id.add_file_floating_button);
        final FloatingActionButton addFolderButton = (FloatingActionButton) findViewById(R.id.add_folder_floating_button);
        addFileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pickFile(view);
                floatingMenu.collapse();

            }
        });
        addFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFolderDialog();
                floatingMenu.collapse();
            }
        });

    }

    private void createNewFolderDialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_folder_alert_dialog, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        setupCreateFolderAlertDialogListener(dialogView, alertDialog);
        alertDialog.show();

    }

    private void setupCreateFolderAlertDialogListener(final View dialogView,final AlertDialog alertDialog){

        Button alertDialogSearchButton = (Button) dialogView.findViewById(R.id.create_folder_alert_dialog_button);
        alertDialogSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newNameEditText = (EditText) dialogView.findViewById(R.id.create_folder_alert_dialog_text);
                String newName = newNameEditText.getText().toString();
                RequestMaker.getInstance().createFolder(activityCallback,email,token,path.toAbsolutePath(),newName);
                alertDialog.hide();


            }
        });
    }


    protected void pickFile(View view){

        ///Codigo que abre la galeria de imagenes y carga la imagen en displayedImage

        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), 1);

    }

    //It's executed when leaving file system
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);
        if (reqCode == 1 && resCode == RESULT_OK && data != null) {
            //Get selected uri
            Uri selectedFile = data.getData();
            //Get file information and create typedFile to send
            ContentResolver contentResolver = this.getContentResolver();
            String fileType = contentResolver.getType(selectedFile);
            Cursor returnCursor = contentResolver.query(selectedFile, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            String completeFileName = returnCursor.getString(nameIndex);
            String fileName = completeFileName.split("\\.")[0];
            String fileExtension = completeFileName.split("\\.")[1];
            InputStream is = null;
            try {
                is = contentResolver.openInputStream(selectedFile);
            }catch(FileNotFoundException e){}
            fileToUpload = new TypedInputStream(fileName, fileType, returnCursor.getLong(sizeIndex),is,activityCallback);

            RequestMaker.getInstance().saveFile(activityCallback, email, token, fileName, "."+fileExtension, email,returnCursor.getLong(sizeIndex),path.toAbsolutePath());
        }
    }

    /*###########################################################################
    ###################     REQUESTS      #################################
    #############################################################################
     */

    private void deleteSelectedFile(){
        int id = selectedFileCard.metadata.id;
        String actualPath = path.toAbsolutePath();
        RequestMaker.getInstance().deleteFile(activityCallback,email,token,actualPath,id);
        toggleUi(false);
    }

    private void getUserFiles(){

        RequestMaker.getInstance().getUserFiles(activityCallback, email, token, path.toAbsolutePath());
        toggleUi(false);
    }



     /*###########################################################################
    ###################     AFTER REQUESTS      #################################
    #############################################################################
     */

    public void onGetUserFilesSuccess(GetUserFilesAnswer answer){

        List<Integer> files = answer.content.files;
        List<String> folders = answer.content.folders;

        totFiles = files.size();

        //Empty previous folders
        folderCards = new ArrayList<>();
        //add return folder
        folderCards.add(new FolderCard(".."));

        //Save folders in list
        for(int i = 0; i<folders.size(); i++){
            folderCards.add(new FolderCard(folders.get(i)));
        }

        //empty files
        fileCards = new ArrayList<>();
        for(int i = 0; i<files.size();i++){
            //gets every file in folder
            RequestMaker.getInstance().getFile(activityCallback,email,token,files.get(i)); //The fileCards are loaded in success
            toggleUi(false);
        }
        if(files.size() == 0){
            updateFileCards();
            updateFolderCards();
        }
        toggleUi(true);
    }

    public void onGetFileSuccess(FileMetadata file){
        totFiles--; //Substract one to know which file is it
        fileCards.add(new FileCard(file));

        if(totFiles == 0){
            updateFileCards();
            updateFolderCards();
            toggleUi(true);
        }


    }

    public void onSaveFileSuccess(int id){
        onFileUploadToggleUI(false);
        RequestMaker.getInstance().uploadFile(activityCallback, fileToUpload, email, token, id);
    }

    public void onLogoutSuccess(){
        //Erase token from "persistence" db

        Database.getInstance().put("token", "");
        toggleUi(true);
        finish();

    }

    public void onRequestFailure(List<String> errors){

        ErrorDisplay.getInstance().showMessages(context, view, errors);
        toggleUi(true);

    }

    public void onConnectionError(){
        ErrorDisplay.getInstance().showMessage(context, view, "Connection error,check configured ip or try again later");
        toggleUi(true);
    }

    public void onUploadFileSuccess(){
        onFileUploadToggleUI(true);
        ErrorDisplay.getInstance().showMessage(context, view, "Upload Successful");
        getUserFiles();
    }


    public void onDeleteFileSuccess(){
        ErrorDisplay.getInstance().showMessage(context, view, "File deleted");
        getUserFiles();
    }


    public void onCreateFolderSuccess(){
        getUserFiles();
    }

    public void onSearchSuccess(List<Integer> files){
        totFiles = files.size();
        //Empty previous folders
        folderCards = new ArrayList<>();
        //add return folder
        folderCards.add(new FolderCard(".."));

        //empty files
        fileCards = new ArrayList<>();
        for(int i = 0; i<files.size();i++){
            //gets every file in folder
            RequestMaker.getInstance().getFile(activityCallback,email,token,files.get(i)); //The fileCards are loaded in success
            toggleUi(false);
        }
        if(files.size() == 0){
            updateFileCards();
            updateFolderCards();
        }
        toggleUi(true);
    }

    public void onFileUploadProgress(final long progress){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                horizontalProgressBar.setProgress((int) progress);
                progressBarPercentage.setText(Long.toString(progress));
            }
        });

    }

    public void onFileDownloadSuccess(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ErrorDisplay.getInstance().showMessage(context, view, "Download Complete");
                onFileDownloadToggleUI(true);
            }
        });

    }

    public void onFileDownloadProgress(final long progress){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                horizontalProgressBar.setProgress((int)progress);
                progressBarPercentage.setText(Long.toString(progress));
            }
        });

    }




}




