package com.zhvk.things;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import com.zhvk.things.databinding.FragmentSelectedListBinding;
import com.zhvk.things.model.CharacterPojo;

public class SelectedListFragment extends Fragment {

    private FragmentSelectedListBinding binding;
    private ThingsViewModel viewModel;
    private SelectedListAdapter adapter;

    private final int shortAnimationDuration = 300;

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

        binding.cardView.setVisibility(View.INVISIBLE);

        adapter = new SelectedListAdapter(viewModel);
        adapter.submitList(viewModel.getSelectedCharacters());
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.resetFocusedCharacter();
                getActivity().onBackPressed();
            }
        });
        binding.buttonRandomizeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRandomCharacter();
            }
        });

        viewModel.focusedCharacter.observe(getViewLifecycleOwner(), new Observer<CharacterPojo>() {
            @Override
            public void onChanged(CharacterPojo character) {
                if (character != null) {
                    String statusText = String.format(getString(R.string.status_formatter), character.getStatus());
                    String speciesText = String.format(getString(R.string.species_formatter), character.getSpecies());
                    String genderText = String.format(getString(R.string.gender_formatter), character.getGender());

                    fadeOutCardView(character.getName(), statusText, speciesText, genderText);
                }

                // TODO: Check how this can be optimized
                adapter.submitList(viewModel.getSelectedCharacters());
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
    }

    private void fadeOutCardView(String nameText, String statusText, String speciesText, String genderText) {
        // Start values
        binding.cardView.setAlpha(1f);

        // Animation
        binding.cardView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        binding.cardView.setVisibility(View.INVISIBLE);

                        // Chaining two animations
                        fadeInCardView(nameText, statusText, speciesText, genderText);
                    }
                });
    }

    private void fadeInCardView(String nameText, String statusText, String speciesText, String genderText) {
        // Setting text values while the CardView is not visible
        binding.characterName.setText(nameText);
        binding.characterStatus.setText(statusText);
        binding.characterSpecies.setText(speciesText);
        binding.characterGender.setText(genderText);

        // Start values
        binding.cardView.setAlpha(0f);
        binding.cardView.setVisibility(View.VISIBLE);

        // Animation
        binding.cardView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);
    }
}