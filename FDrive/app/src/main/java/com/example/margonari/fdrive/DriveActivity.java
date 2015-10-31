package com.example.margonari.fdrive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.margonari.fdrive.requests.RequestAnswer;
import com.example.margonari.fdrive.requests.RequestMaker;
import com.example.margonari.fdrive.requests.ServiceGenerator;
import com.example.margonari.fdrive.requests.UserSignUpService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by luciano on 13/10/15.
 */
public class DriveActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private String drawerTitle;
    private List<FileCard> fileCards = new ArrayList<FileCard>(); //Where all file cards are stored
    private RecyclerView recyclerView;
    private static ProgressBar progressBar;
    public static String email = "",token = "";

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

        setToolbar();

        setOnActionButtonClickListener();

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload"),1);

    }

    //It's executed when leaving file system
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data){
        super.onActivityResult(reqCode, resCode, data);

        if (reqCode == 1 && resCode == RESULT_OK && data != null) {
            Uri selectedFile = data.getData();
            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            toolbar.setTitle(selectedFile.toString());
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


}


