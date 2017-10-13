package com.google.developer.bugmaster;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.developer.bugmaster.data.BugsContract;
import com.google.developer.bugmaster.data.BugsDbHelper;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {
    BugsDbHelper bugsDbHelper;
    SQLiteDatabase db;
    RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        mRecyclerView.setLayoutManager(linearLayoutManager);
        //mRecyclerView.setHasFixedSize(true);

        populateRecyclerView();

    }

    public void populateRecyclerView(){

        bugsDbHelper= new BugsDbHelper(this);
        db=bugsDbHelper.getWritableDatabase();
        Cursor cursor= getAllinsectData();

        InsectRecyclerAdapter insectRecyclerAdapter= new InsectRecyclerAdapter(this,cursor);
        mRecyclerView.setAdapter(insectRecyclerAdapter);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                //TODO: Implement the sort action
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Click events in Floating Action Button */
    @Override
    public void onClick(View v) {
        //TODO: Launch the quiz activity
    }
    public Cursor getAllinsectData(){
        return db.query(
                BugsContract.BugEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                BugsContract.BugEntry.COLUMN_NAME
        );
    }
}
