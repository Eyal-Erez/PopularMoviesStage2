package com.eyalerez.popularmoviesstage2.DataBase;

import android.content.Context;
import android.util.Log;

import com.eyalerez.popularmoviesstage2.Network.MovieStructure;
import com.eyalerez.popularmoviesstage2.Network.NetworkFunctions;
import com.eyalerez.popularmoviesstage2.Network.PageStructure;
import com.eyalerez.popularmoviesstage2.Network.ReviewsStructure;
import com.eyalerez.popularmoviesstage2.Network.VideoStructure;
import com.eyalerez.popularmoviesstage2.moviesGrid;

import java.util.ArrayList;

/**
 * Created by Erez_PC on 3/16/2018.
 */

public class DB {

    private String mSortedMethod;
    private Context mContext;
    private NetworkFunctions nf;
    private moviesGrid mMoviesGrid;

    private ArrayList<MovieStructure> moviesDB = null;
    private int numOfMoviesInDB;


    /*reviews*/
    private ArrayList<ReviewsStructure.ReviewStruct> reviewsDB = null;
    private int numOfReviews;

    private static DB instance=null;

    protected DB(){}

    public static DB getInstance(){
        if (instance==null)
            instance = new DB();

        return instance;
    }



    public void init(Context context, String sortedMethod){
        mContext = context;
        mSortedMethod = sortedMethod;
        moviesDB = new ArrayList<MovieStructure>();
        numOfMoviesInDB = 0;
        nf = new NetworkFunctions(context, sortedMethod, this);
        //mMoviesGrid = mg;
        nf.fetchNextMoviesPage();
    }

    public void setmSortedMethod(String mSortedMethod) {

        Log.d("eyal", "setmSortedMethod:"+mSortedMethod +" mSortedMethod="+mSortedMethod );

        if (this.mSortedMethod == mSortedMethod)
            return;

        /*new sorting method */
        this.mSortedMethod = mSortedMethod;
        numOfMoviesInDB = 0;
        moviesDB.clear();
        mMoviesGrid.notifyDataSetChanged();

        nf.setSortMethod(mSortedMethod);
    }

    public void fetchMoreMovies()
    {
        nf.fetchNextMoviesPage();
    }


    public void updateMoviesGrid(PageStructure page)
    {
        int tempCounter = numOfMoviesInDB;
        numOfMoviesInDB +=page.results.size();
        moviesDB.addAll(page.results);

        //mMoviesGrid.notifyDataSetChanged();
        mMoviesGrid.notifyItemRangeInserted(tempCounter+1, page.results.size());
    }


    /* Reviews */
    public void fetchMovieReviews(String id)
    {
        nf.fetchMovieReviews(id);
    }
    public ArrayList<ReviewsStructure.ReviewStruct> getReviewsFromDB() { return  reviewsDB;}
    public int getNumOfReviews() { return numOfReviews;}

    public void handleReviwesFromWeb(ReviewsStructure reviews)
    {
        /*need to display them in the UI*/
        if (reviewsDB !=null)
            reviewsDB.clear();

        reviewsDB = reviews.results;
        numOfReviews = reviews.total_results;
    }


    /* Videos */
    public void handleVideosFromWeb(VideoStructure vid)
    {
        /*need to display them in the UI*/

    }

    public MovieStructure getMovieFromDB(int position) {
        return moviesDB.get(position);
    }

    public int getNumOfMoviesInDB() {
        return numOfMoviesInDB;
    }

    public void setmMoviesGrid(moviesGrid mMoviesGrid) {
        this.mMoviesGrid = mMoviesGrid;
    }
}
