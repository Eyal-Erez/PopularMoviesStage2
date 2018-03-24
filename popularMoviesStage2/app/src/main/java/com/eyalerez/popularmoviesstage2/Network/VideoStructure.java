package com.eyalerez.popularmoviesstage2.Network;

import java.util.ArrayList;

/**
 * Created by Erez_PC on 3/16/2018.
 */

public class VideoStructure {
    int id;
    ArrayList<videoResult> results;


    private class videoResult{
        String name;
        String site;
        String key; /*should be added with https://www.youtube.com/watch?v=<<key>> */
    }
}
