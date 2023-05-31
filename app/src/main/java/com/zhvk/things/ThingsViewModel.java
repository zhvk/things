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

        Log.d("TVM: " + this.hashCode(), "loading characters...");
        ApiService apiService = ApiClient.getApiService();
        Call<ResponsePojo> call = apiService.getCharacters();

        call.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                ResponsePojo resource = response.body();
//                ResponsePojo.ResponseInfo responseInfo = resource.responseInfo;
                ArrayList<CharacterPojo> data = resource.results;
                characters.setValue(data);
//                focusedCharacter.setValue(null);
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

        for (CharacterPojo character : characters.getValue()) {
            if (character.selected)
                filteredList.add(character);
        }

        Log.d("TVM: " + this.hashCode(), "selected characters: " + filteredList.toString());
        return filteredList;
    }

    public CharacterPojo getFocusedCharacter() {
        return focusedCharacter.getValue();
    }

    public void setFocusedCharacter(CharacterPojo character) {
        this.focusedCharacter.setValue(character);
        Log.d("TVM: " + this.hashCode(), "setting focused character: " + character);
    }

    public void setRandomFocusedCharacter() {
        Random randomGenerator = new Random();
        ArrayList<CharacterPojo> selectedCharacters = getSelectedCharacters();
        CharacterPojo randomCharacter = null;

//        while (randomCharacter != focusedCharacter.getValue()) {
        int index = randomGenerator.nextInt(selectedCharacters.size());
        Log.d("TVM: " + this.hashCode(), "random index: " + index + ", size: " + selectedCharacters.size());
        randomCharacter = selectedCharacters.get(index);
//        }

        focusedCharacter.setValue(randomCharacter);
        Log.d("TVM: " + this.hashCode(), "setting random focused character: " + randomCharacter);
    }

    public void resetFocusedCharacter() {
        setFocusedCharacter(null);
    }

    public void addNewCharacter(String name, String status, String species, String gender) {
        CharacterPojo newCharacter = new CharacterPojo();
        newCharacter.setName(name);
        newCharacter.setStatus(status);
        newCharacter.setSpecies(species);
        newCharacter.setGender(gender);
        newCharacter.setImageUrl("https://picsum.photos/200");

        if (characters.getValue() == null) characters.setValue(new ArrayList<>());
        characters.getValue().add(newCharacter);

        Log.d("TVM: " + this.hashCode(), "adding new character: " + newCharacter);
    }
}
