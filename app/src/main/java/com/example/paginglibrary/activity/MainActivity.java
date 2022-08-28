package com.example.paginglibrary.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.example.paginglibrary.R;
import com.example.paginglibrary.adapter.MoviesAdapter;
import com.example.paginglibrary.adapter.MoviesLoadStateAdapter;
import com.example.paginglibrary.databinding.ActivityMainBinding;
import com.example.paginglibrary.util.GridSpace;
import com.example.paginglibrary.util.MovieComparator;
import com.example.paginglibrary.util.Utils;
import com.example.paginglibrary.viewmodel.MovieViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    MovieViewModel mainActivityViewModel;
    ActivityMainBinding binding;
    MoviesAdapter moviesAdapter;

    @Inject
    RequestManager requestManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Utils.API_KEY == null || Utils.API_KEY.isEmpty()) {
            Toast.makeText(this, "Error in API Key", Toast.LENGTH_SHORT).show();
        }

        moviesAdapter = new MoviesAdapter(new MovieComparator(), requestManager);

        mainActivityViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        initRecyclerviewAndAdapter();

        // Subscribe to Paging Data
        mainActivityViewModel.moviePagingDataFlowable.subscribe(moviePagingData -> {
            moviesAdapter.submitData(getLifecycle(), moviePagingData);
        });

    }

    private void initRecyclerviewAndAdapter() {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        binding.recyclerViewMovies.setLayoutManager(gridLayoutManager);

        binding.recyclerViewMovies.addItemDecoration(new GridSpace(2,20,true));

        binding.recyclerViewMovies.setAdapter(
                moviesAdapter.withLoadStateFooter(
                        new MoviesLoadStateAdapter(view -> {
                            moviesAdapter.retry();
                        })
                )
        );

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return moviesAdapter.getItemViewType(position) == MoviesAdapter.LOADING_ITEM ? 1 : 2;
            }
        });

    }
}