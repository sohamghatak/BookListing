package com.example.soham.booklisting;

import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<Book>> {

    private String finalURL;
    private BookAdapter mAdapter;
    private Button searchButton;
    private String sGenre;
    private View progressLoader;
    private TextView mEmptyTextView;
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        //Remove focus from the Edit Text view so that the keyboard does not pop up on view rotation
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mEmptyTextView = findViewById(R.id.empty_text_view);
        progressLoader = findViewById(R.id.progress_view);
        //Set progress view to invisible when the layout is first loaded
        progressLoader.setVisibility(View.GONE);

        //Check if the savedInstanceState bundle is not null and then initialize the Loader
        if (savedInstanceState != null) {
            getLoaderManager().initLoader(BOOK_LOADER_ID, null, BookActivity.this);
        }

        searchButton = findViewById(R.id.search_book_button);
        //Check for network connection
        if (isNetworkConnected()) {
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressLoader.setVisibility(View.VISIBLE);
                    mEmptyTextView.setText("");
                    StringBuffer mURL = new StringBuffer("https://www.googleapis.com/books/v1/volumes?q=");
                    EditText mGenre = findViewById(R.id.search_text_box);
                    sGenre = mGenre.getText().toString();
                    finalURL = mURL.append(sGenre).toString();
                    if (!sGenre.isEmpty() && sGenre != null) {
                        android.app.LoaderManager loaderManager = getLoaderManager();
                        //Restart the existing loader on Search button Click and register call backs
                        loaderManager.restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
                    } else if (mAdapter != null) {
                        mAdapter.clear();
                        progressLoader.setVisibility(View.GONE);
                        mEmptyTextView.setText(R.string.no_search_text);
                    } else {
                        progressLoader.setVisibility(View.GONE);
                        mEmptyTextView.setText(R.string.no_search_text);
                    }
                }

            });
        }
    }

    //Method to check if there is internet connectivity
    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    //Creates a new Loader
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, finalURL);
    }

    //Called when the Loader called in onCreateLoader has fininshed Loading
    @Override
    public void onLoadFinished(android.content.Loader<List<Book>> loader, List<Book> books) {

        progressLoader.setVisibility(View.GONE);
        //Clear adapter on Loader refresh
        if (mAdapter != null) {
            mAdapter.clear();
            mEmptyTextView.setText("");
        }
        if (books != null && !books.isEmpty()) {
            ListView bookListView = findViewById(R.id.list);
            // Create a new {@link ArrayAdapter} of books
            mAdapter = new BookAdapter(BookActivity.this, new ArrayList<Book>());
            //Add all book objects to the adapter
            mAdapter.addAll(books);
            //Set the adapter to populate the list on UI
            bookListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mEmptyTextView.setText(R.string.no_books_founds);
        }
    }

    //Clear the mAdapter when previous Loader is reset.
    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }
}
