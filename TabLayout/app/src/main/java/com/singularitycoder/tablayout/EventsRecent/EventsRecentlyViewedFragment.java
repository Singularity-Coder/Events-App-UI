package com.singularitycoder.tablayout.EventsRecent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.singularitycoder.tablayout.EventsHome.EventFullViewActivity;
import com.singularitycoder.tablayout.EventsHome.EventItemModel;
import com.singularitycoder.tablayout.R;

import java.util.ArrayList;

public class EventsRecentlyViewedFragment extends Fragment {
    int color;
     EventsRecentRecyclerAdapter recentAdapter;
     ArrayList<EventItemModel> recentList;

    public EventsRecentlyViewedFragment() {
    }

    @SuppressLint("ValidFragment")
    public EventsRecentlyViewedFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_recent, container, false);

        final FrameLayout frameLayout = view.findViewById(R.id.events_fragment);
        frameLayout.setBackgroundColor(color);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_events_recent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recentList = new ArrayList<>();
//            for (int i = 0; i < EventItemModel.data.length; i++) {
//                homeList.add(EventItemModel.data[i]);
//            }

        recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Bongo Bunnies", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Food Mania", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Fry Fries", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Orange Kun", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Namaste Curries", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Bongo Bunnies", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Food Mania", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Fry Fries", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Orange Kun", "Festival", "Trinity Hotel", "$100000"));
        recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Namaste Curries", "Festival", "Trinity Hotel", "$100000"));

        recentAdapter = new EventsRecentRecyclerAdapter(recentList, getContext());
        recentAdapter.setHasStableIds(true);
        recentAdapter.setOnItemClickListener(new EventsRecentRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), EventFullViewActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(recentAdapter);

        return view;
    }
}