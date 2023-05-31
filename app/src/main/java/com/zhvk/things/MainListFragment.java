package com.zhvk.things;

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

import com.google.android.material.textfield.TextInputLayout;
import com.zhvk.things.databinding.FragmentMainListBinding;
import com.zhvk.things.model.CharacterPojo;

import java.util.ArrayList;

public class MainListFragment extends Fragment {

    private FragmentMainListBinding binding;
    private ThingsViewModel viewModel;
    private AlertDialog addCharacterDialog;

    //    private ThingsViewModel viewModel;
    private MainListAdapter adapter;

    public MainListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewModelStoreOwner storeOwner = Navigation.findNavController(view).getViewModelStoreOwner(R.id.nav_graph);
        viewModel = new ViewModelProvider(storeOwner).get(ThingsViewModel.class);
        viewModel.loadCharacters();

        adapter = new MainListAdapter();
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
        binding.progressIndicator.setVisibility(View.VISIBLE);

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel.getSelectedCharacters().size() < 3)
                    Toast.makeText(getContext(), getString(R.string.error_cant_proceed), Toast.LENGTH_SHORT).show();
                else
                    Navigation.findNavController(view).navigate(
                            R.id.action_mainFragment_to_selectionFragment,
                            savedInstanceState);
            }
        });

        binding.buttonAddCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCharacterDialog();
            }
        });

        viewModel.characters.observe(getViewLifecycleOwner(), newData -> {
            populateList(newData);
        });

        createAddCharacterDialog();
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
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View dialogView = factory.inflate(R.layout.dialog_add_character, null);
        addCharacterDialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.add_new_character))
                .setPositiveButton(getString(R.string.add), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Improve this to use ViewBinding
                        TextInputLayout nameView = (TextInputLayout) ((AlertDialog) dialog).findViewById(R.id.dialog_name_view);
                        viewModel.addNewCharacter(nameView.getEditText().getText().toString().trim());
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        addCharacterDialog.setView(dialogView);
    }

    private void showAddCharacterDialog() {
        addCharacterDialog.show();
    }
}