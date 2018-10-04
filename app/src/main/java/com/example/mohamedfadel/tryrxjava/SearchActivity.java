package com.example.mohamedfadel.tryrxjava;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements SearchFragment.OnMoviesListItemSelected{

    private EditText searchEditText;
    private ImageButton searchImageButton;
    private boolean firstSearch = true;
    private boolean mTowPane;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();


        searchImageButton = findViewById(R.id.my_ib_search);
        searchEditText = findViewById(R.id.my_et_search);

        if (findViewById(R.id.search_master) != null){
            mTowPane = true;
            DetailsFragment detailsFragment = new DetailsFragment();
            detailsFragment.setStart(true);
            fragmentManager.beginTransaction()
                    .add(R.id.search_fragment_details, detailsFragment)
                    .commit();
        }else {
            mTowPane = false;
        }

        searchImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setSearchWord(searchEditText.getText().toString().trim());
                searchFragment.setmTwoPane(mTowPane);
                if (firstSearch){
                    fragmentManager.beginTransaction()
                            .add(R.id.search_fragment_container, searchFragment)
                            .commit();
                    firstSearch = false;
                }else {
                    SearchFragment searchFragment2 = new SearchFragment();
                    searchFragment2.setSearchWord(searchEditText.getText().toString().trim());
                    searchFragment2.setmTwoPane(mTowPane);
                    fragmentManager.beginTransaction()
                            .replace(R.id.search_fragment_container, searchFragment2)
                            .commit();
                }
                hideKeyboard();
            }
        });
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            SearchFragment searchFragment = new SearchFragment();
                            searchFragment.setSearchWord(searchEditText.getText().toString().trim());
                            searchFragment.setmTwoPane(mTowPane);
                            if (firstSearch){
                                fragmentManager.beginTransaction()
                                        .add(R.id.search_fragment_container, searchFragment)
                                        .commit();
                                firstSearch = false;
                            }else {
                                SearchFragment searchFragment2 = new SearchFragment();
                                searchFragment2.setSearchWord(searchEditText.getText().toString().trim());
                                searchFragment2.setmTwoPane(mTowPane);
                                fragmentManager.beginTransaction()
                                        .replace(R.id.search_fragment_container, searchFragment2)
                                        .commit();
                            }

                            hideKeyboard();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

    }



    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(this.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (mTowPane){
            DetailsFragment mDetailsFragment = new DetailsFragment();
            mDetailsFragment.setMovie(movie);
            mDetailsFragment.setStart(false);
            fragmentManager.beginTransaction()
                    .replace(R.id.search_fragment_details, mDetailsFragment)
                    .commit();
        }
    }
}
