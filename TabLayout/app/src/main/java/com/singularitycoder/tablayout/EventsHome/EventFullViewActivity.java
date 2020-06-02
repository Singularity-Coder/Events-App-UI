package com.singularitycoder.tablayout.EventsHome;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.singularitycoder.tablayout.R;

import java.util.ArrayList;

public class EventFullViewActivity extends AppCompatActivity {

    TextView tvEventVenue;
    Toolbar viewEventToolbar;
    RecyclerView recyclerView;
    EventFullViewGalleryAdapter galleryAdapter;
    ArrayList<EventFullViewModel> galleryList;
    Button btnEventViewInterested, btnEventViewGoing, btnEventViewShare;
    ImageView imgMoreEventOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_full_view);

        initToolBar();
        fillGalleryList();
        setGalleryAdapter();
        setGalleryRecyclerView();

        imgMoreEventOptions = findViewById(R.id.img_view_event_more);
        imgMoreEventOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreEventOptions();
            }
        });


        tvEventVenue = findViewById(R.id.tv_event_placeholder_venue);
        tvEventVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventFullViewModel eventFullViewModel = new EventFullViewModel();
//                String strUri = "http://maps.google.com/maps?q=loc:" + eventFullViewModel.getLati() + "," + eventFullViewModel.getLongi() + " (" + eventFullViewModel.dummyLocName() + ")";
                String strUri = "http://maps.google.com/maps?q=loc:" + eventFullViewModel.getStrEventFullViewEventVenue();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });

        btnEventViewInterested = findViewById(R.id.btn_view_event_interested);
        btnEventViewGoing = findViewById(R.id.btn_view_event_going);
        btnEventViewShare = findViewById(R.id.btn_view_event_share);
        btnEventViewInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnEventViewInterested.getText().toString().equals("Interested")) {
                    btnEventViewInterested.setText("✓ Interested");
                    btnEventViewInterested.setTextSize(13);

                    Drawable buttonDrawable = btnEventViewInterested.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.colorGreen));
                    btnEventViewInterested.setBackground(buttonDrawable);

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        btnEventViewInterested.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
//                    }
                } else {
                    btnEventViewInterested.setText("Interested");
                    btnEventViewInterested.setTextSize(15);
//
                    Drawable buttonDrawable = btnEventViewInterested.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.colorSkyBlue));
                    btnEventViewInterested.setBackground(buttonDrawable);

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        btnEventViewInterested.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorSkyBlue)));
//                    }
                }
            }
        });
        btnEventViewGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show instamojo page to pay

                if (btnEventViewGoing.getText().toString().equals("I ' m Going")) {
                    btnEventViewGoing.setText("✓ going");
                    btnEventViewGoing.setTextSize(13);

                    Drawable buttonDrawable = btnEventViewGoing.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.colorGreen));
                    btnEventViewGoing.setBackground(buttonDrawable);
                    btnEventViewGoing.setEnabled(false);

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        btnEventViewInterested.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
//                    }
                }

//                else {
//                    btnEventViewGoing.setText("I ' m Going");
//                    btnEventViewGoing.setTextSize(15);
////
//                    Drawable buttonDrawable = btnEventViewGoing.getBackground();
//                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
//                    DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.colorSkyBlue));
//                    btnEventViewGoing.setBackground(buttonDrawable);
//
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                        btnEventViewInterested.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorSkyBlue)));
////                    }
//                }
            }
        });
        btnEventViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Share clicked", Toast.LENGTH_LONG).show();

//                    Uri myUriNew;
//                    myUriNew = Uri.parse(postImageListNew.get(0));
//                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//                    sharingIntent.setType("image/.*");
//                    sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
//                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, data);
//                    sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//                    context.startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                // Extra subject. The receiving app will decide what to do with this. Works in gmail, Telegram
                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
                // Main content. This can be a link or just plain text as well.
                share.putExtra(Intent.EXTRA_TEXT, "http://www.singularitycoder.com");

                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivity(Intent.createChooser(share, "Share to"));
            }
        });
    }

    private void initToolBar() {
        viewEventToolbar = findViewById(R.id.toolbar_create_event);
        viewEventToolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewEventToolbar.setElevation(10);
        }
        setSupportActionBar(viewEventToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showMoreEventOptions() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("More Event Options");

        // add a homeList
        String[] selectArray = {"Hide this event type", "Report Abuse"};
        builder.setItems(selectArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:

                        break;
                    case 1:

                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fillGalleryList() {
        galleryList = new ArrayList<>();
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
        galleryList.add(new EventFullViewModel(R.drawable.header));
    }

    private void setGalleryAdapter() {
        galleryAdapter = new EventFullViewGalleryAdapter(galleryList, this);
        galleryAdapter.setHasStableIds(true);
        galleryAdapter.setOnItemClickListener(new EventFullViewGalleryAdapter.InterfaceOnItemClicked() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getApplicationContext(), position + " got clicked", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setGalleryRecyclerView() {
        recyclerView = findViewById(R.id.recyclerview_view_event_gallery);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setAdapter(galleryAdapter);
    }
}
