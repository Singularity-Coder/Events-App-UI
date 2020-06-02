package com.singularitycoder.tablayout.EventsHome;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.himangi.imagepreview.ImagePreviewActivity;
import com.himangi.imagepreview.PreviewFile;
import com.singularitycoder.tablayout.Helpers;
import com.singularitycoder.tablayout.R;

import java.util.ArrayList;

public class EventFullViewGalleryAdapter extends RecyclerView.Adapter<EventFullViewGalleryAdapter.EventFullViewGalleryViewHolder> {

    ArrayList<EventFullViewModel> galleryList;
    Context context;
    InterfaceOnItemClicked clickListener;

    public EventFullViewGalleryAdapter(ArrayList<EventFullViewModel> galleryList, Context context) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @Override
    public EventFullViewGalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_event_gallery, viewGroup, false);
        return new EventFullViewGalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventFullViewGalleryViewHolder eventFullViewGalleryViewHolder, final int i) {
        EventFullViewModel galleryModel = galleryList.get(i);

        eventFullViewGalleryViewHolder.imgEventGalleryImage.setImageResource(galleryModel.getIntEventFullViewGalleryImage());
        eventFullViewGalleryViewHolder.imgEventGalleryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showGalleryImagePreview(i);
            }
        });
        Helpers.setFadeAnimation(eventFullViewGalleryViewHolder.itemView);
    }

    private void showGalleryImagePreview(int i) {
        ArrayList<PreviewFile> imageList = new ArrayList<>();
        ArrayList<String> imageFiles = new ArrayList<>();
//        imageFiles = galleryList.get(i).getImageList();
        imageFiles.add("https://i.pinimg.com/originals/7b/29/cb/7b29cb65226238a952864857b52d3b7c.jpg");
        imageFiles.add("https://i.pinimg.com/originals/43/53/60/435360db1afcc0e3da15dfd32e18a7a8.jpg");
        imageFiles.add("https://i.pinimg.com/originals/21/9a/d8/219ad8cb67a82ace7579d9dbc59ec442.jpg");
        imageFiles.add("https://i.pinimg.com/originals/b7/22/7f/b7227fe3969734312c194690fb4f517d.jpg");
        for (i = 0; i < imageFiles.size(); i++) {
            imageList.add(new PreviewFile(imageFiles.get(i), ""));
        }

        Intent imagePreviewIntent = new Intent(context, ImagePreviewActivity.class);
        imagePreviewIntent.putExtra(ImagePreviewActivity.IMAGE_LIST, imageList);
        imagePreviewIntent.putExtra(ImagePreviewActivity.CURRENT_ITEM, i);
        context.startActivity(imagePreviewIntent);
    }

    @Override
    public int getItemCount() {
        return galleryList == null ? 0 : galleryList.size();
    }


    class EventFullViewGalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgEventGalleryImage;

        public EventFullViewGalleryViewHolder(View itemView) {
            super(itemView);

            imgEventGalleryImage = itemView.findViewById(R.id.img_event_gallery_item);
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

    public interface InterfaceOnItemClicked {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final InterfaceOnItemClicked itemClickListener) {
        this.clickListener = itemClickListener;
    }

}