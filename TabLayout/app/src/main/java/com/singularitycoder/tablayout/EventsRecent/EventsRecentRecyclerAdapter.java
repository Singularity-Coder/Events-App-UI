package com.singularitycoder.tablayout.EventsRecent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.singularitycoder.tablayout.R;
import com.singularitycoder.tablayout.EventsHome.EventItemModel;

import java.util.ArrayList;

public class EventsRecentRecyclerAdapter extends RecyclerView.Adapter<EventsRecentRecyclerAdapter.EventViewHolder> implements Filterable {

    ArrayList<EventItemModel> eventsList;
    ArrayList<EventItemModel> eventsSearchList;
    Context context;
    OnItemClickListener clickListener;

    public EventsRecentRecyclerAdapter(ArrayList<EventItemModel> eventsList, Context context) {
        this.eventsList = eventsList;
        eventsSearchList = new ArrayList<>(eventsList);
        this.context = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_recent_events, viewGroup, false);
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
    }

    @Override
    public int getItemCount() {
//            return eventsList == null ? 0 : eventsList.size();
        return eventsList.size();
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
//        ImageView imgMoreEventOptions;


        public EventViewHolder(View itemView) {
            super(itemView);

            imgEventImage = itemView.findViewById(R.id.img_event_image);
            tvEventDate = itemView.findViewById(R.id.tv_event_date);
            tvEventTitle = itemView.findViewById(R.id.tv_event_title);
            tvEventCategory = itemView.findViewById(R.id.tv_event_category);
            tvEventVenue = itemView.findViewById(R.id.tv_event_venue);
//            imgMoreEventOptions = itemView.findViewById(R.id.img_event_more);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}