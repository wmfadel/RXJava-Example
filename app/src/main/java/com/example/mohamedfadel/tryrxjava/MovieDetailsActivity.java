package com.example.mohamedfadel.tryrxjava;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements ListFragment.OnListItemSelected{

    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        if (intent.hasExtra("movie")){
            movie = (Movie)intent.getSerializableExtra("movie");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setMovie(movie);
        fragmentManager.beginTransaction()
                .add(R.id.movie_details_container, detailsFragment)
                .commit();

    }


    @Override
    public void onMovieSelected(Movie movie) {

    }
}
