package com.gamla.festin.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.palette.graphics.Palette;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gamla.festin.R;
import com.gamla.festin.databinding.FragmentDetailBinding;
import com.gamla.festin.model.DogBreed;
import com.gamla.festin.model.DogPalette;
import com.gamla.festin.util.Util;
import com.gamla.festin.viewmodel.DetailViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class DetailFragment extends Fragment {


    private int dogUuid;
    private DetailViewModel detailViewModel;
    private FragmentDetailBinding binding;

//    @BindView(R.id.dogImage)
//    ImageView dogImage;
//
//    @BindView(R.id.dogName)
//    TextView dogName;
//
//    @BindView(R.id.dogPurpose)
//    TextView dogPurpose;
//
//    @BindView(R.id.dogTemperament)
//    TextView dogTemperament;
//
//    @BindView(R.id.dogLifespan)
//    TextView dogLifespan;

    public DetailFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//      View view = inflater.inflate(R.layout.fragment_detail, container, false);
//      ButterKnife.bind(this, view);
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container,false);
        this.binding = binding;
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            dogUuid = DetailFragmentArgs.fromBundle(getArguments()).getDogUuid();
        }

        detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        detailViewModel.fetch(dogUuid);

        observeViewModel();

    }

    void observeViewModel() {
        detailViewModel.dogLiveData.observe(this, dogBreed -> {
            if(dogBreed != null && dogBreed instanceof DogBreed && getContext() != null){
                binding.setDog(dogBreed);
//                bin.setText(dogBreed.dogBreed);
//                dogPurpose.setText(dogBreed.breedFor);
//                dogTemperament.setText(dogBreed.temperament);
//                dogLifespan.setText(dogBreed.lifeSpan);
//
//                if(dogBreed.imageUrl != null){
//                    Util.loadImage(dogImage, dogBreed.imageUrl, new CircularProgressDrawable(getContext()));
//                }
                if(dogBreed.imageUrl != null){
                    setupBg(dogBreed.imageUrl);
                }

            }
        });
    }
    private void setupBg(String url){
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                                .generate(palette -> {
                                    int intColor = palette.getLightMutedSwatch().getRgb();
                                    DogPalette myPalette = new DogPalette((intColor));
                                    binding.setPalette(myPalette);
                                });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}
