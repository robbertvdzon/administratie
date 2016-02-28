package com.vdzon.weegschaal.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.List;

@Entity("gewicht")
public class Customer {

    @Id
    private long id;
    private String name;
    private List<Gewicht> gewichten;

    public Customer(String name, long id, List<Gewicht> gewichten) {
        this.name = name;
        this.id = id;
        this.gewichten = gewichten;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public List<Gewicht> getGewichten() {
        return gewichten;
    }
}
