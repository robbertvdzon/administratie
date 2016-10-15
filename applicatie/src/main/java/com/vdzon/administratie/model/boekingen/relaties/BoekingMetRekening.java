package com.vdzon.administratie.model.boekingen.relaties;

import com.vdzon.administratie.model.Rekening;

import java.util.List;

public interface BoekingMetRekening extends BoekingMetAfschrift{
    String getUuid();
    String getRekeningNummer();
}
