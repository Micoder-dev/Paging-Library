package com.example.paginglibrary.util;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.paginglibrary.model.Movie;

//this movie comparator class it compares for movie object to avoid the
public class MovieComparator extends DiffUtil.ItemCallback<Movie> {
    @Override
    public boolean areItemsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return oldItem.getId() == newItem.getId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull Movie oldItem, @NonNull Movie newItem) {
        return oldItem.getId() == newItem.getId();
    }
}
