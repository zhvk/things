package com.zhvk.things;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhvk.things.databinding.ItemCharacterSelectedBinding;
import com.zhvk.things.model.CharacterPojo;

import java.util.ArrayList;

public class SelectedListAdapter extends RecyclerView.Adapter<SelectedListAdapter.SelectedListViewHolder> {

    private final ThingsViewModel viewModel;
    private final ArrayList<CharacterPojo> dataList;

    public SelectedListAdapter(ThingsViewModel viewModel) {
        this.viewModel = viewModel;
        this.dataList = viewModel.getSelectedCharacters();
    }

    @NonNull
    @Override
    public SelectedListAdapter.SelectedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCharacterSelectedBinding binding = ItemCharacterSelectedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        SelectedListAdapter.SelectedListViewHolder holder =  new SelectedListAdapter.SelectedListViewHolder(binding);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                viewModel.setFocusedCharacter(dataList.get(position));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedListAdapter.SelectedListViewHolder holder, int position) {
        CharacterPojo character = dataList.get(position);
        holder.bind(character);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class SelectedListViewHolder extends RecyclerView.ViewHolder {
        public ItemCharacterSelectedBinding binding;

        SelectedListViewHolder(ItemCharacterSelectedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CharacterPojo character) {
            binding.characterName.setText(character.name);
        }
    }
}
