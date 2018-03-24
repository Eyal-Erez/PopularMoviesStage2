package com.eyalerez.popularmoviesstage2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.eyalerez.popularmoviesstage2.DataBase.DB;
import com.eyalerez.popularmoviesstage2.Network.MovieStructure;
import com.facebook.stetho.Stetho;

public class MainDisplay extends AppCompatActivity
        implements moviesGrid.ListItemClickListener{


    private DB db;
    private moviesGrid mAdapter;
    private RecyclerView mMoviesGrid;

    private final String sortMethod = "SortMethod";
    private final String TOP_RATED_METHOD = "top_rated";
    private final String POPULAR_METHOD = "popular";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display);

        Stetho.initializeWithDefaults(this);


        /*Using Shared Preferences*/
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        /*trying to get data from shared Preferences if there isn't use the defualt and store it */
        String sortByPopularMovies = sharedPref.getString(sortMethod, POPULAR_METHOD);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        db = DB.getInstance();
        db.init(this, sortByPopularMovies);


        /*Set RecyclerView */
        mMoviesGrid = (RecyclerView)findViewById(R.id.recyclerView_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mMoviesGrid.setLayoutManager(layoutManager);
        mMoviesGrid.setHasFixedSize(true);
        mAdapter = new moviesGrid(db, this);

        /*connect the DB with the RecyclerView adapter*/
        mMoviesGrid.setAdapter(mAdapter);

        db.setmMoviesGrid(mAdapter);

    }

    @Override
    public void onListItemClick(int itemClickedIndex) {
        MovieStructure tempMovieStruct = db.getMovieFromDB(itemClickedIndex);

        //String msg = tempMovieStruct.getTitle() + " clicked";
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Intent newActivityintent = new Intent(MainDisplay.this, MovieDetails.class);
        newActivityintent.putExtra(MovieDetails.INTENT_MOVIE_POSITION_IN_DB, tempMovieStruct);
        startActivity(newActivityintent);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    db.setmSortedMethod(POPULAR_METHOD);
                    return true;
                case R.id.navigation_dashboard:
                    db.setmSortedMethod(TOP_RATED_METHOD);
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

}
