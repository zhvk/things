package com.zhvk.things;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;
import com.zhvk.things.databinding.FragmentSelectedListBinding;
import com.zhvk.things.model.CharacterPojo;

public class SelectedListFragment extends Fragment {

    private FragmentSelectedListBinding binding;
    private ThingsViewModel viewModel;
    private SelectedListAdapter adapter;

    public SelectedListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectedListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelStoreOwner storeOwner = Navigation.findNavController(view).getViewModelStoreOwner(R.id.nav_graph);
        viewModel = new ViewModelProvider(storeOwner).get(ThingsViewModel.class);

        adapter = new SelectedListAdapter(viewModel);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
//        binding.setLifecycleOwner(this);
//        binding.setViewModel(viewModel);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        binding.buttonRandomizeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRandomCharacter();
            }
        });

        // Decided not to use DataBinding here because of issues with updating of the CardView data.
        //  For some reason, DataBinding updates data only on first entry and data is not refreshed
        //  on later changes. I suspect that the LifecycleOwner is the issue.
        viewModel.focusedCharacter.observe(getViewLifecycleOwner(), new Observer<CharacterPojo>() {
            @Override
            public void onChanged(CharacterPojo character) {
                Picasso.get()
                        .load(character.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .into(binding.characterImage);
                binding.characterName.setText(character.getName());
                binding.characterStatus.setText(character.getStatus());
                binding.characterSpecies.setText(character.getSpecies());
                binding.characterGender.setText(character.getGender());
            }
        });

        selectRandomCharacter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void selectRandomCharacter() {
        viewModel.setRandomFocusedCharacter();
//        binding.executePendingBindings();
    }
}