package com.demoqa.test;

import com.demoqa.pojo.*;
import com.github.javafaker.Faker;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.*;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test extends BaseTest {

    static User user = new User();
    static String userID;
    static String token;


    @org.junit.jupiter.api.Test()
    @Order(1)
    public void createUserTest() {

        Faker faker = new Faker();
        String userName = faker.name().firstName() + faker.number().numberBetween(1, 10);
        System.out.println("userName = " + userName);
        String password = "Pass471!";
        user.setUserName(userName);
        user.setPassword(password);

        Call<UserInformation> call = apiService.createUser(user);

        try {
            // creating API request and getting result
            Response<UserInformation> response = call.execute();

            // check if respond is successful or not
            Assertions.assertTrue(response.isSuccessful());

            // check if respond user is not null or not
            userInformation = response.body();
            System.out.println("userInformation = " + userInformation.getUserID());
            Assertions.assertNotNull(userInformation);

            //Check the first user name
            String expectedName = user.getUserName();
            String actualName = userInformation.getUsername();
            Assertions.assertEquals(expectedName, actualName);

        } catch (IOException e) {
            e.printStackTrace();
            Assertions.fail("API request FAILED: " + e.getMessage());
        }

    }

    @org.junit.jupiter.api.Test
    @Order(2)
    public void generateAuthenticationTest() {

        Call<TokenResponse> call = apiService.generateToken(user);

        try {
            Response<TokenResponse> response = call.execute();

            TokenResponse tokenResponse = response.body();

            System.out.println("tokenResponse.getToken() = " + tokenResponse.getToken());
            token = tokenResponse.getToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @org.junit.jupiter.api.Test
    @Order(3)
    public void getListOfBooksTest() {
        Call<AllBooks> call = apiService.getBooks();

        try {

            Response<AllBooks> response = call.execute();

            AllBooks books = response.body();
            System.out.println("books.getBooks().size() = " + books.getBooks().size());
            assertTrue(response.isSuccessful());
            for (Book book : books.getBooks()) {
                assertThat(book.getTitle(), is(notNullValue()));
                assertNotNull(book.getAuthor(), is(notNullValue()));
                assertNotNull(book.getIsbn(), is(notNullValue()));
                assertNotNull(book.getDescription(), is(notNullValue()));
                assertNotNull(book.getWebsite(), is(notNullValue()));
                assertNotNull(book.getSubTitle(), is(notNullValue()));
                assertNotNull(book.getPublish_date(), is(notNullValue()));
                assertNotNull(book.getPublisher(), is(notNullValue()));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @org.junit.jupiter.api.Test
    @Order(4)
    public void filterByPublisherOrAuthorTest() {
        System.out.println(booksOfFilteredByAuthor("Richard E. Silverman"));
    }

    public ArrayList<Book> booksOfFilteredByAuthor(String author) {

        Call<AllBooks> call = apiService.getBooks();
        ArrayList<Book> booksOfFiltered = new ArrayList<>();

        try {
            Response<AllBooks> response = call.execute();

            AllBooks books = response.body();
            for (Book book : books.getBooks()) {
                if (book.getAuthor().equals(author)) {
                    booksOfFiltered.add(book);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return booksOfFiltered;
    }


    // I have spent 6 hours to handle this test case. I could not handle it with Bearer Token. However it works in RestAssured.

    @org.junit.jupiter.api.Test
    @Order(5)
    public void postBooksToTheUserInContext() {

        System.out.println("userInformation.getUserID() = " + userInformation.getUserID());
        System.out.println("token = " + token);

        AuthInterceptor authInterceptor = new AuthInterceptor(token);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(authInterceptor);

        ApiService apiService1 = RetrofitClient.getRetrofitInstance(token).create(ApiService.class);

        AddBook addBook = new AddBook();
        addBook.setUserId(userInformation.getUserID());
        Isbn isbn = new Isbn();
        isbn.setIsbn("9781449325862");
        List<Isbn> booksList = new ArrayList<>();
        booksList.add(isbn);
        addBook.setCollectionOfIsbns(booksList);

        Call<Isbn> addingBookRequest = apiService1.addBook(addBook);

        try {
            Response<Isbn> bookResponse = addingBookRequest.execute();
            Assertions.assertTrue(bookResponse.isSuccessful());
            Isbn body = bookResponse.body();
            System.out.println("body.getIsbn() = " + body.getIsbn());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Call<UserInformation> userInfo = apiService.getUser(userInformation.getUserID());
        try {
            Response<UserInformation> getUserResponse = userInfo.execute();
            userInformation = getUserResponse.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("userInformation.getUserID() = " + userInformation.getUserID());
        System.out.println("userInformation = " + userInformation);

        List<Book> booksOfUser = userInformation.getBooks();
        System.out.println("booksOfUser = " + booksOfUser);
        ArrayList<String> booksISBN = new ArrayList<>();
        for (Book book : booksOfUser) {
            booksISBN.add(book.getIsbn());
        }



    }

}