package com.google.developer.bugmaster;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.developer.bugmaster.data.BugsContract;
import com.google.developer.bugmaster.data.BugsDbHelper;
import com.google.developer.bugmaster.data.InsectRecyclerAdapter;
import com.google.developer.bugmaster.reminders.AlarmReceiver;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener ,LoaderManager.LoaderCallbacks<Cursor>,SharedPreferences.OnSharedPreferenceChangeListener{
    BugsDbHelper bugsDbHelper;


    SQLiteDatabase db;
    RecyclerView mRecyclerView;
    public static final String SORT_QUERY = "sort";
    private Parcelable mListState = null;
    public static  final String INSECT_ID= "insectId";
    private SharedPreferences mSharedPreferences;
    private static final int LOADER_ID_INSECTS = 1;
    private String sortOrder;
    IntentFilter intentFilter;
    AlarmReceiver alarmReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);


        intentFilter = new IntentFilter();
        alarmReceiver = new AlarmReceiver();
        intentFilter.addAction(Intent.ACTION_BOOT_COMPLETED);

        sortOrder = mSharedPreferences.getString(SORT_QUERY, BugsContract.BugEntry.COLUMN_NAME);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_QUERY)) {
                sortOrder = savedInstanceState.getString(SORT_QUERY);
            }
        }
        bugsDbHelper= new BugsDbHelper(this);
        db=bugsDbHelper.getWritableDatabase();

        populateRecyclerView(sortOrder);
        getSupportLoaderManager().initLoader(LOADER_ID_INSECTS, null, this);
    }

    public void populateRecyclerView(String sortOrder){


        Cursor cursor= getAllinsectData(sortOrder);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
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
                /*switch (sortOrder) {
                    case Constants.SORT_NAME:
                        sortOrder = Constants.DANGER_LEVEL;
                        break;
                    case Constants.DANGER_LEVEL:
                        sortOrder = Constants.SORT_NAME;
                        break;
                    default:
                        sortOrder = Constants.SORT_NAME;
                }*/
                getAllinsectData(sortOrder);
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
    public Cursor getAllinsectData(String sortOrder){
        return db.query(
                BugsContract.BugEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
               // BugsContract.BugEntry.COLUMN_NAME
                sortOrder
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(alarmReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(alarmReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }
}
