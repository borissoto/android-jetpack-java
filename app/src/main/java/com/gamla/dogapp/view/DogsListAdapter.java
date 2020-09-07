package com.gamla.dogapp.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.gamla.dogapp.R;
import com.gamla.dogapp.databinding.ItemDogBinding;
import com.gamla.dogapp.model.DogBreed;

import java.util.ArrayList;
import java.util.List;

public class DogsListAdapter extends RecyclerView.Adapter<DogsListAdapter.DogViewHolder> implements DogClickListener {

    private ArrayList<DogBreed> dogList;

    public DogsListAdapter(ArrayList<DogBreed> dogList) {
        this.dogList = dogList;
    }

    public void updateDogsList(List<DogBreed> newDogList) {
        dogList.clear();
        dogList.addAll(newDogList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemDogBinding view = DataBindingUtil.inflate(inflater, R.layout.item_dog, parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dog, parent, false);
        return new DogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
        holder.itemView.setDog(dogList.get(position));
        holder.itemView.setListener(this);
//        ImageView image = holder.itemView.findViewById(R.id.imageView);
//        TextView name = holder.itemView.findViewById(R.id.name);
//        TextView lifespan = holder.itemView.findViewById(R.id.lifespan);
//        LinearLayout layout = holder.itemView.findViewById(R.id.dogLayout);
//
//        name.setText(dogList.get(position).dogBreed);
//        lifespan.setText(dogList.get(position).dogBreed);
//        Util.loadImage(image, dogList.get(position).imageUrl, Util.getProgressDrawable(image.getContext()));
//        layout.setOnClickListener(v -> {
//            PrincipalFragmentDirections.ActionPrincipalFragmentToDetailFragment action = PrincipalFragmentDirections.actionPrincipalFragmentToDetailFragment();
//            action.setDogUuid(dogList.get(position).uui);
//            Navigation.findNavController(layout).navigate(action);
//        });
    }

    @Override
    public int getItemCount() {
        return dogList.size();
    }

    @Override
    public void onDogClicked(View view) {
        String uuidString = ((TextView) view.findViewById(R.id.dogId)).getText().toString();
        int uuid = Integer.valueOf(uuidString);
        PrincipalFragmentDirections.ActionPrincipalFragmentToDetailFragment action = PrincipalFragmentDirections.actionPrincipalFragmentToDetailFragment();
        action.setDogUuid(uuid);
        Navigation.findNavController(view).navigate(action);
    }

    class DogViewHolder extends RecyclerView.ViewHolder {

        public ItemDogBinding itemView;

        public DogViewHolder(@NonNull ItemDogBinding itemDogBinding) {
            super(itemDogBinding.getRoot());
            this.itemView = itemDogBinding;
        }
    }
}
