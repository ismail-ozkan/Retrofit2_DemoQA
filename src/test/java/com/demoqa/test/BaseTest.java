package com.demoqa.test;

import com.demoqa.pojo.User;
import com.demoqa.pojo.UserInformation;
import com.demoqa.pojo.User;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseTest {

    public static ApiService apiService;
    public static User user;
    public static UserInformation userInformation;

    @BeforeEach
    public void setUp() {
        // Retrofit servisini oluştur
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

    }


}