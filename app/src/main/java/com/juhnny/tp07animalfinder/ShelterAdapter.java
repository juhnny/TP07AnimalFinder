package com.juhnny.tp07animalfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.juhnny.tp07animalfinder.databinding.ShelterRecyclerBinding;

public class ShelterAdapter extends RecyclerView.Adapter<ShelterAdapter.VH> {


    Context context;

    public ShelterAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.shelter_recycler, parent, false);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Shelter shelter = G.shelters.get(position);

        holder.binding.tvShelterName.setText(shelter.shelterName);
    }

    @Override
    public int getItemCount() {
        return G.shelters.size();
    }

    class VH extends RecyclerView.ViewHolder {

        ShelterRecyclerBinding binding;

        public VH(@NonNull View itemView) {
            super(itemView);
            binding = ShelterRecyclerBinding.bind(itemView);

        }
    }
}
