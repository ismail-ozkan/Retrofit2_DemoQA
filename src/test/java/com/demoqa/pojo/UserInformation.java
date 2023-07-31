package com.demoqa.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString
public class UserInformation {

    private String userID;
    private String username;
    private List<Book> books;
}