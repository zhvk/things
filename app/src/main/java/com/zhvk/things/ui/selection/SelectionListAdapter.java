package com.zhvk.things.ui.selection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zhvk.things.databinding.ItemCharacterSelectedBinding;
import com.zhvk.things.model.CharacterPojo;
import com.zhvk.things.ui.ThingsViewModel;

import java.util.Objects;

public class SelectionListAdapter extends ListAdapter<CharacterPojo, SelectionListAdapter.SelectedListViewHolder> {

    private final ThingsViewModel viewModel;

    public SelectionListAdapter(ThingsViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SelectionListAdapter.SelectedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCharacterSelectedBinding binding = ItemCharacterSelectedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        SelectedListViewHolder holder = new SelectedListViewHolder(binding);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                viewModel.setFocusedCharacter(getItem(position));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionListAdapter.SelectedListViewHolder holder, int position) {
        holder.bind(getItem(position), getAlphaForPosition(position));
    }

    public static final DiffUtil.ItemCallback<CharacterPojo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CharacterPojo>() {
                @Override
                public boolean areItemsTheSame(@NonNull CharacterPojo oldUser, @NonNull CharacterPojo newUser) {
                    // Should be ID but in this simple project, we are using name as primary identifier
                    return Objects.equals(oldUser.getName(), newUser.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull CharacterPojo oldUser, @NonNull CharacterPojo newUser) {
                    return oldUser.equals(newUser);
                }
            };

    // Method that dynamically sets different shade of gray background to VH items based on their position.
    //  This logic could use some improvements on this screen because order of items changes.
    private float getAlphaForPosition(int index) {
        return 1f - (0.65f * (index / (getItemCount() - 1f)));
    }

    static class SelectedListViewHolder extends RecyclerView.ViewHolder {
        public ItemCharacterSelectedBinding binding;

        SelectedListViewHolder(ItemCharacterSelectedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CharacterPojo character, float alpha) {
            binding.frameLayout.setAlpha(alpha);
            binding.characterName.setText(character.getName());
        }
    }
}
