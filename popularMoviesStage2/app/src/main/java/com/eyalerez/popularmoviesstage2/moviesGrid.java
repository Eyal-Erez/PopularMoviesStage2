package com.eyalerez.popularmoviesstage2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.eyalerez.popularmoviesstage2.DataBase.DB;
import com.eyalerez.popularmoviesstage2.Network.MovieStructure;
import com.squareup.picasso.Picasso;

/**
 * Created by Erez_PC on 3/17/2018.
 */

public class moviesGrid extends RecyclerView.Adapter<moviesGrid.movieItemViewHolder>{

    private int maxPostionReach = 0;

    private final String moviePoster_WebPath = "https://image.tmdb.org/t/p/w185";/*for now it's w185, there also 92,154,185,342,500,780*/

    private DB m_db;

    final private ListItemClickListener  mOnClickListener;


    public moviesGrid(DB db, ListItemClickListener onClickListener){
        m_db = db;
        mOnClickListener = onClickListener;
    }

    @Override
    public moviesGrid.movieItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutID = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutID, parent, false);
        movieItemViewHolder movieViewHolder = new movieItemViewHolder(view);

        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(moviesGrid.movieItemViewHolder holder, int position) {
        MovieStructure movieStruct = m_db.getMovieFromDB(position);
        holder.bind(movieStruct.getPoster_path());
        Log.d("RecyclerView","postion = "+position);

        if (position > maxPostionReach)
            maxPostionReach = position;
        if (maxPostionReach+5> m_db.getNumOfMoviesInDB())
            m_db.fetchMoreMovies();

    }

    @Override
    public int getItemCount() {return m_db.getNumOfMoviesInDB();}

    public interface ListItemClickListener {
        void onListItemClick(int itemClickedIndex);
    }

    public class movieItemViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        ImageView movieItemImageView;
        private Context mContext;

        public movieItemViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            movieItemImageView = (ImageView)itemView.findViewById(R.id.rv_movieItem);
            itemView.setOnClickListener(this);

        }

        void bind(String imgPath)
        {
            String imagePath = moviePoster_WebPath + imgPath;
            Log.d("RecyclerView", "Image Path = " + imagePath);
            Picasso.with(mContext).load(imagePath).into(movieItemImageView);
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickPosition);
        }
    }


}
