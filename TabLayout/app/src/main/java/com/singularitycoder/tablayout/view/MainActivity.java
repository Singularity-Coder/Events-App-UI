package com.singularitycoder.tablayout.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.singularitycoder.tablayout.R;
import com.singularitycoder.tablayout.adapter.EventsCategoriesRecyclerAdapter;
import com.singularitycoder.tablayout.adapter.EventsGoingRecyclerAdapter;
import com.singularitycoder.tablayout.adapter.EventsHomeRecyclerAdapter;
import com.singularitycoder.tablayout.adapter.EventsInterestedRecyclerAdapter;
import com.singularitycoder.tablayout.adapter.EventsRecentRecyclerAdapter;
import com.singularitycoder.tablayout.model.EventItemModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    static EventsHomeRecyclerAdapter homeAdapter;
    static EventsInterestedRecyclerAdapter interestedAdapter;
    static EventsGoingRecyclerAdapter goingAdapter;
    static EventsRecentRecyclerAdapter recentAdapter;
    static EventsCategoriesRecyclerAdapter categoriesAdapter;
    static ArrayList<EventItemModel> homeList;
    static ArrayList<EventItemModel> interestedList;
    static ArrayList<EventItemModel> goingList;
    static ArrayList<EventItemModel> recentList;
    static ArrayList<EventItemModel> categoriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initViewPager();
        initTabLayout();
    }

    private void initToolBar() {
        toolbar = findViewById(R.id.toolbar_home_events);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Events");
        // For back navigation button use this
        // if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewpager_home_events);
        setupViewPager(viewPager);
    }

    private void initTabLayout() {
        tabLayout = findViewById(R.id.tabs_home_events);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        toast("1");
                        break;
                    case 1:
                        toast("2");
                        break;
                    case 2:
                        toast("3");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new EventsHomeFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "ALL");
        adapter.addFrag(new EventsCategoriesViewedFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "CATEGORIES");
        adapter.addFrag(new EventsInterestedFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "INTERESTED");
        adapter.addFrag(new EventsGoingFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "I'M GOING");
        adapter.addFrag(new EventsRecentlyViewedFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "RECENTLY VIEWED");
        viewPager.setAdapter(adapter);
    }

    public void showEventFiltersDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_event_filters);

        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int) (displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

        ImageView imgCloseDialog = dialog.findViewById(R.id.img_event_filter_dialog_close);
        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final TextView tvDateDialog = dialog.findViewById(R.id.tv_event_filter_dialog_date_setter);
        tvDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CreateEventActivity().showDatePicker(tvDateDialog, MainActivity.this);
            }
        });

        final SeekBar seekBar = dialog.findViewById(R.id.seekbar_event_filter_dialog_proximity);
        final TextView tvSeekBarProgress = dialog.findViewById(R.id.tv_event_filter_dialog_seekbar_value);
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // when progress changed.
                    tvSeekBarProgress.setText(String.valueOf(seekBar.getProgress() * 25) + " m");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // when touch started.
                    tvSeekBarProgress.setText(String.valueOf(seekBar.getProgress() * 25) + " m");
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // when touch stopped.
                    tvSeekBarProgress.setText(String.valueOf(seekBar.getProgress() * 25) + " m");
                }
            });
        }

        final RadioGroup rgCost = dialog.findViewById(R.id.radiogroup_event_filter_dialog_cost);

        TextView tvReset = dialog.findViewById(R.id.tv_event_filter_dialog_reset);
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setProgress(4);
                tvDateDialog.setText("");
                rgCost.clearCheck();
