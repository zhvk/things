package com.zhvk.things.model;

import com.google.gson.annotations.SerializedName;

public class CharacterPojo {

    // TODO: Da li za ove Retrofit objekte treba da budu public fields?
    // TODO: 2 Da li koristimo getere i setere ili direktno mozemo da menjamo?

    @SerializedName("id")
    public Integer id;

    @SerializedName("name")
    public String name;

    @SerializedName("status")
    public String status;

    @SerializedName("species")
    public String species;

    @SerializedName("gender")
    public String gender;

    @SerializedName("image")
    public String imageUrl;

    public boolean selected = false;

    public void select() {
        selected = !selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
