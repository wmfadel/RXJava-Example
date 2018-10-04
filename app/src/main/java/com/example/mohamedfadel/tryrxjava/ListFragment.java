package com.example.mohamedfadel.tryrxjava;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListFragment extends Fragment implements MoviesAdapter.ListItemClickListener{

    private Disposable disposable;
    private RecyclerView recyclerView;
    private final MoviesAdapter moviesAdapter = new MoviesAdapter(this);
    public static int pageNumber = 1;
    public static String EXTRA_REQUEST_URL = "https://yts.am/api/v2/list_movies.json?page=";
    private boolean isLoadong = false;
    private TextView emptyTextView;
    private ProgressBar circleProgress;
    private boolean mTwoPane;
    private OnListItemSelected onListItemSelected;

    public interface OnListItemSelected{
        void onMovieSelected(Movie movie);
    }

    public ListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(moviesAdapter);
        emptyTextView = rootView.findViewById(R.id.main_empty_text);
        circleProgress = rootView.findViewById(R.id.main_progress_bar);

        if (isNetworkAvailable()){
            getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getObserver());
        }else {
            circleProgress.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText(R.string.no_internet);
        }

        final LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoadong && layoutManager.getItemCount()-2 == layoutManager.findLastVisibleItemPosition()){
                    loadMoreMovies();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = 1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onListItemSelected = (OnListItemSelected) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }



    private Single<ArrayList<Movie>> getObservable(){
        return Single.create(new SingleOnSubscribe<ArrayList<Movie>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Movie>> emitter) throws Exception {
                if (!emitter.isDisposed()){
                    isLoadong = true;
                    circleProgress.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.GONE);
                    emitter.onSuccess(QueryUtils.fetchMoviesData(EXTRA_REQUEST_URL+pageNumber));
                    isLoadong = false;
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
                circleProgress.setVisibility(View.GONE);
                if (movies.size()== 0){
                    recyclerView.setVisibility(View.GONE);
                    emptyTextView.setVisibility(View.VISIBLE);
                    emptyTextView.setText(R.string.no_data);
                }else {
                    emptyTextView.setVisibility(View.GONE);
                    moviesAdapter.setMoviesList(movies);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (mTwoPane)
                        onListItemSelected.onMovieSelected(moviesAdapter.getMoviesList().get(0));
                    ++pageNumber;
                    Log.d("RXResult", movies.toString());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("RXResult", e.getMessage());
            }
        };
    }

    private Single<ArrayList<Movie>> getExtraObservable(){
        return Single.create(new SingleOnSubscribe<ArrayList<Movie>>() {
            @Override
            public void subscribe(SingleEmitter<ArrayList<Movie>> emitter) throws Exception {
                if (!emitter.isDisposed()){
                    isLoadong = true;
                    emitter.onSuccess(QueryUtils.fetchMoviesData(EXTRA_REQUEST_URL+pageNumber));
                    isLoadong = false;
                }
            }
        });
    }
    private SingleObserver<ArrayList<Movie>> getExtraObserver(){
        return new SingleObserver<ArrayList<Movie>>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                ++pageNumber;
                Log.d("RXResult", "onSubscribe");
            }

            @Override
            public void onSuccess(ArrayList<Movie> movies) {
                for (Movie movie : movies){
                    moviesAdapter.getMoviesList().add(movie);
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d("RXResult", movies.toString());
            }

            @Override
            public void onError(Throwable e) {
                Log.d("RXResult", e.getMessage());
            }
        };
    }

    private void loadMoreMovies(){
        if (isNetworkAvailable()){
            getExtraObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getExtraObserver());
        }
    }

    // checks if there is network available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null)
            disposable.dispose();
    }

    public void setmTwoPane(boolean mTwoPane) {
        this.mTwoPane = mTwoPane;
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
