package com.zhvk.things;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.zhvk.things.databinding.ItemCharacterMainBinding;
import com.zhvk.things.model.CharacterPojo;

import java.util.ArrayList;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListViewHolder> {

    private ArrayList<CharacterPojo> dataList;

    public MainListAdapter() {
        dataList = new ArrayList<>();
    }

    public MainListAdapter(ArrayList<CharacterPojo> dataList) {
        setDataList(dataList);
    }

    @NonNull
    @Override
    public MainListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCharacterMainBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_character_main,
                parent,
                false);
        MainListViewHolder holder = new MainListViewHolder(binding);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                dataList.get(position).select();
                notifyItemChanged(position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainListViewHolder holder, int position) {
        CharacterPojo character = dataList.get(position);
        holder.bind(character);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(ArrayList<CharacterPojo> dataList) {
        this.dataList = dataList;
    }

    static class MainListViewHolder extends RecyclerView.ViewHolder {
        public ItemCharacterMainBinding binding;

        MainListViewHolder(ItemCharacterMainBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CharacterPojo character) {
            binding.setCharacter(character);
            binding.executePendingBindings();   // TODO: Is this needed here?
        }
    }
}
