package com.demoqa.test;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private String accessToken;

    public AuthInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Add the access token as a header
        Request modifiedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();

        return chain.proceed(modifiedRequest);
    }



}
