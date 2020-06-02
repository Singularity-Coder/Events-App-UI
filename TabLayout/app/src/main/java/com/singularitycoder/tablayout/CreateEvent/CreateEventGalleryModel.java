package com.singularitycoder.tablayout.CreateEvent;

import android.net.Uri;

public class CreateEventGalleryModel {

    private Uri imageUri;

    public CreateEventGalleryModel(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