//                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_events, menu);

        MenuItem searchItem = menu.findItem(R.id.action_events_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_events_search:
                return true;
            case R.id.action_events_add:
                Intent createEventIntent = new Intent(MainActivity.this, CreateEventActivity.class);
                startActivity(createEventIntent);
                return true;
            case R.id.action_filter:
                showEventFiltersDialog(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static class EventsHomeFragment extends Fragment {
        int color;

        public EventsHomeFragment() {
        }

        @SuppressLint("ValidFragment")
        public EventsHomeFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_event_home, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.events_fragment);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_view_events_home);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            homeList = new ArrayList<>();
//            for (int i = 0; i < EventItemModel.data.length; i++) {
//                homeList.add(EventItemModel.data[i]);
//            }

            homeList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Sri Krishna Janmashtami", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Vijaya Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Nityananda Trayodasi", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Bhismastami", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Mohini Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Pandava Nirjala Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Guru Purnima", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Nandotsava", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Lalita Sasti", "Festival", "ISKCON Temple", "$100000"));
            homeList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Durga Puja", "Festival", "ISKCON Temple", "$100000"));

            homeAdapter = new EventsHomeRecyclerAdapter(homeList, getContext());
            homeAdapter.setHasStableIds(true);
            homeAdapter.setOnItemClickListener(new EventsHomeRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), EventFullViewActivity.class);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(homeAdapter);

            return view;
        }
    }

    public static class EventsInterestedFragment extends Fragment {
        int color;

        public EventsInterestedFragment() {
        }

        @SuppressLint("ValidFragment")
        public EventsInterestedFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_event_interested, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.events_fragment);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_view_events_interested);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            interestedList = new ArrayList<>();
//            for (int i = 0; i < EventItemModel.data.length; i++) {
//                homeList.add(EventItemModel.data[i]);
//            }

            interestedList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Sri Krishna Janmashtami", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Vijaya Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Nityananda Trayodasi", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Bhismastami", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Mohini Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Pandava Nirjala Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Guru Purnima", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Nandotsava", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Lalita Sasti", "Festival", "ISKCON Temple", "$100000"));
            interestedList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Durga Puja", "Festival", "ISKCON Temple", "$100000"));

            interestedAdapter = new EventsInterestedRecyclerAdapter(interestedList, getContext());
            interestedAdapter.setHasStableIds(true);
            interestedAdapter.setOnItemClickListener(new EventsInterestedRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), EventFullViewActivity.class);
                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(interestedAdapter);

            return view;
        }
    }

    public static class EventsGoingFragment extends Fragment {
        int color;

        public EventsGoingFragment() {
        }

        @SuppressLint("ValidFragment")
        public EventsGoingFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_event_going, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.events_fragment);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_view_events_going);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            goingList = new ArrayList<>();
//            for (int i = 0; i < EventItemModel.data.length; i++) {
//                homeList.add(EventItemModel.data[i]);
//            }

            goingList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Sri Krishna Janmashtami", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Vijaya Ekadasi", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Nityananda Trayodasi", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Bhismastami", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Mohini Ekadasi", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Pandava Nirjala Ekadasi", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Guru Purnima", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Nandotsava", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Lalita Sasti", "Festival", "ISKCON Temple"));
            goingList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Durga Puja", "Festival", "ISKCON Temple"));

            goingAdapter = new EventsGoingRecyclerAdapter(goingList, getContext());
            goingAdapter.setHasStableIds(true);
            goingAdapter.setOnItemClickListener(new EventsGoingRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent(getContext(), EventFullViewActivity.class);
//                    startActivity(intent);
                }
            });

            recyclerView.setAdapter(goingAdapter);

            return view;
        }
    }

    public static class EventsRecentlyViewedFragment extends Fragment {
        int color;

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

            recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Sri Krishna Janmashtami", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Vijaya Ekadasi", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Nityananda Trayodasi", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Bhismastami", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Mohini Ekadasi", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Pandava Nirjala Ekadasi", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Guru Purnima", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Nandotsava", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Lalita Sasti", "Festival", "ISKCON Temple"));
            recentList.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Durga Puja", "Festival", "ISKCON Temple"));

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

    public static class EventsCategoriesViewedFragment extends Fragment {
        int color;

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

}
