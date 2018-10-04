package com.example.mohamedfadel.tryrxjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchFragment extends Fragment implements MoviesAdapter.ListItemClickListener{

    private static final String REQUEST_URL = "//yts.am/api/v2/list_movies.json";

    private RecyclerView searchRecyclerView;
    private MoviesAdapter moviesAdapter;
    private Disposable disposable;
    private boolean isLoading;
    private String searchWord;
    private OnMoviesListItemSelected onListItemSelected;
    private boolean mTwoPane;

    interface OnMoviesListItemSelected{
        void onMovieSelected(Movie movie);
    }

    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchRecyclerView = rootView.findViewById(R.id.my_rv);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        moviesAdapter = new MoviesAdapter(this);
        searchRecyclerView.setAdapter(moviesAdapter);

        return rootView;
    }


    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
        startSearch();
    }

    private void startSearch() {
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Single<ArrayList<Movie>> getObservable(){
        return Single.create(new SingleOnSubscribe<ArrayList<Movie>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Movie>> emitter) throws Exception {
                if (!emitter.isDisposed()){
                    isLoading = true;
                    emitter.onSuccess(QueryUtils.fetchMoviesData(makeSerchStringURL()));
                    isLoading = false;
                }
            }
        });
    }
    private SingleObserver<ArrayList<Movie>> getObserver(){
        return new SingleObserver<ArrayList<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                Log.d("RXResult", "onSubscribe");
            }

            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                if (movies.size() != 0 && movies != null && moviesAdapter != null){
                    moviesAdapter.setMoviesList(movies);
                    if (mTwoPane){
                        onListItemSelected.onMovieSelected(moviesAdapter.getMoviesList().get(0));
                    }
                    Log.d("RXResult", movies.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("RXResult", e.getMessage());
            }
        };
    }

    private String makeSerchStringURL() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .path(REQUEST_URL)
                .appendQueryParameter("query_term", searchWord);
        return builder.toString();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onListItemSelected = (SearchFragment.OnMoviesListItemSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public boolean ismTwoPane() {
        return mTwoPane;
    }

    public void setmTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mTwoPane){
            onListItemSelected.onMovieSelected(moviesAdapter.getMoviesList().get(clickedItemIndex));
        }else {
            Intent intent = new Intent(getContext(), MovieDetailsActivity.class);
            intent.putExtra("movie", moviesAdapter.getMoviesList().get(clickedItemIndex));
            startActivity(intent);
        }
    }
}
