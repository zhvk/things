package com.zhvk.things.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.zhvk.things.R;
import com.zhvk.things.databinding.DialogAddCharacterBinding;
import com.zhvk.things.databinding.FragmentHomeBinding;
import com.zhvk.things.model.CharacterPojo;
import com.zhvk.things.ui.ThingsViewModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ThingsViewModel viewModel;
    private HomeAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelStoreOwner storeOwner = Navigation.findNavController(view).getViewModelStoreOwner(R.id.nav_graph);
        viewModel = new ViewModelProvider(storeOwner).get(ThingsViewModel.class);
        viewModel.resetFocusedCharacter();

        adapter = new HomeAdapter();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
        binding.progressIndicator.setVisibility(View.VISIBLE);

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getSelectedCharacters().size() < 3)
                    Toast.makeText(getContext(), getString(R.string.error_cant_proceed), Toast.LENGTH_SHORT).show();
                else
                    Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_selectionFragment, savedInstanceState);
            }
        });
        binding.buttonAddCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddCharacterDialog();
            }
        });

        viewModel.getCharacters().observe(getViewLifecycleOwner(), newData -> {
            populateList(newData);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void populateList(ArrayList<CharacterPojo> data) {
        binding.progressIndicator.setVisibility(View.GONE);
        adapter.setDataList(data);
        adapter.notifyDataSetChanged();
    }

    private void createAddCharacterDialog() {
        DialogAddCharacterBinding dialogBinding = DialogAddCharacterBinding.inflate(LayoutInflater.from(getContext()));
        AlertDialog addCharacterDialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.add_new_character))
                .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = dialogBinding.dialogNameEditText.getText().toString().trim();

                        if (!name.isEmpty()) {
                            boolean characterAddedSuccessfully = viewModel.addNewCharacter(name,
                                    dialogBinding.dialogStatusEditText.getText().toString().trim(),
                                    dialogBinding.dialogSpeciesEditText.getText().toString().trim(),
                                    dialogBinding.dialogGenderEditText.getText().toString().trim());
                            if (characterAddedSuccessfully)
                                Toast.makeText(getContext(), getString(R.string.success_character_added), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getContext(), getString(R.string.error_character_already_exists), Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getContext(), getString(R.string.error_cant_create_character), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();

        View dialogView = dialogBinding.getRoot();
        addCharacterDialog.setView(dialogView);
        addCharacterDialog.show();
    }
}