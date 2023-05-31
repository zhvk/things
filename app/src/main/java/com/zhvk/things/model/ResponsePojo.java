package com.zhvk.things.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponsePojo {

    @SerializedName("info")
    public ResponseInfo responseInfo;

    @SerializedName("results")
    public ArrayList<CharacterPojo> results;

    public class ResponseInfo {

        @SerializedName("count")
        public Integer count;

        @SerializedName("pages")
        public Integer pages;

        @SerializedName("next")
        public String next;

        @SerializedName("prev")
        public String previous;
    }
}
