package com.singularitycoder.tablayout.EventsHome;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
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
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.singularitycoder.tablayout.CreateEvent.CreateEventActivity;
import com.singularitycoder.tablayout.EventCategories.EventsCategoriesViewedFragment;
import com.singularitycoder.tablayout.EventsGoing.EventsGoingFragment;
import com.singularitycoder.tablayout.EventsInterested.EventsInterestedFragment;
import com.singularitycoder.tablayout.R;
import com.singularitycoder.tablayout.EventsRecent.EventsRecentlyViewedFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;

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
//                homeAdapter.getFilter().filter(newText);
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


}
