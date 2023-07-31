package com.demoqa.test;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://demoqa.com";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getRetrofitInstance(String token) {

        AuthInterceptor authInterceptor = new AuthInterceptor(token);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(authInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://demoqa.com") // Replace with your API base URL
                    .addConverterFactory(GsonConverterFactory.create()) // Replace with your preferred converter
                    .client(httpClientBuilder.build())
                    .build();

        return retrofit;
    }

}