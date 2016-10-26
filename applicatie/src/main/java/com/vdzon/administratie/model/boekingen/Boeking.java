package com.vdzon.administratie.model.boekingen;

import org.mongodb.morphia.annotations.Entity;

@Entity("boeking")
public abstract class Boeking {
    public abstract String getUuid();
    public abstract String getOmschrijving();
}
