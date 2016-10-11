package com.vdzon.administratie.model.boekingen;


import lombok.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;

@Entity("boeking")
public abstract class Boeking {
    public abstract String getUuid();
    public abstract String getOmschrijving();
}