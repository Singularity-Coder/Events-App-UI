package com.singularitycoder.tablayout;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

public class Helpers {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMan.getActiveNetworkInfo() != null;
    }

    public static void showSnack(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public static void showToast(Context context, String message, int length) {
        switch (length) {
            case 0:
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                break;
        }
    }

    public static void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(550);
        view.startAnimation(anim);
    }

    public static void loadImageWithGlide(Context context, String imgUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.bg_light_grey)
                .error(R.drawable.header) // When unable to load image url fallback on this
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); // Faster loads on the subsequent runs

        Glide.with(context).load(imgUrl)
                .apply(requestOptions)
                .into(imageView);
    }
}
