package com.juhnny.tp07animalfinder;

import java.io.Serializable;

public class Item implements Serializable {

    String imageSrc;
    String happenDate;
    String happenPlace;

    public Item(String imageSrc, String happenDate, String happenPlace) {
        this.imageSrc = imageSrc;
        this.happenDate = happenDate;
        this.happenPlace = happenPlace;
    }

    public Item(String happenDate, String happenPlace) {
        this.happenDate = happenDate;
        this.happenPlace = happenPlace;
    }

    public Item() {
    }
}
