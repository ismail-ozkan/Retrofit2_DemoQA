package com.demoqa.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class AddBook {

    private String userId;
    private List<Isbn> collectionOfIsbns;
}