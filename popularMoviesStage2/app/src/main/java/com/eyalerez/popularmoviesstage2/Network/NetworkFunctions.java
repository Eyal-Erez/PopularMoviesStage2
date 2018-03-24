package com.eyalerez.popularmoviesstage2.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.eyalerez.popularmoviesstage2.DataBase.DB;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Erez_PC on 3/16/2018.
 */

public class NetworkFunctions {
    private final String TAG ="NetworkFunctions";
    private final String myPrivateAPI_KEY = "1edaa6fa70920634e17895cfc281f1e2";

    Retrofit retrofit = null;
    JSONRequestInterface request;
    private DB m_db;

    /*for network state */
    private Context mContext;
    private boolean mNetworkError;

    private String mSortMethod;
    private int mNextPageNumber = 1;
    private int mCurrentPageNumber = 0;
    private boolean currentlyFatchingPage = false;

/*    public NetworkFunctions(Context mContext, String mSortMethod, DB db) {
        this.mContext = mContext;
        this.mSortMethod = mSortMethod;
    }*/

    /* Functions */

    public NetworkFunctions(Context mainContext, String SortMethod, DB db){

        mContext = mainContext;

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/movie/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        request = retrofit.create(JSONRequestInterface.class);

        mSortMethod = SortMethod;
        m_db = db;

        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    public void start(){ fetchNextMoviesPage();}


    /*https://api.themoviedb.org/3/movie/top_rated?api_key=1edaa6fa70920634e17895cfc281f1e2&language=en-US&page=1*/
    public void fetchNextMoviesPage(){

        if (currentlyFatchingPage)
            return;

        if (!isOnline()) {
            mNetworkError = true;
            Log.d(TAG, "Error with the network, found in fetchNextMoviesPage()");
        }

        Call<PageStructure> call = request.getMoviesJSON(mSortMethod, myPrivateAPI_KEY, String.valueOf(mNextPageNumber));
        currentlyFatchingPage = true;

        call.enqueue(new Callback<PageStructure>() {
            @Override
            public void onResponse(Call<PageStructure> call, retrofit2.Response<PageStructure> response) {
                /*call the DB update Movies function*/
                m_db.updateMoviesGrid(response.body());
                currentlyFatchingPage = false;
                mCurrentPageNumber = mNextPageNumber;
                mNextPageNumber++;
            }

            @Override
            public void onFailure(Call<PageStructure> call, Throwable t) {
                Log.d(TAG,"Error while fetching movies");
            }
        });
    }

    /* https://api.themoviedb.org/3/movie/278/videos?api_key=1edaa6fa70920634e17895cfc281f1e2 */
    public void fetchMovieVideos(String id)
    {
        if (!isOnline()) {
            mNetworkError = true;
            Log.d(TAG, "Error with the network, found in fetchMovieVideos()");
        }

        Call<VideoStructure> call = request.getMovieVideos(id, myPrivateAPI_KEY);
        currentlyFatchingPage = true;

        call.enqueue(new Callback<VideoStructure>() {
            @Override
            public void onResponse(Call<VideoStructure> call, retrofit2.Response<VideoStructure> response) {
                /*call the DB update videos function*/
                m_db.handleVideosFromWeb(response.body());
            }

            @Override
            public void onFailure(Call<VideoStructure> call, Throwable t) {
                Log.d(TAG,"Error while fetching videos");
            }
        });
    }

    /* https://api.themoviedb.org/3/movie/278/reviews?api_key=1edaa6fa70920634e17895cfc281f1e2&page=1
    * !!!!for now i'm only downloading one page, even if there is more then one !!!!*/
    public void fetchMovieReviews(String id)
    {
        if (!isOnline()) {
            mNetworkError = true;
            Log.d(TAG, "Error with the network, found in fetchMovieReviews()");
        }

        Call<ReviewsStructure> call = request.getMovieReviews(id, myPrivateAPI_KEY);
        currentlyFatchingPage = true;

        call.enqueue(new Callback<ReviewsStructure>() {
            @Override
            public void onResponse(Call<ReviewsStructure> call, retrofit2.Response<ReviewsStructure> response) {
                /*call the DB update review function*/
                m_db.handleReviwesFromWeb(response.body());
            }

            @Override
            public void onFailure(Call<ReviewsStructure> call, Throwable t) {
                Log.d(TAG,"Error while fetching reviews");
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void setSortMethod(String SortMethod) {

        mSortMethod = SortMethod;
        mNextPageNumber = 1;
        mCurrentPageNumber = 0;
        fetchNextMoviesPage();
    }

}





























