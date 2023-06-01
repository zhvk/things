package com.zhvk.things.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class CharacterPojo {

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status;

    @SerializedName("species")
    private String species;

    @SerializedName("gender")
    private String gender;

    private boolean selected;

    public CharacterPojo(String name, String status, String species, String gender) {
        this.name = name;
        this.status = status;
        this.species = species;
        this.gender = gender;
        this.selected = false;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final CharacterPojo other = (CharacterPojo) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }

        if (!Objects.equals(this.status, other.status)) {
            return false;
        }

        if (!Objects.equals(this.species, other.species)) {
            return false;
        }

        if (!Objects.equals(this.gender, other.gender)) {
            return false;
        }

        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return isNullOrEmpty(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return isNullOrEmpty(species);
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return isNullOrEmpty(gender);
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void switchSelected() {
        this.selected = !this.selected;
    }

    private String isNullOrEmpty(String string) {
        return string == null || string.isEmpty() ? "Unknown" : string;
    }
}
