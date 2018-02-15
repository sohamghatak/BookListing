package com.example.soham.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by soham on 19/12/17.
 */

public class BookUtils {

    private static final String LOG_TAG = BookUtils.class.getName();

    /**
     * Fetches Book data from the request URL
     *
     * @param requestURL This is the request URL
     * @return books  Lis item
     */
    public static List<Book> fetchBookData(String requestURL) {
        // Create URL object
        URL url = createURL(requestURL);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<Book> books = readFromJSONResponse(jsonResponse);
        return books;
    }

    /**
     * Method to establish a connection with the provided request URL
     *
     * @param url Input URL after conversion from String
     * @return String JSON Response
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //Condition to check if URL is null and return gracefully
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            //Checking for server response code
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "No JSON results received.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert input stream to String
     *
     * @param inputStream inputStream received from Http URL connection request
     * @return String JSON response
     */
    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder outputResponse = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                outputResponse.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "No JSON results received.", e);
        }
        return outputResponse.toString();
    }

    /**
     * Returns new URL object from the given string URL.
     *
     * @param stringURL String to be converted to URL
     * @return URL object
     */
    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Return a List of Books after reading the input JSON.
     *
     * @param bookJSON JSON String passed after reading from input stream
     * @return List of Books
     */
    private static List<Book> readFromJSONResponse(String bookJSON) {
        //Using StringBuffer to hold all author names
        StringBuffer allAuthors = new StringBuffer();
        //Return gracefully if String is null
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        List<Book> books = new ArrayList<>();
        try {
            JSONObject rootObject = new JSONObject(bookJSON);
            JSONArray itemsArray = rootObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++) {
                String prefix = "";
                JSONObject values = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = values.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                JSONArray authorArray = volumeInfo.getJSONArray("authors");
                // authors is an array hence looping through all the authors
                for (int j = 0; j < authorArray.length(); j++) {
                    allAuthors.append(prefix);
                    prefix = ", ";
                    allAuthors.append(authorArray.getString(j));
                }
                String publishedDate = volumeInfo.getString("publishedDate");
                Book book = new Book(title, allAuthors.toString(), publishedDate);
                books.add(book);
                allAuthors.setLength(0);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot parse book JSON String", e);
        }
        return books;
    }
}
