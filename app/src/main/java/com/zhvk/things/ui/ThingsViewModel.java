package com.zhvk.things.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zhvk.things.model.CharacterPojo;
import com.zhvk.things.repository.CharactersRepository;

import java.util.ArrayList;
import java.util.Random;

public class ThingsViewModel extends ViewModel {

    // Repository handles loading data received from the API and adding of new local items
    private CharactersRepository repository = CharactersRepository.getInstance();

    private MutableLiveData<CharacterPojo> focusedCharacter = new MutableLiveData<>();

    public LiveData<ArrayList<CharacterPojo>> getCharacters() {
        return repository.getCharacters();
    }

    public ArrayList<CharacterPojo> getSelectedCharacters() {
        ArrayList<CharacterPojo> filteredList = new ArrayList<>();
        if (repository.getCharacters().getValue() == null) return filteredList;

        StringBuilder filteredCharacters = new StringBuilder();
        for (CharacterPojo character : repository.getCharacters().getValue()) {
            if (character.isSelected()) {
                filteredList.add(character);
                filteredCharacters.append(character).append(", ");
            }
        }

        Log.d("TVM: ", "selected characters: " + filteredCharacters);
        return filteredList;
    }

    public LiveData<CharacterPojo> getFocusedCharacter() {
        return focusedCharacter;
    }

    public void setFocusedCharacter(CharacterPojo character) {
        // Switching selected status for previously focused character if there are any
        findCharacterAndSwitchSelectedStatus(this.focusedCharacter.getValue());
        // Switching selected status for new focused character
        findCharacterAndSwitchSelectedStatus(character);
        // Setting new focused character
        this.focusedCharacter.setValue(character);
        Log.d("TVM: ", "setting focused character: " + character);
    }

    public void setRandomFocusedCharacter() {
        Random randomGenerator = new Random();
        ArrayList<CharacterPojo> selectedCharacters = getSelectedCharacters();
        CharacterPojo randomCharacter = null;

//        while (randomCharacter != focusedCharacter.getValue()) {
        int index = randomGenerator.nextInt(selectedCharacters.size());
        Log.d("TVM: ", "random index: " + index + ", size: " + selectedCharacters.size());
        randomCharacter = selectedCharacters.get(index);
//        }

        setFocusedCharacter(randomCharacter);
        Log.d("TVM: ", "setting random focused character: " + randomCharacter);
    }

    public void resetFocusedCharacter() {
        setFocusedCharacter(null);
    }

    public boolean addNewCharacter(String name, String status, String species, String gender) {
        CharacterPojo newCharacter = new CharacterPojo(name, status, species, gender);
        return repository.addCharacter(newCharacter);
    }

    private void findCharacterAndSwitchSelectedStatus(CharacterPojo character) {
        int index = repository.getCharacters().getValue() != null ? repository.getCharacters().getValue().indexOf(character) : -1;
        if (index >= 0) repository.getCharacters().getValue().get(index).switchSelected();
    }
}
