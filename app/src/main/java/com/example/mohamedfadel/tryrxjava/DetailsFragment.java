package com.example.mohamedfadel.tryrxjava;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment implements TorrentAdapter.ListItemClickListener{

    private RecyclerView recyclerView;
    private TorrentAdapter torrentAdapter;
    private ImageView youtubeImage, background, cover;
    private TextView title, description, date, genre, languge;
    private Movie movie;
    private boolean start;

    public DetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        if (!start){
            ScrollView scrollView = rootView.findViewById(R.id.details_main_scroll);
            scrollView.setVisibility(View.VISIBLE);
            youtubeImage = rootView.findViewById(R.id.edt_youtube);
            cover = rootView.findViewById(R.id.edt_cover);
            background = rootView.findViewById(R.id.edt_back);
            title = rootView.findViewById(R.id.edt_title);
            description = rootView.findViewById(R.id.edt_details);
            date = rootView.findViewById(R.id.edt_date);
            genre = rootView.findViewById(R.id.edt_genre);
            languge = rootView.findViewById(R.id.edt_language);
            recyclerView = rootView.findViewById(R.id.edt_rv);

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            torrentAdapter = new TorrentAdapter(this);
            torrentAdapter.setTorrentList(movie.getTorrents());
            recyclerView.setAdapter(torrentAdapter);
            title.setText(movie.getTitle());
            description.setText(movie.getDescription());
            date.setText(movie.getDate());
            genre.setText(movie.getGenres().toString());
            languge.setText(movie.getLanguage());
            Picasso.get().load(movie.getCoverURL()).into(cover);
            Picasso.get().load(movie.getBackgroundURL()).into(background);

            youtubeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openMovieTrailer(movie.getTrailer());
                }
            });
        }
        return rootView;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    private void openMovieTrailer(String url){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.youtube.com")
                .appendPath("watch")
                .appendQueryParameter("v", url);


        Uri webpage = Uri.parse(builder.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.putExtra(SearchManager.QUERY, builder.toString());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        openWebPage(torrentAdapter.getTorrentList().get(clickedItemIndex).getUrl());
    }
}
