package com.eyalerez.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyalerez.popularmoviesstage2.DataBase.DB;
import com.eyalerez.popularmoviesstage2.Network.MovieStructure;
import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    public final static String INTENT_MOVIE_POSITION_IN_DB = "movieInDB";

    private final String moviePoserBaseURL = "https://image.tmdb.org/t/p/w185";

    private final String TAG = "movieDetail";

    private TextView title;
    private TextView releaseYear;
    private ImageView moviePoster;
    private TextView voteAvg;
    private TextView plotDiscription;

    private String movieID;

    private DB m_db;

    private Button reviewButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        context = this;

        /* dispaly back button and make it close this activity and back to the main activity */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = (TextView)findViewById(R.id.movieTitle);
        releaseYear = (TextView)findViewById(R.id.movieDate);
        moviePoster = (ImageView)findViewById(R.id.moviePoster);
        voteAvg = (TextView)findViewById(R.id.movieRating);
        plotDiscription = (TextView)findViewById(R.id.moviePlot);
        reviewButton = (Button)findViewById(R.id.reviewsButton);


        Intent movieInDB = getIntent();
        MovieStructure movieData =null;

        if (movieInDB.hasExtra(INTENT_MOVIE_POSITION_IN_DB) )
            movieData = (MovieStructure)movieInDB.getParcelableExtra(INTENT_MOVIE_POSITION_IN_DB);

        /*debug that info was received */
        if (movieData != null)
            Log.d(TAG,"title = "+ movieData.getTitle());

        if(movieData != null) {
            title.setText(movieData.getTitle());
            releaseYear.setText(movieData.getRelease_date());
            voteAvg.setText("Vote Average: "+ movieData.getVote_average() + "/10");
            Picasso.with(this).load("https://image.tmdb.org/t/p/w342" + movieData.getPoster_path()).into(moviePoster);
            plotDiscription.setText(movieData.getOverview());
            movieID = movieData.getMovieID();
        }

        /*Download the reviews to the DB in the background */
        m_db = DB.getInstance();
        m_db.fetchMovieReviews(movieID);


        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (m_db.getNumOfReviews() > 0 ){
                    Intent displayReviews = new Intent(MovieDetails.this,displayExtraMovieData.class);
                    displayReviews.putExtra(displayExtraMovieData.INTENT_MOVIE_NAME,title.getText());
                    startActivity(displayReviews);
                }
                else{
                    Toast.makeText(context,"No reviews were found",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
