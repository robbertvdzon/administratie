package com.vdzon.administratie.model.boekingen.relaties;

public interface BoekingMetFactuur extends BoekingMetAfschrift{
    String getUuid();
    String getFactuurNummer();
}
