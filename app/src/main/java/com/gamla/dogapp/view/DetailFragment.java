package com.gamla.dogapp.view;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gamla.dogapp.R;
import com.gamla.dogapp.databinding.FragmentDetailBinding;
import com.gamla.dogapp.databinding.SendSmsDialogBinding;
import com.gamla.dogapp.model.DogBreed;
import com.gamla.dogapp.model.DogPalette;
import com.gamla.dogapp.model.SmsInfo;
import com.gamla.dogapp.viewmodel.DetailViewModel;

public class DetailFragment extends Fragment {

    private int dogUuid;
    private DetailViewModel detailViewModel;
    private FragmentDetailBinding binding;
    private Boolean sendSmsStarted = false;

    private DogBreed currentDog;

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
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        this.binding = binding;
        setHasOptionsMenu(true);
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
            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                currentDog = dogBreed;
                binding.setDog(dogBreed);
//                bin.setText(dogBreed.dogBreed);
//                dogPurpose.setText(dogBreed.breedFor);
//                dogTemperament.setText(dogBreed.temperament);
//                dogLifespan.setText(dogBreed.lifeSpan);
//
//                if(dogBreed.imageUrl != null){
//                    Util.loadImage(dogImage, dogBreed.imageUrl, new CircularProgressDrawable(getContext()));
//                }
                if (dogBreed.imageUrl != null) {
                    setupBg(dogBreed.imageUrl);
                }

            }
        });
    }

    private void setupBg(String url) {
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send_sms: {
                if (!sendSmsStarted) {
                    sendSmsStarted = true;
                    ((MainActivity) getActivity()).checkSmsPermisions();
                }
                /*Toast.makeText(getContext(), "Action send sms", Toast.LENGTH_LONG).show();*/
                break;
            }
            case R.id.action_share: {
//                Toast.makeText(getContext(), "Action share", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed");
                intent.putExtra(Intent.EXTRA_TEXT, currentDog.dogBreed + " breed for " + currentDog.breedFor);
                intent.putExtra(Intent.EXTRA_STREAM, currentDog.imageUrl);
                startActivity(Intent.createChooser(intent, "Share With"));
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onPermissionResult(Boolean permissionGranted) {
        if (isAdded() && sendSmsStarted && permissionGranted) {
            SmsInfo smsInfo = new SmsInfo("", currentDog.dogBreed + " breed for " + currentDog.breedFor, currentDog.imageUrl);
            SendSmsDialogBinding dialogBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),
                    R.layout.send_sms_dialog,
                    null,
                    false
            );

            new AlertDialog.Builder(getContext())
                    .setView(dialogBinding.getRoot())
                    .setPositiveButton("Send SMS", ((dialog, which) -> {
                        if(!dialogBinding.smsDestination.getText().toString().isEmpty()){
                            smsInfo.to = dialogBinding.smsDestination.getText().toString();
                            sendSMS(smsInfo);
                        }
                    }))
                    .setNegativeButton("Cancel", ((dialog, which) -> {}))
                    .show();
            sendSmsStarted = false;
            dialogBinding.setSmsInfo(smsInfo);
        }
    }

    private void sendSMS(SmsInfo smsInfo){
        Intent intent = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(smsInfo.to, null, smsInfo.text, pendingIntent, null);
    }
}
