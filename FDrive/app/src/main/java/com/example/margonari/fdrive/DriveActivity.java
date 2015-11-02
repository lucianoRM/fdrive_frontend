package com.example.margonari.fdrive;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.margonari.fdrive.requests.RequestMaker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luciano on 13/10/15.
 */
public class DriveActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String drawerTitle;
    private List<FileCard> fileCards = new ArrayList<FileCard>(); //Where all file cards are stored
    private RecyclerView recyclerView;
    private static ProgressBar progressBar;
    private static String email,token,name,surname;
    private static SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);

        //Sets the progress bar as invisible
        progressBar = (ProgressBar) findViewById(R.id.driveProgressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);


        //Set drawer layout and shown email
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        TextView drawerEmail = (TextView)findViewById(R.id.email);
        drawerEmail.setText(email);

        //Instantiates preferences
        preferences = getSharedPreferences(getResources().getString(R.string.sharedConf), Context.MODE_PRIVATE);


        navigationView = (NavigationView)findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    case R.id.settings_button:
                        startActivity(new Intent(DriveActivity.this,ConfigurationActivity.class));
                }
                return false;
            }

        });


        setToolbar();

        setOnActionButtonClickListener();

        setUserInformation();

        //Initialize recyclerView for showing file cards
        this.recyclerView = (RecyclerView) findViewById(R.id.recycler_files_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(layoutManager);

        this.fileCards  = new ArrayList<>();
        fileCards.add(new FileCard("archivo1",".jpg","50kb"));
        fileCards.add(new FileCard("archivo2",".cpp","100kb"));
        fileCards.add(new FileCard("archivo3", ".txt", "200kb"));

        updateFileCards();



    }





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
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            getMenuInflater().inflate(R.menu.drive_action_bar_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }



    protected void pickFile(View view){

        ///Codigo que abre la galeria de imagenes y carga la imagen en displayedImage

        Intent intent = new Intent();
        intent.setType("file/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), 1);

    }

    //It's executed when leaving file system
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);

        if (reqCode == 1 && resCode == RESULT_OK && data != null) {
            Uri selectedFile = data.getData();
            RequestMaker.uploadFile(this,selectedFile,"this is a file");
        }
    }

    public void setOnActionButtonClickListener(){
        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.floatingButton);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pickFile(view);
            }
        });

    }

    private void updateFileCards(){
        CardAdapter adapter = new CardAdapter(this.fileCards);
        this.recyclerView.setAdapter(adapter);

    }









    private void toggleUi(boolean enable){
        if(enable){
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }else{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }



    //Sets the user information shown in the screen
    private void setUserInformation(){
        email = preferences.getString("email","email");
        name = preferences.getString("name","name");
        surname = preferences.getString("surname","surname");
        token = preferences.getString("token","token");


        //Show information
        TextView uText = (TextView)drawerLayout.findViewById(R.id.username);
        TextView eText = (TextView)drawerLayout.findViewById(R.id.email);

        uText.setText(name + " " + surname);
        eText.setText(email);

    }

}


