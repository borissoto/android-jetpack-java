package com.gamla.festin.util;

import android.content.Context;
import android.graphics.ColorFilter;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gamla.festin.R;

public class Util {

    public static void loadImage(ImageView imageView, String url, CircularProgressDrawable progressDrawable){
        RequestOptions options = new RequestOptions()
                .placeholder(progressDrawable)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext())
                .setDefaultRequestOptions(options)
                .load(url)
                .into(imageView);
    }
    public static CircularProgressDrawable getProgressDrawable(Context context){
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(2f);
        circularProgressDrawable.setCenterRadius(50f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }

    @BindingAdapter("android:imageUrl")
    public static void loadImage(ImageView imageView, String url){
        loadImage(imageView, url, getProgressDrawable(imageView.getContext()));
    }
}
