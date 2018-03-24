package com.eyalerez.popularmoviesstage2.Network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Erez_PC on 3/16/2018.
 */

public interface JSONRequestInterface {
    @GET("{type}")
    Call<PageStructure> getMoviesJSON (@Path("type") String type, @Query("api_key") String api, @Query("page") String page);

    @GET("{id}/reviews")
    Call<ReviewsStructure> getMovieReviews (@Path("id") String type, @Query("api_key") String api);

    @GET("{id}/videos")
    Call<VideoStructure> getMovieVideos (@Path("id") String type, @Query("api_key") String api);



}
