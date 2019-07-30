package com.singularitycoder.tablayout.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.singularitycoder.tablayout.R;
import com.singularitycoder.tablayout.model.CreateEventGalleryModel;

import java.io.IOException;
import java.util.ArrayList;

import static com.singularitycoder.tablayout.view.CreateEventActivity.galleryAdapter;
import static com.singularitycoder.tablayout.view.CreateEventActivity.imgAddGalleryImage;
import static com.singularitycoder.tablayout.view.CreateEventActivity.imgGalleryList;

public class CreateEventGalleryAdapter extends RecyclerView.Adapter<CreateEventGalleryAdapter.CreateEventGalleryViewHolder> {

    ArrayList<CreateEventGalleryModel> galleryList;
    Context context;
    InterfaceOnItemClicked clickListener;

    public CreateEventGalleryAdapter(ArrayList<CreateEventGalleryModel> galleryList, Context context) {
        this.galleryList = galleryList;
        this.context = context;
    }

    @NonNull
    @Override
    public CreateEventGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_event_gallery, parent, false);
        return new CreateEventGalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateEventGalleryViewHolder holder, final int position) {
        CreateEventGalleryModel galleryModel = galleryList.get(position);

        Bitmap postImageBitmap = null;
        try {
            postImageBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), galleryModel.getImageUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        holder.galleryItem.setImageBitmap(postImageBitmap);
        holder.galleryItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDeleteDialog(position);
                return false;
            }
        });
    }

    private void showDeleteDialog(final int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete picture")
                .setMessage("Are you sure you want to delete this picture?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        imgGalleryList.remove(position);
                        galleryAdapter.notifyDataSetChanged();
                        imgAddGalleryImage.setBackgroundColor(context.getResources().getColor(R.color.colorSkyBlue));
                        Toast.makeText(context, "Picture Deleted", Toast.LENGTH_LONG).show();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    class CreateEventGalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView galleryItem;

        public CreateEventGalleryViewHolder(@NonNull View itemView) {
            super(itemView);
            galleryItem = itemView.findViewById(R.id.img_event_gallery_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface InterfaceOnItemClicked {
        void onStuffClicked(View view, int position);
    }

    public void setFuncOnItemClickListener(InterfaceOnItemClicked interfaceOnItemClicked) {
        this.clickListener = interfaceOnItemClicked;
    }
}
