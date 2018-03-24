package com.eyalerez.popularmoviesstage2.Network;

import java.util.ArrayList;

/**
 * Created by Erez_PC on 3/16/2018.
 */

public class ReviewsStructure {

    public int movieID;
    public int page;
    public ArrayList<ReviewStruct> results;
    public int total_pages;
    public int total_results;

    public class ReviewStruct {
        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        String author;
        String content;
    }


}
