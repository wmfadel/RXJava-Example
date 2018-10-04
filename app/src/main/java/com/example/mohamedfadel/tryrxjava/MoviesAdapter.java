package com.example.mohamedfadel.tryrxjava;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>{

    private List<Movie> moviesList;
    final private ListItemClickListener mOnClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, genre, language, rate, generHolder, languageHolder, rateHolder;
        public ImageView cover;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.mnu_txt_title);
            genre = view.findViewById(R.id.mnu_txt_genres);
            language = view.findViewById(R.id.mnu_txt_language);
            rate = view.findViewById(R.id.mnu_txt_rate);
            generHolder = view.findViewById(R.id.mnu_txt_g_holder);
            languageHolder = view.findViewById(R.id.mnu_txt_l_holder);
            rateHolder = view.findViewById(R.id.mnu_txt_r_holder);
            cover = view.findViewById(R.id.mnu_list_cover);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }


    // Handling clicks
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MoviesAdapter( ListItemClickListener listener) {
        this.mOnClickListener = listener;
    }

    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
        if (moviesList != null)
            notifyDataSetChanged();
    }

    public void clear() {
        final int size = moviesList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                moviesList.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenres().toString());
        holder.language.setText(movie.getLanguage());
        holder.rate.setText(String.valueOf(movie.getRate()));
        Picasso.get().load(movie.getCoverURL()).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        if (moviesList == null)
            return 0;
        return moviesList.size();
    }
}
