package com.example.rusheta.service.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
//    private static final String BASE_URL = "https://rusheta.herokuapp.com/";
            private static final String BASE_URL = "http://10.0.2.2:3000";
//    private static final String BASE_URL = "http://localhost:3000";

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static JsonApiPlaceHolder getInterface() {
        return retrofit.create(JsonApiPlaceHolder.class);
    }
}
