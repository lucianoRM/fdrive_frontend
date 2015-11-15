package com.example.margonari.fdrive;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.IntegerRes;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.margonari.fdrive.requests.Answers.GetUserFilesAnswer;
import com.example.margonari.fdrive.requests.RequestMaker;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by luciano on 13/10/15.
 */


public class DriveActivity extends AppCompatActivity implements NetworkCallbackClass.NetworkCallback {

    private static int GENERAL_UPLOAD = 1;
    private static int UPLOAD_PROFILE_PICTURE = 2;
    private static int UPLOAD_NEW_VERSION = 3;


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
    private String lastLocation = "0.0,0.0";


    //Aux
    private Context context;
    private View view;
    private Path path;
    private FileCard selectedFileCard;
    public NetworkCallbackClass activityCallback;
    public TypedInputStream fileToUpload;
    private boolean afterSearch;
    private Map<Integer,String> searchMap;





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

        //Sets locationListener
        setLocationListener();

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
                    case R.id.location_button:
                        WebView wv = (WebView)findViewById(R.id.test_web_view);
                        wv.loadUrl("http://maps.google.com/maps?q=" + lastLocation);
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

        CircleImageView circleImageView = (CircleImageView) leftDrawerView.findViewById(R.id.circle_image);
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                //needed for image persistence
                final int takeFlags = intent.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
// Check for the freshest data.
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), UPLOAD_PROFILE_PICTURE);
            }
        });


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
                        AlertDialogManager.createFileRenameAlertDialog(context,activityCallback);
                        break;
                    case R.id.right_drawer_download_button:
                        FileMetadata metadata = selectedFileCard.metadata;
                        RequestMaker.getInstance().downloadFile(activityCallback,email,token,metadata.id,metadata.name,metadata.extension);
                        onFileDownloadToggleUI(false);
                        break;
                    case R.id.right_drawer_share_button:
                        RequestMaker.getInstance().getUsers(activityCallback,email,token);
                        break;
                    case R.id.right_drawer_upload_button:
                        uploadNewVersion();
                        break;
                    case R.id.right_drawer_add_tag_button:
                        AlertDialogManager.createAddTagAlertDialog(context,activityCallback);
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

        String uriString = Database.getInstance().get(email,"");
        if(!uriString.equals("")){
            Uri imageUri = Uri.parse(uriString);
            CircleImageView circleImageView = (CircleImageView) leftDrawerView.findViewById(R.id.circle_image);

            circleImageView.setImageURI(imageUri);
        }


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

                //sets selected file card and gets information
                selectedFileCard = fileCards.get(position);
                FileMetadata metadata = selectedFileCard.metadata;
                ImageView clickedFileIcon = (ImageView) view.findViewById(R.id.extension_photo);

                //Gets right drawer fields to edit
                TextView rightDrawerCardFileName = (TextView) findViewById(R.id.right_drawer_file_name);
                TextView rightDrawerCardFileSize = (TextView) findViewById(R.id.right_drawer_file_size);
                ImageView rightDrawerCardExtensionPhoto = (ImageView) findViewById(R.id.right_drawer_extension_photo);
                TextView rightDrawerMetadataFileName = (TextView) findViewById(R.id.right_drawer_metadata_file_name);
                TextView rightDrawerMetadataFileExtension = (TextView) findViewById(R.id.right_drawer_metadata_file_extension);
                TextView rightDrawerMetadataLastUser = (TextView) findViewById(R.id.right_drawer_metadata_file_last_user);
                TextView rightDrawerMetadataLastMod = (TextView) findViewById(R.id.right_drawer_metadata_last_mod);
                TextView rightDrawerMetadataOwner = (TextView) findViewById(R.id.right_drawer_metadata_file_owner);
                TextView rightDrawerMetadataVersion = (TextView) findViewById(R.id.right_drawer_metadata_version);
                TextView rightDrawerMetadataSize = (TextView) findViewById(R.id.right_drawer_metadata_file_size);
                TextView rightDrawerMetadataPath = (TextView) findViewById(R.id.right_drawer_metadata_file_path);
                Spinner rightDrawerMetadataShared = (Spinner) findViewById(R.id.right_drawer_metadata_shared);
                Spinner rightDrawerMetadataTags = (Spinner) findViewById(R.id.right_drawer_metadata_tags);


                rightDrawerCardExtensionPhoto.setImageDrawable(clickedFileIcon.getDrawable());
                rightDrawerCardFileName.setText(metadata.name);
                rightDrawerCardFileSize.setText(Integer.toString(metadata.size));

                rightDrawerMetadataFileExtension.setText(metadata.extension);
                rightDrawerMetadataFileName.setText(metadata.name);
                rightDrawerMetadataLastMod.setText(metadata.lastModified);
                rightDrawerMetadataLastUser.setText(metadata.lastUser);
                rightDrawerMetadataOwner.setText(metadata.owner);
                rightDrawerMetadataPath.setText(selectedFileCard.path);
                rightDrawerMetadataSize.setText(Integer.toString(metadata.size));
                rightDrawerMetadataVersion.setText(Integer.toString(metadata.lastVersion));
                rightDrawerMetadataTags.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, metadata.tags));
                rightDrawerMetadataShared.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, metadata.users));

                //Set buttons
                ImageButton editButton = (ImageButton) findViewById(R.id.right_drawer_edit_button);
                ImageButton deleteButtin = (ImageButton) findViewById(R.id.right_drawer_delete_button);

                if (!(metadata.owner.equals(email))) {//No es el owner del archivo
                    editButton.setClickable(false);
                } else {
                    editButton.setClickable(true);
                }

                drawerLayout.openDrawer(rightDrawerView);

            }

        }));

        this.recyclerFoldersView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                TextView clickedFolder = (TextView) view.findViewById(R.id.folder_name);
                RequestMaker.getInstance().getUserFiles(activityCallback, email, token, path.goTo(clickedFolder.getText().toString()));
                toggleUi(false);

            }

        }));


    }

    private void setLocationListener(){

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastLocation = Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(String provider) {}

            @Override
            public void onProviderDisabled(String provider) {}
        };


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }catch (SecurityException e) {};


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
                AlertDialogManager.createSearchAlertDialog(context,activityCallback);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public void addTag(String newTag){

        RequestMaker.getInstance().addTag(activityCallback, email, token, selectedFileCard.metadata.id, newTag);
    }


    public void search(String selectedType,String element){
        path.goTo("search");
        RequestMaker.getInstance().search(activityCallback, email, token, selectedType.toLowerCase(), element);

    }

    public void share(List<String> selectedUsers){
        RequestMaker.getInstance().shareFile(activityCallback, email, token, selectedFileCard.metadata.id, selectedUsers);

    }

    public void createFolder(String newFolder){
        RequestMaker.getInstance().createFolder(activityCallback, email, token, path.toAbsolutePath(), newFolder);

    }

    public void renameFile(String newName){
        RequestMaker.getInstance().renameFile(activityCallback, email, token, selectedFileCard.metadata.id, newName);

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
                AlertDialogManager.createAddFolderAlertDialog(context, activityCallback);
                floatingMenu.collapse();
            }
        });

    }


    private void uploadNewVersion(){

        ///Codigo que abre la galeria de imagenes y carga la imagen en displayedImage
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), UPLOAD_NEW_VERSION);

    }

    protected void pickFile(View view){

        ///Codigo que abre la galeria de imagenes y carga la imagen en displayedImage
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), GENERAL_UPLOAD);


    }

    //It's executed when leaving file system
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);
        //Uploading file
        if (reqCode == GENERAL_UPLOAD && resCode == RESULT_OK && data != null) {
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

        //Updating user image
        if (reqCode == UPLOAD_PROFILE_PICTURE && resCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            //Persisit image
            Database.getInstance().put(email, uri.toString());

            CircleImageView circleImageView = (CircleImageView) leftDrawerView.findViewById(R.id.circle_image);
            circleImageView.setImageURI(uri);

        }
        //Updating user image
        if (reqCode == UPLOAD_NEW_VERSION && resCode == RESULT_OK && data != null){
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

                RequestMaker.getInstance().saveNewVersion(activityCallback, email, token, fileName, "." + fileExtension, selectedFileCard.metadata.id, path.toAbsolutePath(), returnCursor.getLong(sizeIndex), selectedFileCard.metadata.tags, selectedFileCard.metadata.lastVersion);

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
        afterSearch = false;
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
        fileCards.add(new FileCard(file,email));

        if(totFiles == 0){
            if(afterSearch){ //Needs to overwrite paths
                for(int i = 0;i<fileCards.size();i++){
                    FileCard card = fileCards.get(i);
                    String searchedPath = searchMap.get(i);
                    card.path = searchedPath;
                }
            }
            updateFileCards();
            updateFolderCards();
            toggleUi(true);
        }


    }

    //When uploading a new file
    public void onSaveFileSuccess(int id){
        onFileUploadToggleUI(false);
        RequestMaker.getInstance().uploadFile(activityCallback, fileToUpload, email, token, id);
    }

    //When updating a file
    public void onMetadataUploadSuccess(){
       getUserFiles();
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

    public void onSearchSuccess(Map<Integer,String> files){
        afterSearch = true;
        searchMap = files;
        totFiles = files.size();
        //Empty previous folders
        folderCards = new ArrayList<>();
        //add return folder
        folderCards.add(new FolderCard(".."));

        //empty files
        fileCards = new ArrayList<>();

        for (Integer key : files.keySet()) {
            //gets every file in folder
            RequestMaker.getInstance().getFile(activityCallback,email,token,key); //The fileCards are loaded in success
            toggleUi(false);
        }
        if(files.size() == 0){
            updateFileCards();
            updateFolderCards();
        }
        toggleUi(true);
    }

    public void onGetUsersForSharingSuccess(List<String> users){
        AlertDialogManager.createShareAlertDialog(activityCallback, context, users);
    }

    public void onShareSuccess(){
        getUserFiles();
    }

    public void onNewVersionSaveSuccess(){
        onFileUploadToggleUI(false);
        RequestMaker.getInstance().uploadFile(activityCallback, fileToUpload, email, token,selectedFileCard.metadata.id);
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




