package com.zhvk.things;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.zhvk.things.databinding.ItemCharacterSelectedBinding;
import com.zhvk.things.model.CharacterPojo;

import java.util.Objects;

public class SelectedListAdapter extends ListAdapter<CharacterPojo, SelectedListAdapter.SelectedListViewHolder> {

    private final ThingsViewModel viewModel;

    public SelectedListAdapter(ThingsViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SelectedListAdapter.SelectedListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCharacterSelectedBinding binding = ItemCharacterSelectedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        SelectedListViewHolder holder = new SelectedListViewHolder(binding);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition(); // TODO: getLayoutPosition() difference?
                viewModel.setFocusedCharacter(getItem(position));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedListAdapter.SelectedListViewHolder holder, int position) {
        holder.bind(getItem(position), getAlphaForPosition(position));
    }

    public static final DiffUtil.ItemCallback<CharacterPojo> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CharacterPojo>() {
                @Override
                public boolean areItemsTheSame(@NonNull CharacterPojo oldUser, @NonNull CharacterPojo newUser) {
                    // Should be ID but in this sample project, we are using name as primary identifier
                    return Objects.equals(oldUser.getName(), newUser.getName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull CharacterPojo oldUser, @NonNull CharacterPojo newUser) {
                    return oldUser.equals(newUser);
                }
            };

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
