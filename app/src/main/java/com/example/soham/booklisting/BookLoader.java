package com.example.soham.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by soham on 21/12/17.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private String mURL;

    //BookLoader constructor
    public BookLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    //Start the loader
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /*Load background thread and generate
    new set of data published by Loader by calling fetchBookData method*/
    @Override
    public List<Book> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        List<Book> resultBooks = BookUtils.fetchBookData(mURL);
        return resultBooks;
    }
}
