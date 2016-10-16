package com.vdzon.administratie.model.boekingen;


import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VerrijkteBoeking {

    public enum BOEKINGSTYPE {UNKNOWN, BETAALDE_FACTUUR, BETAALDE_REKENING, BETALING_ZONDER_FACTUUR, INKOMSTEN_ZONDER_FACTUUR, PRIVE_BETALING};

    private Boeking boeking;
    private Factuur factuur;
    private Rekening rekening;
    private Afschrift afschrift;
    private BOEKINGSTYPE boekingsType;
    private double boekingsBedrag;
    private double afschriftBedrag;
    private double factuurBedrag;
    private double rekeningBedrag;
    private LocalDate afschriftDate;
    private LocalDate factuurDate;
    private LocalDate rekeningDate;

}
