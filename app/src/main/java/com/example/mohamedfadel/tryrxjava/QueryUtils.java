package com.example.mohamedfadel.tryrxjava;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QueryUtils {

    public QueryUtils() {
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        Log.d("MainActivity", stringBuilder.toString());
        Log.d("MainActivity", String.valueOf(stringBuilder.toString().length()));
        return stringBuilder.toString();
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null)
            return null;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.connect();
            if (httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null)
                inputStream.close();
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return jsonResponse;
    }

    private static ArrayList<Movie> extractMovies(String jsonResponse){
        ArrayList <Movie> movies = new ArrayList<>();
        try {
            JSONObject jsonRoot = new JSONObject(jsonResponse);
            JSONObject jsonData = jsonRoot.getJSONObject("data");
            JSONArray moviesRawData = jsonData.getJSONArray("movies");
            for (int i = 0; i < moviesRawData.length(); i++){
                JSONObject currentObject = moviesRawData.getJSONObject(i);
                if (!currentObject.has("torrents"))
                    continue;
                String movieTitle = currentObject.getString("title_long");
                float movieRate = (float)currentObject.getDouble("rating");
                String movieDescription = currentObject.getString("description_full");
                String movieTrailer = currentObject.getString("yt_trailer_code");
                String movieLanguage = currentObject.getString("language");
                String movieBackground = currentObject.getString("background_image");
                String movieCover = currentObject.getString("medium_cover_image");
                String movieDate = "";
                movieDate = currentObject.optString("date_uploaded").split(" ")[0];
                ArrayList <String> movieGeners = new ArrayList<>();
                JSONArray genersList = currentObject.optJSONArray("genres");
                if (genersList != null )
                    for (int j = 0; j < genersList.length(); j++)
                        movieGeners.add(genersList.optString(j));
                JSONArray torrents = currentObject.optJSONArray("torrents");
                ArrayList<Torrent> movieTorrent = getMovieTorrents(torrents);

                Movie movie = new Movie(movieTitle,
                        movieTrailer,
                        movieRate,
                        movieGeners,
                        movieDescription,
                        movieLanguage,
                        movieBackground,
                        movieCover,
                        movieDate);
                movie.setTorrents(movieTorrent);

                movies.add(movie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    private static ArrayList<Torrent> getMovieTorrents(JSONArray jsonArray){
        ArrayList<Torrent> torrentArrayList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            String url = jsonObject.optString("url");
            String quality = jsonObject.optString("quality");
            String size = jsonObject.optString("size");
            torrentArrayList.add(new Torrent(url, quality, size));
        }
        return torrentArrayList;
    }


    public static ArrayList<Movie> fetchMoviesData(String jsonQueryString){
        URL url= createUrl(jsonQueryString);
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Movie> movieList = extractMovies(jsonResponse);
        return movieList;
    }
}
