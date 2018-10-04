package com.example.mohamedfadel.tryrxjava;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListItemSelected{

    private boolean mTowPane;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#1a6eff\">" + "Recent movies" + "</font>"));


        fragmentManager = getSupportFragmentManager();
        if (findViewById(R.id.master_linear_layout) != null){
            mTowPane = true;
            ListFragment listFragment = new ListFragment();
            listFragment.setmTwoPane(mTowPane);
            fragmentManager.beginTransaction()
                    .add(R.id.master_list, listFragment)
                    .commit();

            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setStart(true);
            fragmentManager.beginTransaction()
                    .replace(R.id.master_movie_details, detailsFragment)
                    .commit();

        }else {
            mTowPane = false;
        }
    }

    // checks if there is network available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        if (!isNetworkAvailable()){
            invalidateOptionsMenu();
            MenuItem item = menu.findItem(R.id.menu_start_search_activity);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == R.id.menu_start_search_activity){
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return true;
        }else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (mTowPane){
            DetailsFragment mDetailsFragment = new DetailsFragment();
            mDetailsFragment.setMovie(movie);
            mDetailsFragment.setStart(false);
            fragmentManager.beginTransaction()
                    .replace(R.id.master_movie_details, mDetailsFragment)
                    .commit();
        }
    }
}
