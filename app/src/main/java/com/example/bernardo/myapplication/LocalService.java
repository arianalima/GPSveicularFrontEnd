package com.example.bernardo.myapplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LocalService {

    @GET("getultimo")
    Call<ArrayList<Float>> listRepos();

}
