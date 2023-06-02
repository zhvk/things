package com.zhvk.things.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhvk.things.databinding.ItemCharacterMainBinding;
import com.zhvk.things.model.CharacterPojo;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MainListViewHolder> {

    private ArrayList<CharacterPojo> dataList;

    public HomeAdapter() {
        dataList = new ArrayList<>();
    }

    public HomeAdapter(ArrayList<CharacterPojo> dataList) {
        setDataList(dataList);
    }

    @NonNull
    @Override
    public MainListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCharacterMainBinding binding = ItemCharacterMainBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        MainListViewHolder holder = new MainListViewHolder(binding);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                dataList.get(position).switchSelected();
                notifyItemChanged(position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainListViewHolder holder, int position) {
        CharacterPojo character = dataList.get(position);
        holder.bind(character, getAlphaForPosition(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(ArrayList<CharacterPojo> dataList) {
        this.dataList = dataList;
    }

    // Method that dynamically sets different shade of gray background to VH items based on their position.
    private float getAlphaForPosition(int index) {
        return 1f - (0.65f * (index / (dataList.size() - 1f)));
    }

    static class MainListViewHolder extends RecyclerView.ViewHolder {
        public ItemCharacterMainBinding binding;

        MainListViewHolder(ItemCharacterMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CharacterPojo character, float alpha) {
            binding.frameLayout.setAlpha(alpha);

            if (character.isSelected()) binding.characterSelectedImage.setVisibility(View.VISIBLE);
            else binding.characterSelectedImage.setVisibility(View.GONE);

            binding.characterName.setText(character.getName());
        }
    }
}
