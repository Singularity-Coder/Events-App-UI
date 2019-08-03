package com.singularitycoder.tablayout.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.singularitycoder.tablayout.R;
import com.singularitycoder.tablayout.Helpers;
import com.singularitycoder.tablayout.model.EventItemModel;

import java.util.ArrayList;

public class EventsHomeRecyclerAdapter extends RecyclerView.Adapter<EventsHomeRecyclerAdapter.EventViewHolder> implements Filterable {

    ArrayList<EventItemModel> eventsList;
    ArrayList<EventItemModel> eventsSearchList;
    Context context;
    OnItemClickListener clickListener;

    public EventsHomeRecyclerAdapter(ArrayList<EventItemModel> eventsList, Context context) {
        this.eventsList = eventsList;
        eventsSearchList = new ArrayList<>(eventsList);
        this.context = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_home_events, viewGroup, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder eventViewHolder, int i) {
        EventItemModel eventModel = eventsList.get(i);

        eventViewHolder.imgEventImage.setImageResource(eventModel.getIntEventImage());
        eventViewHolder.tvEventDate.setText(eventModel.getStrEventDate());
        eventViewHolder.tvEventTitle.setText(eventModel.getStrEventTitle());
        eventViewHolder.tvEventCategory.setText(eventModel.getStrEventCategory());
        eventViewHolder.tvEventVenue.setText(eventModel.getStrEventVenue());
        eventViewHolder.tvEventPrice.setText(eventModel.getStrEventPrice());
        eventViewHolder.btnEventInterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventViewHolder.btnEventInterested.getText().toString().equals("Interested")) {
                    eventViewHolder.btnEventInterested.setText("✓ Interested");
                    eventViewHolder.btnEventInterested.setTextSize(15);

                    Drawable buttonDrawable = eventViewHolder.btnEventInterested.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, context.getResources().getColor(R.color.colorGreen));
                    eventViewHolder.btnEventInterested.setBackground(buttonDrawable);

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        btnEventViewInterested.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorGreen)));
//                    }
                } else {
                    eventViewHolder.btnEventInterested.setText("Interested");
                    eventViewHolder.btnEventInterested.setTextSize(15);
//
                    Drawable buttonDrawable = eventViewHolder.btnEventInterested.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    DrawableCompat.setTint(buttonDrawable, context.getResources().getColor(R.color.colorSkyBlue));
                    eventViewHolder.btnEventInterested.setBackground(buttonDrawable);

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        btnEventViewInterested.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorSkyBlue)));
//                    }
                }
            }
        });
        eventViewHolder.btnEventShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Share btn clicked");

                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                Toast.makeText(context, "Share clicked", Toast.LENGTH_LONG).show();

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
                context.startActivity(Intent.createChooser(share, "Share to"));
            }
        });
        eventViewHolder.imgMoreEventOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreEventOptions();
            }
        });
        Helpers.setFadeAnimation(eventViewHolder.itemView);
    }

    @Override
    public int getItemCount() {
//            return eventsList == null ? 0 : eventsList.size();
        return eventsList.size();
    }

    private void showMoreEventOptions() {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("More Event Options");

        // add a list
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

    private Filter eventsSearchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<EventItemModel> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(eventsSearchList);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (EventItemModel item : eventsSearchList) {
                    if (item.getStrEventTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            eventsList.clear();
            eventsList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return eventsSearchFilter;
    }


    class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgEventImage;
        TextView tvEventDate;
        TextView tvEventTitle;
        TextView tvEventCategory;
        TextView tvEventVenue;
        TextView tvEventPrice;
        Button btnEventInterested;
        Button btnEventShare;
        ImageView imgMoreEventOptions;


        public EventViewHolder(View itemView) {
            super(itemView);

            imgEventImage = itemView.findViewById(R.id.img_event_image);
            tvEventDate = itemView.findViewById(R.id.tv_event_date);
            tvEventTitle = itemView.findViewById(R.id.tv_event_title);
            tvEventCategory = itemView.findViewById(R.id.tv_event_category);
            tvEventVenue = itemView.findViewById(R.id.tv_event_venue);
            tvEventPrice = itemView.findViewById(R.id.tv_event_price);
            btnEventInterested = itemView.findViewById(R.id.btn_event_interested);
            btnEventShare = itemView.findViewById(R.id.btn_event_share);
            imgMoreEventOptions = itemView.findViewById(R.id.img_event_more);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}