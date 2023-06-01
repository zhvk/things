package com.zhvk.things;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.zhvk.things.model.CharacterPojo;
import com.zhvk.things.model.ResponsePojo;
import com.zhvk.things.network.ApiClient;
import com.zhvk.things.network.ApiService;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThingsViewModel extends ViewModel {

    MutableLiveData<ArrayList<CharacterPojo>> characters = new MutableLiveData<>();

    MutableLiveData<CharacterPojo> focusedCharacter = new MutableLiveData<>();

    // TODO: Try to implement Repository pattern between VM and Retrofit
    public void loadCharacters() {
        if (characters.getValue() != null) return; // TODO: Improve a bit

        Log.d("TVM: ", "loading characters...");
        ApiService apiService = ApiClient.getApiService();
        Call<ResponsePojo> call = apiService.getCharacters();

        call.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                ResponsePojo resource = response.body();
//                ResponsePojo.ResponseInfo responseInfo = resource.responseInfo;
                ArrayList<CharacterPojo> data = resource.results;
                characters.setValue(data);
                Log.d("TVM: ", "loaded characters: " + characters.getValue());
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public ArrayList<CharacterPojo> getSelectedCharacters() {
        ArrayList<CharacterPojo> filteredList = new ArrayList<>();
        if (characters == null || characters.getValue() == null) return filteredList;

        StringBuilder filteredCharacters = new StringBuilder();
        for (CharacterPojo character : characters.getValue()) {
            if (character.isSelected()) {
                filteredList.add(character);
                filteredCharacters.append(character).append(", ");
            }
        }

        Log.d("TVM: ", "selected characters: " + filteredCharacters);
        return filteredList;
    }

    public CharacterPojo getFocusedCharacter() {
        return focusedCharacter.getValue();
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

        if (characters.getValue() == null) characters.setValue(new ArrayList<>());

        if (!characters.getValue().contains(newCharacter)) {
            characters.getValue().add(newCharacter);
            Log.d("TVM: ", "added new character: " + newCharacter);
            return true;
        } else {
            Log.d("TVM: ", "failed to add new character: " + newCharacter + ", characters: " + characters.getValue());
            return false;
        }
    }

    private void findCharacterAndSwitchSelectedStatus(CharacterPojo character) {
        int index = characters.getValue() != null ? characters.getValue().indexOf(character) : -1;
        if (index >= 0) characters.getValue().get(index).switchSelected();
    }
}
