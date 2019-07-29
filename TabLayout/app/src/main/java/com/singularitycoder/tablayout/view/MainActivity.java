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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.singularitycoder.tablayout.R;
import com.singularitycoder.tablayout.adapter.EventsRecyclerAdapter;
import com.singularitycoder.tablayout.model.EventItemModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    static EventsRecyclerAdapter adapter;
    static ArrayList<EventItemModel> list;

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
        adapter.addFrag(new EventsFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "ALL");
        adapter.addFrag(new EventsFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "INTERESTED");
        adapter.addFrag(new EventsFragment(ContextCompat.getColor(this, R.color.bg_light_grey)), "I'M GOING");
        viewPager.setAdapter(adapter);
    }

    public void showEventFiltersDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_event_filters);

        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        dialog.getWindow().setLayout((int)(displayRectangle.width() * 0.8f), dialog.getWindow().getAttributes().height);

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
                adapter.getFilter().filter(newText);
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

    public static class EventsFragment extends Fragment {
        int color;

        public EventsFragment() {
        }

        @SuppressLint("ValidFragment")
        public EventsFragment(int color) {
            this.color = color;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.events_fragment, container, false);

            final FrameLayout frameLayout = view.findViewById(R.id.events_fragment);
            frameLayout.setBackgroundColor(color);

            RecyclerView recyclerView = view.findViewById(R.id.recycler_view_events);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);

            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setDrawingCacheEnabled(true);
            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

            list = new ArrayList<>();
//            for (int i = 0; i < EventItemModel.data.length; i++) {
//                list.add(EventItemModel.data[i]);
//            }

            list.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Sri Krishna Janmashtami", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Vijaya Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Nityananda Trayodasi", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Bhismastami", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Mohini Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Pandava Nirjala Ekadasi", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Guru Purnima", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Nandotsava", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header, "SUN, AUG 23 - AUG 25", "Lalita Sasti", "Festival", "ISKCON Temple", "$100000"));
            list.add(new EventItemModel(R.drawable.header2, "SUN, AUG 23 - AUG 25", "Durga Puja", "Festival", "ISKCON Temple", "$100000"));

            adapter = new EventsRecyclerAdapter(list, getContext());
            adapter.setHasStableIds(true);
            adapter.setOnItemClickListener(new EventsRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getContext(), position + " got clicked", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), EventFullViewActivity.class);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);

            return view;
        }
    }

}
