package com.vdzon.weegschaal.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity("gewicht")
public class Gewicht {

    private String gewicht;

    @Id
    private String uuid;

    public Gewicht() {
    }

    public Gewicht(String gewicht, String uuid) {
        this.gewicht = gewicht;
        this.uuid = uuid;
    }

    public void setGewicht(String gewicht) {
        this.gewicht = gewicht;
    }

    public String getGewicht() {
        return gewicht;
    }

    public String getUuid() {
        return String.valueOf(uuid);
    }

}
