package com.example.margonari.fdrive;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
    private TextView progressBarPercentage;
    private LinearLayout progressBarLayout;

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
    public NetworkCallbackClass activityCallback;






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

        NavigationView.OnNavigationItemSelectedListener listener = new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawer(rightDrawerView);

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                }
                return false;
            }

        };

        rightDrawerView.setNavigationItemSelectedListener(listener);
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

        }
        return super.onOptionsItemSelected(item);
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
                floatingMenu.collapse();
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
            Uri selectedFile = data.getData();
            //Create typedFile to send
            ContentResolver contentResolver = this.getContentResolver();
            String fileType = contentResolver.getType(selectedFile);
            Cursor returnCursor = contentResolver.query(selectedFile, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();
            InputStream is = null;
            try {
                is = contentResolver.openInputStream(selectedFile);
            }catch(FileNotFoundException e){}
            TypedInputStream file = new TypedInputStream(returnCursor.getString(nameIndex), fileType, returnCursor.getLong(sizeIndex), is,activityCallback);

            RequestMaker.getInstance().uploadFile(activityCallback,file, "this is a file");
            progressBarLayout.setVisibility(View.VISIBLE);
            toggleUi(false);
        }
    }

    /*###########################################################################
    ###################     REQUESTS      #################################
    #############################################################################
     */

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
        folderCards.add(new FolderCard(".."));

        //Save folders in list
        for(int i = 0; i<folders.size(); i++){
            folderCards.add(new FolderCard(folders.get(i)));
        }

        //empty files
        fileCards = new ArrayList<>();
        for(int i = 0; i<files.size();i++){
            //gets every file in folder
            RequestMaker.getInstance().loadFile(activityCallback,email,token,files.get(i)); //The fileCards are loaded in success
            toggleUi(false);
        }
        toggleUi(true);
    }

    public void onLoadFileSuccess(FileMetadata file){
        totFiles--; //Substract one to know which file is it
        fileCards.add(new FileCard(file.name, file.extension, Integer.toString(20)));

        if(totFiles == 0){
            updateFileCards();
            updateFolderCards();
            toggleUi(true);
        }


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

    public void onUploadFileSuccess(String message){
        progressBarLayout.setVisibility(View.GONE);
        toggleUi(true);
    }

    public void onFileUploadProgress(final long progress){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                horizontalProgressBar.setProgress((int)progress);
                progressBarPercentage.setText(Long.toString(progress));
            }
        });

    }
}




