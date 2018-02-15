package com.example.soham.booklisting;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by soham on 20/12/17.
 */

public class BookAdapter extends ArrayAdapter<Book> {
    private String publishedYear;
    private static final String SEPARATOR = "-";

    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView titleTextView = convertView.findViewById(R.id.title_text_view);
        TextView authorTextView = convertView.findViewById(R.id.author_text_view);
        TextView dateTextView = convertView.findViewById(R.id.date_text_view);

        Book currentData = getItem(position);
        String publishedDate = currentData.getPublishedDate();
        if (publishedDate.contains(SEPARATOR)) {
            String[] splitDate = publishedDate.split(SEPARATOR);
            publishedYear = splitDate[0];
        } else {
            publishedYear = publishedDate;
        }
        titleTextView.setText(currentData.getTitle());
        authorTextView.setText(currentData.getAuthor());
        dateTextView.setText(publishedYear);

        return convertView;
    }
}
