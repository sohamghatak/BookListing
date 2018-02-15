package com.example.soham.booklisting;

/**
 * Created by soham on 19/12/17.
 */

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mPublishedDate;

    //Book Constructor
    public Book(String title, String author, String publishedDate) {
        mTitle = title;
        mAuthor = author;
        mPublishedDate = publishedDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

}
