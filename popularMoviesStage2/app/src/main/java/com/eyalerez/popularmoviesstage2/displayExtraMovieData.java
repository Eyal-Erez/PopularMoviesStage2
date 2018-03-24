package com.eyalerez.popularmoviesstage2;

import android.content.Intent;
import android.graphics.Point;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyalerez.popularmoviesstage2.DataBase.DB;
import com.eyalerez.popularmoviesstage2.Network.ReviewsStructure;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class displayExtraMovieData extends AppCompatActivity {

    public final static String INTENT_MOVIE_NAME = "movieName";
    private final String TAG = "displayExtraMovieData";

    private ListView lv;
    private TextView tv_movieName;
    private TextView tv_reviewContent;
    private Button backButton;


    private ArrayList<ReviewsStructure.ReviewStruct> reviews;
    private ArrayList<String> authors;
    private ArrayList<String> content;

    private int amountOfReviwes;
    private DB m_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_extra_movie_data);


        lv = (ListView)findViewById(R.id.lv_author);
        tv_movieName = (TextView)findViewById(R.id.movieNameExtraData);
        tv_reviewContent = (TextView)findViewById(R.id.tv_content);
        backButton = (Button)findViewById(R.id.expandExtraDataBackButton);

        /*Check if we want to show reviews or videos */
        if (true)
            showReviews();


    }

    private void showReviews()
    {
        String movieName="abcd1234";
        /*TODO:Get the name of the movie from movieDetails Activity via intant*/
        Intent movieNameFromMovieDetailActivity = getIntent();
        if (movieNameFromMovieDetailActivity.hasExtra(INTENT_MOVIE_NAME) )
            movieName = movieNameFromMovieDetailActivity.getStringExtra(INTENT_MOVIE_NAME);

        Log.d(TAG, "movie name: "+movieName);
        tv_movieName.setText(movieName);


        /*TODO(1):Get the reviews from DB and populate them to the ListView*/
        m_db = DB.getInstance();
        reviews = m_db.getReviewsFromDB();
        amountOfReviwes = m_db.getNumOfReviews();

        authors = new ArrayList<>();
        content = new ArrayList<>();

        for(ReviewsStructure.ReviewStruct review: reviews){
            authors.add(review.getAuthor());
            content.add(review.getContent());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                authors );

        /*TODO(2):Populate them to the ListView with onPress that will open the content in the text view */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(),"Click ListItem Number " + position, Toast.LENGTH_LONG).show();
                lv.setVisibility(View.GONE);
                sizeOfCloseButton(0.26);
                tv_reviewContent.setVisibility(View.VISIBLE);
                backButton.setVisibility(View.VISIBLE);
                tv_reviewContent.setText(content.get(position));
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lv.setVisibility(View.VISIBLE);
                sizeOfCloseButton(0);
                tv_reviewContent.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
                tv_reviewContent.setText("");
            }
        });
        lv.setAdapter(arrayAdapter);


    }

    private void sizeOfCloseButton(double butSize)
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        ScrollView mylout = (ScrollView) findViewById(R.id.sv_reviewContent);
        mylout.getLayoutParams().height = height - ((int)(height*butSize));
    }
}
