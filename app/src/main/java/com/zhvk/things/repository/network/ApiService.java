package com.zhvk.things.repository.network;

import com.zhvk.things.model.ResponsePojo;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/api/character")
    Call<ResponsePojo> getCharacters();
}
