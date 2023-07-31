package com.demoqa.test;

import com.demoqa.pojo.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {


    @GET("/BookStore/v1/Books")
    Call<AllBooks> getBooks();

    @POST("/Account/v1/User")
    Call<UserInformation> createUser(@Body User user);

    @POST("/Account/v1/GenerateToken")
    Call<TokenResponse> generateToken(@Body User user);

    @GET("/Account/v1/User/{UUID}")
    Call<UserInformation> getUser(@Path("UUID") String uuid);

    @POST("/BookStore/v1/Books")
    Call<Isbn> addBook(@Body AddBook addBookBody);

}