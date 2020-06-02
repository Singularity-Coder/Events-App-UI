package com.singularitycoder.tablayout.EventCategories;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.singularitycoder.tablayout.EventsHome.EventFullViewActivity;
import com.singularitycoder.tablayout.EventsHome.EventItemModel;
import com.singularitycoder.tablayout.R;

import java.util.ArrayList;

public class EventsCategoriesViewedFragment extends Fragment {
    int color;
    EventsCategoriesRecyclerAdapter categoriesAdapter;
    ArrayList<EventItemModel> categoriesList;

    public EventsCategoriesViewedFragment() {
    }

    @SuppressLint("ValidFragment")
    public EventsCategoriesViewedFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_categories, container, false);

        final FrameLayout frameLayout = view.findViewById(R.id.events_fragment);
        frameLayout.setBackgroundColor(color);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_events_categories);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL));

        categoriesList = new ArrayList<>();
        categoriesList.add(new EventItemModel("Art", R.drawable.header));
        categoriesList.add(new EventItemModel("Causes", R.drawable.header2));
        categoriesList.add(new EventItemModel("Comedy", R.drawable.header2));
        categoriesList.add(new EventItemModel("Crafts", R.drawable.header));
        categoriesList.add(new EventItemModel("Dance", R.drawable.header));
        categoriesList.add(new EventItemModel("Drinks", R.drawable.header2));
        categoriesList.add(new EventItemModel("Film", R.drawable.header2));
        categoriesList.add(new EventItemModel("Fitness", R.drawable.header));
        categoriesList.add(new EventItemModel("Food", R.drawable.header));
        categoriesList.add(new EventItemModel("Games", R.drawable.header2));
        categoriesList.add(new EventItemModel("Gardening", R.drawable.header2));
        categoriesList.add(new EventItemModel("Health", R.drawable.header));
        categoriesList.add(new EventItemModel("Home", R.drawable.header));
        categoriesList.add(new EventItemModel("Literature", R.drawable.header2));
        categoriesList.add(new EventItemModel("Music", R.drawable.header2));

        categoriesAdapter = new EventsCategoriesRecyclerAdapter(categoriesList, getContext());
        categoriesAdapter.setHasStableIds(true);
        categoriesAdapter.setOnItemClickListener(new EventsCategoriesRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), EventFullViewActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(categoriesAdapter);

        return view;
    }
}
