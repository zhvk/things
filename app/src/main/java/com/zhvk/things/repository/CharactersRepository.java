package com.zhvk.things.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zhvk.things.model.CharacterPojo;
import com.zhvk.things.model.ResponsePojo;
import com.zhvk.things.repository.network.ApiClient;
import com.zhvk.things.repository.network.ApiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharactersRepository {

    private static CharactersRepository repository;

    private ApiService apiService;
    private MutableLiveData<ArrayList<CharacterPojo>> charactersLiveData = new MutableLiveData<>();

    public CharactersRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public static CharactersRepository getInstance() {
        if (repository == null)
            repository = new CharactersRepository(ApiClient.getApiServiceInstance());
        return repository;
    }

    // Return LiveData because MutableLiveData object can be changed only by this class
    public LiveData<ArrayList<CharacterPojo>> getCharacters() {
        // Don't load new data from API if data is already loaded
        if (charactersLiveData.getValue() != null) return charactersLiveData;

        Call<ResponsePojo> call = apiService.getCharacters();
        call.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                ResponsePojo resource = response.body();
//                ResponsePojo.ResponseInfo responseInfo = resource.responseInfo;
                ArrayList<CharacterPojo> data = resource.results;
                charactersLiveData.setValue(data);
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                call.cancel();
            }
        });
        return charactersLiveData;
    }

    // Don't add character if they are already added and return false
    public boolean addCharacter(CharacterPojo newCharacter) {
        if (charactersLiveData.getValue() == null) charactersLiveData.setValue(new ArrayList<>());

        if (!charactersLiveData.getValue().contains(newCharacter)) {
            charactersLiveData.getValue().add(newCharacter);
            return true;
        } else
            return false;
    }
}
